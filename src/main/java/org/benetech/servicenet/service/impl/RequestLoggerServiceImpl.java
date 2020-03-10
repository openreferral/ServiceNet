package org.benetech.servicenet.service.impl;

import java.util.UUID;
import org.benetech.servicenet.service.RequestLoggerService;
import org.benetech.servicenet.domain.RequestLogger;
import org.benetech.servicenet.repository.RequestLoggerRepository;
import org.benetech.servicenet.service.dto.RequestLoggerDTO;
import org.benetech.servicenet.service.mapper.RequestLoggerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link RequestLogger}.
 */
@Service
@Transactional
public class RequestLoggerServiceImpl implements RequestLoggerService {

    private final Logger log = LoggerFactory.getLogger(RequestLoggerServiceImpl.class);

    private final RequestLoggerRepository requestLoggerRepository;

    private final RequestLoggerMapper requestLoggerMapper;

    public RequestLoggerServiceImpl(RequestLoggerRepository requestLoggerRepository, RequestLoggerMapper requestLoggerMapper) {
        this.requestLoggerRepository = requestLoggerRepository;
        this.requestLoggerMapper = requestLoggerMapper;
    }

    /**
     * Save a requestLogger.
     *
     * @param requestLoggerDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public RequestLoggerDTO save(RequestLoggerDTO requestLoggerDTO) {
        log.debug("Request to save RequestLogger : {}", requestLoggerDTO);
        RequestLogger requestLogger = requestLoggerMapper.toEntity(requestLoggerDTO);
        requestLogger = requestLoggerRepository.save(requestLogger);
        return requestLoggerMapper.toDto(requestLogger);
    }

    /**
     * Get all the requestLoggers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RequestLoggerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RequestLoggers");
        return requestLoggerRepository.findAll(pageable)
            .map(requestLoggerMapper::toDto);
    }


    /**
     * Get one requestLogger by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RequestLoggerDTO> findOne(UUID id) {
        log.debug("Request to get RequestLogger : {}", id);
        return requestLoggerRepository.findById(id)
            .map(requestLoggerMapper::toDto);
    }

    /**
     * Delete the requestLogger by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(UUID id) {
        log.debug("Request to delete RequestLogger : {}", id);
        requestLoggerRepository.deleteById(id);
    }
}
