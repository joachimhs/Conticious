package nohaagensoftware.cmpiapp.plugin;

import no.haagensoftware.cmpiapp.data.CategoryData;
import no.haagensoftware.cmpiapp.data.FileData;
import no.haagensoftware.cmpiapp.data.SubCategoryData;

import java.io.File;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: joahaage
 * Date: 15.11.13
 * Time: 12:09
 * To change this template use File | Settings | File Templates.
 */
public interface StoragePlugin {

    public List<CategoryData> getCategories();

    public void setCategory(String category, CategoryData categoryData);

    public List<SubCategoryData> getSubCategories(String category);

    public SubCategoryData getSubCategory(String category, String subCategory);

    public void setSubCategory(String category, String subCategory, SubCategoryData subCategoryData);

    public void uploadFile(File file, FileData fileData);

    public List<FileData> getFiles();

    public FileData getFileData(String filename);
}
