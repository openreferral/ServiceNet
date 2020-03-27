package org.benetech.servicenet.service.mapper;

import java.util.UUID;
import org.benetech.servicenet.ServiceNetApp;
import org.benetech.servicenet.domain.UserProfile;
import org.benetech.servicenet.service.UserService;
import org.benetech.servicenet.service.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link UserMapper}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceNetApp.class)
public class UserProfileMapperTest {

    private static final String DEFAULT_LOGIN = "johndoe";
    private static final UUID DEFAULT_ID = UUID.randomUUID();

    @Mock
    private UserService userService;

    @Autowired
    SystemAccountMapper systemAccountMapper;

    @Autowired
    @InjectMocks
    private UserMapper userMapper;

    private UserProfile userProfile;
    private UserDTO userDto;

    @Before
    @Transactional
    public void init() {
        userProfile = new UserProfile();
        userProfile.setLogin(DEFAULT_LOGIN);

        userDto = new UserDTO(userProfile);
    }

    @Test
    @Transactional
    public void usersToUserDTOsShouldMapOnlyNonNullUsers() {
        List<UserProfile> userProfiles = new ArrayList<>();
        userProfiles.add(userProfile);
        userProfiles.add(null);

        List<UserDTO> userDTOS = userMapper.usersToUserDTOs(userProfiles);

        assertThat(userDTOS).isNotEmpty();
        assertThat(userDTOS).size().isEqualTo(1);
    }

    @Test
    @Transactional
    public void userDTOsToUsersShouldMapOnlyNonNullUsers() {
        List<UserDTO> usersDto = new ArrayList<>();
        usersDto.add(userDto);
        usersDto.add(null);

        List<UserProfile> userProfiles = userMapper.userDTOsToUsers(usersDto);

        assertThat(userProfiles).isNotEmpty();
        assertThat(userProfiles).size().isEqualTo(1);
    }

    @Test
    @Transactional
    public void userDTOToUserMapWithNullUserShouldReturnNull() {
        assertThat(userMapper.userDTOToUser(null)).isNull();
    }

    @Test
    @Transactional
    public void testUserFromId() {
        assertThat(userMapper.userFromId(DEFAULT_ID).getId()).isEqualTo(DEFAULT_ID);
        assertThat(userMapper.userFromId(null)).isNull();
    }
}
