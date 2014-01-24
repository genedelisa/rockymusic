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
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.util.NavigableMap;
import java.util.Properties;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
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

import com.rockhoppertech.music.PitchFormat;
import com.rockhoppertech.music.midi.js.KeySignature;
import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.midi.js.TimeSignature;

/**
 * MIDITracks to and from XML.
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * @see MIDITrackTransferHandler
 * @see com.rockhoppertech.music.midi.js.MIDITrack
 */
public class MIDITrackXMLHelper {
    private static final Logger logger = LoggerFactory
            .getLogger(MIDITrackXMLHelper.class);
    /**
     * Emit only the required elements.
     */
    static boolean onlyRequired = true;

    private static void complain(final String string) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JOptionPane.showMessageDialog(null,
                                              string);
            }
        });
    }

    private static DocumentBuilder getDocumentBuilder() {
        // these are jaxp classes
        DocumentBuilder db = null;
        try {
            final DocumentBuilderFactory dbf = DocumentBuilderFactory
                    .newInstance();
            dbf.setCoalescing(true);
            dbf.setExpandEntityReferences(true);
            dbf.setIgnoringComments(false);
            dbf.setIgnoringElementContentWhitespace(true);
            dbf.setNamespaceAware(true);
            dbf.setValidating(false);
            db = dbf.newDocumentBuilder();
            // db.setEntityResolver(EntityResolver er);
            // db.setErrorHandler(ErrorHandler eh);
        } catch (final ParserConfigurationException e) {
            System.err.println(e);
            e.printStackTrace();
            return null;
        }
        return db;
    }

    public static Element getDocumentElementFromFile(final String filename) {
        final DocumentBuilder db = MIDITrackXMLHelper.getDocumentBuilder();
        Document doc = null;
        try {
            final File f = new File(filename);
            final String uri = f.toURI().toString();
            final InputSource is = new InputSource(new FileReader(f));
            // if not set, then it won't show up in exceptions
            is.setSystemId(uri);
            doc = db.parse(is);
            // doc.normalize();
        } catch (final SAXParseException spe) {
            System.err.println(spe);
            final Exception x = spe.getException();
            if (x != null) {
                System.err.println(x);
            }
            MIDITrackXMLHelper.complain(spe.getMessage());
        } catch (final SAXException sxe) {
            Exception x = sxe;
            if (sxe.getException() != null) {
                x = sxe.getException();
                logger.debug(x.getLocalizedMessage());
            }
            MIDITrackXMLHelper.complain(sxe.getMessage());
        } catch (final IOException ioe) {
            ioe.printStackTrace();
            MIDITrackXMLHelper.complain(ioe.getMessage());
        } catch (final Throwable t) {
            t.printStackTrace();
            MIDITrackXMLHelper.complain(t.getMessage());
        }
        return doc.getDocumentElement();
    }

    public static Element getDocumentElementFromString(final String string) {
        final DocumentBuilder db = MIDITrackXMLHelper.getDocumentBuilder();
        Document doc = null;
        try {
            final InputSource is = new InputSource(new StringReader(string));
            doc = db.parse(is);
            // doc.normalize();
        } catch (final SAXParseException spe) {
            final StringBuilder sb = new StringBuilder();
            if (spe.getMessage() != null) {
                sb.append(spe.getMessage()).append('\n');
            }
            logger.error(spe.getMessage(),
                         spe);
            final Exception x = spe.getException();
            if (x != null) {
                logger.error(x.getMessage(),
                             x);
                if (x.getMessage() != null) {
                    sb.append(x.getMessage()).append('\n');
                }
            }
            final String s = sb.toString();
            if ((s != null) && (s.length() > 0)) {
                MIDITrackXMLHelper.complain(s);
            }
        } catch (final SAXException sxe) {
            final StringBuilder sb = new StringBuilder();
            sb.append(sxe.getMessage()).append('\n');
            Exception x = sxe;
            if (sxe.getException() != null) {
                x = sxe.getException();
                logger.debug(x.getLocalizedMessage());
                sb.append(x.getMessage()).append('\n');
            }
            MIDITrackXMLHelper.complain(sb.toString());
        } catch (final IOException ioe) {
            ioe.printStackTrace();
            MIDITrackXMLHelper.complain(ioe.getMessage());
        } catch (final Throwable t) {
            t.printStackTrace();
            MIDITrackXMLHelper.complain(t.getMessage());
        }

        final Element e = doc.getDocumentElement();
        return e;
    }

    /**
     * @return the onlyRequired
     */
    public static boolean isOnlyRequired() {
        return onlyRequired;
    }

    /**
     * Turn a (@link com.rockhoppertech.music.midi.js.MIDITrack) into an XML
     * string.
     * 
     * @param notelist
     * @return
     * @see com.rockhoppertech.music.dnd.MIDITrackTransferHandler.getTransferData
     *      ()
     */
    public static String MIDITrackToString(final MIDITrack notelist) {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        MIDITrackXMLHelper.writeXML(notelist,
                                       os);
        return os.toString();
    }

    /**
     * Parses the content of the node and stores it in the notelist.
     * 
     * @param notelist
     * @param node
     */
    public static MIDITrack parseMIDITrack(final Node node) {
        if (node == null) {
            logger.error("parseNoteList: node is null");
            throw new IllegalArgumentException("Node is null");
        }
        MIDITrack notelist = new MIDITrack();
        if (node.getNodeName().equals("MIDITrack") == false) {
            logger.error("parseNoteList: node is not a MIDITrack");
            throw new IllegalArgumentException("This is not a MIDITrack: "
                    + node.getNodeName());
        }

        final NamedNodeMap attributes = node.getAttributes();
        final String name = attributes.getNamedItem("name").getNodeValue();
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("name attribute is %s",
                                       name));
        }
        notelist.setName(name);

        final int pc = 0;
        int bend = 0;
        double startBeat = 0;
        String pitch = "";
        int midinumber = 0;
        double duration = 0;
        double endBeat = 0;
        int channel = 0;
        int velocity = 0;
        int program = 0;
        final int pitchbend = 0;

        if (node.hasChildNodes()) {
            final NodeList sc = node.getChildNodes();
            final int len = sc.getLength();
            for (int i = 0; i < len; i++) {
                final Node nn = sc.item(i);
                if (nn.getNodeName().equalsIgnoreCase("description")) {
                    final String desc = nn.getTextContent();
                    notelist.setDescription(desc);
                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("description is '%s'",
                                                   desc));
                    }
                } else if (nn.getNodeName().equalsIgnoreCase("keysignature")) {
                    final NamedNodeMap pa = nn.getAttributes();
                    final Node ni = pa.getNamedItem("beat");
                    final double beat = Double.parseDouble(ni.getNodeValue());
                    final String key = nn.getTextContent();
                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("key is '%s'",
                                                   key));
                    }
                    notelist.addKeySignatureAtBeat(beat,
                                                   KeySignature.getByName(key));
                } else if (nn.getNodeName().equalsIgnoreCase("timesignature")) {
                    final NamedNodeMap pa = nn.getAttributes();
                    final Node ni = pa.getNamedItem("beat");
                    final double beat = Double.parseDouble(ni.getNodeValue());
                    final String ts = nn.getTextContent();
                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("ts is '%s'",
                                                   ts));
                    }
                    notelist.addTimeSignatureAtBeat(beat,
                                                    new TimeSignature(ts));
                } else if (nn.getNodeName().equalsIgnoreCase("MIDINote")) {
                    final NamedNodeMap pa = nn.getAttributes();

                    Node ni = pa.getNamedItem("startBeat");
                    if (ni == null) {
                        System.err
                                .println("Error. must have startBeat attribute");
                        continue;
                    }
                    startBeat = Double.parseDouble(ni.getNodeValue());

                    ni = pa.getNamedItem("duration");
                    if (ni == null) {
                        System.err
                                .println("Error. must have duration attribute");
                        continue;
                    }
                    duration = Double.parseDouble(ni.getNodeValue());

                    String s = null;
                    // not optional
                    ni = pa.getNamedItem("pitch");
                    if (ni == null) {
                        System.err.println("Error. must have pitch attribute");
                        continue;
                    }
                    pitch = ni.getNodeValue();

                    // optional
                    ni = pa.getNamedItem("channel");
                    if (ni != null) {
                        s = ni.getNodeValue();
                        channel = Integer.parseInt(s);
                    }

                    // optional
                    ni = pa.getNamedItem("velocity");
                    if (ni != null) {
                        velocity = Integer.parseInt(ni.getNodeValue());
                    }

                    // optional
                    ni = pa.getNamedItem("program");
                    if (ni != null) {
                        program = Integer.parseInt(ni.getNodeValue());
                    }

                    // optional
                    ni = pa.getNamedItem("midiNumber");
                    if (ni != null) {
                        midinumber = Integer.parseInt(ni.getNodeValue());
                    }

                    // optional
                    ni = pa.getNamedItem("pitchBend");
                    if (ni != null) {
                        bend = Integer.parseInt(ni.getNodeValue());
                    }

                    // optional
                    ni = pa.getNamedItem("endBeat");
                    if (ni != null) {
                        endBeat = Double.parseDouble(ni.getNodeValue());
                    }

                    final MIDINote note = new MIDINote(pitch, startBeat,
                            duration, channel, velocity, program, bend);
                    notelist.add(note);
                    // log.debug("XML added note " + note);
                } // if note
            } // for
        } else { // if has children
            logger.debug("notelist has no notes!");
            MIDITrackXMLHelper
                    .complain("What good is a notelist with no notes?");
        }
        return notelist;
    }

    public static MIDITrack readMIDITrackFromXMLString(final String string) {
        final Element e = MIDITrackXMLHelper
                .getDocumentElementFromString(string);
        final String root = e.getNodeName();
        if (root.equals("MIDITrack") == false) {
            throw new IllegalArgumentException("That is not a MIDITrack: "
                    + root);
        }
        final String name = e.getTagName();
        logger.debug("parsing notelist document element " + name);
        MIDITrack notelist = MIDITrackXMLHelper.parseMIDITrack(e);
        return notelist;
    }

    public static MIDITrack readMIDITrackXMLFromFile(final String filename) {
        final Element e = MIDITrackXMLHelper
                .getDocumentElementFromFile(filename);
        final String root = e.getNodeName();
        if (root.equals("MIDITrack") == false) {
            throw new IllegalArgumentException("That is not a MIDITrack: "
                    + root);
        }
        final String name = e.getTagName();
        logger.debug("parsing notelist document element " + name);
        MIDITrack notelist = MIDITrackXMLHelper.parseMIDITrack(e);
        return notelist;
    }

    /**
     * @param onlyRequired
     *            the onlyRequired to set
     */
    public static void setOnlyRequired(final boolean onlyRequired) {
        MIDITrackXMLHelper.onlyRequired = onlyRequired;
    }

    public static Document writeXML(final MIDITrack list) {
        return MIDITrackXMLHelper.writeXML(list,
                                       System.out);
    }

    /**
     * Write the notelist in Rockhopper format.
     * 
     * @param list
     * @param os
     */
    public static Document writeXML(final MIDITrack list, final OutputStream os) {

        final Document doc = createDocument(list);

        try {
            final DOMSource domSource = new DOMSource(doc);
            final StreamResult streamResult = new StreamResult(os);
            final TransformerFactory tf = TransformerFactory.newInstance();
            final Transformer transformer = tf.newTransformer();
            final Properties oprops = new Properties();
            oprops.put(OutputKeys.INDENT,
                       "yes");
            oprops.put(OutputKeys.METHOD,
                       "xml");
            oprops.put(OutputKeys.OMIT_XML_DECLARATION,
                       "no");
            // oprops.put(OutputKeys.STANDALONE, "no");

            // oprops.put(OutputKeys.DOCTYPE_PUBLIC,
            // "-//Rockhopper Technologies//MIDINotelist 1.0//EN");
            // oprops.put(OutputKeys.DOCTYPE_SYSTEM,
            // "http://www.rockhoppertech.com/xml/midinotelist.dtd");
            transformer.setOutputProperties(oprops);
            transformer.transform(domSource,
                                  streamResult);
        } catch (final TransformerException e) {
            System.err.println(e);
        }
        return doc;
    }
    
    public static Document createDocument(final MIDITrack list) {
        DocumentBuilderFactory dbf = null;
        DocumentBuilder db = null;
        try {
            dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            // dbf.setSchema()
            db = dbf.newDocumentBuilder();
        } catch (final ParserConfigurationException e) {
            e.printStackTrace();
            System.exit(1);
        }
        final Document doc = db.newDocument();
        final Element root = doc
                .createElementNS("http://www.rockhoppertech.com/music/MIDITrack",
                                 "MIDITrack");
        doc.appendChild(root);
        root.setAttribute("name",
                          list.getName());

        final String d = list.getDescription();
        if (d != null) {
            final Element desc = doc.createElement("description");
            desc.appendChild(doc.createTextNode(d));
            root.appendChild(desc);
        }

        final NavigableMap<Double, KeySignature> ks = list.getKeySignatures();
        for (final Double t : ks.keySet()) {
            final KeySignature k = ks.get(t);
            final Element e = doc.createElement("keysignature");
            e.setAttribute("beat",
                           "" + t);
            e.appendChild(doc.createTextNode(k.getDisplayName()));
            root.appendChild(e);
        }
        final NavigableMap<Double, TimeSignature> ts = list.getTimeSignatures();
        for (final Double t : ts.keySet()) {
            final Element e = doc.createElement("timesignature");
            e.setAttribute("beat",
                           "" + t);
            e.appendChild(doc.createTextNode(ts.get(t).getDisplayName()));
            root.appendChild(e);
        }

        final DecimalFormat fmt = new DecimalFormat("#,###,###,###.##");

        Element event = null;
        for (final MIDINote note : list) {
            event = doc.createElement("MIDINote");
            event.setAttribute("startBeat",
                               "" + fmt.format(note.getStartBeat()));
            event.setAttribute("pitch",
                               PitchFormat.getInstance()
                                       .format(note.getPitch()));
            event.setAttribute("midiNumber",
                               "" + note.getMidiNumber());
            event.setAttribute("duration",
                               "" + fmt.format(note.getDuration()));
            event.setAttribute("endBeat",
                               "" + fmt.format(note.getEndBeat()));
            event.setAttribute("channel",
                               "" + note.getChannel());
            event.setAttribute("velocity",
                               "" + note.getVelocity());
            event.setAttribute("bank",
                               "" + note.getBank());
            event.setAttribute("program",
                               "" + note.getProgram());
            event.setAttribute("pitchBend",
                               "" + note.getPitchBend());
            event.setAttribute("pan",
                               "" + note.getPan());
            root.appendChild(event);
        }

        doc.getDocumentElement().normalize();
        return doc;
    }
}