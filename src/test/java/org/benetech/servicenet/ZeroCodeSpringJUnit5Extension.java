package org.benetech.servicenet;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.runner.Description;

public class ZeroCodeSpringJUnit5Extension implements BeforeEachCallback, AfterEachCallback {

    private final ZeroCodeSpringReportBuilder builder = new ZeroCodeSpringReportBuilder();

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        builder.setDescription(Description.createTestDescription(
            extensionContext.getRequiredTestClass(), extensionContext.getRequiredTestMethod().getName()));
        builder.prepareRequestReport();
        builder.testRunStarted();
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) {
        builder.setPassed(extensionContext.getExecutionException().isEmpty());
        builder.prepareResponseReport();
        builder.buildReportAndPrintToFile();
        builder.testRunFinished();
    }
}
