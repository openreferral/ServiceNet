package org.benetech.servicenet.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.benetech.servicenet.domain.ClientProfile;
import org.benetech.servicenet.repository.ClientProfileRepository;
import org.benetech.servicenet.service.ClientProfileService;
import org.benetech.servicenet.service.dto.ClientProfileDto;
import org.benetech.servicenet.service.mapper.ClientProfileMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClientProfileServiceImpl implements ClientProfileService {

    private final ClientProfileRepository clientProfileRepository;

    private final ClientProfileMapper clientProfileMapper;

    public ClientProfileServiceImpl(ClientProfileRepository clientProfileRepository,
        ClientProfileMapper clientProfileMapper) {
        this.clientProfileRepository = clientProfileRepository;
        this.clientProfileMapper = clientProfileMapper;
    }

    @Override
    public ClientProfileDto save(ClientProfileDto clientProfileDto) {
        ClientProfile clientProfile = clientProfileMapper.toEntity(clientProfileDto);
        return clientProfileMapper.toDto(clientProfileRepository.save(clientProfile));
    }

    @Override
    public ClientProfileDto update(ClientProfileDto clientProfileDto) {
        Optional<ClientProfile> optionalClientProfile = clientProfileRepository
            .findByClientId(clientProfileDto.getClientId());
        ClientProfile clientProfile = clientProfileMapper.toEntity(clientProfileDto);
        if (optionalClientProfile.isPresent()) {
            clientProfile.setId(optionalClientProfile.get().getId());
        }
        ClientProfile update = clientProfileRepository.save(clientProfile);
        return clientProfileMapper.toDto(update);
    }

    @Override
    public List<ClientProfileDto> findAll() {
        return clientProfileRepository.findAll().stream()
            .map(clientProfileMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Optional<ClientProfileDto> findOne(String clientId) {
        return clientProfileRepository.findByClientId(clientId)
            .map(clientProfileMapper::toDto);
    }

    @Override
    public Optional<ClientProfile> findById(String clientId) {
        return clientProfileRepository.findByClientId(clientId);
    }

    @Override
    public void delete(String clientId) {
        clientProfileRepository.deleteByClientId(clientId);
    }
}
