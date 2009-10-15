/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.web.controller;

import cz.strmik.cmmitool.dao.GenericDao;
import cz.strmik.cmmitool.dao.UserDao;
import cz.strmik.cmmitool.entity.method.Method;
import cz.strmik.cmmitool.entity.model.Model;
import cz.strmik.cmmitool.entity.model.ProcessGroup;
import cz.strmik.cmmitool.entity.User;
import cz.strmik.cmmitool.entity.method.RatingScale;
import cz.strmik.cmmitool.web.controller.propertyeditor.MethodEditor;
import cz.strmik.cmmitool.web.controller.propertyeditor.ModelEditor;
import cz.strmik.cmmitool.web.controller.propertyeditor.ProcessGroupEditor;
import cz.strmik.cmmitool.web.controller.propertyeditor.RatingScaleEditor;
import cz.strmik.cmmitool.web.controller.propertyeditor.UserEditor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

/**
 *
 * @author Lukas Strmiska
 * @version 1.0
 */
public class BindingInitializer implements WebBindingInitializer {

	@Autowired
        private GenericDao<Model, Long> modelDao;
	@Autowired
        private GenericDao<Method, Long> methodDao;
	@Autowired
        private GenericDao<ProcessGroup, Long> processGroupDao;
	@Autowired
        private GenericDao<RatingScale, Long> ratingScaleDao;
	@Autowired
        private UserDao userDao;

        @Override
	public void initBinder(WebDataBinder binder, WebRequest request) {
		binder.registerCustomEditor(Model.class, new ModelEditor(this.modelDao));
		binder.registerCustomEditor(Method.class, new MethodEditor(this.methodDao));
		binder.registerCustomEditor(User.class, new UserEditor(this.userDao));
                binder.registerCustomEditor(ProcessGroup.class, new ProcessGroupEditor(processGroupDao));
                binder.registerCustomEditor(RatingScale.class, new RatingScaleEditor(ratingScaleDao));
	}

}
