package no.haagensoftware.contentice.storage;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import junit.framework.Assert;
import no.haagensoftware.contentice.data.CategoryData;
import no.haagensoftware.contentice.data.CategoryField;
import no.haagensoftware.contentice.data.SubCategoryData;
import org.junit.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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

    @BeforeClass
    public static void setupDirectory() throws IOException {
        File contenticeDir = new File("/srv/contenticeDocumentsTest");
        if (contenticeDir == null || !contenticeDir.exists()) {
            contenticeDir.mkdirs();
        } else {
            //delete it and all its contents
            delete(contenticeDir);
            contenticeDir = new File("/srv/contenticeDocumentsTest");
            contenticeDir.mkdir();
        }
    }

    @AfterClass
    public static void teardownDirectory() throws  IOException {
        delete(new File("/srv/contenticeDocumentsTest"));
    }

    private static void delete(File f) throws IOException {
        if (f.isDirectory()) {
            for (File c : f.listFiles())
                delete(c);
        }
        if (!f.delete())
            throw new FileNotFoundException("Failed to delete file: " + f);
    }

    @Before
    public void setup() {
        System.setProperty("no.haagensoftware.contentice.storage.file.documentsDirectory", "/srv/contenticeDocumentsTest");
        plugin = new FileSystemStoragePlugin();


    }

    @Test
    public void verifyCreateAndDeleteCategory() {
        plugin.setCategory(null, "newCategory", new CategoryData("newCategory"));

        List<CategoryData> categories = plugin.getCategories(null);

        Assert.assertEquals("Expecting 1 Categories inside the document directory", 1, categories.size());
        Assert.assertEquals("Expecting the second category to be newCategory", "newCategory", categories.get(0).getId());

        plugin.deleteCategory(null, "newCategory");

        categories = plugin.getCategories(null);
        Assert.assertEquals("Expecting 0 Categories inside the document directory", 0, categories.size());
    }

    @Test
    public void verifyGetSubcategories() {
        plugin.setCategory(null, "pages", new CategoryData("Pages"));
        SubCategoryData about = new SubCategoryData();
        about.setContent("about");
        about.getKeyMap().put("name", new JsonPrimitive("About"));


        SubCategoryData opensource = new SubCategoryData();
        opensource.setContent("open source");
        opensource.getKeyMap().put("name", new JsonPrimitive("About"));

        plugin.setSubCategory(null, "pages", "about", about);
        plugin.setSubCategory(null, "pages", "opensource", opensource);

        List<SubCategoryData> subCategories = plugin.getSubCategories(null, "pages");

        Assert.assertEquals("Expecting 2 sub categories inside the pages category", 2, subCategories.size());
        Assert.assertEquals("Expecting the first sub category to be about", "pages_about", subCategories.get(0).getId());
        Assert.assertEquals("Expecting the second sub category to be opensource", "pages_opensource", subCategories.get(1).getId());
        Assert.assertTrue("Expecting the the about sub category to have a content", subCategories.get(0).getContent() != null && subCategories.get(0).getContent().length() > 0);
        Assert.assertTrue("Expecting the the opensource sub category to have a content", subCategories.get(1).getContent() != null && subCategories.get(1).getContent().length() > 0);
        Assert.assertTrue("Expecting the the about sub category to have a json keys", subCategories.get(0).getKeyMap() != null && subCategories.get(0).getKeyMap().size() > 0);
        Assert.assertTrue("Expecting the the opensource sub category to have a json keys", subCategories.get(1).getKeyMap() != null && subCategories.get(1).getKeyMap().size() > 0);
    }

    @Test
    public void verifyGetSubCategory() {
        SubCategoryData subCategory = plugin.getSubCategory(null, "pages", "about");

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

        plugin.setSubCategory(null, "pages", "test", subCategoryData);

        SubCategoryData subCategoryAfterInsert = plugin.getSubCategory(null, "pages", "test");
        System.out.println(subCategoryAfterInsert.getContent());

        Assert.assertTrue("Expecting the the about sub category to have a content", subCategoryAfterInsert.getContent() != null && subCategoryAfterInsert.getContent().equals("This is a test content\n"));
        Assert.assertTrue("Expecting the the about sub category to have 2 json keys", subCategoryAfterInsert.getKeyMap() != null && subCategoryAfterInsert.getKeyMap().size() == 2);


        plugin.deleteSubcategory(null, "pages", "test");

        SubCategoryData subCategoryDataAfterDelete = plugin.getSubCategory(null, "pages", "test");

        Assert.assertNull("Expecting Pages/Test to be deleted", subCategoryDataAfterDelete);
    }

    @Test
    public void verifyJsonArrayStorage() {
        CategoryData ticketCategory = new CategoryData();
        ticketCategory.getDefaultFields().add(new CategoryField("tickets", "tickets", "array", false));

        plugin.setCategory(null, "tickets", ticketCategory);

        SubCategoryData newTicket = new SubCategoryData();
        newTicket.setId("newTicketId");
        JsonArray ticketsArray = new JsonArray();
        ticketsArray.add(new JsonPrimitive("ticketOne"));
        ticketsArray.add(new JsonPrimitive("ticketTwo"));
        newTicket.getKeyMap().put("tickets", ticketsArray);

        plugin.setSubCategory(null, "tickets", newTicket.getId(), newTicket);

        SubCategoryData oldTicket = plugin.getSubCategory(null, "tickets", "newTicketId");

        Assert.assertEquals("tickets_newTicketId", oldTicket.getId());
        System.out.println(oldTicket.getListForKey("tickets").size());

        Assert.assertEquals(new Integer(2), new Integer(oldTicket.getListForKey("tickets").size()));
        Assert.assertEquals("ticketOne", oldTicket.getListForKey("tickets").get(0));
        Assert.assertEquals("ticketTwo", oldTicket.getListForKey("tickets").get(1));
    }

    @Test
    public void verifyJsonArrayAsStringStorage() {
        CategoryData ticketCategory = new CategoryData();
        ticketCategory.getDefaultFields().add(new CategoryField("tickets", "tickets", "array", false));

        plugin.setCategory(null, "tickets", ticketCategory);

        SubCategoryData newTicket = new SubCategoryData();
        newTicket.setId("secondTicketId");
        newTicket.getKeyMap().put("tickets", new JsonPrimitive("ticketOne,ticketTwo"));

        plugin.setSubCategory(null, "tickets", newTicket.getId(), newTicket);

        SubCategoryData oldTicket = plugin.getSubCategory(null, "tickets", "secondTicketId");

        Assert.assertEquals("tickets_secondTicketId", oldTicket.getId());
        System.out.println(oldTicket.getListForKey("tickets").size());

        Assert.assertEquals(new Integer(2), new Integer(oldTicket.getListForKey("tickets", ",").size()));
        Assert.assertEquals("ticketOne", oldTicket.getListForKey("tickets", ",").get(0));
        Assert.assertEquals("ticketTwo", oldTicket.getListForKey("tickets", ",").get(1));
    }
}
