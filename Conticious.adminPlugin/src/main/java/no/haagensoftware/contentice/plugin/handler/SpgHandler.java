package no.haagensoftware.contentice.plugin.handler;

import com.google.gson.Gson;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import no.haagensoftware.contentice.data.Domain;
import no.haagensoftware.contentice.data.Settings;
import no.haagensoftware.contentice.data.auth.Session;
import no.haagensoftware.contentice.handler.ContenticeHandler;
import no.haagensoftware.contentice.plugin.admindata.AdminSpgData;
import no.haagensoftware.contentice.spi.AuthenticationPlugin;
import no.haagensoftware.contentice.util.PhantomJsExecutor;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * Created by jhsmbp on 04/05/14.
 */
public class SpgHandler extends ContenticeHandler {
    private static final Logger logger = Logger.getLogger(SpgHandler.class.getName());

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        AuthenticationPlugin authenticationPlugin = getAuthenticationPlugin();

        if (authenticationPlugin == null) {
            sendError(channelHandlerContext, HttpResponseStatus.UNAUTHORIZED);
        } else {
            String cookieUuidToken = getCookieValue(fullHttpRequest, "uuidAdminToken");
            Session session = authenticationPlugin.getSession(cookieUuidToken);

            if (session != null && "super".equals(session.getUser().getRole())) {

                String domain = getParameter("domain");

                logger.info("Static Page Generator started for domain: " + domain);

                handleRequest(channelHandlerContext, fullHttpRequest);
            } else {
                sendError(channelHandlerContext, HttpResponseStatus.UNAUTHORIZED);
            }
        }

        handleRequest(channelHandlerContext, fullHttpRequest);
    }

    private void handleRequest(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) {
        String jsonResponse = "";

        String inputDomain = getParameter("domain");
        Domain foundDomain = null;

        for (Domain domain : Settings.getInstance().getDomains()) {
            if (domain.getDomainName().equals(inputDomain)) {
                foundDomain = domain;
                break;
            }
        }

        logger.info("Found domain, executing PhantomJS");
        if (foundDomain != null) {
            AdminSpgData spgData = new Gson().fromJson(getHttpMessageContent(fullHttpRequest), AdminSpgData.class);

            String urlToReplace = spgData.getHostname();
            if (spgData.getPort() != null && !spgData.getPort().equals("80")) {
                urlToReplace += ":" + spgData.getPort();
            }

            String phantomReturn = PhantomJsExecutor.executePhantomJs(
                "http://" + foundDomain.getDocumentsName(),
                System.getProperty("no.haagensoftware.contentice.webappDir") + File.separatorChar + foundDomain.getDocumentsName() + File.separatorChar + "static",
                System.getProperty("no.haagensoftware.contentice.webappDir") + File.separatorChar + foundDomain.getDocumentsName() + File.separatorChar + "Sitemap.xml",
                foundDomain.getDocumentsName(),
                foundDomain.getDocumentsName()
            );
            logger.info(phantomReturn);
        }

        writeContentsToBuffer(channelHandlerContext, jsonResponse, "application/json");
    }
}
