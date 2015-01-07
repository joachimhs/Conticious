package no.haagensoftware.contentice.util;

import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;

/**
 * Created by jhsmbp on 31/12/14.
 */
public class ImageUploadProcessor {

    /**
     * Example of reading request by chunk and getting values from chunk to chunk
     */
    public static String storeUpload(HttpPostRequestDecoder decoder, String uploadPath) {
        String newFilename = "";
        try {
            while (decoder.hasNext()) {

                InterfaceHttpData data = decoder.next();
                if (data != null) {
                    try {
                        // new value
                        newFilename = ImageUploadProcessor.writeHttpData(uploadPath, data);
                    } finally {
                        data.release();
                    }
                }
            }
        } catch (HttpPostRequestDecoder.EndOfDataDecoderException e1) {

        }

        return newFilename;
    }

    private static String writeHttpData(String uploadPath, InterfaceHttpData data) {
        String newFilename = null;

        if (uploadPath != null && data.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload) {
            FileUpload fileUpload = (FileUpload) data;
            String fileending = getFileEnding(fileUpload);

            if (fileUpload.isCompleted() && fileending != null) {
                try {
                    String uuidFile = fileUpload.getFilename();

                    String path = uploadPath;

                    if (!Files.exists(FileSystems.getDefault().getPath(path))) {
                        Files.createDirectory(FileSystems.getDefault().getPath(path));
                    }

                    String filename = uploadPath + File.separatorChar + uuidFile;

                    fileUpload.renameTo(new File(filename));

                    if (Files.exists(FileSystems.getDefault().getPath(filename))) {
                        newFilename = uuidFile;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return newFilename;
    }

    private static String getFileEnding(FileUpload fileUpload) {
        String fileEnding = null;

        if (fileUpload.getContentType().equalsIgnoreCase("image/png")) {
            fileEnding = ".png";
        }

        if (fileUpload.getContentType().equalsIgnoreCase("image/jpg")) {
            fileEnding = ".jpg";
        }

        if (fileUpload.getContentType().equalsIgnoreCase("image/jpeg")) {
            fileEnding = ".jpg";
        }

        if (fileUpload.getContentType().equalsIgnoreCase("application/zip")) {
            fileEnding = ".zip";
        }

        return fileEnding;
    }
}
