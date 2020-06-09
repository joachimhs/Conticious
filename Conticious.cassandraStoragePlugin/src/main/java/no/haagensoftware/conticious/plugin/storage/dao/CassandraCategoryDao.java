package no.haagensoftware.conticious.plugin.storage.dao;

import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import no.haagensoftware.contentice.data.CategoryData;
import no.haagensoftware.contentice.data.SubCategoryData;
import no.haagensoftware.conticious.plugin.storage.data.CassandraCategoryData;
import no.haagensoftware.conticious.plugin.storage.data.CassandraSubCategoryData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joachimhaagenskeie on 19/02/2017.
 */
public class CassandraCategoryDao {
    private Session session;

    public CassandraCategoryDao(Session session) {
        this.session = session;

        this.initializeTable();
    }

    private void initializeTable() {
        session.execute("CREATE TABLE IF NOT EXISTS conticious.categories (" +
                "name text, " +
                "host text, " +
                "fieldsJson text, " +
                "categoryIsPublic boolean, " +
                "subcategories list<text>, " +
                "PRIMARY KEY (name, host)" +
                ");");

        session.execute("CREATE INDEX IF NOT EXISTS categories_host_idx ON conticious.categories(host);");

        session.execute("CREATE TABLE IF NOT EXISTS conticious.subcategories (" +
                "id text, " +
                "category text, " +
                "host text, " +
                "name text, " +
                "fieldsjson text, " +
                "markdowncontent text, " +
                "PRIMARY KEY (id, category, host)" +
                ");");

        session.execute("CREATE INDEX IF NOT EXISTS subcategories_host_idx ON conticious.subcategories(host);");
    }

    public List<CassandraCategoryData> getCategories(String host) {
        List<CassandraCategoryData> cassandraCategoryDatas = new ArrayList<>();

        CassandraAccessor categoryAccessor = new MappingManager(session).createAccessor(CassandraAccessor.class);
        Result<CassandraCategoryData> categoryDataResult = categoryAccessor.getCategories(host);

        for (CassandraCategoryData cd : categoryDataResult.all()) {
            cassandraCategoryDatas.add(cd);
        }

        return cassandraCategoryDatas;
    }

    public CassandraCategoryData getCategory(String name, String host) {
        CassandraCategoryData cassandraCategoryData = null;

        Mapper<CassandraCategoryData> mapper = new MappingManager(session).mapper(CassandraCategoryData.class);
        cassandraCategoryData = mapper.get(name, host);

        return cassandraCategoryData;
    }

    public void persistCategory(CategoryData categoryData, String host) {
        Mapper<CassandraCategoryData> mapper = new MappingManager(session).mapper(CassandraCategoryData.class);
        mapper.save(new CassandraCategoryData(categoryData, host));
    }

    public List<CassandraSubCategoryData> getSubcategories(String category, String host) {
        List<CassandraSubCategoryData> cassandraSubCategoryDatas = new ArrayList<>();

        CassandraAccessor categoryAccessor = new MappingManager(session).createAccessor(CassandraAccessor.class);
        Result<CassandraSubCategoryData> categoryDataResult = categoryAccessor.getSubCategories(category, host);

        for (CassandraSubCategoryData scd : categoryDataResult.all()) {
            cassandraSubCategoryDatas.add(scd);
        }

        return cassandraSubCategoryDatas;
    }

    public CassandraSubCategoryData getSubcategory(String subcategory, String category, String host) {
        CassandraSubCategoryData subcat = null;

        Mapper<CassandraSubCategoryData> mapper = new MappingManager(session).mapper(CassandraSubCategoryData.class);
        subcat = mapper.get(subcategory, category, host);

        return subcat;
    }

    public void persistSubcategory(SubCategoryData subCategoryData, String category, String host) {
        Mapper<CassandraSubCategoryData> mapper = new MappingManager(session).mapper(CassandraSubCategoryData.class);
        mapper.save(new CassandraSubCategoryData(subCategoryData, category, host));
    }
}
