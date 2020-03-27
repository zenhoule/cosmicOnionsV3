package fr.inti.cosmiconions.web.rest;

import fr.inti.cosmiconions.domain.Goodies;
import fr.inti.cosmiconions.repository.GoodiesRepository;
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
 * REST controller for managing {@link fr.inti.cosmiconions.domain.Goodies}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class GoodiesResource {

    private final Logger log = LoggerFactory.getLogger(GoodiesResource.class);

    private static final String ENTITY_NAME = "goodies";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GoodiesRepository goodiesRepository;

    public GoodiesResource(GoodiesRepository goodiesRepository) {
        this.goodiesRepository = goodiesRepository;
    }

    /**
     * {@code POST  /goodies} : Create a new goodies.
     *
     * @param goodies the goodies to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new goodies, or with status {@code 400 (Bad Request)} if the goodies has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/goodies")
    public ResponseEntity<Goodies> createGoodies(@RequestBody Goodies goodies) throws URISyntaxException {
        log.debug("REST request to save Goodies : {}", goodies);
        if (goodies.getId() != null) {
            throw new BadRequestAlertException("A new goodies cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Goodies result = goodiesRepository.save(goodies);
        return ResponseEntity.created(new URI("/api/goodies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /goodies} : Updates an existing goodies.
     *
     * @param goodies the goodies to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated goodies,
     * or with status {@code 400 (Bad Request)} if the goodies is not valid,
     * or with status {@code 500 (Internal Server Error)} if the goodies couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/goodies")
    public ResponseEntity<Goodies> updateGoodies(@RequestBody Goodies goodies) throws URISyntaxException {
        log.debug("REST request to update Goodies : {}", goodies);
        if (goodies.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Goodies result = goodiesRepository.save(goodies);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, goodies.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /goodies} : get all the goodies.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of goodies in body.
     */
    @GetMapping("/goodies")
    public List<Goodies> getAllGoodies() {
        log.debug("REST request to get all Goodies");
        return goodiesRepository.findAll();
    }

    /**
     * {@code GET  /goodies/:id} : get the "id" goodies.
     *
     * @param id the id of the goodies to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the goodies, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/goodies/{id}")
    public ResponseEntity<Goodies> getGoodies(@PathVariable Long id) {
        log.debug("REST request to get Goodies : {}", id);
        Optional<Goodies> goodies = goodiesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(goodies);
    }

    /**
     * {@code DELETE  /goodies/:id} : delete the "id" goodies.
     *
     * @param id the id of the goodies to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/goodies/{id}")
    public ResponseEntity<Void> deleteGoodies(@PathVariable Long id) {
        log.debug("REST request to delete Goodies : {}", id);
        goodiesRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
