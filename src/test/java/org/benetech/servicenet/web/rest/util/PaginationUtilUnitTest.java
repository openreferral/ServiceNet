package org.benetech.servicenet.web.rest.util;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests based on parsing algorithm in app/components/util/pagination-util.service.js
 *
 * @see PaginationUtil
 */
public class PaginationUtilUnitTest {

    private static final int FOUR = 4;
    private static final int PAGE = 6;
    private static final int SIZE = 50;
    private static final long TOTAL = 400L;

    @Test
    public void generatePaginationHttpHeadersTest() {
        String baseUrl = "/api/_search/example";
        List<String> content = new ArrayList<>();
        Page<String> page = new PageImpl<>(content, PageRequest.of(PAGE, SIZE), TOTAL);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, baseUrl);
        List<String> strHeaders = headers.get(HttpHeaders.LINK);
        assertNotNull(strHeaders);
        assertEquals(1, strHeaders.size());
        String headerData = strHeaders.get(0);
        assertEquals(FOUR, headerData.split(",").length);
        String expectedData = "</api/_search/example?page=7&size=50>; rel=\"next\","
            + "</api/_search/example?page=5&size=50>; rel=\"prev\","
            + "</api/_search/example?page=7&size=50>; rel=\"last\","
            + "</api/_search/example?page=0&size=50>; rel=\"first\"";
        assertEquals(expectedData, headerData);
        List<String> xTotalCountHeaders = headers.get("X-Total-Count");
        assertEquals(1, xTotalCountHeaders.size());
        assertEquals(TOTAL, (long) Long.valueOf(xTotalCountHeaders.get(0)));
    }

}
