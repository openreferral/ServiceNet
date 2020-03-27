package org.benetech.servicenet.repository;

import java.util.UUID;
import org.benetech.servicenet.domain.FieldsDisplaySettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the FieldsDisplaySettings entity.
 */
@Repository
public interface FieldsDisplaySettingsRepository extends
    JpaRepository<FieldsDisplaySettings, UUID> {

    @Query("select fieldsDisplaySettings from FieldsDisplaySettings fieldsDisplaySettings where "
        + "fieldsDisplaySettings.userProfile.login = ?#{principal}")
    List<FieldsDisplaySettings> findByUserIsCurrentUser();

    @Query("select fds from FieldsDisplaySettings fds "
        + "where userProfile.systemAccount.id = :systemAccountId")
    List<FieldsDisplaySettings> findBySystemAccount(@Param("systemAccountId") UUID systemAccountId);

    @Query("select fds from FieldsDisplaySettings fds "
        + "where userProfile.systemAccount.name = :systemAccountName and fds.name = :settingName")
    List<FieldsDisplaySettings> findBySystemAccountAndName(
        @Param("systemAccountName") String systemAccountName,
        @Param("settingName") String settingName
    );
}
