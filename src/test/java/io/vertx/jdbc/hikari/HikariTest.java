package io.vertx.ext.jdbc.hikari;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.test.core.VertxTestBase;

public class HikariTest extends VertxTestBase {

	@Test
	public void testDataSource() {
	JsonObject config = new JsonObject().put("provider_class", "io.vertx.ext.jdbc.spi.impl.HikariDataSourceProvider")
		.put("property_file", "/hikari.properties");
	// .put("dataSourceClassName", "org.mariadb.jdbc.MySQLDataSource")
	// .put("url",
	// "jdbc:mysql://localhost:3306/app?useUnicode=true&characterEncoding=utf-8")
	// .put("driver_class", "org.mariadb.jdbc.Driver").put("username",
	// "app").put("password", "app")
	// .put("max_pool_size", 30);

	JDBCClient client = JDBCClient.createShared(vertx, config);
	assertNotNull(client);

	connection(client).query("select version() v", onSuccess(resultSet -> {
		assertNotNull(resultSet);
		System.out.println(resultSet.getResults());
		testComplete();
	}));
	await(1, TimeUnit.SECONDS);
	}

	private SQLConnection connection(JDBCClient client) {
	CountDownLatch latch = new CountDownLatch(1);
	AtomicReference<SQLConnection> ref = new AtomicReference<>();
	client.getConnection(onSuccess(conn -> {
		ref.set(conn);
		latch.countDown();
	}));

	try {
		latch.await();
	} catch (InterruptedException e) {
		throw new RuntimeException(e);
	}

	return ref.get();
	}

}
