package no.haagensoftware.contentice.data;

import java.util.List;
/**
 * Created by jhsmbp on 19/04/14.
 */
public class Settings {
    private static Settings instance = null;
    private ConticiousOptions conticiousOptions;

    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }

        return instance;
    }

    public ConticiousOptions getConticiousOptions() {
        return conticiousOptions;
    }

    public void setConticiousOptions(ConticiousOptions conticiousOptions) {
        this.conticiousOptions = conticiousOptions;
    }

    public List<Domain> getDomains() {
        List<Domain> domains = null;

        if (conticiousOptions != null) {
            domains = conticiousOptions.getDomains();
        }
        return domains;
    }
}
