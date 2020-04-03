package fr.inti.cosmiconions.web.rest;

import fr.inti.cosmiconions.CosmiconionsApp;
import fr.inti.cosmiconions.domain.Projet;
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
 * Integration tests for the {@link ProjetResource} REST controller.
 */
@SpringBootTest(classes = CosmiconionsApp.class)
public class ProjetResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_VIDEO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_VIDEO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_VIDEO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_VIDEO_CONTENT_TYPE = "image/png";

    private static final Float DEFAULT_OBJECTIF = 1F;
    private static final Float UPDATED_OBJECTIF = 2F;

    private static final Float DEFAULT_SOLDE_COURS = 1F;
    private static final Float UPDATED_SOLDE_COURS = 2F;

    private static final Integer DEFAULT_NB_JOURS_RESTANT = 1;
    private static final Integer UPDATED_NB_JOURS_RESTANT = 2;

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

    private MockMvc restProjetMockMvc;

    private Projet projet;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProjetResource projetResource = new ProjetResource(projetRepository);
        this.restProjetMockMvc = MockMvcBuilders.standaloneSetup(projetResource)
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
    public static Projet createEntity(EntityManager em) {
        Projet projet = new Projet()
            .nom(DEFAULT_NOM)
            .description(DEFAULT_DESCRIPTION)
            .photo(DEFAULT_PHOTO)
            .photoContentType(DEFAULT_PHOTO_CONTENT_TYPE)
            .video(DEFAULT_VIDEO)
            .videoContentType(DEFAULT_VIDEO_CONTENT_TYPE)
            .objectif(DEFAULT_OBJECTIF)
            .soldeCours(DEFAULT_SOLDE_COURS)
            .nbJoursRestant(DEFAULT_NB_JOURS_RESTANT);
        return projet;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Projet createUpdatedEntity(EntityManager em) {
        Projet projet = new Projet()
            .nom(UPDATED_NOM)
            .description(UPDATED_DESCRIPTION)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .video(UPDATED_VIDEO)
            .videoContentType(UPDATED_VIDEO_CONTENT_TYPE)
            .objectif(UPDATED_OBJECTIF)
            .soldeCours(UPDATED_SOLDE_COURS)
            .nbJoursRestant(UPDATED_NB_JOURS_RESTANT);
        return projet;
    }

    @BeforeEach
    public void initTest() {
        projet = createEntity(em);
    }

    @Test
    @Transactional
    public void createProjet() throws Exception {
        int databaseSizeBeforeCreate = projetRepository.findAll().size();

        // Create the Projet
        restProjetMockMvc.perform(post("/api/projets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projet)))
            .andExpect(status().isCreated());

        // Validate the Projet in the database
        List<Projet> projetList = projetRepository.findAll();
        assertThat(projetList).hasSize(databaseSizeBeforeCreate + 1);
        Projet testProjet = projetList.get(projetList.size() - 1);
        assertThat(testProjet.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testProjet.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProjet.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testProjet.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
        assertThat(testProjet.getVideo()).isEqualTo(DEFAULT_VIDEO);
        assertThat(testProjet.getVideoContentType()).isEqualTo(DEFAULT_VIDEO_CONTENT_TYPE);
        assertThat(testProjet.getObjectif()).isEqualTo(DEFAULT_OBJECTIF);
        assertThat(testProjet.getSoldeCours()).isEqualTo(DEFAULT_SOLDE_COURS);
        assertThat(testProjet.getNbJoursRestant()).isEqualTo(DEFAULT_NB_JOURS_RESTANT);
    }

    @Test
    @Transactional
    public void createProjetWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = projetRepository.findAll().size();

        // Create the Projet with an existing ID
        projet.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjetMockMvc.perform(post("/api/projets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projet)))
            .andExpect(status().isBadRequest());

        // Validate the Projet in the database
        List<Projet> projetList = projetRepository.findAll();
        assertThat(projetList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllProjets() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        // Get all the projetList
        restProjetMockMvc.perform(get("/api/projets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projet.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO))))
            .andExpect(jsonPath("$.[*].videoContentType").value(hasItem(DEFAULT_VIDEO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].video").value(hasItem(Base64Utils.encodeToString(DEFAULT_VIDEO))))
            .andExpect(jsonPath("$.[*].objectif").value(hasItem(DEFAULT_OBJECTIF.doubleValue())))
            .andExpect(jsonPath("$.[*].soldeCours").value(hasItem(DEFAULT_SOLDE_COURS.doubleValue())))
            .andExpect(jsonPath("$.[*].nbJoursRestant").value(hasItem(DEFAULT_NB_JOURS_RESTANT)));
    }
    
    @Test
    @Transactional
    public void getProjet() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        // Get the projet
        restProjetMockMvc.perform(get("/api/projets/{id}", projet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(projet.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.photoContentType").value(DEFAULT_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.photo").value(Base64Utils.encodeToString(DEFAULT_PHOTO)))
            .andExpect(jsonPath("$.videoContentType").value(DEFAULT_VIDEO_CONTENT_TYPE))
            .andExpect(jsonPath("$.video").value(Base64Utils.encodeToString(DEFAULT_VIDEO)))
            .andExpect(jsonPath("$.objectif").value(DEFAULT_OBJECTIF.doubleValue()))
            .andExpect(jsonPath("$.soldeCours").value(DEFAULT_SOLDE_COURS.doubleValue()))
            .andExpect(jsonPath("$.nbJoursRestant").value(DEFAULT_NB_JOURS_RESTANT));
    }

    @Test
    @Transactional
    public void getNonExistingProjet() throws Exception {
        // Get the projet
        restProjetMockMvc.perform(get("/api/projets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjet() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        int databaseSizeBeforeUpdate = projetRepository.findAll().size();

        // Update the projet
        Projet updatedProjet = projetRepository.findById(projet.getId()).get();
        // Disconnect from session so that the updates on updatedProjet are not directly saved in db
        em.detach(updatedProjet);
        updatedProjet
            .nom(UPDATED_NOM)
            .description(UPDATED_DESCRIPTION)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .video(UPDATED_VIDEO)
            .videoContentType(UPDATED_VIDEO_CONTENT_TYPE)
            .objectif(UPDATED_OBJECTIF)
            .soldeCours(UPDATED_SOLDE_COURS)
            .nbJoursRestant(UPDATED_NB_JOURS_RESTANT);

        restProjetMockMvc.perform(put("/api/projets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProjet)))
            .andExpect(status().isOk());

        // Validate the Projet in the database
        List<Projet> projetList = projetRepository.findAll();
        assertThat(projetList).hasSize(databaseSizeBeforeUpdate);
        Projet testProjet = projetList.get(projetList.size() - 1);
        assertThat(testProjet.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testProjet.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProjet.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testProjet.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testProjet.getVideo()).isEqualTo(UPDATED_VIDEO);
        assertThat(testProjet.getVideoContentType()).isEqualTo(UPDATED_VIDEO_CONTENT_TYPE);
        assertThat(testProjet.getObjectif()).isEqualTo(UPDATED_OBJECTIF);
        assertThat(testProjet.getSoldeCours()).isEqualTo(UPDATED_SOLDE_COURS);
        assertThat(testProjet.getNbJoursRestant()).isEqualTo(UPDATED_NB_JOURS_RESTANT);
    }

    @Test
    @Transactional
    public void updateNonExistingProjet() throws Exception {
        int databaseSizeBeforeUpdate = projetRepository.findAll().size();

        // Create the Projet

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjetMockMvc.perform(put("/api/projets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projet)))
            .andExpect(status().isBadRequest());

        // Validate the Projet in the database
        List<Projet> projetList = projetRepository.findAll();
        assertThat(projetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProjet() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        int databaseSizeBeforeDelete = projetRepository.findAll().size();

        // Delete the projet
        restProjetMockMvc.perform(delete("/api/projets/{id}", projet.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Projet> projetList = projetRepository.findAll();
        assertThat(projetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
