package no.haagensoftware.conticious.plugin.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import no.haagensoftware.contentice.assembler.DataAssembler;
import no.haagensoftware.contentice.data.CategoryData;
import no.haagensoftware.contentice.data.SubCategoryData;
import no.haagensoftware.contentice.handler.ContenticeHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhsmbp on 1/24/14.
 */
public class DataHandler extends ContenticeHandler {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {

        String jsonReturn = "";

        String category = getParameter("category");

        if (category != null && category.contains("?")) {
            category = category.substring(0, category.indexOf("?"));
        }
        String subcategory = getParameter("subcategory");

        if (category != null) {
            CategoryData categoryData = getStorage().getCategory(getDomain().getDocumentsName(), category);

            List<String> ids = getQueryStringIds();

            if (isGet(fullHttpRequest) && category != null && subcategory == null && ids.size() == 0 && categoryData != null && categoryData.isPublic()) {
                //Get all subcategories for category

                String categoryName = getPluginResolver().getPluralFor(category);

                List<SubCategoryData> subCategoryDataList = getStorage().getSubCategories(getDomain().getDocumentsName(), category);
                jsonReturn = DataAssembler.buildJsonFromSubCategoryData(categoryName, true, "", subCategoryDataList.toArray(new SubCategoryData[subCategoryDataList.size()])).toString();
            } else if (isGet(fullHttpRequest) && category != null && subcategory == null && ids.size() > 0 && categoryData != null && categoryData.isPublic()) {
                //Get subcategories with ids for category

                String appendToId = "";

                List<SubCategoryData> subCategoryDataList = new ArrayList<>();
                for (String id : ids) {
                    if (id.startsWith(category)) {
                        appendToId = category + "_";
                    }
                    SubCategoryData subCategoryData = getStorage().getSubCategory(getDomain().getDocumentsName(), category, id);
                    if (subCategoryData != null) {
                        subCategoryDataList.add(subCategoryData);
                    }
                }
                String categoryName = getPluginResolver().getPluralFor(category);
                jsonReturn = DataAssembler.buildJsonFromSubCategoryData(categoryName, true, appendToId, subCategoryDataList.toArray(new SubCategoryData[subCategoryDataList.size()])).toString();
            } else if (isGet(fullHttpRequest) && category != null && subcategory != null && categoryData != null && categoryData.isPublic()) {
                //get a single subcategory

                String categoryName = getPluginResolver().getSingularFor(category);

                String appendToId = "";
                if (subcategory.startsWith(category)) {
                    appendToId = category + "_";
                }

                SubCategoryData subCategoryData = getStorage().getSubCategory(getDomain().getDocumentsName(), category, subcategory);
                if (subCategoryData != null) {
                    jsonReturn = DataAssembler.buildJsonFromSubCategoryData(categoryName, false  , appendToId, subCategoryData).toString();
                }
            }
        }

        writeContentsToBuffer(channelHandlerContext, jsonReturn, "application/json");
    }
}
