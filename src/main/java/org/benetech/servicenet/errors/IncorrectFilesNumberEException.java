package org.benetech.servicenet.errors;

public class IncorrectFilesNumberEException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public IncorrectFilesNumberEException(final int correctFilesNumber) {
        super(ErrorConstants.INCORRECT_FILES_NUMBER,
            String.format("Incorrect number of files to process. There should be %d files", correctFilesNumber),
            "filesProcessing", "incorrectFilesNumber");
    }
}
