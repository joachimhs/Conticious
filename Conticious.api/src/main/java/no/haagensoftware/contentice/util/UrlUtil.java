package no.haagensoftware.contentice.util;

import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.QueryStringEncoder;

import java.net.URISyntaxException;

/**
 * Created by jhsmbp on 20/02/16.
 */
public class UrlUtil {
    public static String encodeUrl(String url) {
        String encoded = url;

        QueryStringEncoder encoder = new QueryStringEncoder(url);
        try {
            encoded = encoder.toUri().toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            //Nothing to do, return url as it was
        }

        return encoded;
    }

    public static String decodeUrl(String url) {
        QueryStringDecoder decoder = new QueryStringDecoder(url);

        String uri = decoder.uri();
        uri = uri.replaceAll("%20", " ");
        uri = uri.replaceAll("%40", "@");

        return uri;
    }
}
