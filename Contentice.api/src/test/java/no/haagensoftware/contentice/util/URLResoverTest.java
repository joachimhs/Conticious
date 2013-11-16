package no.haagensoftware.contentice.util;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
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
    private ChannelHandler channelHandler1;
    private ChannelHandler channelHandler2;
    private ChannelHandler channelHandler3;
    private ChannelHandler channelHandler4;
    @Before
    public void setup() {
        resolver = new URLResolver();
        channelHandler1 = new SimpleChannelInboundHandler<FullHttpRequest>() {
            @Override
            protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        };
        channelHandler2 = new SimpleChannelInboundHandler<FullHttpRequest>() {
            @Override
            protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        };
        channelHandler3 = new SimpleChannelInboundHandler<FullHttpRequest>() {
            @Override
            protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        };
        channelHandler4 = new SimpleChannelInboundHandler<FullHttpRequest>() {
            @Override
            protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        };

        resolver.addUrlPattern("/categories", channelHandler1);
        resolver.addUrlPattern("/categories/{category}", channelHandler2);
        resolver.addUrlPattern("/categories/{category}/subcategories", channelHandler3);
        resolver.addUrlPattern("/categories/{category}/subcategories/{subcategory}", channelHandler4);
    }
    @Test
    public void verifyUrlWithoutParameters() {
        URLData urlData = resolver.getValueForUrl("/categories");
        Assert.assertNotNull(urlData);
        Assert.assertEquals("/categories", urlData.getUrlPattern());
        Assert.assertEquals(channelHandler1, urlData.getChannelHandler());

    }

    @Test
     public void verifyUrlWithOneParameter() {
        URLData urlData = resolver.getValueForUrl("/categories/pages");
        Assert.assertNotNull(urlData);

        Assert.assertEquals("/categories/{category}", urlData.getUrlPattern());
        Assert.assertEquals("pages", urlData.getParameters().get("category"));
        Assert.assertEquals(channelHandler2, urlData.getChannelHandler());
    }

    @Test
    public void verifyUrlWithMultipleParameters() {
        URLData urlData = resolver.getValueForUrl("/categories/pages/subcategories");
        Assert.assertNotNull(urlData);

        Assert.assertEquals("/categories/{category}/subcategories", urlData.getUrlPattern());
        Assert.assertEquals("pages", urlData.getParameters().get("category"));
        Assert.assertEquals(channelHandler3, urlData.getChannelHandler());

        urlData = resolver.getValueForUrl("/categories/pages/subcategories/home");
        Assert.assertNotNull(urlData);

        Assert.assertEquals("/categories/{category}/subcategories/{subcategory}", urlData.getUrlPattern());
        Assert.assertEquals("pages", urlData.getParameters().get("category"));
        Assert.assertEquals("home", urlData.getParameters().get("subcategory"));
        Assert.assertEquals(channelHandler4, urlData.getChannelHandler());
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
