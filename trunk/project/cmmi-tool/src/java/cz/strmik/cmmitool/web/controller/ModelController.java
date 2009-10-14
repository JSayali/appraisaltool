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
import cz.strmik.cmmitool.entity.model.Artifact;
import cz.strmik.cmmitool.entity.model.Goal;
import cz.strmik.cmmitool.entity.model.Model;
import cz.strmik.cmmitool.entity.model.Practice;
import cz.strmik.cmmitool.entity.model.ProcessArea;
import cz.strmik.cmmitool.entity.model.ProcessGroup;
import cz.strmik.cmmitool.enums.MaturityLevel;
import cz.strmik.cmmitool.service.ModelService;
import cz.strmik.cmmitool.util.tree.TreeGenerator;
import cz.strmik.cmmitool.util.validator.AcronymValidator;
import cz.strmik.cmmitool.util.validator.ModelValidator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
@SessionAttributes({Attribute.MODEL, Attribute.NODE, Attribute.MODEL_TREE, Attribute.GENERIC})
public class ModelController {

    private static final String MODEL_LIST = "/admin/models/list";
    private static final String MODEL_FORM = "/admin/models/form";
    private static final String MODEL_GROUPS = "/admin/models/groups";
    private static final String MODEL_DEFINE = "/admin/models/define";

    private static final String EDIT_MODEL = "editmodel";
    private static final String REMOVE_MODEL = "removemodel";

    @Autowired
    private GenericDao<Model, Long> modelDao;
    @Autowired
    private GenericDao<Goal, Long> goalDao;
    @Autowired
    private GenericDao<Practice, Long> practiceDao;
    @Autowired
    private GenericDao<Artifact, Long> artifactDao;
    @Autowired
    private GenericDao<ProcessArea, Long> processAreaDao;

    @Autowired
    private ModelService modelService;

    // model atributes

    @ModelAttribute("levels1")
    public Collection<MaturityLevel> getLevels1() {
        List<MaturityLevel> levels = new ArrayList<MaturityLevel>(MaturityLevel.values().length);
        for(MaturityLevel level : MaturityLevel.values()) {
            if(level.getLevel()>=1) {
                levels.add(level);
            }
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
    public String deleteModel(@PathVariable("modelId") Long modelId, ModelMap model) {
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
    public String setupFormEdit(@PathVariable("modelId") Long modelId,
            ModelMap modelMap) {
        Model model = modelDao.read(modelId);
        model.getProcessAreas().size();
        model.getProcessGroups().size();
        model.setNew(false);
        modelMap.addAttribute(Attribute.MODEL, model);
        return MODEL_FORM;
    }

    @RequestMapping(method = RequestMethod.POST, value="/save-model.do")
    public String saveModel(@ModelAttribute(Attribute.MODEL) Model model, BindingResult result, ModelMap modelMap, SessionStatus status) {
        new ModelValidator().validate(model, result);
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
    public String addGroup(@PathVariable("modelId") Long modelId, @ModelAttribute(Attribute.GROUP) ProcessGroup group,
            BindingResult result, ModelMap modelMap, SessionStatus status) {
        if(StringUtils.isEmpty(group.getName())) {
            result.rejectValue("name", "field-required");
            return MODEL_GROUPS;
        }
        group.setModel(modelDao.read(modelId));
        modelMap.addAttribute(Attribute.MODEL, modelService.addGroup(group));
        modelMap.addAttribute(Attribute.GROUP, new ProcessGroup());
        modelMap.addAttribute("saved", Boolean.TRUE);
        return MODEL_GROUPS;
    }

    @RequestMapping(method = RequestMethod.GET, value="/remove-group-{groupId}.do")
    public String removeGroup(@ModelAttribute(Attribute.MODEL) Model model, BindingResult result, @PathVariable("groupId") Long groupId, ModelMap modelMap) {
        modelMap.addAttribute(Attribute.MODEL, modelService.removeGroup(groupId));
        modelMap.addAttribute("saved", Boolean.TRUE);
        modelMap.addAttribute(Attribute.GROUP, new ProcessGroup());
        return MODEL_GROUPS;
    }

    // step 3. - define model process areas and so on

    @RequestMapping(method = RequestMethod.GET, value="/define-model.do")
    public String defineModel(@ModelAttribute(Attribute.MODEL) Model model, BindingResult result, ModelMap modelMap) {
        model = modelDao.read(model.getId());
        boolean generic = false;
        computeAbles(modelMap, generic);
        modelMap.addAttribute(Attribute.GENERIC, generic);
        modelMap.addAttribute(Attribute.MODEL_TREE, TreeGenerator.modelToTree(model, generic, EDIT_MODEL, REMOVE_MODEL));
        return MODEL_DEFINE;
    }

    @RequestMapping(method = RequestMethod.GET, value="/add-{element}.do")
    public String addModelElement(@PathVariable("element") String element, 
            @ModelAttribute(Attribute.MODEL) Model model, @ModelAttribute(Attribute.GENERIC) Boolean generic, HttpSession session,
            @RequestParam("acronym") String acronym, @RequestParam("elementName") String name, ModelMap modelMap) {
        if(!StringUtils.isEmpty(acronym) && !StringUtils.isEmpty(name)) {
            model = modelDao.read(model.getId());
            modelMap.remove(Attribute.MODEL);
            modelMap.remove(Attribute.MODEL_TREE);
            AcronymEntity node = (AcronymEntity) session.getAttribute(Attribute.NODE);
            if(ProcessArea.class.getSimpleName().equalsIgnoreCase(element)) {
                ProcessArea process = new ProcessArea();
                setNameId(process, acronym, name);
                process.setModel(model);
                model = modelService.addProcess(process);
            }
            if(Goal.class.getSimpleName().equalsIgnoreCase(element)) {
                Goal goal = new Goal();
                setNameId(goal, acronym, name);
                if(generic) {
                    goal.setModel(model);
                }else {
                    goal.setProcessArea(processAreaDao.read(node.getId()));
                }
                model = modelService.addGoal(goal);
            }
            if(Practice.class.getSimpleName().equalsIgnoreCase(element)) {
                Practice practice = new Practice();
                setNameId(practice, acronym, name);
                practice.setGoal(goalDao.read(node.getId()));
                model = modelService.addPractice(practice);
            }
            if(Artifact.class.getSimpleName().equalsIgnoreCase(element)) {
                Artifact artifact = new Artifact();
                setNameId(artifact, acronym, name);
                artifact.setPractice(practiceDao.read(node.getId()));
                model = modelService.addArtifact(artifact);
            }
            computeAbles(modelMap, generic, node);
            modelMap.addAttribute(Attribute.MODEL, model);
            modelMap.addAttribute(Attribute.MODEL_TREE, TreeGenerator.modelToTree(model, generic, EDIT_MODEL, REMOVE_MODEL));
        }
        return MODEL_DEFINE;
    }

    private void setNameId(AcronymEntity entity, String acronym, String name) {
        entity.setAcronym(acronym);
        entity.setName(name);
    }

    @RequestMapping(method = RequestMethod.GET, value="/"+EDIT_MODEL+"-{element}-{id}.do")
    public String editModelElement(@PathVariable("element") String element, @PathVariable("id") Long id,
            @ModelAttribute(Attribute.MODEL) Model model,@ModelAttribute(Attribute.GENERIC) Boolean generic,
            ModelMap modelMap) {
        if(id!=null) {
            model = modelDao.read(model.getId());
            modelMap.remove(Attribute.MODEL);
            Object node = null;
            if(ProcessArea.class.getSimpleName().equalsIgnoreCase(element)) {
                node = processAreaDao.read(id);
            }
            if(Goal.class.getSimpleName().equalsIgnoreCase(element)) {
                node = goalDao.read(id);
            }
            if(Practice.class.getSimpleName().equalsIgnoreCase(element)) {
                node = practiceDao.read(id);
            }
            if(Artifact.class.getSimpleName().equalsIgnoreCase(element)) {
                node = artifactDao.read(id);
                modelMap.addAttribute("artifactEdit", Boolean.TRUE);
            }
            computeAbles(modelMap, generic, node);
            modelMap.addAttribute(Attribute.NODE, node);
            modelMap.addAttribute(Attribute.MODEL, model);
            modelMap.addAttribute("displayForm", Boolean.TRUE);
        }
        return MODEL_DEFINE;
    }

    @RequestMapping(method = RequestMethod.POST, value="/save-ProcessArea-{id}.do")
    public String saveElementProcessArea(@PathVariable("id") Long id, @ModelAttribute(Attribute.NODE) ProcessArea processArea,
            BindingResult result, ModelMap modelMap, @ModelAttribute(Attribute.GENERIC) Boolean generic) {
        return saveElement(processArea, result, modelMap, generic);
    }

    @RequestMapping(method = RequestMethod.POST, value="/save-Goal-{id}.do")
    public String saveElementGoal(@PathVariable("id") Long id, @ModelAttribute(Attribute.NODE) Goal goal,
            BindingResult result, ModelMap modelMap, @ModelAttribute(Attribute.GENERIC) Boolean generic) {
        return saveElement(goal, result, modelMap, generic);
    }

    @RequestMapping(method = RequestMethod.POST, value="/save-Practice-{id}.do")
    public String saveElementPractice(@PathVariable("id") Long id, @ModelAttribute(Attribute.NODE) Practice practice,
            BindingResult result, ModelMap modelMap, @ModelAttribute(Attribute.GENERIC) Boolean generic) {
        return saveElement(practice, result, modelMap, generic);
    }

    @RequestMapping(method = RequestMethod.POST, value="/save-Artifact-{id}.do")
    public String saveElementArtifact(@PathVariable("id") Long id, @ModelAttribute(Attribute.NODE) Artifact artifact,
            BindingResult result, ModelMap modelMap, @ModelAttribute(Attribute.GENERIC) Boolean generic) {
        return saveElement(artifact, result, modelMap, generic);
    }

    @RequestMapping(method = RequestMethod.GET, value="/"+REMOVE_MODEL+"-{element}-{id}.do")
    public String removeModelElement(@PathVariable("element") String element, @PathVariable("id") Long id,
            @ModelAttribute(Attribute.MODEL) Model model, @ModelAttribute(Attribute.GENERIC) Boolean generic,
            ModelMap modelMap) {
        if(id!=null) {
            model = modelDao.read(model.getId());
            modelMap.remove(Attribute.MODEL);
            modelMap.remove(Attribute.MODEL_TREE);
            modelMap.remove(Attribute.NODE);

            if(ProcessArea.class.getSimpleName().equalsIgnoreCase(element)) {
                model = modelService.removeProcess(id);
            }
            if(Goal.class.getSimpleName().equalsIgnoreCase(element)) {
                model = modelService.removeGoal(id);
            }
            if(Practice.class.getSimpleName().equalsIgnoreCase(element)) {
                model = modelService.removePractice(id);
            }
            if(Artifact.class.getSimpleName().equalsIgnoreCase(element)) {
                model = modelService.removeArtifact(id);
            }

            computeAbles(modelMap, generic);
            modelMap.addAttribute(Attribute.MODEL, model);
            modelMap.addAttribute(Attribute.MODEL_TREE, TreeGenerator.modelToTree(model, generic, EDIT_MODEL, REMOVE_MODEL));
        }
        return MODEL_DEFINE;
    }

    @RequestMapping(method = RequestMethod.GET, value ="/finish.do")
    public String finish(SessionStatus status) {
        status.setComplete();
        return "redirect:/admin/models/";
    }

    @RequestMapping(method = RequestMethod.GET, value ="/switch-dimension.do")
    public String switchDimension(@ModelAttribute(Attribute.GENERIC) Boolean generic,
            @ModelAttribute(Attribute.MODEL) Model model,ModelMap modelMap) {
        generic = !generic;
        modelMap.addAttribute(Attribute.GENERIC, generic);
        model = modelDao.read(model.getId());

        computeAbles(modelMap, generic);
        modelMap.addAttribute(Attribute.MODEL, model);
        modelMap.addAttribute(Attribute.MODEL_TREE, TreeGenerator.modelToTree(model, generic, EDIT_MODEL, REMOVE_MODEL));
        return MODEL_DEFINE;
    }

    private String saveElement(AcronymEntity element, BindingResult result,
            ModelMap modelMap, boolean generic) {
        modelMap.addAttribute("displayForm", Boolean.TRUE);
        new AcronymValidator().validate(element, result);
        if(result.hasErrors()) {
            return MODEL_DEFINE;
        }
        Model model = null;
        if(element instanceof ProcessArea) {
            model = modelService.saveProcess((ProcessArea) element);
        }
        if(element instanceof Goal) {
            model = modelService.saveGoal((Goal) element);
        }
        if(element instanceof Practice) {
            model = modelService.savePractice((Practice) element);
        }
        if(element instanceof Artifact) {
            model = modelService.saveArtifact((Artifact) element);
            modelMap.addAttribute("artifactEdit", Boolean.TRUE);
        }
        if(model!=null) {
            model = modelDao.read(model.getId());
            computeAbles(modelMap, generic, element);
            modelMap.addAttribute(Attribute.MODEL, model);
            modelMap.addAttribute(Attribute.MODEL_TREE, TreeGenerator.modelToTree(model, generic, EDIT_MODEL, REMOVE_MODEL));
            modelMap.addAttribute("saved", Boolean.TRUE);
        }
        return MODEL_DEFINE;
    }

    private void computeAbles(ModelMap modelMap, Boolean generic) {
        computeAbles(modelMap, generic, null);
    }

    private void computeAbles(ModelMap modelMap, Boolean generic, Object node) {
        modelMap.remove("ableAddProcessArea");
        modelMap.remove("ableAddGoal");
        modelMap.remove("ableAddPractice");
        modelMap.remove("ableAddArtifact");

        if(generic!=null && !generic) {
            modelMap.addAttribute("ableAddProcessArea", true);
        }
        if((node instanceof ProcessArea)||(generic!=null && generic)) {
            modelMap.addAttribute("ableAddGoal", true);            
        }
        if(node instanceof Goal) {
            modelMap.addAttribute("ableAddPractice", true);
        }
        if(node instanceof Practice) {
            modelMap.addAttribute("ableAddArtifact", true);
        }
    }

    private static final Log log = LogFactory.getLog(ModelController.class);

}
