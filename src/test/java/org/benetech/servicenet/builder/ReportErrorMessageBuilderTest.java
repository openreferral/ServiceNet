package org.benetech.servicenet.builder;

import org.benetech.servicenet.domain.AbstractEntity;
import org.benetech.servicenet.domain.Language;
import org.benetech.servicenet.domain.Organization;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class ReportErrorMessageBuilderTest {

    @Test
    public void shouldBuildProperFirstReport() {
        var result = ReportErrorMessageBuilder.build(getViolations(new Organization()), "Organization", null,"123");

        var expected =
            "During the import, the following problems occurs:\n" +
                "\n" +
                "For object related to/identified with id=123, of class Organization:\n" +
                "account must not be null\n";

        assertEquals(expected, result);
    }

    @Test
    public void shouldBuildProperAnotherPartOfReport() {
        var firstPart = ReportErrorMessageBuilder.build(getViolations(new Organization()), "Organization", null, "123");
        var result = ReportErrorMessageBuilder.build(getViolations(new Language()), "Language", firstPart, "453");

        var expected =
            "During the import, the following problems occurs:\n" +
                "\n" +
                "For object related to/identified with id=123, of class Organization:\n" +
                "account must not be null\n" +
                "\n" +
                "For object related to/identified with id=453, of class Language:\n" +
                "language must not be null" +
                "\n";

        assertEquals(expected, result);
    }

    private <V extends AbstractEntity> Set<ConstraintViolation<V>> getViolations(V entity) {
        var factory = Validation.buildDefaultValidatorFactory();
        return factory.getValidator().validate(entity);
    }
}
