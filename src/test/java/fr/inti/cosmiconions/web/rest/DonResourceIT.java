package fr.inti.cosmiconions.web.rest;

import fr.inti.cosmiconions.CosmiconionsApp;
import fr.inti.cosmiconions.domain.Don;
import fr.inti.cosmiconions.repository.DonRepository;
import fr.inti.cosmiconions.repository.ProjetRepository;
import fr.inti.cosmiconions.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static fr.inti.cosmiconions.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link DonResource} REST controller.
 */
@SpringBootTest(classes = CosmiconionsApp.class)
public class DonResourceIT {

    private static final Float DEFAULT_MONTANT = 1F;
    private static final Float UPDATED_MONTANT = 2F;

    @Autowired
    private DonRepository donRepository;
    
    @Autowired
    private ProjetRepository projetRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restDonMockMvc;

    private Don don;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DonResource donResource = new DonResource(donRepository, projetRepository);
        this.restDonMockMvc = MockMvcBuilders.standaloneSetup(donResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Don createEntity(EntityManager em) {
        Don don = new Don()
            .montant(DEFAULT_MONTANT);
        return don;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Don createUpdatedEntity(EntityManager em) {
        Don don = new Don()
            .montant(UPDATED_MONTANT);
        return don;
    }

    @BeforeEach
    public void initTest() {
        don = createEntity(em);
    }

    @Test
    @Transactional
    public void createDon() throws Exception {
        int databaseSizeBeforeCreate = donRepository.findAll().size();

        // Create the Don
        restDonMockMvc.perform(post("/api/dons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(don)))
            .andExpect(status().isCreated());

        // Validate the Don in the database
        List<Don> donList = donRepository.findAll();
        assertThat(donList).hasSize(databaseSizeBeforeCreate + 1);
        Don testDon = donList.get(donList.size() - 1);
        assertThat(testDon.getMontant()).isEqualTo(DEFAULT_MONTANT);
    }

    @Test
    @Transactional
    public void createDonWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = donRepository.findAll().size();

        // Create the Don with an existing ID
        don.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDonMockMvc.perform(post("/api/dons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(don)))
            .andExpect(status().isBadRequest());

        // Validate the Don in the database
        List<Don> donList = donRepository.findAll();
        assertThat(donList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllDons() throws Exception {
        // Initialize the database
        donRepository.saveAndFlush(don);

        // Get all the donList
        restDonMockMvc.perform(get("/api/dons?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(don.getId().intValue())))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getDon() throws Exception {
        // Initialize the database
        donRepository.saveAndFlush(don);

        // Get the don
        restDonMockMvc.perform(get("/api/dons/{id}", don.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(don.getId().intValue()))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDon() throws Exception {
        // Get the don
        restDonMockMvc.perform(get("/api/dons/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDon() throws Exception {
        // Initialize the database
        donRepository.saveAndFlush(don);

        int databaseSizeBeforeUpdate = donRepository.findAll().size();

        // Update the don
        Don updatedDon = donRepository.findById(don.getId()).get();
        // Disconnect from session so that the updates on updatedDon are not directly saved in db
        em.detach(updatedDon);
        updatedDon
            .montant(UPDATED_MONTANT);

        restDonMockMvc.perform(put("/api/dons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDon)))
            .andExpect(status().isOk());

        // Validate the Don in the database
        List<Don> donList = donRepository.findAll();
        assertThat(donList).hasSize(databaseSizeBeforeUpdate);
        Don testDon = donList.get(donList.size() - 1);
        assertThat(testDon.getMontant()).isEqualTo(UPDATED_MONTANT);
    }

    @Test
    @Transactional
    public void updateNonExistingDon() throws Exception {
        int databaseSizeBeforeUpdate = donRepository.findAll().size();

        // Create the Don

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDonMockMvc.perform(put("/api/dons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(don)))
            .andExpect(status().isBadRequest());

        // Validate the Don in the database
        List<Don> donList = donRepository.findAll();
        assertThat(donList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDon() throws Exception {
        // Initialize the database
        donRepository.saveAndFlush(don);

        int databaseSizeBeforeDelete = donRepository.findAll().size();

        // Delete the don
        restDonMockMvc.perform(delete("/api/dons/{id}", don.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Don> donList = donRepository.findAll();
        assertThat(donList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
