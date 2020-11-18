package org.benetech.servicenet.mother;

import org.benetech.servicenet.domain.Program;

public final class ProgramMother {

    public static final String PROGRAM_NAME = "Program Name";

    public static Program createDefault() {
        return new Program()
            .name(PROGRAM_NAME);
    }

    private ProgramMother() {
    }
}
