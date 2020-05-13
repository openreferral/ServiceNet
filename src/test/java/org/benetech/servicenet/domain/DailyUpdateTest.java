package org.benetech.servicenet.domain;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.benetech.servicenet.web.rest.TestUtil;

public class DailyUpdateTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DailyUpdate.class);
        DailyUpdate dailyUpdate1 = new DailyUpdate();
        dailyUpdate1.setId(UUID.randomUUID());
        DailyUpdate dailyUpdate2 = new DailyUpdate();
        dailyUpdate2.setId(dailyUpdate1.getId());
        assertThat(dailyUpdate1).isEqualTo(dailyUpdate2);
        dailyUpdate2.setId(UUID.randomUUID());
        assertThat(dailyUpdate1).isNotEqualTo(dailyUpdate2);
        dailyUpdate1.setId(null);
        assertThat(dailyUpdate1).isNotEqualTo(dailyUpdate2);
    }
}
