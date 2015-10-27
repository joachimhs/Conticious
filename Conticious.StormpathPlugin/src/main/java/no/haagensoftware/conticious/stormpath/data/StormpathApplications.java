package no.haagensoftware.conticious.stormpath.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhsmbp on 30/03/15.
 */
public class StormpathApplications {
    private String href;
    private Integer offset;
    private Integer limit;
    private Integer size;
    private List<StormpathApplication> items;

    public StormpathApplications() {
        this.items = new ArrayList<>();
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
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

    public List<StormpathApplication> getItems() {
        return items;
    }

    public void setItems(List<StormpathApplication> items) {
        this.items = items;
    }
}
