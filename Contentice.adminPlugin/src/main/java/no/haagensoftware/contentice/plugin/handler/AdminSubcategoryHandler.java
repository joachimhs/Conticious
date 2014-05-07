package no.haagensoftware.contentice.plugin.handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import no.haagensoftware.contentice.assembler.SubCategoryAssembler;
import no.haagensoftware.contentice.data.CategoryData;
import no.haagensoftware.contentice.data.SubCategoryData;
import no.haagensoftware.contentice.data.auth.Session;
import no.haagensoftware.contentice.handler.ContenticeHandler;
import no.haagensoftware.contentice.plugin.admindata.AdminSubcategoryObject;
import no.haagensoftware.contentice.plugin.assembler.AdminSubCategoryAssembler;
import no.haagensoftware.contentice.spi.AuthenticationPlugin;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: jhsmbp
 * Date: 11/19/13
 * Time: 7:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdminSubcategoryHandler extends ContenticeHandler {
    private static final Logger logger = Logger.getLogger(AdminSubcategoryHandler.class.getName());

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        AuthenticationPlugin authenticationPlugin = getAuthenticationPlugin();

        if (authenticationPlugin == null) {
            sendError(channelHandlerContext, HttpResponseStatus.UNAUTHORIZED);
        } else {
            String cookieUuidToken = getCookieValue(fullHttpRequest, "uuidAdminToken");
            Session session = authenticationPlugin.getSession(cookieUuidToken);

            String subcategory = getParameter("subcategory");

            if (session != null && ("admin".equals(session.getUser().getRole()) || "super".equals(session.getUser().getRole()))) {
                handleRequest(channelHandlerContext, fullHttpRequest);
            } else {
                JsonObject subcategoryObject = new JsonObject();
                subcategoryObject.addProperty("id", subcategory);

                JsonObject topLevelObject = new JsonObject();
                topLevelObject.add("subcategory", subcategoryObject);

                writeContentsToBuffer(channelHandlerContext, topLevelObject.toString(), "application/json");
            }
        }

        handleRequest(channelHandlerContext, fullHttpRequest);
    }

    private void handleRequest(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) {
        logger.info("reading SubCategoryHandler and writing contents to buffer");

        String category = getParameter("category");
        String subcategory = getParameter("subcategory");

        if (category == null && subcategory != null && subcategory.contains("_")) {
            category = subcategory.substring(0, subcategory.indexOf("_"));
            subcategory = subcategory.substring(subcategory.indexOf("_")+1, subcategory.length());
        }

        if (isPost(fullHttpRequest) || isPut((fullHttpRequest))) {

            if (category == null) {
                category = subcategory.substring(0, subcategory.indexOf("_"));
                subcategory = subcategory.substring(subcategory.indexOf("_")+1, subcategory.length());
            }

            //CategoryData categoryData = getStorage().getCategory(category);

            String messageContent = fullHttpRequest.content().toString(CharsetUtil.UTF_8);

            AdminSubcategoryObject adminSubcategory = new Gson().fromJson(messageContent, AdminSubcategoryObject.class);

            if (adminSubcategory != null && adminSubcategory.getSubcategory() != null) {
                logger.info("Subcategory: " + adminSubcategory.getSubcategory().getId());

                getStorage().setSubCategory(getDomain().getWebappName(), category, adminSubcategory.getSubcategory().getName(), adminSubcategory.getSubcategory());
            }
        }

        SubCategoryData subCategoryData = getStorage().getSubCategory(getDomain().getWebappName(), category, subcategory);
        CategoryData categoryData = getStorage().getCategory(getDomain().getWebappName(), category);

        if (subCategoryData == null) {
            write404ToBuffer(channelHandlerContext);

        } else {
            JsonObject topLevelObject = new JsonObject();
            topLevelObject.add("subcategory", AdminSubCategoryAssembler.buildAdminJsonFromSubCategoryData(subCategoryData, categoryData));

            writeContentsToBuffer(channelHandlerContext, topLevelObject.toString(), "application/json");
        }
    }
}
