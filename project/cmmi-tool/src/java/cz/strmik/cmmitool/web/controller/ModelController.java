/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.web.controller;

import cz.strmik.cmmitool.dao.GenericDao;
import cz.strmik.cmmitool.entity.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
@Controller
@RequestMapping("/admin/models")
@SessionAttributes(Attribute.MODELS)
public class ModelController {

    private static final String MODEL_LIST = "/admin/models/list";

    @Autowired
    private GenericDao<Model, String> modelDao;

    @RequestMapping("/")
    public String manageModels(ModelMap model) {
        model.addAttribute(Attribute.MODELS, modelDao.findAll());
        return MODEL_LIST;
    }

    @RequestMapping("/delete-{modelId}.do")
    public String deleteUser(@PathVariable("modelId") String modelId, ModelMap model) {
        modelDao.delete(modelId);
        model.addAttribute(Attribute.MODELS, modelDao.findAll());
        return MODEL_LIST;
    }

}
