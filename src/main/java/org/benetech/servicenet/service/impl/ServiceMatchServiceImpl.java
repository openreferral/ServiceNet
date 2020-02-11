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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServiceMatchServiceImpl implements ServiceMatchService {

    @Autowired
    private ServiceMatchRepository serviceMatchRepository;

    @Autowired
    private ServiceMatchMapper mapper;

    @Override
    public ServiceMatchDto save(ServiceMatchDto serviceMatchDto) {
        Optional<ServiceMatch> existingMatch = serviceMatchRepository.findByServiceIdAndMatchingServiceId(
            serviceMatchDto.getService(), serviceMatchDto.getMatchingService());
        if (existingMatch.isEmpty()) {
            return mapper.toDto(serviceMatchRepository.save(mapper.toEntity(serviceMatchDto)));
        } else {
            return mapper.toDto(existingMatch.get());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceMatchDto> findAllForService(UUID serviceId) {
        return serviceMatchRepository.findByServiceId(serviceId).stream()
            .map(mapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public void delete(UUID serviceId, UUID matchingServiceId) {
        serviceMatchRepository.deleteByServiceIdAndMatchingServiceId(serviceId, matchingServiceId);
    }
}
