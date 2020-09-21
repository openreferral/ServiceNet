package org.benetech.servicenet;

import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.internal.runners.statements.Fail;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.ProfileValueUtils;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

public class ZeroCodeSpringJUnit4Runner extends SpringJUnit4ClassRunner {

    private final ZeroCodeSpringReportBuilder builder = new ZeroCodeSpringReportBuilder();

    private static final Logger LOGGER = LoggerFactory.getLogger(ZeroCodeSpringJUnit4Runner.class);

    public ZeroCodeSpringJUnit4Runner(Class<?> clazz) throws InitializationError {
        super(clazz);
        builder.setDescription(getDescription());
    }

    @Override
    public void run(RunNotifier notifier) {
        if (!ProfileValueUtils.isTestEnabledInThisEnvironment(getTestClass().getJavaClass())) {
            notifier.fireTestIgnored(builder.getDescription());
            return;
        }
        // notifier.addListener(builder.getTestReportListener());
        try {
            builder.testRunStarted();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        super.run(notifier);
        builder.testRunFinished();
    }

    @SuppressWarnings("PMD")
    @Override
    protected void runChild(FrameworkMethod frameworkMethod, RunNotifier notifier) {
        Description description = describeChild(frameworkMethod);
        if (isTestMethodIgnored(frameworkMethod)) {
            notifier.fireTestIgnored(description);
        }
        else {
            Statement statement;
            try {
                statement = methodBlock(frameworkMethod);
            }
            catch (Throwable ex) {
                statement = new Fail(ex);
            }
            runLeafJUnitTest(statement, description, notifier);
        }
    }

    @SuppressWarnings("PMD")
    private void runLeafJUnitTest(Statement statement, Description description,
                                        RunNotifier notifier) {
        LOGGER.info("Running a pure JUnit test...");
        builder.setDescription(description);

        EachTestNotifier eachNotifier = new EachTestNotifier(notifier, description);
        eachNotifier.fireTestStarted();

        builder.prepareRequestReport();

        boolean passed = false;
        try {
            statement.evaluate();
            passed = true;
            LOGGER.info("JUnit test passed = {} ", passed);

        } catch (AssumptionViolatedException e) {
            passed = false;
            LOGGER.warn("JUnit test failed due to : {},  passed = {}", e, passed);

            eachNotifier.addFailedAssumption(e);

        } catch (Throwable e) {
            passed = false;
            LOGGER.warn("JUnit test failed due to : {},  passed = {}", e, passed);

            eachNotifier.addFailure(e);

        } finally {
            LOGGER.info("JUnit test run completed. See the results in the console or log.  passed = {}", passed);
            builder.setPassed(passed);
            builder.prepareResponseReport();
            builder.buildReportAndPrintToFile();

            eachNotifier.fireTestFinished();
        }

    }
}
