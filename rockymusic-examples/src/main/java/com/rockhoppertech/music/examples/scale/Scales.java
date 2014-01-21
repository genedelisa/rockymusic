package com.rockhoppertech.music.examples.scale;

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

import com.rockhoppertech.music.Duration;
import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.scale.Scale;
import com.rockhoppertech.music.scale.ScaleFactory;

public class Scales {
    static Logger logger = LoggerFactory.getLogger(Scales.class);

    public static void main(String[] args) {
        scale();

    }

    public static void scale() {

        // c major scale on middle c (C5)
        MIDITrack track = ScaleFactory.createMIDITrack("Major");
        logger.debug("major track {}", track);

        // d major scale
        track = ScaleFactory.createMIDITrack("Major", Pitch.D5);
        logger.debug("major track on D5 {}", track);

        Scale major = ScaleFactory.getScaleByName("Major");
        logger.debug("major scale {}", major);

        track = ScaleFactory.createMIDITrack(major, Pitch.EF3, 2d, Duration.E);
        logger.debug(
                "major track from scale on Ef starting at beat 2 with dur .5 {}",
                track);

        for (String name : ScaleFactory.getScaleNames()) {
            logger.debug(name);
        }
    }

}
