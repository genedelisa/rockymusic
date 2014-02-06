package com.rockhoppertech.music.examples.series.time;

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

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.midi.js.Score;
import com.rockhoppertech.music.series.time.Sound;
import com.rockhoppertech.music.series.time.TimeSeries;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class TimeSeriesExamples {
    static Logger logger = LoggerFactory.getLogger(TimeSeriesExamples.class);

    public static void main(String[] args) {
        select();
    }

    static void removeSilences() {
        final TimeSeries ts = new TimeSeries();
        for (int i = 0; i < 10; i++) {
            ts.add(new Sound(i + 1, .5));
        }
        logger.debug("ts {}", ts);
        /*
         * for(int i=0; i<10; i++) ts.add( new Silence(i, 2.1) );
         */
        ts.removeSilences();
        logger.debug("ts {}", ts);

    }

    static void play() {
        TimeSeries ts = new TimeSeries(
                "e e e e e e e e e e e e e e e e e s s s s s s s s s s s");
        ts.play(true);
    }

    static void play2() {
        TimeSeries ts = new TimeSeries(
                "e e e e e e e e e e e e e e e e e s s s s s s s s s s s");
        MIDITrack track = ts.toMIDITrack();
        logger.debug("track {}", track);
        // track.play();
        Score score = new Score();
        score.add(track);
        score.playWithClickTrack();
    }

    static void select() {
        final String[] choices = new String[] { "play",
                "play2",
                "removeSilences"
        };
        final String choice = (String) JOptionPane
                .showInputDialog(null,
                        "Which Example?",
                        "Example chooser",
                        JOptionPane.QUESTION_MESSAGE,
                        null, // icon
                        choices,
                        choices[0]);
        if (choice.equals("play")) {
            play();
        } else if (choice.equals("play2")) {
            play2();
        } else if (choice.equals("removeSilences")) {
            removeSilences();
        } else if (choice.equals("")) {            

        }
        
        
    }

}
