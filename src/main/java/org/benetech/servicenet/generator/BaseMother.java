package org.benetech.servicenet.generator;

import javax.persistence.EntityManager;
import org.benetech.servicenet.domain.AbstractEntity;

interface BaseMother<T extends AbstractEntity> {
    T generate(EntityManager em);
}
