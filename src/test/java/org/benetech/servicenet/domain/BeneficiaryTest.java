package org.benetech.servicenet.domain;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.benetech.servicenet.web.rest.TestUtil;

public class BeneficiaryTest {

    UUID id = UUID.randomUUID();
    UUID anotherId = UUID.randomUUID();

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Beneficiary.class);
        Beneficiary beneficiary1 = new Beneficiary();
        beneficiary1.setId(id);
        Beneficiary beneficiary2 = new Beneficiary();
        beneficiary2.setId(beneficiary1.getId());
        assertThat(beneficiary1).isEqualTo(beneficiary2);
        beneficiary2.setId(anotherId);
        assertThat(beneficiary1).isNotEqualTo(beneficiary2);
        beneficiary1.setId(null);
        assertThat(beneficiary1).isNotEqualTo(beneficiary2);
    }
}
