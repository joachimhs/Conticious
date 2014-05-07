package no.haagensoftware.contentice.util;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.*;

/**
 * Created by jhsmbp on 04/05/14.
 */
public class PhantomJsExecutor {
    private static final Logger logger = Logger.getLogger(PhantomJsExecutor.class.getName());

    public static String executePhantomJs(String url, String pathToStaticDir, String pathToSitemapFile, String partOfUrlToReplace, String replacementString) {
        String commandResult = null;

        String phantomPath = null;
        try {
            phantomPath = getPhantomPath();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        File phantomScript = FileUtil.getFileForResource("phantomscript.js");

        URL phantomUrl = FileUtil.getUrlForResource("phantomscript.js");
        File dest = new File("/tmp/conticiousPhantomScript.js");

        try {
            FileUtils.copyURLToFile(phantomUrl, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("phantom script: " + dest.getAbsolutePath());
        logger.info("phantom path: " + phantomPath);
        if (phantomPath != null && dest != null && dest.exists() && dest.isFile()) {
            StringBuilder sb = new StringBuilder();
            sb.append(phantomPath).append(" ");
            sb.append(dest.getAbsolutePath()).append(" ");
            sb.append(url).append(" ");
            sb.append("2 ");
            sb.append(pathToStaticDir).append(" ");
            sb.append(pathToSitemapFile).append(" ");
            sb.append(partOfUrlToReplace).append(" ");
            sb.append(replacementString).append(" ");
            sb.append("3 ");

            logger.info("Executing phantomjs with command: \n" + sb.toString());

            try {
                commandResult = executeCommandAndReturnResult(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return commandResult;
    }

    private static String executeCommandAndReturnResult(String command) throws IOException, InterruptedException {
        String returnMessage = null;

        Process process = Runtime.getRuntime().exec(command);
        int exitStatus = process.waitFor();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String currentLine=null;

        StringBuilder stringBuilder = new StringBuilder();
        if (exitStatus == 0) {
            currentLine= bufferedReader.readLine();
            while(currentLine !=null)
            {
                stringBuilder.append(currentLine);
                currentLine = bufferedReader.readLine();
            }

            returnMessage = stringBuilder.toString();
        }

        return returnMessage;
    }

    private static String getPhantomPath() throws IOException, InterruptedException {
        return executeCommandAndReturnResult("which phantomjs");
    }
}
