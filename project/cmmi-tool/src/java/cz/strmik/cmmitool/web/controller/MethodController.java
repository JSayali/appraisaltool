/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.web.controller;

import cz.strmik.cmmitool.cmmi.DefaultRatingScalesProvider;
import cz.strmik.cmmitool.dao.GenericDao;
import cz.strmik.cmmitool.entity.Method;
import cz.strmik.cmmitool.entity.RatingScale;
import cz.strmik.cmmitool.util.tree.TreeGenerator;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
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
@RequestMapping("/admin/methods")
@SessionAttributes({Attribute.METHOD, Attribute.NODE, Attribute.MODEL_TREE})
public class MethodController {

    private static final String METHOD_LIST = "/admin/methods/list";
    private static final String METHOD_FORM = "/admin/methods/form";
    private static final String METHOD_SCALES = "/admin/methods/scales";

    private static final String CHOOSE_RATING = "chooserating";
    private static final String EDIT_SCALE = "editscale";
    private static final String REMOVE_SCALE = "removescale";

    @Autowired
    private GenericDao<Method, Long> methodDao;
    @Autowired
    private GenericDao<RatingScale, Long> ratingScaleDao;

    @RequestMapping("/")
    public String manageMethods(ModelMap model) {
        model.addAttribute(Attribute.METHODS, methodDao.findAll());
        return METHOD_LIST;
    }

    @RequestMapping("/delete-{methodId}.do")
    public String deleteMethods(@PathVariable("methodId") Long methodId, ModelMap model) {
        methodDao.delete(methodId);
        model.addAttribute(Attribute.METHODS, methodDao.findAll());
        return METHOD_LIST;
    }

    // step 1. - edit/create method

    @RequestMapping(method = RequestMethod.GET, value = "/add.do")
    public String setupFormAdd(ModelMap modelMap) {
        Method method = new Method();
        method.setNew(true);
        modelMap.addAttribute(Attribute.METHOD, method);
        return METHOD_FORM;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/edit-{methodId}.do")
    public String setupFormEdit(@PathVariable("methodId") Long methodId,
            ModelMap modelMap) {
        Method method = methodDao.read(methodId);
        method.setNew(false);
        //TODO: load transient boolean values for checkboxes
        modelMap.addAttribute(Attribute.METHOD, method);
        return METHOD_FORM;
    }

    @RequestMapping(method = RequestMethod.POST, value="/save-method.do")
    public String saveModel(@ModelAttribute(Attribute.METHOD) Method method, BindingResult result, ModelMap modelMap) {
        if(StringUtils.isEmpty(method.getName())) {
            result.rejectValue("name", "field-required");
            return METHOD_FORM;
        }
        if(method.isNew()) {
            new DefaultRatingScalesProvider().addDefaultScales(method);
            methodDao.create(method);
        } else {
            methodDao.update(method);
        }
        modelMap.addAttribute(Attribute.MODEL_TREE, TreeGenerator.methodToTree(method, EDIT_SCALE, CHOOSE_RATING, REMOVE_SCALE));
        return METHOD_SCALES;
    }

    // step 2. - define ratings

}
