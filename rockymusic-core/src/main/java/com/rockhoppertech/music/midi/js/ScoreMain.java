package com.rockhoppertech.music.midi.js;

import javax.sound.midi.Sequence;

import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.midi.parse.MIDIStringParser;

public class ScoreMain {

	public static void main(String[] args) {

		create();
		
		//readMIDIFile();
	}
	static void readMIDIFile() {
		//String filename = "src/test/resources/midifiles/Bb34eighthsascending.mid";
		//String filename = "src/test/resources/midifiles/sib-gmajortest.mid";
		// multi keys, time sigs
		//String filename = "src/test/resources/midifiles/sib-e-minor.mid";		
		String filename = "src/test/resources/midifiles/multiKeySigs.mid";				
		Score score = ScoreFactory.readSequence(filename);
		System.out.println(score);
	}
	
	static void read() {
		String filename = "test.mid";
		Score score = ScoreFactory.readSequence(filename);
		System.out.println(score);

		int resolution = 480;
		Sequence sequence = ScoreFactory.scoreToSequence(score, resolution);
		MIDIPerformer perf = new MIDIPerformer();
		perf.play(sequence);

		ScoreFactory.writeToMIDIFile("foo.mid", score, resolution);
	}
	
	static void create() {
		Score score = new Score();
		score.setName("create test");
		MIDITrack track = new MIDITrack();
		track.setName("Piano");
		score.add(track);
		track.add(Pitch.C5);
		System.out.println("score");
		System.out.println(score);
		System.out.println("writing");
		String filename = "src/test/resources/midifiles/created.mid";						
		ScoreFactory.writeToMIDIFile(filename, score, 256);
		Score score2 = ScoreFactory.readSequence(filename);
		System.out.println("read back");		
		System.out.println(score2);
	}

	static void play() {
		Score score = new Score();
		MIDITrack track = new MIDITrack();
		score.add(track);
		MIDINote note = new MIDINote.Builder().build();
		track.add(note);
		note = new MIDINote.Builder().pitch(Pitch.D5).build();
		track.append(note);

		track.append(new MIDINote.Builder().pitch(Pitch.E5).build()).append(
				new MIDINote.Builder().pitch(Pitch.F5).build());

		track.append(new MIDINote(Pitch.G5)).append(new MIDINote(Pitch.A5));

		track.append(Pitch.G5).append(Pitch.A5);

		MIDIStringParser parser = new MIDIStringParser();
		// S+ puts it into append mode, so start beats are sequential
		// otherwise, they would all have the same start beat.
		String str = "S+ C5, D, E, F, G, A, B, C6";
		track = parser.parseString(str);

		Sequence sequence = MIDITrackFactory.trackToSequence(track, 480);
		MIDIPerformer perf = new MIDIPerformer();
		perf.play(sequence);

	}
}
