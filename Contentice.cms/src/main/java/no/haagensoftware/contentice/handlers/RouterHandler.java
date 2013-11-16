package no.haagensoftware.contentice.handlers;

import static com.danisola.urlrestify.RestParserFactory.parser;
import static com.danisola.urlrestify.types.IntVar.intVar;
import static com.danisola.urlrestify.types.StrVar.strVar;

import com.danisola.urlrestify.RestParser;
import com.danisola.urlrestify.RestUrl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * Created with IntelliJ IDEA.
 * User: jhsmbp
 * Date: 11/16/13
 * Time: 2:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class RouterHandler extends ContenticeGenericHandler {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {


    }
}
