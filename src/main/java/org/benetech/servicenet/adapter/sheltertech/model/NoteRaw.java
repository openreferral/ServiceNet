package org.benetech.servicenet.adapter.sheltertech.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class NoteRaw {

    @SerializedName("id")
    private Integer id;

    @SerializedName("note")
    private String note;
}
