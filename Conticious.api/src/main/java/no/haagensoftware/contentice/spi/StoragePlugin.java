package no.haagensoftware.contentice.spi;

import no.haagensoftware.contentice.data.CategoryData;
import no.haagensoftware.contentice.data.FileData;
import no.haagensoftware.contentice.data.SubCategoryData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: joahaage
 * Date: 15.11.13
 * Time: 12:09
 * To change this template use File | Settings | File Templates.
 */
public abstract class StoragePlugin implements ConticiousPlugin {
    public List<ConticiousPlugin> dependantPluginsList = new ArrayList<>();

    @Override
    public void addPlugin(ConticiousPlugin plugin) {
        dependantPluginsList.add(plugin);
    }

    public abstract List<CategoryData> getCategories(String host);

    public abstract CategoryData getCategory(String host, String category);

    public abstract void setCategory(String host, String category, CategoryData categoryData);

    public abstract List<SubCategoryData> getSubCategories(String host, String category);

    public abstract SubCategoryData getSubCategory(String host, String category, String subCategory);

    public abstract void setSubCategory(String host, String category, String subCategory, SubCategoryData subCategoryData);

    public abstract void uploadFile(File file, FileData fileData);

    public abstract List<FileData> getFiles();

    public abstract FileData getFileData(String filename);

    public abstract void deleteCategory(String host, String category);

    public abstract void deleteSubcategory(String host, String category, String subCategory);
}
