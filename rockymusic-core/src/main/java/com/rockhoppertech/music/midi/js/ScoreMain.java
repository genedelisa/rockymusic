package com.rockhoppertech.music.midi.js;

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequence;

import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.midi.parse.MIDIStringParser;

import static com.rockhoppertech.music.Pitch.*;

public class ScoreMain {

	public static void main(String[] args) {

		create();
		play();

		// readMIDIFile();
	}

	static void readMIDIFile() {
		// String filename =
		// "src/test/resources/midifiles/Bb34eighthsascending.mid";
		// String filename = "src/test/resources/midifiles/sib-gmajortest.mid";
		// multi keys, time sigs
		// String filename = "src/test/resources/midifiles/sib-e-minor.mid";
		String filename = "src/test/resources/midifiles/multiKeySigs.mid";
		Score score=null;
		try {
			score = ScoreFactory.readSequence(filename);
		} catch (InvalidMidiDataException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(score);
	}

	static void read() {
		String filename = "test.mid";
		Score score = null;
		try {
			score = ScoreFactory.readSequence(filename);
		} catch (InvalidMidiDataException | IOException e) {
			e.printStackTrace();
			return;
		}
		System.out.println(score);

		Sequence sequence = ScoreFactory.scoreToSequence(score);
		if(sequence == null) {
			System.err.println("oops");
			return;
		}
		MIDIPerformer perf = new MIDIPerformer();
		perf.play(sequence);

		ScoreFactory.writeToMIDIFile("foo.mid", score);
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
		ScoreFactory.writeToMIDIFile(filename, score);
		Score score2 = null;
		try {
			score2 = ScoreFactory.readSequence(filename);
		} catch (InvalidMidiDataException | IOException e) {
			e.printStackTrace();
		}
		System.out.println("read back");
		System.out.println(score2);
		score.play();
	}

	static void play() {
		Score score = new Score();
		MIDITrack track = new MIDITrack();
		score.add(track);
		MIDINote note = new MIDINote.Builder().build();
		track.add(note);
		note = new MIDINote.Builder().pitch(Pitch.D5).build();
		track.append(note);

		// append changes the start beats. Use add if you want the specified start beat.
		// with import static com.rockhoppertech.music.Pitch.*;
		// you can say just E5 instead of Pitch.E5
		track.add(
				new MIDINote.Builder()
						.pitch(E5)
						.startBeat(2.5)
						.duration(.5)
						.build())
				.add(
						new MIDINote.Builder()
								.pitch(F5)
								.startBeat(6.5)
								.duration(1.5)
								.velocity(90)
								.build());

		track.append(
				new MIDINote(Pitch.G5)).append(
				new MIDINote(Pitch.A5));

		track.append(Pitch.G5).append(Pitch.A5);
		String ps = MIDIStringParser.createStringBrief(track);
		System.out.println(ps);
		//C5,1.0,1.0 D5,2.0,1.0 E5,2.5,0.5 F5,6.5,1.5 G5,8.0,1.0 A5,9.0,1.0 G5,10.0,1.0 A5,11.0,1.0 



		MIDIStringParser parser = new MIDIStringParser();
		// S+ puts it into append mode, so start beats are sequential
		// otherwise, they would all have the same start beat.
		String str = "S+ C5, D, E, F, G, A, B, C6";
		track = parser.parseString(str);

		track.play();
	}
}
