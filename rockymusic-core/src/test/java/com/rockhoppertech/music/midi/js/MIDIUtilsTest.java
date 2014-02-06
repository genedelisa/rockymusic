/**
 * 
 */
package com.rockhoppertech.music.midi.js;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class MIDIUtilsTest {
    static Logger logger = LoggerFactory.getLogger(MIDIUtilsTest.class);

    private Track createTrack() {
        Track track = null;
        try {
            Sequence sequence = new Sequence(Sequence.PPQ, 480);
            track = sequence.createTrack();
        } catch (InvalidMidiDataException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return track;
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#getGMReset()}.
     */
    @Test
    public final void testGetGMReset() {
        byte[] bytes = MIDIUtils.getGMReset();
        assertThat("Not null",
                bytes, notNullValue());
        logger.debug("bytes\n{}", bytes);

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#addEndOfTrack(javax.sound.midi.Track)}
     * .
     */
    @Test
    public final void testAddEndOfTrack() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#appendControlChange(javax.sound.midi.Track, int, int, int)}
     * .
     */
    @Test
    public final void testAppendControlChange() {
        Track track = createTrack();
        assertThat("Not null",
                track, notNullValue());
        int channel = 4;
        MIDIUtils.appendControlChange(
                track,
                channel,
                MIDIControllers.CC_PAN_COARSE,
                64);
        // the insert we just did plus the eot message.
        assertThat("the track size is correct",
                track.size(), is(equalTo(2)));

        MidiEvent event = track.get(0);
        assertThat("event is not null",
                event, is(notNullValue()));
        MidiMessage message = event.getMessage();
        // mask off the channel
        int status = message.getStatus() & 0xF0;
        if (status == ShortMessage.CONTROL_CHANGE) {
            logger.debug("got control change");
        }
        assertThat("the status is correct",
                status, is(equalTo(ShortMessage.CONTROL_CHANGE)));

        int db1 = message.getMessage()[1];
        assertThat("the status is correct",
                db1, is(equalTo(MIDIControllers.CC_PAN_COARSE)));

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#appendNote(javax.sound.midi.Track, int, int, int, long)}
     * .
     */
    @Test
    public final void testAppendNote() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#appendProgramChange(javax.sound.midi.Track, int, int)}
     * .
     */
    @Test
    public final void testAppendProgramChange() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#textToBytes(java.lang.String)}
     * .
     */
    @Test
    public final void testTextToBytes() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#bytesToText(byte[])}.
     */
    @Test
    public final void testBytesToText() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#createMetaTextMessage(long, int, java.lang.String)}
     * .
     */
    @Test
    public final void testCreateMetaTextMessage() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#createShortMessage(int, byte[])}
     * .
     */
    @Test
    public final void testCreateShortMessage() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#filter(javax.sound.midi.Sequence, javax.sound.midi.Track, com.rockhoppertech.music.midi.js.MIDIEventFilter)}
     * .
     */
    @Test
    public final void testFilter() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#getCommandName(int)}.
     */
    @Test
    public final void testGetCommandName() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#getMetaTextEvents(javax.sound.midi.Track)}
     * .
     */
    @Test
    public final void testGetMetaTextEvents() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#getMetaMessages(javax.sound.midi.Track)}
     * .
     */
    @Test
    public final void testGetMetaMessages() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#getKeySignatures(javax.sound.midi.Track)}
     * .
     */
    @Test
    public final void testGetKeySignatures() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#getTimeSignatures(javax.sound.midi.Track)}
     * .
     */
    @Test
    public final void testGetTimeSignatures() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#getTempi(javax.sound.midi.Track)}
     * .
     */
    @Test
    public final void testGetTempi() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#getSequencer()}.
     */
    @Test
    public final void testGetSequencer() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#getSequencer(javax.sound.midi.MidiDevice.Info)}
     * .
     */
    @Test
    public final void testGetSequencerInfo() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#getSequencer(javax.sound.midi.Synthesizer)}
     * .
     */
    @Test
    public final void testGetSequencerSynthesizer() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#getSequenceTrackName(javax.sound.midi.MetaMessage)}
     * .
     */
    @Test
    public final void testGetSequenceTrackNameMetaMessage() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#getSequenceTrackName(javax.sound.midi.Track)}
     * .
     */
    @Test
    public final void testGetSequenceTrackNameTrack() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#getSynthList()}.
     */
    @Test
    public final void testGetSynthList() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#getTimeSignature(javax.sound.midi.MetaMessage)}
     * .
     */
    @Test
    public final void testGetTimeSignature() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#insertControlChange(javax.sound.midi.Track, long, int, int, int)}
     * .
     */
    @Test
    public final void testInsertControlChange() {
        Track track = createTrack();
        assertThat("Not null",
                track, notNullValue());
        int channel = 4;
        long tick = 480;
        MIDIUtils.insertControlChange(
                track,
                tick,
                channel,
                MIDIControllers.CC_PAN_COARSE,
                64);
        // the insert we just did plus the eot message.
        assertThat("the track size is correct",
                track.size(), is(equalTo(2)));

        MidiEvent event = track.get(0);
        assertThat("event is not null",
                event, is(notNullValue()));
        assertThat("the tick is correct",
                event.getTick(), is(equalTo(tick)));

        MidiMessage message = event.getMessage();
        // mask off the channel
        int status = message.getStatus() & 0xF0;
        if (status == ShortMessage.CONTROL_CHANGE) {
            logger.debug("got control change");
        }
        assertThat("the status is correct",
                status, is(equalTo(ShortMessage.CONTROL_CHANGE)));

        int db1 = message.getMessage()[1];
        assertThat("the status is correct",
                db1, is(equalTo(MIDIControllers.CC_PAN_COARSE)));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#insertCopyright(javax.sound.midi.Track, long, java.lang.String)}
     * .
     */
    @Test
    public final void testInsertCopyright() {
        Track track = createTrack();
        assertThat("Not null",
                track, notNullValue());

        long tick = 480;
        String copyrightText = "copyright 2014";
        MIDIUtils.insertCopyright(
                track,
                tick,
                copyrightText);
        // the insert we just did plus the eot message.
        assertThat("the track size is correct",
                track.size(), is(equalTo(2)));

        MidiEvent event = track.get(0);
        assertThat("event is not null",
                event, is(notNullValue()));
        assertThat("the tick is correct",
                event.getTick(), is(equalTo(tick)));

        MidiMessage message = event.getMessage();
        if (message instanceof MetaMessage) {
            MetaMessage meta = (MetaMessage) message;
            if (meta.getType() == MIDIUtils.META_COPYRIGHT) {
                String text = MIDIUtils.bytesToText(meta.getData());
                assertThat("the text is correct",
                        text, is(equalTo(copyrightText)));
            }
        }
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#insertDeviceName(javax.sound.midi.Track, long, java.lang.String)}
     * .
     */
    @Test
    public final void testInsertDeviceName() {
        Track track = createTrack();
        assertThat("Not null",
                track, notNullValue());

        long tick = 480;
        String deviceName = "thingy";
        MIDIUtils.insertDeviceName(
                track,
                tick,
                deviceName);
        // the insert we just did plus the eot message.
        assertThat("the track size is correct",
                track.size(), is(equalTo(2)));

        MidiEvent event = track.get(0);
        assertThat("event is not null",
                event, is(notNullValue()));
        assertThat("the tick is correct",
                event.getTick(), is(equalTo(tick)));

        MidiMessage message = event.getMessage();
        if (message instanceof MetaMessage) {
            MetaMessage meta = (MetaMessage) message;
            if (meta.getType() == MIDIUtils.META_DEVICE_NAME) {
                String text = MIDIUtils.bytesToText(meta.getData());
                assertThat("the text is correct",
                        text, is(equalTo(deviceName)));
            }
        }
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#insertKeySignature(javax.sound.midi.Track, long, int, int)}
     * .
     */
    @Test
    public final void testInsertKeySignature() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#insertLyric(javax.sound.midi.Track, long, java.lang.String)}
     * .
     */
    @Test
    public final void testInsertLyric() {
        Track track = createTrack();
        assertThat("Not null",
                track, notNullValue());

        long tick = 480;
        String lyric = "woo woo woo baby";
        MIDIUtils.insertLyric(
                track,
                tick,
                lyric);
        // the insert we just did plus the eot message.
        assertThat("the track size is correct",
                track.size(), is(equalTo(2)));

        MidiEvent event = track.get(0);
        assertThat("event is not null",
                event, is(notNullValue()));
        assertThat("the tick is correct",
                event.getTick(), is(equalTo(tick)));

        MidiMessage message = event.getMessage();
        if (message instanceof MetaMessage) {
            MetaMessage meta = (MetaMessage) message;
            if (meta.getType() == MIDIUtils.META_LYRIC) {
                String text = MIDIUtils.bytesToText(meta.getData());
                assertThat("the text is correct",
                        text, is(equalTo(lyric)));
            }
        }
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#insertMarker(javax.sound.midi.Track, long, java.lang.String)}
     * .
     */
    @Test
    public final void testInsertMarker() {
        Track track = createTrack();
        assertThat("Not null",
                track, notNullValue());

        long tick = 480;
        String marker = "right here";
        MIDIUtils.insertMarker(
                track,
                tick,
                marker);
        // the insert we just did plus the eot message.
        assertThat("the track size is correct",
                track.size(), is(equalTo(2)));

        MidiEvent event = track.get(0);
        assertThat("event is not null",
                event, is(notNullValue()));
        assertThat("the tick is correct",
                event.getTick(), is(equalTo(tick)));

        MidiMessage message = event.getMessage();
        if (message instanceof MetaMessage) {
            MetaMessage meta = (MetaMessage) message;
            if (meta.getType() == MIDIUtils.META_MARKER) {
                String text = MIDIUtils.bytesToText(meta.getData());
                assertThat("the text is correct",
                        text, is(equalTo(marker)));
            }
        }
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#insertMetaInstrument(javax.sound.midi.Track, long, java.lang.String)}
     * .
     */
    @Test
    public final void testInsertMetaInstrument() {
        Track track = createTrack();
        assertThat("Not null",
                track, notNullValue());

        long tick = 480;
        String instrument = "kazoo";
        MIDIUtils.insertMetaInstrument(
                track,
                tick,
                instrument);
        // the insert we just did plus the eot message.
        assertThat("the track size is correct",
                track.size(), is(equalTo(2)));

        MidiEvent event = track.get(0);
        assertThat("event is not null",
                event, is(notNullValue()));
        assertThat("the tick is correct",
                event.getTick(), is(equalTo(tick)));

        MidiMessage message = event.getMessage();
        if (message instanceof MetaMessage) {
            MetaMessage meta = (MetaMessage) message;
            if (meta.getType() == MIDIUtils.META_INSTRUMENT) {
                String text = MIDIUtils.bytesToText(meta.getData());
                assertThat("the text is correct",
                        text, is(equalTo(instrument)));
            }
        }
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#insertMetaText(javax.sound.midi.Track, long, int, java.lang.String)}
     * .
     */
    @Test
    public final void testInsertMetaText() {
        Track track = createTrack();
        assertThat("Not null",
                track, notNullValue());

        long tick = 480;
        String copyright = "kazoo";
        MIDIUtils.insertMetaText(
                track,
                tick,
                MIDIUtils.META_COPYRIGHT,
                copyright);
        // the insert we just did plus the eot message.
        assertThat("the track size is correct",
                track.size(), is(equalTo(2)));

        MidiEvent event = track.get(0);
        assertThat("event is not null",
                event, is(notNullValue()));
        assertThat("the tick is correct",
                event.getTick(), is(equalTo(tick)));

        MidiMessage message = event.getMessage();
        if (message instanceof MetaMessage) {
            MetaMessage meta = (MetaMessage) message;
            if (meta.getType() == MIDIUtils.META_COPYRIGHT) {
                String text = MIDIUtils.bytesToText(meta.getData());
                assertThat("the text is correct",
                        text, is(equalTo(copyright)));
            }
        }
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#insertNote(javax.sound.midi.Track, long, int, int, int, double)}
     * .
     */
    @Test
    public final void testInsertNote() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#insertProgramChange(javax.sound.midi.Track, long, int, int)}
     * .
     */
    @Test
    public final void testInsertProgramChange() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#insertProgramName(javax.sound.midi.Track, long, java.lang.String)}
     * .
     */
    @Test
    public final void testInsertProgramName() {
        Track track = createTrack();
        assertThat("Not null",
                track, notNullValue());

        long tick = 480;
        String programName = "right here";
        MIDIUtils.insertProgramName(
                track,
                tick,
                programName);
        // the insert we just did plus the eot message.
        assertThat("the track size is correct",
                track.size(), is(equalTo(2)));

        MidiEvent event = track.get(0);
        assertThat("event is not null",
                event, is(notNullValue()));
        assertThat("the tick is correct",
                event.getTick(), is(equalTo(tick)));

        MidiMessage message = event.getMessage();
        if (message instanceof MetaMessage) {
            MetaMessage meta = (MetaMessage) message;
            if (meta.getType() == MIDIUtils.META_PROGRAM_NAME) {
                String text = MIDIUtils.bytesToText(meta.getData());
                assertThat("the text is correct",
                        text, is(equalTo(programName)));
            }
        }

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#insertSequenceName(javax.sound.midi.Track, long, java.lang.String)}
     * .
     */
    @Test
    public final void testInsertSequenceName() {
        Track track = createTrack();
        assertThat("Not null",
                track, notNullValue());

        long tick = 480;
        String programName = "right here";
        MIDIUtils.insertSequenceName(
                track,
                tick,
                programName);
        // the insert we just did plus the eot message.
        assertThat("the track size is correct",
                track.size(), is(equalTo(2)));

        MidiEvent event = track.get(0);
        assertThat("event is not null",
                event, is(notNullValue()));
        assertThat("the tick is correct",
                event.getTick(), is(equalTo(tick)));

        MidiMessage message = event.getMessage();
        if (message instanceof MetaMessage) {
            MetaMessage meta = (MetaMessage) message;
            if (meta.getType() == MIDIUtils.META_NAME) {
                String text = MIDIUtils.bytesToText(meta.getData());
                assertThat("the text is correct",
                        text, is(equalTo(programName)));
            } else {
                fail("wrong type");
            }
        }
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#insertTempo(javax.sound.midi.Track, long, int)}
     * .
     */
    @Test
    public final void testInsertTempo() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#insertText(javax.sound.midi.Track, long, java.lang.String)}
     * .
     */
    @Test
    public final void testInsertText() {
        Track track = createTrack();
        assertThat("Not null",
                track, notNullValue());

        long tick = 480;
        String programName = "right here";
        MIDIUtils.insertText(
                track,
                tick,
                programName);
        // the insert we just did plus the eot message.
        assertThat("the track size is correct",
                track.size(), is(equalTo(2)));

        MidiEvent event = track.get(0);
        assertThat("event is not null",
                event, is(notNullValue()));
        assertThat("the tick is correct",
                event.getTick(), is(equalTo(tick)));

        MidiMessage message = event.getMessage();
        if (message instanceof MetaMessage) {
            MetaMessage meta = (MetaMessage) message;
            if (meta.getType() == MIDIUtils.META_TEXT) {
                String text = MIDIUtils.bytesToText(meta.getData());
                assertThat("the text is correct",
                        text, is(equalTo(programName)));
            } else {
                fail("wrong type");
            }
        }
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#insertTimeSignature(javax.sound.midi.Track, long, int, int)}
     * .
     */
    @Test
    public final void testInsertTimeSignature() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#isChannelMessage(int)}.
     */
    @Test
    public final void testIsChannelMessage() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#isCopyright(javax.sound.midi.MetaMessage)}
     * .
     */
    @Test
    public final void testIsCopyright() {
        Track track = createTrack();
        assertThat("Not null",
                track, notNullValue());

        long tick = 480;
        String copyrightText = "copyright 2014";
        MIDIUtils.insertCopyright(
                track,
                tick,
                copyrightText);
        // the insert we just did plus the eot message.
        assertThat("the track size is correct",
                track.size(), is(equalTo(2)));

        MidiEvent event = track.get(0);
        assertThat("event is not null",
                event, is(notNullValue()));
        assertThat("the tick is correct",
                event.getTick(), is(equalTo(tick)));

        MidiMessage message = event.getMessage();
        MetaMessage meta = (MetaMessage) message;
        assertThat("the predicate is correct",
                MIDIUtils.isCopyright(meta), is(equalTo(true)));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#isCuePoint(javax.sound.midi.MetaMessage)}
     * .
     */
    @Test
    public final void testIsCuePoint() {
        Track track = createTrack();
        assertThat("Not null",
                track, notNullValue());

        long tick = 480;
        String text = "cue";
        MIDIUtils.insertCuePoint(
                track,
                tick,
                text);
        // the insert we just did plus the eot message.
        assertThat("the track size is correct",
                track.size(), is(equalTo(2)));

        MidiEvent event = track.get(0);
        assertThat("event is not null",
                event, is(notNullValue()));
        assertThat("the tick is correct",
                event.getTick(), is(equalTo(tick)));

        MidiMessage message = event.getMessage();
        MetaMessage meta = (MetaMessage) message;
        assertThat("the predicate is correct",
                MIDIUtils.isCuePoint(meta), is(equalTo(true)));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#isInstrument(javax.sound.midi.MetaMessage)}
     * .
     */
    @Test
    public final void testIsInstrument() {
        Track track = createTrack();
        assertThat("Not null",
                track, notNullValue());

        long tick = 480;
        String text = "cue";
        MIDIUtils.insertMetaInstrument(
                track,
                tick,
                text);
        // the insert we just did plus the eot message.
        assertThat("the track size is correct",
                track.size(), is(equalTo(2)));

        MidiEvent event = track.get(0);
        assertThat("event is not null",
                event, is(notNullValue()));
        assertThat("the tick is correct",
                event.getTick(), is(equalTo(tick)));

        MidiMessage message = event.getMessage();
        MetaMessage meta = (MetaMessage) message;
        assertThat("the predicate is correct",
                MIDIUtils.isInstrument(meta), is(equalTo(true)));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#isLyric(javax.sound.midi.MetaMessage)}
     * .
     */
    @Test
    public final void testIsLyric() {
        Track track = createTrack();
        assertThat("Not null",
                track, notNullValue());

        long tick = 480;
        String text = "cue";
        MIDIUtils.insertLyric(
                track,
                tick,
                text);
        // the insert we just did plus the eot message.
        assertThat("the track size is correct",
                track.size(), is(equalTo(2)));

        MidiEvent event = track.get(0);
        assertThat("event is not null",
                event, is(notNullValue()));
        assertThat("the tick is correct",
                event.getTick(), is(equalTo(tick)));

        MidiMessage message = event.getMessage();
        MetaMessage meta = (MetaMessage) message;
        assertThat("the predicate is correct",
                MIDIUtils.isLyric(meta), is(equalTo(true)));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#isMarker(javax.sound.midi.MetaMessage)}
     * .
     */
    @Test
    public final void testIsMarker() {
        Track track = createTrack();
        assertThat("Not null",
                track, notNullValue());

        long tick = 480;
        String text = "cue";
        MIDIUtils.insertMarker(
                track,
                tick,
                text);
        // the insert we just did plus the eot message.
        assertThat("the track size is correct",
                track.size(), is(equalTo(2)));

        MidiEvent event = track.get(0);
        assertThat("event is not null",
                event, is(notNullValue()));
        assertThat("the tick is correct",
                event.getTick(), is(equalTo(tick)));

        MidiMessage message = event.getMessage();
        MetaMessage meta = (MetaMessage) message;
        assertThat("the predicate is correct",
                MIDIUtils.isMarker(meta), is(equalTo(true)));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#isMetaMessage(int)}.
     */
    @Test
    public final void testIsMetaMessage() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#isName(javax.sound.midi.MetaMessage)}
     * .
     */
    @Test
    public final void testIsName() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#isSysexMessage(int)}.
     */
    @Test
    public final void testIsSysexMessage() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#isText(javax.sound.midi.MetaMessage)}
     * .
     */
    @Test
    public final void testIsText() {

    }


    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#print(javax.sound.midi.MetaMessage)}
     * .
     */
    @Test
    public final void testPrintMetaMessage() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#print(javax.sound.midi.MidiFileFormat)}
     * .
     */
    @Test
    public final void testPrintMidiFileFormat() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#print(javax.sound.midi.Sequence)}
     * .
     */
    @Test
    public final void testPrintSequence() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#print(javax.sound.midi.Sequence, com.rockhoppertech.music.midi.js.MIDIEventFilter)}
     * .
     */
    @Test
    public final void testPrintSequenceMIDIEventFilter() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#print(javax.sound.midi.ShortMessage)}
     * .
     */
    @Test
    public final void testPrintShortMessage() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#print(javax.sound.midi.SysexMessage)}
     * .
     */
    @Test
    public final void testPrintSysexMessage() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#print(javax.sound.midi.Track, int)}
     * .
     */
    @Test
    public final void testPrintTrackInt() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#print(javax.sound.midi.Track, com.rockhoppertech.music.midi.js.MIDIEventFilter)}
     * .
     */
    @Test
    public final void testPrintTrackMIDIEventFilter() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#printChannelModeMessage(javax.sound.midi.ShortMessage)}
     * .
     */
    @Test
    public final void testPrintChannelModeMessage() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#printFull(javax.sound.midi.ShortMessage)}
     * .
     */
    @Test
    public final void testPrintFull() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#printKeySignature(javax.sound.midi.MetaMessage)}
     * .
     */
    @Test
    public final void testPrintKeySignature() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#printSystemCommonMessage(javax.sound.midi.ShortMessage)}
     * .
     */
    @Test
    public final void testPrintSystemCommonMessage() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#printTimeSignature(javax.sound.midi.MetaMessage)}
     * .
     */
    @Test
    public final void testPrintTimeSignatureMetaMessage() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#printTimeSignature(java.io.PrintWriter, javax.sound.midi.MetaMessage)}
     * .
     */
    @Test
    public final void testPrintTimeSignaturePrintWriterMetaMessage() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#read(java.io.File)}.
     */
    @Test
    public final void testReadFile() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#read(java.lang.String)}
     * .
     */
    @Test
    public final void testReadString() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#systemInfo()}.
     */
    @Test
    public final void testSystemInfo() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#toString(javax.sound.midi.MetaMessage)}
     * .
     */
    @Test
    public final void testToStringMetaMessage() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#toString(javax.sound.midi.MidiEvent)}
     * .
     */
    @Test
    public final void testToStringMidiEvent() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#toString(javax.sound.midi.MidiMessage)}
     * .
     */
    @Test
    public final void testToStringMidiMessage() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#toString(javax.sound.midi.ShortMessage)}
     * .
     */
    @Test
    public final void testToStringShortMessage() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#toString(javax.sound.midi.SysexMessage)}
     * .
     */
    @Test
    public final void testToStringSysexMessage() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#toStringFull(javax.sound.midi.MidiEvent)}
     * .
     */
    @Test
    public final void testToStringFullMidiEvent() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#toStringFull(javax.sound.midi.MidiMessage)}
     * .
     */
    @Test
    public final void testToStringFullMidiMessage() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#write(javax.sound.midi.Sequence, java.io.OutputStream)}
     * .
     */
    @Test
    public final void testWriteSequenceOutputStream() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#write(javax.sound.midi.Sequence, java.lang.String)}
     * .
     */
    @Test
    public final void testWriteSequenceString() {

    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#write(javax.sound.midi.Sequence, java.lang.String, int)}
     * .
     */
    @Test
    public final void testWriteSequenceStringInt() {

    }


    /**
     * Test method for
     * {@link com.rockhoppertech.music.midi.js.MIDIUtils#stopPlaying()}.
     */
    @Test
    public final void testStopPlaying() {

    }

}
