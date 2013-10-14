/**
 * 
 */
package com.rockhoppertech.music.midi.js;

import java.io.Serializable;
import java.util.Scanner;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Track;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Serializable in order to DnD notelists.
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 *
 */
@SuppressWarnings("serial")
public class TimeSignature implements Serializable {
	 private static final Logger logger = LoggerFactory
				.getLogger(TimeSignature.class);
	    
    private int numerator;
    private int denominator;

    /**
    *
    */
    public TimeSignature() {
        this.numerator = 4;
        this.denominator = 4;
    }

    /**
    *
    * @param numerator
    * @param denominator
    */
    public TimeSignature(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public TimeSignature(String ts) {
		Scanner s = new Scanner(ts);
		s.useDelimiter("/");
		this.numerator = s.nextInt();
		this.denominator = s.nextInt();
	}

	/**
     * @return the numerator
     */
    public int getNumerator() {
        return this.numerator;
    }

    /**
     * @param numerator the numerator to set
     */
    public void setNumerator(int numerator) {
        this.numerator = numerator;
    }

    /**
     * @return the denominator
     */
    public int getDenominator() {
        return this.denominator;
    }

    /**
     * @param denominator the denominator to set
     */
    public void setDenominator(int denominator) {
        this.denominator = denominator;
    }

    /* 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getName());
        sb.append(" numerator=").append(this.numerator);
        sb.append(" denominator=").append(this.denominator);
        return sb.toString();
    }

	public String getDisplayName() {
		return String.format("%d/%d", this.numerator, this.denominator);
	}
	
	  public void addToTrack(Track track, long tick) {
	        MetaMessage mm = this.getMIDITimeSignature();
	        MidiEvent event = new MidiEvent(mm, tick);
	        track.add(event);
	    }

	    public MetaMessage getMIDITimeSignature() {
	        MetaMessage message = null;
	        try {
	            message = new MetaMessage();
	            byte[] a = new byte[2];
	            //TODO fix this
//	            a[0] = (byte) this.sf;
//	            a[1] = (byte) this.mm;
	            message.setMessage(0x59, a, a.length);
	        } catch (InvalidMidiDataException e) {
	            System.err.println(e);
	        }
	        return message;
	    }
}
