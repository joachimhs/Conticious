package no.haagensoftware.contentice.handlers;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import no.haagensoftware.contentice.handler.ContenticeHandler;
import no.haagensoftware.contentice.handler.ContenticeParameterMap;
import no.haagensoftware.contentice.plugin.StoragePluginService;
import no.haagensoftware.contentice.spi.StoragePlugin;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: jhsmbp
 * Date: 11/16/13
 * Time: 1:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContenticeGenericHandler extends ContenticeHandler {
    private Logger logger = Logger.getLogger(ContenticeGenericHandler.class.getName());

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getUri(FullHttpRequest fullHttpRequest) {
        return fullHttpRequest.getUri();
    }

    public StoragePlugin getStorage() {
        return StoragePluginService.getInstance().getStoragePluginWithName("FileSystemStoragePlugin");
    }

    public String getCookieValue(FullHttpRequest fullHttpRequest, String cookieName) {
        String cookieValue = null;

        HttpHeaders httpHeaders = fullHttpRequest.headers();
        String value = httpHeaders.get("Cookie");
        logger.info("cookie header: \n" + value);
        if (value != null) {

            Set<Cookie> cookies = CookieDecoder.decode(value);
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    cookieValue = cookie.getValue();
                    break;
                }
            }
        }

        return cookieValue;
    }

    public boolean isPut(FullHttpRequest fullHttpRequest) {
        HttpMethod method = fullHttpRequest.getMethod();
        return method == HttpMethod.PUT;
    }

    public boolean isPost(FullHttpRequest fullHttpRequest) {
        HttpMethod method = fullHttpRequest.getMethod();
        return method == HttpMethod.POST;
    }

    public boolean isGet(FullHttpRequest fullHttpRequest) {
        HttpMethod method = fullHttpRequest.getMethod();
        return method == HttpMethod.GET;
    }

    public boolean isDelete(FullHttpRequest fullHttpRequest) {
        HttpMethod method = fullHttpRequest.getMethod();
        return method == HttpMethod.DELETE;
    }

    public String getHttpMessageContent(FullHttpRequest fullHttpRequest) {
        String requestContent = null;
        ByteBuf content = fullHttpRequest.content();
        if (content.isReadable()) {
            requestContent = content.toString(CharsetUtil.UTF_8);
        }
        return requestContent;
    }

    public void handleIncomingRequest(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {

    }

    public void writeContentsToBuffer(ChannelHandlerContext ctx, String responseText, String contentType) {
        HttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.copiedBuffer(responseText, CharsetUtil.UTF_8));
        response.headers().set(CONTENT_TYPE, contentType + "; charset=UTF-8");

        ctx.write(response);
        ctx.flush();
    }

    public void write404ToBuffer(ChannelHandlerContext ctx) {
        HttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND, Unpooled.copiedBuffer("404 Not Found", CharsetUtil.UTF_8));
        ctx.write(response);
        ctx.flush();
    }

    protected void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        HttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status, Unpooled.copiedBuffer("Failure: " + status.toString(), CharsetUtil.UTF_8));
        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");

        // Close the connection as soon as the error message is sent.
        ctx.write(response);
        ctx.flush();
    }

}
