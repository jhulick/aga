package gov.max.microservices.gateway.repository;

import gov.max.microservices.gateway.domain.Tenant;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Tenant entity.
 */
public interface TenantRepository extends JpaRepository<Tenant, Long> {

}
