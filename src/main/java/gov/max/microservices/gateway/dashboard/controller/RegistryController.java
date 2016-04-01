package gov.max.microservices.gateway.dashboard.controller;

import gov.max.microservices.gateway.dashboard.repository.RegistryRepository;

import com.google.common.collect.ImmutableMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for retrieve registry information.
 */
@RestController
public class RegistryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistryController.class);

    @Autowired(required = false)
    private RegistryRepository repository;

    /**
     * Return instance history registration/cancellation
     *
     * @return List of registered/cancelled instances
     */
    @RequestMapping(value = "/api/registry/history", method = RequestMethod.GET)
    public ResponseEntity instancesHistory() {

        if (repository == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(ImmutableMap.of(
            "lastRegistered", repository.getRegisteredInstanceHistory(),
            "lastCancelled", repository.getCanceledInstanceHistory()
        ));
    }
}
