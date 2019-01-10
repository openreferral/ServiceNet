package org.benetech.servicenet.conflict.detector;

import org.benetech.servicenet.domain.Conflict;

import java.util.List;

public interface ConflictDetector <T> {

    List<Conflict> detect(T current, T offered);
}
