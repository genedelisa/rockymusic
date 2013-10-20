/*
 * $Id$
 *
 * Copyright 1998,1999,2000,2001 by Rockhopper Technologies, Inc.,
 * 75 Trueman Ave., Haddonfield, New Jersey, 08033-2529, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Rockhopper Technologies, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with RTI.
 */

package com.rockhoppertech.music.midi.js.xml;

/*
 * #%L
 * Rocky Music Core
 * %%
 * Copyright (C) 1996 - 2013 Rockhopper Technologies
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.rockhoppertech.music.scale.Scale;
import com.rockhoppertech.music.scale.ScaleFactory;

/**
 * Class <code>ScaleFactoryXMLHelper</code>
 * 
 * 
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * @version $Revision$, $Date$
 * @since 1.0
 */

public class ScaleFactoryXMLHelper {
    private static Logger logger = LoggerFactory
            .getLogger(ScaleFactoryXMLHelper.class);
    static String scaleDefinitionFileName = ScaleFactory
            .getDefinitionFileName();

    /**
     * @return the scaleDefinitionFileName
     */
    public static String getScaleDefinitionFileName() {
        return scaleDefinitionFileName;
    }

    /**
     * @param scaleDefinitionFileName
     *            the scaleDefinitionFileName to set
     */
    public static void setScaleDefinitionFileName(String scaleDefinitionFileName) {
        ScaleFactoryXMLHelper.scaleDefinitionFileName = scaleDefinitionFileName;
    }

    static void stdout(Document doc) {
        logger.debug("Enter stdout");
        try {
            DOMSource domSource = new DOMSource(doc);
            StreamResult streamResult = new StreamResult(System.out);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, streamResult);
        } catch (TransformerException e) {
            System.err.println(e);
        }
    }

    public static void writeScaleXML(OutputStream out) {
        logger.debug("enter writeScaleXML");
        DocumentBuilderFactory dbf = null;
        DocumentBuilder db = null;
        try {
            dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document doc = db.newDocument();

        Element chords = doc.createElement("scales");
        doc.appendChild(chords);

        // Element extends Node

        List<String> list = ScaleFactory.getScaleNames();

        for (String name : list) {
            Scale scale = ScaleFactory.getScaleByName(name);
            logger.debug("writing scale " + scale);
            Element scaleElement = doc.createElement("scale");
            chords.appendChild(scaleElement);
            scaleElement.setAttribute("name", scale.getName());

            Element intervals = doc.createElement("intervals");
            scaleElement.appendChild(intervals);
            for (Integer i : scale.getIntervals()) {
                Element intervalel = doc.createElement("interval");
                String interval = i.toString();
                intervalel.appendChild(doc.createTextNode(interval));
                intervals.appendChild(intervalel);
            }

            if (scale.isDescendingDifferent()) {
                logger.debug("scale descending is different");
                Element dintervals = doc.createElement("descendingIntervals");
                scaleElement.appendChild(dintervals);
                for (Integer i : scale.getDescendingIntervals()) {
                    Element intervalel = doc.createElement("interval");
                    String interval = i.toString();
                    intervalel.appendChild(doc.createTextNode(interval));
                    dintervals.appendChild(intervalel);
                }
            }

            Element spelling = doc.createElement("spelling");
            scaleElement.appendChild(spelling);
            String s = scale.getSpelling();
            String[] spellings = s.split("\\s");
            for (String degree : spellings) {
                Element degreeEl = doc.createElement("degree");
                degreeEl.appendChild(doc.createTextNode(degree));
                spelling.appendChild(degreeEl);
            }

            String descr = scale.getDescription();
            if (descr != null) {
                Element el = doc.createElement("description");
                scaleElement.appendChild(el);
                el.appendChild(doc.createTextNode(descr));
            }
        }
        // logger.debug("writing to stdout");
        // try {
        // DOMSource domSource = new DOMSource(doc);
        // StreamResult streamResult = new StreamResult(out);
        // TransformerFactory tf = TransformerFactory.newInstance();
        // Transformer transformer = tf.newTransformer();
        // Properties oprops = new Properties();
        // oprops.put(OutputKeys.INDENT, "yes");
        // oprops.put(OutputKeys.METHOD, "xml");
        // oprops.put(OutputKeys.OMIT_XML_DECLARATION, "no");
        // oprops.put(OutputKeys.DOCTYPE_PUBLIC,
        // "-//Rockhopper Technologies//DTD scales 1.0//EN");
        // oprops.put(OutputKeys.DOCTYPE_SYSTEM,
        // "http://www.rockhoppertech.com/dtds/scales.dtd");
        //
        // transformer.setOutputProperties(oprops);
        // transformer.transform(domSource, streamResult);
        // } catch (TransformerException e) {
        // System.err.println(e);
        // }
        logger.debug("leave writexml scale");
    }

    public static void readScales() {
        InputStream is = ScaleFactory.class.getResourceAsStream("/"
                + getScaleDefinitionFileName());

        logger.debug("input stream ", is);

        setDefinitionInputStream(is);

        // this will probably ignore the filename and use the inputstream
        // we just made
        readScales(getScaleDefinitionFileName());
    }

    public static void readScales(String filename) {
        // these are jaxp classes
        DocumentBuilder db = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            // dbf.setCoalescing(true);
            // dbf.setExpandEntityReferences(true);
            // dbf.setIgnoringComments(false);
            // dbf.setIgnoringElementContentWhitespace(true);
            // dbf.setNamespaceAware(true);

            dbf.setValidating(false);
            db = dbf.newDocumentBuilder();
            // db.setEntityResolver(EntityResolver er);
            // db.setErrorHandler(ErrorHandler eh);

        } catch (ParserConfigurationException e) {
            System.err.println(e);
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }
            e.printStackTrace();
        }

        Document doc = null;

        try {

            // all of this nonsense in case you're using this from
            // a swing app or from a servlet. A servlet would set the
            // inputstream
            // in its init method.
            InputSource is = null;

            if (inputStream != null) {
                logger.debug("reading from inputstream");
                is = new InputSource(inputStream);
            } else {

                File f = new File(filename);
                // String uri = f.toURL().toString();
                String uri = f.toURI().toURL().toString();

                logger.debug("defs ", scaleDefinitionFileName);
                logger.debug("uri ", uri);

                is = new InputSource(new FileReader(f));
                // is = new InputSource(ScaleFactoryXMLHelper.class
                // .getResourceAsStream(uri));
                // if not set, then it won't show up in exceptions
                is.setSystemId(uri);
            }

            doc = db.parse(is);
            doc.normalize();
        } catch (SAXParseException spe) {
            logger.error("error parsing", spe);
            Exception x = spe.getException();
            if (x != null) {
                logger.error("error parsing", x);
            }
        } catch (SAXException sxe) {
            Exception x = sxe;
            if (sxe.getException() != null) {
                x = sxe.getException();
                logger.error(x.getMessage(), x);
            }
            // } catch (MalformedURLException mue) {

        } catch (IOException ioe) {
            logger.error("io problem", ioe);
            return;

        } catch (Throwable t) {
            t.printStackTrace();
            return;
        }

        Element e = doc.getDocumentElement();
        String name = e.getTagName();
        logger.debug("parsing scales document element " + name);

        NodeList scales = e.getElementsByTagName("scale");
        if (scales != null) {
            int length = scales.getLength();
            for (int i = 0; i < length; i++) {
                Node nl = scales.item(i);
                logger.debug("parsing scale ");
                parseScale(nl);
            }
        } else {
            System.err.println("no scales!");
        }
    }

    /**
     * Attempt to parse each scale element and register it with the
     * ScaleFactory. Ignores the spelling if the intervals are specified.
     * 
     * ScaleFactory.registerScale(mc);
     * 
     * @param scale
     */
    private static void parseScale(Node scale) {
        if (scale == null) {
            System.err.println("parseScale: node is null");
            return;
        }
        if (scale.getNodeName().equals("scale") == false) {
            System.err.println("parseScale: node is not a scale");
            return;
        }
        NamedNodeMap pa = scale.getAttributes();
        Node ni = pa.getNamedItem("name");
        if (ni == null) {
            throw new IllegalArgumentException(
                    "Invalid scale; no name attribute");
        }
        String name = ni.getNodeValue();
        logger.debug(name);
        Integer[] intervals = null;
        Integer[] dintervals = null;
        String spelling = null;
        String description = null;

        if (scale.hasChildNodes()) {
            NodeList sc = scale.getChildNodes();
            int len = sc.getLength();
            for (int i = 0; i < len; i++) {
                Node nn = sc.item(i);
                if (nn.getNodeName().equalsIgnoreCase("intervals")) {
                    intervals = parseIntervals(nn);
                }
                if (nn.getNodeName().equalsIgnoreCase("descendingIntervals")) {
                    dintervals = parseDescendingIntervals(nn);
                }
                if (nn.getNodeName().equalsIgnoreCase("spelling")) {
                    spelling = parseSpelling(nn);
                }
                if (nn.getNodeName().equalsIgnoreCase("description")) {
                    description = nn.getTextContent();
                    logger.debug("description " + description);
                }
            }
        }
        // String s = String.format("xml %s has intervals %s \n", name,
        // ArrayUtils
        // .asString(intervals));
        // System.err.println(s);

        Scale mc = null;
        if (intervals != null) {
            mc = new Scale(name, intervals, description);
            logger.debug("creating from intervals " + name);
            if (spelling != null) {
                mc.setSpelling(spelling);
            }
        } else if (spelling != null) {
            mc = new Scale(name, spelling);
            mc.setDescription(description);
        }

        if (dintervals != null) {
            mc.setDescendingIntervals(dintervals);
        }
        ScaleFactory.registerScale(mc);
    }

    private static Integer[] parseIntervals(Node intervals) {
        NodeList sc = intervals.getChildNodes();
        int len = sc.getLength();
        // int[] a = new int[len];
        List<Integer> list = new ArrayList<Integer>();

        for (int i = 0; i < len; i++) {
            Node nn = sc.item(i);
            if (nn.getNodeName().equalsIgnoreCase("interval")) {
                // System.out.println(nn.getTextContent());
                // a[i] = Integer.parseInt(nn.getTextContent());
                list.add(Integer.parseInt(nn.getTextContent()));
            }
        }
        Integer[] array = new Integer[list.size()];
        return list.toArray(array);
    }

    /**
     * <p>
     * delete this. no different from parseIntervals
     * </p>
     * 
     * @param intervals
     * @return
     */
    private static Integer[] parseDescendingIntervals(Node intervals) {
        NodeList sc = intervals.getChildNodes();
        int len = sc.getLength();
        int[] a = new int[len];
        List<Integer> list = new ArrayList<Integer>();

        for (int i = 0; i < len; i++) {
            Node nn = sc.item(i);
            if (nn.getNodeName().equalsIgnoreCase("interval")) {
                // System.out.println(nn.getTextContent());
                a[i] = Integer.parseInt(nn.getTextContent());
                list.add(Integer.parseInt(nn.getTextContent()));
            }
        }
        Integer[] array = new Integer[list.size()];
        return list.toArray(array);
    }

    private static String parseSpelling(Node spelling) {
        StringBuilder sb = new StringBuilder();
        NodeList sc = spelling.getChildNodes();
        int len = sc.getLength();
        for (int i = 0; i < len; i++) {
            Node nn = sc.item(i);
            if (nn.getNodeName().equalsIgnoreCase("degree")) {
                sb.append(nn.getTextContent());
                sb.append(' ');
            }
        }
        return sb.toString();
    }

    /**
     * 
     * @param is
     */
    public static void setDefinitionInputStream(InputStream is) {
        inputStream = is;

    }

    static InputStream inputStream;

}
/*
 * 
 * <scale name="maj9-5"> <intervals> <interval>4</interval>
 * <interval>6</interval> </intervals> <spelling> <degree>1</degree>
 * <degree>3</degree> </spelling> <description>major ninth diminished
 * fifth</description> </chscaleord>
 */

/*
 * History:
 * 
 * $Log$
 * 
 * This version: $Revision$ Last modified: $Date$ Last modified by: $Author$
 */
