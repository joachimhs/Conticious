package no.haagensoftware.contentice.main;

import com.google.gson.Gson;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import no.haagensoftware.contentice.data.ConticiousOptions;
import no.haagensoftware.contentice.data.Settings;
import no.haagensoftware.contentice.netty.ContenticePipelineInitializer;
import no.haagensoftware.contentice.plugin.AuthenticationPluginService;
import no.haagensoftware.contentice.plugin.RouterPluginService;
import no.haagensoftware.contentice.plugin.StoragePluginService;
import no.haagensoftware.contentice.spi.ConticiousPlugin;
import no.haagensoftware.contentice.spi.RouterPlugin;
import no.haagensoftware.contentice.spi.StoragePlugin;
import no.haagensoftware.contentice.util.JsonUtil;
import no.haagensoftware.contentice.util.PluginResolver;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private Integer port;
    private ServerBootstrap bootstrap;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.bootstrap();
    }

    public Main() throws IOException {
        if (System.getProperty("log4j.configuration") == null) {
            configureLog4J();
        }

        readProperties();

        String options = JsonUtil.getFileContents(System.getProperty("no.haagensoftware.contentice.storage.file.documentsDirectory") + File.separatorChar + "admin" + File.separatorChar + "conticiousOptions.json");
        if (options != null) {
            ConticiousOptions conticiousOptions = new Gson().fromJson(options, ConticiousOptions.class);
            Settings.getInstance().setConticiousOptions(conticiousOptions);
        }

        String pluginsDir = System.getProperty("no.haagensoftware.contentice.pluginDirectory");
        loadPluginsFromDir(pluginsDir);

        //The specified storage plugin will be added as a dependency to all plugins
        StoragePlugin specifiedStoragePlugin = null;

        Map<String, ConticiousPlugin> loadedPlugins = new HashMap<>();

        for (ConticiousPlugin plugin : AuthenticationPluginService.getInstance().getLoadedPlugins()) {
            loadedPlugins.put(plugin.getPluginName(), plugin);
        }

        for (ConticiousPlugin plugin : RouterPluginService.getInstance().getLoadedPlugins()) {
            loadedPlugins.put(plugin.getPluginName(), plugin);
        }

        for (ConticiousPlugin plugin : StoragePluginService.getInstance().getLoadedPlugins()) {
            loadedPlugins.put(plugin.getPluginName(), plugin);

            if (plugin.getPluginName().equals(System.getProperty("no.haagensoftware.contentice.storage.plugin"))) {
                specifiedStoragePlugin = (StoragePlugin)plugin;
                specifiedStoragePlugin.setup();

            }

        }

        for (ConticiousPlugin plugin : loadedPlugins.values()) {
            //add storage plugin to all other non-storage plugins
            if (!(plugin instanceof StoragePlugin)) {
                plugin.addPlugin(specifiedStoragePlugin);
            }

            for (String dependantPlugin : plugin.getPluginDependencies()) {
                if (loadedPlugins.get(dependantPlugin) != null) {
                    plugin.addPlugin(loadedPlugins.get(dependantPlugin));
                }
            }
        }
    }

    private static void loadPluginsFromDir(String dir) {
        if (dir != null && new File(dir).isDirectory()) {

            try {
                ClassPathUtil.addDirToClasspath(new File(dir));
            } catch (IOException e) {
                e.printStackTrace();
            }
            //PluginClassLoader classLoader = new PluginClassLoader(ClassLoader.getSystemClassLoader());
            //classLoader.addJarsFromDirectory(dir);
        }
    }

    public void bootstrap() throws Exception {
        PluginResolver pluginResolver = new PluginResolver();

        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        try {
            bootstrap = new ServerBootstrap();
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ContenticePipelineInitializer(pluginResolver));

            logger.info("Binding to port: " + port);

            Channel ch = bootstrap.bind(port).sync().channel();
            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public void shutdown() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        bootstrap.childGroup().shutdownGracefully();

        bossGroup = null;
        workerGroup = null;
        bootstrap = null;
    }

    private void readProperties() throws IOException {
        String startWithoutConfigfile = System.getProperty("no.conticious.startWithoutConfigProperties");

        if (startWithoutConfigfile != null && startWithoutConfigfile.equalsIgnoreCase("true")) {
            //Do nothing. This is only useful if you are bootstrapping manually, like with the ContriciousLocal project.
        } else {

            Properties properties = new Properties();
            File configFile = new File("config.properties");
            if (!configFile.exists()) {
                logger.info("config.properties not found at : " + configFile.getAbsolutePath() + " trying one level up.");
                configFile = new File("../config.properties");
            }
            if (!configFile.exists()) {
                logger.info("config.properties not found at : " + configFile.getAbsolutePath() + " trying one level up.");
                configFile = new File("../../config.properties");
            }
            if (configFile.exists()) {
                FileInputStream configStream = new FileInputStream(configFile);
                properties.load(configStream);
                configStream.close();
                logger.info("Server properties loaded from " + configFile.getAbsolutePath());
                for (Enumeration<Object> e = properties.keys(); e.hasMoreElements(); ) {
                    Object property = (String) e.nextElement();
                    logger.info("\t\t* " + property + "=" + properties.get(property));
                }
            } else {
                String message = "Could not find " + configFile.getAbsolutePath() + ". Unable to start.";
                logger.error(message);
                throw new RuntimeException(message);
            }

            setProperties(properties);
        }

        port = Integer.parseInt(System.getProperty("no.haagensoftware.contentice.port", "8080"));
    }

    private void setProperties(Properties properties) {
        if (properties.get("no.haagensoftware.contentice.port") == null) {
            throw new RuntimeException("HTTP Port missing from configuration. Please ensure that no.haagensoftware.contentice.port is defined in config.properties");
        }

        if (properties.get("no.haagensoftware.contentice.storage.file.documentsDirectory") == null) {
            throw new RuntimeException("Documents directory is missing from configuration. Please ensure that no.haagensoftware.contentice.storage.file.documentsDirectory is defined in config.properties");
        }

        if (properties.get("no.haagensoftware.contentice.pluginDirectory") == null) {
//            throw new RuntimeException("Plugin directory is missing from configuration. Please ensure that no.haagensoftware.contentice.pluginDirectory is defined in config.properties");
        }

        if (properties.get("no.haagensoftware.contentice.storage.plugin") == null) {
            properties.setProperty("no.haagensoftware.contentice.storage.plugin", "FileSystemStoragePlugin");
            logger.warn("Storage Plugin is missing from configuration. Using default 'FileSystemStoragePlugin' plugin");
        }

        Enumeration<Object> propEnum = properties.keys();
        while (propEnum.hasMoreElements()) {
            String property = (String) propEnum.nextElement();
            System.setProperty(property, properties.getProperty(property));
        }
    }

    private void configureLog4J() throws IOException {
        Logger root = Logger.getRootLogger();
        if (!root.getAllAppenders().hasMoreElements()) {
            //Log4J is not configured. Set it up correctly!
            root.setLevel(Level.INFO);
            root.addAppender(new ConsoleAppender(new PatternLayout("%d %-5p [%t] %C{1}: %m%n")));
        }
    }
}
