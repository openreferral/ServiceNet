package org.benetech.servicenet.service.dto;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.benetech.servicenet.web.rest.TestUtil;

public class UserGroupDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserGroupDTO.class);
        UserGroupDTO userGroupDTO1 = new UserGroupDTO();
        userGroupDTO1.setId(UUID.randomUUID());
        UserGroupDTO userGroupDTO2 = new UserGroupDTO();
        assertThat(userGroupDTO1).isNotEqualTo(userGroupDTO2);
        userGroupDTO2.setId(userGroupDTO1.getId());
        assertThat(userGroupDTO1).isEqualTo(userGroupDTO2);
        userGroupDTO2.setId(UUID.randomUUID());
        assertThat(userGroupDTO1).isNotEqualTo(userGroupDTO2);
        userGroupDTO1.setId(null);
        assertThat(userGroupDTO1).isNotEqualTo(userGroupDTO2);
    }
}
