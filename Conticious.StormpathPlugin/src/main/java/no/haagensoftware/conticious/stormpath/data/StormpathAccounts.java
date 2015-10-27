package no.haagensoftware.conticious.stormpath.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhsmbp on 31/03/15.
 */
public class StormpathAccounts {
    private String href;
    private Integer offset;
    private Integer limit;
    private Integer size;
    private List<StormpathAccount> items;

    public StormpathAccounts() {
        items = new ArrayList<>();
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public List<StormpathAccount> getItems() {
        return items;
    }

    public void setItems(List<StormpathAccount> items) {
        this.items = items;
    }
}
