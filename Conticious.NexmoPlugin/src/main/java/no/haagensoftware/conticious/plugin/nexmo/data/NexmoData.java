package no.haagensoftware.conticious.plugin.nexmo.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhsmbp on 08/10/16.
 */
public class NexmoData {
    private String username;
    private String password;
    private String from;
    private List<NexmoRecipientData> recipientList;
    private String message;

    public NexmoData() {
        super();

        recipientList = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<NexmoRecipientData> getRecipientList() {
        return recipientList;
    }

    public void setRecipientList(List<NexmoRecipientData> recipientList) {
        this.recipientList = recipientList;
    }

    public void clearRecipients() {
        recipientList.clear();
    }

    public void addRecipient(String number) {
        recipientList.add(new NexmoRecipientData(number));
    }

    public void addRecipients(List<String> numbers) {
        for (String number : numbers) {
            addRecipient(number);
        }
    }
}
