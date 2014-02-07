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

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.FluentIterable;
import com.rockhoppertech.music.scale.Scale;
import com.rockhoppertech.music.scale.ScaleFactory;

/**
 * Reads the modes XML definition file, then creates a collection of Scales
 * based on those definitions.
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class ModeFactoryXMLHelper {
    static Logger logger = LoggerFactory.getLogger(ModeFactoryXMLHelper.class);

    public static void main(String[] args) {
        Scale scale = ScaleFactory.createFromName("Peruvian tritonic 2");
        logger.debug("mode {}", scale);
        write(modeList, "foomodes.xml");
    }

    static {
        init();
    }

    static List<Scale> modeList;

    /**
     * Read modes.xml and create {@code Scale instances} from those definitions.
     */
    public static void init() {
        List<Integer> intervals = null;
        Scale currentMode = null;
        String tagContent = null;
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = null;
        try {
            reader = factory.createXMLStreamReader(
                    ClassLoader.getSystemResourceAsStream("modes.xml"));
        } catch (XMLStreamException e) {
            e.printStackTrace();
            return;
        }

        try {
            while (reader.hasNext()) {
                int event = reader.next();

                switch (event) {
                case XMLStreamConstants.START_ELEMENT:
                    String el = reader.getLocalName();
                    logger.debug("start element '{}'", el);
                    if ("mode".equals(el)) {
                        currentMode = new Scale();
                        intervals = new ArrayList<>();
                    }
                    if ("modes".equals(el)) {
                        modeList = new ArrayList<>();
                    }
                    break;

                case XMLStreamConstants.CHARACTERS:
                    tagContent = reader.getText().trim();
                    logger.debug("tagcontent '{}'", tagContent);
                    break;

                case XMLStreamConstants.END_ELEMENT:
                    switch (reader.getLocalName()) {
                    case "mode":
                        // wow. both guava and commmons to get an int[] array
                        Integer[] array = FluentIterable
                                .from(intervals)
                                .toArray(Integer.class);
                        // same as Integer[] array = intervals.toArray(new
                        // Integer[intervals.size()]);
                        int[] a = ArrayUtils.toPrimitive(array);
                        currentMode.setIntervals(a);
                        modeList.add(currentMode);
                        ScaleFactory.registerScale(currentMode);
                        break;
                    case "interval":
                        logger.debug("interval '{}'", tagContent);
                        logger.debug("intervals '{}'", intervals);
                        intervals.add(Integer.parseInt(tagContent));
                        break;
                    case "name":
                        currentMode.setName(tagContent);
                        break;
                    }
                    break;

                case XMLStreamConstants.START_DOCUMENT:
                    modeList = new ArrayList<>();
                    intervals = new ArrayList<>();
                    break;
                }
            }
        } catch (XMLStreamException e) {
            logger.error(e.getLocalizedMessage(), e);
            e.printStackTrace();
        }

        logger.debug("mode list \n{}", modeList);
    }

    public static void write(List<Scale> modeList, String fileName) {
        XMLOutputFactory xof = XMLOutputFactory.newInstance();
        XMLStreamWriter xtw = null;

        // <mode>
        // <name>Peruvian tritonic 2</name>
        // <interval>3</interval>
        // <interval>4</interval>
        // <interval>5</interval>
        // </mode>

        String ns = "http://rockhoppertech.com/modes-1.0";

        // StringWriter sw = new StringWriter();
        try {
            xtw = xof.createXMLStreamWriter(new FileWriter(fileName));
            // xtw = xof.createXMLStreamWriter(sw);
            xtw.writeComment("all elements here are explicitly in the mode namespace");
            xtw.writeStartDocument("utf-8", "1.0");
            xtw.setPrefix("modes", ns);
            xtw.writeStartElement(ns, "modes");
            xtw.writeNamespace("modes", ns);

            for (Scale scale : modeList) {
                // xtw.writeStartElement(ns, "mode");
                xtw.writeStartElement("mode");

                // xtw.writeStartElement(ns, "name");
                xtw.writeStartElement("name");
                xtw.writeCharacters(scale.getName());
                xtw.writeEndElement();

                // xtw.writeStartElement(ns, "intervals");
                // xtw.writeStartElement(ns, "interval");
                xtw.writeStartElement("intervals");
                int[] intervals = scale.getIntervals();
                for (int i = 0; i < intervals.length; i++) {
                    xtw.writeStartElement("interval");
                    xtw.writeCharacters("" + intervals[i]);
                    xtw.writeEndElement();
                }
                xtw.writeEndElement(); // intervals

                xtw.writeEndElement(); // mode
            }

            xtw.writeEndElement(); // modes
            xtw.writeEndDocument();
            xtw.flush();
            xtw.close();
            // System.err.println(sw.toString());

        } catch (XMLStreamException | IOException e) {
            logger.error(e.getLocalizedMessage(), e);
            e.printStackTrace();
        }

    }
}
