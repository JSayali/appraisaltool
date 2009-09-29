/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.web.controller;
import cz.strmik.cmmitool.dao.UserDao;
import cz.strmik.cmmitool.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public abstract class AbstractController {
    
    @Autowired
    private UserDao userDao;

    public User getLoggedUser() {
        String loggedUserId = ((UserDetails)
                SecurityContextHolder.getContext().getAuthentication().
                getPrincipal()).getUsername();

        return userDao.findUser(loggedUserId);
    }


}
