package no.haagensoftware.contentice.data;

/**
 * Created by jhsmbp on 18/04/14.
 */
public class Domain {
    private String id;
    private String domainName;
    private String webappName;

    //file upload
    private String uploadUrl;
    private String uploadPath;
    private String createCategory;

    private Boolean active;
    private Boolean minified;

    private String postProcessor;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getWebappName() {
        return webappName;
    }

    public void setWebappName(String webappName) {
        this.webappName = webappName;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public String getCreateCategory() {
        return createCategory;
    }

    public void setCreateCategory(String createCategory) {
        this.createCategory = createCategory;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getMinified() {
        return minified;
    }

    public void setMinified(Boolean minified) {
        this.minified = minified;
    }

    public String getPostProcessor() {
        return postProcessor;
    }

    public void setPostProcessor(String postProcessor) {
        this.postProcessor = postProcessor;
    }
}
