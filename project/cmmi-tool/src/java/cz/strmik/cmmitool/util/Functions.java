/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.util;

import cz.strmik.cmmitool.entity.AggregationRule;
import cz.strmik.cmmitool.entity.RatingScale;
import cz.strmik.cmmitool.enums.RuleCompletion;
import java.util.Collection;
import java.util.Set;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public class Functions {

    /**
     * Method returns true if specified object is contained in
     * input collection.
     * @param col Input collection for searching
     * @param obj Searched object
     * @return returns true if specified object is contained in input collection.
     */
    public static boolean inCollection(Collection col, Object obj) {
        if (col == null || obj == null) {
            return false;
        }

        return col.contains(obj);
    }

    public static boolean isRuleCompletion(Object scaleO, Object rulesO, String ruleCompletionString, boolean source) {
        RatingScale scale = (RatingScale) scaleO;
        Set<AggregationRule> rules = (Set<AggregationRule>) rulesO;
        RuleCompletion ruleCompletion = RuleCompletion.valueOf(ruleCompletionString);
        for(AggregationRule rule : rules) {
            if(rule.getScale().equals(scale)) {
                if(source && rule.getSource().equals(ruleCompletion)) {
                    return true;
                }
                if(!source && rule.getTarget().equals(ruleCompletion)) {
                    return true;
                }
            }
        }
        return false;
    }
}
