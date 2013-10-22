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
import java.util.NavigableMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="http://rockhoppertech.com/">Gene De Lisa</a>
 * 
 */
public class Score implements Iterable<MIDITrack> {
    private static final Logger logger = LoggerFactory.getLogger(Score.class);
    private String name;
    private List<MIDITrack> tracks;
    private int resolution = 256;
    private MIDITrack metaTrack;

    public Score() {
        tracks = new ArrayList<>();
        metaTrack = new MIDITrack();
        // if the score's name is set, then this name is overwritten
        metaTrack.setName("meta");
        tracks.add(metaTrack);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Score Name:").append(name).append('\n');
        for (MIDITrack t : tracks) {
//            sb.append("Track Name: ").append(t.getName()).append('\n');
//            for (MIDINote n : t.getNotes()) {
//                sb.append(n).append('\n');
//            }
//            for (MIDIEvent n : t.getEvents()) {
//                sb.append(n).append('\n');
//                sb.append(n.toReadableString()).append('\n');
//            }
//
//            NavigableMap<Double, KeySignature> keys = t.getKeySignatures();
//            for (Double time : keys.keySet()) {
//                KeySignature key = keys.get(time);
//                sb.append("Key: ").append(key).append(" at time ").append(time).append('\n');                
//            }
//
//            NavigableMap<Double, TimeSignature> timeSigs = t
//                    .getTimeSignatures();
//            for (Double time : timeSigs.keySet()) {
//                TimeSignature ts = timeSigs.get(time);
//                sb.append("Time signature: ").append(ts).append(" at time ").append(time).append('\n');                                
//            }
//
//            NavigableMap<Double, Integer> tempoMap = t.getTempoMap();
//            for (Double time : tempoMap.keySet()) {
//                Integer tempo = tempoMap.get(time);
//                sb.append("Tempo: ").append(tempo).append(" at time ").append(time).append('\n');                
//            }

             sb.append(t.toString());
        }
        return sb.toString();
    }

    public Score add(MIDITrack track) {
        tracks.add(track);
        logger.debug("added track. ntracks is now {}", tracks.size());
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        metaTrack.setName(this.name);
    }

    public List<MIDITrack> getTracks() {
        return tracks;
    }

    public void setTracks(List<MIDITrack> tracks) {
        this.tracks = tracks;
    }

    @Override
    public Iterator<MIDITrack> iterator() {
        return tracks.iterator();
    }

    public int getResolution() {
        return resolution;
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    public void play() {
        // Sequence sequence = ScoreFactory.scoreToSequence(this);
        MIDIPerformer perf = new MIDIPerformer();
        perf.play(this);
    }

}
