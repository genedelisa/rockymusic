/**
 * 
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
import java.io.PrintStream;
import java.net.URL;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.chord.Chord;
import com.rockhoppertech.music.chord.ChordFactory;
import com.rockhoppertech.music.chord.ChordProgression;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class ChordFactoryXMLHelperTest {
    private static final Logger logger = LoggerFactory
            .getLogger(ChordFactoryXMLHelperTest.class);

    

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.xml.ChordFactoryXMLHelper#toXMLString(com.rockhoppertech.music.chord.ChordProgression)}
     * .
     */
    @Test
    public void testToXMLString() {
        ChordProgression chordProgression = new ChordProgression();
        Chord chord = ChordFactory.getChordByFullSymbol("Cmaj7");
        chordProgression.add(chord);
        String actual = ChordFactoryXMLHelper.toXMLString(chordProgression);

        logger.debug("action {}", actual);
        assertThat("The actual value is not null.",
                actual, is(notNullValue()));
        // <chordprogression
        // xmlns="http://www.rockhoppertech.com/music/chordprogression">
        // <chord displayName="Cmaj7">
        // <octave>0</octave>
        // <startBeat>1.0</startBeat>
        // <duration>4.0</duration>
        // <voicing>
        // <degree>1</degree>
        // <degree>3</degree>
        // <degree>5</degree>
        // <degree>7</degree>
        // </voicing>
        // </chord>
        // </chordprogression>
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.xml.ChordFactoryXMLHelper#chordToXMLString(com.rockhoppertech.music.chord.Chord)}
     * .
     */
    @Test
    public void testChordToXMLString() {
        Chord chord = ChordFactory.getChordByFullSymbol("Cmaj7");
        String actual = ChordFactoryXMLHelper.chordToXMLString(chord);
        logger.debug("action {}", actual);
        assertThat("The actual value is not null.",
                actual, is(notNullValue()));
        // <chords xmlns="http://www.rockhoppertech.com/music/chords">
        // <chord name="maj7">
        // <intervals>
        // <interval>4</interval>
        // <interval>7</interval>
        // <interval>11</interval>
        // </intervals>
        // <spelling>
        // <degree>1</degree>
        // <degree>3</degree>
        // <degree>5</degree>
        // <degree>b8</degree>
        // </spelling>
        // <description>major seventh</description>
        // <voicing>
        // <degree>1</degree>
        // <degree>3</degree>
        // <degree>5</degree>
        // <degree>7</degree>
        // </voicing>
        // </chord>
        // </chords>
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.xml.ChordFactoryXMLHelper#writeChordProgressionXML(com.rockhoppertech.music.chord.ChordProgression, java.io.OutputStream)}
     * .
     */
    @Test
    public void testWriteChordProgressionXML() {
        ChordProgression chordProgression = new ChordProgression();
        Chord chord = ChordFactory.getChordByFullSymbol("Cmaj7");
        chordProgression.add(chord);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        ChordFactoryXMLHelper.writeChordProgressionXML(chordProgression, ps);
        String actual = baos.toString();
        logger.debug("action {}", actual);
        assertThat("The actual value is not null.",
                actual, is(notNullValue()));
        // <chordprogression
        // xmlns="http://www.rockhoppertech.com/music/chordprogression">
        // <chord displayName="Cmaj7">
        // <octave>0</octave>
        // <startBeat>1.0</startBeat>
        // <duration>4.0</duration>
        // <voicing>
        // <degree>1</degree>
        // <degree>3</degree>
        // <degree>5</degree>
        // <degree>7</degree>
        // </voicing>
        // </chord>
        // </chordprogression>
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.xml.ChordFactoryXMLHelper#xmlToChordProgression(java.lang.String)}
     * .
     */
    @Test
    public void testXmlToChordProgression() {
        StringBuilder sb = new StringBuilder();
        sb.append("<chordprogression xmlns=\"http://www.rockhoppertech.com/music/chordprogression\">");
        sb.append("<chord displayName=\"Cmaj7\">");
        sb.append("<octave>0</octave>");
        sb.append("<startBeat>1.0</startBeat>");
        sb.append("<duration>4.0</duration>");
        sb.append("<voicing>");
        sb.append("<degree>1</degree>");
        sb.append("<degree>3</degree>");
        sb.append("<degree>5</degree>");
        sb.append("<degree>7</degree>");
        sb.append("</voicing>");
        sb.append("</chord>");
        sb.append("</chordprogression>");
        ChordProgression actual = ChordFactoryXMLHelper
                .xmlToChordProgression(sb.toString());
        logger.debug("action {}", actual);
        assertThat("The actual value is not null.",
                actual, is(notNullValue()));
        assertThat("string content is correct",
                actual.toString(), equalTo("Cmaj7 / / / |"));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.xml.ChordFactoryXMLHelper#writeChordXML(java.io.OutputStream)}
     * .
     */
    @Test
    public void testWriteChordXMLOutputStream() {
        // this writes all the currently defined chords to the stream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        ChordFactoryXMLHelper.writeChordXML(ps);

        String actual = baos.toString();
        logger.debug("action {}", actual);
        assertThat("The actual value is not null.",
                actual, is(notNullValue()));

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.xml.ChordFactoryXMLHelper#xmlToChord(java.lang.String)}
     * .
     */
    @Test
    public void testXmlToChord() {
        StringBuilder sb = new StringBuilder();
        sb.append("<chords xmlns=\"http://www.rockhoppertech.com/music/chords\">");
        sb.append("<chord name=\"maj7\">");
        sb.append("<intervals>");
        sb.append("<interval>4</interval>");
        sb.append("<interval>7</interval>");
        sb.append("<interval>11</interval>");
        sb.append("</intervals>");
        sb.append("<spelling>");
        sb.append("<degree>1</degree>");
        sb.append("<degree>3</degree>");
        sb.append("<degree>5</degree>");
        sb.append("<degree>b8</degree>");
        sb.append("</spelling>");
        sb.append("<description>major seventh</description>");
        sb.append("<voicing>");
        sb.append("<degree>1</degree>");
        sb.append("<degree>3</degree>");
        sb.append("<degree>5</degree>");
        sb.append("<degree>7</degree>");
        sb.append("</voicing>");
        sb.append("</chord>");
        sb.append("</chords>");

        Chord actual = ChordFactoryXMLHelper.xmlToChord(sb.toString());
        logger.debug("action {}", actual);
        assertThat("The actual value is not null.",
                actual, is(notNullValue()));

        assertThat("string content is correct",
                actual.getDisplayName(), equalTo("Cmaj7"));

        int[] expectedIntervals = new int[] { 4, 7, 11 };
        assertThat("string content is correct",
                actual.getIntervals(), equalTo(expectedIntervals));

        String expectedSpelling = "1 3 5 b8";
        assertThat("string content is correct",
                actual.getSpelling(), equalTo(expectedSpelling));

        String expectedVoicing = "1 3 5 7";
        assertThat("string content is correct",
                actual.getChordVoicing(), equalTo(expectedVoicing));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.xml.ChordFactoryXMLHelper#xmlToChordList(java.lang.String)}
     * .
     */
    @Test
    public void testXmlToChordList() {
        StringBuilder sb = new StringBuilder();
        sb.append("<chords xmlns=\"http://www.rockhoppertech.com/music/chords\">");
        sb.append("<chord name=\"maj7\">");
        sb.append("<intervals>");
        sb.append("<interval>4</interval>");
        sb.append("<interval>7</interval>");
        sb.append("<interval>11</interval>");
        sb.append("</intervals>");
        sb.append("<spelling>");
        sb.append("<degree>1</degree>");
        sb.append("<degree>3</degree>");
        sb.append("<degree>5</degree>");
        sb.append("<degree>b8</degree>");
        sb.append("</spelling>");
        sb.append("<description>major seventh</description>");
        sb.append("<voicing>");
        sb.append("<degree>1</degree>");
        sb.append("<degree>3</degree>");
        sb.append("<degree>5</degree>");
        sb.append("<degree>7</degree>");
        sb.append("</voicing>");
        sb.append("</chord>");
        sb.append("</chords>");
        String xml = sb.toString();
        List<Chord> actual = ChordFactoryXMLHelper.xmlToChordList(xml);
        logger.debug("actual {}", actual);
        assertThat("The actual value is not null.",
                actual, is(notNullValue()));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.xml.ChordFactoryXMLHelper#readChords(java.lang.String)}
     * .
     */
    @Test
    public void testReadChords() {
        URL resourceUrl = getClass().
                getResource("/chorddefs.xml");
        String filename = resourceUrl.getPath();
        ChordFactoryXMLHelper.readChords(filename);
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.xml.ChordFactoryXMLHelper#setDefinitionFileName(java.lang.String)}
     * .
     */
    @Test
    public void testSetDefinitionFileName() {
        URL resourceUrl = getClass().
                getResource("/chorddefs.xml");
        String filename = resourceUrl.getPath();
        // used in readChords() which is called by loadDefinitions() which is
        // called from static init
        ChordFactoryXMLHelper.setDefinitionFileName(filename);
        // no real test
        // ChordFactoryXMLHelper.
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.xml.ChordFactoryXMLHelper#writeChordXML(com.rockhoppertech.music.chord.Chord, java.io.OutputStream)}
     * .
     */
    @Test
    public void testWriteChordXMLChordOutputStream() {

        Chord chord = ChordFactory.getChordByFullSymbol("Cmaj7");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        ChordFactoryXMLHelper.writeChordXML(chord, ps);
        String actual = baos.toString();
        logger.debug("actual {}", actual);
        assertThat("The actual value is not null.",
                actual, is(notNullValue()));
        // <chords xmlns="http://www.rockhoppertech.com/music/chords">
        // <chord name="maj7">
        // <intervals>
        // <interval>4</interval>
        // <interval>7</interval>
        // <interval>11</interval>
        // </intervals>
        // <spelling>
        // <degree>1</degree>
        // <degree>3</degree>
        // <degree>5</degree>
        // <degree>b8</degree>
        // </spelling>
        // <description>major seventh</description>
        // <voicing>
        // <degree>1</degree>
        // <degree>3</degree>
        // <degree>5</degree>
        // <degree>7</degree>
        // </voicing>
        // </chord>
        // </chords>
    }

}
