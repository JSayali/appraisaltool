/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.web.controller;

import cz.strmik.cmmitool.dao.GenericDao;
import cz.strmik.cmmitool.entity.AcronymEntity;
import cz.strmik.cmmitool.entity.Artifact;
import cz.strmik.cmmitool.entity.Goal;
import cz.strmik.cmmitool.entity.Model;
import cz.strmik.cmmitool.entity.Practice;
import cz.strmik.cmmitool.entity.ProcessArea;
import cz.strmik.cmmitool.entity.ProcessGroup;
import cz.strmik.cmmitool.enums.MaturityLevel;
import cz.strmik.cmmitool.service.ModelService;
import cz.strmik.cmmitool.util.tree.TreeGenerator;
import cz.strmik.cmmitool.util.validator.ModelValidator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpSession;
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
@SessionAttributes({Attribute.MODEL, Attribute.NODE, Attribute.MODEL_TREE})
public class ModelController {

    private static final String MODEL_LIST = "/admin/models/list";
    private static final String MODEL_FORM = "/admin/models/form";
    private static final String MODEL_GROUPS = "/admin/models/groups";
    private static final String MODEL_DEFINE = "/admin/models/define";

    private static final String EDIT_MODEL = "editmodel";

    @Autowired
    private GenericDao<Model, String> modelDao;
    @Autowired
    private GenericDao<Goal, String> goalDao;
    @Autowired
    private GenericDao<Practice, String> practiceDao;
    @Autowired
    private GenericDao<Artifact, String> artifactDao;
    @Autowired
    private GenericDao<ProcessArea, String> processAreaDao;

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

    @ModelAttribute("ableAddGoal")
    public boolean isAbleAddGoal(HttpSession session) {
        Object node = session.getAttribute(Attribute.NODE);
        return node instanceof ProcessArea;
    }

    @ModelAttribute("ableAddPractice")
    public boolean isAbleAddPractice(HttpSession session) {
        Object node = session.getAttribute(Attribute.NODE);
        return node instanceof Goal;
    }

    @ModelAttribute("ableAddArtifact")
    public boolean isAbleAddArtifact(HttpSession session) {
        Object node = session.getAttribute(Attribute.NODE);
        return node instanceof Practice;
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
        modelMap.addAttribute(Attribute.MODEL_TREE, TreeGenerator.modelToTree(model, EDIT_MODEL));
        return MODEL_DEFINE;
    }

    @RequestMapping(method = RequestMethod.GET, value="/add-{element}.do")
    public String addModelElement(@PathVariable("element") String element, 
            @ModelAttribute(Attribute.MODEL) Model model, @ModelAttribute(Attribute.NODE) AcronymEntity node,
            @RequestParam("acronym") String acronym, @RequestParam("elementName") String name, ModelMap modelMap) {
        if(!StringUtils.isEmpty(acronym) && !StringUtils.isEmpty(name)) {
            if(ProcessArea.class.getSimpleName().equalsIgnoreCase(element)) {
                ProcessArea process = new ProcessArea();
                setNameId(process, acronym, name);
                process.setModel(model);
                model = modelService.addProcess(process);
            }
            if(Goal.class.getSimpleName().equalsIgnoreCase(element)) {
                Goal goal = new Goal();
                setNameId(goal, acronym, name);
                goal.setProcessArea((ProcessArea) node);
                model = modelService.addGoal(goal);
            }
            if(Practice.class.getSimpleName().equalsIgnoreCase(element)) {
                Practice practice = new Practice();
                setNameId(practice, acronym, name);
                practice.setGoal((Goal) node);
                model = modelService.addPractice(practice);
            }
            if(Artifact.class.getSimpleName().equalsIgnoreCase(element)) {
                Artifact artifact = new Artifact();
                setNameId(artifact, acronym, name);
                artifact.setPractice((Practice) node);
                model = modelService.addArtifact(artifact);
            }
            modelMap.addAttribute(Attribute.MODEL_TREE, TreeGenerator.modelToTree(model, EDIT_MODEL));
        }
        return MODEL_DEFINE;
    }

    private void setNameId(AcronymEntity entity, String id, String name) {
        entity.setId(id);
        entity.setName(name);
    }

    @RequestMapping(method = RequestMethod.GET, value="/"+EDIT_MODEL+"-{element}-{id}.do")
    public String editModelElement(@PathVariable("element") String element, @PathVariable("id") String id,
            @ModelAttribute(Attribute.MODEL) Model model, ModelMap modelMap, HttpSession session) {
        if(!StringUtils.isEmpty(id)) {
            removeAbles(modelMap);
            if(ProcessArea.class.getSimpleName().equalsIgnoreCase(element)) {
                modelMap.addAttribute(Attribute.NODE, processAreaDao.read(id));
                modelMap.addAttribute("ableAddGoal", Boolean.TRUE);
            }
            if(Goal.class.getSimpleName().equalsIgnoreCase(element)) {
                modelMap.addAttribute(Attribute.NODE, goalDao.read(id));
                modelMap.addAttribute("ableAddPractice", Boolean.TRUE);
            }
            if(Practice.class.getSimpleName().equalsIgnoreCase(element)) {
                modelMap.addAttribute(Attribute.NODE, practiceDao.read(id));
                modelMap.addAttribute("ableAddArtifact", Boolean.TRUE);
            }
            if(Artifact.class.getSimpleName().equalsIgnoreCase(element)) {
                modelMap.addAttribute(Attribute.NODE, artifactDao.read(id));
            }
        }
        return MODEL_DEFINE;
    }

    private void removeAbles(ModelMap modelMap) {
        modelMap.remove("ableAddGoal");
        modelMap.remove("ableAddPractice");
        modelMap.remove("ableAddArtifact");
    }

    
}
