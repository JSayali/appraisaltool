/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.aspect;

import cz.strmik.cmmitool.entity.method.Method;
import java.util.List;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
@Aspect
public class DaoAspect {

    /**
     * Aspect that loads method boos, after loaded by DAO.
     * @param retVal
     */
    @AfterReturning(
        pointcut="bean(methodDao)",
        returning="retVal")
    public void setupMethodBools(Object retVal) {
        if(retVal!=null) {
            if(retVal instanceof Method) {
                ((Method)retVal).setupBools();
            } else if(retVal instanceof List) {
                List<Method> methods = (List<Method>) retVal;
                for(Method m : methods) {
                    m.setupBools();
                }
            }
        }        
    }
    
}
