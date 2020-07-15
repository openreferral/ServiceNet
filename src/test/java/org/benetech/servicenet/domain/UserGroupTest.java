package org.benetech.servicenet.domain;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.benetech.servicenet.web.rest.TestUtil;

public class UserGroupTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserGroup.class);
        UserGroup userGroup1 = new UserGroup();
        userGroup1.setId(UUID.randomUUID());
        UserGroup userGroup2 = new UserGroup();
        userGroup2.setId(userGroup1.getId());
        assertThat(userGroup1).isEqualTo(userGroup2);
        userGroup2.setId(UUID.randomUUID());
        assertThat(userGroup1).isNotEqualTo(userGroup2);
        userGroup1.setId(null);
        assertThat(userGroup1).isNotEqualTo(userGroup2);
    }
}
