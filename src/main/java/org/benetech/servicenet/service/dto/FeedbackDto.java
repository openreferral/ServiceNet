package org.benetech.servicenet.service.dto;

import java.io.Serializable;
import lombok.Getter;

/**
 * A DTO for the Feedback.
 */
public class FeedbackDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    private String emailAddress;

    @Getter
    private String message;

    @Override
    public String toString() {
        return "Address: " + this.emailAddress +
            " Message: " + this.message;
    }
}
