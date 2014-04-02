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
