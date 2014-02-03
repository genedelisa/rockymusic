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

import javax.swing.JOptionPane;

import com.rockhoppertech.music.Duration;
import com.rockhoppertech.music.Pattern;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.scale.Scale;
import com.rockhoppertech.music.scale.ScaleFactory;

import static com.rockhoppertech.music.Pitch.*;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class ScaleMain {

    static void cMajor() {
        // get good ol' C major
        Scale scale = ScaleFactory.getScaleByName("Major");
        MIDITrack track = ScaleFactory.createMIDITrack(scale,
                C5);
        System.err.println("C major scale and track:");
        System.err.println(scale);
        System.err.println(track);
        // track.play();
    }

    static void choose() {
        // choose one
        final String[] choices = ScaleFactory.getScaleNameArray();
        final String chosenScaleName = (String) JOptionPane
                .showInputDialog(null,
                        "Which scale?",
                        "Scale chooser",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        choices,
                        choices[0]);

        if (chosenScaleName != null) {
            Scale scale = ScaleFactory.getScaleByName(chosenScaleName);
            MIDITrack track = ScaleFactory.createMIDITrack(scale,
                    C5);
            System.err.println("Chosen scale and track:");
            System.err.println(scale);
            System.err.println(track);
        }
    }

    static void pattern() {
        // make a pattern
        Scale scale = ScaleFactory.getScaleByName("Major");
        final int[] pattern = new int[] { 0, 2 };
        final int numOctaves = 1;
        final double duration = Duration.Q;
        final double restBetweenPatterns = Duration.Q;
        final boolean reverse = false;

        // final ScalePattern scalePattern = new ScalePattern(scale, pattern,
        // C5,
        // numOctaves, duration, reverse, restBetweenPatterns);
        // scalePattern.setUpAndDown(true);
        // MIDITrack track = scalePattern.createMIDITrack(1d);

        final Pattern pattern1 = new Pattern(scale.getDegrees(), pattern, C5,
                numOctaves, duration, reverse, restBetweenPatterns);
        pattern1.setUpAndDown(true);
        MIDITrack track = pattern1.createTrack(1d, false);

        System.err.println("Pattern 0, 2");
        System.err.println(pattern1);
        System.err.println(track);
        track.sequential();
        track.play();

    }

    static void octatonic() {
        // Igor's favorite
        Scale scale = ScaleFactory.getScaleByName("Octatonic");
        MIDITrack track = ScaleFactory.createMIDITrack(scale,
                C5);
        System.err.println("octatonic");
        System.err.println(scale);
        System.err.println(track);
    }

    /**
     * @param args
     */
    public static void main(final String[] args) {
        pattern();
    }

}
