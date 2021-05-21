package com.chis.communityhealthis.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

@Repository
public abstract class GenericDaoImpl<E, K extends Serializable> implements GenericDao<E,K> {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    protected Class<? extends E> daoType;

    public GenericDaoImpl() {
        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        daoType = (Class) pt.getActualTypeArguments()[0];
    }

    protected Session currentSession() {
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void add(E entity) {
        currentSession().save(entity);
    }

    @Override
    public void saveOrUpdate(E entity) {
        currentSession().saveOrUpdate(entity);
    }

    @Override
    public void update(E entity) {
        currentSession().update(entity);
    }

    @Override
    public void remove(E entity) {
        currentSession().remove(entity);
    }

    @Override
    public E find(K key) {
        return currentSession().get(daoType, key);
    }

    @Override
    public List<E> getAll() {
        CriteriaQuery cq = currentSession().getCriteriaBuilder().createQuery(daoType);
        cq.from(daoType);
        return currentSession().createQuery(cq).getResultList();
    }
}
