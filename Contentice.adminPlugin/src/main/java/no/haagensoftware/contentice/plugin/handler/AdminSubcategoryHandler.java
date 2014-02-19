package no.haagensoftware.contentice.plugin.handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.CharsetUtil;
import no.haagensoftware.contentice.assembler.SubCategoryAssembler;
import no.haagensoftware.contentice.data.CategoryData;
import no.haagensoftware.contentice.data.SubCategoryData;
import no.haagensoftware.contentice.handler.ContenticeHandler;
import no.haagensoftware.contentice.plugin.admindata.AdminSubcategoryObject;
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

                getStorage().setSubCategory(category, adminSubcategory.getSubcategory().getName(), adminSubcategory.getSubcategory());
            }
        }

        SubCategoryData subCategoryData = getStorage().getSubCategory(category, subcategory);
        CategoryData categoryData = getStorage().getCategory(category);

        if (subCategoryData == null) {
            write404ToBuffer(channelHandlerContext);

        } else {
            JsonObject topLevelObject = new JsonObject();
            topLevelObject.add("subCategory", AdminSubCategoryAssembler.buildAdminJsonFromSubCategoryData(subCategoryData, categoryData));

            writeContentsToBuffer(channelHandlerContext, topLevelObject.toString(), "application/json");
        }
    }
}
