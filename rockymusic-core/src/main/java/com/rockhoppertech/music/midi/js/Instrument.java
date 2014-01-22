package com.rockhoppertech.music.midi.js;

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

import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.midi.gm.MIDIGMPatch;

/**
 * Instruments contain a GM patch, and a range.
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class Instrument {

    /**
     * 
     */
    public static final Instrument PIANO = new Instrument(MIDIGMPatch.PIANO,
            MIDIGMPatch.PIANO.getName(), Pitch.A1, Pitch.C8);

    public static final Instrument VIOLIN = new Instrument(MIDIGMPatch.VIOLIN,
            MIDIGMPatch.VIOLIN.getName(), Pitch.G4, Pitch.A8);

    public static final Instrument VIOLA = new Instrument(MIDIGMPatch.VIOLA,
            MIDIGMPatch.VIOLA.getName(), Pitch.C4, Pitch.E7);

    public static final Instrument CELLO = new Instrument(MIDIGMPatch.CELLO,
            MIDIGMPatch.CELLO.getName(), Pitch.C3, Pitch.C7);

    public static final Instrument CONTRABASS = new Instrument(
            MIDIGMPatch.CONTRABASS,
            MIDIGMPatch.CONTRABASS.getName(), Pitch.C3, Pitch.C6);

    public static final Instrument HARP = new Instrument(MIDIGMPatch.HARP,
            MIDIGMPatch.HARP.getName(), Pitch.B2, Pitch.GS8);

    // TODO do the rest
    // http://en.wikipedia.org/wiki/Range_(music)

    // should I reference java sound's Instrument class?
    // Soundbank soundbank = synth.getDefaultSoundbank();
    // Instrument[] instr = soundbank.getInstruments();
    
    /**
     * 
     */
    private MIDIGMPatch patch;
    /**
     * 
     */
    private String name;
    /**
     * 
     */
    private int minPitch;
    /**
     * 
     */
    private int maxPitch;

    /**
     * 
     */
    public Instrument() {

    }

    /**
     * @param patch
     * @param name
     * @param minPitch
     * @param maxPitch
     */
    public Instrument(MIDIGMPatch patch, String name, int minPitch, int maxPitch) {
        this.patch = patch;
        this.name = name;
        this.minPitch = minPitch;
        this.maxPitch = maxPitch;
    }

    /**
     * @param pitch
     * @return
     */
    public boolean isPitchInRange(int pitch) {
        return (pitch >= this.minPitch && pitch <= this.maxPitch);
    }

    /**
     * @param pitch
     * @return
     */
    public boolean isPitchInRange(Pitch pitch) {
        return (pitch.getMidiNumber() >= this.minPitch && pitch.getMidiNumber() <= this.maxPitch);
    }

    /**
     * see Effective Java
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + maxPitch;
        result = prime * result + minPitch;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((patch == null) ? 0 : patch.hashCode());
        return result;
    }

    /*
     * 
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Instrument other = (Instrument) obj;
        if (maxPitch != other.maxPitch) {
            return false;
        }
        if (minPitch != other.minPitch) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (patch != other.patch) {
            return false;
        }
        return true;
    }

    /*
     * 
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Instrument [patch=").append(patch).append(", name=")
                .append(name).append(", minPitch=").append(minPitch)
                .append(", maxPitch=").append(maxPitch).append("]");
        return builder.toString();
    }

    /**
     * @return the patch
     */
    public MIDIGMPatch getPatch() {
        return patch;
    }

    /**
     * @param patch
     *            the patch to set
     */
    public void setPatch(MIDIGMPatch patch) {
        this.patch = patch;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the minPitch
     */
    public int getMinPitch() {
        return minPitch;
    }

    /**
     * @param minPitch
     *            the minPitch to set
     */
    public void setMinPitch(int minPitch) {
        this.minPitch = minPitch;
    }

    /**
     * @return the maxPitch
     */
    public int getMaxPitch() {
        return maxPitch;
    }

    /**
     * @param maxPitch
     *            the maxPitch to set
     */
    public void setMaxPitch(int maxPitch) {
        this.maxPitch = maxPitch;
    }

}
