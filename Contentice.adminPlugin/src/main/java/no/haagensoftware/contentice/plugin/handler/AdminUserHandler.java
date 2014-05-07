package no.haagensoftware.contentice.plugin.handler;

import com.google.gson.JsonObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import no.haagensoftware.contentice.data.auth.Session;
import no.haagensoftware.contentice.handler.ContenticeHandler;
import no.haagensoftware.contentice.spi.AuthenticationPlugin;

/**
 * Created by jhsmbp on 30/04/14.
 */
public class AdminUserHandler extends ContenticeHandler {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        String jsonResponse = "";

        AuthenticationPlugin authenticationPlugin = getAuthenticationPlugin();

        String user = getParameter("user");
        Session session = getAuthenticationPlugin().getSession(user);

        if (authenticationPlugin != null && session != null && isGet(fullHttpRequest)) {
            JsonObject userObject = new JsonObject();
            userObject.addProperty("id", session.getUuid());
            userObject.addProperty("username", session.getUser().getUsername());
            userObject.addProperty("role", session.getUser().getRole());

            JsonObject toplevelObject = new JsonObject();
            toplevelObject.add("user", userObject);

            jsonResponse = toplevelObject.toString();

        }

        /*boolean authenticated = uuid != null && uuid.length() > 10;

        JsonObject userJson = new JsonObject();
        if (authenticated) {
            userJson.addProperty("role", "admin");
        } else {
            userJson.addProperty("role", "none");
        }

        userJson.addProperty("authenticated", authenticated);
        userJson.addProperty("username", userObject.getUsername());
        userJson.addProperty("id", uuid);


        JsonObject toplevelObject = new JsonObject();
        toplevelObject.add("user", userJson);

        jsonResponse = toplevelObject.toString();*/

        writeContentsToBuffer(channelHandlerContext, jsonResponse.toString(), "application/json");
    }
}
