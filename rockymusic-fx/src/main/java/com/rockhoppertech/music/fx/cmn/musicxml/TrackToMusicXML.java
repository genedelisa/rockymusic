package com.rockhoppertech.music.fx.cmn.musicxml;

import java.io.FileOutputStream;
import java.io.IOException;
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

import com.rockhoppertech.music.fx.cmn.Measure;
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

    public static void main(String[] args) {

        MIDITrack track = MIDITrackBuilder.create()
                .name("some track")
                .noteString("c d Ef cs3 f#5 e f g")
                .durations(1d)
                .sequential()
                .build();
        track.addTimeSignatureAtBeat(1d, new TimeSignature(4, 4));
        track.addKeySignatureAtBeat(1d, KeySignature.EFMAJOR);

        try {
            emitXML(track);
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

    static String dtd = "<!DOCTYPE score-partwise PUBLIC  \"-//Recordare//DTD MusicXML 3.0 Partwise//EN\"  \"http://www.musicxml.org/dtds/partwise.dtd\">";

    public static void emitXML(MIDITrack track) throws IOException,
            XMLStreamException {

        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();

        XMLEventWriter writer = outputFactory
                .createXMLEventWriter(new FileOutputStream("testmusicxml.xml"));

        // XMLEventWriter writer = outputFactory
        // .createXMLEventWriter(System.out);

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
        createNode(eventFactory, writer, "work-title", "Title");
        writer.add(eventFactory.createEndElement("", "", "work"));
        writer.add(createNewLine(eventFactory));

        writer.add(eventFactory.createStartElement("", "", "identification"));
        writer.add(createNewLine(eventFactory));

        //TODO make this configurable
        createNode(eventFactory, writer, "creator", "Alban Berg",
                "type", "composer");

        writer.add(eventFactory.createStartElement("", "", "encoding"));
        createNode(eventFactory, writer, "software", "Rockhopper Music");
        createNode(eventFactory, writer, "encoder", "Gene");
        writer.add(eventFactory.createEndElement("", "", "encoding"));
        writer.add(createNewLine(eventFactory));

        writer.add(eventFactory.createEndElement("", "", "identification"));
        writer.add(createNewLine(eventFactory));

        writer.add(eventFactory.createStartElement("", "", "part-list"));
        writer.add(createNewLine(eventFactory));
        writer.add(eventFactory.createStartElement("", "", "score-part"));
        writer.add(eventFactory.createAttribute("id", "P1"));
        writer.add(eventFactory.createStartElement("", "", "part-name"));
        writer.add(eventFactory.createCharacters(track.getName()));
        writer.add(eventFactory.createEndElement("", "", "part-name"));
        writer.add(createNewLine(eventFactory));
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

        for (Measure m : measures.values()) {

            writer.add(eventFactory.createStartElement("", "", "measure"));
            writer.add(eventFactory.createAttribute("number", ""
                    + measureNumber++));
            writer.add(createNewLine(eventFactory));
            writer.add(eventFactory.createStartElement("", "", "attributes"));

            createNode(eventFactory, writer, "divisions", "" + divisions);

            KeySignature mks = m.getKeySignatureAtBeat(1d);
            if (mks == null) {
                mks = ks;
            }
            TimeSignature ts = m.getTimeSignature();

            writer.add(eventFactory.createStartElement("", "", "key"));
            createNode(eventFactory, writer, "fifths", "" + mks.getSf());

            writer.add(eventFactory.createEndElement("", "", "key"));
            writer.add(createNewLine(eventFactory));

            writer.add(eventFactory.createStartElement("", "", "time"));

            createNode(eventFactory, writer, "beats", "" + ts.getNumerator());
            createNode(
                    eventFactory,
                    writer,
                    "beat-type",
                    "" + ts.getDenominator());

            writer.add(eventFactory.createEndElement("", "", "time"));
            writer.add(createNewLine(eventFactory));

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

                if (ps.endsWith("f") || ps.endsWith("b")) {
                    createNode(eventFactory, writer, "alter", "-1");
                }
                if (ps.endsWith("s") || ps.endsWith("#")) {
                    createNode(eventFactory, writer, "alter", "1");
                }

                createNode(eventFactory, writer, "step", step);
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
