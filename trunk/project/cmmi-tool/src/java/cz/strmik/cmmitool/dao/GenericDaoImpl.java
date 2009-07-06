/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.dao;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
@Repository
public class GenericDaoImpl<T, PK extends Serializable>
        implements GenericDao<T, PK> {

    private Class<T> type;
    @PersistenceContext
    private EntityManager entityManager;

    public GenericDaoImpl(Class<T> type) {
        this.type = type;
    }

    @Override
    public T create(T newObject) {
        entityManager.persist(newObject);
        return newObject;
    }

    @Override
    public T update(T transientObject) {
        return entityManager.merge(transientObject);
    }

    @Override
    public T read(PK id) {
        return entityManager.find(type, id);
    }

    @Override
    public void delete(PK id) {
        entityManager.remove(read(id));
    }

    @Override
    public List<T> findAll() {
        try {
            return entityManager.createNamedQuery(type.getSimpleName() + ".findAll").getResultList();
        } catch (IllegalArgumentException ex) {
            throw new UnsupportedOperationException("findAll on "+type.getSimpleName()+" is not supported yet!", ex);
        }
    }

    @Override
    public List<T> findByNamedQuery(String queryName, Object... parameters) {
        checkParameters(parameters);
        Query query = entityManager.createNamedQuery(type.getSimpleName() + "." + queryName);
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i = i + 2) {
                String paramName = (String) parameters[i];
                Object param = parameters[i + 1];
                query.setParameter(paramName, param);
            }
        }
        return query.getResultList();
    }

    private void checkParameters(Object... parameters) {
        if (parameters != null) {
            if (parameters.length % 2 != 0) {
                throw new IllegalArgumentException("invalid count of parameters: " + parameters.length + "!");
            }
            for (int i = 0; i < parameters.length; i = i + 2) {
                if (!(parameters[i] instanceof String)) {
                    throw new IllegalArgumentException("parameter " + i + " is not instance of String!");
                }
            }
        }
    }

}
