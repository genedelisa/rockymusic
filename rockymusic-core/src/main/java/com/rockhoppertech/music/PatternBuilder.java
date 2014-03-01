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

import java.util.Arrays;
import java.util.List;

import com.google.common.primitives.Ints;
import com.rockhoppertech.music.Pitch;

/**
 * Josh Bloch's pattern and not the GoF pattern. Modified a bit though.
 * <p>
 * At a minimum, you need to specify the degrees and pattern.
 * 
 * <pre>
 * @{code
  Pattern p = PatternBuilder.create()
                .degrees(new int[] { 1, 2, 3 })
                .pattern(new int[] { 1, 2, 3 })
                .startPitch(Pitch.C5)
                .duration(Duration.E)
                .numOctaves(2)
                .restBetweenPatterns(Duration.SIXTEENTH_NOTE)
                .upAndDown(true)
                .build();
 }
 * </pre>
 * 
 *
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 */
public class PatternBuilder {

    private int[] degrees;
    private int[] pattern;
    private int startPitch = Pitch.C5;
    private int numOctaves = 1;
    private double duration = Duration.Q;
    private boolean reverse = false;
    private boolean upAndDown = false;
    private double restBetweenPatterns;

    /**
     * The way to get a builder.
     * 
     * @return the {@code PatternBuilder}
     */
    public static PatternBuilder create() {
        return new PatternBuilder();
    }

    /**
     * If you don't use the create method.
     * 
     * @param degrees
     *            the degrees
     * @param pattern
     *            the pattern
     */
    public PatternBuilder(int[] degrees, int[] pattern) {
        this.degrees = Arrays.copyOf(degrees, degrees.length);
        this.pattern = Arrays.copyOf(pattern, pattern.length);
    }

    public PatternBuilder(List<Integer> d, int[] pattern) {

        this.degrees = new int[d.size()];
        for (int i = 0; i < degrees.length; i++) {
            degrees[i] = d.get(i);
        }
        this.pattern = Arrays.copyOf(pattern, pattern.length);
    }

    public PatternBuilder() {

    }

    /**
     * The final method to call in the chain. Actually builds the pattern.
     * 
     * @return the {@code Pattern}
     */
    public Pattern build() {
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

    /**
     * Called by build after the pattern is built.
     */
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

    /**
     * Set the degrees to use. Makes a copy.
     * 
     * @param degrees
     *            an array of degrees
     * @return this to cascade
     */
    public PatternBuilder degrees(int[] degrees) {
        this.degrees = Arrays.copyOf(degrees, degrees.length);
        return this;
    }

    public PatternBuilder degrees(List<Integer> degreeList) {
        int[] a = Ints.toArray(degreeList);
        this.degrees = Arrays.copyOf(a, a.length);
        return this;
    }

    /**
     * Set the pattern to use. Makes a copy.
     * 
     * @param pattern
     *            an array of ints
     * @return this to cascade
     */
    public PatternBuilder pattern(int[] pattern) {
        this.pattern = Arrays.copyOf(pattern, pattern.length);
        return this;
    }

    public PatternBuilder pattern(List<Integer> patternList) {
        int[] a = Ints.toArray(patternList);
        this.pattern = Arrays.copyOf(a, a.length);
        return this;
    }

    public PatternBuilder startPitch(int startPitch) {
        this.startPitch = startPitch;
        return this;
    }

    public PatternBuilder numOctaves(int numOctaves) {
        this.numOctaves = numOctaves;
        return this;
    }

    public PatternBuilder duration(double duration) {
        this.duration = duration;
        return this;
    }

    public PatternBuilder restBetweenPatterns(double restBetweenPatterns) {
        this.restBetweenPatterns = restBetweenPatterns;
        return this;
    }

    public PatternBuilder reverse(boolean reverse) {
        this.reverse = reverse;
        return this;
    }

    public PatternBuilder upAndDown(boolean upAndDown) {
        this.upAndDown = upAndDown;
        return this;
    }
}
