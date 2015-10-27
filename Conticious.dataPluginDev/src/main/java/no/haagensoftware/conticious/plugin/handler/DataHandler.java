package no.haagensoftware.conticious.plugin.handler;

import com.google.gson.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import no.haagensoftware.contentice.assembler.DataAssembler;
import no.haagensoftware.contentice.data.CategoryData;
import no.haagensoftware.contentice.data.SubCategoryData;
import no.haagensoftware.contentice.handler.ContenticeHandler;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jhsmbp on 1/24/14.
 */
public class DataHandler extends ContenticeHandler {
    private static final Logger logger = Logger.getLogger(DataHandler.class.getName());

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {

        String jsonReturn = "";

        String category = getParameter("category");

        if (category != null && category.contains("?")) {
            category = category.substring(0, category.indexOf("?"));
        }
        String subcategory = getParameter("subcategory");

        if (category != null) {
            CategoryData categoryData = getStorage().getCategory(getDomain().getWebappName(), category);

            List<String> ids = getQueryStringIds();

            if (isGet(fullHttpRequest) && category != null && subcategory == null && ids.size() == 0 && categoryData != null && categoryData.isPublic()) {
                //Get all subcategories for category

                String categoryName = getPluginResolver().getPluralFor(category);

                List<SubCategoryData> subCategoryDataList = getStorage().getSubCategories(getDomain().getWebappName(), category);
                jsonReturn = DataAssembler.buildJsonFromSubCategoryData(categoryName, true, "", subCategoryDataList.toArray(new SubCategoryData[subCategoryDataList.size()])).toString();
            } else if (isGet(fullHttpRequest) && category != null && subcategory == null && ids.size() > 0 && categoryData != null && categoryData.isPublic()) {
                //Get subcategories with ids for category

                String appendToId = "";

                List<SubCategoryData> subCategoryDataList = new ArrayList<>();
                for (String id : ids) {
                    if (id.startsWith(category)) {
                        appendToId = category + "_";
                    }
                    SubCategoryData subCategoryData = getStorage().getSubCategory(getDomain().getWebappName(), category, id);
                    if (subCategoryData != null) {
                        subCategoryDataList.add(subCategoryData);
                    }
                }
                String categoryName = getPluginResolver().getPluralFor(category);
                jsonReturn = DataAssembler.buildJsonFromSubCategoryData(categoryName, true, appendToId, subCategoryDataList.toArray(new SubCategoryData[subCategoryDataList.size()])).toString();
            } else if (isGet(fullHttpRequest) && category != null && subcategory != null && categoryData != null && categoryData.isPublic()) {
                //get a single subcategory

                String appendToId = "";
                if (subcategory.startsWith(category)) {
                    appendToId = category + "_";
                }
                String categoryName = getPluginResolver().getSingularFor(category);

                SubCategoryData subCategoryData = getStorage().getSubCategory(getDomain().getWebappName(), category, subcategory);
                if (subCategoryData != null) {
                    jsonReturn = DataAssembler.buildJsonFromSubCategoryData(categoryName, true, appendToId, subCategoryData).toString();
                }
            } else if (isPut(fullHttpRequest) && category != null && subcategory != null && categoryData != null && categoryData.isPublic()) {
                SubCategoryData updatedSubcategory = null;

                JsonParser parser = new JsonParser();
                JsonObject topLevelObject = parser.parse(getHttpMessageContent(fullHttpRequest)).getAsJsonObject();
                for (Map.Entry<String, JsonElement> entry : topLevelObject.entrySet()) {
                    logger.info("entry:" + entry.getKey() + " value: " + entry.getValue());

                    String singleKey = getPluginResolver().getSingularFor(category);

                    if (entry.getKey().equals(singleKey)) {
                        updatedSubcategory = new SubCategoryData();
                        updatedSubcategory.setId(subcategory);

                        JsonObject valueObject = entry.getValue().getAsJsonObject();

                        for (Map.Entry<String, JsonElement> subcategoryEntry : valueObject.entrySet()) {
                            logger.info("\tsubcat entry: " + subcategoryEntry.getKey() + " value: " + subcategoryEntry.getValue());
                            if (subcategoryEntry.getKey().equals("name") && !subcategoryEntry.getValue().isJsonNull()) {
                                updatedSubcategory.setName(subcategoryEntry.getValue().getAsString());
                                updatedSubcategory.getKeyMap().put(subcategoryEntry.getKey(), subcategoryEntry.getValue());
                            } else if (subcategoryEntry.getKey().equals("content") && !subcategoryEntry.getValue().isJsonNull()) {
                                updatedSubcategory.setContent(subcategoryEntry.getValue().getAsString());
                            } else if (!subcategoryEntry.getValue().isJsonNull()) {
                                updatedSubcategory.getKeyMap().put(subcategoryEntry.getKey(), subcategoryEntry.getValue());
                            }
                        }
                    }
                }

                if (updatedSubcategory != null) {
                    String subcatName = subcategory;
                    if (subcatName.startsWith(category + "_")) {
                        subcatName = subcatName.substring(category.length()+1);
                    }
                    getStorage().setSubCategory(getDomain().getWebappName(), category, subcatName, updatedSubcategory);
                }
            } else if (isPost(fullHttpRequest) && category != null) {
                //{"photo":{"id":"test.jpg","href":null,"name":null,"description":null}}

                SubCategoryData newSubcategory = null;

                JsonParser parser = new JsonParser();
                JsonObject topLevelObject = parser.parse(getHttpMessageContent(fullHttpRequest)).getAsJsonObject();
                for (Map.Entry<String, JsonElement> entry : topLevelObject.entrySet()) {
                    logger.info("entry:" + entry.getKey() + " value: " + entry.getValue());

                    String singleKey = getPluginResolver().getSingularFor(category);

                    if (entry.getKey().equals(singleKey)) {
                        newSubcategory = new SubCategoryData();
                        JsonObject valueObject = entry.getValue().getAsJsonObject();

                        for (Map.Entry<String, JsonElement> subcategoryEntry : valueObject.entrySet()) {
                            logger.info("\tsubcat entry: " + subcategoryEntry.getKey() + " value: " + subcategoryEntry.getValue());
                            if (subcategoryEntry.getKey().equals("name") && !subcategoryEntry.getValue().isJsonNull()) {
                                newSubcategory.setName(subcategoryEntry.getValue().getAsString());
                                newSubcategory.getKeyMap().put(subcategoryEntry.getKey(), subcategoryEntry.getValue());
                            } else if (subcategoryEntry.getKey().equals("content") && !subcategoryEntry.getValue().isJsonNull()) {
                                newSubcategory.setContent(subcategoryEntry.getValue().getAsString());
                            } else if (subcategoryEntry.getKey().equals("id")  && !subcategoryEntry.getValue().isJsonNull()) {
                                String newId = subcategoryEntry.getValue().getAsString();

                                if (newId.startsWith(category + "_")) {
                                    newId = newId.substring(category.length()+1);
                                }
                                newSubcategory.setId(newId);
                            } else if (!subcategoryEntry.getValue().isJsonNull()) {
                                newSubcategory.getKeyMap().put(subcategoryEntry.getKey(), subcategoryEntry.getValue());
                            }
                        }
                    }
                }

                if (newSubcategory != null && newSubcategory.getId() != null) {
                    getStorage().setSubCategory(getDomain().getWebappName(), category, newSubcategory.getId(), newSubcategory);
                }
            }
        }

        writeContentsToBuffer(channelHandlerContext, jsonReturn, "application/json");
    }
}
