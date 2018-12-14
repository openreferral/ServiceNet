package org.benetech.servicenet.interceptor;

import org.benetech.servicenet.listener.HibernatePostCreateListener;
import org.benetech.servicenet.listener.HibernatePostDeleteListener;
import org.benetech.servicenet.listener.HibernatePostUpdateListener;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

@Component
public class HibernateInterceptor {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private HibernatePostCreateListener postCreateListener;

    @Autowired
    private HibernatePostUpdateListener postUpdateListener;

    @Autowired
    private HibernatePostDeleteListener postDeleteListener;

    @PostConstruct
    public void init() {
        SessionFactoryImpl sessionFactory = entityManagerFactory.unwrap(SessionFactoryImpl.class);
        EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
        registry.getEventListenerGroup(EventType.POST_INSERT).appendListener(postCreateListener);
        registry.getEventListenerGroup(EventType.POST_UPDATE).appendListener(postUpdateListener);
        registry.getEventListenerGroup(EventType.POST_DELETE).appendListener(postDeleteListener);
    }
}
