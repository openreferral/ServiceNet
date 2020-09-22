package org.benetech.servicenet;

import java.lang.reflect.Method;
import java.util.Arrays;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.jsmart.zerocode.core.domain.LoadWith;
import org.jsmart.zerocode.core.domain.TestMapping;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// A zerocode ParallelLoadExtension with csv reporting disabled
// Prevents duplicate rows when the runner already generates reports
@API(
    status = Status.EXPERIMENTAL
)
public class ParallelLoadExtension implements BeforeEachCallback {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParallelLoadExtension.class);

    public ParallelLoadExtension() {
    }

    public void beforeEach(ExtensionContext extensionContext) {
        Method testMethod = extensionContext.getRequiredTestMethod();
        Class<?> testClass = extensionContext.getRequiredTestClass();
        String loadPropertiesFile = this.validateAndGetLoadPropertiesFile(testClass, testMethod);
        JupiterLoadProcessor loadProcessor = new JupiterLoadProcessor(loadPropertiesFile);
        TestMapping[] testMappingArray = testMethod.getAnnotationsByType(TestMapping.class);
        Arrays.stream(testMappingArray).forEach((thisMapping) -> {
            loadProcessor.addJupiterTest(thisMapping.testClass(), thisMapping.testMethod());
        });
        boolean hasFailed = loadProcessor.processMultiLoad();

        if (hasFailed) {
            this.failTest(testMethod, testClass);
        } else {
            LOGGER.info("\nAll Passed \ud83d\udc3c. \nSee the granular 'csv report' for individual test statistics.");
        }

    }

    private void failTest(Method testMethod, Class<?> testClass) {
        String failureMessage = testClass.getName() + " with load/stress test(s): " + testMethod + " have Failed";
        LOGGER.error("\n" + failureMessage + ". \n\ud83d\udc47" + "\na) See the 'target/' for granular 'csv report' for pass/fail/response-delay statistics.\ud83d\ude0e" + "\n-Also- " + "\nb) See the 'target/logs' for individual failures by their correlation-ID.\n\n");
        String testDescription = testClass + "#" + testMethod;
        Assertions.fail(testDescription, new RuntimeException(failureMessage));
    }

    @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
    protected String validateAndGetLoadPropertiesFile(Class<?> testClass, Method method) {
        LoadWith loadClassWith = testClass.getAnnotation(LoadWith.class);
        LoadWith loadMethodWith = method.getAnnotation(LoadWith.class);
        if (loadClassWith != null) {
            return loadClassWith.value();
        } else if (loadMethodWith != null) {
            return loadMethodWith.value();
        } else {
            throw new RuntimeException(String.format("\n<< Ah! Missing the the @LoadWith(...) on this Class '%s' or Method '%s' >> ", testClass.getName(), method.getName()));
        }
    }
}
