package org.easybank.config;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.resource.FileSystemResourceAccessor;
import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.jdbcx.JdbcDataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.util.Objects;

/**
 * Configure H2 in-memory DB.
 * - setup connection;
 * - execute liquibase change sets to create tables;
 *
 * @author Nikolay Smirnov
 */
public final class DBConfig {

    private final JdbcConnectionPool connectionPool;
    private final JdbcDataSource     dataSource;

    private DBConfig() {
        var appConf = AppConfiguration.getInstance();

        this.dataSource = createDatasource(appConf);
        this.connectionPool = createConnectionPool(appConf);

        executeLiquibaseChangeSets();
    }

    public DSLContext initDBContext() {
        try {
            var settings = new Settings();
            settings.withRenderSchema(false);

            return DSL.using(dataSource, SQLDialect.H2, settings);
        } catch (Exception ex) {
            throw new AppInitializeException(ex.getMessage());
        }
    }

    private void executeLiquibaseChangeSets() {
        try (var con = connectionPool.getConnection()) {
            var database = getDatabase(con);
            var pathToChaneSet = pathToDbChangeSet();

            var liquibase = new Liquibase(pathToChaneSet, new FileSystemResourceAccessor(), database);
            liquibase.update("");
        } catch (Exception ex) {
            throw new AppInitializeException(ex.getMessage());
        }
    }

    private JdbcDataSource createDatasource(AppConfiguration appConf) {
        var dataSource = new JdbcDataSource();

        dataSource.setURL(appConf.getProperty("db.url"));
        dataSource.setUser(appConf.getProperty("db.user"));
        dataSource.setPassword(appConf.getProperty("db.password"));

        return dataSource;
    }

    private JdbcConnectionPool createConnectionPool(AppConfiguration appConf) {
        var connectionPool = JdbcConnectionPool.create(dataSource);
        connectionPool.setMaxConnections(appConf.getIntProperty("db.connection.pool.max.connections"));
        return connectionPool;
    }

    private Database getDatabase(Connection con) throws DatabaseException {
        var databaseFactory = DatabaseFactory.getInstance();
        return databaseFactory.findCorrectDatabaseImplementation(new JdbcConnection(con));
    }

    private String pathToDbChangeSet() throws IOException {
        Path temp = Files.createTempFile("resource-db-changelog", ".xml");
        var classLoader = getClass().getClassLoader();
        var changeSetInputStream = classLoader.getResourceAsStream("db-changelog.xml");

        if (Objects.isNull(changeSetInputStream)) {
            throw new AppInitializeException("DB change sets file is not found 'db-changelog.xml'");
        }
        Files.copy(changeSetInputStream, temp, StandardCopyOption.REPLACE_EXISTING);

        return temp.toString();
    }

    public static DBConfig getInstance() {
        return LazyDBConfigHolder.APP_CONFIGURATION;
    }

    private static class LazyDBConfigHolder {
        private static final DBConfig APP_CONFIGURATION = new DBConfig();
    }
}
