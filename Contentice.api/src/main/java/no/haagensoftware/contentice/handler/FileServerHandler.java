package no.haagensoftware.contentice.handler;


import com.sun.management.UnixOperatingSystemMXBean;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import no.haagensoftware.contentice.data.Domain;
import no.haagensoftware.contentice.util.ContentTypeUtil;
import no.haagensoftware.conticious.scriptcache.ScriptCache;
import no.haagensoftware.conticious.scriptcache.ScriptFile;
import no.haagensoftware.conticious.scriptcache.ScriptHash;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.net.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * A file server that can serve files from file system and class path.
 *
 * If you wish to customize the error message, please sub-class and override sendError().
 * Based on Trustin Lee's original file serving example
 */
public class FileServerHandler extends ContenticeHandler {
    private static final Logger logger = Logger.getLogger(FileServerHandler.class.getName());

    private static final String HTTP_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";

    protected String rootPath;

    private String stripFromUri;
    private int cacheMaxAge = -1;
    private boolean fromClasspath = false;
    private boolean isAdmin = false;
    private MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();

    private int maxCacheSeconds = 10;

    public FileServerHandler() {
        if (System.getProperty("no.haagensoftware.contentice.webappDir") != null && System.getProperty("no.haagensoftware.contentice.webappDir").length() > 3) {
            rootPath = System.getProperty("no.haagensoftware.contentice.webappDir");
        }
    }

    public void setFromClasspath(boolean fromClasspath) {
        this.fromClasspath = fromClasspath;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    protected String sanitizeUri(String uri) throws URISyntaxException {
        // Decode the path.
        try {
            uri = URLDecoder.decode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            try {
                uri = URLDecoder.decode(uri, "ISO-8859-1");
            } catch (UnsupportedEncodingException e1) {
                throw new Error();
            }
        }

        // Convert file separators.
        uri = uri.replace(File.separatorChar, '/');

        // Simplistic dumb security check.
        // You will have to do something serious in the production environment.
        if (uri.contains(File.separator + ".") ||
            uri.contains("." + File.separator) ||
            uri.startsWith(".") || uri.endsWith(".")) {
            return null;
        }

        QueryStringDecoder decoder = new QueryStringDecoder(uri);
        uri = decoder.path();

        if (uri.endsWith("/")) {
            uri += "index.html";
        } else if (uri.startsWith("/static/") && !uri.endsWith(".html")) {
            uri = uri + ".html";
        } else if (uri.startsWith("/static") && !uri.endsWith(".html")) {
            uri = uri + "/index.html";
        }

        return uri;
    }

    public static long getOpenFileDescriptorCount() {
        OperatingSystemMXBean osStats = ManagementFactory.getOperatingSystemMXBean();
        if(osStats instanceof UnixOperatingSystemMXBean) {
            return ((UnixOperatingSystemMXBean)osStats).getOpenFileDescriptorCount();
        }

        return 0;
    }
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        logger.info("FileServerHandler channelRead0: " + rootPath + " File handlers before: " + FileServerHandler.getOpenFileDescriptorCount());

        if (!fullHttpRequest.getDecoderResult().isSuccess()) {
            sendError(channelHandlerContext, HttpResponseStatus.BAD_REQUEST);
            return;
        }

        if (!isGet(fullHttpRequest)) {
            sendError(channelHandlerContext, HttpResponseStatus.METHOD_NOT_ALLOWED);
            return;
        }

        String uri = fullHttpRequest.getUri();
        if (stripFromUri != null) {
            uri = uri.replaceFirst(stripFromUri, "");
        }

        String path = sanitizeUri(uri);
        if (path == null) {
            sendError(channelHandlerContext, HttpResponseStatus.FORBIDDEN);
            return;
        }

        logger.info("path: " + path);

        logger.info("//FileServerHandler channelRead0: " + rootPath + " File handlers before: " + FileServerHandler.getOpenFileDescriptorCount());

        String fileContent = "";
        if (fromClasspath) {
            URL fileUrl = this.getClass().getResource(path);
            File file = new File(fileUrl.getPath());
            if (file.isDirectory()) {
                path = path + File.separatorChar + "index.html";
            }

            InputStream in = null;
            try {
                in = this.getClass().getResourceAsStream(path);
                fileContent = convertStreamToString(in);
            } finally {
                if (in != null) {
                    in.close();
                }
            }

            writeContentsToBuffer(channelHandlerContext, fileContent.toString(), ContentTypeUtil.getContentType(path));
        } else {
            String originalPath = path;

            String webappName = null;

            Domain domain = getDomain();

            if (domain == null && originalPath.startsWith("/admin")) {
                webappName = "admin";
            } else if (domain != null) {
                webappName = domain.getWebappName();
            }

            if (webappName == null) {
                write404DomainNotFoundToBuffer(channelHandlerContext);
            } else {
                if (webappName != null && !isAdmin) {
                    path = "/" + webappName + path;
                }

                File file = new File(this.rootPath + path);

                File staticFile = new File(rootPath + File.separatorChar + webappName + File.separatorChar + "static" + File.separatorChar + originalPath);

                if (!file.exists()) {
                    path = rootPath + File.separatorChar + webappName + File.separatorChar + "index.html";
                } else if (file.isDirectory()) {
                    path = this.rootPath + path + File.separatorChar + "index.html";
                } else {
                    path = this.rootPath + path;
                }

                String staticPath = staticFile.getAbsolutePath();

                if (staticFile.isDirectory() || !staticFile.exists()) {
                    staticPath = staticFile.getAbsolutePath() + ".html";
                }

                staticFile = new File(staticPath);

                logger.info("path: " + path);
                logger.info("original path: " + originalPath);
                logger.info("static path: " + staticPath);

                if (path.endsWith(".html")) {
                    ScriptCache cache = ScriptHash.getScriptCache(path);
                    if (cache == null || cache.isExpired()) {
                        //If file is not cached, or cache is expired, update the cache.
                        logger.info("Updating index.html from filesystem path: " + path);
                        cache = updateScriptCacheForPath(getDomain().getWebappName(), path);
                    }

                    String htmlContents = cache.getHtmlContents();

                    //Add static contents inside a NOSCRIPT tag
                    if (path.endsWith(".html") && staticFile.exists() && staticFile.isFile()) {
                        logger.info("Adding noscript tag");

                        Document htmlDocument = Jsoup.parse(htmlContents, "UTF-8");
                        Document staticDocument = parseHtmlPage(staticFile.getAbsolutePath());

                        logger.info(htmlDocument.toString());
                        logger.info("\n\n---\n\n");

                        Element noscript = htmlDocument.body().appendElement("noscript");

                        for (Element element : staticDocument.getElementsByTag("body").get(0).children()) {
                            noscript.appendChild(element);
                        }

                        htmlContents = htmlDocument.toString();
                    }

                    writeContentsToBuffer(channelHandlerContext, htmlContents, ContentTypeUtil.getContentType(path));
                } else {
                    writeFileToBuffer(channelHandlerContext, path, ContentTypeUtil.getContentType(path));
                }
            }
        }
    }

    private ScriptCache updateScriptCacheForPath(String host, String path) throws IOException {
        Long before = System.currentTimeMillis();
        List<ScriptFile> scriptPathList = new ArrayList<ScriptFile>();

        Document htmlDocument = parseHtmlPage(path);

        //extract out the JavaScript tags with src attribute and replace with a single
        //call to a cached minified script file
        if (htmlDocument != null) {
            Elements elements = htmlDocument.select("body");
            Element headElement = elements.get(0);

            Elements scriptElements = headElement.getElementsByTag("script");
            for (Element scriptElement : scriptElements) {
                String scriptSrc = scriptElement.attr("src");
                if (scriptSrc == null || scriptSrc.startsWith("http")) {
                    //Keep the script as-is
                } else if (scriptSrc != null && scriptSrc.endsWith(".js")) {
                    File minifiedScriptFile = new File(rootPath + File.separatorChar + host + File.separatorChar + scriptSrc.substring(0, scriptSrc.length() - 3) + "min.js");
                    String fileContent = null;

                    if (minifiedScriptFile != null && minifiedScriptFile.isFile()) {
                        fileContent = getFileContent(minifiedScriptFile.getAbsolutePath());
                    } else {
                        fileContent = getFileContent(rootPath + File.separatorChar +  host + File.separatorChar + scriptSrc);
                    }

                    //cache and remove this <script src tag from the DOM
                    if (fileContent != null) {
                        scriptPathList.add(new ScriptFile(scriptSrc, fileContent));
                    }
                    scriptElement.remove();
                }
            }

            //Append a new script element to the head-tag representing the cached and
            //minified script
            headElement.appendElement("script")
                    .attr("src", "/cachedScript" + path + ".js")
                    .attr("type", "text/javascript")
                    .attr("charset", "utf-8");
        }

        //Create or update the script contents for this HTML file path
        ScriptCache cache = ScriptHash.updateScriptContents(path, scriptPathList, htmlDocument.html(), System.currentTimeMillis() + (maxCacheSeconds * 1000));
        logger.info("Finished extracting script contents took: " + (System.currentTimeMillis() - before) + " ms.");

        return cache;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if (ctx.channel().isActive()) {
            sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static String convertStreamToString(java.io.InputStream is) {
        if (is != null) {
            java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
            return (s != null && s.hasNext()) ? s.next() : "";
        }

        return "";
    }

    protected String getFileContent(String path) {
        String fileContent = null;
        InputStream is;
        try {
            if (fromClasspath) {
                is = this.getClass().getResourceAsStream(path);
            } else {
                is = new FileInputStream(path);
            }

            if (is == null) {
                return null;
            }

            fileContent = convertStreamToString(is);

        } catch (IOException e) {
            return null;
        }

        return fileContent;
    }

    /**
     * Simple method for parsing the HTML contents
     *
     * @param path
     * @return
     * @throws IOException
     */
    private Document parseHtmlPage(String path) throws IOException {
        Document htmlDocument = null;

        File input = new File(path);
        if (input != null && input.exists() && input.isFile()) {
            htmlDocument = Jsoup.parse(input, "UTF-8");
        }
        return htmlDocument;
    }
}