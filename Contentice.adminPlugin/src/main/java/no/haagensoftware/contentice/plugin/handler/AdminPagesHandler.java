package no.haagensoftware.contentice.plugin.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import no.haagensoftware.contentice.handler.FileServerHandler;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: jhsmbp
 * Date: 11/19/13
 * Time: 11:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdminPagesHandler extends FileServerHandler {
    private static final Logger logger = Logger.getLogger(AdminPagesHandler.class.getName());

    public AdminPagesHandler() {
        setFromClasspath(false);

        if (System.getProperty("no.haagensoftware.contentice.adminDir") != null && System.getProperty("no.haagensoftware.contentice.adminDir").length() > 3) {
            setRootPath(System.getProperty("no.haagensoftware.contentice.adminDir"));
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        logger.info("AdminPagesHandler channelRead0");
        super.channelRead0(channelHandlerContext, fullHttpRequest);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
