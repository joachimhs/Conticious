package no.haagensoftware.contentice.handlers;

import io.netty.channel.*;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.stream.ChunkedFile;
import no.haagensoftware.contentice.handler.CommonConticiousOutboundHandler;
import no.haagensoftware.contentice.handler.ContenticeHandler;
import no.haagensoftware.contentice.spi.PostProcessorPlugin;

/**
 * Created by jhsmbp on 12/09/15.
 */
public class PostProcesorHandler extends CommonConticiousOutboundHandler {
    private PostProcessorPlugin postProcessorPlugin;
    private String uri;
    private String contentType;

    public PostProcesorHandler(PostProcessorPlugin postProcessorPlugin) {
        this.postProcessorPlugin = postProcessorPlugin;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        ctx.write(msg, promise);
    }
}
