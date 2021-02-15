package org.benetech.servicenet.converter;

import java.io.IOException;

public abstract class AbstractDataConverter {

    /**
     * @param data data to be converted to unified format (JSON)
     * @return converted file in JSON format
     * @throws IOException if there's problem with reading the file
     */
    public abstract ImportData convert(Object data) throws IOException;
}
