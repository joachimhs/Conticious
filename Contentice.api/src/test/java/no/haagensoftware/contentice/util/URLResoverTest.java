package no.haagensoftware.contentice.util;

import junit.framework.Assert;
import no.haagensoftware.contentice.data.URLData;
import org.junit.Before;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: joahaage
 * Date: 16.11.13
 * Time: 16:18
 * To change this template use File | Settings | File Templates.
 */
public class URLResoverTest {
    private URLResolver resolver;

    @Before
    public void setup() {
        resolver = new URLResolver();
    }
    @Test
    public void verifyUrlWithoutParameters() {
        URLData urlData = resolver.getValueForUrl("/categories");
        Assert.assertNotNull(urlData);
        Assert.assertEquals("/categories", urlData.getUrlPattern());
    }

    @Test
     public void verifyUrlWithOneParameter() {
        URLData urlData = resolver.getValueForUrl("/categories/pages");
        Assert.assertNotNull(urlData);

        Assert.assertEquals("/categories/{category}", urlData.getUrlPattern());
        Assert.assertEquals("pages", urlData.getParameters().get("category"));
    }

    @Test
    public void verifyUrlWithMultipleParameters() {
        URLData urlData = resolver.getValueForUrl("/categories/pages/subcategories");
        Assert.assertNotNull(urlData);

        Assert.assertEquals("/categories/{category}/subcategories", urlData.getUrlPattern());
        Assert.assertEquals("pages", urlData.getParameters().get("category"));

        urlData = resolver.getValueForUrl("/categories/pages/subcategories/home");
        Assert.assertNotNull(urlData);

        Assert.assertEquals("/categories/{category}/subcategories/{subcategory}", urlData.getUrlPattern());
        Assert.assertEquals("pages", urlData.getParameters().get("category"));
        Assert.assertEquals("home", urlData.getParameters().get("subcategory"));
    }

    @Test
    public void verifyNoMatchReturnsNull() {
        URLData urlData = resolver.getValueForUrl("/pages/home");
        Assert.assertNull(urlData);
    }

    @Test
    public void verifyPartialMatchReturnsNull() {
        URLData urlData = resolver.getValueForUrl("/categories/pages/subcategory/home");
        Assert.assertNull(urlData);
    }

    @Test
    public void verifyTooLongURLhReturnsNull() {
        URLData urlData = resolver.getValueForUrl("/categories/pages/subcategories/home/too/long");
        Assert.assertNull(urlData);
    }
}
