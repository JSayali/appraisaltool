/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.beans;

import cz.strmik.cmmitool.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public interface UserDao extends UserDetailsService {

    void createUser(User user);

    User findUser(String id);

    void removeUser(String id);

}
