package no.haagensoftware.conticious.plugin.nexmo.data;

/**
 * Created by jhsmbp on 08/10/16.
 */
public class NexmoRecipientData {
    private String number;
    private String responseText;

    public NexmoRecipientData() {
    }

    public NexmoRecipientData(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }
}
