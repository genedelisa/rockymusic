package com.rockhoppertech.music.midi.gm;

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

import java.util.EnumSet;

/**
 * An update to MIDIUtils' and MIDIInstrument's hashmap chacha.
 * 
 * Compare
 * 
 * <pre>
 * int program = MIDIInstrument.getPatchNumber(MIDIInstrument.BARISAX_PATCH);
 * 
 * to
 * import static com.rockhoppertech.music.midi.gm.MIDIGMPatch.*;
 * int program = BARISAX.getProgram();
 * 
 * or even
 * new MIDINote(BARISAX, etc.
 * </pre>
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * @see <a href="http://www.midi.org/about-midi/gm/gm1sound.shtml">The GM
 *      spec.</a>
 */
public enum MIDIGMPatch {

    PIANO("Piano", 0),
    BRITEPIANO("BritePiano", 1),
    HAMMERPIANO("HammerPiano", 2),
    HONKEYTONK("HonkeyTonk", 3),
    NEWTINES("NewTines", 4),
    DIGIPIANO("DigiPiano", 5),
    HARPSICORD("Harpsicord", 6),
    CLAV("Clav", 7),
    CELESTA("Celesta", 8),
    GLOCKEN("Glocken", 9),
    MUSICBOX("MusicBox", 10),
    VIBES("Vibes", 11),
    MARIMBA("Marimba", 12),
    XYLOPHON("Xylophon", 13),
    TUBULAR("Tubular", 14),
    DULCIMER("Dulcimer", 15),
    FULLORGAN("FullOrgan", 16),
    PERCORGAN("PercOrgan", 17),
    BX3ORGAN("BX3Organ", 18),
    CHURCHPIPE("ChurchPipe", 19),
    POSITIVE("Positive", 20),
    MUSETTE("Musette", 21),
    HARMONICA("Harmonica", 22),
    TANGO("Tango", 23),
    CLASSICGTR("ClassicGtr", 24),
    ACOUSTICGUITAR("AcousticGuitar", 25),
    JAZZGUITAR("JazzGuitar", 26),
    CLEANGTR("CleanGtr", 27),
    MUTEGUITAR("MuteGuitar", 28),
    OVERDRIVE("OverDrive", 29),
    DISTGUITAR("DistGuitar", 30),
    ROCKMONICS("RockMonics", 31),
    JAZZBASS("JazzBass", 32),
    DEEPBASS("DeepBass", 33),
    PICKBASS("PickBass", 34),
    FRETLESS("FretLess", 35),
    SLAPBASS1("SlapBass1", 36),
    SLAPBASS2("SlapBass2", 37),
    SYNTHBASS1("SynthBass1", 38),
    SYNTHBASS2("SynthBass2", 39),
    VIOLIN("Violin", 40),
    VIOLA("Viola", 41),
    CELLO("Cello", 42),
    CONTRABASS("ContraBass", 43),
    TREMOLOSTR("TremoloStr", 44),
    PIZZICATO("Pizzicato", 45),
    HARP("Harp", 46),
    TIMPANI("Timpani", 47),
    StringEnsemble1("String Ensemble 1", 48),
    StringEnsemble2("String Ensemble 2", 49),
    SynthStrings1("SynthStrings 1", 50),
    SynthStrings2("SynthStrings 2", 51),
    CHOIR("Choir", 52),
    DOOVOICE("DooVoice", 53),
    VOICES("Voices", 54),
    ORCHHIT("OrchHit", 55),
    TRUMPET("Trumpet", 56),
    TROMBONE("Trombone", 57),
    TUBA("Tuba", 58),
    MUTEDTRUMPET("MutedTrumpet", 59),
    FRENCHHORN("FrenchHorn", 60),
    BRASS("Brass", 61),
    SYNBRASS1("SynBrass1", 62),
    SYNBRASS2("SynBrass2", 63),
    SOPRANOSAX("SopranoSax", 64),
    ALTOSAX("AltoSax", 65),
    TENORSAX("TenorSax", 66),
    BARISAX("BariSax", 67),
    OBOE("Oboe", 68),
    ENGLISHHORN("EnglishHorn", 69),
    BASSOON("Bassoon", 70),
    CLARINET("Clarinet", 71),
    PICCOLO("Piccolo", 72),
    FLUTE("Flute", 73),
    RECORDER("Recorder", 74),
    PANFLUTE("PanFlute", 75),
    BOTTLE("Bottle", 76),
    SHAKUHACHI("Shakuhachi", 77),
    WHISTLE("Whistle", 78),
    OCARINA("Ocarina", 79),
    SQUAREWAVE("SquareWave", 80),
    SAWWAVE("SawWave", 81),
    CALLIOPE("Calliope", 82),
    SYNCHIFF("SynChiff", 83),
    CHARANG("Charang", 84),
    AIRCHORUS("AirChorus", 85),
    FIFTHS("fifths", 86),
    BASSLEAD("BassLead", 87),
    NewAgePad("New Age", 88),
    WARMPAD("WarmPad", 89),
    POLYPAD("PolyPad", 90),
    GHOSTPAD("GhostPad", 91),
    BOWEDGLAS("BowedGlas", 92),
    METALPAD("MetalPad", 93),
    HALOPAD("HaloPad", 94),
    SWEEP("Sweep", 95),
    ICERAIN("IceRain", 96),
    SOUNDTRACK("SoundTrack", 97),
    CRYSTAL("Crystal", 98),
    ATMOSPHERE("Atmosphere", 99),
    BRIGHTNESS("Brightness", 100),
    GOBLIN("Goblin", 101),
    ECHODROP("EchoDrop", 102),
    SCIFI("SciFi effect", 103),
    SITAR("Sitar", 104),
    BANJO("Banjo", 105),
    SHAMISEN("Shamisen", 106),
    KOTO("Koto", 107),
    KALIMBA("Kalimba", 108),
    SCOTLAND("Scotland", 109),
    FIDDLE("Fiddle", 110),
    SHANAI("Shanai", 111),
    METALBELL("MetalBell", 112),
    AGOGO("Agogo", 113),
    STEELDRUMS("SteelDrums", 114),
    WOODBLOCK("Woodblock", 115),
    TAIKO("Taiko", 116),
    TOM("Tom", 117),
    SYNTHTOM("SynthTom", 118),
    REVCYMBAL("RevCymbal", 119),
    FRETNOISE("FretNoise", 120),
    NOISECHIFF("NoiseChiff", 121),
    SEASHORE("Seashore", 122),
    BIRDS("Birds", 123),
    TELEPHONE("Telephone", 124),
    HELICOPTER("Helicopter", 125),
    STADIUM("Stadium", 126),
    GUNSHOT("GunShot", 127),

    // 35-81
    OPEN_TRIANGLE_PERC("OPEN TRIANGLE", 81),
    COWBELL_PERC("COWBELL", 56),
    HI_BONGO_PERC("HI_BONGO", 60),
    LOW_FLOOR_TOM_PERC("LOW_FLOOR_TOM", 41),
    CABASA_PERC("CABASA", 69),
    LOW_WOOD_BLOCK_PERC("LOW_WOOD_BLOCK", 77),
    RIDE_BELL_PERC("RIDE_BELL", 53),
    ELECTRIC_SNARE_PERC("ELECTRIC_SNARE", 40),
    LONG_GUIRO_PERC("LONG_GUIRO", 74),
    CRASH_CYMBAL_1_PERC("CRASH_CYMBAL_1", 49),
    MUTE_CUICA_PERC("MUTE_CUICA", 78),
    HAND_CLAP_PERC("HAND_CLAP", 39),
    HIGH_FLOOR_TOM_PERC("HIGH_FLOOR_TOM", 43),
    ACOUSTIC_BASS_DRUM_PERC("ACOUSTIC_BASS_DRUM", 35),
    LOW_TOM_PERC("LOW_TOM", 45),
    SHORT_GUIRO_PERC("SHORT_GUIRO", 73),
    HI_WOOD_BLOCK_PERC("HI_WOOD_BLOCK", 76),
    LOW_TIMBALE_PERC("LOW_TIMBALE", 66),
    LOW_AGOGO_PERC("LOW_AGOGO", 68),
    MUTE_TRIANGLE_PERC("MUTE_TRIANGLE", 80),
    OPEN_HI_CONGA_PERC("OPEN_HI_CONGA", 63),
    MUTE_HI_CONGA_PERC("MUTE_HI_CONGA", 62),
    TAMBOURINE_PERC("TAMBOURINE", 54),
    PEDAL_HI_HAT_PERC("PEDAL_HI_HAT", 44),
    HIGH_TOM_PERC("HIGH_TOM", 50),
    SPLASH_CYMBAL_PERC("SPLASH_CYMBAL", 55),
    BASS_DRUM_1_PERC("BASS_DRUM_1", 36),
    SHORT_WHISTLE_PERC("SHORT_WHISTLE", 71),
    MARACAS_PERC("MARACAS", 70),
    OPEN_CUICA_PERC("OPEN_CUICA", 79),
    RIDE_CYMBAL_1_PERC("RIDE_CYMBAL_1", 51),
    HI_MID_TOM_PERC("HI_MID_TOM", 48),
    LOW_MID_TOM_PERC("LOW_MID_TOM", 47),
    CLOSED_HI_HAT_PERC("CLOSED_HI_HAT", 42),
    HIGH_TIMBALE_PERC("HIGH_TIMBALE", 65),
    CRASH_CYMBAL_2_PERC("CRASH_CYMBAL_2", 57),
    VIBRASLAP_PERC("VIBRASLAP", 58),
    LONG_WHISTLE_PERC("LONG_WHISTLE", 72),
    RIDE_CYMBAL_2_PERC("RIDE_CYMBAL_2", 59),
    LOW_BONGO_PERC("LOW_BONGO", 61),
    CHINESE_CYMBAL_PERC("CHINESE_CYMBAL", 52),
    HIGH_AGOGO_PERC("HIGH_AGOGO", 67),
    SIDE_STICK_PERC("SIDE_STICK", 37),
    CLAVES_PERC("CLAVES", 75),
    OPEN_HI_HAT_PERC("OPEN_HI_HAT", 46),
    ACOUSTIC_SNARE_PERC("ACOUSTIC_SNARE", 38),
    LOW_CONGA_PERC("LOW_CONGA", 64);

    int p;
    String name;

    /**
     * @param name
     * @param p
     */
    MIDIGMPatch(String name, int p) {
        this.p = p;
        this.name = name;

    }

    /**
     * @return the program
     */
    public int getProgram() {
        return p;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    // static EnumMap<MIDIGMPatch, String> map = new EnumMap<MIDIGMPatch,
    // String>(
    // MIDIGMPatch.class);

    // families
    /**
	 * 
	 */
    public static volatile EnumSet<MIDIGMPatch> pianos;
    /**
	 * 
	 */
    public static volatile EnumSet<MIDIGMPatch> chromaticPercussion;
    /**
	 * 
	 */
    public static volatile EnumSet<MIDIGMPatch> organ;
    /**
	 * 
	 */
    public static volatile EnumSet<MIDIGMPatch> guitar;
    /**
	 * 
	 */
    public static volatile EnumSet<MIDIGMPatch> bass;
    /**
	 * 
	 */
    public static volatile EnumSet<MIDIGMPatch> strings;
    /**
	 * 
	 */
    public static volatile EnumSet<MIDIGMPatch> ensemble;
    /**
	 * 
	 */
    public static volatile EnumSet<MIDIGMPatch> brass;
    /**
	 * 
	 */
    public static volatile EnumSet<MIDIGMPatch> reed;
    /**
	 * 
	 */
    public static volatile EnumSet<MIDIGMPatch> pipe;
    /**
	 * 
	 */
    public static volatile EnumSet<MIDIGMPatch> synthLead;
    /**
	 * 
	 */
    public static volatile EnumSet<MIDIGMPatch> synthPad;
    /**
	 * 
	 */
    public static volatile EnumSet<MIDIGMPatch> synthEffects;
    /**
	 * 
	 */
    public static volatile EnumSet<MIDIGMPatch> ethnic;
    /**
	 * 
	 */
    public static volatile EnumSet<MIDIGMPatch> percussive;
    /**
	 * 
	 */
    public static volatile EnumSet<MIDIGMPatch> soundEffects;
    /**
     * 
     */
    private static volatile EnumSet<MIDIGMPatch> percussion;

    /**
     * 
     */
    private static volatile EnumSet<MIDIGMPatch> allPitched;

    ;

    /**
     * @return an {@code EnumSet} of the piano patches
     */
    public static EnumSet<MIDIGMPatch> getPianos() {
        if (pianos == null) {
            pianos = EnumSet.range(MIDIGMPatch.PIANO,
                    MIDIGMPatch.CLAV);
        }
        return pianos;
    }

    /**
     * 
     * @return an {@code EnumSet} of the percussion patches
     */
    public static EnumSet<MIDIGMPatch> getChromaticPercussion() {
        if (chromaticPercussion == null) {
            chromaticPercussion = EnumSet.range(MIDIGMPatch.CELESTA,
                    MIDIGMPatch.DULCIMER);
        }
        return chromaticPercussion;
    }

    /**
     * 
     * @return an {@code EnumSet} of the organ patches
     */
    public static EnumSet<MIDIGMPatch> getOrgans() {
        if (organ == null) {
            organ = EnumSet.range(MIDIGMPatch.FULLORGAN,
                    MIDIGMPatch.TANGO);
        }
        return organ;
    }

    /**
     * @return an {@code EnumSet} of the guitar patches
     */
    public static EnumSet<MIDIGMPatch> getGuitars() {
        if (guitar == null) {
            guitar = EnumSet.range(MIDIGMPatch.CLASSICGTR,
                    MIDIGMPatch.ROCKMONICS);
        }
        return guitar;
    }

    /**
     * @return an {@code EnumSet} of the bass patches
     */
    public static EnumSet<MIDIGMPatch> getBasses() {
        if (bass == null) {
            bass = EnumSet.range(MIDIGMPatch.JAZZBASS,
                    MIDIGMPatch.SYNTHBASS2);
        }
        return bass;
    }

    /**
     * @return an {@code EnumSet} of the string patches
     */
    public static EnumSet<MIDIGMPatch> getStrings() {
        if (strings == null) {
            strings = EnumSet.range(MIDIGMPatch.VIOLIN,
                    MIDIGMPatch.TIMPANI);
            // yeah. I know. Timpani. on what planet is that a stringed
            // instrument?
        }
        return strings;
    }

    /**
     * @return an {@code EnumSet} of the ensemble patches
     */
    public static EnumSet<MIDIGMPatch> getEnsemble() {
        if (ensemble == null) {
            ensemble = EnumSet.range(MIDIGMPatch.StringEnsemble1,
                    MIDIGMPatch.ORCHHIT);
        }
        return ensemble;
    }

    /**
     * @return an {@code EnumSet} of the brass patches
     */
    public static EnumSet<MIDIGMPatch> getBrass() {
        if (brass == null) {
            brass = EnumSet.range(MIDIGMPatch.TRUMPET,
                    MIDIGMPatch.SYNBRASS2);
        }
        return brass;
    }

    /**
     * @return an {@code EnumSet} of the redd patches
     */
    public static EnumSet<MIDIGMPatch> getReeds() {
        if (reed == null) {
            reed = EnumSet.range(MIDIGMPatch.SOPRANOSAX,
                    MIDIGMPatch.CLARINET);
        }
        return reed;
    }

    /**
     * @return an {@code EnumSet} of the pipe patches
     */
    public static EnumSet<MIDIGMPatch> getPipes() {
        if (pipe == null) {
            pipe = EnumSet.range(MIDIGMPatch.PICCOLO,
                    MIDIGMPatch.OCARINA);
        }
        return pipe;
    }

    /**
     * @return an {@code EnumSet} of the synth lead patches
     */
    public static EnumSet<MIDIGMPatch> getSynthLeads() {
        if (synthLead == null) {
            synthLead = EnumSet.range(MIDIGMPatch.SQUAREWAVE,
                    MIDIGMPatch.BASSLEAD);
        }
        return synthLead;
    }

    /**
     * @return an {@code EnumSet} of the synth pads patches
     */
    public static EnumSet<MIDIGMPatch> getSynthPads() {
        if (synthPad == null) {
            synthPad = EnumSet.range(MIDIGMPatch.NewAgePad,
                    MIDIGMPatch.SWEEP);
        }
        return synthPad;
    }

    /**
     * @return an {@code EnumSet} of the synth effects patches
     */
    public static EnumSet<MIDIGMPatch> getSynthEffects() {
        if (synthEffects == null) {
            synthEffects = EnumSet.range(MIDIGMPatch.ICERAIN,
                    MIDIGMPatch.SCIFI);
        }
        return synthEffects;
    }

    /**
     * @return an {@code EnumSet} of the ethnic patches
     */
    public static EnumSet<MIDIGMPatch> getEthnic() {
        if (ethnic == null) {
            ethnic = EnumSet.range(MIDIGMPatch.SITAR,
                    MIDIGMPatch.SHANAI);
        }
        return ethnic;
    }

    /**
     * @return an {@code EnumSet} of the percussive patches
     */
    public static EnumSet<MIDIGMPatch> getPercussive() {
        if (percussive == null) {
            percussive = EnumSet.range(MIDIGMPatch.METALBELL,
                    MIDIGMPatch.REVCYMBAL);
        }
        return percussive;
    }

    /**
     * @return an {@code EnumSet} of the percussion patches
     */
    public static EnumSet<MIDIGMPatch> getPercussion() {
        if (percussion == null) {
            percussion = EnumSet.range(MIDIGMPatch.OPEN_TRIANGLE_PERC,
                    MIDIGMPatch.LOW_CONGA_PERC);
        }
        return percussion;
    }

    /**
     * @return an {@code EnumSet} of the sound effect patches
     */
    public static EnumSet<MIDIGMPatch> getSoundEffects() {
        if (soundEffects == null) {
            soundEffects = EnumSet.range(MIDIGMPatch.FRETNOISE,
                    MIDIGMPatch.GUNSHOT);
        }
        return soundEffects;
    }

    /**
     * @return a set of MIDIGMPatches for non percussion insts
     */
    public static EnumSet<MIDIGMPatch> getAllPitched() {
        if (allPitched == null) {
            allPitched = EnumSet.range(MIDIGMPatch.PIANO,
                    MIDIGMPatch.GUNSHOT);
        }
        return allPitched;
    }

    /**
     * @param name the patch name
     * @return a patch
     */
    public static MIDIGMPatch getPatch(String name) {
        MIDIGMPatch match = null;
        for (MIDIGMPatch patch : MIDIGMPatch.values()) {
            if (patch.getName().equals(name)) {
                match = patch;
                break;
            }
        }
        return match;
    }

    /**
     * Match's the supplied program with the patche's program.
     * @param program the program
     * @return the name
     */
    public static String getName(int program) {
        String match = null;
        for (MIDIGMPatch patch : MIDIGMPatch.values()) {
            if (patch.getProgram() == program) {
                match = patch.getName();
                break;
            }
        }
        return match;
    }
    /*
     * http://www.midi.org/about-midi/gm/gm1sound.shtml
     */
}
