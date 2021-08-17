package org.craftedsw.config;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.FileSystemResourceAccessor;
import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.jdbcx.JdbcDataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;

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
        AppConfiguration appConf = AppConfiguration.getInstance();

        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL(appConf.getProperty("db.url"));
        dataSource.setUser(appConf.getProperty("db.user"));
        dataSource.setPassword(appConf.getProperty("db.password"));

        JdbcConnectionPool connectionPool = JdbcConnectionPool.create(dataSource);
        connectionPool.setMaxConnections(appConf.getIntProperty("db.connection.pool.max.connections"));

        this.connectionPool = connectionPool;
        this.dataSource     = dataSource;

        executeLiquibaseChangeSets();
    }

    public DSLContext initDBContext() {
        try {
            Settings settings = new Settings();
            settings.withRenderSchema(false);

            return DSL.using(dataSource, SQLDialect.H2, settings);
        } catch (Exception ex) {
            // NOPE
        }
        return null;
    }

    private void executeLiquibaseChangeSets() {

        try (Connection con = connectionPool.getConnection()) {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(con));

            Path temp = Files.createTempFile("resource-db-changelog", ".xml");
            Files.copy(getClass().getClassLoader().getResourceAsStream("db-changelog.xml"), temp, StandardCopyOption.REPLACE_EXISTING);

            Liquibase liquibase = new Liquibase(
                    temp.toString(),
                    new FileSystemResourceAccessor(),
                    database
            );
            liquibase.update("");
        } catch (Exception ex) {
            // NOPE
        }
    }

    public static DBConfig getInstance() {
        return LazyDBConfigHolder.APP_CONFIGURATION;
    }

    private static class LazyDBConfigHolder {
        private static final DBConfig APP_CONFIGURATION = new DBConfig();
    }
}
