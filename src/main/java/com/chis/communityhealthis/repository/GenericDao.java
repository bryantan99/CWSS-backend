package com.chis.communityhealthis.repository;

import java.util.List;

public interface GenericDao<E,K> {
    K add(E entity) ;
    void saveOrUpdate(E entity) ;
    void update(E entity) ;
    void remove(E entity);
    E find(K key);
    List<E> getAll() ;
}
