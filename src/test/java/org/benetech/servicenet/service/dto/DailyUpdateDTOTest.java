package org.benetech.servicenet.service.dto;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.benetech.servicenet.web.rest.TestUtil;

public class DailyUpdateDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DailyUpdateDTO.class);
        DailyUpdateDTO dailyUpdateDTO1 = new DailyUpdateDTO();
        dailyUpdateDTO1.setId(UUID.randomUUID());
        DailyUpdateDTO dailyUpdateDTO2 = new DailyUpdateDTO();
        assertThat(dailyUpdateDTO1).isNotEqualTo(dailyUpdateDTO2);
        dailyUpdateDTO2.setId(dailyUpdateDTO1.getId());
        assertThat(dailyUpdateDTO1).isEqualTo(dailyUpdateDTO2);
        dailyUpdateDTO2.setId(UUID.randomUUID());
        assertThat(dailyUpdateDTO1).isNotEqualTo(dailyUpdateDTO2);
        dailyUpdateDTO1.setId(null);
        assertThat(dailyUpdateDTO1).isNotEqualTo(dailyUpdateDTO2);
    }
}
