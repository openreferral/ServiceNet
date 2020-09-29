package org.benetech.servicenet.service.mapper;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ReferralMapperTest {

    private ReferralMapper referralMapper;

    @BeforeEach
    public void setUp() {
        referralMapper = new ReferralMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        UUID id = UUID.randomUUID();
        assertThat(referralMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(referralMapper.fromId(null)).isNull();
    }
}
