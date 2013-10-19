package com.rockhoppertech.music.midi.js;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

/**
 * Class <code>MIDIControllers</code> helps you work with MIDI control messages.
 * 
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * @since 1.0
 */
public final class MIDIControllers {

    public static final String ALL_CONTROLLERS_OFF_CONTROLLER = "All Controllers Off";
    public static final String ALL_NOTES_OFF_CONTROLLER = "All Notes Off";
    public static final String ALL_SOUND_OFF_CONTROLLER = "All Sound Off";

    public static final String BALANCE_COARSE_CONTROLLER = "Balance (coarse)";
    public static final String BALANCE_FINE_CONTROLLER = "Balance (fine)";

    public static final String BANK_SELECT_COARSE_CONTROLLER = "Bank Select (coarse)";
    public static final String BANK_SELECT_FINE_CONTROLLER = "Bank Select (fine)";

    public static final String BREATH_CONTROLLER_COARSE_CONTROLLER = "Breath controller (coarse)";

    public static final String BREATH_CONTROLLER_FINE_CONTROLLER = "Breath controller (fine)";

    // look at CONTROLLER_NAMES
    public static final int CC_BANK_SELECT_COARSE = 0;

    public static final int CC_BANK_SELECT_FINE = 32;

    public static final int CC_MODWHEEL_COARSE = 1;

    public static final int CC_MODWHEEL_FINE = 33;

    public static final int CC_PAN_COARSE = 10;

    public static final int CC_PAN_FINE = 42;

    /**
     * 0 (to 63) is off. 127 (to 64) is on.
     */
    public static final int CC_PORTAMENTO_ON_OFF = 65;

    public static final int CC_PORTAMENTO_TIME_COARSE = 5;

    public static final int CC_PORTAMENTO_TIME_FINE = 37;

    /**
     * sometimes called the hold pedal. 0 (to 63) is off. 127 (to 64) is on.
     */
    public static final int CC_SUSTAIN = 64;

    public static final int CC_VOLUME_COARSE = 7;

    public static final int CC_VOLUME_FINE = 39;

    public static final String CELESTE_LEVEL_CONTROLLER = "Celeste Level";

    public static final String CHORUS_LEVEL_CONTROLLER = "Chorus Level";

    // defined controllers
    public static final Map<String, Integer> CONTROLLER_NAMES = new HashMap<String, Integer>();

    public static final String DATA_BUTTON_DECREMENT_CONTROLLER = "Data Button decrement";

    public static final String DATA_BUTTON_INCREMENT_CONTROLLER = "Data Button increment";

    public static final String DATA_ENTRY_COARSE_CONTROLLER = "Data Entry (coarse)";

    public static final String DATA_ENTRY_FINE_CONTROLLER = "Data Entry (fine)";

    public static final String EFFECT_CONTROL_1_COARSE_CONTROLLER = "Effect Control 1 (coarse)";
    public static final String EFFECT_CONTROL_1_FINE_CONTROLLER = "Effect Control 1 (fine)";
    public static final String EFFECT_CONTROL_2_COARSE_CONTROLLER = "Effect Control 2 (coarse)";
    public static final String EFFECT_CONTROL_2_FINE_CONTROLLER = "Effect Control 2 (fine)";
    public static final String EFFECTS_LEVEL_CONTROLLER = "Effects Level";

    public static final String EXPRESSION_COARSE_CONTROLLER = "Expression (coarse)";
    public static final String EXPRESSION_FINE_CONTROLLER = "Expression (fine)";

    public static final String FOOT_PEDAL_COARSE_CONTROLLER = "Foot Pedal (coarse)";

    public static final String FOOT_PEDAL_FINE_CONTROLLER = "Foot Pedal (fine)";

    public static final String GENERAL_PURPOSE_BUTTON_1_ON_OFF_CONTROLLER = "General Purpose Button 1 (on/off)";

    public static final String GENERAL_PURPOSE_BUTTON_2_ON_OFF_CONTROLLER = "General Purpose Button 2 (on/off)";

    public static final String GENERAL_PURPOSE_BUTTON_3_ON_OFF_CONTROLLER = "General Purpose Button 3 (on/off)";

    public static final String GENERAL_PURPOSE_BUTTON_4_ON_OFF_CONTROLLER = "General Purpose Button 4 (on/off)";

    public static final String GENERAL_PURPOSE_SLIDER_1_CONTROLLER = "General Purpose Slider 1";

    public static final String GENERAL_PURPOSE_SLIDER_2_CONTROLLER = "General Purpose Slider 2";

    public static final String GENERAL_PURPOSE_SLIDER_3_CONTROLLER = "General Purpose Slider 3";

    public static final String GENERAL_PURPOSE_SLIDER_4_CONTROLLER = "General Purpose Slider 4";

    public static final String HOLD_2_PEDAL_ON_OFF_CONTROLLER = "Hold 2 Pedal (on/off)";
    public static final String HOLD_PEDAL_ON_OFF_CONTROLLER = "Hold Pedal (on/off)";

    public static final String LEGATO_PEDAL_ON_OFF_CONTROLLER = "Legato Pedal (on/off)";

    public static final String LOCAL_KEYBOARD_ON_OFF_CONTROLLER = "Local Keyboard (on/off)";

    public static final String MODULATION_WHEEL_COARSE_CONTROLLER = "Modulation Wheel (coarse)";
    public static final String MODULATION_WHEEL_FINE_CONTROLLER = "Modulation Wheel (fine)";
    public static final String MONO_OPERATION_CONTROLLER = "Mono Operation";

    public static final String NON_REGISTERED_PARAMETER_COARSE_CONTROLLER = "Non-registered Parameter (coarse)";
    public static final String NON_REGISTERED_PARAMETER_FINE_CONTROLLER = "Non-registered Parameter (fine)";

    public static final String OMNI_MODE_OFF_CONTROLLER = "Omni Mode Off";
    public static final String OMNI_MODE_ON_CONTROLLER = "Omni Mode On";

    public static final String PAN_POSITION_COARSE_CONTROLLER = "Pan position (coarse)";
    public static final String PAN_POSITION_FINE_CONTROLLER = "Pan position (fine)";

    public static final String PHASER_LEVEL_CONTROLLER = "Phaser Level";

    public static final String POLY_OPERATION_CONTROLLER = "Poly Operation";

    public static final String PORTAMENTO_ON_OFF_CONTROLLER = "Portamento (on/off)";
    public static final String PORTAMENTO_TIME_COARSE_CONTROLLER = "Portamento Time (coarse)";
    public static final String PORTAMENTO_TIME_FINE_CONTROLLER = "Portamento Time (fine)";

    public static final String REGISTERED_PARAMETER_COARSE_CONTROLLER = "Registered Parameter (coarse)";
    public static final String REGISTERED_PARAMETER_FINE_CONTROLLER = "Registered Parameter (fine)";

    public static final String SOFT_PEDAL_ON_OFF_CONTROLLER = "Soft Pedal (on/off)";

    public static final String SOUND_ATTACK_TIME_CONTROLLER = "Sound Attack Time";
    public static final String SOUND_BRIGHTNESS_CONTROLLER = "Sound Brightness";
    public static final String SOUND_CONTROL_10_CONTROLLER = "Sound Control 10";
    public static final String SOUND_CONTROL_6_CONTROLLER = "Sound Control 6";
    public static final String SOUND_CONTROL_7_CONTROLLER = "Sound Control 7";
    public static final String SOUND_CONTROL_8_CONTROLLER = "Sound Control 8";
    public static final String SOUND_CONTROL_9_CONTROLLER = "Sound Control 9";
    public static final String SOUND_RELEASE_TIME_CONTROLLER = "Sound Release Time";
    public static final String SOUND_TIMBRE_CONTROLLER = "Sound Timbre";
    public static final String SOUND_VARIATION_CONTROLLER = "Sound Variation";

    public static final String SUSTENUTO_PEDAL_ON_OFF_CONTROLLER = "Sustenuto Pedal (on/off)";

    public static final String TREMULO_LEVEL_CONTROLLER = "Tremulo Level";

    public static final String VOLUME_COARSE_CONTROLLER = "Volume (coarse)";
    public static final String VOLUME_FINE_CONTROLLER = "Volume (fine)";

    static {

        // public static final String BANK_SELECT_CONTROLLER = "Bank Select
        // (coarse)";
        CONTROLLER_NAMES.put(HOLD_PEDAL_ON_OFF_CONTROLLER,
                new Integer(64));
        CONTROLLER_NAMES.put(ALL_NOTES_OFF_CONTROLLER,
                new Integer(123));
        CONTROLLER_NAMES.put(SOUND_TIMBRE_CONTROLLER,
                new Integer(71));
        CONTROLLER_NAMES.put(GENERAL_PURPOSE_BUTTON_2_ON_OFF_CONTROLLER,
                new Integer(81));
        CONTROLLER_NAMES.put(FOOT_PEDAL_FINE_CONTROLLER,
                new Integer(36));
        CONTROLLER_NAMES.put(BREATH_CONTROLLER_FINE_CONTROLLER,
                new Integer(34));
        CONTROLLER_NAMES.put(BALANCE_FINE_CONTROLLER,
                new Integer(40));
        CONTROLLER_NAMES.put(FOOT_PEDAL_COARSE_CONTROLLER,
                new Integer(4));
        CONTROLLER_NAMES.put(SOUND_CONTROL_7_CONTROLLER,
                new Integer(76));
        CONTROLLER_NAMES.put(GENERAL_PURPOSE_BUTTON_1_ON_OFF_CONTROLLER,
                new Integer(80));
        CONTROLLER_NAMES.put(PHASER_LEVEL_CONTROLLER,
                new Integer(95));
        CONTROLLER_NAMES.put(MONO_OPERATION_CONTROLLER,
                new Integer(126));
        CONTROLLER_NAMES.put(EFFECT_CONTROL_2_COARSE_CONTROLLER,
                new Integer(13));
        CONTROLLER_NAMES.put(DATA_ENTRY_FINE_CONTROLLER,
                new Integer(38));
        CONTROLLER_NAMES.put(SOUND_CONTROL_9_CONTROLLER,
                new Integer(78));
        CONTROLLER_NAMES.put(BANK_SELECT_FINE_CONTROLLER,
                new Integer(32));
        CONTROLLER_NAMES.put(EFFECT_CONTROL_2_FINE_CONTROLLER,
                new Integer(45));
        CONTROLLER_NAMES.put(BALANCE_COARSE_CONTROLLER,
                new Integer(8));
        CONTROLLER_NAMES.put(EXPRESSION_COARSE_CONTROLLER,
                new Integer(11));
        CONTROLLER_NAMES.put(LOCAL_KEYBOARD_ON_OFF_CONTROLLER,
                new Integer(122));
        CONTROLLER_NAMES.put(SOUND_ATTACK_TIME_CONTROLLER,
                new Integer(73));
        CONTROLLER_NAMES.put(SUSTENUTO_PEDAL_ON_OFF_CONTROLLER,
                new Integer(66));
        CONTROLLER_NAMES.put(REGISTERED_PARAMETER_COARSE_CONTROLLER,
                new Integer(101));
        CONTROLLER_NAMES.put(DATA_BUTTON_DECREMENT_CONTROLLER,
                new Integer(97));
        CONTROLLER_NAMES.put(SOUND_CONTROL_8_CONTROLLER,
                new Integer(77));
        CONTROLLER_NAMES.put(LEGATO_PEDAL_ON_OFF_CONTROLLER,
                new Integer(68));
        CONTROLLER_NAMES.put(CHORUS_LEVEL_CONTROLLER,
                new Integer(93));
        CONTROLLER_NAMES.put(PAN_POSITION_COARSE_CONTROLLER,
                new Integer(10));
        CONTROLLER_NAMES.put(ALL_CONTROLLERS_OFF_CONTROLLER,
                new Integer(121));
        CONTROLLER_NAMES.put(SOUND_CONTROL_6_CONTROLLER,
                new Integer(75));
        CONTROLLER_NAMES.put(VOLUME_FINE_CONTROLLER,
                new Integer(39));
        CONTROLLER_NAMES.put(OMNI_MODE_ON_CONTROLLER,
                new Integer(125));
        CONTROLLER_NAMES.put(PORTAMENTO_ON_OFF_CONTROLLER,
                new Integer(65));
        CONTROLLER_NAMES.put(SOUND_RELEASE_TIME_CONTROLLER,
                new Integer(72));
        CONTROLLER_NAMES.put(GENERAL_PURPOSE_SLIDER_3_CONTROLLER,
                new Integer(18));
        CONTROLLER_NAMES.put(GENERAL_PURPOSE_BUTTON_3_ON_OFF_CONTROLLER,
                new Integer(82));
        CONTROLLER_NAMES.put(PORTAMENTO_TIME_FINE_CONTROLLER,
                new Integer(37));
        CONTROLLER_NAMES.put(VOLUME_COARSE_CONTROLLER,
                new Integer(7));
        CONTROLLER_NAMES.put(GENERAL_PURPOSE_SLIDER_4_CONTROLLER,
                new Integer(19));
        CONTROLLER_NAMES.put(DATA_BUTTON_INCREMENT_CONTROLLER,
                new Integer(96));
        CONTROLLER_NAMES.put(REGISTERED_PARAMETER_FINE_CONTROLLER,
                new Integer(100));
        CONTROLLER_NAMES.put(PAN_POSITION_FINE_CONTROLLER,
                new Integer(42));
        CONTROLLER_NAMES.put(EFFECTS_LEVEL_CONTROLLER,
                new Integer(91));
        CONTROLLER_NAMES.put(NON_REGISTERED_PARAMETER_COARSE_CONTROLLER,
                new Integer(99));
        CONTROLLER_NAMES.put(TREMULO_LEVEL_CONTROLLER,
                new Integer(92));
        CONTROLLER_NAMES.put(GENERAL_PURPOSE_SLIDER_2_CONTROLLER,
                new Integer(17));
        CONTROLLER_NAMES.put(CELESTE_LEVEL_CONTROLLER,
                new Integer(94));
        CONTROLLER_NAMES.put(EFFECT_CONTROL_1_FINE_CONTROLLER,
                new Integer(44));
        CONTROLLER_NAMES.put(EXPRESSION_FINE_CONTROLLER,
                new Integer(43));
        CONTROLLER_NAMES.put(SOUND_BRIGHTNESS_CONTROLLER,
                new Integer(74));
        CONTROLLER_NAMES.put(NON_REGISTERED_PARAMETER_FINE_CONTROLLER,
                new Integer(98));
        CONTROLLER_NAMES.put(SOUND_CONTROL_10_CONTROLLER,
                new Integer(79));
        CONTROLLER_NAMES.put(BREATH_CONTROLLER_COARSE_CONTROLLER,
                new Integer(2));
        CONTROLLER_NAMES.put(PORTAMENTO_TIME_COARSE_CONTROLLER,
                new Integer(5));
        CONTROLLER_NAMES.put(MODULATION_WHEEL_COARSE_CONTROLLER,
                new Integer(1));
        CONTROLLER_NAMES.put(HOLD_2_PEDAL_ON_OFF_CONTROLLER,
                new Integer(69));
        CONTROLLER_NAMES.put(EFFECT_CONTROL_1_COARSE_CONTROLLER,
                new Integer(12));
        CONTROLLER_NAMES.put(BANK_SELECT_COARSE_CONTROLLER,
                new Integer(0));
        CONTROLLER_NAMES.put(POLY_OPERATION_CONTROLLER,
                new Integer(127));
        CONTROLLER_NAMES.put(GENERAL_PURPOSE_SLIDER_1_CONTROLLER,
                new Integer(16));
        CONTROLLER_NAMES.put(ALL_SOUND_OFF_CONTROLLER,
                new Integer(120));
        CONTROLLER_NAMES.put(GENERAL_PURPOSE_BUTTON_4_ON_OFF_CONTROLLER,
                new Integer(83));
        CONTROLLER_NAMES.put(MODULATION_WHEEL_FINE_CONTROLLER,
                new Integer(33));
        CONTROLLER_NAMES.put(OMNI_MODE_OFF_CONTROLLER,
                new Integer(124));
        CONTROLLER_NAMES.put(DATA_ENTRY_COARSE_CONTROLLER,
                new Integer(6));
        CONTROLLER_NAMES.put(SOFT_PEDAL_ON_OFF_CONTROLLER,
                new Integer(67));
        CONTROLLER_NAMES.put(SOUND_VARIATION_CONTROLLER,
                new Integer(70));
        /*
         * CONTROLLER_NAMES.put("Bank Select (coarse)", new Integer(0));
         * CONTROLLER_NAMES.put("Modulation Wheel (coarse)", new Integer(1));
         * CONTROLLER_NAMES.put("Breath controller (coarse)", new Integer(2));
         * CONTROLLER_NAMES.put("Foot Pedal (coarse)", new Integer(4));
         * CONTROLLER_NAMES.put("Portamento Time (coarse)", new Integer(5));
         * CONTROLLER_NAMES.put("Data Entry (coarse)", new Integer(6));
         * CONTROLLER_NAMES.put("Volume (coarse)", new Integer(7));
         * CONTROLLER_NAMES.put("Balance (coarse)", new Integer(8));
         * CONTROLLER_NAMES.put("Pan position (coarse)", new Integer(10));
         * CONTROLLER_NAMES.put("Expression (coarse)", new Integer(11));
         * CONTROLLER_NAMES.put("Effect Control 1 (coarse)", new Integer(12));
         * CONTROLLER_NAMES.put("Effect Control 2 (coarse)", new Integer(13));
         * CONTROLLER_NAMES.put("General Purpose Slider 1", new Integer(16));
         * CONTROLLER_NAMES.put("General Purpose Slider 2", new Integer(17));
         * CONTROLLER_NAMES.put("General Purpose Slider 3", new Integer(18));
         * CONTROLLER_NAMES.put("General Purpose Slider 4", new Integer(19));
         * CONTROLLER_NAMES.put("Bank Select (fine)", new Integer(32));
         * CONTROLLER_NAMES.put("Modulation Wheel (fine)", new Integer(33));
         * CONTROLLER_NAMES.put("Breath controller (fine)", new Integer(34));
         * CONTROLLER_NAMES.put("Foot Pedal (fine)", new Integer(36));
         * CONTROLLER_NAMES.put("Portamento Time (fine)", new Integer(37));
         * CONTROLLER_NAMES.put("Data Entry (fine)", new Integer(38));
         * CONTROLLER_NAMES.put("Volume (fine)", new Integer(39));
         * CONTROLLER_NAMES.put("Balance (fine)", new Integer(40));
         * CONTROLLER_NAMES.put("Pan position (fine)", new Integer(42));
         * CONTROLLER_NAMES.put("Expression (fine)", new Integer(43));
         * CONTROLLER_NAMES.put("Effect Control 1 (fine)", new Integer(44));
         * CONTROLLER_NAMES.put("Effect Control 2 (fine)", new Integer(45));
         * CONTROLLER_NAMES.put("Hold Pedal (on/off)", new Integer(64));
         * CONTROLLER_NAMES.put("Portamento (on/off)", new Integer(65));
         * CONTROLLER_NAMES.put("Sustenuto Pedal (on/off)", new Integer(66));
         * CONTROLLER_NAMES.put("Soft Pedal (on/off)", new Integer(67));
         * CONTROLLER_NAMES.put("Legato Pedal (on/off)", new Integer(68));
         * CONTROLLER_NAMES.put("Hold 2 Pedal (on/off)", new Integer(69));
         * CONTROLLER_NAMES.put("Sound Variation", new Integer(70));
         * CONTROLLER_NAMES.put("Sound Timbre", new Integer(71));
         * CONTROLLER_NAMES.put("Sound Release Time", new Integer(72));
         * CONTROLLER_NAMES.put("Sound Attack Time", new Integer(73));
         * CONTROLLER_NAMES.put("Sound Brightness", new Integer(74));
         * CONTROLLER_NAMES.put("Sound Control 6", new Integer(75));
         * CONTROLLER_NAMES.put("Sound Control 7", new Integer(76));
         * CONTROLLER_NAMES.put("Sound Control 8", new Integer(77));
         * CONTROLLER_NAMES.put("Sound Control 9", new Integer(78));
         * CONTROLLER_NAMES.put("Sound Control 10", new Integer(79));
         * CONTROLLER_NAMES.put("General Purpose Button 1 (on/off)", new
         * Integer( 80)); CONTROLLER_NAMES.put("General Purpose Button 2
         * (on/off)", new Integer( 81)); CONTROLLER_NAMES.put("General Purpose
         * Button 3 (on/off)", new Integer( 82)); CONTROLLER_NAMES.put("General
         * Purpose Button 4 (on/off)", new Integer( 83));
         * CONTROLLER_NAMES.put("Effects Level", new Integer(91));
         * CONTROLLER_NAMES.put("Tremulo Level", new Integer(92));
         * CONTROLLER_NAMES.put("Chorus Level", new Integer(93));
         * CONTROLLER_NAMES.put("Celeste Level", new Integer(94));
         * CONTROLLER_NAMES.put("Phaser Level", new Integer(95));
         * CONTROLLER_NAMES.put("Data Button increment", new Integer(96));
         * CONTROLLER_NAMES.put("Data Button decrement", new Integer(97));
         * CONTROLLER_NAMES .put("Non-registered Parameter (fine)", new
         * Integer(98)); CONTROLLER_NAMES.put("Non-registered Parameter
         * (coarse)", new Integer( 99)); CONTROLLER_NAMES.put("Registered
         * Parameter (fine)", new Integer(100));
         * CONTROLLER_NAMES.put("Registered Parameter (coarse)", new
         * Integer(101)); CONTROLLER_NAMES.put("All Sound Off", new
         * Integer(120)); CONTROLLER_NAMES.put("All Controllers Off", new
         * Integer(121)); CONTROLLER_NAMES.put("Local Keyboard (on/off)", new
         * Integer(122)); CONTROLLER_NAMES.put("All Notes Off", new
         * Integer(123)); CONTROLLER_NAMES.put("Omni Mode Off", new
         * Integer(124)); CONTROLLER_NAMES.put("Omni Mode On", new
         * Integer(125)); CONTROLLER_NAMES.put("Mono Operation", new
         * Integer(126)); CONTROLLER_NAMES.put("Poly Operation", new
         * Integer(127));
         */
    }

    /**
     * @param track append to this track
     * @param chan on this channel
     * @param d1 data1
     * @param d2 data2
     */
    public static void appendControlChange(final Track track, final int chan,
            final int d1, final int d2) {
        try {
            ShortMessage message = new ShortMessage();
            long tick = track.ticks();
            // message.setMessage(0xB0 + chan, d1, d2);
            message.setMessage(ShortMessage.CONTROL_CHANGE,
                    chan,
                    d1,
                    d2);
            MidiEvent event = new MidiEvent(message, tick);
            track.add(event);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @param value the controller number
     * @return the controller name
     */
    public static String getControllerName(final Integer value) {
        String r = "Unknown controller: " + value;
        for (Entry<String, Integer> e : CONTROLLER_NAMES.entrySet()) {
            if (e.getValue().equals(value)) {
                r = e.getKey();
                break;
            }
        }
        return r;
    }

    /**
     * @param name controller name
     * @return the controller number
     */
    public static int getControllerNumber(final String name) {
        Integer i = CONTROLLER_NAMES.get(name);
        if (i == null) {
            System.err.println("name is null");
            throw new IllegalArgumentException(name);
        }
        return i.intValue();
    }

    /**
     * @param track the track to be modified
     * @param tick when to insert it
     * @param chan on which channel
     * @param d1 data1
     * @param d2 data2
     */
    public static void insertControlChange(final Track track, final long tick,
            final int chan,
            final int d1, final int d2) {
        try {
            ShortMessage message = new ShortMessage();
            // message.setMessage(0xB0 + chan, d1, d2);
            message.setMessage(ShortMessage.CONTROL_CHANGE,
                    chan,
                    d1,
                    d2);
            MidiEvent event = new MidiEvent(message, tick);
            track.add(event);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @param track
     * @param tick
     * @param chan
     */
    public static void insertSustainOff(final Track track, final long tick,
            final int chan) {
        insertControlChange(track,
                tick,
                chan,
                CC_SUSTAIN,
                0);
    }

    /**
     * @param track
     * @param tick
     * @param chan
     */
    public static void insertSustainOn(final Track track, final long tick,
            final int chan) {
        insertControlChange(track,
                tick,
                chan,
                CC_SUSTAIN,
                127);
    }

    /**
     * @param track
     * @param tick
     * @param chan
     * @param value
     */
    public static void insertVolumeChange(final Track track, final long tick,
            final int chan,
            final int value) {
        insertControlChange(track,
                tick,
                chan,
                CC_VOLUME_COARSE,
                value);
    }

    /**
     * @param track
     * @param tick
     * @param chan
     * @param value
     */
    public static void insertPan(final Track track, final long tick,
            final int chan, final int value) {
        if (value < 0 || value > 127) {
            throw new IllegalArgumentException(
                    "value must be between 0 and 127");
        }
        insertControlChange(track, tick, chan, CC_PAN_COARSE, value);
    }

    /**
     * @param track
     * @param tick
     * @param chan
     */
    public static void insertPortamentoOff(final Track track, final long tick,
            final int chan) {
        insertControlChange(track, tick, chan, CC_PORTAMENTO_ON_OFF, 0);
    }

    /**
     * @param track
     * @param tick
     * @param chan
     */
    public static void insertPortamentoOn(final Track track, final long tick,
            final int chan) {
        insertControlChange(track, tick, chan, CC_PORTAMENTO_ON_OFF, 127);
    }

    /**
     * No instantiation.
     */
    private MIDIControllers() {
    }

}
