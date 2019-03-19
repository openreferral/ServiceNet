package org.benetech.servicenet.conflict.detector;

import org.benetech.servicenet.domain.Conflict;

import java.util.List;

public interface ConflictDetector<T> {

    List<Conflict> detectConflicts(T current, T offered);

    boolean areConflicted(T current, T offered);
}
