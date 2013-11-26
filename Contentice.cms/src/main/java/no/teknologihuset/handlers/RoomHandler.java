package no.teknologihuset.handlers;

import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import no.haagensoftware.contentice.data.CategoryData;
import no.haagensoftware.contentice.data.SubCategoryData;
import no.haagensoftware.contentice.handler.ContenticeHandler;

/**
 * Created with IntelliJ IDEA.
 * User: jhsmbp
 * Date: 11/26/13
 * Time: 12:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class RoomHandler extends ContenticeHandler {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        List<SubCategoryData> rooms = getStorage().getSubCategories("rooms");

        JsonArray roomsArray = new JsonArray();
        for (SubCategoryData room : rooms) {
            roomsArray.add(RoomAssembler.buildRoomJsonFromSubCategoryData(room));
        }

        JsonObject topLevelObject = new JsonObject();
        topLevelObject.add("rooms", roomsArray);

        writeContentsToBuffer(channelHandlerContext, topLevelObject.toString(), "application/json; charset=UTF-8");
    }
}
