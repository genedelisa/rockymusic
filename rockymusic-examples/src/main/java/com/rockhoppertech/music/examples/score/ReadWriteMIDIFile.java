package com.rockhoppertech.music.examples.score;

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

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.midi.js.Score;
import com.rockhoppertech.music.midi.js.ScoreFactory;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 *
 */
public class ReadWriteMIDIFile {
    
    private static final Logger logger = LoggerFactory
            .getLogger(ReadWriteMIDIFile.class);

    public static void main(String[] args) {

        writeMIDIfileAndReadItBack();
    }

    static void writeMIDIfileAndReadItBack() {
        Score score = new Score("Create test");

        MIDITrack track = new MIDITrack();
        track.setName("Piano");
        track.add(Pitch.C5);

        score.add(track);
        score.play();
        
        logger.debug("score");
        logger.debug(score.toString());
        logger.debug("writing");
        String filename = "src/test/resources/midifiles/created.mid";
        ScoreFactory.writeToMIDIFile(filename, score);

        Score score2 = null;
        try {
            score2 = ScoreFactory.readSequence(filename);
        } catch (InvalidMidiDataException | IOException e) {
            e.printStackTrace();
            logger.error(e.getLocalizedMessage(), e);
            return;
        }
        logger.debug("read back");
        logger.debug(score2.toString());
        score2.play();        
    }

}
