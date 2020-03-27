package fr.inti.cosmiconions.web.rest;

import fr.inti.cosmiconions.CosmiconionsApp;
import fr.inti.cosmiconions.domain.Goodies;
import fr.inti.cosmiconions.repository.GoodiesRepository;
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
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static fr.inti.cosmiconions.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link GoodiesResource} REST controller.
 */
@SpringBootTest(classes = CosmiconionsApp.class)
public class GoodiesResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_CONTENT_TYPE = "image/png";

    private static final Float DEFAULT_PALIER = 1F;
    private static final Float UPDATED_PALIER = 2F;

    @Autowired
    private GoodiesRepository goodiesRepository;

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

    private MockMvc restGoodiesMockMvc;

    private Goodies goodies;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GoodiesResource goodiesResource = new GoodiesResource(goodiesRepository);
        this.restGoodiesMockMvc = MockMvcBuilders.standaloneSetup(goodiesResource)
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
    public static Goodies createEntity(EntityManager em) {
        Goodies goodies = new Goodies()
            .nom(DEFAULT_NOM)
            .photo(DEFAULT_PHOTO)
            .photoContentType(DEFAULT_PHOTO_CONTENT_TYPE)
            .palier(DEFAULT_PALIER);
        return goodies;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Goodies createUpdatedEntity(EntityManager em) {
        Goodies goodies = new Goodies()
            .nom(UPDATED_NOM)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .palier(UPDATED_PALIER);
        return goodies;
    }

    @BeforeEach
    public void initTest() {
        goodies = createEntity(em);
    }

    @Test
    @Transactional
    public void createGoodies() throws Exception {
        int databaseSizeBeforeCreate = goodiesRepository.findAll().size();

        // Create the Goodies
        restGoodiesMockMvc.perform(post("/api/goodies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goodies)))
            .andExpect(status().isCreated());

        // Validate the Goodies in the database
        List<Goodies> goodiesList = goodiesRepository.findAll();
        assertThat(goodiesList).hasSize(databaseSizeBeforeCreate + 1);
        Goodies testGoodies = goodiesList.get(goodiesList.size() - 1);
        assertThat(testGoodies.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testGoodies.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testGoodies.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
        assertThat(testGoodies.getPalier()).isEqualTo(DEFAULT_PALIER);
    }

    @Test
    @Transactional
    public void createGoodiesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = goodiesRepository.findAll().size();

        // Create the Goodies with an existing ID
        goodies.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGoodiesMockMvc.perform(post("/api/goodies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goodies)))
            .andExpect(status().isBadRequest());

        // Validate the Goodies in the database
        List<Goodies> goodiesList = goodiesRepository.findAll();
        assertThat(goodiesList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllGoodies() throws Exception {
        // Initialize the database
        goodiesRepository.saveAndFlush(goodies);

        // Get all the goodiesList
        restGoodiesMockMvc.perform(get("/api/goodies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(goodies.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO))))
            .andExpect(jsonPath("$.[*].palier").value(hasItem(DEFAULT_PALIER.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getGoodies() throws Exception {
        // Initialize the database
        goodiesRepository.saveAndFlush(goodies);

        // Get the goodies
        restGoodiesMockMvc.perform(get("/api/goodies/{id}", goodies.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(goodies.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.photoContentType").value(DEFAULT_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.photo").value(Base64Utils.encodeToString(DEFAULT_PHOTO)))
            .andExpect(jsonPath("$.palier").value(DEFAULT_PALIER.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingGoodies() throws Exception {
        // Get the goodies
        restGoodiesMockMvc.perform(get("/api/goodies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGoodies() throws Exception {
        // Initialize the database
        goodiesRepository.saveAndFlush(goodies);

        int databaseSizeBeforeUpdate = goodiesRepository.findAll().size();

        // Update the goodies
        Goodies updatedGoodies = goodiesRepository.findById(goodies.getId()).get();
        // Disconnect from session so that the updates on updatedGoodies are not directly saved in db
        em.detach(updatedGoodies);
        updatedGoodies
            .nom(UPDATED_NOM)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .palier(UPDATED_PALIER);

        restGoodiesMockMvc.perform(put("/api/goodies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedGoodies)))
            .andExpect(status().isOk());

        // Validate the Goodies in the database
        List<Goodies> goodiesList = goodiesRepository.findAll();
        assertThat(goodiesList).hasSize(databaseSizeBeforeUpdate);
        Goodies testGoodies = goodiesList.get(goodiesList.size() - 1);
        assertThat(testGoodies.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testGoodies.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testGoodies.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testGoodies.getPalier()).isEqualTo(UPDATED_PALIER);
    }

    @Test
    @Transactional
    public void updateNonExistingGoodies() throws Exception {
        int databaseSizeBeforeUpdate = goodiesRepository.findAll().size();

        // Create the Goodies

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGoodiesMockMvc.perform(put("/api/goodies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(goodies)))
            .andExpect(status().isBadRequest());

        // Validate the Goodies in the database
        List<Goodies> goodiesList = goodiesRepository.findAll();
        assertThat(goodiesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGoodies() throws Exception {
        // Initialize the database
        goodiesRepository.saveAndFlush(goodies);

        int databaseSizeBeforeDelete = goodiesRepository.findAll().size();

        // Delete the goodies
        restGoodiesMockMvc.perform(delete("/api/goodies/{id}", goodies.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Goodies> goodiesList = goodiesRepository.findAll();
        assertThat(goodiesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
