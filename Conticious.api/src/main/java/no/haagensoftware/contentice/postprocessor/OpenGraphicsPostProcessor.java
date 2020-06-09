package no.haagensoftware.contentice.postprocessor;

import no.haagensoftware.contentice.data.SubCategoryData;
import no.haagensoftware.contentice.spi.PostProcessorPlugin;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created by jhsmbp on 10/03/16.
 */
public class OpenGraphicsPostProcessor extends PostProcessorPlugin {

    @Override
    public String getPluginName() {
        return "ConticiousOpenGraphicsProcessor";
    }

    @Override
    public String postProcess(String input, String originalUrl, String filePath, String queryString, String contentType) {
        String output = input;
        if (contentType.contains("text/html") && filePath.endsWith(".html")) {
            SubCategoryData ogData = getStorage().getSubCategory(getDomain().getDocumentsName(), "openGraphics", originalUrl.replace("/", "_"));

            if (ogData != null) {
                Document htmlDocument = Jsoup.parse(input, "UTF-8");
                Element headElement = htmlDocument.head();

                for (Element metaElement :headElement.getElementsByTag("meta")) {
                    if (metaElement.attr("property") != null &&
                            (metaElement.attr("property").startsWith("og:") || metaElement.attr("property").startsWith("twitter:"))) {
                        metaElement.remove();
                    }
                }

                if (ogData.getValueForKey("siteName") != null) {
                    headElement.appendElement("meta").attr("property", "og:name").attr("content", ogData.getValueForKey("siteName"));
                    headElement.appendElement("meta").attr("property", "og:site_name").attr("content", ogData.getValueForKey("siteName"));
                }

                if (ogData.getValueForKey("image") != null) {
                    headElement.appendElement("meta").attr("property", "og:image").attr("content", ogData.getValueForKey("image"));
                    headElement.appendElement("meta").attr("property", "og:image:url").attr("content", ogData.getValueForKey("image"));
                    headElement.appendElement("meta").attr("property", "twitter:image").attr("content", ogData.getValueForKey("image"));
                    headElement.appendElement("meta").attr("property", "twitter:card").attr("content", "summary_large_image");
                }

                if (ogData.getValueForKey("description") != null) {
                    headElement.appendElement("meta").attr("property", "og:description").attr("content", ogData.getValueForKey("description"));
                    headElement.appendElement("meta").attr("property", "twitter:description").attr("content", ogData.getValueForKey("description"));
                }

                if (ogData.getValueForKey("title") != null) {
                    headElement.appendElement("meta").attr("property", "og:title").attr("content", ogData.getValueForKey("title"));
                    headElement.appendElement("meta").attr("property", "twitter:title").attr("content", ogData.getValueForKey("title"));
                }

                if (ogData.getValueForKey("width") != null) {
                    headElement.appendElement("meta").attr("property", "og:image:width").attr("content", ogData.getValueForKey("width"));
                }

                if (ogData.getValueForKey("height") != null) {
                    headElement.appendElement("meta").attr("property", "og:image:width").attr("content", ogData.getValueForKey("height"));
                }

                if (ogData.getValueForKey("twitterHandle") != null) {
                    headElement.appendElement("meta").attr("property", "twitter:site").attr("content", "@" + ogData.getValueForKey("twitterHandle"));
                    headElement.appendElement("meta").attr("property", "twitter:creator").attr("content", "@" + ogData.getValueForKey("twitterHandle"));
                }

                headElement.appendElement("meta").attr("property", "og:type").attr("content", "website");

                output = htmlDocument.toString();
            }
        }

        return output;
    }
}
