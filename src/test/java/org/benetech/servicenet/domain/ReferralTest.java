package org.benetech.servicenet.domain;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.benetech.servicenet.web.rest.TestUtil;

public class ReferralTest {

    UUID id = UUID.randomUUID();
    UUID anotherId = UUID.randomUUID();

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Referral.class);
        Referral referral1 = new Referral();
        referral1.setId(id);
        Referral referral2 = new Referral();
        referral2.setId(referral1.getId());
        assertThat(referral1).isEqualTo(referral2);
        referral2.setId(anotherId);
        assertThat(referral1).isNotEqualTo(referral2);
        referral1.setId(null);
        assertThat(referral1).isNotEqualTo(referral2);
    }
}
