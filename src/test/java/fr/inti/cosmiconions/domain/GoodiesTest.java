package fr.inti.cosmiconions.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.inti.cosmiconions.web.rest.TestUtil;

public class GoodiesTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Goodies.class);
        Goodies goodies1 = new Goodies();
        goodies1.setId(1L);
        Goodies goodies2 = new Goodies();
        goodies2.setId(goodies1.getId());
        assertThat(goodies1).isEqualTo(goodies2);
        goodies2.setId(2L);
        assertThat(goodies1).isNotEqualTo(goodies2);
        goodies1.setId(null);
        assertThat(goodies1).isNotEqualTo(goodies2);
    }
}
