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
 * CRUD + findAll generic DAO.
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public interface GenericDao <T, PK extends Serializable> {

    T create(T newObject);

    T update(T transientObject);

    T read(PK id);

    void delete(PK id);

    List<T> findAll();

}
