package gov.max.microservices.gateway.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Tenant.
 */
@Entity
@Table(name = "MAX_TENANT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Tenant implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "tenant_name")
    private String tenantName;

    @Column(name = "db_host")
    private String dbHost;

    @Column(name = "db_port")
    private Integer dbPort;

    @Column(name = "db_name")
    private String dbName;

    @Column(name = "db_user_name")
    private String dbUserName;

    @Column(name = "db_password")
    private String dbPassword;

    @Column(name = "is_enabled")
    private Boolean isEnabled;

    @Column(name = "has_database")
    private Boolean hasDatabase;

    @Column(name = "has_generated_schema")
    private Boolean hasGeneratedSchema;

    @ManyToOne
    private DbType dbtype;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getDbHost() {
        return dbHost;
    }

    public void setDbHost(String dbHost) {
        this.dbHost = dbHost;
    }

    public Integer getDbPort() {
        return dbPort;
    }

    public void setDbPort(Integer dbPort) {
        this.dbPort = dbPort;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbUserName() {
        return dbUserName;
    }

    public void setDbUserName(String dbUserName) {
        this.dbUserName = dbUserName;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public Boolean isEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Boolean hasDatabase() {
        return hasDatabase;
    }

    public void setHasDatabase(Boolean hasDatabase) {
        this.hasDatabase = hasDatabase;
    }

    public Boolean hasGeneratedSchema() {
        return hasGeneratedSchema;
    }

    public void setHasGeneratedSchema(Boolean hasGeneratedSchema) {
        this.hasGeneratedSchema = hasGeneratedSchema;
    }

    public DbType getDbtype() {
        return dbtype;
    }

    public void setDbtype(DbType dbType) {
        this.dbtype = dbType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Tenant tenant = (Tenant) o;

        if ( ! Objects.equals(id, tenant.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Tenant{" +
                "id=" + id +
                ", tenantName='" + tenantName + "'" +
                ", dbHost='" + dbHost + "'" +
                ", dbPort='" + dbPort + "'" +
                ", dbName='" + dbName + "'" +
                ", dbUserName='" + dbUserName + "'" +
                ", dbPassword='" + dbPassword + "'" +
                ", isEnabled='" + isEnabled + "'" +
                ", hasDatabase='" + hasDatabase + "'" +
                ", hasGeneratedSchema='" + hasGeneratedSchema + "'" +
                '}';
    }
}
