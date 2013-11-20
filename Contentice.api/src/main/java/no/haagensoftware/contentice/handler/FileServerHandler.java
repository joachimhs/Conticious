package no.haagensoftware.contentice.handler;


import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import no.haagensoftware.contentice.handler.ContenticeHandler;
import no.haagensoftware.contentice.util.ContentTypeUtil;
import org.apache.log4j.Logger;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.net.*;

/**
 * A file server that can serve files from file system and class path.
 *
 * If you wish to customize the error message, please sub-class and override sendError().
 * Based on Trustin Lee's original file serving example
 */
public class FileServerHandler extends ContenticeHandler {
    private static final Logger logger = Logger.getLogger(FileServerHandler.class.getName());

    private static final String HTTP_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
    private String rootPath;
    private String stripFromUri;
    private int cacheMaxAge = -1;
    private boolean fromClasspath = false;
    private MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();

    public void setPath(String path) {
        rootPath = path;
        rootPath = rootPath.replace(File.separatorChar, '/');
    }

    protected String sanitizeUri(String uri) throws URISyntaxException {
        // Decode the path.
        try {
            uri = URLDecoder.decode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            try {
                uri = URLDecoder.decode(uri, "ISO-8859-1");
            } catch (UnsupportedEncodingException e1) {
                throw new Error();
            }
        }

        // Convert file separators.
        uri = uri.replace(File.separatorChar, '/');

        // Simplistic dumb security check.
        // You will have to do something serious in the production environment.
        if (uri.contains(File.separator + ".") ||
            uri.contains("." + File.separator) ||
            uri.startsWith(".") || uri.endsWith(".")) {
            return null;
        }

        QueryStringDecoder decoder = new QueryStringDecoder(uri);
        uri = decoder.path();

        if (uri.endsWith("/")) {
            uri += "index.html";
        }

        return uri;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        logger.info("FileServerHandler channelRead0");
        if (!fullHttpRequest.getDecoderResult().isSuccess()) {
            sendError(channelHandlerContext, HttpResponseStatus.BAD_REQUEST);
            return;
        }

        if (!isGet(fullHttpRequest)) {
            sendError(channelHandlerContext, HttpResponseStatus.METHOD_NOT_ALLOWED);
            return;
        }

        String uri = fullHttpRequest.getUri();
        if (stripFromUri != null) {
            uri = uri.replaceFirst(stripFromUri, "");
        }

        String path = sanitizeUri(uri);
        if (path == null) {
            sendError(channelHandlerContext, HttpResponseStatus.FORBIDDEN);
            return;
        }

        logger.info("path: " + path);

        URL fileUrl = this.getClass().getResource(path);
        File file = new File(fileUrl.getPath());
        if (file.isDirectory()) {
            path = path + File.separatorChar + "index.html";
        }

        InputStream in = this.getClass().getResourceAsStream(path);
        String fileContent = convertStreamToString(in);



        writeContentsToBuffer(channelHandlerContext, fileContent.toString(), ContentTypeUtil.getContentType(path));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if (ctx.channel().isActive()) {
            sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}