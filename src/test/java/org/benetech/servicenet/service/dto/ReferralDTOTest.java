package org.benetech.servicenet.service.dto;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.benetech.servicenet.web.rest.TestUtil;

public class ReferralDTOTest {

    UUID id = UUID.randomUUID();
    UUID anotherId = UUID.randomUUID();

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReferralDTO.class);
        ReferralDTO referralDTO1 = new ReferralDTO();
        referralDTO1.setId(id);
        ReferralDTO referralDTO2 = new ReferralDTO();
        assertThat(referralDTO1).isNotEqualTo(referralDTO2);
        referralDTO2.setId(referralDTO1.getId());
        assertThat(referralDTO1).isEqualTo(referralDTO2);
        referralDTO2.setId(anotherId);
        assertThat(referralDTO1).isNotEqualTo(referralDTO2);
        referralDTO1.setId(null);
        assertThat(referralDTO1).isNotEqualTo(referralDTO2);
    }
}
