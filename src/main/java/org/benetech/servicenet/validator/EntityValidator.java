package org.benetech.servicenet.validator;

import lombok.extern.slf4j.Slf4j;
import org.benetech.servicenet.builder.ReportErrorMessageBuilder;
import org.benetech.servicenet.domain.AbstractEntity;
import org.benetech.servicenet.domain.DataImportReport;
import org.benetech.servicenet.service.factory.records.builder.BuilderUtils;

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

        Set<ConstraintViolation<V>> violations = getViolations(entity);

        if (!violations.isEmpty()) {
            report.setErrorMessage(
                ReportErrorMessageBuilder.build(violations, entity.getClass().getSimpleName(),
                    report.getErrorMessage(), externalDbId));
            return false;
        }

        return true;
    }

    public static <V extends AbstractEntity> void validateAndFix(V entity, DataImportReport report, String externalDbId) {
        Set<ConstraintViolation<V>> violations = getViolations(entity);

        if (!violations.isEmpty()) {

            if (report != null) {
                report.setErrorMessage(
                    ReportErrorMessageBuilder.build(violations, entity.getClass().getSimpleName(),
                        report.getErrorMessage(), externalDbId));
            }

            for (ConstraintViolation violation : violations) {
                log.info(
                    "Field for {} with ID: {} is invalid. Replacing with empty string.",
                    entity.getClass(),
                    entity.getId()
                );
                BuilderUtils.setField(
                    entity,
                    violation.getPropertyPath().toString(),
                    "",
                    entity.getClass()
                );
            }
        }
    }

    private static <V extends AbstractEntity> Set<ConstraintViolation<V>> getViolations(V entity) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        return validator.validate(entity);
    }

    private EntityValidator() {
    }
}
