/**
 * The contents of this file are subject to the terms
 * of the GNU GPL 2.0 license. You may not use this
 * file except in compliance with the license.
 *
 * Copyright 2009 Lukáš Strmiska, All rights reserved.
 */
package cz.strmik.cmmitool.cmmi;

import cz.strmik.cmmitool.dao.GenericDao;
import cz.strmik.cmmitool.entity.method.Method;
import cz.strmik.cmmitool.entity.method.RatingScale;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Lukáš Strmiska, strmik@gmail.com
 * @version 1.0
 */
public class DefaultRatingScalesProvider {

    private static final String SCALES_XML = "/config/ratings.xml";
    private static final String SCALES_XSD = "/config/ratingSchema.xsd";
    private static final String LANG = "en";

    public DefaultRatingScalesProvider(GenericDao<RatingScale, Long> ratingScaleDao) {
        this.ratingScaleDao = ratingScaleDao;
    }

    /**
     * Adds default scales to method. Only scales, that
     * are required and are null or empty. Uses en locale
     * to name scales.
     *
     * Scales are automatically persisted.
     *
     * Thread safe.
     *
     * @param method Method to add scales.
     */
    public void addDefaultScales(Method method) {
        if (method.isCharPracticeImplementation() && (method.getPracticeImplementation() == null ||
                method.getPracticeImplementation().isEmpty())) {
            method.setPracticeImplementation(getScales("practiceImplementation", LANG));
            for (RatingScale scale : method.getPracticeImplementation()) {
                scale.setMethodPracImpl(method);
            }
        }
        if (method.isRateGoalSatisfaction() && (method.getGoalSatisfaction() == null ||
                method.getGoalSatisfaction().isEmpty())) {
            method.setGoalSatisfaction(getScales("goalSatisfaction", LANG));
            for (RatingScale scale : method.getGoalSatisfaction()) {
                scale.setMethodGoalSat(method);
            }
        }
        if (method.isRateOrgMaturityLevel() && (method.getOrgMaturityLevel() == null ||
                method.getOrgMaturityLevel().isEmpty())) {
            method.setOrgMaturityLevel(getScales("orgMaturityLevel", LANG));
            for (RatingScale scale : method.getOrgMaturityLevel()) {
                scale.setMethodMatLevel(method);
            }
        }
        if (method.isRateProcessAreaCapLevel() && (method.getProcessAreaCapLevel() == null ||
                method.getProcessAreaCapLevel().isEmpty())) {
            method.setProcessAreaCapLevel(getScales("processAreaCapLevel", LANG));
            for (RatingScale scale : method.getProcessAreaCapLevel()) {
                scale.setMethodProcessCap(method);
            }
        }
        if (method.isRateProcessAreaSatisfaction() && (method.getProcessAreaSatisfaction() == null || method.getProcessAreaSatisfaction().isEmpty())) {
            method.setProcessAreaSatisfaction(getScales("processAreaSatisfaction", LANG));
            for (RatingScale scale : method.getProcessAreaSatisfaction()) {
                scale.setMethodProcessSat(method);
            }
        }
    }

    private Set<RatingScale> getScales(String id, String lang) {
        String key = id + lang;
        if (!loadedScales.containsKey(key)) {
            List<RatingScale> ratingScales = readScales(id, lang);
            loadedScales.putIfAbsent(key, ratingScales);
        }
        Set<RatingScale> resultSet = new HashSet<RatingScale>(loadedScales.get(key).size());
        for (RatingScale readRs : loadedScales.get(key)) {
            RatingScale rs = new RatingScale(readRs.getName(), readRs.getOrder(), readRs.getScore(), readRs.getColor());
            ratingScaleDao.create(rs);
            resultSet.add(rs);
        }
        return resultSet;
    }

    private List<RatingScale> readScales(String id, String lang) {
        List<RatingScale> scales = new ArrayList<RatingScale>();
        try {
            // TODO: throws expection, when everyting is OK.
            //validate(SCALES_XML);
            InputSource inputSource = new InputSource(getClass().getResourceAsStream(SCALES_XML));
            XPathFactory factory = XPathFactory.newInstance();
            XPath xPath = factory.newXPath();
            NodeList nodes = (NodeList) xPath.evaluate("/ratings/rating[@id='" + id + "']/scale", inputSource, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                int score = Integer.parseInt(node.getAttributes().getNamedItem("score").getTextContent());
                String name = xPath.evaluate("name[@lang='" + lang + "']", node);
                String color = xPath.evaluate("color[@type='html']", node);
                RatingScale rs = new RatingScale(name, i, score, color);
                if (i == 0) {
                    rs.setDefaultRating(true);
                }
                scales.add(rs);
            }
        } catch (Exception ex) {
            _log.warn("Unable to load default scales for id=" + id + ",lang=" + lang + " ratings from " + SCALES_XML, ex);
        }
        return scales;
    }

    private void validate(String scalesFile) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = parser.parse(new InputSource(getClass().getResourceAsStream(scalesFile)));
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        Source schemaFile = new StreamSource(getClass().getResourceAsStream(SCALES_XSD));
        Schema schema = factory.newSchema(schemaFile);

        Validator validator = schema.newValidator();
        validator.validate(new DOMSource(document));
    }

    private ConcurrentMap<String, List<RatingScale>> loadedScales = new ConcurrentHashMap<String, List<RatingScale>>();
    private final GenericDao<RatingScale, Long> ratingScaleDao;
    private static final Log _log = LogFactory.getLog(DefaultRatingScalesProvider.class);

}
