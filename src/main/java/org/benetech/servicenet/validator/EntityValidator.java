package org.benetech.servicenet.validator;

import lombok.extern.slf4j.Slf4j;
import org.benetech.servicenet.builder.ReportErrorMessageBuilder;
import org.benetech.servicenet.domain.AbstractEntity;
import org.benetech.servicenet.domain.DataImportReport;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

@Slf4j
public final class EntityValidator {

    public static <V extends AbstractEntity> boolean isNotValid(V entity, DataImportReport report, String externalDbId) {
        return !isValid(entity, report, externalDbId);
    }

    public static <V extends AbstractEntity> boolean isValid(V entity, DataImportReport report, String externalDbId) {
        if (entity == null) {
            return false;
        }

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<V>> violations = validator.validate(entity);

        if (!violations.isEmpty()) {
            report.setErrorMessage(
                ReportErrorMessageBuilder.build(violations, entity.getClass().getSimpleName(),
                    report.getErrorMessage(), externalDbId));
            return false;
        }

        return true;
    }

    private EntityValidator() {
    }
}
