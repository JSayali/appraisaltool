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
import cz.strmik.cmmitool.entity.ProcessArea;
import cz.strmik.cmmitool.entity.ProcessGroup;
import cz.strmik.cmmitool.enums.MaturityLevel;
import cz.strmik.cmmitool.service.ModelService;
import cz.strmik.cmmitool.util.tree.TreeGenerator;
import cz.strmik.cmmitool.util.validator.ModelValidator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

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
    private static final String MODEL_GROUPS = "/admin/models/groups";
    private static final String MODEL_DEFINE = "/admin/models/define";
    private static final String MODEL_TREE = "/admin/models/tree";

    @Autowired
    private GenericDao<Model, String> modelDao;

    @Autowired
    private ModelService modelService;

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

    // manage models - models list

    @RequestMapping("/")
    public String manageModels(ModelMap model) {
        model.addAttribute(Attribute.MODELS, modelDao.findAll());
        return MODEL_LIST;
    }

    @RequestMapping("/delete-{modelId}.do")
    public String deleteModel(@PathVariable("modelId") String modelId, ModelMap model) {
        modelDao.delete(modelId);
        model.addAttribute(Attribute.MODELS, modelDao.findAll());
        return MODEL_LIST;
    }

    // step 1. - edit/create model

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

    @RequestMapping(method = RequestMethod.POST, value="/save-model.do")
    public String saveProject(@ModelAttribute(Attribute.MODEL) Model model, BindingResult result, ModelMap modelMap, SessionStatus status) {
        new ModelValidator(modelDao).validate(model, result);
        if(result.hasErrors()) {
            return MODEL_FORM;
        }
        if(model.isNew()) {
            modelDao.create(model);
        } else {
            modelDao.update(model);
        }
        modelMap.addAttribute(Attribute.GROUP, new ProcessGroup());
        return MODEL_GROUPS;
    }

    // step 2. - process group management

    @RequestMapping(method = RequestMethod.POST, value="/add-group-{modelId}.do")
    public String addGroup(@PathVariable("modelId") String modelId, @ModelAttribute(Attribute.GROUP) ProcessGroup group,
            BindingResult result, ModelMap modelMap, SessionStatus status) {
        if(StringUtils.isEmpty(group.getName())) {
            result.rejectValue("name", "field-required");
            return MODEL_GROUPS;
        }
        group.setModel(modelDao.read(modelId));
        modelService.addGroup(group);
        modelMap.addAttribute(Attribute.GROUP, new ProcessGroup());
        modelMap.addAttribute("saved", Boolean.TRUE);
        return MODEL_GROUPS;
    }

    @RequestMapping(method = RequestMethod.GET, value="/remove-group-{groupId}.do")
    public String removegroup(@ModelAttribute(Attribute.MODEL) Model model, BindingResult result, @PathVariable("groupId") Long groupId, ModelMap modelMap) {
        modelService.removeGroup(groupId);
        modelMap.addAttribute("saved", Boolean.TRUE);
        modelMap.addAttribute(Attribute.GROUP, new ProcessGroup());
        return MODEL_GROUPS;
    }

    // step 3. - define model process areas and so on

    @RequestMapping(method = RequestMethod.GET, value="/define-model.do")
    public String defineModel(@ModelAttribute(Attribute.MODEL) Model model, BindingResult result, ModelMap modelMap) {
        modelMap.addAttribute("modelTree", TreeGenerator.modelToTree(model));
        return MODEL_DEFINE;
    }

    @RequestMapping(method = RequestMethod.GET, value="/add-{element}.do")
    public String addModelElement(@PathVariable("element") String element, @ModelAttribute(Attribute.MODEL) Model model,
            @RequestParam("acronym") String acronym, @RequestParam("elementName") String name, ModelMap modelMap) {
        if(!StringUtils.isEmpty(acronym) && !StringUtils.isEmpty(name)) {
            if("process".equals(element)) {
                ProcessArea process = new ProcessArea();
                process.setModel(model);
                process.setId(acronym);
                process.setName(name);
                modelService.addProcess(process);
            }
        }
        modelMap.addAttribute("modelTree", TreeGenerator.modelToTree(model));
        return MODEL_DEFINE;
    }

    
}
