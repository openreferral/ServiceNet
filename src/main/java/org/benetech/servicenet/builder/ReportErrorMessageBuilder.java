package org.benetech.servicenet.builder;

import org.apache.commons.lang3.StringUtils;
import org.benetech.servicenet.domain.AbstractEntity;
import org.benetech.servicenet.domain.DataImportReport;

import javax.validation.ConstraintViolation;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class ReportErrorMessageBuilder {

    private static final String SPACE = " ";
    private static final String LINE_END = "\n";
    private static final String FIRST_LINE = "During the import, the following problems occurs:" + LINE_END + LINE_END;
    private static final String FOR_CLASS = "For object related to/identified with id=%s, of class %s:" + LINE_END;

    public static <V extends AbstractEntity> String build(Set<ConstraintViolation<V>> violations, String className,
                                                          String previousPart, String externalDbId) {
        StringBuilder builder = initializeBuilder(previousPart, className, externalDbId);
        for (ConstraintViolation<V> violation : sort(violations)) {
            builder.append(violation.getPropertyPath())
                .append(SPACE)
                .append(violation.getMessage())
                .append(LINE_END);
        }

        return builder.toString();
    }

    public static String buildForError(String message, DataImportReport report) {
        String prevPart;
        if (StringUtils.isBlank(report.getErrorMessage())) {
            prevPart = FIRST_LINE;
        } else {
            prevPart = report.getErrorMessage();
        }

        return prevPart + message + LINE_END + LINE_END;
    }

    private static <V extends AbstractEntity> List<ConstraintViolation<V>> sort(Set<ConstraintViolation<V>> entry) {
        return entry.stream()
            .sorted(Comparator.comparing(x-> x.getPropertyPath() + x.getMessage()))
            .collect(Collectors.toList());
    }

    private static StringBuilder initializeBuilder(String previousPart, String className, String externalDbId) {
        StringBuilder builder;
        if (StringUtils.isBlank(previousPart)) {
            builder = new StringBuilder(FIRST_LINE);
        } else {
            builder = new StringBuilder(previousPart + LINE_END);
        }
        return builder.append(String.format(FOR_CLASS, externalDbId, className));
    }

    private ReportErrorMessageBuilder() {
    }
}
