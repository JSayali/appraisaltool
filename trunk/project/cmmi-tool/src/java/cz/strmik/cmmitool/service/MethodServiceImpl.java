/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.service;

import cz.strmik.cmmitool.dao.GenericDao;
import cz.strmik.cmmitool.entity.AggregationRule;
import cz.strmik.cmmitool.entity.Method;
import cz.strmik.cmmitool.entity.PracticeRuleAggregation;
import cz.strmik.cmmitool.entity.RatingScale;
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
    private GenericDao<PracticeRuleAggregation, Long> practiceRuleAggregationDao;

    @Autowired
    private GenericDao<AggregationRule, Long> aggregationRuleDao;

    @Override
    public Method removeScale(Method method, RatingScale scale) {
        if(method.getGoalSatisfaction()!=null) {
            method.getGoalSatisfaction().remove(scale);
        }
        if(method.getOrgMaturityLevel()!=null) {
            method.getOrgMaturityLevel().remove(scale);
        }
        if(method.getPracticeImplementation()!=null) {
            method.getPracticeImplementation().remove(scale);
        }
        if(method.getProcessAreaCapLevel()!=null) {
            method.getProcessAreaCapLevel().remove(scale);
        }
        if(method.getProcessAreaSatisfaction()!=null) {
            method.getProcessAreaSatisfaction().remove(scale);
        }
        ratingScaleDao.delete(scale.getId());
        return methodDao.update(method);
    }

    @Override
    public Method removeUnusedScales(Method method) {
        if(!method.isRateGoalSatisfaction()) {
            if(method.getGoalSatisfaction()!=null) {
                deleteScales(method.getGoalSatisfaction());
                method.setGoalSatisfaction(null);
            }
        }
        if(!method.isRateOrgMaturityLevel()) {
            if(method.getOrgMaturityLevel()!=null) {
                deleteScales(method.getOrgMaturityLevel());
                method.setOrgMaturityLevel(null);
            }
        }
        if(!method.isCharPracticeImplementation()) {
            if(method.getPracticeImplementation()!=null) {
                deleteScales(method.getPracticeImplementation());
                method.setPracticeImplementation(null);
            }
        }
        if(!method.isRateProcessAreaCapLevel()) {
            if(method.getProcessAreaCapLevel()!=null) {
                deleteScales(method.getProcessAreaCapLevel());
                method.setProcessAreaCapLevel(null);
            }
        }
        if(!method.isRateProcessAreaSatisfaction()) {
            if(method.getProcessAreaSatisfaction()!=null) {
                deleteScales(method.getProcessAreaSatisfaction());
                method.setProcessAreaSatisfaction(null);
            }
        }
        return methodDao.update(method);
    }

    private void deleteScales(Set<RatingScale> scales) {
        for(RatingScale scale :  scales) {
            ratingScaleDao.delete(scale.getId());
        }
    }

    @Override
    public Method addPracticeRuleAggregation(Method method, PracticeRuleAggregation pra) {
        pra.setRuleNo(method.getPracticeRuleAggregation().size()+1);
        pra.setMethod(method);
        method.getPracticeRuleAggregation().add(pra);
        return methodDao.update(method);
    }

    @Override
    public Method removePracticeRuleAggregation(Method method, Long id) {
        Iterator<PracticeRuleAggregation> it = method.getPracticeRuleAggregation().iterator();
        while(it.hasNext()) {
            PracticeRuleAggregation pra = it.next();
            if(pra.getId().equals(id)) {
                it.remove();
                break;
            }
        }
        practiceRuleAggregationDao.delete(id);
        return methodDao.update(method);
    }

    @Override
    public Method updatePracticeRuleAggregation(Long prId, Set<AggregationRule> rules) {
        PracticeRuleAggregation pra = practiceRuleAggregationDao.read(prId);
        Iterator<AggregationRule> it = pra.getRules().iterator();
        while(it.hasNext()) {
            AggregationRule ar = it.next();
            aggregationRuleDao.delete(ar.getId());
            it.remove();
        }
        pra.getRules().addAll(rules);
        practiceRuleAggregationDao.update(pra);
        return pra.getMethod();
    }

}
