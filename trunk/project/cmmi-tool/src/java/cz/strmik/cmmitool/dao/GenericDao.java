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

/**
 * CRUD + findAll + findByNamedQuerd generic DAO.
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public interface GenericDao <T, PK extends Serializable> {

    /**
     * Persists new object.
     * 
     * @param newObject new Entity class
     * @return Persited object.
     */
    T create(T newObject);

    /**
     * Updates entity. Object must be managed with entity
     * manager.
     *
     * @param transientObject
     * @return Updated object.
     */
    T update(T transientObject);

    /**
     * Finds entity by primary key.
     *
     * @param id Primary key.
     * @return Enetity with specified primary key.
     */
    T read(PK id);

    /**
     * Removes entity with specified id form database.
     *
     * @param id Primary key of removed entity.
     */
    void delete(PK id);

    /**
     * Return list of all persisted entities.
     *
     * @return All entities.
     */
    List<T> findAll();

    /**
     * Executes specified named query with parameters.
     *
     * @param queryName Name of query (withnout entity name).
     * @param parameters Must be even count, [String, Object] or
     * null, when you dont eant to speficy parameters.
     * 
     * @return List of all entitties found by specified query.
     *
     * @throws IllegalArugmentException when parameters is
     * wrong count or every first (0,2,4,...) parameters is
     * not String or when specified query does not exists.
     */
    List<T> findByNamedQuery(String queryName, Object... parameters);

}
