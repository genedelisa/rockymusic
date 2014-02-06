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

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.midi.js.MIDITrackBuilder;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class MIDITrackXMLHelperTest {
    private static final Logger logger = LoggerFactory
            .getLogger(MIDITrackXMLHelperTest.class);

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.xml.MIDITrackXMLHelper#getDocumentElementFromFile(java.lang.String)}
     * .
     */
    @Test
    public void testGetDocumentElementFromFile() {
        URL resourceUrl = getClass().
                getResource("/testTrack.xml");

        Element doc = MIDITrackXMLHelper.getDocumentElementFromFile(resourceUrl
                .getPath());
        assertThat(
                "The document element is not null.",
                doc, is(notNullValue()));
        logger.debug("document element {}", doc);

        assertThat("the node name is correct",
                doc.getNodeName(),
                is(equalTo("MIDITrack")));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.xml.MIDITrackXMLHelper#getDocumentElementFromString(java.lang.String)}
     * .
     */
    @Test
    @Ignore
    public void testGetDocumentElementFromString() {

        // called from readMIDITrackFromXMLString
        Element docel = MIDITrackXMLHelper.getDocumentElementFromString("");
        assertThat(
                "The document element is not null.",
                docel,
                is(notNullValue()));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.xml.MIDITrackXMLHelper#isOnlyRequired()}
     * .
     */
    // @Test
    // public void testIsOnlyRequired() {
    // MIDITrackXMLHelper.setOnlyRequired(true);
    // assertThat("is only required",
    // MIDITrackXMLHelper.isOnlyRequired(), is(equalTo(true)));
    //
    // MIDITrackXMLHelper.setOnlyRequired(false);
    // assertThat("is not only required",
    // MIDITrackXMLHelper.isOnlyRequired(), is(equalTo(false)));
    // }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.xml.MIDITrackXMLHelper#MIDITrackToString(com.rockhoppertech.music.midi.js.MIDITrack)}
     * .
     */
    @Test
    public void testMIDITrackToString() {
        MIDITrack track = MIDITrackBuilder.create()
                .name("test track")
                .noteString("C ")
                .build();
        String actual = MIDITrackXMLHelper.MIDITrackToString(track, false);
        logger.debug("'{}'", actual);
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.xml.MIDITrackXMLHelper#parseMIDITrack(org.w3c.dom.Node)}
     * .
     */
    @Test
    public void testParseMIDITrack() {
        URL resourceUrl = getClass().
                getResource("/testTrack.xml");

        Element doc = MIDITrackXMLHelper.getDocumentElementFromFile(resourceUrl
                .getPath());
        MIDITrack track = MIDITrackXMLHelper.parseMIDITrack(doc);
        assertThat(
                "The track is not null.",
                track,
                is(notNullValue()));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.xml.MIDITrackXMLHelper#readMIDITrackFromXMLString(java.lang.String)}
     * .
     */
    @Test
    public void testReadMIDITrackFromXMLString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
        sb.append("<MIDITrack name=\"test track\" xmlns=\"http://www.rockhoppertech.com/music/MIDITrack\">\n");
        sb.append("<MIDINote bank=\"0\" channel=\"0\" duration=\"1\" endBeat=\"2\" midiNumber=\"60\" pan=\"64\" pitch=\"C5 \" pitchBend=\"0\" program=\"0\" startBeat=\"1\" velocity=\"64\"/>\n");
        sb.append("</MIDITrack>\n");
        String xml = sb.toString();

        MIDITrack track = MIDITrackXMLHelper
                .readMIDITrackFromXMLString(xml);
        assertThat(
                "The track is not null.",
                track,
                is(notNullValue()));
        logger.debug("{}", track);
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.xml.MIDITrackXMLHelper#readMIDITrackXMLFromFile(java.lang.String)}
     * .
     */
    @Test
    public void testReadMIDITrackXMLFromFile() {
        URL resourceUrl = getClass().
                getResource("/testTrack.xml");
        MIDITrack track = MIDITrackXMLHelper
                .readMIDITrackXMLFromFile(resourceUrl.getPath());
        assertThat(
                "The track is not null.",
                track,
                is(notNullValue()));
        logger.debug("{}", track);
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.xml.MIDITrackXMLHelper#setOnlyRequired(boolean)}
     * .
     */
    @Test
    @Ignore
    public void testSetOnlyRequired() {

        // just start, duration, and pitch
        // MIDITrackXMLHelper.setOnlyRequired(true);
        MIDITrack track = MIDITrackBuilder.create()
                .name("test track")
                .noteString("C ")
                .build();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        // PrintStream out = System.out;
        // System.setOut(ps);
        MIDITrackXMLHelper.writeXML(track, ps, true);
        // System.setOut(out);
        logger.debug("'{}'", baos.toString());
        // MIDITrackXMLHelper.setOnlyRequired(false);

        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
        sb.append("<MIDITrack name=\"test track\" xmlns=\"http://www.rockhoppertech.com/music/MIDITrack\">\n");
        sb.append("<MIDINote duration=\"1\" pitch=\"C5 \" startBeat=\"1\"/>\n");
        sb.append("</MIDITrack>\n");

        // String xml =
        // "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<MIDITrack xmlns=\"http://www.rockhoppertech.com/music/MIDITrack\" name=\"test track\">\n<MIDINote duration=\"1\" pitch=\"C5 \" startBeat=\"1\"/>\n</MIDITrack>\n";

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<MIDITrack name=\"test track\" xmlns=\"http://www.rockhoppertech.com/music/MIDITrack\">\n<MIDINote duration=\"1\" pitch=\"C5 \" startBeat=\"1\"/>\n</MIDITrack>\n";

        assertThat("string content is correct",
                baos.toString(), equalTo(xml));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.xml.MIDITrackXMLHelper#writeXML(com.rockhoppertech.music.midi.js.MIDITrack)}
     * .
     */
    @Test
    @Ignore
    public void testWriteXMLMIDITrack() {

        MIDITrack track = MIDITrackBuilder.create()
                .name("test track")
                .noteString("C ")
                .build();
        // MIDITrackXMLHelper.setOnlyRequired(false);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream out = System.out;
        System.setOut(ps);
        MIDITrackXMLHelper.writeXML(track);
        System.setOut(out);

        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
        sb.append("<MIDITrack name=\"test track\" xmlns=\"http://www.rockhoppertech.com/music/MIDITrack\">\n");
        sb.append("<MIDINote bank=\"0\" channel=\"0\" duration=\"1\" endBeat=\"2\" midiNumber=\"60\" pan=\"64\" pitch=\"C5 \" pitchBend=\"0\" program=\"0\" startBeat=\"1\" velocity=\"64\"/>\n");
        sb.append("</MIDITrack>\n");
        // String xml = sb.toString();
        // String xml =
        // "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<MIDITrack xmlns=\"http://www.rockhoppertech.com/music/MIDITrack\" name=\"test track\">\n<MIDINote bank=\"0\" channel=\"0\" duration=\"1\" endBeat=\"2\" midiNumber=\"60\" pan=\"64\" pitch=\"C5 \" pitchBend=\"0\" program=\"0\" startBeat=\"1\" velocity=\"64\"/>\n</MIDITrack>\n";

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<MIDITrack name=\"test track\" xmlns=\"http://www.rockhoppertech.com/music/MIDITrack\">\n<MIDINote bank=\"0\" channel=\"0\" duration=\"1\" endBeat=\"2\" midiNumber=\"60\" pan=\"64\" pitch=\"C5 \" pitchBend=\"0\" program=\"0\" startBeat=\"1\" velocity=\"64\"/>\n</MIDITrack>\n";

        assertThat("string content is correct",
                baos.toString(), equalTo(xml));

        logger.debug("'{}'", baos.toString());
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.xml.MIDITrackXMLHelper#writeXML(com.rockhoppertech.music.midi.js.MIDITrack, java.io.OutputStream)}
     * .
     */
    @Test
    @Ignore
    public void testWriteXMLMIDITrackOutputStream() {
        MIDITrack track = MIDITrackBuilder.create()
                .name("test track")
                .noteString("C ")
                .build();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        // MIDITrackXMLHelper.setOnlyRequired(false);
        MIDITrackXMLHelper.writeXML(track, ps, false);

        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
        sb.append("<MIDITrack name=\"test track\" xmlns=\"http://www.rockhoppertech.com/music/MIDITrack\">\n");
        sb.append("<MIDINote bank=\"0\" channel=\"0\" duration=\"1\" endBeat=\"2\" midiNumber=\"60\" pan=\"64\" pitch=\"C5 \" pitchBend=\"0\" program=\"0\" startBeat=\"1\" velocity=\"64\"/>\n");
        sb.append("</MIDITrack>\n");
        // String xml = sb.toString();
        // String xml =
        // "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<MIDITrack xmlns=\"http://www.rockhoppertech.com/music/MIDITrack\" name=\"test track\">\n<MIDINote bank=\"0\" channel=\"0\" duration=\"1\" endBeat=\"2\" midiNumber=\"60\" pan=\"64\" pitch=\"C5 \" pitchBend=\"0\" program=\"0\" startBeat=\"1\" velocity=\"64\"/>\n</MIDITrack>\n";

        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<MIDITrack name=\"test track\" xmlns=\"http://www.rockhoppertech.com/music/MIDITrack\">\n<MIDINote bank=\"0\" channel=\"0\" duration=\"1\" endBeat=\"2\" midiNumber=\"60\" pan=\"64\" pitch=\"C5 \" pitchBend=\"0\" program=\"0\" startBeat=\"1\" velocity=\"64\"/>\n</MIDITrack>\n";

        assertThat("string content is correct",
                baos.toString(), equalTo(xml));

        logger.debug("'{}'", baos.toString());

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.xml.MIDITrackXMLHelper#createDocument(com.rockhoppertech.music.midi.js.MIDITrack)}
     * .
     */
    @Test
    public void testCreateDocument() {
        MIDITrack track = MIDITrackBuilder.create()
                .name("test track")
                .noteString("C ")
                .build();
        Document doc = MIDITrackXMLHelper.createDocument(track, false);
        assertThat(
                "The document  is not null.",
                doc,
                is(notNullValue()));
        logger.debug("doc {}", doc);
    }

}
