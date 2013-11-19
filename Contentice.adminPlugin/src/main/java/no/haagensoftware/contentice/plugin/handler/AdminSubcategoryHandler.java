package no.haagensoftware.contentice.plugin.handler;

import com.google.gson.JsonObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import no.haagensoftware.contentice.assembler.SubCategoryAssembler;
import no.haagensoftware.contentice.data.SubCategoryData;
import no.haagensoftware.contentice.handler.ContenticeHandler;
import no.haagensoftware.contentice.plugin.assembler.AdminSubCategoryAssembler;
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
        logger.info("reading SubCategoryHandler and writing contents to buffer");

        String category = getParameter("category");
        String subcategory = getParameter("subcategory");

        SubCategoryData subCategoryData = getStorage().getSubCategory(category, subcategory);

        if (subCategoryData == null) {
            write404ToBuffer(channelHandlerContext);

        } else {
            JsonObject topLevelObject = new JsonObject();
            topLevelObject.add("subCategory", AdminSubCategoryAssembler.buildAdminJsonFromSubCategoryData(subCategoryData, category));

            writeContentsToBuffer(channelHandlerContext, topLevelObject.toString(), "application/json; charset=UTF-8");
            channelHandlerContext.fireChannelRead(fullHttpRequest);
        }
    }
}
