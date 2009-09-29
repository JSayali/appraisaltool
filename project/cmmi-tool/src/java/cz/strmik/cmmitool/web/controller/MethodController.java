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
import cz.strmik.cmmitool.entity.ScaleRule;
import cz.strmik.cmmitool.entity.Method;
import cz.strmik.cmmitool.entity.RuleAggregation;
import cz.strmik.cmmitool.entity.RatingScale;
import cz.strmik.cmmitool.enums.RuleCompletion;
import cz.strmik.cmmitool.service.MethodService;
import cz.strmik.cmmitool.util.Constants;
import cz.strmik.cmmitool.util.tree.TreeGenerator;
import cz.strmik.cmmitool.util.validator.RatingScaleValidator;
import cz.strmik.cmmitool.web.lang.LangProvider;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
@Controller
@RequestMapping("/admin/methods")
@SessionAttributes({Attribute.METHOD, Attribute.NODE, Attribute.MODEL_TREE, Attribute.RATING})
public class MethodController {

    // JSPs
    private static final String METHOD_LIST = "/admin/methods/list";
    private static final String METHOD_FORM = "/admin/methods/form";
    private static final String METHOD_SCALES = "/admin/methods/scales";
    private static final String METHOD_AGGREGATION = "/admin/methods/aggregation";

    // tree commands
    private static final String CHOOSE_RATING = "chooserating";
    private static final String EDIT_SCALE = "editscale";
    private static final String REMOVE_SCALE = "removescale";

    @Autowired
    private GenericDao<Method, Long> methodDao;
    @Autowired
    private GenericDao<RatingScale, Long> ratingScaleDao;
    @Autowired
    private GenericDao<RuleAggregation, Long> ruleAggregationDao;
    @Autowired
    private MethodService methodService;

    @ModelAttribute("ableAddScale")
    public boolean isAbleAddScale(HttpSession session) {
        return session.getAttribute(Attribute.RATING) != null;
    }

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
        modelMap.addAttribute(Attribute.METHOD, method);
        return METHOD_FORM;
    }

    @RequestMapping(method = RequestMethod.POST, value="/save-method.do")
    public String saveMethod(@ModelAttribute(Attribute.METHOD) Method method, BindingResult result, ModelMap modelMap) {
        if(StringUtils.isEmpty(method.getName())) {
            result.rejectValue("name", "field-required");
            return METHOD_FORM;
        }
        if(method.isNew()) {
            DefaultRatingScalesProvider scalesProvider = new DefaultRatingScalesProvider(ratingScaleDao);
            scalesProvider.addDefaultScales(method);
            methodDao.create(method);
        } else {
            method = methodService.refreshRatingScales(method);
        }
        modelMap.addAttribute(Attribute.METHOD, method);
        modelMap.addAttribute(Attribute.MODEL_TREE, TreeGenerator.methodToTree(method, EDIT_SCALE, CHOOSE_RATING, REMOVE_SCALE));
        return METHOD_SCALES;
    }

    // step 2. - define ratings

    @RequestMapping(method = RequestMethod.GET, value = "/chooserating-{rating}.do")
    public String chooseRating(@PathVariable("rating") String rating, ModelMap modelMap) {
        modelMap.addAttribute(Attribute.RATING, rating);
        modelMap.addAttribute("ableAddScale", true);
        return METHOD_SCALES;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/add-scale.do")
    public String addScale(@ModelAttribute(Attribute.METHOD) Method method, @ModelAttribute(Attribute.RATING) String rating,
            ModelMap modelMap) {
        RatingScale scale = getNewRS();
        if(rating.equals(Constants.Ratings.CHAR_PRACTICE_IMPL.toString())) {
            method.getPracticeImplementation().add(scale);
            scale.setMethodPracImpl(method);
        }else if(rating.equals(Constants.Ratings.GOAL_SATISFACTION.toString())) {
            method.getGoalSatisfaction().add(scale);
            scale.setMethodGoalSat(method);
        }else if(rating.equals(Constants.Ratings.ORG_MATURITY_LEVEL.toString())) {
            method.getOrgMaturityLevel().add(scale);
            scale.setMethodMatLevel(method);
        }else if(rating.equals(Constants.Ratings.PROCESS_AREA_CAP_LEVEL.toString())) {
            method.getProcessAreaCapLevel().add(scale);
            scale.setMethodProcessCap(method);
        }else if(rating.equals(Constants.Ratings.PROCESS_AREA_SATISFACTION.toString())) {
            method.getProcessAreaSatisfaction().add(scale);
            scale.setMethodProcessSat(method);
        }
        method = methodDao.update(method);
        modelMap.addAttribute(Attribute.METHOD, method);
        modelMap.addAttribute(Attribute.MODEL_TREE, TreeGenerator.methodToTree(method, EDIT_SCALE, CHOOSE_RATING, REMOVE_SCALE));
        return METHOD_SCALES;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/removescale-{id}.do")
    public String removeScale(@ModelAttribute(Attribute.METHOD) Method method, @PathVariable("id") Long scaleId,
            ModelMap modelMap) {
        method = methodService.removeScale(method.getId(), scaleId);
        modelMap.addAttribute(Attribute.METHOD, method);
        modelMap.addAttribute(Attribute.MODEL_TREE, TreeGenerator.methodToTree(method, EDIT_SCALE, CHOOSE_RATING, REMOVE_SCALE));
        return METHOD_SCALES;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/editscale-{id}.do")
    public String editScale(@ModelAttribute(Attribute.METHOD) Method method, @PathVariable("id") Long scaleId,
            ModelMap modelMap) {
        RatingScale rs = ratingScaleDao.read(scaleId);
        modelMap.addAttribute("scale", rs);
        modelMap.addAttribute("displayForm", true);
        return METHOD_SCALES;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/editscale-{id}.do")
    public String saveScale(@ModelAttribute("scale") RatingScale editedScale, BindingResult result, @PathVariable("id") Long scaleId,
            ModelMap modelMap, HttpSession session) {
        new RatingScaleValidator().validate(editedScale, result);
        if(result.hasErrors()) {
            modelMap.addAttribute("scale", editedScale);
            modelMap.addAttribute("displayForm", true);
            return METHOD_SCALES;
        }
        RatingScale rs = ratingScaleDao.read(scaleId);
        rs.setName(editedScale.getName());
        rs.setScore(editedScale.getScore());
        ratingScaleDao.update(rs);
        Method method = (Method) session.getAttribute(Attribute.METHOD);
        method = methodDao.read(method.getId());
        modelMap.addAttribute(Attribute.METHOD, method);
        modelMap.addAttribute(Attribute.MODEL_TREE, TreeGenerator.methodToTree(method, EDIT_SCALE, CHOOSE_RATING, REMOVE_SCALE));
        return METHOD_SCALES;
    }

    private RatingScale getNewRS() {
        RatingScale rs = new RatingScale();
        rs.setName(LangProvider.getString("unknown"));
        return rs;
    }

    // step 3. - define aggregation rules

    @RequestMapping(method = RequestMethod.GET, value="/aggregation.do")
    public String defineAggregation(@ModelAttribute(Attribute.METHOD) Method method, ModelMap modelMap) {
        method = methodDao.read(method.getId());
        modelMap.addAttribute(Attribute.METHOD, method);
        return METHOD_AGGREGATION;
    }

    // step 3.a - define practice aggregation rules

    @RequestMapping(method = RequestMethod.POST, value="/addpracticerule.do")
    public String addPracticeRule(@ModelAttribute(Attribute.METHOD) Method method, ModelMap modelMap, HttpServletRequest request) {
        RuleAggregation ruleAggregation = getRulesFromRequest(method.getPracticeImplementation(), request);
        method = methodService.addPracticeRuleAggregation(method, ruleAggregation);
        modelMap.addAttribute(Attribute.METHOD, method);
        return METHOD_AGGREGATION;
    }

    @RequestMapping(method = RequestMethod.GET, value="/deleterule-{id}.do")
    public String deleteRule(@ModelAttribute(Attribute.METHOD) Method method, ModelMap modelMap, @PathVariable("id") Long prId) {
        method = methodService.removeRuleAggregation(method.getId(), prId);
        modelMap.addAttribute(Attribute.METHOD, method);
        return METHOD_AGGREGATION;
    }

    @RequestMapping(method = RequestMethod.GET, value="/editpracticerule-{id}.do")
    public String editPracticeRule(@ModelAttribute(Attribute.METHOD) Method method, ModelMap modelMap, @PathVariable("id") Long prId) {
        for(RuleAggregation pra : method.getPracticeRuleAggregation()) {
            if(pra.getId().equals(prId)) {
                modelMap.addAttribute("practiceRule", pra);
                break;
            }
        }
        return METHOD_AGGREGATION;
    }

    @RequestMapping(method = RequestMethod.POST, value="/editpracticerule-{id}.do")
    public String savePracticeRule(@ModelAttribute(Attribute.METHOD) Method method, ModelMap modelMap, @PathVariable("id") Long prId,
            HttpServletRequest request) {
        RuleAggregation ruleAggregation = getRulesFromRequest(method.getPracticeImplementation(), request);
        ruleAggregation.setId(prId);
        method = methodService.updatePracticeRuleAggregation(method.getId(), ruleAggregation);
        modelMap.addAttribute(Attribute.METHOD, method);
        return METHOD_AGGREGATION;
    }

    // step 3.b - define goal aggregation rules

    @RequestMapping(method = RequestMethod.POST, value="/addgoalrule.do")
    public String addGoalRule(@ModelAttribute(Attribute.METHOD) Method method, ModelMap modelMap, HttpServletRequest request) {
        RuleAggregation ruleAggregation = getRulesFromRequest(method.getGoalSatisfaction(), method.getPracticeImplementation(), request);
        method = methodService.addGoalRuleAggregation(method, ruleAggregation);
        modelMap.addAttribute(Attribute.METHOD, method);
        return METHOD_AGGREGATION;
    }

    @RequestMapping(method = RequestMethod.GET, value="/editgoalrule-{id}.do")
    public String editGoalRule(@ModelAttribute(Attribute.METHOD) Method method, ModelMap modelMap, @PathVariable("id") Long prId) {
        for(RuleAggregation pra : method.getGoalRuleAggregation()) {
            if(pra.getId().equals(prId)) {
                modelMap.addAttribute("goalRule", pra);
                break;
            }
        }
        return METHOD_AGGREGATION;
    }

    @RequestMapping(method = RequestMethod.POST, value="/editgoalrule-{id}.do")
    public String saveGoalRule(@ModelAttribute(Attribute.METHOD) Method method, ModelMap modelMap, @PathVariable("id") Long prId,
            HttpServletRequest request) {
        RuleAggregation ruleAggregation = getRulesFromRequest(method.getPracticeImplementation(), method.getGoalSatisfaction(), request);
        ruleAggregation.setId(prId);
        method = methodService.updateGoalRuleAggregation(method.getId(), ruleAggregation);
        modelMap.addAttribute(Attribute.METHOD, method);
        return METHOD_AGGREGATION;
    }

    @RequestMapping(method = RequestMethod.GET, value="/finish-method.do")
    public String finishMethod(@ModelAttribute(Attribute.METHOD) Method method, SessionStatus status) {
        status.setComplete();
        return "redirect:/admin/methods/";
    }

    private RuleAggregation getRulesFromRequest(Set<RatingScale> scales1, Set<RatingScale> scales2, HttpServletRequest request) {
        Set<RatingScale> scales = new HashSet<RatingScale>();
        scales.addAll(scales1);
        scales.addAll(scales2);
        return getRulesFromRequest(scales, request);
    }
    
    private RuleAggregation getRulesFromRequest(Set<RatingScale> scales, HttpServletRequest request) {
        RuleAggregation ra = new RuleAggregation();
        ra.setSources(new HashSet<ScaleRule>());
        ra.setTargets(new HashSet<ScaleRule>());
        for(RatingScale scale : scales) {
            Enumeration en = request.getParameterNames();
            ScaleRule rule = null;
            while(en.hasMoreElements()) {
                String attrName = (String) en.nextElement();
                if(attrName.startsWith(scale.getId().toString())) {
                    rule = new ScaleRule();
                    rule.setScale(scale);
                    rule.setRuleCompletion(RuleCompletion.valueOf(request.getParameter(attrName)));

                    if(attrName.endsWith("-source")) {
                        ra.getSources().add(rule);
                    }
                    if(attrName.endsWith("-target")) {
                        ra.getTargets().add(rule);
                    }
                }
            }
        }
        return ra;
    }

    private static final Log log = LogFactory.getLog(MethodController.class);

}
