package no.haagensoftware.contentice.util;

import io.netty.channel.ChannelHandler;
import junit.framework.Assert;
import no.haagensoftware.contentice.data.URLData;

import no.haagensoftware.contentice.spi.RouterPlugin;
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
    private PluginResolver resolver;
    private RouterPlugin plugin1;
    private RouterPlugin plugin2;

    private Class<ChannelHandler> channelHandler1;
    private Class<ChannelHandler> channelHandler2;
    private Class<ChannelHandler> channelHandler3;
    private Class<ChannelHandler> channelHandler4;
    private Class<ChannelHandler> channelHandler5;
    @Before
    public void setup() {
        resolver = new PluginResolver();

        resolver.addUrlPattern("/categories", plugin1);
        resolver.addUrlPattern("/categories/{category}", plugin1);
        resolver.addUrlPattern("/categories/{category}/subcategories", plugin1);
        resolver.addUrlPattern("/categories/{category}/subcategories/{subcategory}", plugin2);
        resolver.addUrlPattern("/json/data/{category}", plugin2);
        resolver.addUrlPattern("/json/data/{category}/{subcategory}", plugin2);
        resolver.addUrlPattern("classpath:/admin", plugin2);
    }

    @Test
    public void verifyUrlWithoutParameters() {
        URLData urlData = resolver.getValueForUrl("/categories");
        Assert.assertNotNull(urlData);
        Assert.assertEquals("/categories", urlData.getUrlPattern());
        Assert.assertEquals(plugin1, urlData.getPlugin());

    }

    @Test
     public void verifyUrlWithOneParameter() {
        URLData urlData = resolver.getValueForUrl("/categories/pages");
        Assert.assertNotNull(urlData);

        Assert.assertEquals("/categories/{category}", urlData.getUrlPattern());
        Assert.assertEquals("pages", urlData.getParameters().get("category"));
        Assert.assertEquals(plugin1, urlData.getPlugin());
    }

    @Test
    public void verifyUrlWithOneParameterWithoutUrlIndex() {
        URLData urlData = resolver.getValueForUrl("/json/data/pages");
        Assert.assertNotNull(urlData);

        Assert.assertEquals("/json/data/{category}", urlData.getUrlPattern());
        Assert.assertEquals("pages", urlData.getParameters().get("category"));
        Assert.assertEquals(plugin2, urlData.getPlugin());
    }

    @Test
    public void verifyUrlWithMultipleParameters() {
        URLData urlData = resolver.getValueForUrl("/categories/pages/subcategories");
        Assert.assertNotNull(urlData);

        Assert.assertEquals("/categories/{category}/subcategories", urlData.getUrlPattern());
        Assert.assertEquals("pages", urlData.getParameters().get("category"));
        Assert.assertEquals(plugin1, urlData.getPlugin());

        urlData = resolver.getValueForUrl("/categories/pages/subcategories/home");
        Assert.assertNotNull(urlData);

        Assert.assertEquals("/categories/{category}/subcategories/{subcategory}", urlData.getUrlPattern());
        Assert.assertEquals("pages", urlData.getParameters().get("category"));
        Assert.assertEquals("home", urlData.getParameters().get("subcategory"));
        Assert.assertEquals(plugin2, urlData.getPlugin());
    }

    @Test
    public void verifyUrlWithMultipleParametersWihtoutUrlIndex() {
        URLData urlData = resolver.getValueForUrl("/json/data/pages/pageOne");
        Assert.assertNotNull(urlData);

        Assert.assertEquals("/json/data/{category}/{subcategory}", urlData.getUrlPattern());
        Assert.assertEquals("pages", urlData.getParameters().get("category"));
        Assert.assertEquals("pageOne", urlData.getParameters().get("subcategory"));
        Assert.assertEquals(plugin2, urlData.getPlugin());
    }

    @Test
    public void verifyClasspathUrl() {
        URLData urlData = resolver.getValueForUrl("/admin/index.html");

        Assert.assertNotNull(urlData);
        Assert.assertEquals("classpath:/admin", urlData.getUrlPattern());
        Assert.assertEquals("/admin/index.html", urlData.getRealUrl());
        Assert.assertEquals(plugin2, urlData.getPlugin());

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

    @Test
    public void testQueryParams() {
        //resolver.addUrlPattern("/json/data/{category}?id=123", plugin2);

        URLData urlData = resolver.getValueForUrl("/json/data/category?id=333");
        System.out.println(urlData);
    }
}
