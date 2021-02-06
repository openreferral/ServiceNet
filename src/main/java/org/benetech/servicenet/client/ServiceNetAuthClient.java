package org.benetech.servicenet.client;

import java.util.List;
import javax.validation.Valid;
import org.benetech.servicenet.service.dto.ClientDTO;
import org.benetech.servicenet.service.dto.UserDTO;
import org.benetech.servicenet.service.dto.UserRegisterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@AuthorizedFeignClient(name = "servicenetauth")
public interface ServiceNetAuthClient {

    // User resource

    @RequestMapping(value = "/api/users/authorities", method = RequestMethod.GET)
    List<String> getAuthorities();

    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    Page<UserDTO> getUsers(Pageable pageable);

    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    UserDTO createUser(UserDTO userDTO);

    @RequestMapping(value = "/api/users", method = RequestMethod.PUT)
    UserDTO updateUser(UserDTO userDTO,
        @RequestHeader("x-forwarded-proto") String xForwardedProto,
        @RequestHeader("x-forwarded-host") String xForwardedHost);

    @RequestMapping(value = "/api/users/{login}", method = RequestMethod.GET)
    UserDTO getUser(@PathVariable("login") String login);

    @RequestMapping(value = "/api/users/{login}", method = RequestMethod.DELETE)
    void deleteUser(@PathVariable("login") String login);

    // Client Resource

    @RequestMapping(value = "/api/clients", method = RequestMethod.POST)
    ClientDTO createClient(@Valid @RequestBody ClientDTO clientDTO);

    @RequestMapping(value = "/api/clients", method = RequestMethod.PUT)
    ClientDTO updateClient(@Valid @RequestBody ClientDTO clientDTO);

    @RequestMapping(value = "api/clients/{id}", method = RequestMethod.GET)
    ClientDTO getClient(@PathVariable("id") String id);

    @RequestMapping(value = "/api/clients", method = RequestMethod.GET)
    List<ClientDTO> getAllClients(Pageable pageable);

    @RequestMapping(value = "/api/clients/{id}", method = RequestMethod.DELETE)
    void deleteClient(@PathVariable("id") String id);

    @RequestMapping(value = "/api/register", method = RequestMethod.POST)
    UserDTO registerUser(UserRegisterDTO userRegisterDTO,
        @RequestHeader("x-forwarded-proto") String xForwardedProto,
        @RequestHeader("x-forwarded-host") String xForwardedHost);
}
