package org.benetech.servicenet.service.mapper;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class BeneficiaryMapperTest {

    private BeneficiaryMapper beneficiaryMapper;

    @BeforeEach
    public void setUp() {
        beneficiaryMapper = new BeneficiaryMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        UUID id = UUID.randomUUID();
        assertThat(beneficiaryMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(beneficiaryMapper.fromId(null)).isNull();
    }
}
