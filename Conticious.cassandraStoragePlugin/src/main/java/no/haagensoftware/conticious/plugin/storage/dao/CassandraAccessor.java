package no.haagensoftware.conticious.plugin.storage.dao;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;
import no.haagensoftware.conticious.plugin.storage.data.CassandraCategoryData;

/**
 * Created by joachimhaagenskeie on 19/02/2017.
 */
@Accessor
public interface CassandraAccessor {
    @Query("SELECT * FROM conticious.categories where host = :host")
    public Result<CassandraCategoryData> getCategories(@Param("host") String host);
}
