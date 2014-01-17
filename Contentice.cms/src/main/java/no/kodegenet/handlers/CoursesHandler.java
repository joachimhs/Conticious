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
public class CoursesHandler extends ContenticeHandler {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        String jsonReturn = "";

        List<SubCategoryData> courses = getStorage().getSubCategories("courses");

        JsonArray pageArray = new JsonArray();
        for (SubCategoryData course : courses) {
            pageArray.add(CourseAssembler.buildPageJsonFromSubCategoryData(course));
        }

        JsonObject topLevelObject = new JsonObject();
        topLevelObject.add("courses", pageArray);
        jsonReturn = topLevelObject.toString();

        writeContentsToBuffer(channelHandlerContext, jsonReturn, "application/json; charset=UTF-8");
    }
}
