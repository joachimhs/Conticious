package no.teknologihuset.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import no.haagensoftware.contentice.handler.ContenticeHandler;

/**
 * Created by jhsmbp on 1/21/14.
 */
public class BookingInquiriesHandler extends ContenticeHandler {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {

        writeContentsToBuffer(channelHandlerContext, "{\"bookingInquiry\": {}}", "application/json; charset=UTF-8");
    }
}
