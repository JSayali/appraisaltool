/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.web.controller.propertyeditor;

import cz.strmik.cmmitool.entity.model.Model;
import cz.strmik.cmmitool.entity.*;
import cz.strmik.cmmitool.dao.GenericDao;
import java.beans.PropertyEditorSupport;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public class ModelEditor extends PropertyEditorSupport {

    private GenericDao<Model, Long> modelDao;

    public ModelEditor(GenericDao<Model, Long> modelDao) {
        this.modelDao = modelDao;
    }

    @Override
    public void setAsText(String text) {
        if (StringUtils.isEmpty(text)) {
            setValue(null);
        } else {
            setValue(modelDao.read(Long.parseLong(text)));
        }
    }

    @Override
    public String getAsText() {
        Model value = (Model) getValue();
        return (value != null ? value.getId().toString() : "");
    }
    
}
