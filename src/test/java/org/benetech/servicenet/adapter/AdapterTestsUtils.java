package org.benetech.servicenet.adapter;

import org.apache.commons.io.IOUtils;
import org.mockserver.client.MockServerClient;
import org.mockserver.matchers.Times;
import org.mockserver.model.Header;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public final class AdapterTestsUtils {

    private static final String RESOURCE_PACKAGE = "adapters/";

    public static String readResourceAsString(String resourceName) throws IOException {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try (InputStream is = classloader.getResourceAsStream(RESOURCE_PACKAGE + resourceName)) {
            return IOUtils.toString(is, StandardCharsets.UTF_8);
        }
    }

    public static <T extends AbstractElement> void mockEndpointWithBatch(List<T> batch,
                                             String resourceName,
                                             MockServerClient mockServer) throws IOException {
        mockServer.when(request()
                .withMethod("GET")
                .withPath("/v1/Resource")
                .withQueryStringParameter("id", getParameters(batch))
                .withHeaders(
                    new Header("Content-Type", "application/json"),
                    new Header("Authorization", "Bearer default_key")),
            Times.once())
            .respond(response()
                .withStatusCode(200)
                .withBody(AdapterTestsUtils.readResourceAsString(resourceName))
                .withDelay(TimeUnit.SECONDS,1)
            );
    }

    private static <T extends AbstractElement> String[] getParameters(List<T> elements) {
        List<String> params = new ArrayList<>();
        for (T element : elements) {
            params.add(element.getId().toString());
        }

        String[] result = new String[params.size()];
        return params.toArray(result);
    }

    private AdapterTestsUtils() {
    }
}
