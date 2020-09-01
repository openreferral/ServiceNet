package org.benetech.servicenet;

import java.util.UUID;

public class TestConstants {

    public static final UUID UUID_1 = UUID.fromString("C56A4180-65AA-42EC-A945-5FD21DEC0538");

    public static final UUID UUID_2 = UUID.fromString("42BB65E2-ECAB-11E8-8EB2-F2801F1B9FD1");

    public static final UUID UUID_42 = UUID.fromString("DC4435E0-ECAB-11E8-8EB2-F2801F1B9FD1");

    public static final UUID NON_EXISTING_UUID = UUID.fromString("250598AC-ECAF-11E8-8EB2-F2801F1B9FD1");

    public static final String PROVIDER = "provider";
    public static final String NEW_EXTERNAL_ID = "1000";
    public static final String EXISTING_EXTERNAL_ID = "2000";
    public static final String NEW_STRING = "new string";
    public static final String EXISTING_STRING = "existing string";
    public static final String OTHER_STRING = "other string";
    public static final boolean NEW_BOOLEAN = true;
    public static final boolean EXISTING_BOOLEAN = false;
    public static final Integer NEW_INT = 1;
    public static final Integer OTHER_INT = 2;
    public static final Integer EXISTING_INT = 3;
    public static final String SYSTEM = "system";

    private TestConstants() {
    }
}
