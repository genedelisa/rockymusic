package com.rockhoppertech.music.midi.js;

/*
 * #%L
 * Rocky Music Core
 * %%
 * Copyright (C) 1996 - 2014 Rockhopper Technologies
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.midi.gm.MIDIGMPatch;

/**
 * Instruments contain a GM patch, and a range.
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class Instrument implements Serializable {

    /**
     * For Serialization.
     */
    private static final long serialVersionUID = 3727585180676573574L;
    
    private static List<Instrument> all;
    private static Map<String, Instrument> allMap;
    
    static {
        all = new ArrayList<>();
        allMap = new TreeMap<>();
    }
    
    /**
     * 
     */
    public static final Instrument PIANO = new Instrument(MIDIGMPatch.PIANO,
            MIDIGMPatch.PIANO.getName(), Pitch.A1, Pitch.C8);
    

    public static final Instrument HARP = new Instrument(MIDIGMPatch.HARP,
            MIDIGMPatch.HARP.getName(), Pitch.B2, Pitch.GS8);

    public static final Instrument BRITEPIANO = new Instrument(
            MIDIGMPatch.BRITEPIANO, Pitch.C0, Pitch.C8);
    public static final Instrument HAMMERPIANO = new Instrument(
            MIDIGMPatch.HAMMERPIANO, Pitch.C0, Pitch.C8);
    public static final Instrument HONKEYTONK = new Instrument(
            MIDIGMPatch.HONKEYTONK, Pitch.C0, Pitch.C8);
    public static final Instrument NEWTINES = new Instrument(
            MIDIGMPatch.NEWTINES, Pitch.C0, Pitch.C8);
    public static final Instrument DIGIPIANO = new Instrument(
            MIDIGMPatch.DIGIPIANO, Pitch.C0, Pitch.C8);
    public static final Instrument HARPSICORD = new Instrument(
            MIDIGMPatch.HARPSICORD, Pitch.C0, Pitch.C8);
    public static final Instrument CLAV = new Instrument(MIDIGMPatch.CLAV,
            Pitch.C0, Pitch.C8);
    public static final Instrument CELESTA = new Instrument(
            MIDIGMPatch.CELESTA, Pitch.C0, Pitch.C8);
    public static final Instrument GLOCKEN = new Instrument(
            MIDIGMPatch.GLOCKEN, Pitch.C0, Pitch.C8);
    public static final Instrument MUSICBOX = new Instrument(
            MIDIGMPatch.MUSICBOX, Pitch.C0, Pitch.C8);
    public static final Instrument VIBES = new Instrument(MIDIGMPatch.VIBES,
            Pitch.C0, Pitch.C8);
    public static final Instrument MARIMBA = new Instrument(
            MIDIGMPatch.MARIMBA, Pitch.C0, Pitch.C8);
    public static final Instrument XYLOPHON = new Instrument(
            MIDIGMPatch.XYLOPHON, Pitch.C0, Pitch.C8);
    public static final Instrument TUBULAR = new Instrument(
            MIDIGMPatch.TUBULAR, Pitch.C0, Pitch.C8);
    public static final Instrument DULCIMER = new Instrument(
            MIDIGMPatch.DULCIMER, Pitch.C0, Pitch.C8);
    public static final Instrument FULLORGAN = new Instrument(
            MIDIGMPatch.FULLORGAN, Pitch.C0, Pitch.C8);
    public static final Instrument PERCORGAN = new Instrument(
            MIDIGMPatch.PERCORGAN, Pitch.C0, Pitch.C8);
    public static final Instrument BX3ORGAN = new Instrument(
            MIDIGMPatch.BX3ORGAN, Pitch.C0, Pitch.C8);
    public static final Instrument CHURCHPIPE = new Instrument(
            MIDIGMPatch.CHURCHPIPE, Pitch.C0, Pitch.C8);
    public static final Instrument POSITIVE = new Instrument(
            MIDIGMPatch.POSITIVE, Pitch.C0, Pitch.C8);
    public static final Instrument MUSETTE = new Instrument(
            MIDIGMPatch.MUSETTE, Pitch.C0, Pitch.C8);
    public static final Instrument HARMONICA = new Instrument(
            MIDIGMPatch.HARMONICA, Pitch.C0, Pitch.C8);
    public static final Instrument TANGO = new Instrument(MIDIGMPatch.TANGO,
            Pitch.C0, Pitch.C8);
    public static final Instrument CLASSICGTR = new Instrument(
            MIDIGMPatch.CLASSICGTR, Pitch.C0, Pitch.C8);
    public static final Instrument ACOUSTICGUITAR = new Instrument(
            MIDIGMPatch.ACOUSTICGUITAR, Pitch.C0, Pitch.C8);
    public static final Instrument JAZZGUITAR = new Instrument(
            MIDIGMPatch.JAZZGUITAR, Pitch.C0, Pitch.C8);
    public static final Instrument CLEANGTR = new Instrument(
            MIDIGMPatch.CLEANGTR, Pitch.C0, Pitch.C8);
    public static final Instrument MUTEGUITAR = new Instrument(
            MIDIGMPatch.MUTEGUITAR, Pitch.C0, Pitch.C8);
    public static final Instrument OVERDRIVE = new Instrument(
            MIDIGMPatch.OVERDRIVE, Pitch.C0, Pitch.C8);
    public static final Instrument DISTGUITAR = new Instrument(
            MIDIGMPatch.DISTGUITAR, Pitch.C0, Pitch.C8);
    public static final Instrument ROCKMONICS = new Instrument(
            MIDIGMPatch.ROCKMONICS, Pitch.C0, Pitch.C8);
    public static final Instrument JAZZBASS = new Instrument(
            MIDIGMPatch.JAZZBASS, Pitch.C0, Pitch.C8);
    public static final Instrument DEEPBASS = new Instrument(
            MIDIGMPatch.DEEPBASS, Pitch.C0, Pitch.C8);
    public static final Instrument PICKBASS = new Instrument(
            MIDIGMPatch.PICKBASS, Pitch.C0, Pitch.C8);
    public static final Instrument FRETLESS = new Instrument(
            MIDIGMPatch.FRETLESS, Pitch.C0, Pitch.C8);
    public static final Instrument SLAPBASS1 = new Instrument(
            MIDIGMPatch.SLAPBASS1, Pitch.C0, Pitch.C8);
    public static final Instrument SLAPBASS2 = new Instrument(
            MIDIGMPatch.SLAPBASS2, Pitch.C0, Pitch.C8);
    public static final Instrument SYNTHBASS1 = new Instrument(
            MIDIGMPatch.SYNTHBASS1, Pitch.C0, Pitch.C8);
    public static final Instrument SYNTHBASS2 = new Instrument(
            MIDIGMPatch.SYNTHBASS2, Pitch.C0, Pitch.C8);

    public static final Instrument VIOLIN = new Instrument(MIDIGMPatch.VIOLIN,
            Pitch.G4, Pitch.A8);

    public static final Instrument VIOLA = new Instrument(MIDIGMPatch.VIOLA,
            Pitch.C4, Pitch.E7);

    public static final Instrument CELLO = new Instrument(MIDIGMPatch.CELLO,
            Pitch.C3, Pitch.C7);

    public static final Instrument CONTRABASS = new Instrument(
            MIDIGMPatch.CONTRABASS,
            Pitch.C3, Pitch.C6);

    public static final Instrument TREMOLOSTR = new Instrument(
            MIDIGMPatch.TREMOLOSTR, Pitch.C0, Pitch.C8);
    public static final Instrument PIZZICATO = new Instrument(
            MIDIGMPatch.PIZZICATO, Pitch.C0, Pitch.C8);

    public static final Instrument TIMPANI = new Instrument(
            MIDIGMPatch.TIMPANI, Pitch.C0, Pitch.C8);
    public static final Instrument StringEnsemble1 = new Instrument(
            MIDIGMPatch.StringEnsemble1, Pitch.C0, Pitch.C8);
    public static final Instrument StringEnsemble2 = new Instrument(
            MIDIGMPatch.StringEnsemble2, Pitch.C0, Pitch.C8);
    public static final Instrument SynthStrings1 = new Instrument(
            MIDIGMPatch.SynthStrings1, Pitch.C0, Pitch.C8);
    public static final Instrument SynthStrings2 = new Instrument(
            MIDIGMPatch.SynthStrings2, Pitch.C0, Pitch.C8);
    public static final Instrument CHOIR = new Instrument(MIDIGMPatch.CHOIR,
            Pitch.C0, Pitch.C8);
    public static final Instrument DOOVOICE = new Instrument(
            MIDIGMPatch.DOOVOICE, Pitch.C0, Pitch.C8);
    public static final Instrument VOICES = new Instrument(MIDIGMPatch.VOICES,
            Pitch.C0, Pitch.C8);
    public static final Instrument ORCHHIT = new Instrument(
            MIDIGMPatch.ORCHHIT, Pitch.C0, Pitch.C8);
    public static final Instrument TRUMPET = new Instrument(
            MIDIGMPatch.TRUMPET, Pitch.C0, Pitch.C8);
    public static final Instrument TROMBONE = new Instrument(
            MIDIGMPatch.TROMBONE, Pitch.C0, Pitch.C8);
    public static final Instrument TUBA = new Instrument(MIDIGMPatch.TUBA,
            Pitch.C0, Pitch.C8);
    public static final Instrument MUTEDTRUMPET = new Instrument(
            MIDIGMPatch.MUTEDTRUMPET, Pitch.C0, Pitch.C8);
    public static final Instrument FRENCHHORN = new Instrument(
            MIDIGMPatch.FRENCHHORN, Pitch.C0, Pitch.C8);
    public static final Instrument BRASS = new Instrument(MIDIGMPatch.BRASS,
            Pitch.C0, Pitch.C8);
    public static final Instrument SYNBRASS1 = new Instrument(
            MIDIGMPatch.SYNBRASS1, Pitch.C0, Pitch.C8);
    public static final Instrument SYNBRASS2 = new Instrument(
            MIDIGMPatch.SYNBRASS2, Pitch.C0, Pitch.C8);
    public static final Instrument SOPRANOSAX = new Instrument(
            MIDIGMPatch.SOPRANOSAX, Pitch.C0, Pitch.C8);
    public static final Instrument ALTOSAX = new Instrument(
            MIDIGMPatch.ALTOSAX, Pitch.C0, Pitch.C8);
    public static final Instrument TENORSAX = new Instrument(
            MIDIGMPatch.TENORSAX, Pitch.C0, Pitch.C8);
    public static final Instrument BARISAX = new Instrument(
            MIDIGMPatch.BARISAX, Pitch.C0, Pitch.C8);
    public static final Instrument OBOE = new Instrument(MIDIGMPatch.OBOE,
            Pitch.C0, Pitch.C8);
    public static final Instrument ENGLISHHORN = new Instrument(
            MIDIGMPatch.ENGLISHHORN, Pitch.C0, Pitch.C8);
    public static final Instrument BASSOON = new Instrument(
            MIDIGMPatch.BASSOON, Pitch.C0, Pitch.C8);
    public static final Instrument CLARINET = new Instrument(
            MIDIGMPatch.CLARINET, Pitch.C0, Pitch.C8);
    public static final Instrument PICCOLO = new Instrument(
            MIDIGMPatch.PICCOLO, Pitch.C0, Pitch.C8);
    public static final Instrument FLUTE = new Instrument(MIDIGMPatch.FLUTE,
            Pitch.C0, Pitch.C8);
    public static final Instrument RECORDER = new Instrument(
            MIDIGMPatch.RECORDER, Pitch.C0, Pitch.C8);
    public static final Instrument PANFLUTE = new Instrument(
            MIDIGMPatch.PANFLUTE, Pitch.C0, Pitch.C8);
    public static final Instrument BOTTLE = new Instrument(MIDIGMPatch.BOTTLE,
            Pitch.C0, Pitch.C8);
    public static final Instrument SHAKUHACHI = new Instrument(
            MIDIGMPatch.SHAKUHACHI, Pitch.C0, Pitch.C8);
    public static final Instrument WHISTLE = new Instrument(
            MIDIGMPatch.WHISTLE, Pitch.C0, Pitch.C8);
    public static final Instrument OCARINA = new Instrument(
            MIDIGMPatch.OCARINA, Pitch.C0, Pitch.C8);
    public static final Instrument SQUAREWAVE = new Instrument(
            MIDIGMPatch.SQUAREWAVE, Pitch.C0, Pitch.C8);
    public static final Instrument SAWWAVE = new Instrument(
            MIDIGMPatch.SAWWAVE, Pitch.C0, Pitch.C8);
    public static final Instrument CALLIOPE = new Instrument(
            MIDIGMPatch.CALLIOPE, Pitch.C0, Pitch.C8);
    public static final Instrument SYNCHIFF = new Instrument(
            MIDIGMPatch.SYNCHIFF, Pitch.C0, Pitch.C8);
    public static final Instrument CHARANG = new Instrument(
            MIDIGMPatch.CHARANG, Pitch.C0, Pitch.C8);
    public static final Instrument AIRCHORUS = new Instrument(
            MIDIGMPatch.AIRCHORUS, Pitch.C0, Pitch.C8);
    public static final Instrument FIFTHS = new Instrument(MIDIGMPatch.FIFTHS,
            Pitch.C0, Pitch.C8);
    public static final Instrument BASSLEAD = new Instrument(
            MIDIGMPatch.BASSLEAD, Pitch.C0, Pitch.C8);
    public static final Instrument NewAgePad = new Instrument(
            MIDIGMPatch.NewAgePad, Pitch.C0, Pitch.C8);
    public static final Instrument WARMPAD = new Instrument(
            MIDIGMPatch.WARMPAD, Pitch.C0, Pitch.C8);
    public static final Instrument POLYPAD = new Instrument(
            MIDIGMPatch.POLYPAD, Pitch.C0, Pitch.C8);
    public static final Instrument GHOSTPAD = new Instrument(
            MIDIGMPatch.GHOSTPAD, Pitch.C0, Pitch.C8);
    public static final Instrument BOWEDGLAS = new Instrument(
            MIDIGMPatch.BOWEDGLAS, Pitch.C0, Pitch.C8);
    public static final Instrument METALPAD = new Instrument(
            MIDIGMPatch.METALPAD, Pitch.C0, Pitch.C8);
    public static final Instrument HALOPAD = new Instrument(
            MIDIGMPatch.HALOPAD, Pitch.C0, Pitch.C8);
    public static final Instrument SWEEP = new Instrument(MIDIGMPatch.SWEEP,
            Pitch.C0, Pitch.C8);
    public static final Instrument ICERAIN = new Instrument(
            MIDIGMPatch.ICERAIN, Pitch.C0, Pitch.C8);
    public static final Instrument SOUNDTRACK = new Instrument(
            MIDIGMPatch.SOUNDTRACK, Pitch.C0, Pitch.C8);
    public static final Instrument CRYSTAL = new Instrument(
            MIDIGMPatch.CRYSTAL, Pitch.C0, Pitch.C8);
    public static final Instrument ATMOSPHERE = new Instrument(
            MIDIGMPatch.ATMOSPHERE, Pitch.C0, Pitch.C8);
    public static final Instrument BRIGHTNESS = new Instrument(
            MIDIGMPatch.BRIGHTNESS, Pitch.C0, Pitch.C8);
    public static final Instrument GOBLIN = new Instrument(MIDIGMPatch.GOBLIN,
            Pitch.C0, Pitch.C8);
    public static final Instrument ECHODROP = new Instrument(
            MIDIGMPatch.ECHODROP, Pitch.C0, Pitch.C8);
    public static final Instrument SCIFI = new Instrument(MIDIGMPatch.SCIFI,
            Pitch.C0, Pitch.C8);
    public static final Instrument SITAR = new Instrument(MIDIGMPatch.SITAR,
            Pitch.C0, Pitch.C8);
    public static final Instrument BANJO = new Instrument(MIDIGMPatch.BANJO,
            Pitch.C0, Pitch.C8);
    public static final Instrument SHAMISEN = new Instrument(
            MIDIGMPatch.SHAMISEN, Pitch.C0, Pitch.C8);
    public static final Instrument KOTO = new Instrument(MIDIGMPatch.KOTO,
            Pitch.C0, Pitch.C8);
    public static final Instrument KALIMBA = new Instrument(
            MIDIGMPatch.KALIMBA, Pitch.C0, Pitch.C8);
    public static final Instrument SCOTLAND = new Instrument(
            MIDIGMPatch.SCOTLAND, Pitch.C0, Pitch.C8);
    public static final Instrument FIDDLE = new Instrument(MIDIGMPatch.FIDDLE,
            Pitch.C0, Pitch.C8);
    public static final Instrument SHANAI = new Instrument(MIDIGMPatch.SHANAI,
            Pitch.C0, Pitch.C8);
    public static final Instrument METALBELL = new Instrument(
            MIDIGMPatch.METALBELL, Pitch.C0, Pitch.C8);
    public static final Instrument AGOGO = new Instrument(MIDIGMPatch.AGOGO,
            Pitch.C0, Pitch.C8);
    public static final Instrument STEELDRUMS = new Instrument(
            MIDIGMPatch.STEELDRUMS, Pitch.C0, Pitch.C8);
    public static final Instrument WOODBLOCK = new Instrument(
            MIDIGMPatch.WOODBLOCK, Pitch.C0, Pitch.C8);
    public static final Instrument TAIKO = new Instrument(MIDIGMPatch.TAIKO,
            Pitch.C0, Pitch.C8);
    public static final Instrument TOM = new Instrument(MIDIGMPatch.TOM,
            Pitch.C0, Pitch.C8);
    public static final Instrument SYNTHTOM = new Instrument(
            MIDIGMPatch.SYNTHTOM, Pitch.C0, Pitch.C8);
    public static final Instrument REVCYMBAL = new Instrument(
            MIDIGMPatch.REVCYMBAL, Pitch.C0, Pitch.C8);
    public static final Instrument FRETNOISE = new Instrument(
            MIDIGMPatch.FRETNOISE, Pitch.C0, Pitch.C8);
    public static final Instrument NOISECHIFF = new Instrument(
            MIDIGMPatch.NOISECHIFF, Pitch.C0, Pitch.C8);
    public static final Instrument SEASHORE = new Instrument(
            MIDIGMPatch.SEASHORE, Pitch.C0, Pitch.C8);
    public static final Instrument BIRDS = new Instrument(MIDIGMPatch.BIRDS,
            Pitch.C0, Pitch.C8);
    public static final Instrument TELEPHONE = new Instrument(
            MIDIGMPatch.TELEPHONE, Pitch.C0, Pitch.C8);
    public static final Instrument HELICOPTER = new Instrument(
            MIDIGMPatch.HELICOPTER, Pitch.C0, Pitch.C8);
    public static final Instrument STADIUM = new Instrument(
            MIDIGMPatch.STADIUM, Pitch.C0, Pitch.C8);
    public static final Instrument GUNSHOT = new Instrument(
            MIDIGMPatch.GUNSHOT, Pitch.C0, Pitch.C8);

    // percussion

    public static final Instrument OPEN_TRIANGLE_PERC = new Instrument(
            MIDIGMPatch.OPEN_TRIANGLE_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument COWBELL_PERC = new Instrument(
            MIDIGMPatch.COWBELL_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument HI_BONGO_PERC = new Instrument(
            MIDIGMPatch.HI_BONGO_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument LOW_FLOOR_TOM_PERC = new Instrument(
            MIDIGMPatch.LOW_FLOOR_TOM_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument CABASA_PERC = new Instrument(
            MIDIGMPatch.CABASA_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument LOW_WOOD_BLOCK_PERC = new Instrument(
            MIDIGMPatch.LOW_WOOD_BLOCK_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument RIDE_BELL_PERC = new Instrument(
            MIDIGMPatch.RIDE_BELL_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument ELECTRIC_SNARE_PERC = new Instrument(
            MIDIGMPatch.ELECTRIC_SNARE_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument LONG_GUIRO_PERC = new Instrument(
            MIDIGMPatch.LONG_GUIRO_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument CRASH_CYMBAL_1_PERC = new Instrument(
            MIDIGMPatch.CRASH_CYMBAL_1_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument MUTE_CUICA_PERC = new Instrument(
            MIDIGMPatch.MUTE_CUICA_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument HAND_CLAP_PERC = new Instrument(
            MIDIGMPatch.HAND_CLAP_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument HIGH_FLOOR_TOM_PERC = new Instrument(
            MIDIGMPatch.HIGH_FLOOR_TOM_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument ACOUSTIC_BASS_DRUM_PERC = new Instrument(
            MIDIGMPatch.ACOUSTIC_BASS_DRUM_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument LOW_TOM_PERC = new Instrument(
            MIDIGMPatch.LOW_TOM_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument SHORT_GUIRO_PERC = new Instrument(
            MIDIGMPatch.SHORT_GUIRO_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument HI_WOOD_BLOCK_PERC = new Instrument(
            MIDIGMPatch.HI_WOOD_BLOCK_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument LOW_TIMBALE_PERC = new Instrument(
            MIDIGMPatch.LOW_TIMBALE_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument LOW_AGOGO_PERC = new Instrument(
            MIDIGMPatch.LOW_AGOGO_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument MUTE_TRIANGLE_PERC = new Instrument(
            MIDIGMPatch.MUTE_TRIANGLE_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument OPEN_HI_CONGA_PERC = new Instrument(
            MIDIGMPatch.OPEN_HI_CONGA_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument MUTE_HI_CONGA_PERC = new Instrument(
            MIDIGMPatch.MUTE_HI_CONGA_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument TAMBOURINE_PERC = new Instrument(
            MIDIGMPatch.TAMBOURINE_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument PEDAL_HI_HAT_PERC = new Instrument(
            MIDIGMPatch.PEDAL_HI_HAT_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument HIGH_TOM_PERC = new Instrument(
            MIDIGMPatch.HIGH_TOM_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument SPLASH_CYMBAL_PERC = new Instrument(
            MIDIGMPatch.SPLASH_CYMBAL_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument BASS_DRUM_1_PERC = new Instrument(
            MIDIGMPatch.BASS_DRUM_1_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument SHORT_WHISTLE_PERC = new Instrument(
            MIDIGMPatch.SHORT_WHISTLE_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument MARACAS_PERC = new Instrument(
            MIDIGMPatch.MARACAS_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument OPEN_CUICA_PERC = new Instrument(
            MIDIGMPatch.OPEN_CUICA_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument RIDE_CYMBAL_1_PERC = new Instrument(
            MIDIGMPatch.RIDE_CYMBAL_1_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument HI_MID_TOM_PERC = new Instrument(
            MIDIGMPatch.HI_MID_TOM_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument LOW_MID_TOM_PERC = new Instrument(
            MIDIGMPatch.LOW_MID_TOM_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument CLOSED_HI_HAT_PERC = new Instrument(
            MIDIGMPatch.CLOSED_HI_HAT_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument HIGH_TIMBALE_PERC = new Instrument(
            MIDIGMPatch.HIGH_TIMBALE_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument CRASH_CYMBAL_2_PERC = new Instrument(
            MIDIGMPatch.CRASH_CYMBAL_2_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument VIBRASLAP_PERC = new Instrument(
            MIDIGMPatch.VIBRASLAP_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument LONG_WHISTLE_PERC = new Instrument(
            MIDIGMPatch.LONG_WHISTLE_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument RIDE_CYMBAL_2_PERC = new Instrument(
            MIDIGMPatch.RIDE_CYMBAL_2_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument LOW_BONGO_PERC = new Instrument(
            MIDIGMPatch.LOW_BONGO_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument CHINESE_CYMBAL_PERC = new Instrument(
            MIDIGMPatch.CHINESE_CYMBAL_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument HIGH_AGOGO_PERC = new Instrument(
            MIDIGMPatch.HIGH_AGOGO_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument SIDE_STICK_PERC = new Instrument(
            MIDIGMPatch.SIDE_STICK_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument CLAVES_PERC = new Instrument(
            MIDIGMPatch.CLAVES_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument OPEN_HI_HAT_PERC = new Instrument(
            MIDIGMPatch.OPEN_HI_HAT_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument ACOUSTIC_SNARE_PERC = new Instrument(
            MIDIGMPatch.ACOUSTIC_SNARE_PERC, Pitch.C0, Pitch.C8);
    public static final Instrument LOW_CONGA_PERC = new Instrument(
            MIDIGMPatch.LOW_CONGA_PERC, Pitch.C0, Pitch.C8);

    // TODO do the rest or the ranges
    // http://en.wikipedia.org/wiki/Range_(music)

    // should I reference java sound's Instrument class?
    // Soundbank soundbank = synth.getDefaultSoundbank();
    // Instrument[] instr = soundbank.getInstruments();

    static {
//        all.add(PIANO);
    }
    
    /**
     * 
     */
    private MIDIGMPatch patch;
    /**
     * 
     */
    private String name;
    /**
     * 
     */
    private int minPitch;
    /**
     * 
     */
    private int maxPitch;

    /**
     * 
     */
   

    /**
     * 
     */
    public Instrument() {

    }

    /**
     * @param patch
     *            the patch to use
     * @param name
     *            the name
     * @param minPitch
     *            the min pitch
     * @param maxPitch
     *            the max pitch
     */
    public Instrument(MIDIGMPatch patch, String name, int minPitch, int maxPitch) {
        this.patch = patch;
        this.name = name;
        this.minPitch = minPitch;
        this.maxPitch = maxPitch;
        all.add(this);
        allMap.put(this.name, this);
    }

    public Instrument(MIDIGMPatch patch, int minPitch, int maxPitch) {
        this.patch = patch;
        this.name = patch.getName();
        this.minPitch = minPitch;
        this.maxPitch = maxPitch;
        all.add(this);
        allMap.put(this.name, this);
    }

    public static List<Instrument> getAll() {
        return all;
    }
    
    public static Instrument getByName(String name) {
        return allMap.get(name);
    }

    /**
     * @param pitch
     *            a MIDI number
     * @return true if it is in range for this instrument
     */
    public boolean isPitchInRange(int pitch) {
        return (pitch >= this.minPitch && pitch <= this.maxPitch);
    }

    /**
     * @param pitch
     *            a Pitch instance
     * @return true if it is in range for this instrument
     */
    public boolean isPitchInRange(Pitch pitch) {
        return (pitch.getMidiNumber() >= this.minPitch && pitch.getMidiNumber() <= this.maxPitch);
    }

    /**
     * see Effective Java
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + maxPitch;
        result = prime * result + minPitch;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((patch == null) ? 0 : patch.hashCode());
        return result;
    }

    /*
     * 
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Instrument other = (Instrument) obj;
        if (maxPitch != other.maxPitch) {
            return false;
        }
        if (minPitch != other.minPitch) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (patch != other.patch) {
            return false;
        }
        return true;
    }

    /*
     * 
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Instrument [patch=").append(patch).append(", name=")
                .append(name).append(", minPitch=").append(minPitch)
                .append(", maxPitch=").append(maxPitch).append("]");
        return builder.toString();
    }

    /**
     * @return the patch
     */
    public MIDIGMPatch getPatch() {
        return patch;
    }

    /**
     * @param patch
     *            the patch to set
     */
    public void setPatch(MIDIGMPatch patch) {
        this.patch = patch;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the minPitch
     */
    public int getMinPitch() {
        return minPitch;
    }

    /**
     * @param minPitch
     *            the minPitch to set
     */
    public void setMinPitch(int minPitch) {
        this.minPitch = minPitch;
    }

    /**
     * @return the maxPitch
     */
    public int getMaxPitch() {
        return maxPitch;
    }

    /**
     * @param maxPitch
     *            the maxPitch to set
     */
    public void setMaxPitch(int maxPitch) {
        this.maxPitch = maxPitch;
    }

}
