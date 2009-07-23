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
import cz.strmik.cmmitool.enums.MaturityLevel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
@Controller
@RequestMapping("/admin/models")
@SessionAttributes(Attribute.MODEL)
public class ModelController {

    private static final String MODEL_LIST = "/admin/models/list";
    private static final String MODEL_FORM = "/admin/models/form";

    @Autowired
    private GenericDao<Model, String> modelDao;

    // model atributes

    @ModelAttribute("levels")
    public Collection<MaturityLevel> getLevels() {
        List<MaturityLevel> levels = new ArrayList<MaturityLevel>(MaturityLevel.values().length);
        for(MaturityLevel level : MaturityLevel.values()) {
            levels.add(level);
        }
        return levels;
    }


    // request mappings

    @RequestMapping("/")
    public String manageModels(ModelMap model) {
        model.addAttribute(Attribute.MODELS, modelDao.findAll());
        return MODEL_LIST;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/add.do")
    public String setupFormAdd(ModelMap modelMap) {
        Model model = new Model();
        model.setNew(true);
        modelMap.addAttribute(Attribute.MODEL, model);
        return MODEL_FORM;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/edit-{modelId}.do")
    public String setupFormEdit(@PathVariable("modelId") String modelId,
            ModelMap modelMap) {
        Model model = modelDao.read(modelId);
        model.setNew(false);
        modelMap.addAttribute(Attribute.MODEL, model);
        return MODEL_FORM;
    }

    @RequestMapping("/delete-{modelId}.do")
    public String deleteUser(@PathVariable("modelId") String modelId, ModelMap model) {
        modelDao.delete(modelId);
        model.addAttribute(Attribute.MODELS, modelDao.findAll());
        return MODEL_LIST;
    }

}
