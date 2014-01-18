package no.haagensoftware.contentice.storage;

import com.google.gson.*;
import no.haagensoftware.contentice.data.CategoryData;
import no.haagensoftware.contentice.data.CategoryField;
import no.haagensoftware.contentice.data.FileData;
import no.haagensoftware.contentice.data.SubCategoryData;
import no.haagensoftware.contentice.spi.StoragePlugin;
import org.apache.log4j.Logger;

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
                    CategoryData categoryData = new CategoryData(p.getFileName().toString());
                    String json = getFileContents(documentsDirectory + File.separatorChar + p.getFileName() + ".json");
                    if (json != null) {
                        JsonElement jsonElement = new JsonParser().parse(json);
                        if (jsonElement.isJsonArray()) {
                            List<CategoryField> defaultFields = new ArrayList<>();

                            for (JsonElement elem : jsonElement.getAsJsonArray()) {
                                if (elem.isJsonObject()) {
                                    JsonObject elemObj = elem.getAsJsonObject();

                                    String fieldId = categoryData.getId() + "_" + elemObj.getAsJsonPrimitive("name").getAsString();
                                    if (elemObj.has("id")) {
                                        fieldId = elemObj.get("id").getAsString();
                                    }
                                    if (elemObj.has("name") && elemObj.has("type") && elemObj.has("required")) {
                                        defaultFields.add(new CategoryField(
                                                fieldId,
                                                elemObj.get("name").getAsString(),
                                                elemObj.get("type").getAsString(),
                                                elemObj.get("required").getAsBoolean()
                                        ));
                                    }

                                }
                            }
                            categoryData.setDefaultFields(defaultFields);
                        }
                    }
                    categories.add(categoryData);
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
                categoryData = currCategory;
                break;
            }
        }

        return categoryData;
    }

    @Override
    public void setCategory(String category, CategoryData categoryData) {
        Path path = FileSystems.getDefault().getPath(documentsDirectory + File.separatorChar + category);

        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        if (Files.exists(path)) {
            Path jsonPath = FileSystems.getDefault().getPath(documentsDirectory + File.separatorChar + category + ".json");

            JsonArray fieldArray = new JsonArray();
            for (CategoryField cf : categoryData.getDefaultFields()) {
                fieldArray.add(new Gson().toJsonTree(cf));
            }
            String jsonContent =  fieldArray.toString();

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
                subCategory = new SubCategoryData(category + "_" + filename.substring(0, filename.length() - 3));
                subCategory.setName(filename.substring(0, filename.length() - 3));
            } else if (filename.endsWith(".json")) {
                subCategory = new SubCategoryData(category + "_" + filename.substring(0, filename.length() - 5));
                subCategory.setName(filename.substring(0, filename.length() - 4));
            } else {
                subCategory = new SubCategoryData(category + "_" + filename);
                subCategory.setName(filename);
            }

            subCategory.setContent(getMarkdownContent(category, subCategory));
            subCategory.setKeyMap(getJsonContent(category, subCategory));
        }

        return subCategory;
    }

    private String getMarkdownContent(String category, SubCategoryData subCategory) throws IOException {
        return getFileContents(documentsDirectory + File.separatorChar + category + File.separatorChar + subCategory.getName() + ".md");
    }

    private Map<String, JsonElement> getJsonContent(String category, SubCategoryData subCategory) throws IOException {
        String jsonContent = getFileContents(documentsDirectory + File.separatorChar + category + File.separatorChar + subCategory.getName() + ".json");

        return buildKeysMapFromJsonObject(jsonContent);
    }

    private Map<String, JsonElement> buildKeysMapFromJsonObject(String jsonContent) {
        logger.info("Building Keymap for Json: " + jsonContent);
        Map<String, JsonElement> keysMap = new HashMap<>();

        if (jsonContent != null) {
            JsonElement jsonElement = new JsonParser().parse(jsonContent);
            if (jsonElement.isJsonObject()) {
                extractJsonObject(keysMap, jsonElement);
            }
        }

        return keysMap;
    }

    private Map<String, JsonElement> buildKeysMapFromJsonArray(String jsonContent) {
        Map<String, JsonElement> keysMap = new HashMap<>();

        JsonElement jsonElement = new JsonParser().parse(jsonContent);
        if (jsonElement.isJsonObject()) {
            extractJsonObject(keysMap, jsonElement);
        }

        return keysMap;
    }

    private void extractJsonObject(Map<String, JsonElement> keysMap, JsonElement jsonElement) {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        logger.info(jsonObject.toString());
        Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
        for (Map.Entry<String, JsonElement> entry : entrySet) {
            if (entry.getValue().isJsonArray()) {
                keysMap.put(entry.getKey(), entry.getValue().getAsJsonArray());
            } else {
                keysMap.put(entry.getKey(), entry.getValue());
            }
        }
    }

    private String getFileContents(String path) throws IOException {
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

    @Override
    public SubCategoryData getSubCategory(String category, String subCategory) {
        String useSubcategory = subCategory;

        if (useSubcategory.startsWith(category + "_")) {
            useSubcategory = useSubcategory.substring(9);
        }

        Path path = FileSystems.getDefault().getPath(documentsDirectory + File.separatorChar + category + File.separatorChar + useSubcategory + ".md");
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
            String markdownContent = "";
            if (subCategoryData != null && subCategoryData.getContent() != null && subCategoryData.getContent().length() > 0) {
                markdownContent = subCategoryData.getContent();
            }
            markdownWriter = Files.newBufferedWriter(markdownPath, Charset.forName("utf-8"));
            markdownWriter.write(markdownContent, 0, markdownContent.length());
            markdownWriter.flush();

            String jsonContent = convertKeyMapToJson(subCategoryData.getKeyMap());
            if (jsonContent.length() > 2) {
                jsonWriter = Files.newBufferedWriter(jsonPath, Charset.forName("utf-8"));
                jsonWriter.write(jsonContent, 0, jsonContent.length());
                jsonWriter.flush();
            }

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
