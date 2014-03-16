package com.rockhoppertech.music.fx.cmn.musicxml;

/*
 * #%L
 * Rocky Music FX
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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.NavigableMap;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.fx.cmn.Measure;
import com.rockhoppertech.music.midi.js.Instrument;
import com.rockhoppertech.music.midi.js.KeySignature;
import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.midi.js.MIDITrackBuilder;
import com.rockhoppertech.music.midi.js.TimeSignature;

/**
 * Uses Stax to emit MusicXML for a MIDITrack.
 * 
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * 
 */
public class TrackToMusicXML {
    private static final Logger logger = LoggerFactory
            .getLogger(TrackToMusicXML.class);

    public static void main(String[] args) {

        MIDITrack track = MIDITrackBuilder.create()
                .name("some track")
                .description("this is the description")
                .noteString("c d Ef cs3 f#5 e f g")
                .durations(1d, 1.5, .5)
                .instrument(Instrument.TRUMPET)
                .sequential()
                .build();
        track.addTimeSignatureAtBeat(1d, new TimeSignature(4, 4));
        track.addKeySignatureAtBeat(1d, KeySignature.EFMAJOR);
        track.play();

        try {
            OutputStream os = new FileOutputStream("testmusicxml.xml");
            emitXML(track, os, "Grand Opus", "Giacomo");
        } catch (IOException | XMLStreamException e) {
            e.printStackTrace();
        }
    }

    /*
     * <?xml version="1.0" encoding="UTF-8" standalone="no"?> <!DOCTYPE
     * score-partwise PUBLIC "-//Recordare//DTD MusicXML 3.0 Partwise//EN"
     * "http://www.musicxml.org/dtds/partwise.dtd"> <score-partwise
     * version="3.0"> <part-list> <score-part id="P1">
     * <part-name>Music</part-name> </score-part> </part-list> <part id="P1">
     * <measure number="1"> <attributes> <divisions>1</divisions> <key>
     * <fifths>0</fifths> </key> <time> <beats>4</beats>
     * <beat-type>4</beat-type> </time> <clef> <sign>G</sign> <line>2</line>
     * </clef> </attributes> <note> <pitch> <step>C</step> <octave>4</octave>
     * </pitch> <duration>4</duration> <type>whole</type> </note> </measure>
     * </part> </score-partwise>
     */

    /*
     * <identification> <miscellaneous> <miscellaneous-field
     * name="description">All pitches from G to c'''' in ascending steps; First
     * without accidentals, then with a sharp and then with a flat accidental.
     * Double alterations and cautionary accidentals are tested at the
     * end.</miscellaneous-field> </miscellaneous> </identification>
     */

    /*
     * <part-list> <score-part id="P1"> <part-name>some track</part-name>
     * <part-name-display> <display-text>some track</display-text>
     * </part-name-display> <score-instrument id="P1-I1"> <instrument-name>
     * </instrument-name> <virtual-instrument> <virtual-library>General
     * MIDI</virtual-library> <virtual-name>Bright Piano</virtual-name>
     * </virtual-instrument> </score-instrument> </score-part> </part-list>
     */

    static String dtd = "<!DOCTYPE score-partwise PUBLIC  \"-//Recordare//DTD MusicXML 3.0 Partwise//EN\"  \"http://www.musicxml.org/dtds/partwise.dtd\">";

    /**
     * Write a track to a stream as MusicXML.
     * 
     * @param track
     *            the {@code MIDITrack}
     * @param out
     *            the output stream to write to
     * @param workTitle
     * @param composer
     * @throws IOException
     *             oops
     * @throws XMLStreamException
     *             oops
     */

    public static void emitXML(MIDITrack track, OutputStream out,
            String workTitle, String composer)
            throws IOException,
            XMLStreamException {

        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();

        XMLEventWriter writer = outputFactory.createXMLEventWriter(out);

        XMLEventFactory eventFactory = XMLEventFactory.newInstance();
        // DTD dtddec = eventFactory.createDTD(dtd);
        // writer.add(eventFactory.createDTD(dtd));
        // writer.add(createNewLine(eventFactory));

        StartDocument startDocument = eventFactory.createStartDocument();
        writer.add(startDocument);

        StartElement scoreElement = eventFactory.createStartElement("",
                "", "score-partwise");
        writer.add(scoreElement);
        writer.add(eventFactory.createAttribute("version", "3.0"));
        writer.add(createNewLine(eventFactory));

        writer.add(eventFactory.createStartElement("", "", "work"));
        writer.add(createNewLine(eventFactory));
        createNode(eventFactory, writer, "work-title", workTitle);
        writer.add(eventFactory.createEndElement("", "", "work"));
        writer.add(createNewLine(eventFactory));

        // Indentification
        writer.add(eventFactory.createStartElement("", "", "identification"));
        writer.add(createNewLine(eventFactory));

        writer.add(eventFactory.createStartElement("", "", "miscellaneous"));
        writer.add(createNewLine(eventFactory));
        createNode(
                eventFactory,
                writer,
                "miscellaneous-field",
                track.getDescription(),
                "name",
                "description");
        writer.add(eventFactory.createEndElement("", "", "miscellaneous"));
        writer.add(createNewLine(eventFactory));

        createNode(eventFactory, writer, "creator", composer,
                "type", "composer");

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date d = new Date();

        writer.add(eventFactory.createStartElement("", "", "encoding"));
        writer.add(createNewLine(eventFactory));
        createNode(eventFactory, writer, "encoding-date", df.format(d));
        createNode(eventFactory, writer, "software", "Rockhopper Music");
        createNode(eventFactory, writer, "encoder", "Gene");
        writer.add(eventFactory.createEndElement("", "", "encoding"));
        writer.add(createNewLine(eventFactory));

        writer.add(eventFactory.createEndElement("", "", "identification"));
        writer.add(createNewLine(eventFactory));
        // / end Indentification

        writer.add(eventFactory.createStartElement("", "", "part-list"));
        writer.add(createNewLine(eventFactory));
        writer.add(eventFactory.createStartElement("", "", "score-part"));
        writer.add(eventFactory.createAttribute("id", "P1"));
        writer.add(createNewLine(eventFactory));
        writer.add(eventFactory.createStartElement("", "", "part-name"));
        writer.add(eventFactory.createCharacters(track.getName()));
        writer.add(eventFactory.createEndElement("", "", "part-name"));
        writer.add(createNewLine(eventFactory));

        // <part-name-display>
        // <display-text>some track</display-text>
        // </part-name-display>
        writer.add(eventFactory.createStartElement("", "", "part-name-display"));
        writer.add(createNewLine(eventFactory));
        createNode(eventFactory, writer, "display-text", track.getName());
        writer.add(eventFactory.createEndElement("", "", "part-name-display"));
        writer.add(createNewLine(eventFactory));

        // <score-instrument id="P1-I1">
        // <instrument-name> </instrument-name>
        // <virtual-instrument>
        // <virtual-library>General MIDI</virtual-library>
        // <virtual-name>Bright Piano</virtual-name>
        // </virtual-instrument>
        // </score-instrument>
        writer.add(eventFactory.createStartElement("", "", "score-instrument"));
        writer.add(eventFactory.createAttribute("id", "P1-I1"));
        writer.add(createNewLine(eventFactory));
        createNode(eventFactory, writer, "instrument-name", track
                .getInstrument().getName());
        writer.add(createNewLine(eventFactory));

        writer.add(eventFactory
                .createStartElement("", "", "virtual-instrument"));
        writer.add(createNewLine(eventFactory));
        createNode(eventFactory, writer, "virtual-library", "General MIDI");
        createNode(eventFactory, writer, "virtual-name", track.getInstrument()
                .getName());
        writer.add(eventFactory.createEndElement("", "", "virtual-instrument"));
        writer.add(createNewLine(eventFactory));

        writer.add(eventFactory.createEndElement("", "", "score-instrument"));
        writer.add(createNewLine(eventFactory));
        //

        writer.add(eventFactory.createEndElement("", "", "score-part"));
        writer.add(createNewLine(eventFactory));
        writer.add(eventFactory.createEndElement("", "", "part-list"));
        writer.add(createNewLine(eventFactory));

        writer.add(eventFactory.createStartElement("", "", "part"));
        writer.add(eventFactory.createAttribute("id", "P1"));
        writer.add(createNewLine(eventFactory));

        int divisions = 64;
        NavigableMap<Double, Measure> measures = Measure.createMeasures(track);
        int measureNumber = 1;
        System.out.println("n mesures: " + measures.size());
        KeySignature ks = track.getKeySignatureAtBeat(1d);
        TimeSignature ts = track.getTimeSignatureAtBeat(1d);

        for (Measure m : measures.values()) {
            logger.debug("measure {}", m);

            writer.add(eventFactory.createStartElement("", "", "measure"));
            writer.add(eventFactory.createAttribute("number", ""
                    + measureNumber++));
            writer.add(createNewLine(eventFactory));
            writer.add(eventFactory.createStartElement("", "", "attributes"));

            createNode(eventFactory, writer, "divisions", "" + divisions);

            // TODO add the ks and ts only if different from the previous ks or
            // ts
            // KeySignature mks = m.getKeySignatureAtBeat(1d);
            // TimeSignature mts = m.getTimeSignature();
            KeySignature mks = null;
            TimeSignature mts = null;

            // first time through (it's already been incremented)
            if (measureNumber == 2) {
                // always add the ts and ks
                mks = ks;
                mts = ts;
                if (mks != null) {
                    writer.add(eventFactory.createStartElement("", "", "key"));
                    createNode(eventFactory, writer, "fifths", "" + mks.getSf());
                    writer.add(eventFactory.createEndElement("", "", "key"));
                    writer.add(createNewLine(eventFactory));
                }

                if (mts != null) {
                    writer.add(eventFactory.createStartElement("", "", "time"));
                    createNode(
                            eventFactory,
                            writer,
                            "beats",
                            "" + mts.getNumerator());
                    createNode(
                            eventFactory,
                            writer,
                            "beat-type",
                            "" + mts.getDenominator());
                    writer.add(eventFactory.createEndElement("", "", "time"));
                    writer.add(createNewLine(eventFactory));
                }

            } else {
                mks = m.getKeySignatureAtBeat(m.getStartBeat());
                mts = m.getTimeSignature();

                logger.debug("mks is {}", mks);
                // if the current ks is different from the previous ks, add the
                // new one
                if (mks != null) {
                    if (!mks.equals(ks)) {
                        writer.add(eventFactory.createStartElement(
                                "",
                                "",
                                "key"));
                        createNode(
                                eventFactory,
                                writer,
                                "fifths",
                                "" + mks.getSf());
                        writer.add(eventFactory.createEndElement("", "", "key"));
                        writer.add(createNewLine(eventFactory));
                    }
                }

                // if the current ts is different from the previous ts, add the
                // new one
                if (mts != null) {
                    if (!mts.equals(ts)) {
                        writer.add(eventFactory.createStartElement(
                                "",
                                "",
                                "time"));
                        createNode(
                                eventFactory,
                                writer,
                                "beats",
                                "" + mts.getNumerator());
                        createNode(
                                eventFactory,
                                writer,
                                "beat-type",
                                "" + mts.getDenominator());
                        writer.add(eventFactory
                                .createEndElement("", "", "time"));
                        writer.add(createNewLine(eventFactory));
                    }
                }
            }

            // <clef> <sign>G</sign> <line>2</line> </clef>

            writer.add(eventFactory.createEndElement("", "", "attributes"));
            writer.add(createNewLine(eventFactory));

            for (MIDINote n : m.getMIDITrack()) {
                writer.add(eventFactory.createStartElement("", "", "note"));
                writer.add(createNewLine(eventFactory));

                writer.add(eventFactory.createStartElement("", "", "pitch"));
                writer.add(createNewLine(eventFactory));

                String ps = n.getPitch().getPreferredSpelling();
                // preferred spelling includes the octave. get rid of it
                ps = ps.substring(0, ps.length() - 1);

                String step = ps.substring(0, 1).toUpperCase(Locale.ENGLISH);
                createNode(eventFactory, writer, "step", step);

                if (ps.endsWith("b")) {
                    createNode(eventFactory, writer, "alter", "-1");
                }
                // f can mean flat or pitch step
                if (ps.endsWith("f") && ps.length() > 1) {
                    createNode(eventFactory, writer, "alter", "-1");
                }
                if (ps.endsWith("s") || ps.endsWith("#")) {
                    createNode(eventFactory, writer, "alter", "1");
                }

                // bloody musicxml makes middle c in oct 4.
                int oct = (n.getMidiNumber() - 12) / 12;
                createNode(eventFactory, writer, "octave", "" + oct);
                writer.add(eventFactory.createEndElement("", "", "pitch"));
                writer.add(createNewLine(eventFactory));

                writer.add(eventFactory.createStartElement("", "", "duration"));
                writer.add(eventFactory.createCharacters("" + divisions
                        * n.getDuration()));
                writer.add(eventFactory.createEndElement("", "", "duration"));
                writer.add(createNewLine(eventFactory));

                writer.add(eventFactory.createEndElement("", "", "note"));
                writer.add(createNewLine(eventFactory));
                writer.add(createNewLine(eventFactory));
            }

            writer.add(eventFactory.createEndElement("", "", "measure"));
            writer.add(createNewLine(eventFactory));
            writer.add(createNewLine(eventFactory));
        }

        writer.add(eventFactory.createEndElement("", "", "part"));

        writer.add(eventFactory.createEndElement("", "", "score-partwise"));
        writer.add(createNewLine(eventFactory));
        writer.add(eventFactory.createEndDocument());

        writer.flush();
        writer.close();
    }

    public static XMLEvent createNewLine(XMLEventFactory eventFactory) {
        return eventFactory.createDTD("\n");
    }

    public static XMLEvent createTab(XMLEventFactory eventFactory) {
        return eventFactory.createDTD("\t");
    }

    private static void createNode(XMLEventFactory eventFactory,
            XMLEventWriter writer, String name,
            String value) throws XMLStreamException {

        StartElement sElement = eventFactory.createStartElement("", "", name);

        writer.add(createTab(eventFactory));
        writer.add(sElement);

        Characters characters = eventFactory.createCharacters(value);
        writer.add(characters);

        EndElement eElement = eventFactory.createEndElement("", "", name);
        writer.add(eElement);
        writer.add(createNewLine(eventFactory));
    }

    private static void createNode(XMLEventFactory eventFactory,
            XMLEventWriter writer, String name,
            String value, String aName, String aValue)
            throws XMLStreamException {

        StartElement sElement = eventFactory.createStartElement("", "", name);

        writer.add(createTab(eventFactory));
        writer.add(sElement);

        writer.add(eventFactory.createAttribute(aName, aValue));

        Characters characters = eventFactory.createCharacters(value);
        writer.add(characters);

        EndElement eElement = eventFactory.createEndElement("", "", name);
        writer.add(eElement);
        writer.add(createNewLine(eventFactory));
    }

}
