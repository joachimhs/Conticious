package no.haagensoftware.contentice.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.apache.log4j.Logger;

public class FileUtil {
    private static final Logger logger = Logger.getLogger(FileUtil.class.getName());

    public static URL getUrlForResource(String resourceName) {
        URL resource = null;
        try {
            resource = FileUtil.class.getResource(resourceName);
            logger.info(resource.getPath());
        } catch (Exception e) {
            logger.info("fant ikke " + resourceName + ", pr/" + resourceName);
            resource = FileUtil.class.getResource("/" + resourceName);
            logger.info(resource.getPath());
        }
        logger.info("Got Resource: " + resource.getFile());
        return resource;
    }

    public static File getFileForResource(String resourceName) {
        File resource = null;
        String javaHome = System.getProperty("java.home");
        logger.info("sensemaps.embedded: " + System.getProperty("sensemaps.embedded"));
        if (System.getProperty("sensemaps.embedded") != null && javaHome.contains("Contents")) {
            int contentsIndex = javaHome.indexOf("Contents");
            resource = new File(javaHome.substring(0, contentsIndex) + "Contents/Java/" + resourceName);
            logger.info("resourceFile: " + resource.getAbsolutePath());
        } else if (System.getProperty("sensemaps.embedded") != null && javaHome.contains("runtime")) {
            int contentsIndex = javaHome.indexOf("runtime");
            resource = new File(javaHome.substring(0, contentsIndex) + "app" + File.separatorChar + resourceName);
            logger.info("resourceFile: " + resource.getAbsolutePath());
        } else {
            resource = new File(getUrlForResource(resourceName).getPath());
        }
        logger.info("Got Resource: " + resource.getAbsolutePath());
        return resource;
    }

    public static String getFilecontents(File inputFile) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader inStream = new BufferedReader(new FileReader(inputFile));
        String nextLine = inStream.readLine();
        while (nextLine != null) {
            sb.append(nextLine).append("\n");
            nextLine = inStream.readLine();
        }
        inStream.close();
        return sb.toString();
    }

    public static String getFileContents(String path) throws IOException {
        String returnString = null;
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            StringBuffer sb = new StringBuffer();
            List<String> allLines = Files.readAllLines(file.toPath(), StandardCharsets.ISO_8859_1);
            for (String line : allLines) {
                sb.append(line);
                sb.append("\n");
            }
            if (sb.length() > 0)
                returnString = sb.toString();
        }
        return returnString;
    }

    public static String getHexFileContent(String directory, String filename) {
        String fileContent = null;
        Path path = FileSystems.getDefault().getPath(directory, new String[] { filename });
        if (Files.isRegularFile(path, new java.nio.file.LinkOption[0]))
            try {
                fileContent = getFileContents(path.toAbsolutePath().toString());
            } catch (IOException e) {
                fileContent = null;
                e.printStackTrace();
            }
        return fileContent;
    }

    public static byte[] getFileByteContent(String path) throws IOException {
        byte[] allBytes;
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            allBytes = Files.readAllBytes(Paths.get(path, new String[0]));
        } else {
            allBytes = null;
        }
        return allBytes;
    }
}
