package no.haagensoftware.contentice.storage;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import junit.framework.Assert;
import no.haagensoftware.contentice.data.CategoryData;
import no.haagensoftware.contentice.data.SubCategoryData;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jhsmbp
 * Date: 11/16/13
 * Time: 10:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class FileSystemStoragePluginTest {
    private FileSystemStoragePlugin plugin;

    @Before
    public void setup() {
        System.setProperty("no.haagensoftware.contentice.storage.file.documentsDirectory", "/srv/contenticeDocuments");
        plugin = new FileSystemStoragePlugin();
    }

    @Test
    public void verifyDocumentRootListing() {
        List<CategoryData> categories = plugin.getCategories();
        Assert.assertEquals("Expecting 2 Categories inside the document directory", 2, categories.size());
        Assert.assertEquals("Expecting the first category to be blogPosts", "blogPosts", categories.get(0).getId());
        Assert.assertEquals("Expecting the second category to be pages", "pages", categories.get(1).getId());
    }

    @Test
    public void verifyCreateAndDeleteCategory() {
        plugin.setCategory("newCategory", new CategoryData("newCategory"));

        List<CategoryData> categories = plugin.getCategories();

        Assert.assertEquals("Expecting 3 Categories inside the document directory", 3, categories.size());
        Assert.assertEquals("Expecting the first category to be blogPosts", "blogPosts", categories.get(0).getId());
        Assert.assertEquals("Expecting the second category to be newCategory", "newCategory", categories.get(1).getId());
        Assert.assertEquals("Expecting the third category to be pages", "pages", categories.get(2).getId());

        plugin.deleteCategory("newCategory");

        categories = plugin.getCategories();
        Assert.assertEquals("Expecting 2 Categories inside the document directory", 2, categories.size());
        Assert.assertEquals("Expecting the first category to be blogPosts", "blogPosts", categories.get(0).getId());
        Assert.assertEquals("Expecting the second category to be pages", "pages", categories.get(1).getId());
    }

    @Test
    public void verifyGetSubcategories() {
        List<SubCategoryData> subCategories = plugin.getSubCategories("pages");

        Assert.assertEquals("Expecting 2 sub categories inside the pages category", 2, subCategories.size());
        Assert.assertEquals("Expecting the first sub category to be about", "about", subCategories.get(0).getId());
        Assert.assertEquals("Expecting the second sub category to be opensource", "opensource", subCategories.get(1).getId());
        Assert.assertTrue("Expecting the the about sub category to have a content", subCategories.get(0).getContent() != null && subCategories.get(0).getContent().length() > 0);
        Assert.assertTrue("Expecting the the opensource sub category to have a content", subCategories.get(1).getContent() != null && subCategories.get(1).getContent().length() > 0);
        Assert.assertTrue("Expecting the the about sub category to have a json keys", subCategories.get(0).getKeyMap() != null && subCategories.get(0).getKeyMap().size() > 0);
        Assert.assertTrue("Expecting the the opensource sub category to have a json keys", subCategories.get(1).getKeyMap() != null && subCategories.get(1).getKeyMap().size() > 0);
    }

    @Test
    public void verifyGetSubCategory() {
        SubCategoryData subCategory = plugin.getSubCategory("pages", "about");

        Assert.assertTrue("Expecting the the about sub category to have a content", subCategory.getContent() != null && subCategory.getContent().length() > 0);
        Assert.assertTrue("Expecting the the about sub category to have a json keys", subCategory.getKeyMap() != null && subCategory.getKeyMap().size() > 0);
    }

    @Test
    public void setSubCategory() {
        SubCategoryData subCategoryData = new SubCategoryData("test");
        subCategoryData.setContent("This is a test content");
        Map<String, JsonElement> keyMap = new HashMap<>();
        keyMap.put("pageName", new JsonPrimitive("testPage"));
        keyMap.put("pageTitle", new JsonPrimitive("Test Title Page"));
        subCategoryData.setKeyMap(keyMap);

        plugin.setSubCategory("pages", "test", subCategoryData);

        SubCategoryData subCategoryAfterInsert = plugin.getSubCategory("pages", "test");
        System.out.println(subCategoryAfterInsert.getContent());

        Assert.assertTrue("Expecting the the about sub category to have a content", subCategoryAfterInsert.getContent() != null && subCategoryAfterInsert.getContent().equals("This is a test content\n"));
        Assert.assertTrue("Expecting the the about sub category to have 2 json keys", subCategoryAfterInsert.getKeyMap() != null && subCategoryAfterInsert.getKeyMap().size() == 2);


        plugin.deleteSubcategory("pages", "test");

        SubCategoryData subCategoryDataAfterDelete = plugin.getSubCategory("pages", "test");

        Assert.assertNull("Expecting Pages/Test to be deleted", subCategoryDataAfterDelete);
    }
}
