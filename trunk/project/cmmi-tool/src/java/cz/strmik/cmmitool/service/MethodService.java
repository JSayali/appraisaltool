/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.service;

import cz.strmik.cmmitool.entity.method.Method;
import cz.strmik.cmmitool.entity.method.RuleAggregation;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public interface MethodService {

    /**
     * Creates method and adds default scales.
     * 
     * @param method
     * @return Created method
     */
    Method createMethod(Method method);

    Method addPracticeRuleAggregation(Method method, RuleAggregation pra);

    Method addGoalRuleAggregation(Method method, RuleAggregation pra);

    Method updatePracticeRuleAggregation(long methodId, RuleAggregation ruleAggergation);

    Method updateGoalRuleAggregation(long methodId, RuleAggregation ruleAggergation);

    /**
     * Removes aggergation rule. ife there are scale rules int this aggregation rule,
     * they are also removed.
     *
     * @param methodId Id of method, that contains desired aggegation rule to remove.
     * @param id If of aggergation rule to remove
     *
     * @return modfied and persisted Method
     */
    Method removeRuleAggregation(long methodId, Long id);

    /**
     * Removes Rating scale. if rating scale is used in some scale rule,
     * this rule is removed also. When Scale if also removed from
     * aggregation rule, if some aggregation rule that is using this sale
     * exists.
     *
     * Method is automatically persisted into DB.
     *
     * @param methodId existing Method id which this scale belongs to.
     * @param scaleId existing scale id to remove
     *
     * @return modified and persisted Method
     */
    Method removeScale(long methodId, long scaleId);

    /**
     * Removes unused method rating scales (where boolean is false). Collection
     * is cleared and scales are removed. when there are any aggregation rules, they are
     * removed too.
     *
     * @return modified and persisted Method
     */
    Method refreshRatingScales(Method method);

}
