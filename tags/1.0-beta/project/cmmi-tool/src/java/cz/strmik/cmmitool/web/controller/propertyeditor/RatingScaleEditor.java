/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.web.controller.propertyeditor;

import org.apache.commons.lang.StringUtils;
import cz.strmik.cmmitool.dao.GenericDao;
import cz.strmik.cmmitool.entity.method.RatingScale;
import java.beans.PropertyEditorSupport;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public class RatingScaleEditor extends PropertyEditorSupport {

    private GenericDao<RatingScale, Long> ratingScaleDao;

    public RatingScaleEditor(GenericDao<RatingScale, Long> ratingScaleDao) {
        this.ratingScaleDao = ratingScaleDao;
    }

    @Override
    public void setAsText(String text) {
        if (StringUtils.isEmpty(text)) {
            setValue(null);
        } else {
            setValue(ratingScaleDao.read(Long.parseLong(text)));
        }
    }

    @Override
    public String getAsText() {
        RatingScale value = (RatingScale) getValue();
        return (value != null ? value.getId().toString() : "");
    }
}
