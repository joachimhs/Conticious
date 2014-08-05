package no.haagensoftware.contentice.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by jhsmbp on 18/04/14.
 */
public class JsonUtil {

    public static void writeJsonToFile(Path jsonPath, String jsonContent) {
        BufferedWriter jsonWriter = null;
        try {
            jsonWriter = Files.newBufferedWriter(jsonPath, Charset.forName("utf-8"));
            jsonWriter.write(jsonContent, 0, jsonContent.length());
            jsonWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            if (jsonWriter != null) {
                try {
                    jsonWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getFileContents(String path) throws IOException {
        String returnString = null;
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            BufferedReader fileBufferedReader = null;
            try {
                fileBufferedReader = Files.newBufferedReader((FileSystems.getDefault().getPath(path)), Charset.forName("utf-8"));

                StringBuffer sb = new StringBuffer();
                String line = null;
                while ((line = fileBufferedReader.readLine()) != null) {
                    sb.append(line).append("\n");
                }

                if (sb.length() > 0) {
                    returnString = sb.toString();
                }
            } finally {
                if (fileBufferedReader != null) {
                    fileBufferedReader.close();
                }
            }
        }

        return returnString;
    }
}
