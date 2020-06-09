package no.haagensoftware.contentice.main;

import no.haagensoftware.contentice.data.CategoryData;
import no.haagensoftware.contentice.data.SubCategoryData;
import no.haagensoftware.contentice.data.SubcategoryField;
import no.haagensoftware.contentice.storage.FileSystemStoragePlugin;
import no.haagensoftware.conticious.plugin.storage.CassandraStoragePlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joachimhaagenskeie on 12/03/2018.
 */
public class ConvertToCassandraTool {
    public static void main(String[] args) {
        List<String> hosts = new ArrayList<>();
        hosts.add("admin");
        hosts.add("kodegenet.no");

        System.setProperty("no.haagensoftware.conticious.db.cassandra.hosts", "127.0.0.1");
        CassandraStoragePlugin cassandraStoragePlugin = new CassandraStoragePlugin();
        cassandraStoragePlugin.setup();

        System.setProperty("no.haagensoftware.contentice.storage.file.documentsDirectory", "/srv/convertToCassandra");

        FileSystemStoragePlugin fileSystemStoragePlugin = new FileSystemStoragePlugin();
        fileSystemStoragePlugin.setup();

        for (String host : hosts) {
            List<CategoryData> fileCategories = fileSystemStoragePlugin.getCategories(host);
            for (CategoryData cd : fileCategories) {
                Long start = System.currentTimeMillis();

                List<SubCategoryData> subCategoryDatas = fileSystemStoragePlugin.getSubCategories(host, cd.getId());
                for (SubCategoryData sd : subCategoryDatas) {
                    if (sd.getId().startsWith(cd.getId())) {
                        sd.setId(sd.getId().substring(cd.getId().length() + 1));
                    }
                    cassandraStoragePlugin.setSubCategory(host, cd.getId(), sd.getId(), sd);
                    cd.addSubcategory(sd);
                }


                cassandraStoragePlugin.setCategory(host, cd.getId(), cd);

                System.out.println("Category converted: " + cd.getId() + " num subs: " + subCategoryDatas.size() + " took: " + (System.currentTimeMillis() - start) + " ms.");
            }
        }

        System.exit(0);
    }
}
