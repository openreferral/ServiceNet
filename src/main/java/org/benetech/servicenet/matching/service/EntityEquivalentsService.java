package org.benetech.servicenet.matching.service;

import org.benetech.servicenet.matching.model.WrappedEquivalent;

public interface EntityEquivalentsService<E extends WrappedEquivalent, V> {

    E generateEquivalent(V base, V partner);
}
