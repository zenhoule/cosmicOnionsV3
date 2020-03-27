package fr.inti.cosmiconions.web.rest;

import fr.inti.cosmiconions.domain.LeType;
import fr.inti.cosmiconions.repository.LeTypeRepository;
import fr.inti.cosmiconions.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional; 
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link fr.inti.cosmiconions.domain.LeType}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LeTypeResource {

    private final Logger log = LoggerFactory.getLogger(LeTypeResource.class);

    private static final String ENTITY_NAME = "leType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LeTypeRepository leTypeRepository;

    public LeTypeResource(LeTypeRepository leTypeRepository) {
        this.leTypeRepository = leTypeRepository;
    }

    /**
     * {@code POST  /le-types} : Create a new leType.
     *
     * @param leType the leType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leType, or with status {@code 400 (Bad Request)} if the leType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/le-types")
    public ResponseEntity<LeType> createLeType(@RequestBody LeType leType) throws URISyntaxException {
        log.debug("REST request to save LeType : {}", leType);
        if (leType.getId() != null) {
            throw new BadRequestAlertException("A new leType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LeType result = leTypeRepository.save(leType);
        return ResponseEntity.created(new URI("/api/le-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /le-types} : Updates an existing leType.
     *
     * @param leType the leType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leType,
     * or with status {@code 400 (Bad Request)} if the leType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the leType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/le-types")
    public ResponseEntity<LeType> updateLeType(@RequestBody LeType leType) throws URISyntaxException {
        log.debug("REST request to update LeType : {}", leType);
        if (leType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LeType result = leTypeRepository.save(leType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leType.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /le-types} : get all the leTypes.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leTypes in body.
     */
    @GetMapping("/le-types")
    public List<LeType> getAllLeTypes() {
        log.debug("REST request to get all LeTypes");
        return leTypeRepository.findAll();
    }

    /**
     * {@code GET  /le-types/:id} : get the "id" leType.
     *
     * @param id the id of the leType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/le-types/{id}")
    public ResponseEntity<LeType> getLeType(@PathVariable Long id) {
        log.debug("REST request to get LeType : {}", id);
        Optional<LeType> leType = leTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(leType);
    }

    /**
     * {@code DELETE  /le-types/:id} : delete the "id" leType.
     *
     * @param id the id of the leType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/le-types/{id}")
    public ResponseEntity<Void> deleteLeType(@PathVariable Long id) {
        log.debug("REST request to delete LeType : {}", id);
        leTypeRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
