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

    public void disableEventListeners() {
        getRegistry().getEventListenerGroup(EventType.POST_INSERT).clear();
        getRegistry().getEventListenerGroup(EventType.POST_UPDATE).clear();
        getRegistry().getEventListenerGroup(EventType.POST_DELETE).clear();
    }

    @PostConstruct
    public void init() {
        getRegistry().getEventListenerGroup(EventType.POST_INSERT).appendListener(postCreateListener);
        getRegistry().getEventListenerGroup(EventType.POST_UPDATE).appendListener(postUpdateListener);
        getRegistry().getEventListenerGroup(EventType.POST_DELETE).appendListener(postDeleteListener);
    }

    private EventListenerRegistry getRegistry() {
        SessionFactoryImpl sessionFactory = entityManagerFactory.unwrap(SessionFactoryImpl.class);
        return sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
    }
}
