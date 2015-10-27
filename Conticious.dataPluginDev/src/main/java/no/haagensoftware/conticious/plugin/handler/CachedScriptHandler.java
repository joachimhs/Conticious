package no.haagensoftware.conticious.plugin.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import no.haagensoftware.contentice.handler.FileServerHandler;
import no.haagensoftware.conticious.scriptcache.ScriptCache;
import no.haagensoftware.conticious.scriptcache.ScriptHash;

import java.util.logging.Logger;

/**
 * Created by jhsmbp on 06/05/14.
 */
public class CachedScriptHandler extends FileServerHandler {
    private Logger logger = Logger.getLogger(CachedScriptHandler.class.getName());

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        if (!isGet(fullHttpRequest)) {
            sendError(channelHandlerContext, HttpResponseStatus.METHOD_NOT_ALLOWED);
        } else {
            String uri = fullHttpRequest.getUri();

            String path = sanitizeUri(uri);
            if (path == null) {
                sendError(channelHandlerContext, HttpResponseStatus.FORBIDDEN);
                return;
            }

            logger.info("path: " + path);

            //Substring out the root path
            if (path.startsWith("/cachedScript")) {
                path = path.substring(13);
            }


            if (path.contains("?")) {
                path = path.substring(0, path.lastIndexOf("?"));
            }

            //Substring ut the .js ending
            if (path.endsWith(".js")) {
                path = path.substring(0, path.length() - 3);
            }


            logger.info("CachedScriptHandler path: " + path);

            ScriptCache scriptCache = ScriptHash.getScriptCache(path);
            //if there is no cache at the path, return a 404.
            if (scriptCache == null) {
                sendError(channelHandlerContext, HttpResponseStatus.NOT_FOUND);
                return;
            }

            String returnContent = null;
            if (getDomain() != null && getDomain().getMinified() != null && getDomain().getMinified().booleanValue()) {
                returnContent = scriptCache.getMinifiedScriptContent();
            } else {
                returnContent = scriptCache.getScriptContent();
            }

            //Set up and send the response.
            writeContentsToBuffer(channelHandlerContext, returnContent, "application/javascript");
        }
    }
}
