package no.haagensoftware.conticious.plugin.storage;

import com.datastax.driver.core.*;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import no.haagensoftware.contentice.data.CategoryData;
import no.haagensoftware.contentice.data.CategoryField;
import no.haagensoftware.contentice.data.FileData;
import no.haagensoftware.contentice.data.SubCategoryData;
import no.haagensoftware.contentice.spi.StoragePlugin;
import no.haagensoftware.contentice.util.IntegerParser;
import no.haagensoftware.conticious.plugin.storage.dao.CassandraCategoryDao;
import no.haagensoftware.conticious.plugin.storage.data.CassandraCategoryData;
import no.haagensoftware.conticious.plugin.storage.data.CassandraSubCategoryData;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by joachimhaagenskeie on 19/02/2017.
 */
public class CassandraStoragePlugin extends StoragePlugin {
    private Cluster cluster;
    private Session session;
    private CassandraCategoryDao cassandraCategoryDao;

    public CassandraStoragePlugin() {
    }

    @Override
    public void setup() {
        //Setting up the Cassandra environment, session and DAOs
        cluster = null;

        String hostString = System.getProperty("no.haagensoftware.conticious.db.cassandra.hosts", "127.0.0.1");

        Cluster.Builder clusterBuilder = Cluster.builder();
        if (hostString.contains(",")) {
            for (String host : hostString.split(",")) {
                clusterBuilder.addContactPoint(host.trim());
            }
        } else {
            clusterBuilder.addContactPoint(hostString.trim());
        }

        cluster = clusterBuilder.build();
        Metadata metadata = cluster.getMetadata();

        System.out.println("Connected to cluster: " + metadata.getClusterName());
        for (Host host : metadata.getAllHosts()) {
            System.out.printf("Datacenter: %s; Host: %s; Rack: %s;", host.getDatacenter(), host.getAddress(), host.getRack());
        }

        session = cluster.connect();
        this.initializeDb();

        cassandraCategoryDao = new CassandraCategoryDao(session);

        ResultSet rs = session.execute("select release_version from system.local");
        Row row = rs.one();
        System.out.println(row.getString("release_version"));
    }

    public void teardown() {
        if (cluster != null) cluster.close();
    }

    private void initializeDb() {
        Integer replication = IntegerParser.parseIntegerFromString(System.getProperty("no.haagensoftware.conticious.db.cassandra.replicationFactor"), 1);

        session.execute("CREATE KEYSPACE IF NOT EXISTS conticious WITH replication = {'class': 'SimpleStrategy', 'replication_factor': " + replication.intValue() + "};");
    }

    public static void main(String[] args) {
        CassandraStoragePlugin plugin = new CassandraStoragePlugin();
        plugin.setup();

        CategoryData cd1 = new CategoryData();
        cd1.setId("category1");
        cd1.setPublic(false);

        CategoryField cf1 = new CategoryField();
        cf1.setId("cf1");
        cf1.setName("cf1");
        cf1.setRequired(false);
        cf1.setType("textfield");

        List<CategoryField> categoryFieldList = new ArrayList<>();
        categoryFieldList.add(cf1);

        cd1.setDefaultFields(categoryFieldList);

        cd1.setNumberOfSubcategories(0);

        SubCategoryData scd1 = new SubCategoryData();
        scd1.setId("category1_subcategory1");
        scd1.setName("subcategory1");
        scd1.setContent("Markdown Content");

        Map<String, JsonElement> keyMap = new HashMap<>();
        keyMap.put("TestProp", new JsonPrimitive("testVal"));
        keyMap.put("TestProp2", new JsonPrimitive(true));
        keyMap.put("TestProp3", new JsonPrimitive(123));

        scd1.setKeyMap(keyMap);
        cd1.addSubcategory(scd1);

        plugin.setCategory("mywebsite.com", "category1", cd1);
        plugin.setSubCategory("mywebsite.com", "category1", "category1_subcategory!", scd1);

        List<CategoryData> categoryDataList = plugin.getCategories("mywebsite.com");
        System.out.println(new Gson().toJson(categoryDataList));

    }

    @Override
    public List<String> getPluginDependencies() {
        return null;
    }

    @Override
    public String getPluginName() {
        return "CassandraStoragePlugin";
    }

    @Override
    public List<CategoryData> getCategories(String host) {
        List<CategoryData> categoryDataList = new ArrayList<>();

        //Convert between Cassandra data type and Conticious data type
        for (CassandraCategoryData cassandraCategoryData : cassandraCategoryDao.getCategories(host)) {
            CategoryData cd = cassandraCategoryData.toCategoryData();

            for (String subCatId : cassandraCategoryData.getSubcategories()) {
                cd.addSubcategory(this.getSubCategory(host, cassandraCategoryData.getName(), subCatId));
            }

            categoryDataList.add(cd);
        }

        return categoryDataList;
    }


    @Override
    public CategoryData getCategory(String host, String category) {
        CategoryData cd = null;

        //Convert between Cassandra data type and Conticious data type
        CassandraCategoryData ccd = cassandraCategoryDao.getCategory(category, host);
        if (ccd != null) {
            cd = ccd.toCategoryData();
        }

        return cd;
    }

    @Override
    public void setCategory(String host, String category, CategoryData categoryData) {
        cassandraCategoryDao.persistCategory(categoryData, host);
    }

    @Override
    public Integer getNumberOfSubcategories(String host, String category) {
        int num = 0;

        CassandraCategoryData ccd = cassandraCategoryDao.getCategory(host, category);
        if (ccd != null) {
            num = ccd.getSubcategories() != null ? ccd.getSubcategories().size() : 0;
        }

        return num;
    }

    @Override
    public List<SubCategoryData> getSubCategories(String host, String category) {
        List<SubCategoryData> subCategoryDatas = new ArrayList<>();

        for (CassandraSubCategoryData scd : cassandraCategoryDao.getSubcategories(category, host)) {
                subCategoryDatas.add(scd.toSubCategoryData());
        }

        return subCategoryDatas;
    }

    @Override
    public SubCategoryData getSubCategory(String host, String category, String subCategory) {
        return cassandraCategoryDao.getSubcategory(subCategory, host).toSubCategoryData();
    }


    @Override
    public void setSubCategory(String host, String category, String subCategory, SubCategoryData subCategoryData) {
        cassandraCategoryDao.persistSubcategory(subCategoryData, host);
    }

    @Override
    public void uploadFile(File file, FileData fileData) {

    }

    @Override
    public List<FileData> getFiles() {
        return null;
    }

    @Override
    public FileData getFileData(String filename) {
        return null;
    }

    @Override
    public void deleteCategory(String host, String category) {

    }

    @Override
    public void deleteSubcategory(String host, String category, String subCategory) {

    }
}
