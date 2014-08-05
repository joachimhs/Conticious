package no.haagensoftware.contentice.plugin.handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import no.haagensoftware.contentice.data.auth.User;
import no.haagensoftware.contentice.handler.ContenticeHandler;
import no.haagensoftware.contentice.spi.AuthenticationPlugin;
import org.apache.log4j.Logger;

/**
 * Created by jhsmbp on 30/04/14.
 */
public class AdminAuthHandler extends ContenticeHandler {
    private static final Logger logger = Logger.getLogger(AdminAuthHandler.class.getName());

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        String jsonResponse = "";

        String messageContent = getHttpMessageContent(fullHttpRequest);
        logger.info(messageContent);

        AuthenticationPlugin authenticationPlugin = getAuthenticationPlugin();

        if (authenticationPlugin != null && isPost(fullHttpRequest) && messageContent != null && messageContent.length() > 18) {
            User userObject = new Gson().fromJson(messageContent, User.class);

            logger.info("validating username: " + userObject.getUsername() + " and password: " + userObject.getPassword());

            String uuid = authenticationPlugin.authenticateUser(userObject.getUsername(), userObject.getPassword());

            JsonObject uuidObject = new JsonObject();
            uuidObject.addProperty("uuidAdminToken", uuid);

            jsonResponse = uuidObject.toString();
        }

        writeContentsToBuffer(channelHandlerContext, jsonResponse.toString(), "application/json");
    }
}
