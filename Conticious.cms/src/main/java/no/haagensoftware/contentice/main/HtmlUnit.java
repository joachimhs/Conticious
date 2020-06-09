package no.haagensoftware.contentice.main;


import no.haagensoftware.contentice.util.FileUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by jhsmbp on 03/05/14.
 */
public class HtmlUnit {
    private String docString = null;



    public static void main(String[] args) throws IOException, InterruptedException {
        File phantomScript = FileUtil.getFileForResource("phantomscript.js");
        System.out.println("phantom script: " + phantomScript.getAbsolutePath());

        StringBuilder sb = new StringBuilder();
        sb.append("/usr/local/phantomjs/bin/phantomjs ");
        sb.append(phantomScript.getAbsolutePath()).append(" ");
        sb.append("http://dev.filmpower.no:8085 ");
        sb.append("2 ");
        sb.append("/Users/jhsmbp/Projects/filmpower/static ");
        sb.append("/Users/jhsmbp/Projects/filmpower/Sitemap.xml ");
        sb.append("dev.filmpower.no:8085 ");
        sb.append("filmpower.no ");
        sb.append("3 ");

        System.out.println("Executing phantomjs with command: \n" + sb.toString());

        Process process = Runtime.getRuntime().exec(sb.toString());
        int exitStatus = process.waitFor();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String currentLine=null;
        StringBuilder stringBuilder = new StringBuilder(exitStatus==0?"SUCCESS:":"ERROR:");
        currentLine= bufferedReader.readLine();
        while(currentLine !=null)
        {
            stringBuilder.append(currentLine);
            currentLine = bufferedReader.readLine();
        }
        System.out.println(stringBuilder.toString());
    }
}