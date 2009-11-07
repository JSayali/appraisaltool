/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.enums;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public enum PracticeEvidenceAdequacy {
    NOT_YET_REVIEWED, STRONG_EVIDENCE, NO_EVIDENCE, CONFLICTING_EVIDENCE, ANOMALOUS_EVIDENCE, INSUFFICIENT_EVIDENCE;

    public String getKey() {
        return getClass().getName() + "." + toString();
    }

}
