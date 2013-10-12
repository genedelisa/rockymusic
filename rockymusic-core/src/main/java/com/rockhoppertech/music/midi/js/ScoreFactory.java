package com.rockhoppertech.music.midi.js;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiFileFormat;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Patch;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="http://rockhoppertech.com/">Gene De Lisa</a>
 * 
 */
public class ScoreFactory {
	private static final Logger logger = LoggerFactory
			.getLogger(ScoreFactory.class);

	public static Score readSequence(String filename) {

		Score score = new Score();
		if (filename == null) {
			filename = "test.mid";
		}

		Sequence sequence = MIDIUtils.read(filename);
		MidiFileFormat fileFormat = null;
		try {
			fileFormat = MidiSystem.getMidiFileFormat(new File(filename));
		} catch (InvalidMidiDataException | IOException e) {
			e.printStackTrace();
		}

		Track[] tracks = sequence.getTracks();
		logger.debug(String.format(
				"there are %d tracks in midifile %s",
				tracks.length,
				filename));
		MIDITrack track;
		if (sequence.getDivisionType() != Sequence.PPQ) {
			logger.error("I don't grok non-PPQ sequences");
			// oops
		}
		int resolution = sequence.getResolution();

		if (fileFormat.getType() == 0) {
			logger.debug("{} is a MIDI file type 0", filename);
			logger.debug("{} tracks", tracks.length);			
			// well, it should be just one track
			for (int i = 0; i < tracks.length; i++) {
				track = MIDITrackFactory
						.trackToMIDITrack(tracks[i], resolution);
				logger.debug(String.format(
						"track %d len %d",
						i,
						tracks[i].size()));
				for (int j = 0; j < tracks[i].size(); j++) {
					logger.debug(MIDIUtils.toString(tracks[i].get(j)));
				}
				score.add(track);
			}
			
		} else if (fileFormat.getType() == 1) {
			logger.debug("{} is a MIDI file type 1", filename);
			logger.debug("{} tracks", tracks.length);						
			Track metaTrack = tracks[0];
			extractMetaData(score, metaTrack);
			
			for (int i = 1; i < tracks.length; i++) {
				track = MIDITrackFactory
						.trackToMIDITrack(tracks[i], resolution);
				logger.debug(String.format(
						"track %d len %d",
						i,
						tracks[i].size()));
				for (int j = 0; j < tracks[i].size(); j++) {
					logger.debug(MIDIUtils.toString(tracks[i].get(j)));
				}
				score.add(track);
			}
		}

		for (Patch p : sequence.getPatchList()) {
			logger.debug(String.format(
					"Sequence has patch on bank %d program %d ",
					p.getBank(),
					p.getProgram()));
		}
		return score;
	}

	private static void extractMetaData(Score score, Track metaTrack) {
		score.setName(MIDIUtils.getSequenceTrackName(metaTrack));
		logger.debug("Extracting meta data for {}", score.getName());
		
		//TODO do something with the rest of these guys
		
		List<MidiEvent> allmeta = MIDIUtils.getMetaMessages(metaTrack);
		logger.debug("there are {} meta messages", allmeta.size());
		for(MidiEvent me: allmeta) {
			logger.debug("meta {} at tick {}",MIDIUtils.toString(me.getMessage()), me.getTick());
		}
		
		
		List<MidiEvent> keysigs = MIDIUtils.getKeySignatures(metaTrack);
		logger.debug("there are {} keysigs", keysigs.size());
		for(MidiEvent me: keysigs) {
			logger.debug("key sig {}",MIDIUtils.toString(me.getMessage()));
		}
		
		List<MidiEvent> timesigs = MIDIUtils.getTimeSignatures(metaTrack);
		logger.debug("there are {} timesigs", timesigs.size());
		for(MidiEvent me: timesigs) {
			logger.debug("timesig {} at tick {}",MIDIUtils.toString(me.getMessage()), me.getTick());			
		}
		
		List<MetaMessage> mm = MIDIUtils.getMetaTextEvents(metaTrack);
		for(MetaMessage m : mm) {
			logger.debug("{} metatext", MIDIUtils.toString(m));
		}
	}

	/**
	 * Create a JavaSound Sequence instance from the Score.
	 * 
	 * @param score
	 * @param resolution
	 *            in PPQ
	 * @return the Sequence
	 */
	public static Sequence scoreToSequence(Score score, int resolution) {
		Sequence sequence = null;
		// scores have a list of MIDITracks
		try {
			sequence = new Sequence(Sequence.PPQ, resolution);

			for (MIDITrack t : score) {
				// convert the MIDITracks to Tracks
				MIDITrackFactory.trackFromMIDITrack(t, sequence);
			}

			// MIDIUtils.insertTempo(track, 0, 128);
			// if (this.name != null) {
			// MIDIUtils.insertSequenceName(track, 0, this.getName());
			// }

		} catch (InvalidMidiDataException e) {
			logger.error(e.getMessage(), e);
		}
		return sequence;
	}

	public static Sequence writeToMIDIFile(String fileName, Score score,
			int resolution) {
		Sequence s = scoreToSequence(score, resolution);
		MIDIUtils.write(s, fileName);
		return s;
	}

	/**
	 * <code>toTrack</code> creates a new Track that belongs to the supplied
	 * Sequence. The MIDITRack is iterated and <code>MidiEvent</code> objects
	 * (patch, note on/off, eotrack) are created and placed into the Track.
	 * 
	 * @param sequence
	 *            a JavaSound <code>Sequence</code> instance
	 * 
	 * @return a JavaSound <code>Track</code> instance
	 */
	// public static Track trackFromMIDITrack(MIDITrack mt, Sequence sequence) {
	// logger.debug("Enter toTrack");
	//
	// if (sequence.getDivisionType() == Sequence.PPQ) {
	// logger.debug("ppq: ");
	// }
	//
	// logger.debug("sequence resolution: " + sequence.getResolution());
	// int resolution = sequence.getResolution();
	//
	// Track track = sequence.createTrack();
	// if (mt.getName() != null) {
	// MIDIUtils.insertSequenceName(track, 0, mt.getName());
	//
	// }
	//
	// /*
	// * in the Java Sound API, the ticks aren't delta values; they're the
	// * previous event's time value plus the delta value. In other words, in
	// * the Java Sound API the timing value for each event is always greater
	// * than that of the previous event in the sequence (or equal, if the
	// * events are supposed to be simultaneous). Each event's timing value
	// * measures the time elapsed since the beginning of the sequence.
	// */
	// for (MIDIEvent event : mt.getEvents()) {
	// // events are based on beats in this way
	// MidiEvent sun = event.toMidiEvent(resolution);
	// // MidiMessage sunmess = sun.getMessage();
	// track.add(sun);
	// }
	//
	// int bank = -1;
	// int program = -1;
	// long tick = -1;
	// for (MIDINote n : mt) {
	//
	// ShortMessage se = null;
	// try {
	// int p = n.getProgram();
	// int b = n.getBank();
	// if (p != program || b != bank) {
	// if (logger.isDebugEnabled()) {
	// logger.debug(String.format(
	// "old new program %d %d bank %d %d for note %s",
	// program, p, bank, b, n));
	// }
	// program = p;
	// bank = b;
	//
	// se = new ShortMessage();
	// se.setMessage(ShortMessage.CONTROL_CHANGE + n.getChannel(),
	// n.getChannel(), 0, // bank select
	// n.getBank());
	// // event ticks are 0 based but Notes are 1 based.
	// // Musicicans don't say "start at the zeroth beat"
	// tick = (long) ((n.getStartBeat() - 1) * resolution);
	// MidiEvent event = new MidiEvent(se, tick);
	//
	// track.add(event);
	//
	// se = new ShortMessage();
	// se.setMessage(ShortMessage.PROGRAM_CHANGE, n.getChannel(),
	// n.getProgram(), n.getBank());
	// tick = (long) ((n.getStartBeat() - 1) * resolution);
	// event = new MidiEvent(se, tick);
	// track.add(event);
	//
	// }
	//
	// // the on message
	// se = new ShortMessage();
	// se.setMessage(ShortMessage.NOTE_ON + n.getChannel(), n
	// .getPitch().getMidiNumber(), n.getVelocity());
	// // event ticks are 0 based but Notes are 1 based.
	// // Musicicans don't say "start at the zeroth beat"
	// tick = (long) ((n.getStartBeat() - 1) * resolution);
	// MidiEvent event = new MidiEvent(se, tick);
	// // this is zero based: (long) (n.getStartBeat() * division));
	// track.add(event);
	// logger.info(String.format("sb %f tick %d %s", n.getStartBeat(),
	// tick, MIDIUtils.toString(se)));
	//
	// // now the off message
	// se = new ShortMessage();
	// se.setMessage(ShortMessage.NOTE_ON + n.getChannel(), n
	// .getPitch().getMidiNumber(), 0); // velocity of 0
	// // means off
	// tick = (long) ((n.getStartBeat() - 1 + n.getDuration()) * resolution);
	// logger.info(String.format("sb %f tick %d %s", n.getStartBeat(),
	// tick, MIDIUtils.toString(se)));
	//
	// event = new MidiEvent(se, tick);
	// track.add(event);
	// } catch (InvalidMidiDataException e) {
	// System.err.println(e);
	// MIDIUtils.print(se);
	// }
	// }
	//
	// // logger.debug("Adding end of track message");
	// // try {
	// // MetaMessage me = new MetaMessage();
	// // me.setMessage(0x2F, new byte[] { 0 }, 1);
	// // MidiEvent event = new MidiEvent(me, track.ticks());
	// // track.add(event);
	// // } catch (InvalidMidiDataException e) {
	// // System.err.println(e);
	// // }
	// logger.debug("leaving toTrack");
	// return track;
	// }

	/**
	 * @param div
	 * @param track
	 */
	// public void fromTrack(int div, Track track) {
	// List<List<MidiEvent>> channelList = new ArrayList<List<MidiEvent>>(16);
	// for (int i = 0; i < 16; i++) {
	// channelList.add(i, new ArrayList<MidiEvent>());
	// }
	// trackToChannelList(channelList, track);
	// trimChannelList(channelList);
	// logChannelList(channelList);
	//
	// // go through each channel create a notelist for each channel
	// for (List<MidiEvent> list : channelList) {
	// MIDITrack mtrack = eventsToMIDITrack(list, div);
	// //this.add(mtrack);
	// }
	//
	// }

}
