/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.service;

import cz.strmik.cmmitool.dao.GenericDao;
import cz.strmik.cmmitool.entity.ScaleRule;
import cz.strmik.cmmitool.entity.Method;
import cz.strmik.cmmitool.entity.RuleAggregation;
import cz.strmik.cmmitool.entity.RatingScale;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public class MethodServiceImpl implements MethodService {

    @Autowired
    private GenericDao<RatingScale, Long> ratingScaleDao;
    @Autowired
    private GenericDao<Method, Long> methodDao;
    @Autowired
    private GenericDao<RuleAggregation, Long> ruleAggregationDao;
    @Autowired
    private GenericDao<ScaleRule, Long> scaleRuleDao;

    @Override
    public Method removeScale(Method method, RatingScale scale) {
        if (method.getGoalSatisfaction() != null) {
            method.getGoalSatisfaction().remove(scale);
        }
        if (method.getOrgMaturityLevel() != null) {
            method.getOrgMaturityLevel().remove(scale);
        }
        if (method.getPracticeImplementation() != null) {
            method.getPracticeImplementation().remove(scale);
        }
        if (method.getProcessAreaCapLevel() != null) {
            method.getProcessAreaCapLevel().remove(scale);
        }
        if (method.getProcessAreaSatisfaction() != null) {
            method.getProcessAreaSatisfaction().remove(scale);

        }
        removeScaleRulesFromAggregationRules(method, scale);
        ratingScaleDao.delete(scale.getId());
        return methodDao.update(method);
    }

    @Override
    public Method removeUnusedRatingScales(Method method) {
        if (!method.isRateGoalSatisfaction()) {
            if (method.getGoalSatisfaction() != null && !method.getGoalSatisfaction().isEmpty()) {
                deleteScales(method, method.getGoalSatisfaction());
                for(RuleAggregation ar : method.getGoalRuleAggregation()) {
                    ruleAggregationDao.delete(ar.getId());
                }
                method.setGoalRuleAggregation(null);
                method.setGoalSatisfaction(null);
            }
        }
        if (!method.isRateOrgMaturityLevel()) {
            if (method.getOrgMaturityLevel() != null && !method.getOrgMaturityLevel().isEmpty()) {
                deleteScales(method, method.getOrgMaturityLevel());
                method.setOrgMaturityLevel(null);
            }
        }
        if (!method.isCharPracticeImplementation()) {
            if (method.getPracticeImplementation() != null && !method.getPracticeImplementation().isEmpty()) {
                deleteScales(method, method.getPracticeImplementation());
                for(RuleAggregation ar : method.getPracticeRuleAggregation()) {
                    ruleAggregationDao.delete(ar.getId());
                }
                method.setPracticeRuleAggregation(null);
                method.setPracticeImplementation(null);
            }
        }
        if (!method.isRateProcessAreaCapLevel()) {
            if (method.getProcessAreaCapLevel() != null && !method.getProcessAreaCapLevel().isEmpty()) {
                deleteScales(method, method.getProcessAreaCapLevel());
                method.setProcessAreaCapLevel(null);
            }
        }
        if (!method.isRateProcessAreaSatisfaction()) {
            if (method.getProcessAreaSatisfaction() != null && !method.getProcessAreaSatisfaction().isEmpty()) {
                deleteScales(method, method.getProcessAreaSatisfaction());
                method.setProcessAreaSatisfaction(null);
            }
        }
        return methodDao.update(method);
    }

    @Override
    public Method addPracticeRuleAggregation(Method method, RuleAggregation pra) {
        if (method.getPracticeRuleAggregation() == null) {
            method.setPracticeRuleAggregation(new HashSet<RuleAggregation>());
        }
        pra.setRuleNo(method.getPracticeRuleAggregation().size() + 1);
        pra.setMethodPractice(method);
        setOwningSide(pra);
        pra = ruleAggregationDao.create(pra);
        method.getPracticeRuleAggregation().add(pra);
        return methodDao.update(method);
    }

    @Override
    public Method addGoalRuleAggregation(Method method, RuleAggregation pra) {
        if (method.getGoalRuleAggregation() == null) {
            method.setGoalRuleAggregation(new HashSet<RuleAggregation>());
        }
        pra.setRuleNo(method.getGoalRuleAggregation().size() + 1);
        pra.setMethodGoal(method);
        setOwningSide(pra);
        pra = ruleAggregationDao.create(pra);
        method.getGoalRuleAggregation().add(pra);
        return methodDao.update(method);
    }

    @Override
    public Method updatePracticeRuleAggregation(Method method, RuleAggregation ruleAggergation) {
        for(RuleAggregation ra : method.getPracticeRuleAggregation()) {
            if(ra.getId().equals(ruleAggergation.getId())) {
                removeScaleRules(ra.getSources().iterator(), method.getPracticeImplementation());
                removeScaleRules(ra.getTargets().iterator(), method.getPracticeImplementation());
                ra.setSources(ruleAggergation.getSources());
                ra.setTargets(ruleAggergation.getTargets());
                setOwningSide(ra);
            }
        }
        return methodDao.update(method);
    }

    @Override
    public Method updateGoalRuleAggregation(Method method, RuleAggregation ruleAggergation) {
        for(RuleAggregation ra : method.getGoalRuleAggregation()) {
            if(ra.getId().equals(ruleAggergation.getId())) {
                removeScaleRules(ra.getSources().iterator(), method.getPracticeImplementation()); // practicticeimplementatation
                removeScaleRules(ra.getTargets().iterator(), method.getGoalSatisfaction());
                ra.setSources(ruleAggergation.getSources());
                ra.setTargets(ruleAggergation.getTargets());
                setOwningSide(ra);
            }
        }
        return methodDao.update(method);
    }

    private void setOwningSide(RuleAggregation ra) {
                for(ScaleRule rule : ra.getSources()) {
                    rule.setRuleAggregationSource(ra);
                }
                for(ScaleRule rule : ra.getTargets()) {
                    rule.setRuleAggregationTarget(ra);
                }
    }

    @Override
    public Method removeRuleAggregation(Method method, Long id) {
        Iterator<RuleAggregation> it = method.getPracticeRuleAggregation().iterator();
        if (!removeRulesAggregation(it, id)) {
            it = method.getGoalRuleAggregation().iterator();
            removeRulesAggregation(it, id);
        }
        return methodDao.update(method);
    }

    private boolean removeRulesAggregation(Iterator<RuleAggregation> it, Long id) {
        while (it.hasNext()) {
            RuleAggregation pra = it.next();
            if (pra.getId().equals(id)) {
                for(ScaleRule rule : pra.getSources()) {
                    //scaleRuleDao.delete(rule.getId());
                    rule.getScale().getScaleRules().remove(rule);
                }
                for(ScaleRule rule : pra.getTargets()) {
                    //scaleRuleDao.delete(rule.getId());
                    rule.getScale().getScaleRules().remove(rule);
                }
                ruleAggregationDao.delete(id);
                it.remove();
                return true;
            }
        }
        return false;
    }

    private void removeScaleRules(Iterator<ScaleRule> it, Set<RatingScale> rss) {
        while (it.hasNext()) {
            ScaleRule ar = it.next();
            if(rss!=null) {
                for(RatingScale rs : rss) {
                    rs.getScaleRules().remove(ar);
                }
            }
            scaleRuleDao.delete(ar.getId());
            it.remove();
        }
    }

    private RatingScale removeScaleRulesFromAggregationRules(Method method, RatingScale scale) {
        Set<ScaleRule> scaleRules = scale.getScaleRules();
        for(ScaleRule scaleRule: scaleRules) {
            if(method.getGoalRuleAggregation()!=null) {
                for(RuleAggregation ra : method.getGoalRuleAggregation()) {
                    ra.getSources().remove(scaleRule);
                    ra.getTargets().remove(scaleRule);
                }
            }
            if(method.getPracticeImplementation()!=null) {
                for(RuleAggregation ra : method.getPracticeRuleAggregation()) {
                    ra.getSources().remove(scaleRule);
                    ra.getTargets().remove(scaleRule);
                }
            }
        }
        return scale;
    }

    private void deleteScales(Method method, Set<RatingScale> scales) {
        for (RatingScale scale : scales) {
            removeScaleRulesFromAggregationRules(method, scale);
            ratingScaleDao.delete(scale.getId());
        }
    }
}
