package fr.inti.cosmiconions.web.rest;

import fr.inti.cosmiconions.CosmiconionsApp;
import fr.inti.cosmiconions.domain.LeType;
import fr.inti.cosmiconions.repository.LeTypeRepository;
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
 * Integration tests for the {@link LeTypeResource} REST controller.
 */
@SpringBootTest(classes = CosmiconionsApp.class)
public class LeTypeResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    @Autowired
    private LeTypeRepository leTypeRepository;

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

    private MockMvc restLeTypeMockMvc;

    private LeType leType;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LeTypeResource leTypeResource = new LeTypeResource(leTypeRepository);
        this.restLeTypeMockMvc = MockMvcBuilders.standaloneSetup(leTypeResource)
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
    public static LeType createEntity(EntityManager em) {
        LeType leType = new LeType()
            .nom(DEFAULT_NOM);
        return leType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LeType createUpdatedEntity(EntityManager em) {
        LeType leType = new LeType()
            .nom(UPDATED_NOM);
        return leType;
    }

    @BeforeEach
    public void initTest() {
        leType = createEntity(em);
    }

    @Test
    @Transactional
    public void createLeType() throws Exception {
        int databaseSizeBeforeCreate = leTypeRepository.findAll().size();

        // Create the LeType
        restLeTypeMockMvc.perform(post("/api/le-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leType)))
            .andExpect(status().isCreated());

        // Validate the LeType in the database
        List<LeType> leTypeList = leTypeRepository.findAll();
        assertThat(leTypeList).hasSize(databaseSizeBeforeCreate + 1);
        LeType testLeType = leTypeList.get(leTypeList.size() - 1);
        assertThat(testLeType.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    @Transactional
    public void createLeTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = leTypeRepository.findAll().size();

        // Create the LeType with an existing ID
        leType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeTypeMockMvc.perform(post("/api/le-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leType)))
            .andExpect(status().isBadRequest());

        // Validate the LeType in the database
        List<LeType> leTypeList = leTypeRepository.findAll();
        assertThat(leTypeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllLeTypes() throws Exception {
        // Initialize the database
        leTypeRepository.saveAndFlush(leType);

        // Get all the leTypeList
        restLeTypeMockMvc.perform(get("/api/le-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(leType.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));
    }
    
    @Test
    @Transactional
    public void getLeType() throws Exception {
        // Initialize the database
        leTypeRepository.saveAndFlush(leType);

        // Get the leType
        restLeTypeMockMvc.perform(get("/api/le-types/{id}", leType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(leType.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM));
    }

    @Test
    @Transactional
    public void getNonExistingLeType() throws Exception {
        // Get the leType
        restLeTypeMockMvc.perform(get("/api/le-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLeType() throws Exception {
        // Initialize the database
        leTypeRepository.saveAndFlush(leType);

        int databaseSizeBeforeUpdate = leTypeRepository.findAll().size();

        // Update the leType
        LeType updatedLeType = leTypeRepository.findById(leType.getId()).get();
        // Disconnect from session so that the updates on updatedLeType are not directly saved in db
        em.detach(updatedLeType);
        updatedLeType
            .nom(UPDATED_NOM);

        restLeTypeMockMvc.perform(put("/api/le-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLeType)))
            .andExpect(status().isOk());

        // Validate the LeType in the database
        List<LeType> leTypeList = leTypeRepository.findAll();
        assertThat(leTypeList).hasSize(databaseSizeBeforeUpdate);
        LeType testLeType = leTypeList.get(leTypeList.size() - 1);
        assertThat(testLeType.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    public void updateNonExistingLeType() throws Exception {
        int databaseSizeBeforeUpdate = leTypeRepository.findAll().size();

        // Create the LeType

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeTypeMockMvc.perform(put("/api/le-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(leType)))
            .andExpect(status().isBadRequest());

        // Validate the LeType in the database
        List<LeType> leTypeList = leTypeRepository.findAll();
        assertThat(leTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLeType() throws Exception {
        // Initialize the database
        leTypeRepository.saveAndFlush(leType);

        int databaseSizeBeforeDelete = leTypeRepository.findAll().size();

        // Delete the leType
        restLeTypeMockMvc.perform(delete("/api/le-types/{id}", leType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LeType> leTypeList = leTypeRepository.findAll();
        assertThat(leTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
