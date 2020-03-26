package org.benetech.servicenet.client;

import java.util.List;
import org.benetech.servicenet.service.dto.UserDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@AuthorizedFeignClient(name = "servicenetauth")
public interface ServiceNetAuthClient {

    @RequestMapping(value = "/api/users/authorities", method = RequestMethod.GET)
    List<String> getAuthorities();

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    List<UserDTO> getUsers(Pageable pageable);

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    UserDTO createUser(UserDTO userDTO);

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    UserDTO updateUser(UserDTO userDTO);

    @RequestMapping(value = "/api/users/{login}", method = RequestMethod.GET)
    UserDTO getUser(@PathVariable("login") String login);

    @RequestMapping(value = "/api/users/{login}", method = RequestMethod.DELETE)
    void deleteUser(@PathVariable("login") String login);
}
