package org.benetech.servicenet.web.rest;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.benetech.servicenet.config.Constants;
import org.benetech.servicenet.domain.UserProfile;
import org.benetech.servicenet.errors.BadRequestAlertException;
import org.benetech.servicenet.security.AuthoritiesConstants;
import org.benetech.servicenet.service.SiloService;
import org.benetech.servicenet.service.UserService;
import org.benetech.servicenet.service.dto.SiloDTO;
import org.benetech.servicenet.service.dto.UserDTO;

import io.github.jhipster.web.util.HeaderUtil;

import org.benetech.servicenet.service.dto.UserRegisterDTO;
import org.benetech.servicenet.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing users.
 * <p>
 * This class fetches and updates the {@link UserDTO} entity from ServiceNet Authorization service
 * as well as {@link UserProfile} from ServiceNet core.
 * <p>
 */
@RestController
@RequestMapping("/api")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserService userService;

    private final SiloService siloService;

    public UserResource(UserService userService, SiloService siloService) {
        this.userService = userService;
        this.siloService = siloService;
    }

    /**
     * {@code POST  /users}  : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     *
     * @param userDTO the user to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new user,
     * or with status {@code 400 (Bad Request)} if the login or email is already in use.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     * @throws BadRequestAlertException {@code 400 (Bad Request)} if the login or email is already in use.
     */
    @PostMapping("/users")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) throws URISyntaxException {
        log.debug("REST request to save User : {}", userDTO);

        if (userDTO.getId() != null) {
            throw new BadRequestAlertException("A new user cannot already have an ID", "userManagement", "idexists");
            // Lowercase the user login before comparing with database
        } else {
            UserDTO newUser = userService.createUser(userDTO);
            return ResponseEntity.created(new URI("/api/users/" + newUser.getLogin()))
                .headers(HeaderUtil.createAlert(applicationName,  "userManagement.created", newUser.getLogin()))
                .body(newUser);
        }
    }

    /**
     * {@code PUT /users} : Updates an existing User.
     *
     * @param userDTO the user to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated user.
     */
    @PutMapping("/users")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO) {
        log.debug("REST request to update User : {}", userDTO);
        UserDTO updatedUser = userService.updateUser(userDTO);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "userManagement.updated", userDTO.getLogin()))
            .body(updatedUser);
    }

    /**
     * {@code GET /users} : get all users.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all users.
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers(Pageable pageable) {
        final Page<UserDTO> page = userService.getAllManagedUsers(pageable);
        HttpHeaders headers = PaginationUtil
            .generatePaginationHttpHeaders(page, "/api/users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * {@code GET /users/search} : search for users
     *
     * @param systemAccount the system account of a user
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all users.
     */
    @GetMapping("/users/search")
    public Set<UserDTO> searchUsers(@RequestParam(required = false) String systemAccount) {
        return userService.getAllManagedUsers(PageRequest.of(0, Integer.MAX_VALUE)).stream()
            .filter(u -> systemAccount.equals(u.getSystemAccountName())
        ).collect(Collectors.toSet());
    }

    /**
     * Gets a list of all roles.
     * @return a string list of all roles.
     */
    @GetMapping("/users/authorities")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public List<String> getAuthorities() {
        return userService.getAuthorities();
    }

    /**
     * {@code GET /users/:login} : get the "login" user.
     *
     * @param login the login of the user to find.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the "login" user,
     * or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String login) {
        log.debug("REST request to get User : {}", login);
        return new ResponseEntity<>(userService.getUser(login), HttpStatus.OK);
    }

    /**
     * {@code DELETE /users/:login} : delete the "login" User.
     *
     * @param login the login of the user to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteUser(@PathVariable String login) {
        log.debug("REST request to delete User: {}", login);
        userService.deleteUser(login);
        return ResponseEntity.noContent().headers(
            HeaderUtil.createAlert(applicationName,  "userManagement.deleted", login)
        ).build();
    }

    /**
     * {@code GET  /account} : get the current user.
     *
     * @return the current user.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be returned.
     */
    @GetMapping("/account")
    public UserDTO getAccount() {
        return userService.getAccount();
    }

    /**
     * {@code POST  /register/:siloName} : register user within a silo.
     *
     * @return the current user.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be returned.
     */
    @PostMapping("/register/{siloName}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserDTO> registerAccount(
        @Valid @PathVariable String siloName,
        @Valid @RequestBody UserRegisterDTO userRegisterDTO
    ) throws URISyntaxException {
        Optional<SiloDTO> silo = siloService.findOneByName(siloName);
        if (silo.isEmpty()) {
            throw new BadRequestAlertException("Provided Silo does not exist", "silo", "notFound");
        }
        userRegisterDTO.setSiloId(silo.get().getId());
        UserDTO user = userService.registerUser(userRegisterDTO);
        return ResponseEntity.ok()
            .headers(org.benetech.servicenet.web.rest.util.HeaderUtil
                .createEntityCreationAlert("user", user.getLogin()))
            .body(user);
    }

    /**
     * {@code POST  /register/ : register user.
     *
     * @return the current user.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be returned.
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserDTO> registerUser(
        @Valid @RequestBody UserRegisterDTO userRegisterDTO
    ) throws URISyntaxException {
        UserDTO user = userService.registerUser(userRegisterDTO);
        return ResponseEntity.ok()
            .headers(org.benetech.servicenet.web.rest.util.HeaderUtil
                .createEntityCreationAlert("user", user.getLogin()))
            .body(user);
    }

    /**
     * {@code PUT  /account : update user account.
     *
     * @return the updated current user.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be returned.
     */
    @PutMapping("/account")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDTO> updateAccount(
        @Valid @RequestBody UserDTO userDTO
    ) throws URISyntaxException {
        UserDTO user = userService.updateUser(userDTO);
        return ResponseEntity.ok()
            .headers(org.benetech.servicenet.web.rest.util.HeaderUtil
                .createEntityUpdateAlert("user", user.getLogin()))
            .body(user);
    }
}
