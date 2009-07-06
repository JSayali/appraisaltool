/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.dao;

import cz.strmik.cmmitool.entity.User;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public interface UserDao extends UserDetailsService {

    void createUser(User user);

    User updateUser(User user);

    User findUser(String id);

    List<User> findAll();

    List<User> findActive();

    void removeUser(String id);

}
