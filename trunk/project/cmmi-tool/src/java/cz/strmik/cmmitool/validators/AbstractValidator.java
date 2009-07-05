/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.validators;

import java.util.regex.Pattern;
import org.springframework.validation.Validator;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public abstract class AbstractValidator implements Validator {

    protected static Pattern PATTERN_EMAIL = Pattern.compile(".+@.+\\.[a-z]+");
    protected static Pattern PATTERN_ID = Pattern.compile("\\w+");

}
