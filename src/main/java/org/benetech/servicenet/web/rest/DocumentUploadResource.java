package org.benetech.servicenet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.github.jhipster.web.util.ResponseUtil;
import org.benetech.servicenet.security.AuthoritiesConstants;
import org.benetech.servicenet.adapter.shared.model.FileInfo;
import org.benetech.servicenet.service.DocumentUploadService;
import org.benetech.servicenet.service.dto.DocumentUploadDTO;
import org.benetech.servicenet.errors.BadRequestAlertException;
import org.benetech.servicenet.web.rest.util.HeaderUtil;
import org.benetech.servicenet.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing DocumentUpload.
 */
@RestController
@RequestMapping("/api")
public class DocumentUploadResource {

    private static final String ENTITY_NAME = "documentUpload";
    private final Logger log = LoggerFactory.getLogger(DocumentUploadResource.class);

    @Autowired
    private DocumentUploadService documentUploadService;

    /**
     * POST  /document-uploads : Create a new documentUpload.
     *
     * @param documentUploadDTO the documentUploadDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new documentUploadDTO,
     * or with status 400 (Bad Request) if the documentUpload has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PostMapping("/document-uploads")
    @Timed
    public ResponseEntity<DocumentUploadDTO> createDocumentUpload(
        @Valid @RequestBody DocumentUploadDTO documentUploadDTO) throws URISyntaxException {
        log.debug("REST request to save DocumentUpload : {}", documentUploadDTO);
        if (documentUploadDTO.getId() != null) {
            throw new BadRequestAlertException("A new documentUpload cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DocumentUploadDTO result = documentUploadService.save(documentUploadDTO);
        return ResponseEntity.created(new URI("/api/document-uploads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * POST  /file : upload a file and save information about the upload.
     *
     * @param file the file to be uploaded
     * @return the ResponseEntity with status 201 (Created) and with body the new documentUploadDTO,
     * or with status 400 (Bad Request) if file is of wrong type
     * @throws java.io.IOException if there's problem with reading the file
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @SuppressWarnings("PMD.PreserveStackTrace")
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PostMapping("/file")
    @Timed
    public ResponseEntity<DocumentUploadDTO> uploadDocument(@RequestParam("filepond") MultipartFile file,
                                                            HttpServletRequest request)
        throws URISyntaxException, IOException {
        try {
            DocumentUploadDTO result = documentUploadService.uploadFile(file, request.getHeader("DELIMITER"),
                request.getHeader("PROVIDER"));
            return ResponseEntity.created(new URI("/api/document-uploads/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
        } catch (IllegalArgumentException e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "badrequest");
        }
    }

    /**
     * POST  /map : process files that were uploaded and map the data included in those files
     *
     * @param files that will be mapped
     * @return the ResponseEntity with status 201 (Created) and with body the new documentUploadDTO,
     * or with status 400 (Bad Request) if file is of wrong type
     * @throws JsonSyntaxException if something is wrong with JSON documents
     */
    @SuppressWarnings("PMD.PreserveStackTrace")
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PostMapping("/map")
    @Timed
    public ResponseEntity<Void> mapDocuments(@RequestBody List<String> files,  HttpServletRequest request)
        throws JsonSyntaxException, IOException {
        try {
            List<FileInfo> fileInfoList = new ArrayList<>();
            Gson gson = new Gson();
            for (String file : files) {
                fileInfoList.add(gson.fromJson(file, FileInfo.class));
            }

            boolean result = documentUploadService.processFiles(fileInfoList, request.getHeader("PROVIDER"));

            return result ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (IllegalArgumentException e) {
            throw new BadRequestAlertException(e.getMessage(), ENTITY_NAME, "badrequest");
        }
    }

    /**
     * PUT  /document-uploads : Updates an existing documentUpload.
     *
     * @param documentUploadDTO the documentUploadDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated documentUploadDTO,
     * or with status 400 (Bad Request) if the documentUploadDTO is not valid,
     * or with status 500 (Internal Server Error) if the documentUploadDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @PutMapping("/document-uploads")
    @Timed
    public ResponseEntity<DocumentUploadDTO> updateDocumentUpload(
        @Valid @RequestBody DocumentUploadDTO documentUploadDTO) throws URISyntaxException {
        log.debug("REST request to update DocumentUpload : {}", documentUploadDTO);
        if (documentUploadDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DocumentUploadDTO result = documentUploadService.save(documentUploadDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, documentUploadDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /document-uploads : get all the documentUploads.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of documentUploads in body
     */
    @GetMapping("/document-uploads")
    @Timed
    public ResponseEntity<List<DocumentUploadDTO>> getAllDocumentUploads(Pageable pageable) {
        log.debug("REST request to get all DocumentUploads");
        Page<DocumentUploadDTO> page = documentUploadService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/document-uploads");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /document-uploads/:id : get the "id" documentUpload.
     *
     * @param id the id of the documentUploadDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the documentUploadDTO, or with status 404 (Not Found)
     */
    @GetMapping("/document-uploads/{id}")
    @Timed
    public ResponseEntity<DocumentUploadDTO> getDocumentUpload(@PathVariable UUID id) {
        log.debug("REST request to get DocumentUpload : {}", id);
        Optional<DocumentUploadDTO> documentUploadDTO = documentUploadService.findOne(id);
        return ResponseUtil.wrapOrNotFound(documentUploadDTO);
    }

    /**
     * DELETE  /document-uploads/:id : delete the "id" documentUpload.
     *
     * @param id the id of the documentUploadDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @PreAuthorize("hasRole('" + AuthoritiesConstants.ADMIN + "')")
    @DeleteMapping("/document-uploads/{id}")
    @Timed
    public ResponseEntity<Void> deleteDocumentUpload(@PathVariable UUID id) {
        log.debug("REST request to delete DocumentUpload : {}", id);
        documentUploadService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
