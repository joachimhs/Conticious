package no.haagensoftware.conticious.stormpath;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import io.netty.util.CharsetUtil;
import no.haagensoftware.conticious.stormpath.data.*;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.mozilla.universalchardet.UniversalDetector;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * Created by jhsmbp on 04/04/15.
 */
public class ConticiousStormpath {
    private static final Logger logger = Logger.getLogger(ConticiousStormpath.class.getName());
    
    private static ConticiousStormpath instance = null;
    private String authString = null;
    private String base64String = null;
    private StormpathTenant tenant = null;
    private StormpathApplication application = null;

    private ConticiousStormpath(String applicationName) {
        String idKey = "stormpath.id";
        String secretKey = "stormpath.secret";

        if (applicationName != null && applicationName.length() > 0) {
            idKey = applicationName + "." + idKey;
            secretKey = applicationName + "." + secretKey;
        }

        this.authString = System.getProperty(idKey) + ":" + System.getProperty(secretKey);

        base64String = base64EncodeString(authString);

        this.fetchTennant();
        this.fetchApplication(applicationName);
    }

    public static ConticiousStormpath getInstance(String applicationName) {
        if (instance == null) {
            instance = new ConticiousStormpath(applicationName);
        }

        return instance;
    }

    public StormpathAccount createUser(StormpathAccount account) {
        StormpathAccount spAccount = null;

        if (this.application != null && this.application.getAccounts() != null && this.application.getAccounts().getHref() != null && this.base64String != null) {
            String path = this.application.getAccounts().getHref();
            String json = new Gson().toJson(account);

            String retJson = postJsonContent(path, json);

            spAccount = new Gson().fromJson(retJson, StormpathAccount.class);
        }

        return spAccount;
    }

    public boolean usernameTaken(String email) {
        boolean taken = false;

        if (this.application != null && this.application.getAccounts() != null && this.application.getAccounts().getHref() != null && this.base64String != null) {
            String path = this.application.getAccounts().getHref() + "?email=" + email;

            String retJson = fetchJsonContent(path);
            logger.info("Found user with username: ");
            logger.info(retJson);

            StormpathAccounts accounts = new Gson().fromJson(retJson, StormpathAccounts.class);

            taken = accounts != null && accounts.getItems() != null && accounts.getItems().size() > 0;
        }

        return taken;
    }

    public boolean verifyEmail(String emailVerificationToken) {
        boolean verified = false;
        if (this.application != null && this.application.getVerificationEmails() != null && this.application.getVerificationEmails().getHref() != null && this.base64String != null) {
            String path = "https://api.stormpath.com/v1/accounts/emailVerificationTokens/" + emailVerificationToken;

            String retJson = postJsonContent(path, "");
            StormpathHref href = new Gson().fromJson(retJson, StormpathHref.class);
            verified = href != null && href.getHref() != null && href.getHref().length() > 10;

            logger.info("Email Verification: ");
            logger.info(retJson);
        }

        return  verified;
    }

    public StormpathPasswordResetToken resetPassword(String email) {
        JsonObject resetJson = new JsonObject();
        resetJson.addProperty("email", email);

        String jsonReturn = postJsonContent(application.getPasswordResetTokens().getHref(), resetJson.toString());

        StormpathPasswordResetToken token = new Gson().fromJson(jsonReturn, StormpathPasswordResetToken.class);

        return token;
    }

    public StormpathAccount authenticateUser(String username, String password) {
        StormpathAccount authenticatedAccount = null;

        if (base64String != null && application != null && application.getLoginAttempts() != null && application.getLoginAttempts().getHref() != null) {
            JsonObject spRequest = new JsonObject();
            spRequest.addProperty("type", "basic");
            spRequest.addProperty("value", base64EncodeString(username + ":" + password));

            String jsonReturn = postJsonContent(application.getLoginAttempts().getHref(), spRequest.toString());
            StormpathAccountObject spAccount = new Gson().fromJson(jsonReturn, StormpathAccountObject.class);

            if (spAccount != null && spAccount.getAccount() != null && spAccount.getAccount().getHref() != null) {
                String realAccountJson = fetchJsonContent(spAccount.getAccount().getHref());
                authenticatedAccount = new Gson().fromJson(realAccountJson, StormpathAccount.class);
            }
        }

        return authenticatedAccount;
    }

    public StormpathAccount authenticateUser(String username, String password, String groupId) {
        StormpathAccount authenticatedAccount = null;

        if (base64String != null && application != null && application.getLoginAttempts() != null && application.getLoginAttempts().getHref() != null) {
            JsonObject spRequest = new JsonObject();
            spRequest.addProperty("type", "basic");
            spRequest.addProperty("value", base64EncodeString(username + ":" + password));

            JsonObject accountStore = new JsonObject();
            accountStore.addProperty("href", "https://api.stormpath.com/v1/groups/" + groupId);

            spRequest.add("accountStore", accountStore);

            String jsonReturn = postJsonContent(application.getLoginAttempts().getHref(), spRequest.toString());
            StormpathAccountObject spAccount = new Gson().fromJson(jsonReturn, StormpathAccountObject.class);

            if (spAccount != null && spAccount.getAccount() != null && spAccount.getAccount().getHref() != null) {
                String realAccountJson = fetchJsonContent(spAccount.getAccount().getHref());
                authenticatedAccount = new Gson().fromJson(realAccountJson, StormpathAccount.class);
            }
        }

        return authenticatedAccount;
    }

    public StormpathAccount authenticateFacebookUser(ProviderDataObject providerDataObject) {
        StormpathAccount authenticatedAccount = null;

        if (base64String != null && application != null && application.getAccounts() != null && application.getAccounts().getHref() != null) {
            String jsonReturn = postJsonContent(application.getAccounts().getHref(), new Gson().toJson(providerDataObject));
            StormpathAccount spAccount = new Gson().fromJson(jsonReturn, StormpathAccount.class);
            if (spAccount != null && spAccount.getEmail() != null) {
                authenticatedAccount = spAccount;
            }
        }

        return authenticatedAccount;
    }

    private String base64EncodeString(String authString) {
        ByteBuf authChannelBuffer = Unpooled.copiedBuffer(authString, CharsetUtil.UTF_8);
        ByteBuf encodedAuthChannelBuffer = Base64.encode(authChannelBuffer);

        String encoded = encodedAuthChannelBuffer.toString(CharsetUtil.UTF_8);
        encoded = encoded.replace("\n", "");
        return encoded;
    }

    private String postJsonContent(String url, String jsonContent) {
        String json = null;


        try {
            String encoding = guessEncoding(jsonContent);
            byte[] jsonBytes = jsonContent.getBytes(encoding);
            if (jsonBytes != null) {
                jsonContent = new String(jsonBytes, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            //Nothing to do. using string as-is
        }

        logger.info("POSTING TO: " + url);

        //very dirty fix for Norwegian characters...
        jsonContent = jsonContent.replace("ø", "_oe_");
        jsonContent = jsonContent.replace("æ", "_ae_");
        jsonContent = jsonContent.replace("å", "_aa_");
        jsonContent = jsonContent.replace("Ø", "_Oe_");
        jsonContent = jsonContent.replace("Æ", "_Ae_");
        jsonContent = jsonContent.replace("Å", "_Aa_");

        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {

            if (base64String != null) {
                HttpPost httpPost = new HttpPost(url);
                StringEntity requestEntity = new StringEntity(jsonContent);
                httpPost.setEntity(requestEntity);

                httpPost.setHeader("Authorization", "Basic " + this.base64String);
                httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
                httpPost.setHeader("Accept", "application/json");


                for (Header header : httpPost.getAllHeaders()) {
                    logger.info("Req header " + header.getName() + " : " + header.getValue());
                }

                logger.info("\n" + EntityUtils.toString(httpPost.getEntity(), "UTF-8"));

                HttpResponse response = httpclient.execute(httpPost);

                HttpEntity entity = response.getEntity();
                json = EntityUtils.toString(entity);

                logger.info("---");
                for (Header header : response.getAllHeaders()) {
                    logger.info("Res header " + header.getName() + " : " + header.getValue());
                }
                logger.info(json);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }

        return json;
    }

    private String fetchJsonContent(String url) {
        String json = null;

        logger.info("FETCHING FROM: " + url);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {

            if (base64String != null) {
                HttpGet httpGet = new HttpGet(url);
                httpGet.setHeader("Authorization", "Basic " + this.base64String);

                for (Header header : httpGet.getAllHeaders()) {
                    logger.info("Req header " + header.getName() + " : " + header.getValue());
                }

                HttpResponse response = httpclient.execute(httpGet);

                HttpEntity entity = response.getEntity();
                json = EntityUtils.toString(entity);

                logger.info("---");
                for (Header header : response.getAllHeaders()) {
                    logger.info("Res header " + header.getName() + " : " + header.getValue());
                }

                logger.info(json);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }

        return json;
    }

    private StormpathTenant fetchTennant() {
        StormpathTenant tenant = null;

        if (base64String != null) {
            String json = fetchJsonContent("https://api.stormpath.com/v1/tenants/current");
            logger.info("GOT JSON!");
            logger.info(json);
            if (json != null && json.length() > 20) {
                StormpathTenant spTenant = new Gson().fromJson(json, StormpathTenant.class);
                if (spTenant != null && spTenant.getHref() != null && spTenant.getHref().contains("/")) {
                    tenant = spTenant;
                }
            }
        }

        this.tenant = tenant;

        logger.info("TENNANT: " + this.tenant);
        return tenant;
    }

    private StormpathApplication fetchApplication(String applicationName) {
        if (tenant != null && tenant.getApplications() != null && tenant.getApplications().getHref() != null) {
            String applicationReturn = fetchJsonContent(tenant.getApplications().getHref() + "?name=" + applicationName);
            logger.info("APPLICATIONS RETURN");
            logger.info(applicationReturn);

            StormpathApplications spApps = new Gson().fromJson(applicationReturn, StormpathApplications.class);

            if (spApps.getItems().size() == 1) {
                this.application = spApps.getItems().get(0);
            }

        }

        return this.application;
    }

    public static String guessEncoding(String input) {
        UniversalDetector detector = new UniversalDetector(null);
        byte[] buf = new byte[4096];

        detector.handleData(input.getBytes(), 0, input.getBytes().length-1);

        detector.dataEnd();

        String encoding = detector.getDetectedCharset();
        if (encoding == null) {
            encoding = "UTF-8";
            logger.info("Bruker standard UTF-8 encoding");
        } else {
            logger.info("Gjetter " + encoding + " encoding");
        }

        return encoding;
    }
}
