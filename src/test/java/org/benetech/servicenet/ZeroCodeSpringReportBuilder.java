package org.benetech.servicenet;

import static org.jsmart.zerocode.core.domain.builders.ZeroCodeExecResultBuilder.newInstance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import java.time.LocalDateTime;
import org.jsmart.zerocode.core.di.provider.ObjectMapperProvider;
import org.jsmart.zerocode.core.domain.builders.ZeroCodeExecResultBuilder;
import org.jsmart.zerocode.core.domain.builders.ZeroCodeReportBuilder;
import org.jsmart.zerocode.core.engine.listener.ZeroCodeTestReportListener;
import org.jsmart.zerocode.core.logbuilder.LogCorrelationshipPrinter;
import org.jsmart.zerocode.core.report.ZeroCodeReportGenerator;
import org.jsmart.zerocode.core.report.ZeroCodeReportGeneratorImpl;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZeroCodeSpringReportBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZeroCodeSpringReportBuilder.class);

    private LogCorrelationshipPrinter logCorrelationshipPrinter;

    private boolean passed;

    private Description description;

    private final ZeroCodeTestReportListener testReportListener;

    private String logPrefixRelationshipId;

    private final ObjectMapper objectMapperProvider;

    private final ZeroCodeReportGenerator injectedReportGenerator;

    public ZeroCodeSpringReportBuilder() {
        this.objectMapperProvider = new ObjectMapperProvider().get();
        this.injectedReportGenerator = Guice.createInjector().getInstance(ZeroCodeReportGeneratorImpl.class);
        this.testReportListener = new ZeroCodeTestReportListener(objectMapperProvider, injectedReportGenerator);
    }

    public void buildReportAndPrintToFile() {
        ZeroCodeExecResultBuilder reportResultBuilder = newInstance().loop(0).scenarioName(description.getClassName());
        reportResultBuilder.step(logCorrelationshipPrinter.buildReportSingleStep());

        ZeroCodeReportBuilder reportBuilder = ZeroCodeReportBuilder.newInstance().timeStamp(LocalDateTime.now());
        reportBuilder.result(reportResultBuilder.build());
        reportBuilder.printToFile(description.getClassName() + logCorrelationshipPrinter.getCorrelationId() + ".json");
    }

    public void prepareResponseReport() {
        LocalDateTime timeNow = LocalDateTime.now();
        LOGGER.info("JUnit *responseTimeStamp:{}, \nJUnit Response:{}", timeNow, logPrefixRelationshipId);
        logCorrelationshipPrinter.aResponseBuilder()
            .relationshipId(logPrefixRelationshipId)
            .responseTimeStamp(timeNow);

        logCorrelationshipPrinter.result(passed);
        logCorrelationshipPrinter.buildResponseDelay();
    }

    public void prepareRequestReport() {
        logCorrelationshipPrinter = LogCorrelationshipPrinter.newInstance(LOGGER);
        logCorrelationshipPrinter.stepLoop(0);
        logPrefixRelationshipId = logCorrelationshipPrinter.createRelationshipId();
        LocalDateTime timeNow = LocalDateTime.now();
        String stepName = description.getMethodName();
        logCorrelationshipPrinter.aRequestBuilder()
            .stepLoop(0)
            .relationshipId(logPrefixRelationshipId)
            .requestTimeStamp(timeNow)
            .step(stepName);
        LOGGER.info("JUnit *requestTimeStamp:{}, \nJUnit Request:{}", timeNow, logPrefixRelationshipId);
    }

    public void testRunStarted() throws Exception {
        testReportListener.testRunStarted(description);
    }

    public void testRunFinished() {
        testReportListener.testRunFinished(new Result());
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public ZeroCodeTestReportListener getTestReportListener() {
        return testReportListener;
    }
}
