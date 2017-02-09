package no.haagensoftware.conticious.plugin.nexmo;

import no.haagensoftware.conticious.plugin.nexmo.data.NexmoData;
import no.haagensoftware.conticious.plugin.nexmo.data.NexmoRecipientData;

import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by jhsmbp on 08/10/16.
 */
public class NexmoAlertService {
    private static NexmoAlertService instance = null;
    private String apiKey = null;
    private String secretKey  = null;
    private ScheduledThreadPoolExecutor threadPool;

    private NexmoAlertService(String applicationName) {
        String idKey = "nexmo.id";
        String secretKey = "nexmo.secret";

        if (applicationName != null && applicationName.length() > 0) {
            idKey = applicationName + "." + idKey;
            secretKey = applicationName + "." + secretKey;
        }

        this.apiKey = System.getProperty(idKey);
        this.secretKey = System.getProperty(secretKey);

        threadPool = new ScheduledThreadPoolExecutor(1);
    }

    public static NexmoAlertService getInstance(String applicationName) {
        if (instance == null) {
            instance = new NexmoAlertService(applicationName);
        }

        return instance;
    }

    public List<NexmoRecipientData> sendSms(List<String> recipients, String message, SendSmsTaskCallback callback) {
        NexmoData nexmoData = new NexmoData();
        nexmoData.setFrom("Kodegenet");
        nexmoData.setMessage(message);
        nexmoData.setUsername(apiKey);
        nexmoData.setPassword(secretKey);
        nexmoData.addRecipients(recipients);

        for (NexmoRecipientData nrd : nexmoData.getRecipientList()) {
            threadPool.schedule(createRunnableWithCallback(new SendSmsTask(nexmoData, nrd), callback), 1000, TimeUnit.MILLISECONDS);
        }

        return nexmoData.getRecipientList();
    }


    private static <T extends Runnable> Runnable createRunnableWithCallback(
            final SendSmsTask runnable, final SendSmsTaskCallback<T> callback)
    {
        return new Runnable()
        {
            @Override
            public void run()
            {
                runnable.run();
                if (callback != null) {
                    callback.runnableFinished(runnable);
                }
            }
        };
    }
}
