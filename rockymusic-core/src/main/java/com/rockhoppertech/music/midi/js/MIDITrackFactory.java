package com.rockhoppertech.music.midi.js;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class MIDITrackFactory {
	private static final Logger logger = LoggerFactory
			.getLogger(MIDITrackFactory.class);

	public static Sequence trackToSequence(MIDITrack mt, int resolution) {
		Sequence sequence = null;
		try {
			sequence = new Sequence(Sequence.PPQ, resolution);
			Track track = trackFromMIDITrack(mt, sequence);

			// MIDIUtils.insertTempo(track, 0, 128);
			if (mt.getName() != null) {
				MIDIUtils.insertSequenceName(track, 0, mt.getName());
			}

		} catch (InvalidMidiDataException e) {
			logger.error(e.getMessage(), e);
		}
		return sequence;
	}

	/**
	 * <code>trackFromMIDITrack</code> creates a new Track that belongs to the
	 * supplied Sequence. The MIDITRack is iterated and <code>MidiEvent</code>
	 * objects (patch, note on/off, eotrack) are created and placed into the
	 * Track.
	 * 
	 * @param sequence
	 *            a JavaSound <code>Sequence</code> instance
	 * 
	 * @return a JavaSound <code>Track</code> instance
	 */
	public static Track trackFromMIDITrack(MIDITrack mt, Sequence sequence) {
		logger.debug("Enter toTrack");

		if (sequence.getDivisionType() == Sequence.PPQ) {
			logger.debug("ppq: ");
		}

		logger.debug("sequence resolution: " + sequence.getResolution());
		int resolution = sequence.getResolution();

		Track track = sequence.createTrack();
		if (mt.getName() != null) {
			MIDIUtils.insertSequenceName(track, 0, mt.getName());

		}

		/*
		 * in the Java Sound API, the ticks aren't delta values; they're the
		 * previous event's time value plus the delta value. In other words, in
		 * the Java Sound API the timing value for each event is always greater
		 * than that of the previous event in the sequence (or equal, if the
		 * events are supposed to be simultaneous). Each event's timing value
		 * measures the time elapsed since the beginning of the sequence.
		 */
		for (MIDIEvent event : mt.getEvents()) {
			// events are based on beats in this way
			MidiEvent sun = event.toMidiEvent(resolution);
			// MidiMessage sunmess = sun.getMessage();
			track.add(sun);
		}

		int bank = -1;
		int program = -1;
		long tick = -1;
		for (MIDINote n : mt) {

			ShortMessage se = null;
			try {
				int p = n.getProgram();
				int b = n.getBank();
				if (p != program || b != bank) {
					if (logger.isDebugEnabled()) {
						logger.debug(String.format(
								"old new program %d %d bank %d %d for note %s",
								program,
								p,
								bank,
								b,
								n));
					}
					program = p;
					bank = b;

					se = new ShortMessage();
					se.setMessage(
							ShortMessage.CONTROL_CHANGE + n.getChannel(),
							n.getChannel(),
							0, // bank select
							n.getBank());
					// event ticks are 0 based but Notes are 1 based.
					// Musicicans don't say "start at the zeroth beat"
					tick = (long) ((n.getStartBeat() - 1) * resolution);
					MidiEvent event = new MidiEvent(se, tick);

					track.add(event);

					se = new ShortMessage();
					se.setMessage(
							ShortMessage.PROGRAM_CHANGE,
							n.getChannel(),
							n.getProgram(),
							n.getBank());
					tick = (long) ((n.getStartBeat() - 1) * resolution);
					event = new MidiEvent(se, tick);
					track.add(event);

				}

				// the on message
				se = new ShortMessage();
				se.setMessage(ShortMessage.NOTE_ON + n.getChannel(), n
						.getPitch().getMidiNumber(), n.getVelocity());
				// event ticks are 0 based but Notes are 1 based.
				// Musicicans don't say "start at the zeroth beat"
				tick = (long) ((n.getStartBeat() - 1) * resolution);
				MidiEvent event = new MidiEvent(se, tick);
				// this is zero based: (long) (n.getStartBeat() * division));
				track.add(event);
				logger.info(String.format(
						"sb %f tick %d %s",
						n.getStartBeat(),
						tick,
						MIDIUtils.toString(se)));

				// now the off message
				se = new ShortMessage();
				se.setMessage(ShortMessage.NOTE_ON + n.getChannel(), n
						.getPitch().getMidiNumber(), 0); // velocity of 0
				// means off
				tick = (long) ((n.getStartBeat() - 1 + n.getDuration()) * resolution);
				logger.info(String.format(
						"sb %f tick %d %s",
						n.getStartBeat(),
						tick,
						MIDIUtils.toString(se)));

				event = new MidiEvent(se, tick);
				track.add(event);
			} catch (InvalidMidiDataException e) {
				System.err.println(e);
				MIDIUtils.print(se);
			}
		}

		// logger.debug("Adding end of track message");
		// try {
		// MetaMessage me = new MetaMessage();
		// me.setMessage(0x2F, new byte[] { 0 }, 1);
		// MidiEvent event = new MidiEvent(me, track.ticks());
		// track.add(event);
		// } catch (InvalidMidiDataException e) {
		// System.err.println(e);
		// }
		logger.debug("leaving toTrack");
		return track;
	}

	public Score createChannelizedMIDITracks(Track track, int resolution) {
		List<List<MidiEvent>> channelList = new ArrayList<List<MidiEvent>>(16);
		for (int i = 0; i < 16; i++) {
			channelList.add(i, new ArrayList<MidiEvent>());
		}
		trackToChannelList(channelList, track);
		trimChannelList(channelList);
		logChannelList(channelList);

		Score score = new Score();
		score.setResolution(resolution);
		// go through each channel create a track for each channel
		for (List<MidiEvent> list : channelList) {
			MIDITrack mtrack = eventsToMIDITrack(list, resolution);
			score.add(mtrack);
		}
		return score;
	}

	/**
	 * Add a new MIDITrack to the Score for each channel in the JavaSound Track.
	 * 
	 * @param score
	 * @param track
	 * @return
	 */
	public Score createChannelizedMIDITracks(Score score, Track track) {
		List<List<MidiEvent>> channelList = new ArrayList<List<MidiEvent>>(16);
		for (int i = 0; i < 16; i++) {
			channelList.add(i, new ArrayList<MidiEvent>());
		}
		trackToChannelList(channelList, track);
		trimChannelList(channelList);
		logChannelList(channelList);

		int resolution = score.getResolution();
		// go through each channel create a track for each channel
		for (List<MidiEvent> list : channelList) {
			MIDITrack mtrack = eventsToMIDITrack(list, resolution);
			score.add(mtrack);
		}
		return score;
	}

	/**
	 * Describe <code>logChannelList</code> method here.
	 * 
	 * @param channelList
	 *            a <code>List</code> value
	 */
	private static void logChannelList(List<List<MidiEvent>> channelList) {
		if (logger.isDebugEnabled()) {
			logger.debug("Dumping channel list");

			for (List<MidiEvent> elist : channelList) {
				for (MidiEvent me : elist) {
					logger.debug(MIDIUtils.toString(me));
				}
			}
			/*
			 * for (Iterator i = channelList.iterator(); i.hasNext();) {
			 * List<MidiEvent> elist = (List<MidiEvent>) i.next();
			 * logger.debug("Channel list"); if (elist != null &&
			 * elist.isEmpty() == false) { int elsize = elist.size(); for (int j
			 * = 0; j < elsize; j++) { MidiEvent se = (MidiEvent) elist.get(j);
			 * logger.debug(MidiUtils.toString(se)); } } }
			 */

		}
	}

	/**
	 * <code>trackToChannelList</code> puts all <code>ShortMessages</code> into
	 * individual lists by channel.
	 * 
	 * @param channelList
	 *            a <code>List</code> containing the channel lists. The results
	 *            are put in this list of lists.
	 * @param t
	 *            a JavaSound <code>Track</code>
	 */
	private static void trackToChannelList(List<List<MidiEvent>> channelList,
			Track t) {
		int ts = t.size();
		for (int i = 0; i < ts; i++) {
			MidiEvent me = t.get(i);
			MidiMessage mm = me.getMessage();
			if (mm instanceof ShortMessage) {
				ShortMessage se = (ShortMessage) mm;
				int chan = se.getChannel();
				List<MidiEvent> elist = null;
				if ((elist = (ArrayList<MidiEvent>) channelList.get(chan)) == null) {
					// this shouldn't happen
					logger.debug("null list, creating new for " + chan);
					elist = new ArrayList<MidiEvent>();
					channelList.add(chan, elist);
				}
				logger.debug("adding " + MIDIUtils.toString(me));
				elist.add(me);
			}
		}
	}

	/**
	 * <code>trimChannelList</code> removes the empty lists
	 * 
	 * @param channelList
	 *            a <code>List</code> value
	 */
	private static void trimChannelList(List<List<MidiEvent>> channelList) {

		for (Iterator<List<MidiEvent>> iterator = channelList.iterator(); iterator
				.hasNext();) {
			List<MidiEvent> list = iterator.next();
			if (list.isEmpty() == true) {
				iterator.remove();
			}
		}
		// remove as above. otherwise you get a concurrent modification problem

		// synchronized (channelList) {
		// for (List<MidiEvent> list : channelList) {
		// if (list.isEmpty() == true) {
		// channelList.remove(list);
		// }
		// }
		// }
	}

	/**
	 * <code>eventsToNotelist</code> turns a collection of MidiEvents containing
	 * ShortMessages only (e.g. no sysex) into a notelist. The Note Messages are
	 * placed in notes as <code>MIDINotes</code>, other messages wrapped and are
	 * placed in a separate list - the instance variable <code>events</code>
	 * 
	 * <p>
	 * You might get the division like this: double div = (double)
	 * sequence.getResolution();
	 * </p>
	 * 
	 * @param t
	 *            a <code>MIDITrack</code> result
	 * @param events
	 *            a <code>List<MidiEvent></code> to be converted
	 * @param resolution
	 *            a <code>int</code> sequence PPQ resolution aka division
	 */
	public static MIDITrack trackToMIDITrack(Track t, int resolution) {
		List<MidiEvent> events = new ArrayList<>();

		int ts = t.size();
		for (int i = 0; i < ts; i++) {
			MidiEvent me = t.get(i);
			// MidiMessage mm = me.getMessage();
			// if (mm instanceof ShortMessage) {
			events.add(me);
			logger.debug("adding " + MIDIUtils.toString(me));
			// }
		}
		// List<MetaMessage> textevents = MIDIUtils.getMetaTextEvents(t);
		// for(MetaMessage me: textevents) {
		// MIDIUtils.print(me);
		// }

		MIDITrack track = eventsToMIDITrack(events, resolution);
		return track;
	}

	private static MIDITrack eventsToMIDITrack(List<MidiEvent> events,
			int resolution) {
		MIDITrack track = new MIDITrack();
		double div = resolution;
		int program = 0;
		// int channel = 0;
		// need to match note ons with note offs.
		Map<Integer, MidiEvent> outstanding = new HashMap<Integer, MidiEvent>();

		for (MidiEvent me : events) {
			
			if (me.getMessage() instanceof MetaMessage) {
				MetaMessage mm = (MetaMessage) me.getMessage();
				if(mm.getType() == MIDIUtils.META_TEXT) {
					logger.debug("meta text");
					logger.debug(new String(mm.getData()));
				}
				if(mm.getType() == MIDIUtils.META_TIME_SIG) {
					logger.debug("time sig");
					logger.debug(MIDIUtils.getTimeSignature(mm));
				}
				if(mm.getType() == MIDIUtils.META_COPYRIGHT) {
					logger.debug("copyright");
					logger.debug(new String(mm.getData()));					
				}
				if(mm.getType() == MIDIUtils.META_KEY_SIG) {
					logger.debug("Key signature");
					logger.debug(MIDIUtils.getTimeSignature(mm));					
					
					
				}
				if(mm.getType() == MIDIUtils.META_TEMPO) {
					
				}
				// In a format 1 midifile, track 0 will contain the sequence name.
				// in Sibelius, this would be the title
				// Track 1 will be the instrument name, e.g. Piano
				if(mm.getType() == MIDIUtils.META_NAME) {
					logger.debug("name");
					logger.debug(new String(mm.getData()));					
					track.setName(new String(mm.getData()));										
				}
			}

			if (me.getMessage() instanceof ShortMessage) {

				ShortMessage se = (ShortMessage) me.getMessage();

				// ShortMessage se = (ShortMessage) i.next();
				int command = se.getCommand();
				int chan = se.getChannel();
				int d1 = se.getData1();
				int d2 = se.getData2();
				long tick = me.getTick();

				logger.debug("Command:" + Integer.toHexString(command));
				logger.debug(MIDIUtils.toString(se));
				if (command == ShortMessage.CONTROL_CHANGE) {
					if (logger.isDebugEnabled()) {
						logger.debug("control change " + se);
					}
					track.add(new MIDIEvent(me));

				}
				if (command == ShortMessage.CHANNEL_PRESSURE) {
					if (logger.isDebugEnabled()) {
						logger.debug("channel pressure " + se);
					}
					track.add(new MIDIEvent(me));
				}
				if (command == ShortMessage.POLY_PRESSURE) {
					if (logger.isDebugEnabled()) {
						logger.debug("poly pressure " + se);
					}
					track.add(new MIDIEvent(me));
				}

				if (command == ShortMessage.PROGRAM_CHANGE) {
					logger.debug("patch:" + program);
					program = d1;
					track.add(new MIDIEvent(me));
				}
				if (command == ShortMessage.PITCH_BEND) {
					logger.debug("pitch bend:" + d1 + " " + d2);
					track.add(new MIDIEvent(me));
				}

				if (command == ShortMessage.NOTE_ON && d2 != 0) {
					logger.debug("Note on " + d1 + " tick " + tick);
					outstanding.put(new Integer(d1), me);
				}

				if ((command == ShortMessage.NOTE_ON && d2 == 0)
						|| command == ShortMessage.NOTE_OFF) {
					logger.debug("Note off " + d1 + " tick " + tick);
					// find the event with the same d1
					if (outstanding.isEmpty()) {
						logger.error("no outstanding:");
						continue;
					}
					me = (MidiEvent) outstanding.remove(new Integer(d1));
					ShortMessage out = null;
					if (me != null) {
						out = (ShortMessage) me.getMessage();
					}

					if (out != null) {
						long tickdelta = tick - me.getTick();
						double duration = tickdelta / div;
						double start = (double) me.getTick();
						if (start != 0d) {
							start = start / div;
						}
						if (logger.isDebugEnabled()) {
							logger.debug("Note off tick delta:" + tickdelta);
							logger.debug("Note off start:" + start);
							logger.debug("Note off duration:" + duration);
						}
						// add one to start since Events are 0 based but Notes
						// are 1
						// based.
						MIDINote n = new MIDINote(d1, start + 1, duration,
								chan, out.getData2(), program);
						track.add(n);
					} else {
						if (logger.isErrorEnabled()) {
							logger.error("Note off no match:");
						}
					}
				} // end of note (noteoff or noteone+0vel)
			}

		}
		return track;
	}
}
