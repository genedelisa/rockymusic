package com.rockhoppertech.music.chord;

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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.rockhoppertech.music.PitchFormat;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.scale.Scale;
import com.rockhoppertech.music.scale.ScaleFactory;

/**
 * A sequence of chords. Can use a format similar to fake books.
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class ChordProgression implements Iterable<Chord> {
    private static final Logger logger = LoggerFactory
            .getLogger(ChordFactory.class);

    /**
     * The data.
     */
    private List<Chord> chords = new ArrayList<Chord>();

    /**
     * 
     */
    private String name = "Untitled";

    /**
     * The default key.
     */
    private String key = "C";

    /**
     * The default scale.
     */
    private String scaleName = "Major";

    /**
     * The default beats per measure.
     */
    private int beatsPerMeasure = 4;

    public ChordProgression() {
    }

    public ChordProgression(final List<Chord> chords) {
        this.chords = Lists.newArrayList(chords);
        // this.chords = chords;
    }

    public ChordProgression(final List<Chord> chords, final String name) {
        this(chords);
        this.name = name;
    }

    /**
     * Append a chord to the progression.
     * 
     * @param chord
     * @return this to allow cascading
     */
    public ChordProgression add(final Chord chord) {
        this.chords.add(chord);
        return this;
    }

    public static ChordProgression create(final File file)
            throws UnknownChordException {
        return ChordProgressionParser.createFromFile(file);
    }

    /**
     * Input string can be either roman or std.
     * 
     * @param input
     * @return ChordProgression
     * @throws UnknownChordException
     */
    public static ChordProgression create(final String input)
            throws UnknownChordException {
        return ChordProgressionParser.create(input);
    }

    public static ChordProgression createFromNames(final String input)
            throws UnknownChordException {
        return ChordProgressionParser.chordsFromStdNames(input);
    }

    /**
     * Specify roman numerals for chords. The default scale and key is C major.
     * 
     * @param input
     *            roman numberals
     * @return a progression based on the input
     */
    public static ChordProgression createFromRoman(final String input) {
        return ChordProgressionParser.chordsFromRoman(input);
    }

    public static String toRoman(final ChordProgression chordProgression,
            final String key, final Scale scale, final int beatsPerMeasure) {
        final StringBuilder sb = new StringBuilder();
        double beat = 1d;
        for (final Chord chord : chordProgression) {
            // String ps = PitchFormat.getPitchString(chord.getRoot());
            // String roman = RomanChordParser.pitchNameToRoman(scale, ps, key);
            final String roman = scale.getRoman(key, chord);

            // String sym = chord.getSymbol();
            if (beat > beatsPerMeasure) {
                beat = 1d;
                sb.append('|');
            }
            sb.append(roman).append(' ');

            // if (sym.equals("maj") || sym.equals("")) {
            // sb.append(roman).append(' ');
            // } else {
            // sb.append(roman).append(' ');
            // //sb.append(roman).append(chord.getSymbol()).append(' ');
            // }

            final double chordDur = chord.getDuration();
            if (ChordProgression.logger.isDebugEnabled()) {
                ChordProgression.logger.debug(String.format(
                        "beat %f chord dur %f", beat, chordDur));
            }
            if (chordDur > 1d) {
                for (int i = 0; i < chordDur - 1; i++) {
                    sb.append("/ ");
                }
            }

            beat += chordDur;
        }
        return sb.toString().trim();
    }

    /**
     * Used by the transferhandler.
     * 
     * @param chordProgression
     * @param beatsPerMeasure
     * @return
     */
    public static String asString(final ChordProgression chordProgression,
            final int beatsPerMeasure) {
        final StringBuilder sb = new StringBuilder();
        double beat = 1d;
        sb.append("key:").append(chordProgression.getKey()).append("\n");
        sb.append("beatsPerMeasure:").append(
                chordProgression.getBeatsPerMeasure()).append("\n");
        for (final Chord chord : chordProgression) {
            if (beat > beatsPerMeasure) {
                beat = 1d;
                sb.append('|');
            }
            sb.append(chord.getDisplayName()).append(' ');
            final double chordDur = chord.getDuration();
            if (ChordProgression.logger.isDebugEnabled()) {
                ChordProgression.logger.debug(String.format(
                        "beat %f chord dur %f", beat, chordDur));
            }
            if (chordDur > 1d) {
                for (int i = 0; i < chordDur - 1; i++) {
                    sb.append("/ ");
                }
            }
            beat += chordDur;
        }
        return sb.toString().trim();
    }

    public double closeTimeline() {
        return this.closeTimeline(0d);
    }

    /**
     * Removes beats between chords.
     * 
     * @param gap
     *            duration between chords
     */
    public double closeTimeline(final double gap) {
        final int size = this.chords.size();
        final Chord n = this.chords.get(0);
        double s = n.getStartBeat();
        final double begin = s;
        double d = n.getDuration();
        double total = d - s;
        for (int i = 1; i < size; i++) {
            final Chord nn = this.chords.get(i);
            nn.setStartBeat(s + d + gap);
            s = nn.getStartBeat();
            d = nn.getDuration();
            total = (s + d) - begin;
        }
        return total;
    }

    public Chord get(final int index) {
        return this.chords.get(index);
    }

    public int getBeatsPerMeasure() {
        return this.beatsPerMeasure;
    }

    /**
     * Return the first Chord >= parameter after
     * 
     * @param after
     *            a start beat
     * @return a Chord or null
     */
    public Chord getChordAtBeat(final double after) {
        logger.debug("looking for  sb {}", after);
        for (final Chord n : this) {
            final double s = n.getStartBeat();
            final double e = n.getEndBeat();
            logger.debug("checking sb {} of chord {}", s, n);
            Range<Double> range = Range.between(s, e - .0001);
            logger.debug("range {}", range);
            if (range.contains(after)) {
                logger.debug("returning chord {}", n);
                return n;
            }

            // if (s >= after) {
            // logger.debug("matched chord {}", n);
            // return n;
            // }
        }
        return null;
    }

    public String getKey() {
        return this.key;
    }

    public int getLength() {
        return this.chords.size();
    }

    public MIDITrack createMIDITrack() {
        if (this.chords != null) {
            final MIDITrack notelist = ChordProgressionParser
                    .createMIDITrack(this.chords);
            return notelist;
        }
        return null;
    }

    public String getName() {
        return this.name;
    }

    public String getScaleName() {
        return this.scaleName;
    }

    public boolean insertAtIndex(final int index,
            final ChordProgression chordProgression) {
        return this.chords.addAll(index, chordProgression.chords);
    }

    public boolean isEmpty() {
        return this.chords.isEmpty();
    }

    @Override
    public Iterator<Chord> iterator() {
        return this.chords.iterator();
    }

    public void play() {
        final MIDITrack notelist = this.createMIDITrack();
        if (notelist != null) {
            notelist.play();
        }
    }

    /**
     * Removes the chord but leaves the timeline intact.
     * 
     * @param chord
     * @return boolean
     * @see ChordProgression#closeTimeline()
     */
    public boolean remove(final Chord chord) {
        return this.chords.remove(chord);
    }

    public Chord remove(final int index) {
        return this.chords.remove(index);
    }

    public Chord set(final int index, final Chord chord) {
        return this.chords.set(index, chord);
    }

    public void setBeatsPerMeasure(final int beatsPerMeasure) {
        this.beatsPerMeasure = beatsPerMeasure;
    }

    public void setChords(final List<Chord> chords) {
        this.chords = chords;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setScaleName(final String scaleName) {
        this.scaleName = scaleName;
    }

    public void sortByStartBeat() {
        if (this.chords == null) {
            return;
        }
        final Comparator<Chord> c = new Comparator<Chord>() {
            @Override
            public int compare(final Chord o1, final Chord o2) {
                if (o1.getStartBeat() == o2.getStartBeat()) {
                    return 0;
                }
                if (o1.getStartBeat() < o2.getStartBeat()) {
                    return -1;
                }
                if (o1.getStartBeat() > o2.getStartBeat()) {
                    return 1;
                }
                return 0;
            }
        };
        Collections.sort(this.chords, c);
    }

    public String toRoman(final boolean omitSymbol) {
        final StringBuilder sb = new StringBuilder();
        double beat = 1d;
        final Scale scale = ScaleFactory.getScaleByName(this.scaleName);

        for (final Chord chord : this) {
            final String roman = scale.getRoman(this.key, chord, omitSymbol);
            if (beat > this.beatsPerMeasure) {
                beat = 1d;
                sb.append('|');
            }
            sb.append(roman).append(' ');

            // if (sym.equals("maj") || sym.equals("")) {
            // sb.append(roman).append(' ');
            // } else {
            // sb.append(roman).append(' ');
            // //sb.append(roman).append(chord.getSymbol()).append(' ');
            // }

            final double chordDur = chord.getDuration();
            if (ChordProgression.logger.isDebugEnabled()) {
                ChordProgression.logger.debug(String.format(
                        "beat %f chord dur %f", beat, chordDur));
            }
            if (chordDur > 1d) {
                for (int i = 0; i < chordDur - 1; i++) {
                    sb.append("/ ");
                }
            }
            beat += chordDur;
        }
        sb.append("|");
        return sb.toString().trim();
    }

    public String toHTML() {
        return toHTML(4);
    }

    public String toHTML(int measuresPerRow) {
        StringBuilder sb = new StringBuilder();
        String std = toStd(false, true);
        String[] a = std.split("[.]* \\|");
        // String[] a = std.split("[A-G][#sbf]?[m|maj]? |");
        int count = 0;
        sb.append("<html>\n");
        sb.append("<head>\n");
        sb.append(String.format("<title>%s</title>\n", this.getName()));
        sb.append("</head>\n");
        sb.append("<body>\n");
        sb.append(String.format("<p>%s</p>\n", this.getName()));
        sb.append(String.format("<p>%s %s</p>\n", this.getKey(), this
                .getScaleName()));
        sb.append(String.format("<p>%d</p>\n", this.getBeatsPerMeasure()));
        sb
                .append("<table border=\"1\" cellpadding=\"4\" summary=\"Chord Progression\">\n");
        sb.append("<tbody>\n");
        for (String s : a) {
            if (count == 0) {
                sb.append("<tr>");
            }

            sb.append("<td>");
            // the regexp eats the |
            sb.append(s.trim());
            // sb.append(s.trim()).append('|');
            sb.append("</td>");

            if (!(++count < measuresPerRow)) {
                sb.append("</tr>\n");
                count = 0;
            }
        }
        sb.append("</tr>");
        sb.append("\n</tbody></table>\n");
        sb.append("</body></html>");
        return sb.toString();
    }

    public String toRomanRows(int measuresPerRow) {
        String newline = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("name:%s%s", this.getName(), newline));
        sb.append(String.format("key:%s %s%s", this.getKey(), this
                .getScaleName(), newline));
        sb.append(String.format(
                "beatsPerMeasure:%d%s",
                this.getBeatsPerMeasure(),
                newline));

        String std = toRoman(false);
        String[] a = std.split("[.]* \\|");
        // String[] a = std.split("[A-G][#sbf]?[m|maj]? |");
        int count = 0;
        for (String s : a) {
            // the regexp eats the |
            sb.append(s.trim()).append('|');
            if (!(++count < measuresPerRow)) {
                count = 0;
                sb.append(newline);
            }
        }
        sb.append(newline);
        return sb.toString();
    }

    /**
     * Standard chord names but broken into the specified number of measures per
     * row.
     * 
     * @param measuresPerRow
     * @return
     */
    public String toStdRows(int measuresPerRow) {
        String newline = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("name:%s%s", this.getName(), newline));
        sb.append(String.format("key:%s %s%s", this.getKey(), this
                .getScaleName(), newline));
        sb.append(String.format(
                "beatsPerMeasure:%d%s",
                this.getBeatsPerMeasure(),
                newline));

        String std = toStd(false, true);
        String[] a = std.split("[.]* \\|");
        // String[] a = std.split("[A-G][#sbf]?[m|maj]? |");
        int count = 0;
        for (String s : a) {
            // the regexp eats the |
            sb.append(s.trim()).append('|');
            if (!(++count < measuresPerRow)) {
                count = 0;
                sb.append(newline);
            }
        }
        sb.append(newline);
        return sb.toString();
    }

    public String toStd() {
        return toStd(false, true);
    }

    public String toStd(final boolean omitSymbol) {
        return toStd(omitSymbol, true);
    }

    /**
     * The toString values for each chord for debugging.
     * 
     * @return s string
     */
    public String toChordStrings() {
        final StringBuilder sb = new StringBuilder();
        for (final Chord chord : this) {
            sb.append(chord.toString()).append('\n');
        }
        return sb.toString();
    }

    public String toStd(final boolean omitSymbol, final boolean useSlashes) {
        final StringBuilder sb = new StringBuilder();
        double beat = 1d;

        for (final Chord chord : this) {
            if (beat > this.beatsPerMeasure) {
                beat = 1d;
                sb.append("| ");
            }
            if (omitSymbol) {
                sb.append(PitchFormat.getPitchString(chord.getRoot())).append(
                        ' ');
            } else {
                sb.append(chord.getDisplayName()).append(' ');
            }
            final double chordDur = chord.getDuration();
            if (ChordProgression.logger.isDebugEnabled()) {
                ChordProgression.logger.debug(String.format(
                        "beat %f chord dur %f", beat, chordDur));
            }
            if (chordDur > 1d) {
                if (chordDur > this.beatsPerMeasure) {
                    for (int len = 0; len < chordDur; len += this.beatsPerMeasure) {
                        if (useSlashes) {
                            for (int i = 0; i < this.beatsPerMeasure - 1; i++) {
                                sb.append("/ ");
                            }
                        }
                        if (logger.isDebugEnabled()) {
                            logger.debug(String.format("len %d chordDur %f",
                                    len, chordDur));
                        }
                        if (len < chordDur - beatsPerMeasure) {
                            sb.append("| ");
                            sb.append(chord.getDisplayName()).append(' ');
                        }
                        beat = 1d;
                    }
                } else {
                    if (useSlashes) {
                        for (int i = 0; i < chordDur - 1; i++) {
                            sb.append("/ ");
                        }
                    }
                }
            }
            beat += chordDur;
        }
        sb.append("|");
        return sb.toString().trim();
    }

    @Override
    public String toString() {
        return this.toStd(false);
    }

    public double getEndBeat() {
        double endBeat = 1d;
        for (Chord chord : this.chords) {
            double d = chord.getEndBeat();
            logger.debug("end beat of chord {}", chord);
            logger.debug("end beat {}", d);
            if (d > endBeat) {
                endBeat = d;
            }
        }
        return endBeat;
        // Chord last = this.chords.get(chords.size() - 1);
        // return last.getStartBeat() + last.getDuration();
    }

    public ChordProgression append(ChordProgression chordProgression) {
        this.closeTimeline();
        chordProgression.closeTimeline();
        double startBeat = this.getEndBeat();

        // avoid a concurrent modification exception
        if (this == chordProgression) {
            List<Chord> list = new ArrayList<Chord>();
            list.addAll(this.chords);
            for (Chord c : chordProgression.chords) {
                Chord dupe = new Chord(c);
                dupe.setStartBeat(startBeat);
                list.add(dupe);
                startBeat += dupe.getDuration();
            }
            this.setChords(list);
            return this;
        }

        for (Chord c : chordProgression.chords) {
            Chord dupe = new Chord(c);
            dupe.setStartBeat(startBeat);
            this.add(dupe);
            startBeat += dupe.getDuration();
        }
        return this;
    }

}
