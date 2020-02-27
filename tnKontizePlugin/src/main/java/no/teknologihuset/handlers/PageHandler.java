package no.teknologihuset.handlers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import no.haagensoftware.contentice.data.SubCategoryData;
import no.haagensoftware.contentice.handler.ContenticeHandler;
import no.teknologihuset.epost.EpostExecutor;

import java.util.List;
/**
 * Created by jhsmbp on 12/18/13.
 */
public class PageHandler extends ContenticeHandler {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        String jsonReturn = "";

        //Start the email service
        EpostExecutor.getInstance(getDomain().getWebappName()).sendRemainingEmails(getStorage());

        String pageId = getParameter("page");

        List<SubCategoryData> pages = getStorage().getSubCategories(getDomain().getWebappName(), "pages");
        if (isGet(fullHttpRequest) && pageId == null) {
            //Get all pages
            JsonArray pageArray = new JsonArray();
            for (SubCategoryData page : pages) {
                pageArray.add(PageAssembler.buildPageJsonFromSubCategoryData(page));
            }

            JsonObject topLevelObject = new JsonObject();
            topLevelObject.add("pages", pageArray);
            jsonReturn = topLevelObject.toString();
        }

        writeContentsToBuffer(channelHandlerContext, jsonReturn, "application/json");
    }
}
