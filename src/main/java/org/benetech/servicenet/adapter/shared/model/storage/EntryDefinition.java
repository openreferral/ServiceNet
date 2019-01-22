package org.benetech.servicenet.adapter.shared.model.storage;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class EntryDefinition<T> {

    private Class baseClass;

    private Class relatedToClass;

    private T entry;
}
