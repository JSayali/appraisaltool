/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.cmmi;

import cz.strmik.cmmitool.dao.GenericDao;
import cz.strmik.cmmitool.entity.Method;
import cz.strmik.cmmitool.entity.RatingScale;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public class DefaultRatingScalesProvider {

    private static final String SCALES_XML = "/config/ratings.xml";
    private static final String LANG = "en";

    /**
     * Adds default scales to method. Only scales, that
     * are required. Uses en locale to name scales.
     *
     * Thread safe.
     *
     * @param method Method to add scales.
     */
    public void addDefaultScales(Method method) {
        if(method.isCharPracticeImplementation()) {
            method.setPracticeImplementation(getScales("practiceImplementation", LANG));
        }
        if(method.isRateGoalSatisfaction()) {
            method.setGoalSatisfaction(getScales("goalSatisfaction", LANG));
        }
        if(method.isRateOrgMaturityLevel()) {
            method.setOrgMaturityLevel(getScales("orgMaturityLevel", LANG));
        }
        if(method.isRateProcessAreaCapLevel()) {
            method.setProcessAreaCapLevel(getScales("processAreaCapLevel", LANG));
        }
        if(method.isRateProcessAreaSatisfaction()) {
            method.setProcessAreaSatisfaction(getScales("processAreaSatisfaction", LANG));
        }
    }

    private Set<RatingScale> getScales(String id, String lang) {
        String key = id+lang;
        if(!loadedScales.containsKey(key)) {
            Set<RatingScale> ratingScales = readScales(id, lang);
            loadedScales.putIfAbsent(key, ratingScales);
        }
        return loadedScales.get(key);
    }

    private Set<RatingScale> readScales(String id, String lang) {
        Set<RatingScale> scales = new HashSet<RatingScale>();
        try {
            InputSource inputSource = new InputSource(getClass().getResourceAsStream(SCALES_XML));
            XPathFactory factory = XPathFactory.newInstance();
            XPath xPath = factory.newXPath();
            NodeList nodes = (NodeList) xPath.evaluate("/ratings/rating[@id='"+id+"']/scale", inputSource,XPathConstants.NODESET);
            for(int i=0;i<nodes.getLength();i++) {
                Node node = nodes.item(i);
                int score = Integer.parseInt(node.getAttributes().getNamedItem("score").getTextContent());
                String name = xPath.evaluate("name[@lang='"+lang+"']", node);
                RatingScale rs = new RatingScale();
                rs.setName(name);
                rs.setOrder(i);
                rs.setScore(score);
                scales.add(rs);
            }
        } catch (XPathExpressionException ex) {
            _log.warn("Unable to load default scales for id="+id+",lang="+lang+" ratings from "+SCALES_XML, ex);
        }
        return scales;
    }

    private ConcurrentMap<String, Set<RatingScale>> loadedScales = new ConcurrentHashMap<String, Set<RatingScale>>();
    private static final Log _log = LogFactory.getLog(DefaultRatingScalesProvider.class);

}
