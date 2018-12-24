package org.benetech.servicenet.adapter.eden.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TakeAllRequest {

    private String term = "*";

    @NonNull
    private String modifiedSince;

    private boolean takeALL = true;
}
