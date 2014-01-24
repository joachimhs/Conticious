package no.kodegenet.handlers;

import com.google.gson.JsonElement;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import no.haagensoftware.contentice.data.SubCategoryData;
import no.haagensoftware.contentice.handler.ContenticeHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.List;

/**
 * Created by jhsmbp on 1/19/14.
 */
public class RevealHandler extends ContenticeHandler {
    String rootPath = null;

    public RevealHandler() {
        if (System.getProperty("no.haagensoftware.contentice.webappDir") != null && System.getProperty("no.haagensoftware.contentice.webappDir").length() > 3) {
            rootPath = System.getProperty("no.haagensoftware.contentice.webappDir");
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        String htmlReturn = "";

        List<String> slideIds = getQueryStringIds();
        String slides = null;

        if (slideIds != null && slideIds.size() > 0) {
            String slideID = slideIds.get(0);
            SubCategoryData chapter = getStorage().getSubCategory("chapters", slideID);
            if (chapter != null && chapter.getKeyMap().get("slides") != null) {
                slides = chapter.getKeyMap().get("slides").getAsString();
            }
        }

        htmlReturn = getFileContents(rootPath + "/reveal.html");

        if (htmlReturn != null && slides != null) {
            htmlReturn = htmlReturn.replace("{{Reveal-slides}}", slides);
        }

        writeContentsToBuffer(channelHandlerContext, htmlReturn, "text/html; charset=UTF-8");
    }

    private String getFileContents(String path) throws IOException {
        String returnString = null;
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            BufferedReader fileBufferedReader = null;
            try {
                fileBufferedReader = Files.newBufferedReader((FileSystems.getDefault().getPath(path)), Charset.forName("utf-8"));

                StringBuffer sb = new StringBuffer();
                String line = null;
                while ((line = fileBufferedReader.readLine()) != null) {
                    sb.append(line).append("\n");
                }

                if (sb.length() > 0) {
                    returnString = sb.toString();
                }
            } finally {
                if (fileBufferedReader != null) {
                    fileBufferedReader.close();
                }
            }
        }

        return returnString;
    }
}
