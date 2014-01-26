/**
 * 
 */
package com.rockhoppertech.music;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.chord.Chord;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.midi.js.MIDITrackFactory;
import com.rockhoppertech.music.scale.Scale;

/**
 * Essentially a holder for the values to create a MIDITrack based on a given
 * set of degrees and a pattern contained in an int array.
 * 
 * The real work is done by {@link MIDITrackFactory#getTrackPattern()}
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class Pattern {

    private static final Logger logger = LoggerFactory
            .getLogger(Pattern.class);

    /**
     * Bloch's pattern and not the GoF pattern.
     * 
     * Pattern pattern = new
     * Pattern.Builder(chord.getPitchClasses(),someInts).build();
     * 
     * or even
     * 
     * <pre>
     * Pattern pattern = new Pattern.Builder(chord.getPitchClasses(), someInts)
     *         .startPitch(C5).numOctaves(3).duration(Q)
     *         .reverse(false).restBetweenPatterns(0).upAndDown(false)
     *         .build();
     * </pre>
     * 
     * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
     * 
     */
    public static class Builder {
        private int[] degrees;
        private int[] pattern;
        private int startPitch = Pitch.C5;
        private int numOctaves = 1;
        private double duration = Duration.Q;
        private boolean reverse = false;
        private boolean upAndDown = false;
        private double restBetweenPatterns;

        /**
         * These are the non optional fields.
         * 
         * @param degrees
         * @param pattern
         */
        public Builder(int[] degrees, int[] pattern) {
            this.degrees = degrees;
            this.pattern = pattern;
        }

        public Builder(List<Integer> d, int[] pattern) {
            this.degrees = new int[d.size()];
            for (int i = 0; i < degrees.length; i++) {
                degrees[i] = d.get(i);
            }
            this.pattern = pattern;
        }

        public Pattern build() {
            // enforced by the constructor.
            if (this.degrees == null || this.pattern == null) {
                throw new IllegalArgumentException(
                        "You need both degrees and pattern");
            }
            Pattern result = new Pattern(this.degrees, this.pattern,
                    this.startPitch, this.numOctaves, this.duration,
                    this.reverse, this.restBetweenPatterns);
            result.setUpAndDown(this.upAndDown);
            this.reset();
            return result;
        }

        private void reset() {
            this.degrees = null;
            this.pattern = null;
            this.startPitch = Pitch.C5;
            this.numOctaves = 1;
            this.duration = Duration.Q;
            this.reverse = false;
            this.upAndDown = false;
            this.restBetweenPatterns = 0;
        }

        public Builder degrees(int[] degrees) {
            this.degrees = degrees;
            return this;
        }

        public Builder pattern(int[] pattern) {
            this.pattern = pattern;
            return this;
        }

        public Builder startPitch(int startPitch) {
            this.startPitch = startPitch;
            return this;
        }

        public Builder numOctaves(int numOctaves) {
            this.numOctaves = numOctaves;
            return this;
        }

        public Builder duration(double duration) {
            this.duration = duration;
            return this;
        }

        public Builder restBetweenPatterns(double restBetweenPatterns) {
            this.restBetweenPatterns = restBetweenPatterns;
            return this;
        }

        public Builder reverse(boolean reverse) {
            this.reverse = reverse;
            return this;
        }

        public Builder upAndDown(boolean upAndDown) {
            this.upAndDown = upAndDown;
            return this;
        }
    }

    static int[] spread(final int[] degrees, final int numOctaves) {
        final int[] a = new int[degrees.length * numOctaves];
        int j = 0;
        for (int i = 0; i < numOctaves; i++) {
            for (final int d : degrees) {
                a[j++] = d + (i * 12);
            }
        }
        return a;
    }

    private int[] degrees;
    private int[] pattern;
    private int startPitch;
    private int numOctaves;
    private double duration;
    private boolean reverse;
    private double restBetweenPatterns;
    private String displayName;
    private boolean upAndDown = false;
    private List<Double> durationList;

    private int limit;

    public Pattern(final Chord chord, final int[] pattern,
            final int startPitch, final int numOctaves, final double duration,
            final boolean reverse, final double restBetweenPatterns) {

        this(chord.getPitchClasses(), pattern, startPitch, numOctaves,
                duration, reverse, restBetweenPatterns);
    }

    public Pattern(final Chord chord, final int[] pattern,
            final int startPitch) {
        this(chord.getPitchClasses(), pattern, startPitch, 2,
                Duration.Q, false, 0d);
    }

    public Pattern(final int[] degrees, final int[] pattern) {
        this(degrees, pattern, Pitch.C5, 1, Duration.Q, false, 0);
    }

    /**
     * Each entry in the pattern is an index into the degrees. So this value
     * must be < the length of the degree array.
     * 
     * A pattern = new int[] { 0 } is the identity result.
     * 
     * <pre>
     * int[] degrees = new int[] { 0, 2, 4, 5, 6, 9, 11 };
     * int[] pattern = new int[] { 0, 2 };
     * int numOctaves = 1;
     * double duration = Duration.Q;
     * double restBetweenPatterns = Duration.Q;
     * boolean reverse = false;
     * Pattern nlPattern = new Pattern(degrees, pattern, C5, numOctaves, duration,
     *         reverse, restBetweenPatterns);
     * nlPattern.setUpAndDown(true);
     * MIDITrack nl = nlPattern.createTrack(1d);
     * </pre>
     * 
     * @param degrees
     * @param pattern
     * @param startPitch
     * @param numOctaves
     * @param duration
     * @param reverse
     * @param restBetweenPatterns
     */
    public Pattern(final int[] degrees, final int[] pattern,
            final int startPitch, final int numOctaves, final double duration,
            final boolean reverse, final double restBetweenPatterns) {

        this.degrees = degrees;
        this.startPitch = startPitch;
        this.pattern = pattern;
        this.numOctaves = numOctaves;
        this.duration = duration;
        this.reverse = reverse;
        this.restBetweenPatterns = restBetweenPatterns;
        final Date now = new Date();
        final SimpleDateFormat df = new SimpleDateFormat(
                "yyyy MMM dd kk:mm:ss:SSS");
        this.setDisplayName(df.format(now));
        // this.setDisplayName(DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.LONG).format(now));
    }

    public Pattern(final Scale scale, final int[] pattern) {
        this(scale, pattern, Pitch.C5, 1, Duration.Q, false, Duration.Q);
    }

    public Pattern(final Scale scale, final int[] pattern, final int startPitch) {
        this(scale, pattern, startPitch, 1, Duration.Q, false, Duration.Q);
    }

    /**
     * <pre>
     * Scale scale = ScaleFactory.getScaleByName(&quot;Major&quot;);
     * int[] pattern = new int[] { 0, 2 };
     * int numOctaves = 1;
     * double duration = Duration.Q;
     * double restBetweenPatterns = Duration.Q;
     * boolean reverse = false;
     * Pattern scalePattern = new Pattern(scale, pattern, C5, numOctaves, duration,
     *         reverse, restBetweenPatterns);
     * scalePattern.setUpAndDown(true);
     * MIDITrack nl = scalePattern.createTrack(1d);
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
    public Pattern(final Scale scale, final int[] pattern,
            final int startPitch, final int numOctaves, final double duration,
            final boolean reverse, final double restBetweenPatterns) {

        this(Interval.intervalsToDegrees(scale.getIntervals(), numOctaves),
                pattern, startPitch, numOctaves, duration, reverse,
                restBetweenPatterns);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Degrees: ").append(ArrayUtils.toString(this.degrees))
                .append(' ');
        sb.append("Pattern: ").append(ArrayUtils.toString(this.pattern))
                .append(' ');
        sb.append("Limit: ").append(this.limit).append(' ');
        sb.append("Start Pitch: ").append(this.startPitch).append(' ');
        sb.append("nOct: ").append(this.numOctaves).append(' ');
        return sb.toString();
    }

    /**
     * @param gap
     *            the duration between patterns
     * @return a MIDITrack containing the patterns
     */
    public MIDITrack createTrack() {
        boolean spread = false;
        return createTrack(0d, spread);
    }

    public static int max(int[] values) {
        int max = Integer.MIN_VALUE;
        for (int i : values) {
            if (i > max)
                max = i;
        }
        return max;

    }

    /**
     * If the pattern is based on degrees instead of midipitches then spread
     * will project the degress over the number of octaves
     * 
     * @param gap
     * @param spread
     * @return
     */
    public MIDITrack createTrack(final double gap, boolean spread) {
        if (this.degrees == null) {
            throw new IllegalStateException("degrees are  null");
        }

        // this.limit = this.degrees.length - this.pattern.length + 1;
        this.limit = this.degrees.length;
        final int max = max(this.degrees);

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("max %d limit %d", max, limit));
        }
        MIDITrack notelist = null;

        if (spread) {
            final int numOcts = (Pitch.MAX - this.startPitch) / 12 + 1;
            final int[] a = Pattern.spread(this.degrees, numOcts);

            notelist = MIDITrackFactory.getTrackPattern(a, this
                    .getPattern(), this.limit, this.getStartPitch(), this
                    .getNumOctaves(), this.getDuration(), this.isReverse(),
                    this.getRestBetweenPatterns(), this.upAndDown);
        } else {
            List<Integer> midiNumberList = new ArrayList<Integer>();
            // degrees is int[] and not Integer[]
            // Collections.addAll(midiNumberList, this.degrees);
            for (int d : this.degrees) {
                midiNumberList.add(d);
            }
            List<Integer> patternList = new ArrayList<Integer>();
            for (int d : this.pattern) {
                patternList.add(d);
            }
            notelist = MIDITrackFactory.applyPattern(midiNumberList,
                    patternList, this.getStartPitch(), this.getNumOctaves(),
                    this.isReverse(), this.upAndDown).sequential();

        }

        String s = String.format("%s - %s - s%d o%d d%f r%b g%f u%b",
                ArrayUtils.toString(this.degrees), ArrayUtils.toString(this
                        .getPattern()), this.getStartPitch(), this
                        .getNumOctaves(), this.getDuration(), this.isReverse(),
                this.getRestBetweenPatterns(), this.upAndDown);
        notelist.setName(s);
        s = String
                .format(
                        "Generated from %s degrees.\nThe pattern applied was %s\nthe start pitch is %d\nnumber of octaves %d\nduration %f\nis reverse %b\ngap %f\nup and down %b\n",
                        ArrayUtils.toString(this.degrees),
                        ArrayUtils
                                .toString(this.getPattern()),
                        this
                                .getStartPitch(),
                        this.getNumOctaves(),
                        this
                                .getDuration(),
                        this.isReverse(),
                        this
                                .getRestBetweenPatterns(),
                        this.upAndDown);
        notelist.setDescription(s);

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

        return notelist;
    }

    public MIDITrack createTrackWithLimit(final int limit) {
        if (this.degrees == null) {
            throw new IllegalStateException("degrees are  null");
        }

        // this.limit = this.degrees.length - this.pattern.length + 1;
        final int max = max(this.degrees);

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("max %d limit %d", max, limit));
        }

        // final int numOcts = (Pitch.MAX - this.startPitch) / 12 + 1;
        final int numOcts = 2;

        final int[] a = Pattern.spread(this.degrees, numOcts);

        final MIDITrack notelist = MIDITrackFactory.getTrackPattern(a,
                this.getPattern(), this.limit, this.getStartPitch(), this
                        .getNumOctaves(), this.getDuration(), this.isReverse(),
                this.getRestBetweenPatterns(), this.upAndDown);

        String s = String.format("%s - %s - s%d o%d d%f r%b g%f u%b",
                ArrayUtils.toString(this.degrees), ArrayUtils.toString(this
                        .getPattern()), this.getStartPitch(), this
                        .getNumOctaves(), this.getDuration(), this.isReverse(),
                this.getRestBetweenPatterns(), this.upAndDown);
        notelist.setName(s);
        s = String
                .format(
                        "Generated from %s degrees.\nThe pattern applied was %s\nthe start pitch is %d\nnumber of octaves %d\nduration %f\nis reverse %b\ngap %f\nup and down %b\n",
                        ArrayUtils.toString(this.degrees),
                        ArrayUtils
                                .toString(this.getPattern()),
                        this
                                .getStartPitch(),
                        this.getNumOctaves(),
                        this
                                .getDuration(),
                        this.isReverse(),
                        this
                                .getRestBetweenPatterns(),
                        this.upAndDown);
        notelist.setDescription(s);

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

        return notelist;
    }

    /**
     * @return the degrees
     */
    public int[] getDegrees() {
        return this.degrees;
    }

    /**
     * 
     * @return
     */
    public String getDisplayName() {
        return this.displayName;
    }

    /**
     * @return the duration
     */
    public double getDuration() {
        return this.duration;
    }

    /**
     * @return the numOctaves
     */
    public int getNumOctaves() {
        return this.numOctaves;
    }

    /**
     * @return the pattern
     */
    public int[] getPattern() {
        return this.pattern;
    }

    public String getPatternAsString() {
        return ArrayUtils.toString(this.pattern);
    }

    /**
     * @return the restBetweenPatterns
     */
    public double getRestBetweenPatterns() {
        return this.restBetweenPatterns;
    }

    /**
     * @return the startPitch
     */
    public int getStartPitch() {
        return this.startPitch;
    }

    /**
     * @return the reverse
     */
    public boolean isReverse() {
        return this.reverse;
    }

    public boolean isUpAndDown() {
        return this.upAndDown;
    }

    /**
     * @param degrees
     *            the degrees to set
     */
    public void setDegrees(final int[] degrees) {
        this.degrees = degrees;
    }

    /**
     * @param displayName
     *            the displayName to set
     */
    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    /**
     * @param duration
     *            the duration to set
     */
    public void setDuration(final double duration) {
        this.duration = duration;
    }

    /**
     * @param numOctaves
     *            the numOctaves to set
     */
    public void setNumOctaves(final int numOctaves) {
        this.numOctaves = numOctaves;
    }

    /**
     * @param pattern
     *            the pattern to set
     */
    public void setPattern(final int[] pattern) {
        this.pattern = pattern;
    }

    /**
     * @param restBetweenPatterns
     *            the restBetweenPatterns to set
     */
    public void setRestBetweenPatterns(final double restBetweenPatterns) {
        this.restBetweenPatterns = restBetweenPatterns;
    }

    /**
     * @param reverse
     *            the reverse to set
     */
    public void setReverse(final boolean reverse) {
        this.reverse = reverse;
    }

    /**
     * @param startPitch
     *            the startPitch to set
     */
    public void setStartPitch(final int startPitch) {
        this.startPitch = startPitch;
    }

    public void setUpAndDown(final boolean upAndDown) {
        this.upAndDown = upAndDown;
    }
}
