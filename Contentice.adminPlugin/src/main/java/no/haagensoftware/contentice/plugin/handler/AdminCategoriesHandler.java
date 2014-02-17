package no.haagensoftware.contentice.plugin.handler;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.CharsetUtil;
import no.haagensoftware.contentice.assembler.CategoryAssembler;
import no.haagensoftware.contentice.assembler.SubCategoryAssembler;
import no.haagensoftware.contentice.data.CategoryData;
import no.haagensoftware.contentice.data.CategoryField;
import no.haagensoftware.contentice.data.SubCategoryData;
import no.haagensoftware.contentice.data.SubcategoryField;
import no.haagensoftware.contentice.handler.ContenticeHandler;
import no.haagensoftware.contentice.plugin.admindata.AdminCategoryObject;
import no.haagensoftware.contentice.plugin.assembler.AdminCategoryAssembler;
import no.haagensoftware.contentice.plugin.assembler.AdminSubCategoryAssembler;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jhsmbp
 * Date: 11/19/13
 * Time: 5:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdminCategoriesHandler extends ContenticeHandler {
    private Logger logger = Logger.getLogger(AdminCategoriesHandler.class.getName());

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        logger.info("reading CategoriesHandler and writing contents to buffer");

        List<CategoryData> categories = getStorage().getCategories();
        logger.info("Got " + categories.size() + " categories");

        if (isPost(fullHttpRequest)) {
            String messageContent = fullHttpRequest.content().toString(CharsetUtil.UTF_8);
            AdminCategoryObject adminCategory = new Gson().fromJson(messageContent, AdminCategoryObject.class);

            if (adminCategory != null && adminCategory.getCategory() != null) {
                logger.info("Category: " + adminCategory.getCategory().getId());

                getStorage().setCategory(adminCategory.getCategory().getId(), adminCategory.getCategory());
            }
        }

        //Always return the updated categories
        JsonArray categoryArray = new JsonArray();
        JsonArray subCategoriesArray = new JsonArray();
        JsonArray defaultFieldsArray = new JsonArray();
        JsonArray subcategoryFieldArray= new JsonArray();

        for (CategoryData category : categories) {
            for (SubCategoryData subcategoryData : getStorage().getSubCategories(category.getId())) {
                category.addSubcategory(subcategoryData);
                subCategoriesArray.add(AdminSubCategoryAssembler.buildAdminJsonFromSubCategoryData(subcategoryData, category));

                for (CategoryField cf : category.getDefaultFields()) {
                    SubcategoryField subField = new SubcategoryField();
                    subField.setId(subcategoryData.getId() + "_" + cf.getName());
                    subField.setRequired(cf.getRequired());
                    subField.setType(cf.getType());
                    subField.setName(cf.getName());
                    if (subcategoryData.getKeyMap().get(cf.getName()) != null) {
                        JsonElement element = subcategoryData.getKeyMap().get(cf.getName());
                        if (element.isJsonArray()) {
                            subField.setValue(element.getAsJsonArray().toString());
                        } else {
                            subField.setValue(element.getAsString());
                        }
                    }

                    subcategoryFieldArray.add(new Gson().toJsonTree(subField));
                }
            }

            categoryArray.add(AdminCategoryAssembler.buildAdminCategoryJson(category));

            for (CategoryField field : category.getDefaultFields()) {
                defaultFieldsArray.add(new Gson().toJsonTree(field));
            }
        }

        JsonObject topLevelObject = new JsonObject();
        topLevelObject.add("categories", categoryArray);
        topLevelObject.add("subcategories", subCategoriesArray);
        topLevelObject.add("categoryFields", defaultFieldsArray);
        topLevelObject.add("subcategoryFields", subcategoryFieldArray);

        writeContentsToBuffer(channelHandlerContext, topLevelObject.toString(), "application/json; charset=UTF-8");
    }
}
