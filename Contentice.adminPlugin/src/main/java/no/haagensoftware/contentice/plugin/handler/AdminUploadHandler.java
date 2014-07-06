package no.haagensoftware.contentice.plugin.handler;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.multipart.*;
import no.haagensoftware.contentice.data.CategoryData;
import no.haagensoftware.contentice.data.CategoryField;
import no.haagensoftware.contentice.data.SubCategoryData;
import no.haagensoftware.contentice.data.auth.Session;
import no.haagensoftware.contentice.handler.ContenticeHandler;
import no.haagensoftware.contentice.spi.AuthenticationPlugin;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;

/**
 * Created by jhsmbp on 06/07/14.
 */
public class AdminUploadHandler extends ContenticeHandler {
    private static final Logger logger = Logger.getLogger(AdminUploadHandler.class.getName());

    private HttpPostRequestDecoder decoder;
    private static final HttpDataFactory factory = new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        AuthenticationPlugin authenticationPlugin = getAuthenticationPlugin();

        if (authenticationPlugin == null) {
            sendError(channelHandlerContext, HttpResponseStatus.UNAUTHORIZED);
        } else {
            String cookieUuidToken = getCookieValue(fullHttpRequest, "uuidAdminToken");
            Session session = authenticationPlugin.getSession(cookieUuidToken);

            if (session != null && ("admin".equals(session.getUser().getRole()) || "super".equals(session.getUser().getRole()))) {
                String newFilename = handleRequest(channelHandlerContext, fullHttpRequest);

                JsonObject jsonReturn = new JsonObject();

                logger.info("newFilename: " + newFilename);
                if (newFilename != null) {
                    jsonReturn.addProperty("filename", newFilename);

                    if (getDomain().getCreateCategory() != null) {
                        CategoryData categoryData = getStorage().getCategory(getDomain().getWebappName(), getDomain().getCreateCategory());
                        if (categoryData == null) {
                            categoryData = new CategoryData();
                            categoryData.setId(getDomain().getCreateCategory());
                            getStorage().setCategory(getDomain().getWebappName(), categoryData.getId(), categoryData);
                        }

                        boolean categoryHasHref = false;

                        for (CategoryField cf : categoryData.getDefaultFields()) {
                            if (cf.getId().equals("href")) {
                                categoryHasHref = true;
                                break;
                            }
                        }


                        if (!categoryHasHref) {
                            CategoryField cf = new CategoryField();
                            cf.setId("href");
                            cf.setName("href");
                            cf.setType("textfield");

                            categoryData.getDefaultFields().add(cf);
                            getStorage().setCategory(getDomain().getWebappName(), categoryData.getId(), categoryData);
                        }

                        SubCategoryData sc = new SubCategoryData();
                        sc.setId(newFilename);
                        sc.getKeyMap().put("href", new JsonPrimitive(getDomain().getUploadPath() + "/" + newFilename));

                        getStorage().setSubCategory(getDomain().getWebappName(), getDomain().getCreateCategory(), sc.getId(), sc);
                    }

                }

                writeContentsToBuffer(channelHandlerContext, jsonReturn.toString(), "application/json");
            } else {
                JsonObject topLevelObject = new JsonObject();
                topLevelObject.add("subcategories", new JsonArray());

                writeContentsToBuffer(channelHandlerContext, topLevelObject.toString(), "application/json");
            }
        }


    }

    private String handleRequest(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) {
        StringBuilder responseContent = new StringBuilder();
        String newFilename = null;

        if (isPost(fullHttpRequest)) {
            try {
                decoder = new HttpPostRequestDecoder(factory, fullHttpRequest);
            } catch (HttpPostRequestDecoder.ErrorDataDecoderException e1) {
                e1.printStackTrace();
                responseContent.append(e1.getMessage());
                channelHandlerContext.channel().close();
                return null;
            }

            if (decoder != null) {
                HttpContent chunk = fullHttpRequest.duplicate();
                try {
                    decoder.offer(chunk);
                } catch (HttpPostRequestDecoder.ErrorDataDecoderException e1) {
                    e1.printStackTrace();
                    responseContent.append(e1.getMessage());
                    return null;
                }

                // example of reading chunk by chunk (minimize memory usage due to
                // Factory)
                newFilename = readHttpDataChunkByChunk(getDomain().getWebappName());

                SubCategoryData subCategoryData = new SubCategoryData();
                subCategoryData.setId(newFilename);
                subCategoryData.getKeyMap().put("filename", new JsonPrimitive(newFilename));
                getStorage().setSubCategory(getDomain().getWebappName(), "uploads", newFilename, subCategoryData);
            }
        }

        return newFilename;
    }

    /**
     * Example of reading request by chunk and getting values from chunk to chunk
     */
    private String readHttpDataChunkByChunk(String host) {
        String newFilename = null;

        try {
            while (decoder.hasNext()) {

                InterfaceHttpData data = decoder.next();
                if (data != null) {
                    try {
                        // new value
                        newFilename = writeHttpData(host, data);
                    } finally {
                        data.release();
                    }
                }
            }
        } catch (HttpPostRequestDecoder.EndOfDataDecoderException e1) {

        }

        return newFilename;
    }

    private String writeHttpData(String host, InterfaceHttpData data) {
        String newFilename = null;

        String uploadPath = getDomain().getUploadPath();

        if (uploadPath != null && data.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload) {
            FileUpload fileUpload = (FileUpload) data;
            String fileending = getFileEnding(fileUpload);

            if (fileUpload.isCompleted() && fileending != null) {
                try {
                    String uuidFile = fileUpload.getFilename();

                    String filename = System.getProperty("no.haagensoftware.contentice.webappDir") + "/" + host +  uploadPath + "/" + uuidFile;

                    fileUpload.renameTo(new File(filename));

                    if (Files.exists(FileSystems.getDefault().getPath(filename))) {
                        newFilename = uuidFile;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return newFilename;
    }

    public String getFileEnding(FileUpload fileUpload) {
        String fileEnding = null;

        if (fileUpload.getContentType().equalsIgnoreCase("image/png")) {
            fileEnding = ".png";
        }

        if (fileUpload.getContentType().equalsIgnoreCase("image/jpg")) {
            fileEnding = ".jpg";
        }

        if (fileUpload.getContentType().equalsIgnoreCase("image/jpeg")) {
            fileEnding = ".jpg";
        }

        if (fileUpload.getContentType().equalsIgnoreCase("application/zip")) {
            fileEnding = ".zip";
        }

        return fileEnding;
    }
}
