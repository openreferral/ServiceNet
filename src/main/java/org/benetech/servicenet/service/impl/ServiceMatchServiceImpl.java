package org.benetech.servicenet.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.benetech.servicenet.domain.ServiceMatch;
import org.benetech.servicenet.repository.ServiceMatchRepository;
import org.benetech.servicenet.service.ServiceMatchService;
import org.benetech.servicenet.service.dto.ServiceMatchDto;
import org.benetech.servicenet.service.mapper.ServiceMatchMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServiceMatchServiceImpl implements ServiceMatchService {

    private final Logger log = LoggerFactory.getLogger(ServiceMatchServiceImpl.class);

    @Autowired
    private ServiceMatchRepository serviceMatchRepository;

    @Autowired
    private ServiceMatchMapper mapper;

    @Override
    public ServiceMatchDto save(ServiceMatchDto serviceMatchDto) {
        Optional<ServiceMatch> existingMatch = serviceMatchRepository.findByServiceIdAndMatchingServiceId(
            serviceMatchDto.getService(), serviceMatchDto.getMatchingService());
        if (existingMatch.isEmpty()) {
            log.debug("Request to save ServiceMatch for service id : {} and matching service id: {}",
                serviceMatchDto.getService(), serviceMatchDto.getMatchingService());
            return mapper.toDto(serviceMatchRepository.save(mapper.toEntity(serviceMatchDto)));
        } else {
            return mapper.toDto(existingMatch.get());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceMatchDto> findAllForService(UUID serviceId) {
        log.debug("Request to get all ServiceMatches for service id : {}", serviceId);
        return serviceMatchRepository.findByServiceId(serviceId).stream()
            .map(mapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public void delete(ServiceMatchDto serviceMatchDto) {
        Optional<ServiceMatch> existingMatch = serviceMatchRepository.findByServiceIdAndMatchingServiceId(
            serviceMatchDto.getService(), serviceMatchDto.getMatchingService());
        if (existingMatch.isPresent()) {
            log.debug("Request to delete ServiceMatch : {}", existingMatch.get().getId());
            serviceMatchRepository.delete(existingMatch.get());
        }
    }
}
