package no.haagensoftware.contentice.storage;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import no.haagensoftware.contentice.data.CategoryData;
import no.haagensoftware.contentice.data.FileData;
import no.haagensoftware.contentice.data.SubCategoryData;
import no.haagensoftware.contentice.spi.StoragePlugin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: jhsmbp
 * Date: 11/16/13
 * Time: 10:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class FileSystemStoragePlugin extends StoragePlugin {
    private static final Logger logger = Logger.getLogger(FileSystemStoragePlugin.class.getName());
    private String documentsDirectory;

    @Override
    public String getStoragePluginName() {
        return "FileSystemStoragePlugin";
    }

    public FileSystemStoragePlugin() {
        String docDirectory = System.getProperty("no.haagensoftware.contentice.storage.file.documentsDirectory");

        if (docDirectory == null) {
            throw new RuntimeException("no.haagensoftware.contentice.storage.file.documentsDirectory is not set. Configure in Contentice's config.properties file");
        } else {
            File documentsDirectory = new File(docDirectory);
            if (!documentsDirectory.exists() && !documentsDirectory.isDirectory()) {
                throw new RuntimeException(docDirectory + " does not exist or is not a directory Contentice have access to");
            }
        }

        //if we got here, we have a valid documents directory
        this.documentsDirectory = docDirectory;
    }

    @Override
    public List<CategoryData> getCategories() {
        List<CategoryData> categories = new ArrayList<>();

        try (DirectoryStream<Path> ds = Files.newDirectoryStream(FileSystems.getDefault().getPath(documentsDirectory))) {
            for (Path p : ds) {
                if (Files.isDirectory(p)) {
                    // Iterate over the paths in the directory and print filenames
                    categories.add(new CategoryData(p.getFileName().toString()));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return categories;
    }

    @Override
    public CategoryData getCategory(String category) {
        CategoryData categoryData = null;

        for (CategoryData currCategory : getCategories()) {
            if (currCategory.getId().equals(category)) {
                categoryData = new CategoryData(currCategory.getId());
                break;
            }
        }

        return categoryData;
    }

    @Override
    public void setCategory(String category, CategoryData categoryData) {
        Path path = FileSystems.getDefault().getPath(documentsDirectory + File.separatorChar + category);

        if (Files.exists(path)) {
            //Category exists, nothing to do
        } else {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    @Override
    public void deleteCategory(String category) {
        Path path = FileSystems.getDefault().getPath(documentsDirectory + File.separatorChar + category);

        if (Files.exists(path)) {
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    @Override
    public List<SubCategoryData> getSubCategories(String category) {
        List<SubCategoryData> subCategories = new ArrayList<>();

        try (DirectoryStream<Path> ds = Files.newDirectoryStream(FileSystems.getDefault().getPath(documentsDirectory + File.separatorChar + category), "*.md")) {
            for (Path p : ds) {
                subCategories.add(getSubCategoryData(category, subCategories, p));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return subCategories;
    }

    private SubCategoryData getSubCategoryData(String category, List<SubCategoryData> subCategories, Path p) throws IOException {
        SubCategoryData subCategory = null;

        if (Files.isRegularFile(p)) {
            // Iterate over the paths in the directory and print filenames
            String filename = p.getFileName().toString();
            if (filename.endsWith(".md")) {
                subCategory = new SubCategoryData(filename.substring(0, filename.length() - 3));
            } else if (filename.endsWith(".json")) {
                subCategory = new SubCategoryData(filename.substring(0, filename.length() - 5));
            } else {
                subCategory = new SubCategoryData(filename);
            }

            subCategory.setContent(getMarkdownContent(category, subCategory));
            subCategory.setKeyMap(getJsonContent(category, subCategory));
        }

        return subCategory;
    }

    private String getMarkdownContent(String category, SubCategoryData subCategory) throws IOException {
        StringBuffer sb = new StringBuffer();
        BufferedReader fileBufferedReader = Files.newBufferedReader((FileSystems.getDefault().getPath(documentsDirectory + File.separatorChar + category + File.separatorChar + subCategory.getId() + ".md")), Charset.forName("utf-8"));
        String line = null;
        while ((line = fileBufferedReader.readLine()) != null) {
            sb.append(line).append("\n");
        }

        return sb.toString();
    }

    private Map<String, JsonElement> getJsonContent(String category, SubCategoryData subCategory) throws IOException {
        Map<String, JsonElement> keysMap = new HashMap<>();

        BufferedReader fileBufferedReader = Files.newBufferedReader((FileSystems.getDefault().getPath(documentsDirectory + File.separatorChar + category + File.separatorChar + subCategory.getId() + ".json")), Charset.forName("utf-8"));
        StringBuffer sb = new StringBuffer();
        String line = null;
        while ((line = fileBufferedReader.readLine()) != null) {
            sb.append(line).append("\n");
        }

        JsonElement jsonElement = new JsonParser().parse(sb.toString());
        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
            for (Map.Entry<String, JsonElement> entry : entrySet) {
                if (entry.getValue().isJsonArray()) {
                    keysMap.put(entry.getKey(), entry.getValue().getAsJsonArray());
                } else {
                    keysMap.put(entry.getKey(), entry.getValue());
                }
            }
        }

        return keysMap;
    }

    @Override
    public SubCategoryData getSubCategory(String category, String subCategory) {
        Path path = FileSystems.getDefault().getPath(documentsDirectory + File.separatorChar + category + File.separatorChar + subCategory + ".md");
        SubCategoryData subCategoryData = null;
        try {
            subCategoryData = this.getSubCategoryData(category, getSubCategories(category), path);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return subCategoryData;
    }

    @Override
    public void setSubCategory(String category, String subCategory, SubCategoryData subCategoryData) {
        Path markdownPath = FileSystems.getDefault().getPath(documentsDirectory + File.separatorChar + category + File.separatorChar + subCategory + ".md");
        Path jsonPath = FileSystems.getDefault().getPath(documentsDirectory + File.separatorChar + category + File.separatorChar + subCategory + ".json");

        BufferedWriter markdownWriter = null;
        BufferedWriter jsonWriter = null;
        try {
            markdownWriter = Files.newBufferedWriter(markdownPath, Charset.forName("utf-8"));
            markdownWriter.write(subCategoryData.getContent(), 0, subCategoryData.getContent().length());
            markdownWriter.flush();


            String jsonContent = convertKeyMapToJson(subCategoryData.getKeyMap());
            jsonWriter = Files.newBufferedWriter(jsonPath, Charset.forName("utf-8"));
            jsonWriter.write(jsonContent, 0, jsonContent.length());
            jsonWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            if (markdownWriter != null) {
                try {
                    markdownWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }

            if (jsonWriter != null) {
                try {
                    jsonWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
    }

    private String convertKeyMapToJson(Map<String, JsonElement> keyMap) {
        JsonObject jsonObject = new JsonObject();

        for (String key : keyMap.keySet()) {
            jsonObject.add(key, keyMap.get(key));
        }

        return jsonObject.toString();
    }

    @Override
    public void deleteSubcategory(String category, String subCategory) {
        Path markdownPath = FileSystems.getDefault().getPath(documentsDirectory + File.separatorChar + category + File.separatorChar + subCategory + ".md");
        Path jsonPath = FileSystems.getDefault().getPath(documentsDirectory + File.separatorChar + category + File.separatorChar + subCategory + ".json");

        try {
            Files.deleteIfExists(markdownPath);
            Files.deleteIfExists(jsonPath);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public void uploadFile(File file, FileData fileData) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<FileData> getFiles() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public FileData getFileData(String filename) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
