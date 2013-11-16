package no.haagensoftware.contentice.util;

import no.haagensoftware.contentice.data.URLData;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: joahaage
 * Date: 16.11.13
 * Time: 16:17
 * To change this template use File | Settings | File Templates.
 */
public class URLResolver {
    private Map<String, String> urlMap;

    public URLResolver() {
        urlMap = new HashMap<>();
        urlMap.put("/categories", "Categories");
        urlMap.put("/categories/{category}", "Category");
        urlMap.put("/categories/{category}/subcategories", "Subcategories");
        urlMap.put("/categories/{category}/subcategories/{subcategory}", "Subcategory");
    }

    public URLData getValueForUrl(String url) {
        URLData urlData = null;

        //Try Excact match first
        String value = getValueForExactMatch(url);

        if (value != null) {
            urlData = new URLData(url, url, new HashMap<String, String>());
        } else {
            urlData = getValueForUrlWithParameters(url);
        }

        return urlData;
    }

    public String getValueForExactMatch(String url) {
        String value = null;

        for (String currUrl : urlMap.keySet()) {
            if (currUrl.equals(url)) {
                value = urlMap.get(currUrl);
                break;
            }
        }

        return value;
    }

    public URLData getValueForUrlWithParameters(String actualUrl) {
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
                    urlData = new URLData(urlPattern, actualUrl, propertyMap);

                    break;
                }
            }
        }

        return urlData;
    }

}
