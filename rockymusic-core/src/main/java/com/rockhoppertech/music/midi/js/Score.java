package com.rockhoppertech.music.midi.js;

/*
 * #%L
 * Rocky Music Core
 * %%
 * Copyright (C) 1996 - 2013 Rockhopper Technologies
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Score instance represents a standard MIDI file format 1.
 * 
 * There is a collection of MIDITracks, which will map to JavaSound Tracks.
 * 
 * The zeroth track is for meta messages such as tempo, key signature, time
 * signature.
 * 
 * The name of the Score will be the sequence name in the MIDI file.
 * 
 * @author <a href="http://rockhoppertech.com/">Gene De Lisa</a>
 * 
 */
public class Score implements Iterable<MIDITrack>, Serializable {
    /**
     * For Serializaiton.
     */
    private static final long serialVersionUID = -4003934288960105262L;
    /**
     * for logging.
     */
    private static final Logger logger = LoggerFactory.getLogger(Score.class);
    /**
     * The name of the score. It will be written as the Sequence name in the
     * MIDI file.
     */
    private String name;
    /**
     * The tracks.
     */
    private List<MIDITrack> tracks;
    /**
     * The resolution of the MIDI file.
     */
    private int resolution = 256;
    /**
     * The zeroth track in the score for meta messages.
     */
    private MIDITrack metaTrack;

    /**
     * Initializes the Score instance and adds the meta track.
     */
    public Score() {
        // if the score's name is set, then this name is overwritten
        this("meta");
    }

    /**
     * Initializes the Score instance and adds the meta track.
     * 
     * @param scoreName
     *            the name of the score
     */
    public Score(final String scoreName) {
        tracks = new ArrayList<>();
        metaTrack = new MIDITrack();
        metaTrack.setName(scoreName);
        tracks.add(metaTrack);
    }

    /**
     * The usual. Delegates to the MIDITrack's toString.
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Score Name:").append(name).append('\n');
        sb.append("Resolution:").append(resolution).append('\n');
        for (MIDITrack t : tracks) {
            sb.append(t.toString());
        }
        return sb.toString();
    }

    /**
     * Adds a track to the Score.
     * 
     * @param track
     *            the track to add
     * @return the Score for a fluent interface
     */
    public final Score add(final MIDITrack track) {
        tracks.add(track);
        track.setScore(this);
        logger.debug("added track. ntracks is now {}", tracks.size());
        return this;
    }

    /**
     * Find a track with the specified name.
     * 
     * @param trackName
     *            name of the track
     * @return a {@code MIDITrack} or null
     */
    public MIDITrack getTrackWithName(final String trackName) {
        MIDITrack track = null;
        for (MIDITrack t : this) {
            logger.debug("comparing '{}' with '{}'", t.getName(), trackName);
            if (t.getName().equals(trackName)) {
                track = t;
                break;
            }
        }
        return track;
    }

    /**
     * Removes a track from the Score.
     * 
     * @param track
     *            the track to remove.
     * @return the Score for a fluent interface
     */
    public final Score remove(final MIDITrack track) {
        tracks.remove(track);
        track.setScore(null);
        logger.debug("removed track. ntracks is now {}", tracks.size());
        return this;
    }

    /**
     * Removes the track from the Score. This does not check if the index is
     * valid.
     * 
     * @param index
     *            the index of the track.
     * @return the Score for a fluent interface
     */
    public final Score remove(final int index) {
        MIDITrack track = tracks.remove(index);
        track.setScore(null);

        logger.debug(
                "removed track at index {}. ntracks is now {}",
                index,
                tracks.size());
        return this;
    }

    /**
     * @return the Score's name
     */
    public final String getName() {
        return name;
    }

    /**
     * @param name
     *            the score name
     */
    public final void setName(final String name) {
        this.name = name;
        metaTrack.setName(this.name);
    }

    /**
     * @return the tracks. Not a copy.
     */
    public final List<MIDITrack> getTracks() {
        return tracks;
    }

    /**
     * Creates a copy of the tracks.
     * 
     * @param tracks
     *            the tracks
     */
    public void setTracks(final List<MIDITrack> tracks) {
        this.tracks.clear();
        for (MIDITrack track : tracks) {
            this.add(new MIDITrack(track));
        }
    }

    /**
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public final Iterator<MIDITrack> iterator() {
        return tracks.iterator();
    }

    /**
     * @return the resolution
     */
    public int getResolution() {
        return resolution;
    }

    /**
     * @param resolution
     *            the MIDI file's resolution
     */
    public final void setResolution(int resolution) {
        this.resolution = resolution;
    }

    /**
     * Creates a MIDIPerformer and plays the Score.
     */
    public final void play() {
        // Sequence sequence = ScoreFactory.scoreToSequence(this);
        MIDIPerformer perf = new MIDIPerformer();
        perf.play(this);
    }

    /**
     * @return the meta track
     */
    public MIDITrack getMetaTrack() {
        return this.metaTrack;
    }

    /**
     * Add tempo to the meta track.
     * 
     * @param beat
     *            the beat
     * @param bpm
     *            beats per minute
     * @return this instance
     */
    public Score setTempoAtBeat(final double beat, final int bpm) {
        this.metaTrack.addTempoAtBeat(beat, bpm);
        return this;
    }

    /**
     * Add a key signature to the meta track.
     * 
     * @param beat
     *            the beat
     * @param key
     *            the key
     * @return this instance
     */
    public Score setKeySignatureAtBeat(final double beat,
            final KeySignature key) {
        this.metaTrack.addKeySignatureAtBeat(beat, key);
        return this;
    }

    /**
     * Add a time signature to the meta track.
     * 
     * @param beat
     *            the beat
     * @param numerator
     *            the time signature numerator
     * @param denominator
     *            the time signature denominator
     * @return this instance
     */
    public Score setTimeSignatureAtBeat(final double beat, final int numerator,
            final int denominator) {
        this.metaTrack.addTimeSignatureAtBeat(beat, new TimeSignature(
                numerator, denominator));
        return this;
    }

}
