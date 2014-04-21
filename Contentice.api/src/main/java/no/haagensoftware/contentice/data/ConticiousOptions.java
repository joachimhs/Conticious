package no.haagensoftware.contentice.data;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by jhsmbp on 18/04/14.
 */
public class ConticiousOptions {
    List<Domain> domains;

    public ConticiousOptions() {
        domains = new ArrayList<>();
    }

    public List<Domain> getDomains() {
        return domains;
    }

    public void setDomains(List<Domain> domains) {
        this.domains = domains;
    }

    public Domain getWebappForHost(String host) {
        Domain domain = null;

        for (Domain currDomain : domains) {
            if (currDomain.getDomainName().equals(host)) {
                domain = currDomain;
                break;
            }
        }

        return domain;
    }
}
