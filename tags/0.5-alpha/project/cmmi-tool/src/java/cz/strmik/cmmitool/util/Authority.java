/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.util;

import cz.strmik.cmmitool.enums.ApplicationRole;
import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public class Authority implements GrantedAuthority {

    private ApplicationRole role;

    public Authority(ApplicationRole role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role.toString();
    }

    @Override
    public int compareTo(GrantedAuthority o) {
        return role.toString().compareTo(o.getAuthority());
    }

}
