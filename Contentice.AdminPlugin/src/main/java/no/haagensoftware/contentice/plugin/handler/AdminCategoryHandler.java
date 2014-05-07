package no.haagensoftware.contentice.plugin.handler;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import no.haagensoftware.contentice.assembler.CategoryAssembler;
import no.haagensoftware.contentice.data.CategoryData;
import no.haagensoftware.contentice.data.CategoryField;
import no.haagensoftware.contentice.data.SubCategoryData;
import no.haagensoftware.contentice.data.SubcategoryField;
import no.haagensoftware.contentice.data.auth.Session;
import no.haagensoftware.contentice.handler.ContenticeHandler;
import no.haagensoftware.contentice.handler.ContenticeParameterMap;
import no.haagensoftware.contentice.plugin.admindata.AdminCategoryObject;
import no.haagensoftware.contentice.plugin.admindata.AdminCategoryObjectWithIds;
import no.haagensoftware.contentice.plugin.assembler.AdminCategoryAssembler;
import no.haagensoftware.contentice.plugin.assembler.AdminSubCategoryAssembler;
import no.haagensoftware.contentice.spi.AuthenticationPlugin;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: joahaage
 * Date: 17.11.13
 * Time: 12:22
 * To change this template use File | Settings | File Templates.
 */
public class AdminCategoryHandler extends ContenticeHandler  {
    private static final Logger logger = Logger.getLogger(AdminCategoriesHandler.class.getName());

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        AuthenticationPlugin authenticationPlugin = getAuthenticationPlugin();

        if (authenticationPlugin == null) {
            sendError(channelHandlerContext, HttpResponseStatus.UNAUTHORIZED);
        } else {
            String cookieUuidToken = getCookieValue(fullHttpRequest, "uuidAdminToken");
            Session session = authenticationPlugin.getSession(cookieUuidToken);

            String category = getParameter("category");

            if (session != null && "admin".equals(session.getUser().getRole())) {
                handleRequest(channelHandlerContext, fullHttpRequest);
            } else {
                JsonObject categoryObject = new JsonObject();
                categoryObject.addProperty("id", category);

                JsonObject toplevelObject = new JsonObject();
                toplevelObject.add("category", categoryObject);

                writeContentsToBuffer(channelHandlerContext, toplevelObject.toString(), "application/json");
            }
        }

    }

    private void handleRequest(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) {
        logger.info("reading CategoryHandler and writing contents to buffer");

        String category = getParameter("category");
        CategoryData categoryData = getStorage().getCategory(getDomain().getWebappName(), category);

        if (isPost(fullHttpRequest)) {
            logger.info("POSTING CATEGORY: " + category);

        } else if (isPut(fullHttpRequest) && category != null) {
            logger.info("PUTTING CATEGORY: " + category);
            String messageContent = getHttpMessageContent(fullHttpRequest);
            logger.info(messageContent);

            AdminCategoryObjectWithIds adminCategory = new Gson().fromJson(messageContent, AdminCategoryObjectWithIds.class);
            CategoryData storedCategory = getStorage().getCategory(getDomain().getWebappName(), category);

            if (adminCategory != null && adminCategory.getCategory() != null && storedCategory != null) {
                logger.info("Category: " + adminCategory.getCategory().getId());

                storedCategory.setPublic(adminCategory.getCategory().isPublic());

                getStorage().setCategory(getDomain().getWebappName(), storedCategory.getId(), storedCategory);
            }

            JsonObject topLevelObject = convertCategoryToJson(getDomain().getWebappName(), storedCategory);
            writeContentsToBuffer(channelHandlerContext, topLevelObject.toString(), "application/json");

        } else if (isGet(fullHttpRequest)) {
            if (categoryData == null) {
                write404ToBuffer(channelHandlerContext);
            } else {
                JsonObject topLevelObject = convertCategoryToJson(getDomain().getWebappName(), categoryData);
                writeContentsToBuffer(channelHandlerContext, topLevelObject.toString(), "application/json");
            }
        }
    }

    private JsonObject convertCategoryToJson(String host, CategoryData categoryData) {
        JsonObject topLevelObject = new JsonObject();

        JsonArray subCategoriesArray = new JsonArray();
        JsonArray defaultFieldsArray = new JsonArray();
        JsonArray subcategoryFieldArray= new JsonArray();

        for (SubCategoryData subcategoryData : getStorage().getSubCategories(host, categoryData.getId())) {
            categoryData.addSubcategory(subcategoryData);
            subCategoriesArray.add(AdminSubCategoryAssembler.buildAdminJsonFromSubCategoryData(subcategoryData, categoryData));

            for (CategoryField cf : categoryData.getDefaultFields()) {
                SubcategoryField subField = new SubcategoryField();
                subField.setId(categoryData.getId() + "_" + cf.getName());
                subField.setRequired(cf.getRequired());
                subField.setType(cf.getType());
                subField.setName(cf.getName());
                if (subcategoryData.getKeyMap().get(cf.getName()) != null) {
                    subField.setValue(subcategoryData.getKeyMap().get(cf.getName()).getAsString());
                }

                subcategoryFieldArray.add(new Gson().toJsonTree(subField));
            }
        }

        topLevelObject.add("category", AdminCategoryAssembler.buildAdminCategoryJson(categoryData));

        for (CategoryField field : categoryData.getDefaultFields()) {
            defaultFieldsArray.add(new Gson().toJsonTree(field));
        }

        topLevelObject.add("subcategories", subCategoriesArray);
        topLevelObject.add("categoryFields", defaultFieldsArray);
        topLevelObject.add("subcategoryFields", subcategoryFieldArray);
        return topLevelObject;
    }
}
