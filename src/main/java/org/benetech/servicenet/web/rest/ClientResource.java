package org.benetech.servicenet.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.validation.Valid;
import org.benetech.servicenet.domain.ClientProfile;
import org.benetech.servicenet.errors.BadRequestAlertException;
import org.benetech.servicenet.security.AuthoritiesConstants;
import org.benetech.servicenet.service.ClientService;
import org.benetech.servicenet.service.dto.ClientDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing users.
 * <p>
 * This class fetches and updates the {@link org.benetech.servicenet.service.dto.ClientDTO} entity from ServiceNet Authorization service
 * as well as {@link ClientProfile} from ServiceNet core.
 * <p>
 */
@RestController
@RequestMapping("/api")
public class ClientResource {

    private final Logger log = LoggerFactory.getLogger(ClientResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private ClientService clientService;

    /**
     * {@code POST  /clients}  : Creates a new client.
     *
     *
     * @param clientDTO the client to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new client, or with status {@code 400 (Bad Request)} if the id is already in use.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     * @throws BadRequestAlertException {@code 400 (Bad Request)} if the id is already in use.
     */
    @PostMapping("/clients")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<ClientDTO> createClient(@Valid @RequestBody ClientDTO clientDTO) throws URISyntaxException, BadRequestAlertException {
        log.debug("REST request to save Client : {}", clientDTO);

        ClientDTO newClient = clientService.createClient(clientDTO);
        return ResponseEntity.created(new URI("/api/clients/" + newClient.getClientId()))
            .headers(HeaderUtil.createAlert(applicationName,  "clientManagement.created", newClient.getClientId()))
            .body(newClient);
    }

    /**
     * {@code PUT /clients} : Updates an existing Client.
     *
     * @param clientDTO the client to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated client.
     */
    @PutMapping("/clients")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<ClientDTO> updateClient(@Valid @RequestBody ClientDTO clientDTO) {
        log.debug("REST request to update Client : {}", clientDTO);
        ClientDTO updatedClient = clientService.updateClient(clientDTO);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "userManagement.updated", updatedClient.getClientId()))
            .body(updatedClient);
    }


    /**
     * {@code GET /clients} : get all clients.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all clients.
     */
    @GetMapping("/clients")
    public ResponseEntity<List<ClientDTO>> getAllClients(Pageable pageable) {
        final List<ClientDTO> page = clientService.getAllClients(pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * {@code GET /clients/:id} : get the "id" client.
     *
     * @param id the id of the client to find.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the "id" client, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/clients/{id}")
    public ResponseEntity<ClientDTO> getClient(@PathVariable String id) {
        log.debug("REST request to get Client : {}", id);
        return new ResponseEntity<>(clientService.getClient(id), HttpStatus.OK);
    }

    /**
     * {@code DELETE /clients/:id} : delete the "id" Client.
     *
     * @param id the id of the client to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/clients/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteClient(@PathVariable String id) {
        log.debug("REST request to delete Client: {}", id);
        clientService.deleteClient(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName,  "clientManagement.deleted", id)).build();
    }
}
