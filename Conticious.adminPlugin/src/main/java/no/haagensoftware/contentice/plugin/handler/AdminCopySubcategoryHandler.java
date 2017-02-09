package no.haagensoftware.contentice.plugin.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import no.haagensoftware.contentice.data.SubCategoryData;
import no.haagensoftware.contentice.data.auth.Session;
import no.haagensoftware.contentice.handler.ContenticeHandler;
import no.haagensoftware.contentice.spi.AuthenticationPlugin;
import org.apache.log4j.Logger;

/**
 * Created by jhsmbp on 14/11/14.
 */
public class AdminCopySubcategoryHandler extends ContenticeHandler {
    private Logger logger = Logger.getLogger(AdminCopySubcategoryHandler.class.getName());

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        AuthenticationPlugin authenticationPlugin = getAuthenticationPlugin();

        if (authenticationPlugin == null) {
            sendError(channelHandlerContext, HttpResponseStatus.UNAUTHORIZED);
        } else {
            String cookieUuidToken = getCookieValue(fullHttpRequest, "uuidAdminToken");
            Session session = authenticationPlugin.getSession(cookieUuidToken);

            if (session != null && ("admin".equals(session.getUser().getRole()) || "super".equals(session.getUser().getRole()))) {
                handleRequest(channelHandlerContext, fullHttpRequest);
            } else {

                JsonObject topLevelObject = new JsonObject();

                writeContentsToBuffer(channelHandlerContext, topLevelObject.toString(), "application/json");
            }
        }
    }


    private void handleRequest(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) {
        logger.info("renaming subcategory and writing to buffer");

        JsonObject topLevelObject = new JsonObject();

        String renameSubcategoryFrom = getParameter("oldSubcategory");
        String renameSubcategoryTo = getParameter("newSubcategory");

        if (isPost(fullHttpRequest) && renameSubcategoryFrom != null && renameSubcategoryTo != null) {
            String messageContent = fullHttpRequest.content().toString(CharsetUtil.UTF_8);

            String fromCategory = null;
            String fromSubcategory = null;
            String toCategory = null;
            String toSubcategory = null;

            String[] idParts = renameSubcategoryFrom.split("_");
            if (idParts.length >= 2) {
                fromCategory = idParts[0];
                fromSubcategory = renameSubcategoryFrom.substring(renameSubcategoryFrom.indexOf("_")+1);
            }

            idParts = renameSubcategoryTo.split("_");
            if (idParts.length >= 2) {
                toCategory = idParts[0];
                toSubcategory = renameSubcategoryTo.substring(renameSubcategoryFrom.indexOf("_")+1);
            }

            logger.info("copy subcat from: " + fromCategory + " // " + fromSubcategory);
            logger.info("copy subcat to: " + toCategory + " // " + toSubcategory);

            if (fromCategory != null && fromSubcategory != null && toCategory != null && toSubcategory != null) {
                SubCategoryData fromSubcatData = getStorage().getSubCategory(getDomain().getWebappName(), fromCategory, fromSubcategory);
                fromSubcatData.getId();

                SubCategoryData toSubcatData = new SubCategoryData(renameSubcategoryTo);
                toSubcatData.setName(toSubcategory);
                toSubcatData.setContent(fromSubcatData.getContent());

                for (String key : fromSubcatData.getKeyMap().keySet()) {
                    JsonElement jsonElement = fromSubcatData.getKeyMap().get(key);
                    toSubcatData.getKeyMap().put(key, jsonElement);
                }

                getStorage().setSubCategory(getDomain().getWebappName(), toCategory, toSubcategory, toSubcatData);
                logger.info("DONE");
            }

            //getStorage().getSubCategory(getDomain().getWebappName(), "", "");

            writeContentsToBuffer(channelHandlerContext, topLevelObject.toString(), "application/json");
        }
    }
}
