package io.vertx.ext.jdbc.spi.impl;

import java.sql.SQLException;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.spi.DataSourceProvider;

public class HikariDataSourceProvider implements DataSourceProvider {

	@Override
	public DataSource getDataSource(JsonObject config) throws SQLException {

	if (config.getString("property_file") == null) {
		HikariDataSource ds = new HikariDataSource();
		ds.setJdbcUrl(config.getString("jdbcUrl"));
		ds.setUsername(config.getString("username"));
		ds.setPassword(config.getString("password"));
		ds.setDataSourceClassName(config.getString("dataSourceClassName"));
		return ds;
	} else {
		HikariConfig hikariConfig = new HikariConfig(config.getString("property_file"));
		HikariDataSource ds = new HikariDataSource(hikariConfig);
		return ds;
	}
	}

	@Override
	public void close(DataSource dataSource) throws SQLException {
	((HikariDataSource) dataSource).close();
	}

}
