package no.haagensoftware.contentice.util;

import io.netty.channel.ChannelHandler;
import no.haagensoftware.contentice.data.URLData;
import no.haagensoftware.contentice.spi.ConticiousPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jhsmbp on 20/04/14.
 */
public class PluginResolver {
    private Map<String, ConticiousPlugin> urlMap;
    private Map<String, String> plurals;
    private Map<String, String> singulars;

    public PluginResolver() {
        urlMap = new HashMap<>();
        plurals = new HashMap<>();
        singulars = new HashMap<>();
    }

    public void addUrlPattern(String urlPattern, ConticiousPlugin plugin) {
        urlMap.put(urlPattern, plugin);
    }

    public void addPlural(String singular, String plural) {
        plurals.put(singular, plural);
        singulars.put(plural, singular);
    }

    public String getPluralFor(String singular) {
        String plural = plurals.get(singular);

        if (plural == null && singulars.get(singular) != null) {
            plural = singular;
        }

        if (plural == null) {
            plural = singular;

            if (!plural.endsWith("s")) {
                plural = plural + "s";
            }
        }

        return plural;
    }

    public String getSingularFor(String plural) {
        String singular = singulars.get(plural);

        if (singular == null) {
            singular = plural;

            if (singular.endsWith("s")) {
                singular = singular.substring(0, singular.length()-1);
            }
        }

        return singular;
    }

    public ConticiousPlugin getPluginForUrl(String url) {
        ConticiousPlugin foundPlugin = null;

        if (url.endsWith("/")) {
            url = url.substring(0, url.length()-1);
        }

        if (url.endsWith(".json")) {
            url = url.substring(0, url.length()-5);
        }

        //Try Excact match first
        String value = getKeyForExactMatch(url);
        URLData urlData = getValueForUrlWithParameters(url);

        if (value != null) {
            foundPlugin = urlMap.get(value);
        } else if (urlData != null) {
            //No excact match, try pattern
            foundPlugin = urlData.getPlugin();
        }



        return foundPlugin;
    }

    public URLData getValueForUrl(String url) {
        if (url.endsWith("/")) {
            url = url.substring(0, url.length()-1);
        }
        if (url.endsWith(".json")) {
            url = url.substring(0, url.length()-5);
        }
        URLData urlData = null;

        //Try Excact match first
        String value = getKeyForExactMatch(url);

        if (value != null) {
            urlData = new URLData(value, url, new HashMap<String, String>(), urlMap.get(value));
        } else {
            urlData = getValueForUrlWithParameters(url);
        }

        for (String urlPattern : urlMap.keySet()) {
            if (urlPattern.startsWith("classpath:") && url.startsWith(urlPattern.substring(10))) {
                urlData = new URLData(urlPattern, url, new HashMap<String, String>(), urlMap.get(urlPattern));
            }
            if (urlPattern.startsWith("startsWith:") && url.startsWith(urlPattern.substring(11))) {
                urlData = new URLData(urlPattern, url, new HashMap<String, String>(), urlMap.get(urlPattern));
            }
        }

        List queryIds = buildIdsFromQueryStringString(url);
        if (urlData != null) {
            urlData.setQueryStringIds(queryIds);
        }

        appendQueryStringParamsToPropertyMap(url, urlData);

        return urlData;
    }

    protected void appendQueryStringParamsToPropertyMap(String url, URLData urlData) {
        //propertyMap.put(currUrlParts[i].substring(1, currUrlParts[i].length()-1), urlParts[i]);

        if (url.contains("?")) {
            String queryString = url.substring(url.indexOf("?") + 1, url.length());
            for (String query : queryString.split("&")) {
                if (query.contains("=")) {
                    String[] parts = query.split("=");
                    if (parts.length == 2) {
                        urlData.addParameter(parts[0], parts[1]);
                    }
                }
            }
        }
    }

    protected List<String> buildIdsFromQueryStringString(String url) {
        //?ids[]=
        List<String> ids = new ArrayList<>();

        if (url.contains("?")) {
            String queryString = url.substring(url.indexOf("?") + 1, url.length());

            queryString = queryString.replaceAll("%5B", "");
            queryString = queryString.replaceAll("%5D", "");

            for (String query : queryString.split("&")) {
                if (query.startsWith("ids=")) {
                    ids.add(query.substring(4, query.length()));
                }
            }
        }

        return ids;
    }

    protected String getKeyForExactMatch(String url) {
        String key = null;

        if (url.contains("?")) {
            url = url.substring(0, url.indexOf("?"));
        }

        if (url.endsWith(".json")) {
            url = url.substring(0, url.length()-5);
        }

        for (String currUrl : urlMap.keySet()) {
            if (currUrl.equals(url)) {
                key= currUrl;
                break;
            }

            if (currUrl.startsWith("classpath:") && url.startsWith(currUrl.substring(10))) {
                key = currUrl;
            }
            if (currUrl.startsWith("startsWith:") && url.startsWith(currUrl.substring(11))) {
                key = currUrl;
            }
        }

        return key;
    }

    protected URLData getValueForUrlWithParameters(String actualUrl) {
        URLData urlData = null;

        for (String urlPattern : urlMap.keySet()) {
            if (urlPattern.contains("{") && urlPattern.contains("}")) {
                boolean urlMatch = true;
                Map<String, String> propertyMap = new HashMap<>();

                String[] urlParts = actualUrl.split("/");       //The parts of the actual URL,split by /
                String[] currUrlParts = urlPattern.split("/");  //The parts of the URL pattern, split by /

                if (urlParts.length == currUrlParts.length) {
                    URLData tempUrlData = null;
                    for (int i = 0; i < currUrlParts.length; i++)  {
                        if (currUrlParts[i].startsWith("{") && currUrlParts[i].endsWith("}")) { //this is a parameter, not part of the URL match
                            propertyMap.put(currUrlParts[i].substring(1, currUrlParts[i].length()-1), urlParts[i]);
                        } else if (!currUrlParts[i].equals(urlParts[i])) {
                            urlMatch = false;
                        }
                    }
                } else {
                    urlMatch = false;
                }

                if (urlMatch) {
                    urlData = new URLData(urlPattern, actualUrl, propertyMap, urlMap.get(urlPattern));

                    break;
                }
            }
        }

        return urlData;
    }
}
