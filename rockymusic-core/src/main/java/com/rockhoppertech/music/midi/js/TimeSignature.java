/**
 * 
 */
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
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
@SuppressWarnings("serial")
public class TimeSignature implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6234631269571034107L;

	private static final Logger logger = LoggerFactory
			.getLogger(TimeSignature.class);

	private int numerator;
	private int denominator;

	/**
    *
    */
	public TimeSignature() {
		numerator = 4;
		denominator = 4;
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
		numerator = s.nextInt();
		denominator = s.nextInt();
	}

	/**
	 * @return the numerator
	 */
	public int getNumerator() {
		return numerator;
	}

	/**
	 * @param numerator
	 *            the numerator to set
	 */
	public void setNumerator(int numerator) {
		this.numerator = numerator;
	}

	/**
	 * @return the denominator
	 */
	public int getDenominator() {
		return denominator;
	}

	/**
	 * @param denominator
	 *            the denominator to set
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
		sb.append(" numerator=").append(numerator);
		sb.append(" denominator=").append(denominator);
		return sb.toString();
	}

	public String getDisplayName() {
		return String.format("%d/%d", numerator, denominator);
	}

	public void addToTrack(Track track, long tick) {
		MetaMessage mm = getMIDITimeSignature();
		MidiEvent event = new MidiEvent(mm, tick);
		track.add(event);
	}

	public MetaMessage getMIDITimeSignature() {
		MetaMessage message = null;
		try {
			message = new MetaMessage();
			byte[] a = new byte[2];
			// TODO fix this
			// a[0] = (byte) this.sf;
			// a[1] = (byte) this.mm;
			message.setMessage(0x59, a, a.length);
		} catch (InvalidMidiDataException e) {
			System.err.println(e);
		}
		return message;
	}
}
