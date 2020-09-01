package org.benetech.servicenet.errors;

import com.netflix.hystrix.exception.HystrixBadRequestException;

public class IdAlreadyUsedException extends HystrixBadRequestException {

    private static final long serialVersionUID = 1L;

    public IdAlreadyUsedException() {
        super("Conflict");
    }
}
