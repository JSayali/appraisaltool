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
public enum MaturityLevel {

    ML2(2),
    ML3(3),
    ML4(4),
    ML5(5);

    private final int level;

    MaturityLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
    
}
