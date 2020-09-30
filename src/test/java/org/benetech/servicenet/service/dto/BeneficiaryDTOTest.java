package org.benetech.servicenet.service.dto;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.benetech.servicenet.web.rest.TestUtil;

public class BeneficiaryDTOTest {

    UUID id = UUID.randomUUID();
    UUID anotherId = UUID.randomUUID();

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BeneficiaryDTO.class);
        BeneficiaryDTO beneficiaryDTO1 = new BeneficiaryDTO();
        beneficiaryDTO1.setId(id);
        BeneficiaryDTO beneficiaryDTO2 = new BeneficiaryDTO();
        assertThat(beneficiaryDTO1).isNotEqualTo(beneficiaryDTO2);
        beneficiaryDTO2.setId(beneficiaryDTO1.getId());
        assertThat(beneficiaryDTO1).isEqualTo(beneficiaryDTO2);
        beneficiaryDTO2.setId(anotherId);
        assertThat(beneficiaryDTO1).isNotEqualTo(beneficiaryDTO2);
        beneficiaryDTO1.setId(null);
        assertThat(beneficiaryDTO1).isNotEqualTo(beneficiaryDTO2);
    }
}
