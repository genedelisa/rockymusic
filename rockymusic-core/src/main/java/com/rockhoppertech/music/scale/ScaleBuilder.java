package com.rockhoppertech.music.scale;

import java.util.Arrays;

import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.midi.js.MIDITrack;

/**
 * Create a {@code MIDITrack} from a {@code Scale}.
 * 
 * <pre>
 * {@code
  MIDITrack track = ScaleBuilder.create()
      .name("Major")
      .root(Pitch.C5)
      .track()
      .sequential();
          
          }
  </pre>
 * 
 * @author gene
 * 
 */
public class ScaleBuilder {
    private String name;
    private int rootMidiNum = Pitch.C5;
    private int[] intervals;
    private double startBeat = 1d;
    private double duration = 4d;
    private boolean upAndDown;
    private int nOct = 1;

    /**
     * Should be the first call. Creates an instance of the builder.
     * 
     * @return the builder
     */
    public static ScaleBuilder create() {
        return new ScaleBuilder();
    }

    /**
     * Usually the final method. But this builder will create either a
     * {@code Scale} or a {@code MIDITrack}. This is actually no easier than
     * using the {@code ScaleFactory}.
     * 
     * @return a {@code Scale}
     */
    public Scale build() {
        Scale result = ScaleFactory.createFromName(name);
        reset();
        return result;
    }

    /**
     * Final call in the chain to build the track. You set either the scale
     * name, or the intervals. The preference if you set both is to use the
     * scale name.
     * 
     * @return a new {@code MIDITrack}
     */
    public MIDITrack track() {
        MIDITrack result = null;
        Scale scale = null;
        // result = createMIDITrack(name);
        // result = createMIDITrack(name, rootMidiNum);
        // result = createMIDITrack(scale);

        if (name != null) {
            scale = ScaleFactory.createFromName(name);
            result = ScaleFactory.createMIDITrack(
                    scale,
                    rootMidiNum,
                    startBeat,
                    duration,
                    upAndDown,
                    nOct);
            reset();
            return result;
        }

        result = ScaleFactory.createMIDITrack(
                intervals,
                rootMidiNum,
                startBeat,
                duration,
                nOct,
                upAndDown);
        result.setName(this.name);

        reset();
        return result;
    }

    public ScaleBuilder name(String s) {
        name = s;
        return this;
    }

    public ScaleBuilder root(int n) {
        rootMidiNum = n;
        return this;
    }

    public ScaleBuilder numberOfOctaves(int n) {
        this.nOct = n;
        return this;
    }

    public ScaleBuilder intervals(int[] n) {
        this.intervals = Arrays.copyOf(n, n.length);
        return this;
    }

    public ScaleBuilder startBeat(double n) {
        this.startBeat = n;
        return this;
    }

    public ScaleBuilder duration(double n) {
        this.duration = n;
        return this;
    }

    public ScaleBuilder upAndDown(boolean b) {
        this.upAndDown = b;
        return this;
    }

    private void reset() {
        name = null;
        rootMidiNum = Pitch.C5;
        startBeat = 1d;
        duration = 4d;
        upAndDown = false;
        nOct = 1;
    }
}
