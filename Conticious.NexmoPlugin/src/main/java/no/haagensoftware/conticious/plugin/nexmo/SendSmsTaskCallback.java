package no.haagensoftware.conticious.plugin.nexmo;

/**
 * Created by jhsmbp on 08/10/16.
 */
public interface SendSmsTaskCallback<T extends Runnable> {
    void runnableFinished(SendSmsTask runnable);
}
