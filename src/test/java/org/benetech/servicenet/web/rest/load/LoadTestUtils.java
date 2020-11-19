package org.benetech.servicenet.web.rest.load;

import java.util.Optional;
import org.benetech.servicenet.domain.Silo;
import org.benetech.servicenet.domain.UserProfile;
import org.benetech.servicenet.repository.SiloRepository;
import org.benetech.servicenet.repository.UserProfileRepository;

public class LoadTestUtils {

    private UserProfileRepository userProfileRepository;

    private SiloRepository siloRepository;

    public LoadTestUtils(UserProfileRepository userProfileRepository, SiloRepository siloRepository) {
        this.userProfileRepository = userProfileRepository;
        this.siloRepository = siloRepository;
    }

    public final static String TEST_SILO_NAME = "testSilo";

    public void setTestSilo(String login) {
        UserProfile up = userProfileRepository.findOneByLogin(login).get();
        Optional<Silo> siloOpt = siloRepository.findAll().stream()
            .filter(silo -> silo.getName().equals(TEST_SILO_NAME)).findFirst();
        Silo silo;
        if (siloOpt.isPresent()) {
            silo = siloOpt.get();
        }
        else {
            silo = new Silo();
            silo.setName(TEST_SILO_NAME);
            silo.setPublic(true);
            silo = siloRepository.save(silo);
        }
        up.setSilo(silo);
        userProfileRepository.save(up);
    }
}
