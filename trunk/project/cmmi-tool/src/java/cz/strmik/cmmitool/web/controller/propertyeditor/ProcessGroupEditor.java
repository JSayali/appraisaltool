/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.web.controller.propertyeditor;

import cz.strmik.cmmitool.dao.GenericDao;
import cz.strmik.cmmitool.entity.model.ProcessGroup;
import java.beans.PropertyEditorSupport;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public class ProcessGroupEditor extends PropertyEditorSupport {

        private GenericDao<ProcessGroup, Long> processGroupDao;

        public ProcessGroupEditor(GenericDao<ProcessGroup, Long> processGroupDao) {
            this.processGroupDao = processGroupDao;
        }

	@Override
	public void setAsText(String text) {
            if(StringUtils.isEmpty(text)) {
                setValue(null);
            } else {
                setValue(processGroupDao.read(Long.parseLong(text)));
            }
	}

	@Override
	public String getAsText() {
            ProcessGroup value = (ProcessGroup) getValue();
            return (value != null ? value.getId().toString() : "");
	}

}
