package no.haagensoftware.conticious.plugin.storage.dao;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import no.haagensoftware.conticious.plugin.storage.data.CassandraCategoryData;
import no.haagensoftware.conticious.plugin.storage.data.CassandraSubCategoryData;

/**
 * Created by joachimhaagenskeie on 19/02/2017.
 */
@Accessor
public interface CassandraAccessor {
    @Query("SELECT * FROM conticious.categories where host = :host")
    public Result<CassandraCategoryData> getCategories(@Param("host") String host);

    @Query("select * from conticious.subcategories where category = :category and  host = :host ALLOW FILTERING")
    public Result<CassandraSubCategoryData> getSubCategories(@Param("category") String category, @Param("host") String host);
}
