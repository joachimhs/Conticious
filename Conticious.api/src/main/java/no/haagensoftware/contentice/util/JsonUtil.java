package no.haagensoftware.contentice.util;

import com.google.gson.*;
import no.haagensoftware.contentice.data.CategoryField;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

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

    public static List<CategoryField> convertJsonToDefaultFields(JsonElement jsonElement, String categoryId) {
        List<CategoryField> defaultFields = new ArrayList<>();

        for (JsonElement elem : jsonElement.getAsJsonArray()) {
            if (elem.isJsonObject()) {
                JsonObject elemObj = elem.getAsJsonObject();

                String fieldId = categoryId + "_" + elemObj.getAsJsonPrimitive("name").getAsString();
                if (elemObj.has("id")) {
                    fieldId = elemObj.get("id").getAsString();
                }
                if (elemObj.has("name") && elemObj.has("type") && elemObj.has("required")) {
                    CategoryField newField = new CategoryField(
                            fieldId,
                            elemObj.get("name").getAsString(),
                            elemObj.get("type").getAsString(),
                            elemObj.get("required").getAsBoolean()
                    );

                    if (elemObj.has("relation")) {
                        newField.setRelation(elemObj.get("relation").getAsString());
                    }

                    defaultFields.add(newField);
                }
            }
        }

        return defaultFields;
    }

    public static String convertCategoryFieldsToJsonString(List<CategoryField> categoryFields) {
        JsonArray fieldArray = new JsonArray();
        for (CategoryField cf : categoryFields) {
            fieldArray.add(new Gson().toJsonTree(cf));
        }
        return fieldArray.toString();
    }

    public static String convertKeyMapToJson(Map<String, JsonElement> keyMap) {
        JsonObject jsonObject = new JsonObject();

        for (String key : keyMap.keySet()) {
            jsonObject.add(key, keyMap.get(key));
        }

        return jsonObject.toString();
    }

    public static Map<String, JsonElement> buildKeysMapFromJsonObject(String jsonContent) {
        Map<String, JsonElement> keysMap = new HashMap<>();

        if (jsonContent != null) {
            JsonElement jsonElement = new JsonParser().parse(jsonContent);
            if (jsonElement.isJsonObject()) {
                extractJsonObject(keysMap, jsonElement);
            }
        }

        return keysMap;
    }

    public static void extractJsonObject(Map<String, JsonElement> keysMap, JsonElement jsonElement) {
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
}
