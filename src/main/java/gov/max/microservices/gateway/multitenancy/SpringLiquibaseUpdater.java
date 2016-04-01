package gov.max.microservices.gateway.multitenancy;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ResourceAccessor;

import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class SpringLiquibaseUpdater {
    private Logger logger = LoggerFactory.getLogger(SpringLiquibaseUpdater.class.getName());

    private class SpringResourceOpener implements ResourceAccessor {

        @Override
        public Set<InputStream> getResourcesAsStream(String file) throws IOException {
            try {
                Resource resource = getResource(file);
                return Collections.singleton(resource.getInputStream());
            } catch (FileNotFoundException ex) {
                return null;
            }
        }

        private Resource getResource(String file) {
            return getResourceLoader().getResource(file);
        }


        @Override
        public Set<String> list(String s, String s1, boolean b, boolean b1, boolean b2) throws IOException {
            return null;
        }

        @Override
        public ClassLoader toClassLoader() {
            return getResourceLoader().getClassLoader();
        }
    }

    private ResourceLoader resourceLoader;

    private final ConnectionProviderHolder connectionProviderHolder;

    private final String changeLog;

    private String contexts;

    private Map<String, String> parameters;

    private String defaultSchema;

    private boolean dropFirst = false;

    private String changeLogTableName;
    private String changeLogLockTableName;

    public SpringLiquibaseUpdater(ConnectionProviderHolder connectionProviderHolder, String changeLog, ResourceLoader resourceLoader) {
        super();
        this.connectionProviderHolder = connectionProviderHolder;
        this.changeLog = changeLog;
        this.resourceLoader = resourceLoader;
    }

    public boolean isDropFirst() {
        return dropFirst;
    }

    public void setDropFirst(boolean dropFirst) {
        this.dropFirst = dropFirst;
    }

    public String getDatabaseProductName() throws DatabaseException {
        Connection connection = null;
        String name = "unknown";
        try {
            connection = getConnectionProvider().getConnection();
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(getConnectionProvider().getConnection()));
            name = database.getDatabaseProductName();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        } finally {
            if (connection != null) {
                try {
                    if (!connection.getAutoCommit()) {
                        connection.rollback();
                    }
                    connection.close();
                } catch (Exception e) {
                    logger.warn("problem closing connection", e);
                }
            }
        }
        return name;
    }

    /**
     * The ConnectionProvider that liquibase will use to perform the migration.
     *
     * @return
     */
    private ConnectionProvider getConnectionProvider() {
        return connectionProviderHolder.dataSource;
    }

    /**
     * Returns a Resource that is able to resolve to a file or classpath resource.
     *
     * @return
     */
    private String getChangeLog() {
        return changeLog;
    }

    public String getContexts() {
        return contexts;
    }

    public void setContexts(String contexts) {
        this.contexts = contexts;
    }

    public String getDefaultSchema() {
        return defaultSchema;
    }

    public void setDefaultSchema(String defaultSchema) {
        this.defaultSchema = defaultSchema;
    }

    /**
     * Executed automatically when the bean is initialized.
     */
    public void update() throws LiquibaseException {
        Connection connection = null;
        Liquibase liquibase = null;
        try {
            connection = getConnectionProvider().getConnection();
            liquibase = createLiquibase(connection);
            liquibase.update("development, production");
        } catch (SQLException e) {
            throw new DatabaseException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.rollback();
                    connection.close();
                } catch (SQLException e) {
                    //nothing to do
                }
            }
        }
    }

    protected Liquibase createLiquibase(Connection c) throws LiquibaseException, IOException {
        Liquibase liquibase = new Liquibase(getChangeLog(), createResourceOpener(), createDatabase(c));
        if (parameters != null) {
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                liquibase.setChangeLogParameter(entry.getKey(), entry.getValue());
            }
        }

        if (isDropFirst()) {
            liquibase.dropAll();
        }

        return liquibase;
    }

    /**
     * Subclasses may override this method add change some database settings such as
     * default schema before returning the database object.
     *
     * @param c
     * @return a Database implementation retrieved from the {@link liquibase.database.DatabaseFactory}.
     * @throws liquibase.exception.DatabaseException
     */
    protected Database createDatabase(Connection c) throws DatabaseException {
        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(c));
        if (this.changeLogTableName != null)
            database.setDatabaseChangeLogTableName(this.changeLogTableName);
        if (this.changeLogLockTableName != null)
            database.setDatabaseChangeLogLockTableName(this.changeLogLockTableName);
        if (this.defaultSchema != null)
            database.setDefaultSchemaName(this.defaultSchema);
        return database;
    }

    public void setChangeLogParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    /**
     * Create a new resourceOpener.
     */
    protected SpringResourceOpener createResourceOpener() {
        return new SpringResourceOpener();
    }

    private ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    public String getChangeLogLockTableName() {
        return changeLogLockTableName;
    }

    public void setChangeLogLockTableName(String changeLogLockTableName) {
        this.changeLogLockTableName = changeLogLockTableName;
    }

    public String getChangeLogTableName() {
        return changeLogTableName;
    }

    public void setChangeLogTableName(String changeLogTableName) {
        this.changeLogTableName = changeLogTableName;
    }

    @Override
    public String toString() {
        return getClass().getName() + "(" + this.getResourceLoader().toString() + ")";
    }
}
