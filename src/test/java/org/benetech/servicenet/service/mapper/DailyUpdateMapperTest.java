package org.benetech.servicenet.service.mapper;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class DailyUpdateMapperTest {

    private DailyUpdateMapper dailyUpdateMapper;

    @BeforeEach
    public void setUp() {
        dailyUpdateMapper = new DailyUpdateMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        UUID id = UUID.randomUUID();
        assertThat(dailyUpdateMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(dailyUpdateMapper.fromId(null)).isNull();
    }
}
