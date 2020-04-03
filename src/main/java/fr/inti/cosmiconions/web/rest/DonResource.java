package fr.inti.cosmiconions.web.rest;

import fr.inti.cosmiconions.domain.Don;
import fr.inti.cosmiconions.repository.DonRepository;
import fr.inti.cosmiconions.repository.ProjetRepository;
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
 * REST controller for managing {@link fr.inti.cosmiconions.domain.Don}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DonResource {

    private final Logger log = LoggerFactory.getLogger(DonResource.class);

    private static final String ENTITY_NAME = "don";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DonRepository donRepository;
    private final ProjetRepository projetRepository;

    /* public DonResource(DonRepository donRepository) {
        this.donRepository = donRepository;
    } */
    
    public DonResource(DonRepository donRepository, ProjetRepository projetRepository) {
		super();
		this.donRepository = donRepository;
		this.projetRepository = projetRepository;
	}
    
    

    /**
     * {@code POST  /dons} : Create a new don.
     *
     * @param don the don to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new don, or with status {@code 400 (Bad Request)} if the don has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dons")
    public ResponseEntity<Don> createDon(@RequestBody Don don) throws URISyntaxException {
        log.debug("REST request to save Don : {}", don);
        if (don.getId() != null) {
            throw new BadRequestAlertException("A new don cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Don result = donRepository.save(don);
        if (don.getProjet() != null) {
        	don.getProjet().setSoldeCours(don.getProjet().getSoldeCours()+don.getMontant());
        	projetRepository.save(don.getProjet());
        }
        return ResponseEntity.created(new URI("/api/dons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

	/**
     * {@code PUT  /dons} : Updates an existing don.
     *
     * @param don the don to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated don,
     * or with status {@code 400 (Bad Request)} if the don is not valid,
     * or with status {@code 500 (Internal Server Error)} if the don couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dons")
    public ResponseEntity<Don> updateDon(@RequestBody Don don) throws URISyntaxException {
        log.debug("REST request to update Don : {}", don);
        if (don.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Don result = donRepository.save(don);
        if (don.getProjet() != null) {
        	don.getProjet().setSoldeCours(don.getProjet().getSoldeCours()+don.getMontant());
        	projetRepository.save(don.getProjet());
        }
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, don.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /dons} : get all the dons.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dons in body.
     */
    @GetMapping("/dons")
    public List<Don> getAllDons() {
        log.debug("REST request to get all Dons");
        return donRepository.findAll();
    }

    /**
     * {@code GET  /dons/:id} : get the "id" don.
     *
     * @param id the id of the don to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the don, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dons/{id}")
    public ResponseEntity<Don> getDon(@PathVariable Long id) {
        log.debug("REST request to get Don : {}", id);
        Optional<Don> don = donRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(don);
    }

    /**
     * {@code DELETE  /dons/:id} : delete the "id" don.
     *
     * @param id the id of the don to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dons/{id}")
    public ResponseEntity<Void> deleteDon(@PathVariable Long id) {
        log.debug("REST request to delete Don : {}", id);
        donRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
