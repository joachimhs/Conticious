package no.haagensoftware.contentice.util;

import java.util.Hashtable;

public class ContentTypeUtil {
	private static Hashtable<String, String> contentTypeHash = new Hashtable<String, String>();
	
	static {
		contentTypeHash.put("png", "image/png");
		contentTypeHash.put("PNG", "image/png");
		contentTypeHash.put("txt", "text/plain; charset=UTF-8");
		contentTypeHash.put("text", "text/plain; charset=UTF-8");
		contentTypeHash.put("TXT", "text/plain; charset=UTF-8");
		contentTypeHash.put("js", "application/javascript; charset=UTF-8");
		contentTypeHash.put("jpg", "image/jpeg");
		contentTypeHash.put("jpeg", "image/jpeg");
		contentTypeHash.put("JPG", "image/jpeg");
		contentTypeHash.put("JPEG", "image/jpeg");
		contentTypeHash.put("css", "text/css; charset=UTF-8");
		contentTypeHash.put("CSS", "text/css; charset=UTF-8");
		contentTypeHash.put("json", "text/json; charset=UTF-8");
		contentTypeHash.put("html", "text/html; charset=UTF-8");
		contentTypeHash.put("htm", "text/html; charset=UTF-8");
		contentTypeHash.put("pdf", "application/pdf");
		contentTypeHash.put("PDF", "application/pdf");

	}
	
	public static String getContentType(String filename) {
        if (filename.contains("/static/")) {
            return contentTypeHash.get("html");
        }

		String fileEnding = filename.substring(filename.lastIndexOf(".")+1);
		if (contentTypeHash.get(fileEnding) != null) {
			return contentTypeHash.get(fileEnding);
		}
		
		return "text/plain; charset=UTF-8";
	}
}