/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.web.controller.propertyeditor;

import cz.strmik.cmmitool.dao.UserDao;
import cz.strmik.cmmitool.entity.User;
import java.beans.PropertyEditorSupport;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public class UserEditor extends PropertyEditorSupport {

    private UserDao userDao;

    public UserEditor(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void setAsText(String text) {
        if (StringUtils.isEmpty(text)) {
            setValue(null);
        } else {
            setValue(userDao.findUser(text));
        }
    }

    @Override
    public String getAsText() {
        User value = (User) getValue();
        return (value != null ? value.getId() : "");
    }

}
