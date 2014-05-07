package no.haagensoftware.contentice.plugin.handler;

import com.google.gson.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import no.haagensoftware.contentice.data.ConticiousOptions;
import no.haagensoftware.contentice.data.Domain;
import no.haagensoftware.contentice.data.Settings;
import no.haagensoftware.contentice.data.auth.Session;
import no.haagensoftware.contentice.handler.ContenticeHandler;
import no.haagensoftware.contentice.spi.AuthenticationPlugin;
import no.haagensoftware.contentice.util.JsonUtil;
import org.apache.log4j.Logger;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhsmbp on 19/04/14.
 */
public class SettingsHandler extends ContenticeHandler {
    private static final Logger logger = Logger.getLogger(SettingsHandler.class.getName());

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        AuthenticationPlugin authenticationPlugin = getAuthenticationPlugin();

        if (authenticationPlugin == null) {
            sendError(channelHandlerContext, HttpResponseStatus.UNAUTHORIZED);
        } else {
            String cookieUuidToken = getCookieValue(fullHttpRequest, "uuidAdminToken");
            Session session = authenticationPlugin.getSession(cookieUuidToken);

            if (session != null && "super".equals(session.getUser().getRole())) {
                handleRequest(channelHandlerContext, fullHttpRequest);
            } else {
                JsonObject topLevelObject = buildDomainObject(new ArrayList<Domain>());

                writeContentsToBuffer(channelHandlerContext, topLevelObject.toString(), "application/json");
            }
        }

        handleRequest(channelHandlerContext, fullHttpRequest);
    }

    private void handleRequest(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) {
        String jsonResponse = "";

        if (isGet(fullHttpRequest)) {
            List<Domain> domains = Settings.getInstance().getDomains();

            JsonObject topLevelObject = buildDomainObject(domains);

            writeContentsToBuffer(channelHandlerContext, topLevelObject.toString(), "application/json");
        } else if(isPost(fullHttpRequest)) {
            String jsonData = getHttpMessageContent(fullHttpRequest);
            logger.info(jsonData);
            ConticiousOptions conticiousOptions = new Gson().fromJson(jsonData, ConticiousOptions.class);

            for (Domain domain : conticiousOptions.getDomains()) {
                domain.setId(domain.getDomainName());
            }

            if (conticiousOptions != null) {
                Settings.getInstance().setConticiousOptions(conticiousOptions);
            }

            String docDirectory = System.getProperty("no.haagensoftware.contentice.storage.file.documentsDirectory");

            JsonUtil.writeJsonToFile(FileSystems.getDefault().getPath(docDirectory + File.separatorChar + "admin" + File.separatorChar + "conticiousOptions.json"), new Gson().toJson(conticiousOptions));

            JsonObject topLevelObject = buildDomainObject(conticiousOptions.getDomains());

            writeContentsToBuffer(channelHandlerContext, topLevelObject.toString(), "application/json");
        }
    }

    private JsonObject buildDomainObject(List<Domain> domains) {
        JsonArray domainArray = new JsonArray();
        JsonArray domainStringArray = new JsonArray();
        for (Domain domain : domains) {
            JsonElement domainObject = new Gson().toJsonTree(domain);
            domainArray.add(domainObject);
            if (domain.getId() == null) {
                domain.setId(domain.getDomainName());
            }
            domainStringArray.add(new JsonPrimitive(domain.getId()));
        }

        JsonObject domainObject = new JsonObject();
        domainObject.addProperty("id", "ConticiousSettings");
        domainObject.add("domains", domainStringArray);

        JsonObject topLevelObject = new JsonObject();
        topLevelObject.add("setting", domainObject);
        topLevelObject.add("domains", domainArray);
        return topLevelObject;
    }
}
