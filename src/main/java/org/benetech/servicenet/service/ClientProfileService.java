package org.benetech.servicenet.service;

import java.util.List;
import java.util.Optional;
import org.benetech.servicenet.domain.ClientProfile;
import org.benetech.servicenet.service.dto.ClientProfileDto;

public interface ClientProfileService {

    ClientProfile save(ClientProfileDto clientProfileDto);

    ClientProfileDto save(ClientProfile clientProfile);

    ClientProfileDto update(ClientProfileDto clientProfileDto);

    List<ClientProfileDto> findAll();

    Optional<ClientProfileDto> findOne(String clientId);

    Optional<ClientProfile> findById(String clientId);

    void delete(String clientId);
}
