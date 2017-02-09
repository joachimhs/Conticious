package no.haagensoftware.conticious.plugin.nexmo;

import no.haagensoftware.conticious.plugin.nexmo.data.NexmoData;
import no.haagensoftware.conticious.plugin.nexmo.data.NexmoRecipientData;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by jhsmbp on 08/10/16.
 */
public class SendSmsTask implements Runnable {
    private NexmoRecipientData nexmoRecipientData;
    private NexmoData nexmoData;

    public SendSmsTask(NexmoData nexmoData, NexmoRecipientData nexmoRecipientData) {
        this.nexmoData = nexmoData;
        this.nexmoRecipientData = nexmoRecipientData;
    }

    @Override
    public void run() {
        try {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                //No big deal if we cannot sleep
            }

            String requestString = buildRequestString(nexmoRecipientData.getNumber());
            String responseText = "";
            URLConnection connection = new URL("https://rest.nexmo.com/sms/json?" + requestString).openConnection();
            InputStream response = connection.getInputStream();
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(response));
                for (String line; (line = reader.readLine()) != null;) {
                    responseText += line + "\n";
                }

                System.out.println("Response: " + responseText);
            } finally {
                if (reader != null) {
                    nexmoRecipientData.setResponseText(responseText);
                    try {
                        reader.close();
                    } catch (IOException logOrIgnore) {

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMessage() {
        return nexmoData.getMessage();
    }

    public NexmoRecipientData getNexmoRecipientData() {
        return nexmoRecipientData;
    }

    private String buildRequestString(String recipient) throws UnsupportedEncodingException {
        String query = String.format("api_key=%s&api_secret=%s&from=%s&to=%s&text=%s",
                URLEncoder.encode(nexmoData.getUsername(), "UTF-8"),
                URLEncoder.encode(nexmoData.getPassword(), "UTF-8"),
                URLEncoder.encode(nexmoData.getFrom(), "UTF-8"),
                URLEncoder.encode(recipient, "UTF-8"),
                URLEncoder.encode(nexmoData.getMessage(), "UTF-8"));

        System.out.println("requestString: " + query);
        return query;
    }
}
