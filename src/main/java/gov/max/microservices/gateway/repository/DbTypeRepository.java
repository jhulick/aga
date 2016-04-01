package gov.max.microservices.gateway.repository;

import gov.max.microservices.gateway.domain.DbType;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the DbType entity.
 */
public interface DbTypeRepository extends JpaRepository<DbType, Long> {

}
