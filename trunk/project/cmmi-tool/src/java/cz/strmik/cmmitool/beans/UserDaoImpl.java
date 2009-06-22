/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.beans;

import cz.strmik.cmmitool.entity.User;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;
    private String defaultAdminPassword;

    public void setDefaultAdminPassword(String defaultAdminPassword) {
        this.defaultAdminPassword = defaultAdminPassword;
    }

    @Override
    public void createUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public User findUser(String id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public void removeUser(String id) {
        entityManager.remove(findUser(id));
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException, DataAccessException {
        User user = findUser(userName);
        if (user == null) {
            throw new UsernameNotFoundException("User with id=" + userName + " not found!");
        }
        return user;
    }

    private static Log _log = LogFactory.getLog(UserDao.class);
}
