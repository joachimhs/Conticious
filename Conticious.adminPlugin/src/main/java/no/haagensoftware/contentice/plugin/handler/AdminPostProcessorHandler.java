package no.haagensoftware.contentice.plugin.handler;

import com.google.gson.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import no.haagensoftware.contentice.data.ConticiousOptions;
import no.haagensoftware.contentice.data.Domain;
import no.haagensoftware.contentice.data.Settings;
import no.haagensoftware.contentice.data.auth.Session;
import no.haagensoftware.contentice.handler.ContenticeHandler;
import no.haagensoftware.contentice.plugin.admindata.AdminPostProcessor;
import no.haagensoftware.contentice.spi.AuthenticationPlugin;
import no.haagensoftware.contentice.spi.PostProcessorPlugin;
import no.haagensoftware.contentice.util.JsonUtil;
import no.haagensoftware.hyrrokkin.serializer.RestSerializer;
import org.apache.log4j.Logger;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhsmbp on 19/04/14.
 */
public class AdminPostProcessorHandler extends ContenticeHandler {
    private static final Logger logger = Logger.getLogger(AdminPostProcessorHandler.class.getName());
    private RestSerializer serializer;

    public AdminPostProcessorHandler() {
        serializer = new RestSerializer();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        AuthenticationPlugin authenticationPlugin = getAuthenticationPlugin();

        if (authenticationPlugin == null) {
            sendError(channelHandlerContext, HttpResponseStatus.UNAUTHORIZED);
        } else {
            String cookieUuidToken = getCookieValue(fullHttpRequest, "uuidAdminToken");
            Session session = authenticationPlugin.getSession(cookieUuidToken);

            if (session != null && "super".equals(session.getUser().getRole())) {
                handleSuperRequest(channelHandlerContext, fullHttpRequest);
            } else {
                writeContentsToBuffer(channelHandlerContext, serializer.serialize(new ArrayList<AdminPostProcessor>()).toString(), "application/json");
            }
        }

        sendError(channelHandlerContext, HttpResponseStatus.UNAUTHORIZED);
    }

    private void handleSuperRequest(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) {
        String jsonResponse = "";

        if (isGet(fullHttpRequest)) {
            List<AdminPostProcessor> adminPostProcessors = new ArrayList<>();

            for (PostProcessorPlugin ppp : getPostProcessorPlugins()) {
                  adminPostProcessors.add(new AdminPostProcessor(ppp.getPluginName(), ppp.getPluginName()));
            }

            writeContentsToBuffer(channelHandlerContext, serializer.serialize(adminPostProcessors).toString(), "application/json");
        } else {
            sendError(channelHandlerContext, HttpResponseStatus.METHOD_NOT_ALLOWED);
        }
    }
}
