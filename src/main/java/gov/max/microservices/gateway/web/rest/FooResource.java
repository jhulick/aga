package gov.max.microservices.gateway.web.rest;

import com.codahale.metrics.annotation.Timed;

import gov.max.microservices.gateway.domain.Foo;
import gov.max.microservices.gateway.repository.FooRepository;
import gov.max.microservices.gateway.web.rest.util.HeaderUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Foo.
 */
@RestController
@RequestMapping("/api")
public class FooResource {

    private final Logger log = LoggerFactory.getLogger(FooResource.class);

    @Inject
    private FooRepository fooRepository;

    /**
     * POST  /foos -> Create a new foo.
     */
    @RequestMapping(value = "/foos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Foo> createFoo(@RequestBody Foo foo) throws URISyntaxException {
        log.debug("REST request to save Foo : {}", foo);
        if (foo.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("foo", "idexists", "A new foo cannot already have an ID")).body(null);
        }
        Foo result = fooRepository.save(foo);
        return ResponseEntity.created(new URI("/api/foos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("foo", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /foos -> Updates an existing foo.
     */
    @RequestMapping(value = "/foos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Foo> updateFoo(@RequestBody Foo foo) throws URISyntaxException {
        log.debug("REST request to update Foo : {}", foo);
        if (foo.getId() == null) {
            return createFoo(foo);
        }
        Foo result = fooRepository.save(foo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("foo", foo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /foos -> get all the foos.
     */
    @RequestMapping(value = "/foos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Foo> getAllFoos() {
        log.debug("REST request to get all Foos");
        return fooRepository.findAll();
    }

    /**
     * GET  /foos/:id -> get the "id" foo.
     */
    @RequestMapping(value = "/foos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Foo> getFoo(@PathVariable Long id) {
        log.debug("REST request to get Foo : {}", id);
        Foo foo = fooRepository.findOne(id);
        return Optional.ofNullable(foo)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /foos/:id -> delete the "id" foo.
     */
    @RequestMapping(value = "/foos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFoo(@PathVariable Long id) {
        log.debug("REST request to delete Foo : {}", id);
        fooRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("foo", id.toString())).build();
    }
}
