/**
 * 
 */
package com.rockhoppertech.music.examples;

/*
 * #%L
 * Rocky Music Examples
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.midi.js.MIDITrackBuilder;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class TrackAdditions {
    static Logger logger = LoggerFactory.getLogger(TrackAdditions.class);

    /**
     * @param args
     */
    public static void main(String[] args) {
        // append();
        appendNIndexes();
        // appendN();

    }

    public static void append() {

        MIDITrack track = MIDITrackBuilder.create()
                .name("track 1")
                .noteString("C D E")
                .sequential()
                .build();
        MIDITrack track2 = MIDITrackBuilder.create()
                .name("track 2")
                .noteString("F G")
                .durations(.5, .25)
                .sequential()
                .build();

        track.append(track2);
        logger.debug("appended {}", track);

    }

    public static void appendN() {
        MIDITrack track = MIDITrackBuilder.create()
                .name("track x 3")
                .noteString("C D E")
                .sequential()
                .build();
        track.appendNTimes(track, 3);
        logger.debug("appended {}", track);
    }

    public static void appendNIndexes() {
        MIDITrack track = MIDITrackBuilder.create()
                .name("track n with indexes")
                .noteString("C D E F G A B")
                .sequential()
                .build();

        double gap = 1.5;
        int from = 2; // start at the E
        int to = 5; // stop with the G
        track.append(track, gap, from, to);
        logger.debug("appended {}", track);
    }

}
