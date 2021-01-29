package org.benetech.servicenet.util;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class RequestUtils {

    private RequestUtils() {
    }

    public static Optional<HttpServletRequest> getCurrentHttpRequest() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
            .filter(requestAttributes -> ServletRequestAttributes.class.isAssignableFrom(requestAttributes.getClass()))
            .map(requestAttributes -> ((ServletRequestAttributes) requestAttributes))
            .map(ServletRequestAttributes::getRequest);
    }

    public static String getXForwardedProto() {
        Optional<HttpServletRequest> requestOptional = getCurrentHttpRequest();
        return requestOptional
            .map(httpServletRequest -> httpServletRequest.getHeader("x-forwarded-proto"))
            .orElse(null);
    }

    public static String getXForwardedHost() {
        Optional<HttpServletRequest> requestOptional = getCurrentHttpRequest();
        return requestOptional
            .map(httpServletRequest -> httpServletRequest.getHeader("x-forwarded-host"))
            .orElse(null);
    }

    public static String getBaseUrl() {
        Optional<HttpServletRequest> requestOptional = getCurrentHttpRequest();
        if (requestOptional.isPresent()) {
            HttpServletRequest request = requestOptional.get();
            String proto = request.getHeader("x-forwarded-proto");
            if (StringUtils.isBlank(proto)) {
                proto = request.getScheme();
            }
            String host = request.getHeader("x-forwarded-host");
            if (StringUtils.isBlank(host)) {
                host = request.getRemoteHost();
            }
            return proto + "://" + host;
        }
        return "";
    }
}
