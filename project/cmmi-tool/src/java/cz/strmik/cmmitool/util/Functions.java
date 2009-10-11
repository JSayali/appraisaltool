/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.util;

import cz.strmik.cmmitool.entity.method.ScaleRule;
import cz.strmik.cmmitool.entity.method.RatingScale;
import cz.strmik.cmmitool.entity.method.RuleAggregation;
import cz.strmik.cmmitool.enums.RuleCompletion;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Set;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public class Functions {
    
    public static String URLEncode(java.lang.String url) {
        try {
            return URLEncoder.encode(url.toString(), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

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

    public static boolean isRuleCompletion(RatingScale scale, RuleAggregation ruleAggergation, String ruleCompletionString, boolean source) {
        RuleCompletion ruleCompletion = RuleCompletion.valueOf(ruleCompletionString);
        Set<ScaleRule> rules;
        if(source) {
            rules = ruleAggergation.getSources();
        } else {
            rules = ruleAggergation.getTargets();
        }

        for(ScaleRule rule : rules) {
            if(rule.getScale().equals(scale)) {
                return rule.getRuleCompletion().equals(ruleCompletion);
            }
        }
        return false;
    }
}
