package gov.max.microservices.gateway.repository;

import gov.max.microservices.gateway.domain.Foo;

import org.springframework.data.jpa.repository.*;

/**
 * Spring Data JPA repository for the Foo entity.
 */
public interface FooRepository extends JpaRepository<Foo,Long> {

}
