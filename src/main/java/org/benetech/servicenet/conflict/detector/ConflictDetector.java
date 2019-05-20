package org.benetech.servicenet.conflict.detector;

import org.benetech.servicenet.domain.AbstractEntity;
import org.benetech.servicenet.domain.Conflict;

import java.util.List;

public interface ConflictDetector<T extends AbstractEntity> {

    List<Conflict> detectConflicts(T current, T offered);
}
