/**
 * 
 */
package com.rockhoppertech.music.scale;

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


import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.ArrayUtils;

import com.rockhoppertech.music.Duration;
import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.midi.js.MIDITrack;

/**
 * Essentially a holder for the values to create a MIDITrack based on a given
 * Scale and a pattern contained in an int array.
 * 
 * The real work is done by {@link ScaleFactory#getNoteListPattern()}
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class ScalePattern {
    public static final String PITCH = "PITCH";
    public static final String SCALE = "SCALE";
    public static final String PATTERN = "PATTERN";
    public static final String NUM_OCTAVES = "NUM_OCTAVES";
    public static final String DURATION = "DURATION";
    public static final String REVERSE = "REVERSE";
    public static final String REST = "REST";
    public static final String DISPLAY_NAME = "DISPLAY_NAME";

    private Scale scale;
    private int startPitch;
    private int[] pattern;
    private int numOctaves;
    private double duration;
    private boolean reverse;
    private double restBetweenPatterns;
    private String displayName;
    private boolean upAndDown = false;

    /**
     * <pre>
     * Scale scale = ScaleFactory.getScaleByName(&quot;Major&quot;);
     * int[] pattern = new int[] { 0, 2 };
     * int numOctaves = 1;
     * double duration = Duration.Q;
     * double restBetweenPatterns = Duration.Q;
     * boolean reverse = false;
     * ScalePattern scalePattern = new ScalePattern(scale, pattern, C5,
     *         numOctaves, duration, reverse, restBetweenPatterns);
     * scalePattern.setUpAndDown(true);
     * MIDITrack nl = scalePattern.createNoteList(1d);
     * </pre>
     * 
     * @param scale
     * @param pattern
     * @param startPitch
     * @param numOctaves
     * @param duration
     * @param reverse
     * @param restBetweenPatterns
     */
    public ScalePattern(Scale scale, int[] pattern, int startPitch,
            int numOctaves, double duration, boolean reverse,
            double restBetweenPatterns) {
        this.scale = scale;
        this.startPitch = startPitch;
        this.pattern = pattern;
        this.numOctaves = numOctaves;
        this.duration = duration;
        this.reverse = reverse;
        this.restBetweenPatterns = restBetweenPatterns;
        Date now = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy MMM dd kk:mm:ss:SSS");
        this.setDisplayName(df.format(now));
        // this.setDisplayName(DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.LONG).format(now));
    }

    public ScalePattern(Scale scale, int[] pattern, int startPitch) {
        this(scale, pattern, startPitch, 1, Duration.Q, false, Duration.Q);
    }

    public ScalePattern(Scale scale, int[] pattern) {
        this(scale, pattern, Pitch.C5, 1, Duration.Q, false, Duration.Q);
    }

    /**
     * @return the scale
     */
    public Scale getScale() {
        return this.scale;
    }

    /**
     * @param scale
     *            the scale to set
     */
    public void setScale(Scale scale) {
        this.scale = scale;
    }

    /**
     * @return the startPitch
     */
    public int getStartPitch() {
        return this.startPitch;
    }

    /**
     * @param startPitch
     *            the startPitch to set
     */
    public void setStartPitch(int startPitch) {
        this.startPitch = startPitch;
    }

    /**
     * @return the pattern
     */
    public int[] getPattern() {
        return this.pattern;
    }

    public String getPatternAsString() {
        return ArrayUtils.toString(this.pattern);
        // return ArrayUtils.asString(this.pattern);
    }

    /**
     * @param pattern
     *            the pattern to set
     */
    public void setPattern(int[] pattern) {
        this.pattern = pattern;
    }

    /**
     * @return the numOctaves
     */
    public int getNumOctaves() {
        return this.numOctaves;
    }

    /**
     * @param numOctaves
     *            the numOctaves to set
     */
    public void setNumOctaves(int numOctaves) {
        this.numOctaves = numOctaves;
    }

    /**
     * @return the duration
     */
    public double getDuration() {
        return this.duration;
    }

    /**
     * @param duration
     *            the duration to set
     */
    public void setDuration(double duration) {
        this.duration = duration;
    }

    /**
     * @return the reverse
     */
    public boolean isReverse() {
        return this.reverse;
    }

    /**
     * @param reverse
     *            the reverse to set
     */
    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    /**
     * @return the restBetweenPatterns
     */
    public double getRestBetweenPatterns() {
        return this.restBetweenPatterns;
    }

    /**
     * @param restBetweenPatterns
     *            the restBetweenPatterns to set
     */
    public void setRestBetweenPatterns(double restBetweenPatterns) {
        this.restBetweenPatterns = restBetweenPatterns;
    }

    /**
     * @param gap
     *            the duration between patterns
     * @return a MIDITrack containing the patterns
     */
    public MIDITrack createMIDITrack(double gap) {
        MIDITrack track = new MIDITrack();
        if (this.scale == null) {
            throw new IllegalStateException("Scale is null");
        }
        MIDITrack list = ScaleFactory
                .getNoteListPattern(this.getScale(),
                        this.getPattern(),
                        this.getScale().getLength(),
                        this.getStartPitch(),
                        this.getNumOctaves(),
                        this.getDuration(),
                        this.isReverse(),
                        this.getRestBetweenPatterns(),
                        this.upAndDown);

        String s = String
                .format(
                        "%s - pattern %s - start %d octs %d dur %f reverse %b restBetween %f updown %b",
                        this.getScale().getName(),
                        ArrayUtils.toString(this.getPattern()),
                        // arrayToString(this.getPattern()),
                        this.getStartPitch(),
                        this.getNumOctaves(),
                        this.getDuration(),
                        this.isReverse(),
                        this.getRestBetweenPatterns(),
                        this.upAndDown);
        track.setName(s);
        s = String
                .format(
                        "Generated from %s scale.\nThe pattern applied was %s\nthe start pitch is %d\nnumber of octaves %d\nduration %f\nis reverse %b\ngap %f\nup and down %b\n",
                        this.getScale().getName(),
                        ArrayUtils.toString(this.getPattern()),
                        this.getStartPitch(),
                        this.getNumOctaves(),
                        this.getDuration(),
                        this.isReverse(),
                        this.getRestBetweenPatterns(),
                        this.upAndDown);
        track.setDescription(s);

        track.append(list,
                gap);

        // if (this.upAndDown) {
        // if (this.getScale().isDescendingDifferent()) {
        // MIDITrack reverse = ScaleFactory.createMIDITrack(this
        // .getStartPitch() + 12, this.getScale()
        // .getDescendingIntervals(), 1d, this.getDuration(), this
        // .getNumOctaves(), true);
        // notelist.append(reverse);
        // notelist.sequential();
        // } else {
        // MIDITrack reverse = notelist.retrograde();
        // notelist.append(reverse);
        // notelist.sequential();
        // }
        // }

        return track;
    }

    /**
     * 
     * @return the display name
     */
    public String getDisplayName() {
        return this.displayName;
    }

    /**
     * @param displayName
     *            the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @return if it is up and down
     */
    public boolean isUpAndDown() {
        return this.upAndDown;
    }

    /**
     * @param upAndDown
     */
    public void setUpAndDown(boolean upAndDown) {
        this.upAndDown = upAndDown;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Pattern Name:").append(this.displayName).append(' ');
        sb.append("pattern:").append(this.getPatternAsString()).append(' ');
        sb.append("startPitch:").append(this.startPitch).append(' ');
        sb.append("duration:").append(this.duration).append(' ');        
        sb.append("upAndDown:").append(this.upAndDown).append(' ');
        sb.append("numOctaves:").append(this.numOctaves).append(' ');        
        sb.append("reverse:").append(this.reverse).append(' ');                
        sb.append("restBetweenPatterns:").append(this.restBetweenPatterns);                
        return sb.toString();
    }
}
