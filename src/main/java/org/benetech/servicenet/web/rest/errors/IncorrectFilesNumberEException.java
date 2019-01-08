package org.benetech.servicenet.web.rest.errors;

public class IncorrectFilesNumberEException extends BadRequestAlertException {

    public IncorrectFilesNumberEException(final int correctFilesNumber) {
        super(ErrorConstants.INCORRECT_FILES_NUMBER,
            String.format("Incorrect number of files to process. There should be %d files", correctFilesNumber),
            "filesProcessing", "incorrectFilesNumber");
    }
}
