package com.rockhoppertech.music.fx.cmn.musicxml;

import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.midi.js.MIDITrackBuilder;
import org.junit.Test;

import java.io.StringWriter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Created by gene on 3/21/14.
 */
public class TrackToMusicXMLTest {
    @Test
    public void testEmitXML() throws Exception {
        MIDITrack track = MIDITrackBuilder.create()
                .noteString("c d e")
                .build();
        StringWriter sw = new StringWriter();
        TrackToMusicXML.emitXML(track, sw, "foo", "moats art");
        String xml = sw.toString();
        System.out.println(xml);

        assertThat("the xml is not null", xml, notNullValue());

    }
}
