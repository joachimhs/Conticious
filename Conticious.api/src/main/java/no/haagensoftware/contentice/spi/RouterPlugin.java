package no.haagensoftware.contentice.spi;

import io.netty.channel.ChannelHandler;
import no.haagensoftware.contentice.data.URLData;
import no.haagensoftware.contentice.handler.ContenticeHandler;
import no.haagensoftware.contentice.handler.FileServerHandler;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: jhsmbp
 * Date: 11/16/13
 * Time: 10:42 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class RouterPlugin implements ConticiousPlugin{
    private static final Logger logger = Logger.getLogger(RouterPlugin.class.getName());

    public List<ConticiousPlugin> dependantPluginsList = new ArrayList<>();

    @Override
    public void addPlugin(ConticiousPlugin plugin) {
        dependantPluginsList.add(plugin);
    }

    public abstract LinkedHashMap<String, Class<? extends ChannelHandler>> getRoutes();

    public ChannelHandler getHandlerForUrl(String url) {
        URLData urlData = getUrlDataForUrl(url);
        ChannelHandler channelHandler = null;


        if (urlData != null) {
            Class<? extends ChannelHandler> channelHandlerClass = getRoutes().get(urlData.getUrlPattern());

            if (channelHandlerClass != null) {
                try {
                    channelHandler = channelHandlerClass.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            if (channelHandler != null && channelHandler instanceof FileServerHandler) {

            } else if (channelHandler != null && channelHandler instanceof ContenticeHandler) {
                ((ContenticeHandler)channelHandler).setParameterMap(urlData.getParameters());
                ((ContenticeHandler)channelHandler).setQueryStringIds(urlData.getQueryStringIds());

                for (ConticiousPlugin plugin : getDependantPluginsList()) {
                    if (plugin instanceof StoragePlugin && plugin.getPluginName().equals(System.getProperty("no.haagensoftware.contentice.storage.plugin"))) {
                        logger.info("Adding storage plugin: " + plugin.getPluginName() + " to Handler: " + channelHandler.getClass().getName());
                        ((ContenticeHandler)channelHandler).setStorage((StoragePlugin) plugin);
                    }

                    if (plugin instanceof AuthenticationPlugin) {
                        logger.info("Adding authentication plugin: " + plugin.getPluginName() + " to Handler: " + channelHandler.getClass().getName());
                        ((ContenticeHandler)channelHandler).setAuthenticationPlugin((AuthenticationPlugin)plugin);
                    }
                }
            }
        }

        return channelHandler;
    }

    public URLData getUrlDataForUrl(String url) {
        URLData urlData = null;

        //Get all route keys for this plugin
        for (String urlPattern : getRoutes().keySet()) {
            String urlToFind = url;

            if (urlToFind.contains("?")) {
                urlToFind = url.substring(0, url.indexOf("?"));
            }

            boolean urlMatch = false;
            Map<String, String> propertyMap = new HashMap<>();

            if (urlPattern.startsWith("classpath:") && url.startsWith(urlPattern.substring(10))) {
                urlMatch = true;
            } else if (urlPattern.startsWith("startsWith:") && url.startsWith(urlPattern.substring(11))) {
                urlMatch = true;
            } else if (urlPattern.equals(urlToFind)) {
                //Excact match
                urlMatch = true;
            } else if (urlPattern.contains("{") && urlPattern.contains("}")) {
                urlMatch = true;
                //If route contains dynamic parts
                String[] urlParts = url.split("/");       //The parts of the actual URL,split by /
                String[] currUrlParts = urlPattern.split("/");  //The parts of the URL pattern, split by /

                //A match needs the same amount of parts
                if (urlParts.length == currUrlParts.length) {
                    URLData tempUrlData = null;
                    for (int i = 0; i < currUrlParts.length; i++)  {
                        //If current part is a parameter/dynamic part, extract it an add it to the parameter map
                        if (currUrlParts[i].startsWith("{") && currUrlParts[i].endsWith("}")) { //this is a parameter, not part of the URL match
                            propertyMap.put(currUrlParts[i].substring(1, currUrlParts[i].length()-1), urlParts[i]);
                        } else if (!currUrlParts[i].equals(urlParts[i])) {
                            //If the current non parameter/dynamic part does not match, the URL do not match
                            urlMatch = false;
                        }
                    }
                } else {
                    urlMatch = false;
                }
            }

            //If Url match, build up a URL data object
            if (urlMatch) {
                urlData = new URLData(urlPattern, url, propertyMap, this);

                break;
            }
        }

        List queryIds = buildIdsFromQueryStringString(url);
        if (urlData != null) {
            urlData.setQueryStringIds(queryIds);
        }

        return urlData;
    }

    protected List<String> buildIdsFromQueryStringString(String url) {
        //?ids[]=
        List<String> ids = new ArrayList<>();

        if (url.contains("?")) {
            String queryString = url.substring(url.indexOf("?") + 1, url.length());

            queryString = queryString.replaceAll("%5B", "");
            queryString = queryString.replaceAll("%5D", "");
            queryString = queryString.replaceAll("%40", "@");

            for (String query : queryString.split("&")) {
                if (query.startsWith("ids=")) {
                    ids.add(query.substring(4, query.length()));
                }
            }
        }

        return ids;
    }

    public abstract Map<String, String> getPlurals();

    public List<ConticiousPlugin> getDependantPluginsList() {
        return dependantPluginsList;
    }
}
