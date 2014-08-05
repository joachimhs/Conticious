package no.haagensoftware.conticious.scriptcache;

import java.io.*;

/**
 * Created by jhsmbp on 06/05/14.
 */
public class ScriptFile {
    private String fileSrc;
    private String fileContents;
    private String fileMinifiedContents;

    public ScriptFile(String fileSrc, String fileContents) {
        super();
        this.fileSrc = fileSrc;
        this.fileContents = fileContents;
        this.fileContents = this.fileContents.replaceAll("Âµ", "mu");
        this.fileContents = this.fileContents.replaceAll("µ", "mu");
        this.fileContents = this.fileContents.replaceAll("Ï�", "sigma");
        this.fileContents = this.fileContents.replaceAll("�", "sigma");
        this.minifyScript();
    }

    public String getFileSrc() {
        return fileSrc;
    }

    public void setFileSrc(String fileSrc) {
        this.fileSrc = fileSrc;
    }

    public String getFileContents() {
        return fileContents;
    }

    public void setFileContents(String fileContents) {
        this.fileContents = fileContents;
        this.minifyScript();
    }

    public String getFileMinifiedContents() {
        return fileMinifiedContents;
    }

    private void minifyScript() {
        if (fileSrc.endsWith("min.js")) {
            fileMinifiedContents = fileContents;
        } else {
            InputStream in;
            try {
                in = new ByteArrayInputStream(this.getFileContents().getBytes("UTF-8"));
                OutputStream out = new ByteArrayOutputStream();

                JsMin jsmin = new JsMin(in, out);
                jsmin.jsmin();

                fileMinifiedContents = new String(out.toString());
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                fileMinifiedContents = fileContents;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                fileMinifiedContents = fileContents;
            } catch (JsMin.UnterminatedRegExpLiteralException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                fileMinifiedContents = fileContents;
            } catch (JsMin.UnterminatedCommentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                fileMinifiedContents = fileContents;
            } catch (JsMin.UnterminatedStringLiteralException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                fileMinifiedContents = fileContents;
            }
        }
    }
}
