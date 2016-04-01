package gov.max.microservices.gateway.multitenancy;

import gov.max.microservices.gateway.domain.Tenant;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TenantRowMapper implements RowMapper<Tenant> {

    @Override
    public Tenant mapRow(ResultSet resultSet, int i) throws SQLException {
        return null;
    }
}
