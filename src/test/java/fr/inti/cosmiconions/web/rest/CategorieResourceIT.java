package fr.inti.cosmiconions.web.rest;

import fr.inti.cosmiconions.CosmiconionsApp;
import fr.inti.cosmiconions.domain.Categorie;
import fr.inti.cosmiconions.repository.CategorieRepository;
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
 * Integration tests for the {@link CategorieResource} REST controller.
 */
@SpringBootTest(classes = CosmiconionsApp.class)
public class CategorieResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    @Autowired
    private CategorieRepository categorieRepository;

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

    private MockMvc restCategorieMockMvc;

    private Categorie categorie;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CategorieResource categorieResource = new CategorieResource(categorieRepository);
        this.restCategorieMockMvc = MockMvcBuilders.standaloneSetup(categorieResource)
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
    public static Categorie createEntity(EntityManager em) {
        Categorie categorie = new Categorie()
            .nom(DEFAULT_NOM);
        return categorie;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Categorie createUpdatedEntity(EntityManager em) {
        Categorie categorie = new Categorie()
            .nom(UPDATED_NOM);
        return categorie;
    }

    @BeforeEach
    public void initTest() {
        categorie = createEntity(em);
    }

    @Test
    @Transactional
    public void createCategorie() throws Exception {
        int databaseSizeBeforeCreate = categorieRepository.findAll().size();

        // Create the Categorie
        restCategorieMockMvc.perform(post("/api/categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(categorie)))
            .andExpect(status().isCreated());

        // Validate the Categorie in the database
        List<Categorie> categorieList = categorieRepository.findAll();
        assertThat(categorieList).hasSize(databaseSizeBeforeCreate + 1);
        Categorie testCategorie = categorieList.get(categorieList.size() - 1);
        assertThat(testCategorie.getNom()).isEqualTo(DEFAULT_NOM);
    }

    @Test
    @Transactional
    public void createCategorieWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = categorieRepository.findAll().size();

        // Create the Categorie with an existing ID
        categorie.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategorieMockMvc.perform(post("/api/categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(categorie)))
            .andExpect(status().isBadRequest());

        // Validate the Categorie in the database
        List<Categorie> categorieList = categorieRepository.findAll();
        assertThat(categorieList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCategories() throws Exception {
        // Initialize the database
        categorieRepository.saveAndFlush(categorie);

        // Get all the categorieList
        restCategorieMockMvc.perform(get("/api/categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categorie.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)));
    }
    
    @Test
    @Transactional
    public void getCategorie() throws Exception {
        // Initialize the database
        categorieRepository.saveAndFlush(categorie);

        // Get the categorie
        restCategorieMockMvc.perform(get("/api/categories/{id}", categorie.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(categorie.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM));
    }

    @Test
    @Transactional
    public void getNonExistingCategorie() throws Exception {
        // Get the categorie
        restCategorieMockMvc.perform(get("/api/categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCategorie() throws Exception {
        // Initialize the database
        categorieRepository.saveAndFlush(categorie);

        int databaseSizeBeforeUpdate = categorieRepository.findAll().size();

        // Update the categorie
        Categorie updatedCategorie = categorieRepository.findById(categorie.getId()).get();
        // Disconnect from session so that the updates on updatedCategorie are not directly saved in db
        em.detach(updatedCategorie);
        updatedCategorie
            .nom(UPDATED_NOM);

        restCategorieMockMvc.perform(put("/api/categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCategorie)))
            .andExpect(status().isOk());

        // Validate the Categorie in the database
        List<Categorie> categorieList = categorieRepository.findAll();
        assertThat(categorieList).hasSize(databaseSizeBeforeUpdate);
        Categorie testCategorie = categorieList.get(categorieList.size() - 1);
        assertThat(testCategorie.getNom()).isEqualTo(UPDATED_NOM);
    }

    @Test
    @Transactional
    public void updateNonExistingCategorie() throws Exception {
        int databaseSizeBeforeUpdate = categorieRepository.findAll().size();

        // Create the Categorie

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategorieMockMvc.perform(put("/api/categories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(categorie)))
            .andExpect(status().isBadRequest());

        // Validate the Categorie in the database
        List<Categorie> categorieList = categorieRepository.findAll();
        assertThat(categorieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCategorie() throws Exception {
        // Initialize the database
        categorieRepository.saveAndFlush(categorie);

        int databaseSizeBeforeDelete = categorieRepository.findAll().size();

        // Delete the categorie
        restCategorieMockMvc.perform(delete("/api/categories/{id}", categorie.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Categorie> categorieList = categorieRepository.findAll();
        assertThat(categorieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
