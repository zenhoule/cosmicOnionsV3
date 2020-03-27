package fr.inti.cosmiconions.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import fr.inti.cosmiconions.web.rest.TestUtil;

public class LeTypeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LeType.class);
        LeType leType1 = new LeType();
        leType1.setId(1L);
        LeType leType2 = new LeType();
        leType2.setId(leType1.getId());
        assertThat(leType1).isEqualTo(leType2);
        leType2.setId(2L);
        assertThat(leType1).isNotEqualTo(leType2);
        leType1.setId(null);
        assertThat(leType1).isNotEqualTo(leType2);
    }
}
