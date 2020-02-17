package org.benetech.servicenet.web.rest;

import java.util.UUID;
import org.benetech.servicenet.domain.Option;
import org.benetech.servicenet.domain.enumeration.OptionType;
import org.benetech.servicenet.repository.OptionRepository;
import org.benetech.servicenet.errors.BadRequestAlertException;
import org.benetech.servicenet.web.rest.util.HeaderUtil;
import org.benetech.servicenet.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.benetech.servicenet.security.AuthoritiesConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing Option.
 */
@RestController
@RequestMapping("/api")
public class OptionResource {

    private final Logger log = LoggerFactory.getLogger(OptionResource.class);

    private static final String ENTITY_NAME = "option";

    private final OptionRepository optionRepository;

    public OptionResource(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    /**
     * POST  /options : Create a new option.
     *
     * @param option the option to create
     * @return the ResponseEntity with status 201 (Created) and with body the new option,
     * or with status 400 (Bad Request) if the option has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PostMapping("/options")
    public ResponseEntity<Option> createOption(@RequestBody Option option) throws URISyntaxException {
        log.debug("REST request to save Option : {}", option);
        if (option.getId() != null) {
            throw new BadRequestAlertException("A new option cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Option result = optionRepository.save(option);
        return ResponseEntity.created(new URI("/api/options/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /options : Updates an existing option.
     *
     * @param option the option to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated option,
     * or with status 400 (Bad Request) if the option is not valid,
     * or with status 500 (Internal Server Error) if the option couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PutMapping("/options")
    public ResponseEntity<Option> updateOption(@RequestBody Option option) throws URISyntaxException {
        log.debug("REST request to update Option : {}", option);
        if (option.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Option result = optionRepository.save(option);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, option.getId().toString()))
            .body(result);
    }

    /**
     * GET  /options : get all the options.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of options in body
     */
    @GetMapping("/options")
    public ResponseEntity<List<Option>> getAllOptions(Pageable pageable) {
        log.debug("REST request to get all Options");
        Page<Option> page = optionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/options");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /options/search : search the options.
     * @param type the type of the option
     *
     * @return the ResponseEntity with status 200 (OK) and the list of options in body
     */
    @GetMapping("/options/search")
    public List<Option> searchOptions(@RequestParam(required = false) OptionType type) {
        log.debug("REST request to get options with type: {}", type);
        if (type != null) {
            return optionRepository.findByType(type);
        } else {
            return optionRepository.findAll();
        }
    }

    /**
     * GET  /options/:id : get the "id" option.
     *
     * @param id the id of the option to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the option, or with status 404 (Not Found)
     */
    @GetMapping("/options/{id}")
    public ResponseEntity<Option> getOption(@PathVariable UUID id) {
        log.debug("REST request to get Option : {}", id);
        Optional<Option> option = optionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(option);
    }

    /**
     * DELETE  /options/:id : delete the "id" option.
     *
     * @param id the id of the option to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @DeleteMapping("/options/{id}")
    public ResponseEntity<Void> deleteOption(@PathVariable UUID id) {
        log.debug("REST request to delete Option : {}", id);
        optionRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
