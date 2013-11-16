package no.haagensoftware.contentice.main;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import no.haagensoftware.contentice.netty.ContenticePipelineInitializer;
import no.haagensoftware.contentice.util.URLResolver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private Integer port;

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.bootstrap();
    }

    public Main() throws IOException {
        readProperties();

        port = 8085;
    }

    public void bootstrap() throws Exception {
        URLResolver urlResolver = new URLResolver();


        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.option(ChannelOption.SO_BACKLOG, 1024);
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ContenticePipelineInitializer(urlResolver));

            Channel ch = b.bind(port).sync().channel();
            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private void readProperties() throws IOException {
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
            for (Enumeration<Object> e = properties.keys(); e.hasMoreElements();) {
                Object property = (String) e.nextElement();
                logger.info("\t\t* " + property + "=" + properties.get(property));
            }
        } else {
            String message = "Could not find " + configFile.getAbsolutePath() + ". Unable to start.";
            logger.severe(message);
            throw new RuntimeException(message);
        }

        setProperties(properties);
    }

    private void setProperties(Properties properties) {
        Enumeration<Object> propEnum = properties.keys();
        while (propEnum.hasMoreElements()) {
            String property = (String) propEnum.nextElement();
            System.setProperty(property, properties.getProperty(property));
        }
    }
}
