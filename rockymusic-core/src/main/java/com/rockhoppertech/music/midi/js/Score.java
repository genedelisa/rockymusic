package com.rockhoppertech.music.midi.js;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
		this.tracks = new ArrayList<>();
		this.metaTrack = new MIDITrack();
		// if the score's name is set, then this name is overwritten
		this.metaTrack.setName("meta");
		this.tracks.add(metaTrack);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Score Name:").append(this.name).append('\n');
		for (MIDITrack t : tracks) {
			sb.append("Track Name: ").append(t.getName()).append('\n');
			for (MIDINote n : t.getNotes()) {
				sb.append(n).append('\n');
			}
			for (MIDIEvent n : t.getEvents()) {
				sb.append(n).append('\n');
				sb.append(n.toReadableString()).append('\n');				
			}
		}
		return sb.toString();
	}

	public Score add(MIDITrack track) {
		this.tracks.add(track);
		logger.debug("added track. ntracks is now {}", this.tracks.size());
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.metaTrack.setName(this.name);
	}

	public List<MIDITrack> getTracks() {
		return tracks;
	}

	public void setTracks(List<MIDITrack> tracks) {
		this.tracks = tracks;
	}

	@Override
	public Iterator<MIDITrack> iterator() {
		return this.tracks.iterator();
	}

	public int getResolution() {
		return this.resolution;
	}

	public void setResolution(int resolution) {
		this.resolution = resolution;
	}

}
