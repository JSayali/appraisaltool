/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.web.controller.propertyeditor;

import cz.strmik.cmmitool.entity.*;
import cz.strmik.cmmitool.dao.GenericDao;
import java.beans.PropertyEditorSupport;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public class MethodEditor extends PropertyEditorSupport {

        private GenericDao<Method, Long> methodDao;

        public MethodEditor(GenericDao<Method, Long> methodDao) {
            this.methodDao = methodDao;
        }

	@Override
	public void setAsText(String text) {
            if(StringUtils.isEmpty(text)) {
                setValue(null);
            } else {
                setValue(methodDao.read(Long.parseLong(text)));
            }
	}

	@Override
	public String getAsText() {
            Method value = (Method) getValue();
            return (value != null ? value.getId().toString() : "");
	}

}
