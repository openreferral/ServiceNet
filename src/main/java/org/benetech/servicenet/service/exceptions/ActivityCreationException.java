package org.benetech.servicenet.service.exceptions;

public class ActivityCreationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ActivityCreationException() {
        super();
    }

    public ActivityCreationException(String s) {
        super(s);
    }

    public ActivityCreationException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ActivityCreationException(Throwable throwable) {
        super(throwable);
    }

}
