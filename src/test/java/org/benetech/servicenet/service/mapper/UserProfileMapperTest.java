package org.benetech.servicenet.service.mapper;

import java.util.UUID;
import org.benetech.servicenet.domain.UserProfile;
import org.benetech.servicenet.repository.UserProfileRepository;
import org.benetech.servicenet.service.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link UserMapper}.
 */
public class UserProfileMapperTest {

    private static final String DEFAULT_LOGIN = "johndoe";
    private static final UUID DEFAULT_ID = UUID.randomUUID();

    private UserMapper userMapper;
    private SystemAccountMapper systemAccountMapper;
    private UserProfile userProfile;
    @Mock
    private UserProfileRepository userProfileRepository;
    private UserDTO userDto;

    @BeforeEach
    public void init() {
        systemAccountMapper = new SystemAccountMapperImpl();
        userMapper = new UserMapper(systemAccountMapper, null, userProfileRepository);
        userProfile = new UserProfile();
        userProfile.setLogin(DEFAULT_LOGIN);

        userDto = new UserDTO(userProfile);
    }

    @Test
    public void usersToUserDTOsShouldMapOnlyNonNullUsers() {
        List<UserProfile> userProfiles = new ArrayList<>();
        userProfiles.add(userProfile);
        userProfiles.add(null);

        List<UserDTO> userDTOS = userMapper.usersToUserDTOs(userProfiles);

        assertThat(userDTOS).isNotEmpty();
        assertThat(userDTOS).size().isEqualTo(1);
    }

    @Test
    public void userDTOsToUsersShouldMapOnlyNonNullUsers() {
        List<UserDTO> usersDto = new ArrayList<>();
        usersDto.add(userDto);
        usersDto.add(null);

        List<UserProfile> userProfiles = userMapper.userDTOsToUsers(usersDto);

        assertThat(userProfiles).isNotEmpty();
        assertThat(userProfiles).size().isEqualTo(1);
    }

    @Test
    public void userDTOToUserMapWithNullUserShouldReturnNull() {
        assertThat(userMapper.userDTOToUser(null)).isNull();
    }

    @Test
    public void testUserFromId() {
        assertThat(userMapper.userFromId(DEFAULT_ID).getId()).isEqualTo(DEFAULT_ID);
        assertThat(userMapper.userFromId(null)).isNull();
    }
}
