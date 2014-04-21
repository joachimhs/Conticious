package no.haagensoftware.contentice.plugin.handler;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.CharsetUtil;
import no.haagensoftware.contentice.assembler.SubCategoryAssembler;
import no.haagensoftware.contentice.data.CategoryData;
import no.haagensoftware.contentice.data.CategoryField;
import no.haagensoftware.contentice.data.SubCategoryData;
import no.haagensoftware.contentice.data.SubcategoryField;
import no.haagensoftware.contentice.handler.ContenticeHandler;
import no.haagensoftware.contentice.plugin.admindata.AdminSubcategoryObject;
import no.haagensoftware.contentice.plugin.assembler.AdminSubCategoryAssembler;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jhsmbp
 * Date: 11/19/13
 * Time: 7:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdminSubcategoriesHandler extends ContenticeHandler {
    private Logger logger = Logger.getLogger(AdminSubcategoryHandler.class.getName());

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        logger.info("reading SubCategoriesHandler and writing contents to buffer");

        JsonObject topLevelObject = new JsonObject();

        String category = getParameter("category");

        if (isPost(fullHttpRequest)) {
            String messageContent = fullHttpRequest.content().toString(CharsetUtil.UTF_8);

            AdminSubcategoryObject adminSubcategory = new Gson().fromJson(messageContent, AdminSubcategoryObject.class);

            if (adminSubcategory != null && adminSubcategory.getSubcategory() != null) {
                if (category == null) {
                    category = adminSubcategory.getSubcategory().getId().substring(0, adminSubcategory.getSubcategory().getId().indexOf("_"));
                }

                logger.info("Category: " + adminSubcategory.getSubcategory().getId());

                getStorage().setSubCategory(getDomain().getWebappName(), category, adminSubcategory.getSubcategory().getName(), adminSubcategory.getSubcategory());


                CategoryData categoryData = getStorage().getCategory(getDomain().getWebappName(), category);
                topLevelObject.add("subcategory", AdminSubCategoryAssembler.buildAdminJsonFromSubCategoryData(adminSubcategory.getSubcategory(), categoryData));

                JsonArray subcategoryFieldArray= new JsonArray();
                for (CategoryField cf : categoryData.getDefaultFields()) {
                    SubcategoryField subField = new SubcategoryField();
                    subField.setId(categoryData.getId() + "_" + cf.getName());
                    subField.setRequired(cf.getRequired());
                    subField.setType(cf.getType());
                    subField.setName(cf.getName());
                    if (adminSubcategory.getSubcategory().getKeyMap().get(cf.getName()) != null) {
                        subField.setValue(adminSubcategory.getSubcategory().getKeyMap().get(cf.getName()).getAsString());
                    }

                    subcategoryFieldArray.add(new Gson().toJsonTree(subField));
                }

                topLevelObject.add("subcategoryFields", subcategoryFieldArray);

            }
        } else if (isPut(fullHttpRequest)) {

        } else {
            //Always return the updated subcategories
            List<SubCategoryData> subCategories = getStorage().getSubCategories(getDomain().getWebappName(), category);
            CategoryData categoryData = getStorage().getCategory(getDomain().getWebappName(), category);

            if (subCategories == null) {
                write404ToBuffer(channelHandlerContext);
            } else {
                JsonArray subCategoryArray = new JsonArray();

                JsonArray subcategoryFieldArray= new JsonArray();
                for (SubCategoryData subCategory : subCategories) {
                    subCategoryArray.add(AdminSubCategoryAssembler.buildAdminJsonFromSubCategoryData(subCategory, categoryData));

                    for (CategoryField cf : categoryData.getDefaultFields()) {
                        SubcategoryField subField = new SubcategoryField();
                        subField.setId(categoryData.getId() + "_" + cf.getName());
                        subField.setRequired(cf.getRequired());
                        subField.setType(cf.getType());
                        subField.setName(cf.getName());
                        if (subCategory.getKeyMap().get(cf.getName()) != null) {
                            subField.setValue(subCategory.getKeyMap().get(cf.getName()).getAsString());
                        }

                        subcategoryFieldArray.add(new Gson().toJsonTree(subField));
                    }
                }

                topLevelObject.add("subcategories", subCategoryArray);
                topLevelObject.add("subcategoryFields", subcategoryFieldArray);
            }
        }

        writeContentsToBuffer(channelHandlerContext, topLevelObject.toString(), "application/json");
    }
}
