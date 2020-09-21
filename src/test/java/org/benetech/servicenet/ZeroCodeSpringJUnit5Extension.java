package org.benetech.servicenet;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.runner.Description;

public class ZeroCodeSpringJUnit5Extension implements BeforeEachCallback, AfterEachCallback,
    BeforeAllCallback, AfterAllCallback {

    private final ZeroCodeSpringReportBuilder builder = new ZeroCodeSpringReportBuilder();

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        builder.setDescription(Description.createTestDescription(
            extensionContext.getRequiredTestClass().getName(),
            extensionContext.getRequiredTestMethod().getName(),
            extensionContext.getUniqueId()));
        builder.prepareRequestReport();
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) {
        builder.setPassed(extensionContext.getExecutionException().isEmpty());
        builder.prepareResponseReport();
        builder.buildReportAndPrintToFile();
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        builder.testRunStarted();
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) {
        builder.testRunFinished();
    }
}
