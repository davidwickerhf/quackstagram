package net.group3.quackstagram.backend.database;

import java.util.HashMap;
import java.util.Map;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;

public class DataSourceFactory {

    private Map<String, HikariDataSource> dataSources = new HashMap<>();

    public DataSource getDataSource(String name) {
        return dataSources.get(name);
    }

    public DataSource createDataSource(String name, String jdbcUrl) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        // Additional configuration can be set here

        HikariDataSource dataSource = new HikariDataSource(config);
        dataSources.put(name, dataSource);
        return dataSource;
    }

    public void shutdown() {
        for (HikariDataSource ds : dataSources.values()) {
            if (ds != null && !ds.isClosed()) {
                ds.close();
            }
        }
    }
}
