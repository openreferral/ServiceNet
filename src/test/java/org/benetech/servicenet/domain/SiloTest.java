package org.benetech.servicenet.domain;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.benetech.servicenet.web.rest.TestUtil;

public class SiloTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Silo.class);
        Silo silo1 = new Silo();
        silo1.setId(UUID.randomUUID());
        Silo silo2 = new Silo();
        silo2.setId(silo1.getId());
        assertThat(silo1).isEqualTo(silo2);
        silo2.setId(UUID.randomUUID());
        assertThat(silo1).isNotEqualTo(silo2);
        silo1.setId(null);
        assertThat(silo1).isNotEqualTo(silo2);
    }
}
