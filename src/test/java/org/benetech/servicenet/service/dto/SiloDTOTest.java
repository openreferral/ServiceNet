package org.benetech.servicenet.service.dto;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.benetech.servicenet.web.rest.TestUtil;

public class SiloDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SiloDTO.class);
        SiloDTO siloDTO1 = new SiloDTO();
        siloDTO1.setId(UUID.randomUUID());
        SiloDTO siloDTO2 = new SiloDTO();
        assertThat(siloDTO1).isNotEqualTo(siloDTO2);
        siloDTO2.setId(siloDTO1.getId());
        assertThat(siloDTO1).isEqualTo(siloDTO2);
        siloDTO2.setId(UUID.randomUUID());
        assertThat(siloDTO1).isNotEqualTo(siloDTO2);
        siloDTO1.setId(null);
        assertThat(siloDTO1).isNotEqualTo(siloDTO2);
    }
}
