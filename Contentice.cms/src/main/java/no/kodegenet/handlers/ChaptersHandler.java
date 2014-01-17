package no.kodegenet.handlers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import no.haagensoftware.contentice.data.SubCategoryData;
import no.haagensoftware.contentice.handler.ContenticeHandler;

import java.util.List;

/**
 * Created by jhsmbp on 1/16/14.
 */
public class ChaptersHandler extends ContenticeHandler {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        String jsonReturn = "";

        String chapterId = getParameter("chapter");

        List<String> chaptersIds = getQueryStringIds();

        if (isGet(fullHttpRequest) && chapterId == null && chaptersIds != null && chaptersIds.size() == 0) {
            List<SubCategoryData> chapters = getStorage().getSubCategories("chapters");

            JsonArray chaptersArray = new JsonArray();
            for (SubCategoryData chapter : chapters) {
                chaptersArray.add(CourseAssembler.buildPageJsonFromSubCategoryData(chapter));
            }

            JsonObject topLevelObject = new JsonObject();
            topLevelObject.add("chapters", chaptersArray);
            jsonReturn = topLevelObject.toString();
        } else if (isGet(fullHttpRequest) && chapterId != null) {
            SubCategoryData chapter = getStorage().getSubCategory("chapters", chapterId);
            if (chapter != null) {
                JsonObject topLevelObject = new JsonObject();
                JsonObject chapterObject = CourseAssembler.buildPageJsonFromSubCategoryData(chapter);
                topLevelObject.add("chapter", chapterObject);

                jsonReturn = topLevelObject.toString();
            }
        } else if (isGet(fullHttpRequest) && chaptersIds != null && chaptersIds.size() > 0) {
            JsonArray chaptersArray = new JsonArray();
            for (String id : chaptersIds) {
                SubCategoryData chapter = getStorage().getSubCategory("chapters", id);
                if (chapter != null) {
                    JsonObject chapterObject = CourseAssembler.buildPageJsonFromSubCategoryData(chapter);
                    chaptersArray.add(chapterObject);
                }
            }

            JsonObject topLevelObject = new JsonObject();
            topLevelObject.add("chapters", chaptersArray);
            jsonReturn = topLevelObject.toString();
        }

        writeContentsToBuffer(channelHandlerContext, jsonReturn, "application/json; charset=UTF-8");
    }
}
