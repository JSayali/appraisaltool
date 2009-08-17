/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.service;

import cz.strmik.cmmitool.entity.Method;
import cz.strmik.cmmitool.entity.RatingScale;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public interface MethodService {

    Method removeScale(Method method, RatingScale scale);

    /**
     * Removes unused method scales (when boolean is false). Collection
     * is cleared and scales are removed.
     *
     * @param method
     */
    Method removeUnusedScales(Method method);

}
