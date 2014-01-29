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
 * Copyright (C) 1996 - 2014 Rockhopper Technologies
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
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

import com.rockhoppertech.music.chord.Chord;
import com.rockhoppertech.music.chord.ChordFactory;
import com.rockhoppertech.music.chord.ChordProgression;
import com.rockhoppertech.music.chord.ChordVoicing;
import com.rockhoppertech.music.chord.UnknownChordException;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.scale.ScaleFactory;

/**
 * Class <code>ChordFactoryXMLHelper</code>
 * 
 * 
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * @version $Revision$, $Date$
 * @since 1.0
 */

public class ChordFactoryXMLHelper {
    private static final Logger logger = LoggerFactory
            .getLogger(ChordFactoryXMLHelper.class);
    static String definitionFileName = ChordFactory.getDefinitionFileName();
    static InputStream inputStream;

    public static void setDefinitionInputStream(InputStream is) {
        inputStream = is;
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

    public static String toXMLString(ChordProgression chordProgression) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ChordFactoryXMLHelper.writeChordProgressionXML(chordProgression, os);
        try {
            return os.toString("ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return null;
    }

    public static String chordToXMLString(Chord chord) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ChordFactoryXMLHelper.writeChordXML(chord, os);
        try {
            return os.toString("ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    public static void writeChordProgressionXML(
            ChordProgression chordProgression, OutputStream out) {
        logger.debug("enter writeChordXML");
        DocumentBuilderFactory dbf = null;
        DocumentBuilder db = null;
        try {
            dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            // dbf.setSchema()
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return;
        }
        Document doc = (Document) db.newDocument();
        String namespaceURI = "http://www.rockhoppertech.com/music/chordprogression";
        Element root = doc.createElementNS(namespaceURI, "chordprogression");
        doc.appendChild(root);

        // DecimalFormat fmt = new DecimalFormat("#,###,###,###.##");
        Element chordElement = null;
        for (Chord chord : chordProgression) {
            chordElement = doc.createElement("chord");
            root.appendChild(chordElement);
            chordElement.setAttribute("displayName", chord.getDisplayName());

            Element octave = doc.createElement("octave");
            octave.appendChild(doc.createTextNode("" + chord.getOctave()));
            chordElement.appendChild(octave);

            Element startBeat = doc.createElement("startBeat");
            startBeat
                    .appendChild(doc.createTextNode("" + chord.getStartBeat()));
            chordElement.appendChild(startBeat);

            Element duration = doc.createElement("duration");
            duration.appendChild(doc.createTextNode("" + chord.getDuration()));
            chordElement.appendChild(duration);

            Element voicing = doc.createElement("voicing");
            chordElement.appendChild(voicing);
            String[] voicingDegrees = chord.getChordVoicing()
                    .split("\\s");
            for (String degree : voicingDegrees) {
                Element degreeEl = doc.createElement("degree");
                degreeEl.appendChild(doc.createTextNode(degree));
                voicing.appendChild(degreeEl);
            }
        }

        doc.getDocumentElement().normalize();

        try {
            DOMSource domSource = new DOMSource(doc);
            StreamResult streamResult = new StreamResult(out);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            Properties oprops = new Properties();
            oprops.put(OutputKeys.INDENT, "yes");
            oprops.put(OutputKeys.METHOD, "xml");
            oprops.put(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperties(oprops);
            transformer.transform(domSource, streamResult);
        } catch (TransformerException e) {
            System.err.println(e);
        }
    }

    public static ChordProgression xmlToChordProgression(String xml)
            throws UnknownChordException {
        DocumentBuilder db = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setCoalescing(true);
            dbf.setExpandEntityReferences(true);
            dbf.setIgnoringComments(false);
            dbf.setIgnoringElementContentWhitespace(true);
            dbf.setNamespaceAware(true);
            dbf.setValidating(false);
            db = dbf.newDocumentBuilder();
            // db.setEntityResolver(EntityResolver er);
            // db.setErrorHandler(ErrorHandler eh);
        } catch (ParserConfigurationException e) {
            logger.error(e.getLocalizedMessage(), e);
            e.printStackTrace();
            return null;
        }

        Document doc = null;
        try {
            InputSource is = new InputSource(new StringReader(xml));
            doc = db.parse(is);
            doc.normalize();
        } catch (SAXParseException spe) {
            logger.error(spe.getLocalizedMessage(), spe);
            Exception x = spe.getException();
            if (x != null) {
                logger.error(x.getLocalizedMessage(), x);
            }
            return null;
        } catch (SAXException sxe) {
            Exception x = sxe;
            if (sxe.getException() != null) {
                x = sxe.getException();
                logger.error(x.getLocalizedMessage(), x);
            }
            return null;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            logger.error(ioe.getLocalizedMessage(), ioe);            
            return null;
        } catch (Throwable t) {
            t.printStackTrace();
            logger.error(t.getLocalizedMessage(), t);            
            return null;
        }

        Element e = doc.getDocumentElement();
        String name = e.getTagName();
        logger.debug("parsing chords document element " + name);
        Chord chord = null;
        List<Chord> list = new ArrayList<Chord>();
        if (name.equalsIgnoreCase("ChordProgression")) {
            NodeList chords = e.getElementsByTagName("chord");
            if (chords != null) {
                int length = chords.getLength();
                for (int i = 0; i < length; i++) {
                    Node nl = chords.item(i);
                    logger.debug("parsing chord ");
                    chord = parseChordProgressionChord(nl);
                    list.add(chord);
                }
            } else {
                System.err.println("no chords!");
            }
        }

        return new ChordProgression(list, "");
    }

    public static void writeChordXML(OutputStream out) {
        logger.debug("enter writeChordXML");
        DocumentBuilderFactory dbf = null;
        DocumentBuilder db = null;
        try {
            dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return;
        }
        Document doc = (Document) db.newDocument();

        Element chords = doc.createElement("chords");
        doc.appendChild(chords);

        // Element extends Node

        Set<String> list = ChordFactory.getSymbolNames();

        for (String symbol : list) {
            Chord mc = null;
            try {
                mc = ChordFactory.getChordBySymbol(symbol);
            } catch (UnknownChordException e) {
                e.printStackTrace();
                logger.error(e.getLocalizedMessage(), e);
                return;
            }
            Element chord = doc.createElement("chord");
            chords.appendChild(chord);
            chord.setAttribute("name", mc.getSymbol());

            Element intervals = doc.createElement("intervals");
            chord.appendChild(intervals);
            for (Integer i : mc.getIntervals()) {
                Element intervalel = doc.createElement("interval");
                String interval = i.toString();
                intervalel.appendChild(doc.createTextNode(interval));
                intervals.appendChild(intervalel);
            }

            Element spelling = doc.createElement("spelling");
            chord.appendChild(spelling);
            String s = mc.getSpelling();
            String[] spellings = s.split("\\s");
            for (String degree : spellings) {
                Element degreeEl = doc.createElement("degree");
                degreeEl.appendChild(doc.createTextNode(degree));
                spelling.appendChild(degreeEl);
            }

            String descr = mc.getDescription();
            if (descr != null) {
                Element el = doc.createElement("description");
                chord.appendChild(el);
                el.appendChild(doc.createTextNode(descr));
            }
        }
        logger.debug("writing to stdout");
        try {
            DOMSource domSource = new DOMSource(doc);
            StreamResult streamResult = new StreamResult(out);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            Properties oprops = new Properties();
            oprops.put(OutputKeys.INDENT, "yes");
            oprops.put(OutputKeys.METHOD, "xml");
            oprops.put(OutputKeys.OMIT_XML_DECLARATION, "no");
            oprops.put(OutputKeys.DOCTYPE_PUBLIC,
                    "-//Rockhopper Technologies//DTD chords 1.0//EN");
            oprops.put(OutputKeys.DOCTYPE_SYSTEM,
                    "http://www.rockhoppertech.com/dtds/chords.dtd");

            transformer.setOutputProperties(oprops);
            transformer.transform(domSource, streamResult);
        } catch (TransformerException e) {
            System.err.println(e);
        }
        logger.debug("leave writexml chord");
    }

    public static Chord xmlToChord(String xml) {
        DocumentBuilder db = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setCoalescing(true);
            dbf.setExpandEntityReferences(true);
            dbf.setIgnoringComments(false);
            dbf.setIgnoringElementContentWhitespace(true);
            dbf.setNamespaceAware(true);
            dbf.setValidating(false);
            db = dbf.newDocumentBuilder();
            // db.setEntityResolver(EntityResolver er);
            // db.setErrorHandler(ErrorHandler eh);
        } catch (ParserConfigurationException e) {
            logger.error(e.getLocalizedMessage(), e);
            e.printStackTrace();
            return null;
        }

        Document doc = null;
        try {
            InputSource is = new InputSource(new StringReader(xml));
            doc = db.parse(is);
            doc.normalize();
        } catch (SAXParseException spe) {
            logger.error(spe.getLocalizedMessage(), spe);
            Exception x = spe.getException();
            if (x != null) {
                logger.error(x.getLocalizedMessage(), x);
            }
            return null;
        } catch (SAXException sxe) {
            Exception x = sxe;
            if (sxe.getException() != null) {
                x = sxe.getException();
                logger.error(x.getLocalizedMessage(), x);
            }
            return null;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            logger.error(ioe.getLocalizedMessage(), ioe);
            return null;
        } catch (Throwable t) {
            t.printStackTrace();
            logger.error(t.getLocalizedMessage(), t);
            return null;
        }

        Element e = doc.getDocumentElement();
        String name = e.getTagName();
        logger.debug("parsing chords document element " + name);
        Chord chord = null;

        if (name.equalsIgnoreCase("Chords")) {
            NodeList chords = e.getElementsByTagName("chord");
            if (chords != null) {
                int length = chords.getLength();
                for (int i = 0; i < length; i++) {
                    Node nl = chords.item(i);
                    logger.debug("parsing chord ");
                    chord = parseChord(nl);
                    // just return the first one
                    return chord;
                }
            } else {
                System.err.println("no chords!");
            }
        } else if (name.equalsIgnoreCase("MIDITrack")) {
            // FIXME this fails on non default voicings. Probably too much to
            // take voicing into account.
            MIDITrack nl = MIDITrackXMLHelper
                    .readMIDITrackFromXMLString(xml);
            chord = ChordFactory.createFromIntervals(nl
                    .getPitchIntervalsAbsolute(), nl.get(0).getMidiNumber());
            String vs = chord.getVoicingString(nl);
            logger.debug("Calculated voicing s " + vs);
        }

        return chord;
    }

    public static List<Chord> xmlToChordList(String xml) {
        DocumentBuilder db = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setCoalescing(true);
            dbf.setExpandEntityReferences(true);
            dbf.setIgnoringComments(false);
            dbf.setIgnoringElementContentWhitespace(true);
            dbf.setNamespaceAware(true);
            dbf.setValidating(false);
            db = dbf.newDocumentBuilder();
            // db.setEntityResolver(EntityResolver er);
            // db.setErrorHandler(ErrorHandler eh);
        } catch (ParserConfigurationException e) {
            logger.error(e.getLocalizedMessage(),e);
            e.printStackTrace();
            return null;
        }

        Document doc = null;
        try {
            InputSource is = new InputSource(new StringReader(xml));
            doc = db.parse(is);
            doc.normalize();
        } catch (SAXParseException spe) {
            logger.error(spe.getLocalizedMessage(), spe);
            Exception x = spe.getException();
            if (x != null) {
                logger.error(x.getLocalizedMessage(), x);
            }
            return null;
        } catch (SAXException sxe) {
            Exception x = sxe;
            if (sxe.getException() != null) {
                x = sxe.getException();
                logger.error(x.getLocalizedMessage(), x);
            }
            return null;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }

        Element e = doc.getDocumentElement();
        String name = e.getTagName();
        logger.debug("parsing chords document element " + name);
        Chord chord = null;
        List<Chord> list = new ArrayList<Chord>();
        if (name.equalsIgnoreCase("Chords")) {
            NodeList chords = e.getElementsByTagName("chord");
            if (chords != null) {
                int length = chords.getLength();
                for (int i = 0; i < length; i++) {
                    Node nl = chords.item(i);
                    logger.debug("parsing chord ");
                    chord = parseChord(nl);
                    list.add(chord);
                }
            } else {
                System.err.println("no chords!");
            }
        } else if (name.equalsIgnoreCase("MIDITrack")) {
            // FIXME this fails on non default voicings. Probably too much to
            // take voicing into account.
            MIDITrack nl = MIDITrackXMLHelper
                    .readMIDITrackFromXMLString(xml);
            chord = ChordFactory.createFromIntervals(nl
                    .getPitchIntervalsAbsolute(), nl.get(0).getMidiNumber());
            list.add(chord);
        }

        return list;
    }

    public static void readChords(String filename) {
        InputStream inputstream = ScaleFactory.class.getResourceAsStream("/"
                + filename);
        if (logger.isDebugEnabled()) {
            logger.debug("input stream " + inputstream);
        }
        setDefinitionInputStream(inputstream);

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
            logger.error(e.getLocalizedMessage(), e);
            e.printStackTrace();
            return;
        }

        Document doc = null;
        try {
            // File f = new File(filename);
            // String uri = f.toURL().toString();
            // InputSource is = new InputSource(new FileReader(f));
            // // if not set, then it won't show up in exceptions
            // is.setSystemId(uri);

            // all of this nonsense in case you're using this from
            // a swing app or from a servlet. A servlet would set the
            // inputstream
            // in its init method.
            InputSource is = null;

            if (inputStream != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("reading from inputstream");
                }
                is = new InputSource(inputStream);
            } else {

                File f = new File(filename);
                String uri = f.toURI().toString();
                if (logger.isDebugEnabled()) {
                    logger.debug("defs " + definitionFileName);
                    logger.debug("uri " + uri);
                }
                // filereader always uses default encoding
                // is = new InputSource(new FileReader(f));
                is = new InputSource(new InputStreamReader(new FileInputStream(
                        f), Charset.forName("ISO-8859-1")));
                // if not set, then it won't show up in exceptions
                is.setSystemId(uri);
            }

            doc = db.parse(is);
            doc.normalize();
        } catch (SAXParseException spe) {
            System.err.println(spe);
            Exception x = spe.getException();
            if (x != null) {
                System.err.println(x);
            }
            return;
        } catch (SAXException sxe) {
            Exception x = sxe;
            if (sxe.getException() != null) {
                x = sxe.getException();
                logger.debug(x.getLocalizedMessage());
            }
            return;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        } catch (Throwable t) {
            t.printStackTrace();
            return;
        }

        Element e = doc.getDocumentElement();
        String name = e.getTagName();
        logger.debug("parsing chords document element " + name);

        NodeList chords = e.getElementsByTagName("chord");
        if (chords != null) {
            int length = chords.getLength();
            for (int i = 0; i < length; i++) {
                Node nl = chords.item(i);
                logger.debug("parsing chord ");
                parseChord(nl);
            }
        } else {
            System.err.println("no chords!");
        }
    }

    private static Chord parseChordProgressionChord(Node chord)
            throws UnknownChordException {
        if (chord == null) {
            System.err.println("parseChord: node is null");
            return null;
        }
        if (chord.getNodeName().equals("chord") == false) {
            System.err.println("parseChord: node is not a chord");
            return null;
        }
        NamedNodeMap pa = chord.getAttributes();
        Node ni = pa.getNamedItem("displayName");
        if (ni == null) {
            throw new IllegalArgumentException("Invalid chord");
        }
        String displayName = ni.getNodeValue();
        String voicing = null;
        int octave = 0;
        double startBeat = 1d;
        double duration = 4d;

        /*
         * <chordprogression
         * xmlns="http://www.rockhoppertech.com/music/chordprogression"> <chord
         * displayName="C"> <octave>5</octave> <startBeat>1.0</startBeat>
         * <duration>2.0</duration> <voicing> <degree>1</degree>
         * <degree>3</degree> <degree>5</degree> </voicing> </chord>
         * 
         * </chordprogression>
         */
        if (chord.hasChildNodes()) {
            NodeList sc = chord.getChildNodes();
            int len = sc.getLength();
            for (int i = 0; i < len; i++) {
                Node nn = sc.item(i);
                if (nn.getNodeName().equalsIgnoreCase("voicing")) {
                    voicing = parseVoicing(nn);
                }
                if (nn.getNodeName().equalsIgnoreCase("octave")) {
                    NodeList cn = nn.getChildNodes();
                    Node on = cn.item(0);
                    octave = Integer.parseInt(on.getTextContent());
                }
                if (nn.getNodeName().equalsIgnoreCase("startBeat")) {
                    NodeList cn = nn.getChildNodes();
                    Node on = cn.item(0);
                    startBeat = Double.parseDouble(on.getTextContent());
                }
                if (nn.getNodeName().equalsIgnoreCase("duration")) {
                    NodeList cn = nn.getChildNodes();
                    Node on = cn.item(0);
                    duration = Double.parseDouble(on.getTextContent());
                }
            }
        }
        Chord mc = ChordFactory.getChordByFullSymbol(displayName);
        if (voicing != null) {
            mc.setChordVoicing(voicing);
        }
        mc.setOctave(octave);
        mc.setStartBeat(startBeat);
        mc.setDuration(duration);

        return mc;
    }

    /**
     * Attempt to parse each chord element and add it to the ChordFactory.
     * Ignores the spelling if the intervals are specified.
     * 
     * @param chord
     */
    private static Chord parseChord(Node chord) {
        if (chord == null) {
            System.err.println("parseChord: node is null");
            return null;
        }
        if (chord.getNodeName().equals("chord") == false) {
            System.err.println("parseChord: node is not a chord");
            return null;
        }
        NamedNodeMap pa = chord.getAttributes();
        Node ni = pa.getNamedItem("name");
        if (ni == null) {
            throw new IllegalArgumentException("Invalid chord");
        }
        String symbol = ni.getNodeValue();
        Integer[] intervals = null;
        String spelling = null;
        String description = null;
        String[] aliases = null;
        String voicing = null;

        if (chord.hasChildNodes()) {
            NodeList sc = chord.getChildNodes();
            int len = sc.getLength();
            for (int i = 0; i < len; i++) {
                Node nn = sc.item(i);
                if (nn.getNodeName().equalsIgnoreCase("intervals")) {
                    intervals = parseIntervals(nn);
                }
                if (nn.getNodeName().equalsIgnoreCase("spelling")) {
                    spelling = parseSpelling(nn);
                }
                if (nn.getNodeName().equalsIgnoreCase("description")) {
                    description = nn.getTextContent();
                    description = description.trim();
                }
                if (nn.getNodeName().equalsIgnoreCase("aliases")) {
                    aliases = parseAliases(nn);
                }
                if (nn.getNodeName().equalsIgnoreCase("voicing")) {
                    voicing = parseVoicing(nn);
                }
            }
        }
        Chord mc = null;

        if (intervals != null) {
            mc = new Chord(symbol, intervals, description);
        } else {
            mc = new Chord(symbol, spelling, description);
        }
        if (voicing != null) {
            mc.setChordVoicing(voicing);
        }

        if (aliases == null) {
            ChordFactory.registerChord(mc, mc.getSymbol());
            if (logger.isDebugEnabled()) {
                String s = String.format("registering chord no alias '%s'", mc);
                logger.debug(s);
            }
        } else {
            ChordFactory.registerChord(mc, mc.getSymbol());
            for (String alias : aliases) {
                mc.addAlias(alias);
                ChordFactory.registerChord(mc, alias);
                if (logger.isDebugEnabled()) {
                    String s = String.format("registering chord alias '%s'",
                            alias);
                    logger.debug(s);
                }
            }
        }
        return mc;
        /*
         * System.out.println(mc); System.out.println(intervals);
         * System.out.println(spelling);
         */
    }

    private static String[] parseAliases(Node aliases) {
        NodeList sc = aliases.getChildNodes();
        int len = sc.getLength();
        List<String> list = new ArrayList<String>();

        for (int i = 0; i < len; i++) {
            Node nn = sc.item(i);
            if (nn.getNodeName().equalsIgnoreCase("alias")) {
                // System.out.println(nn.getTextContent());
                list.add(nn.getTextContent());
            }
        }
        String[] array = new String[list.size()];
        return list.toArray(array);
    }

    private static Integer[] parseIntervals(Node intervals) {
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

    private static String parseVoicing(Node voicing) {
        ChordVoicing cv = null;
        StringBuilder sb = new StringBuilder();
        NodeList sc = voicing.getChildNodes();
        int len = sc.getLength();
        for (int i = 0; i < len; i++) {
            Node nn = sc.item(i);
            if (nn.getNodeName().equalsIgnoreCase("degree")) {
                sb.append(nn.getTextContent());
                sb.append(' ');
            }
        }
        cv = new ChordVoicing(sb.toString());
        if (logger.isDebugEnabled()) {
            String s = String.format("created voicing '%s'", cv);
            logger.debug(s);
        }
        return sb.toString();
    }

    /**
     * 
     * @param definitionFileName
     */
    public static void setDefinitionFileName(String definitionFileName) {
        ChordFactoryXMLHelper.definitionFileName = definitionFileName;

    }

    public static void writeChordXML(Chord chord, OutputStream out) {
        logger.debug("enter writeChordXML");
        DocumentBuilderFactory dbf = null;
        DocumentBuilder db = null;
        try {
            dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            // dbf.setSchema()
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return;
        }
        Document doc = (Document) db.newDocument();
        String namespaceURI = "http://www.rockhoppertech.com/music/chords";
        Element root = doc.createElementNS(namespaceURI, "chords");
        doc.appendChild(root);

        // DecimalFormat fmt = new DecimalFormat("#,###,###,###.##");
        Element chordElement = null;
        chordElement = doc.createElement("chord");
        root.appendChild(chordElement);
        chordElement.setAttribute("name", chord.getSymbol());

        Element intervalsElement = doc.createElement("intervals");
        chordElement.appendChild(intervalsElement);
        for (Integer i : chord.getIntervals()) {
            Element intervalElement = doc.createElement("interval");
            intervalElement.appendChild(doc.createTextNode(i.toString()));
            intervalsElement.appendChild(intervalElement);
        }

        Element spelling = doc.createElement("spelling");
        chordElement.appendChild(spelling);
        String s = chord.getSpelling();
        String[] spellings = s.split("\\s");
        for (String degree : spellings) {
            Element degreeEl = doc.createElement("degree");
            degreeEl.appendChild(doc.createTextNode(degree));
            spelling.appendChild(degreeEl);
        }

        String descr = chord.getDescription();
        if (descr != null) {
            Element el = doc.createElement("description");
            chordElement.appendChild(el);
            el.appendChild(doc.createTextNode(descr));
        }

        Element voicing = doc.createElement("voicing");
        chordElement.appendChild(voicing);
        String[] voicingDegrees = chord.getChordVoicing()
                .split("\\s");
        for (String degree : voicingDegrees) {
            Element degreeEl = doc.createElement("degree");
            degreeEl.appendChild(doc.createTextNode(degree));
            voicing.appendChild(degreeEl);
        }

        /*
         * <chord name="maj"> <intervals> <interval>4</interval>
         * <interval>7</interval> </intervals> <spelling> <degree>1</degree>
         * <degree>3</degree> <degree>5</degree> </spelling> <voicing>
         * <degree>1</degree> <degree>3</degree> <degree>5</degree>
         * <degree>+1</degree> </voicing> <description>major</description>
         * </chord>
         */
        doc.getDocumentElement().normalize();

        try {
            DOMSource domSource = new DOMSource(doc);
            StreamResult streamResult = new StreamResult(out);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            Properties oprops = new Properties();
            oprops.put(OutputKeys.INDENT, "yes");
            oprops.put(OutputKeys.METHOD, "xml");
            oprops.put(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperties(oprops);
            transformer.transform(domSource, streamResult);
        } catch (TransformerException e) {
            System.err.println(e);
        }
    }

}
/*
 * 
 * <chord name="maj9-5"> <intervals> <interval>4</interval>
 * <interval>6</interval> </intervals> <spelling> <degree>1</degree>
 * <degree>3</degree> </spelling> <description>major ninth diminished
 * fifth</description> </chord>
 */

/*
 * History:
 * 
 * $Log$
 * 
 * This version: $Revision$ Last modified: $Date$ Last modified by: $Author$
 */
