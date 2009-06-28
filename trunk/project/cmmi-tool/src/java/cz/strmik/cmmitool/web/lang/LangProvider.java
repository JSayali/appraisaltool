/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.web.lang;

import java.util.ResourceBundle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public class LangProvider {

    private static final ResourceBundle lang = ResourceBundle.getBundle("content.Language");
    private static final Log _log = LogFactory.getLog(LangProvider.class);

    /**
     * No instances.
     */
    private LangProvider() {
    }

    /**
     * Returns localized message key, or ???key??? if key not found.
     *
     * @param key of string
     * @return Localized string
     */
    public static String getString(String key) {
        if(lang.containsKey(key)) {
            return lang.getString(key);
        } else {
            _log.info("Can not find message for key = "+key);
            return "???"+key+"???";
        }
    }

}
