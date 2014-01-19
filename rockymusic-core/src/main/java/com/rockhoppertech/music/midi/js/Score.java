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
public class Score implements Iterable<MIDITrack> {
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
     */
    public Score(String scoreName) {
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
        logger.debug("added track. ntracks is now {}", tracks.size());
        return this;
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
        tracks.remove(index);
        logger.debug(
                "removed track at index {}. ntracks is now {}",
                index,
                tracks.size());
        return this;
    }

    /**
     * @return the Score's name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(final String name) {
        this.name = name;
        metaTrack.setName(this.name);
    }

    /**
     * @return the tracks. Not a copy.
     */
    public List<MIDITrack> getTracks() {
        return tracks;
    }

    /**
     * @param tracks
     */
    public void setTracks(final List<MIDITrack> tracks) {
        this.tracks = tracks;
    }

    /**
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<MIDITrack> iterator() {
        return tracks.iterator();
    }

    /**
     * @return
     */
    public int getResolution() {
        return resolution;
    }

    /**
     * @param resolution
     *            the MIDI file's resolution
     */
    public void setResolution(int resolution) {
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

}
