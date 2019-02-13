package org.benetech.servicenet;

import org.benetech.servicenet.domain.SystemAccount;
import org.benetech.servicenet.domain.User;
import org.benetech.servicenet.repository.UserRepository;
import org.benetech.servicenet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service class used only for tests, with some methods mocked
 */
@Service
@Transactional
public class TestUserService extends UserService {

    private static final String ADMIN_LOGIN = "admin";

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesAndAccount() {
        return userRepository.findOneByLogin(ADMIN_LOGIN);
    }

    @Override
    public Optional<SystemAccount> getCurrentSystemAccount() {
        return userRepository.findOneByLogin(ADMIN_LOGIN).map(User::getSystemAccount);
    }
}
