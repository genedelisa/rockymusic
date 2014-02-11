/**
 * 
 */
package com.rockhoppertech.music.fx.musicfont.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Duration;

/**
 * <p>
 * Since each music font has the symbols in different locations we have to do
 * this tap dance.
 * </p>
 * 
 * 
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 * 
 */

public class Symbols {

    final static Logger logger = LoggerFactory.getLogger(Symbols.class);

    // unicode
    // public static final String ARPEGGIATO_DOWN="Arpeggiato_down";
    // public static final String ARPEGGIATO_UP="Arpeggiato_up";
    // public static final String BEGIN_BEAM="Begin_beam";
    // public static final String BEGIN_PHRASE="Begin_phrase";
    // public static final String BEGIN_SLUR="Begin_slur";
    // public static final String BEGIN_TIE="Begin_tie";
    // public static final String BRACE="Brace";
    // public static final String BRACKET="Bracket";
    // public static final String BREATH_MARK="Breath_mark";
    // public static final String BREVE="Breve";
    // public static final String BREVIS="Brevis";
    // public static final String BREVIS_REST="Brevis_rest";
    // public static final String CAESURA="Caesura";
    // public static final String CIRCLE_X_NOTEHEAD="Circle_x_notehead";
    // public static final String CLIMACUS="Climacus";
    // public static final String CLIVIS="Clivis";
    // public static final String
    // CLUSTER_NOTEHEAD_BLACK="Cluster_notehead_black";
    // public static final String
    // CLUSTER_NOTEHEAD_WHITE="Cluster_notehead_white";
    // public static final String CODA="Coda";
    // public static final String COMBINING_ACCENT="Combining_accent";
    // public static final String
    // COMBINING_ACCENT_STACCATO="Combining_accent_staccato";
    // public static final String
    // COMBINING_AUGMENTATION_DOT="Combining_augmentation_dot";
    // public static final String COMBINING_BEND="Combining_bend";
    // public static final String COMBINING_DOIT="Combining_doit";
    // public static final String
    // COMBINING_DOUBLE_TONGUE="Combining_double_tongue";
    // public static final String COMBINING_DOWN_BOW="Combining_down_bow";
    // public static final String COMBINING_FLAG_1="Combining_flag_1";
    // public static final String COMBINING_FLAG_2="Combining_flag_2";
    // public static final String COMBINING_FLAG_3="Combining_flag_3";
    // public static final String COMBINING_FLAG_4="Combining_flag_4";
    // public static final String COMBINING_FLAG_5="Combining_flag_5";
    // public static final String COMBINING_FLIP="Combining_flip";
    // public static final String COMBINING_HARMONIC="Combining_harmonic";
    // public static final String COMBINING_LOURE="Combining_loure";
    // public static final String COMBINING_MARCATO="Combining_marcato";
    // public static final String
    // COMBINING_MARCATO_STACCATO="Combining_marcato_staccato";
    // public static final String COMBINING_RIP="Combining_rip";
    // public static final String COMBINING_SMEAR="Combining_smear";
    // public static final String
    // COMBINING_SNAP_PIZZICATO="Combining_snap_pizzicato";
    // public static final String
    // COMBINING_SPRECHGESANG_STEM="Combining_sprechgesang_stem";
    // public static final String
    // COMBINING_STACCATISSIMO="Combining_staccatissimo";
    // public static final String COMBINING_STACCATO="Combining_staccato";
    // public static final String COMBINING_STEM="Combining_stem";
    // public static final String COMBINING_TENUTO="Combining_tenuto";
    // public static final String COMBINING_TREMOLO_1="Combining_tremolo_1";
    // public static final String COMBINING_TREMOLO_2="Combining_tremolo_2";
    // public static final String COMBINING_TREMOLO_3="Combining_tremolo_3";
    // public static final String
    // COMBINING_TRIPLE_TONGUE="Combining_triple_tongue";
    // public static final String COMBINING_UP_BOW="Combining_up_bow";
    // public static final String COMMON_TIME="Common_time";
    // public static final String CRESCENDO="Crescendo";
    // public static final String CROIX="Croix";
    // public static final String CUT_TIME="Cut_time";
    // public static final String C_CLEF="C_clef";
    // public static final String DAL_SEGNO="Dal_segno";
    // public static final String DAMP="Damp";
    // public static final String DAMP_ALL="Damp_all";
    // public static final String DASHED_BARLINE="Dashed_barline";
    // public static final String DA_CAPO="Da_capo";
    // public static final String DECRESCENDO="Decrescendo";
    // public static final String DEGREE_SLASH="Degree_slash";
    // public static final String DOUBLE_BARLINE="Double_barline";
    // public static final String DOUBLE_FLAT="Double_flat";
    // public static final String DOUBLE_SHARP="Double_sharp";
    // public static final String DRUM_CLEF_1="Drum_clef_1";
    // public static final String DRUM_CLEF_2="Drum_clef_2";
    // public static final String EIGHTH_NOTE="Eighth_note";
    // public static final String EIGHTH_REST="Eighth_rest";
    // public static final String END_BEAM="End_beam";
    // public static final String END_OF_STIMME="End_of_stimme";
    // public static final String END_PHRASE="End_phrase";
    // public static final String END_SLUR="End_slur";
    // public static final String END_TIE="End_tie";
    // public static final String FERMATA="Fermata";
    // public static final String FERMATA_BELOW="Fermata_below";
    // public static final String FINAL_BARLINE="Final_barline";
    // public static final String FINGERED_TREMOLO_1="Fingered_tremolo_1";
    // public static final String FINGERED_TREMOLO_2="Fingered_tremolo_2";
    // public static final String FINGERED_TREMOLO_3="Fingered_tremolo_3";
    // public static final String FIVE_LINE_STAFF="Five_line_staff";
    // public static final String FLAT_DOWN="Flat_down";
    // public static final String FLAT_UP="Flat_up";
    // public static final String FORTE="Forte";
    // public static final String FOUR_LINE_STAFF="Four_line_staff";
    // public static final String FOUR_STRING_FRETBOARD="Four_string_fretboard";
    // public static final String FUSA_BLACK="Fusa_black";
    // public static final String FUSA_WHITE="Fusa_white";
    // public static final String F_CLEF="F_clef";
    // public static final String F_CLEF_OTTAVA_ALTA="F_clef_ottava_alta";
    // public static final String F_CLEF_OTTAVA_BASSA="F_clef_ottava_bassa";
    // public static final String GLISSANDO_DOWN="Glissando_down";
    // public static final String GLISSANDO_UP="Glissando_up";
    // public static final String GRACE_NOTE_NO_SLASH="Grace_note_no_slash";
    // public static final String GRACE_NOTE_SLASH="Grace_note_slash";
    // public static final String GREGORIAN_C_CLEF="Gregorian_c_clef";
    // public static final String GREGORIAN_F_CLEF="Gregorian_f_clef";
    // public static final String G_CLEF="G_clef";
    // public static final String G_CLEF_OTTAVA_ALTA="G_clef_ottava_alta";
    // public static final String G_CLEF_OTTAVA_BASSA="G_clef_ottava_bassa";
    // public static final String HALF_NOTE="Half_note";
    // public static final String HALF_PEDAL_MARK="Half_pedal_mark";
    // public static final String HALF_REST="Half_rest";
    // public static final String HAUPTSTIMME="Hauptstimme";
    // public static final String INVERTED_TURN="Inverted_turn";
    // public static final String LEFT_REPEAT_SIGN="Left_repeat_sign";
    // public static final String LONGA="Longa";
    // public static final String LONGA_IMPERFECTA_REST="Longa_imperfecta_rest";
    // public static final String LONGA_PERFECTA_REST="Longa_perfecta_rest";
    // public static final String MAXIMA="Maxima";
    // public static final String MEZZO="Mezzo";
    // public static final String MINIMA="Minima";
    // public static final String MINIMA_BLACK="Minima_black";
    // public static final String MINIMA_REST="Minima_rest";
    // public static final String MOON_NOTEHEAD_BLACK="Moon_notehead_black";
    // public static final String MOON_NOTEHEAD_WHITE="Moon_notehead_white";
    // public static final String MULTIPLE_MEASURE_REST="Multiple_measure_rest";
    // public static final String MULTI_REST="Multi_rest";
    // public static final String NATURAL_DOWN="Natural_down";
    // public static final String NATURAL_UP="Natural_up";
    // public static final String NEBENSTIMME="Nebenstimme";
    // public static final String NOTEHEAD_BLACK="Notehead_black";
    // public static final String NULL_NOTEHEAD="Null_notehead";
    // public static final String
    // ONE_HUNDRED_TWENTY_EIGHTH_NOTE="One_hundred_twenty_eighth_note";
    // public static final String
    // ONE_HUNDRED_TWENTY_EIGHTH_REST="One_hundred_twenty_eighth_rest";
    // public static final String ONE_LINE_STAFF="One_line_staff";
    // public static final String ORNAMENT_STROKE_1="Ornament_stroke_1";
    // public static final String ORNAMENT_STROKE_10="Ornament_stroke_10";
    // public static final String ORNAMENT_STROKE_11="Ornament_stroke_11";
    // public static final String ORNAMENT_STROKE_2="Ornament_stroke_2";
    // public static final String ORNAMENT_STROKE_3="Ornament_stroke_3";
    // public static final String ORNAMENT_STROKE_4="Ornament_stroke_4";
    // public static final String ORNAMENT_STROKE_5="Ornament_stroke_5";
    // public static final String ORNAMENT_STROKE_6="Ornament_stroke_6";
    // public static final String ORNAMENT_STROKE_7="Ornament_stroke_7";
    // public static final String ORNAMENT_STROKE_8="Ornament_stroke_8";
    // public static final String ORNAMENT_STROKE_9="Ornament_stroke_9";
    // public static final String OTTAVA_ALTA="Ottava_alta";
    // public static final String OTTAVA_BASSA="Ottava_bassa";
    // public static final String PARENTHESIS_NOTEHEAD="Parenthesis_notehead";
    // public static final String PEDAL_MARK="Pedal_mark";
    // public static final String PEDAL_UP_MARK="Pedal_up_mark";
    // public static final String PES_SUBPUNCTIS="Pes_subpunctis";
    // public static final String PIANO="Piano";
    // public static final String PLUS_NOTEHEAD="Plus_notehead";
    // public static final String PODATUS="Podatus";
    // public static final String PORRECTUS="Porrectus";
    // public static final String PORRECTUS_FLEXUS="Porrectus_flexus";
    // public static final String QUARTER_NOTE="Quarter_note";
    // public static final String QUARTER_REST="Quarter_rest";
    // public static final String QUARTER_TONE_FLAT="Quarter_tone_flat";
    // public static final String QUARTER_TONE_SHARP="Quarter_tone_sharp";
    // public static final String QUINDICESIMA_ALTA="Quindicesima_alta";
    // public static final String QUINDICESIMA_BASSA="Quindicesima_bassa";
    // public static final String REPEATED_FIGURE_1="Repeated_figure_1";
    // public static final String REPEATED_FIGURE_2="Repeated_figure_2";
    // public static final String REPEATED_FIGURE_3="Repeated_figure_3";
    // public static final String REPEAT_DOTS="Repeat_dots";
    // public static final String REVERSE_FINAL_BARLINE="Reverse_final_barline";
    // public static final String RIGHT_REPEAT_SIGN="Right_repeat_sign";
    // public static final String RINFORZANDO="Rinforzando";
    // public static final String SCANDICUS="Scandicus";
    // public static final String SCANDICUS_FLEXUS="Scandicus_flexus";
    // public static final String SEGNO="Segno";
    // public static final String SEMIBREVIS_BLACK="Semibrevis_black";
    // public static final String SEMIBREVIS_REST="Semibrevis_rest";
    // public static final String SEMIBREVIS_WHITE="Semibrevis_white";
    // public static final String SEMIMINIMA_BLACK="Semiminima_black";
    // public static final String SEMIMINIMA_REST="Semiminima_rest";
    // public static final String SEMIMINIMA_WHITE="Semiminima_white";
    // public static final String SHARP_DOWN="Sharp_down";
    // public static final String SHARP_UP="Sharp_up";
    // public static final String SHORT_BARLINE="Short_barline";
    // public static final String SINGLE_BARLINE="Single_barline";
    // public static final String SIXTEENTH_NOTE="Sixteenth_note";
    // public static final String SIXTEENTH_REST="Sixteenth_rest";
    // public static final String SIXTY_FOURTH_NOTE="Sixty_fourth_note";
    // public static final String SIXTY_FOURTH_REST="Sixty_fourth_rest";
    // public static final String SIX_LINE_STAFF="Six_line_staff";
    // public static final String SIX_STRING_FRETBOARD="Six_string_fretboard";
    // public static final String SQUARE_B="Square_b";
    // public static final String SQUARE_NOTEHEAD_BLACK="Square_notehead_black";
    // public static final String SQUARE_NOTEHEAD_WHITE="Square_notehead_white";
    // public static final String SUBITO="Subito";
    // public static final String
    // TEMPUS_IMPERFECTUM_CUM_PROLATIONE_IMPERFECTA="Tempus_imperfectum_cum_prolatione_imperfecta";
    // public static final String
    // TEMPUS_IMPERFECTUM_CUM_PROLATIONE_IMPERFECTA_DIMINUTION_1="Tempus_imperfectum_cum_prolatione_imperfecta_diminution_1";
    // public static final String
    // TEMPUS_IMPERFECTUM_CUM_PROLATIONE_IMPERFECTA_DIMINUTION_2="Tempus_imperfectum_cum_prolatione_imperfecta_diminution_2";
    // public static final String
    // TEMPUS_IMPERFECTUM_CUM_PROLATIONE_IMPERFECTA_DIMINUTION_3="Tempus_imperfectum_cum_prolatione_imperfecta_diminution_3";
    // public static final String
    // TEMPUS_IMPERFECTUM_CUM_PROLATIONE_PERFECTA="Tempus_imperfectum_cum_prolatione_perfecta";
    // public static final String
    // TEMPUS_PERFECTUM_CUM_PROLATIONE_IMPERFECTA="Tempus_perfectum_cum_prolatione_imperfecta";
    // public static final String
    // TEMPUS_PERFECTUM_CUM_PROLATIONE_PERFECTA="Tempus_perfectum_cum_prolatione_perfecta";
    // public static final String
    // TEMPUS_PERFECTUM_CUM_PROLATIONE_PERFECTA_DIMINUTION_1="Tempus_perfectum_cum_prolatione_perfecta_diminution_1";
    // public static final String THIRTY_SECOND_NOTE="Thirty_second_note";
    // public static final String THIRTY_SECOND_REST="Thirty_second_rest";
    // public static final String THREE_LINE_STAFF="Three_line_staff";
    // public static final String TORCULUS="Torculus";
    // public static final String TORCULUS_RESUPINUS="Torculus_resupinus";
    // public static final String TR="Tr";
    // public static final String
    // TRIANGLE_NOTEHEAD_DOWN_BLACK="Triangle_notehead_down_black";
    // public static final String
    // TRIANGLE_NOTEHEAD_DOWN_WHITE="Triangle_notehead_down_white";
    // public static final String
    // TRIANGLE_NOTEHEAD_LEFT_BLACK="Triangle_notehead_left_black";
    // public static final String
    // TRIANGLE_NOTEHEAD_LEFT_WHITE="Triangle_notehead_left_white";
    // public static final String
    // TRIANGLE_NOTEHEAD_RIGHT_BLACK="Triangle_notehead_right_black";
    // public static final String
    // TRIANGLE_NOTEHEAD_RIGHT_WHITE="Triangle_notehead_right_white";
    // public static final String
    // TRIANGLE_NOTEHEAD_UP_BLACK="Triangle_notehead_up_black";
    // public static final String
    // TRIANGLE_NOTEHEAD_UP_RIGHT_BLACK="Triangle_notehead_up_right_black";
    // public static final String
    // TRIANGLE_NOTEHEAD_UP_RIGHT_WHITE="Triangle_notehead_up_right_white";
    // public static final String
    // TRIANGLE_NOTEHEAD_UP_WHITE="Triangle_notehead_up_white";
    // public static final String
    // TRIANGLE_ROUND_NOTEHEAD_DOWN_BLACK="Triangle_round_notehead_down_black";
    // public static final String
    // TRIANGLE_ROUND_NOTEHEAD_DOWN_WHITE="Triangle_round_notehead_down_white";
    // public static final String TURN="Turn";
    // public static final String TURN_SLASH="Turn_slash";
    // public static final String TURN_UP="Turn_up";
    // public static final String TWO_LINE_STAFF="Two_line_staff";
    // public static final String VIRGA="Virga";
    // public static final String VOID_NOTEHEAD="Void_notehead";
    // public static final String WHOLE_NOTE="Whole_note";
    // public static final String WHOLE_REST="Whole_rest";
    // public static final String WITH_FINGERNAILS="With_fingernails";
    // public static final String X_NOTEHEAD="X_notehead";
    // public static final String Z="Z";

    // constants for the map.
    // TODO get rid of these
    public static final String EIGHT = "eight";
    public static final String FFF = "fff";
    public static final String TREBLECLEF = "trebleClef";
    public static final String ONETWENTYEIGHTHREST = "oneTwentyEighthRest";
    public static final String P = "p";
    public static final String SQUARENOTEHEAD = "squareNoteHead";
    public static final String ENDREPEAT = "endRepeat";
    public static final String TR = "tr";
    public static final String SIXTEENTHREST = "sixteenthRest";
    public static final String SIX = "six";
    public static final String BEGINDOUBLEBAR = "beginDoubleBar";
    public static final String MP = "mp";
    public static final String QUARTERREST = "quarterRest";
    public static final String TOPBRACE = "topBrace";
    public static final String PED = "ped";
    public static final String EIGHTHDOWN = "eighthDown";
    public static final String DS = "ds";
    public static final String TENUTO = "tenuto";
    public static final String DOWNBOW = "downBow";
    public static final String ZERO = "zero";
    public static final String FLAT = "flat";
    public static final String SIXTYFOURTHREST = "sixtyFourthRest";
    public static final String BOTTOMBRACE = "bottomBrace";
    public static final String THREE = "three";
    public static final String MF = "mf";
    public static final String DOUBLEWHOLENOTESQUARE = "doubleWholeNoteSquare";
    public static final String HALFNOTEHEAD = "halfNoteHead";
    public static final String HALFREST = "halfRest";
    public static final String SAMEMEASURE = "sameMeasure";
    public static final String ONE = "one";
    public static final String UPPERMORDENT = "upperMordent";
    public static final String SOLIDNOTEHEAD = "solidNoteHead";
    public static final String FF = "ff";
    public static final String TREMOLO = "tremolo";
    public static final String NATURAL = "natural";
    public static final String QUARTERUP = "quarterUp";
    public static final String WHOLEREST = "wholeRest";
    public static final String TURN = "turn";
    public static final String HALFUP = "halfUp";
    public static final String LOWERMORDENT = "lowerMordent";
    public static final String ACCENT = "accent";
    public static final String SHARP = "sharp";
    public static final String THIRTYSECONDREST = "thirtySecondRest";
    public static final String HALFDOWN = "halfDown";
    public static final String CCLEF = "cClef";
    public static final String BASSCLEF = "bassClef";
    public static final String SF = "sf";
    public static final String F = "f";
    public static final String THIRTYSECONDUP = "thirtySecondUp";
    public static final String OTTAVA = "ottava";
    public static final String FOUR = "four";
    public static final String QUARTERDOWN = "quarterDown";
    public static final String FZ = "fz";
    public static final String FIVE = "five";
    public static final String STAFF = "staff";
    public static final String ONETWENTYEIGHTHUP = "oneTwentyEighthUp"; // there
    // is no
    // down
    public static final String SEVEN = "seven";
    public static final String SIXTEENTHDOWN = "sixteenthDown";
    public static final String ENDDOUBLEBAR = "endDoubleBar";
    public static final String CUTTIME = "cutTime";
    public static final String FERMATA = "fermata";
    public static final String DOUBLESHARP = "doubleSharp";
    public static final String DOUBLEWHOLENOTE = "doubleWholeNote";
    public static final String SEGNO = "segno";
    public static final String EIGHTHUP = "eighthUp";
    public static final String NINE = "nine";
    public static final String BEGINREPEAT = "beginRepeat";
    public static final String SIXTEENTHUP = "sixteenthUp";
    public static final String THIRTYSECONDDOWN = "thirtySecondDown";
    public static final String PP = "pp";
    public static final String SOLIDNOTEHEAD2 = "solidNoteHead2";
    public static final String PPP = "ppp";
    public static final String BARLINE = "barline";
    public static final String EIGHTHREST = "eighthRest";
    public static final String WHOLENOTE = "wholeNote";
    public static final String COMMONTIME = "commonTime";
    public static final String QUARTERREST2 = "quarterRest2";
    public static final String TWO = "two";
    public static final String DC = "dc";
    public static final String CODA = "coda";
    public static final String STACCATO = "staccato";
    public static final String THICKBAR = "thickBar";
    public static final String DOT = "dot";
    public static final String SIXTYFOURTHUP = "sixtyfourthup";
    public static final String SIXTYFOURTHDOWN = "sixtyfourthdown";
    public static final String LEDGERLINE = "ledgerLine";

    public static Map<String, Character> fontMap = new HashMap<String, Character>();
    // all based on beat = quarter note
    public static Map<Double, String> durationMap = new HashMap<Double, String>();
    public static Map<Double, String> durationFlagDownMap = new HashMap<Double, String>();
    public static Map<Double, String> durationNoFlagMap = new HashMap<Double, String>();
    public static Map<Double, String> restMap = new HashMap<Double, String>();

    static {
        setMapDefaults();

        Properties p = new Properties();
        try {
            InputStream inputstream = Symbols.class.getResourceAsStream("/"
                    + "musicsymbols.properties");

            // FileInputStream fis = new
            // FileInputStream("musicsymbols.properties");
            p.load(inputstream);

            // the properties entryset is object, object not string,string. go
            // figure.
            for (Map.Entry<Object, Object> e : p.entrySet()) {
                String val = (String) e.getValue();
                int code = 0;
                if (val.startsWith("0x")) {
                    // can you believe that parseInt cannot handle the 0x
                    // prefix?
                    code = Integer.parseInt(val.substring(2),
                            16);
                } else {
                    code = Integer.parseInt(val);
                }

                Character c = new Character((char) code);
                // char cc = Character.forDigit(code,10);
                // if (logger.isDebugEnabled()) {
                // String s = String.format("code %d cc '%c'",code, cc);
                // logger.debug(s);
                // }

                fontMap.put((String) e.getKey(),
                        c);
                logger.debug((String) e.getKey() + " " + c);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error(e.getLocalizedMessage(), e);
            setMapDefaults();
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
            e.printStackTrace();
            setMapDefaults();
        }
    }

    static void setMapDefaults() {
        if (logger.isDebugEnabled()) {
            logger.debug("map defaults");
        }
        initFontMap();
        initDurationMap();
        initRestMap();
    }

    static void initFontMap() {
        fontMap.put(EIGHT,
                new Character((char) 56));
        fontMap.put(FFF,
                new Character((char) 236));
        fontMap.put(TREBLECLEF,
                new Character((char) 38));
        fontMap.put(ONETWENTYEIGHTHREST,
                new Character((char) 229));
        fontMap.put(P,
                new Character((char) 112));
        fontMap.put(SQUARENOTEHEAD,
                new Character((char) 208));
        fontMap.put(ENDREPEAT,
                new Character((char) 125));
        fontMap.put(TR,
                new Character((char) 217));
        fontMap.put(SIXTEENTHREST,
                new Character((char) 197));
        fontMap.put(SIX,
                new Character((char) 54));
        fontMap.put(BEGINDOUBLEBAR,
                new Character((char) 210));
        fontMap.put(MP,
                new Character((char) 80));
        fontMap.put(QUARTERREST,
                new Character((char) 103));
        fontMap.put(TOPBRACE,
                new Character((char) 167));
        fontMap.put(PED,
                new Character((char) 161));
        fontMap.put(EIGHTHDOWN,
                new Character((char) 69));
        fontMap.put(DS,
                new Character((char) 100));
        fontMap.put(TENUTO,
                new Character((char) 45));
        fontMap.put(DOWNBOW,
                new Character((char) 179));
        fontMap.put(ZERO,
                new Character((char) 48));
        fontMap.put(FLAT,
                new Character((char) 98));
        fontMap.put(SIXTYFOURTHREST,
                new Character((char) 244));
        fontMap.put(BOTTOMBRACE,
                new Character((char) 234));
        fontMap.put(THREE,
                new Character((char) 51));
        fontMap.put(MF,
                new Character((char) 70));
        fontMap.put(DOUBLEWHOLENOTESQUARE,
                new Character((char) 221));
        fontMap.put(HALFNOTEHEAD,
                new Character((char) 250));
        // TODO fix this
        fontMap.put(HALFREST,
                new Character((char) 238));
        fontMap.put(SAMEMEASURE,
                new Character((char) 212));
        fontMap.put(ONE,
                new Character((char) 49));
        fontMap.put(UPPERMORDENT,
                new Character((char) 109));
        fontMap.put(SOLIDNOTEHEAD,
                new Character((char) 207));
        fontMap.put(FF,
                new Character((char) 196));
        fontMap.put(TREMOLO,
                new Character((char) 190));
        fontMap.put(NATURAL,
                new Character((char) 110));
        fontMap.put(QUARTERUP,
                new Character((char) 113));
        fontMap.put(WHOLEREST,
                new Character((char) 238));
        fontMap.put(TURN,
                new Character((char) 84));
        fontMap.put(HALFUP,
                new Character((char) 104));
        fontMap.put(LOWERMORDENT,
                new Character((char) 77));
        fontMap.put(ACCENT,
                new Character((char) 62));
        fontMap.put(SHARP,
                new Character((char) 35));
        fontMap.put(THIRTYSECONDREST,
                new Character((char) 168));
        fontMap.put(HALFDOWN,
                new Character((char) 72));
        fontMap.put(CCLEF,
                new Character((char) 66));
        fontMap.put(BASSCLEF,
                new Character((char) 63));
        fontMap.put(SF,
                new Character((char) 83));
        fontMap.put(F,
                new Character((char) 102));
        fontMap.put(THIRTYSECONDUP,
                new Character((char) 114));
        fontMap.put(OTTAVA,
                new Character((char) 195));
        fontMap.put(FOUR,
                new Character((char) 52));
        fontMap.put(QUARTERDOWN,
                new Character((char) 81));
        fontMap.put(FZ,
                new Character((char) 90));
        fontMap.put(FIVE,
                new Character((char) 53));
        fontMap.put(STAFF,
                new Character((char) 61));
        fontMap.put(ONETWENTYEIGHTHUP,
                new Character((char) 141));
        fontMap.put(SEVEN,
                new Character((char) 55));
        fontMap.put(SIXTEENTHDOWN,
                new Character((char) 88));
        fontMap.put(ENDDOUBLEBAR,
                new Character((char) 211));
        fontMap.put(CUTTIME,
                new Character((char) 67));
        fontMap.put(FERMATA,
                new Character((char) 85));
        fontMap.put(DOUBLESHARP,
                new Character((char) 220));
        fontMap.put(DOUBLEWHOLENOTE,
                new Character((char) 87));
        fontMap.put(SEGNO,
                new Character((char) 37));
        fontMap.put(EIGHTHUP,
                new Character((char) 101));
        fontMap.put(NINE,
                new Character((char) 57));
        fontMap.put(BEGINREPEAT,
                new Character((char) 93));
        fontMap.put(SIXTEENTHUP,
                new Character((char) 120));
        fontMap.put(THIRTYSECONDDOWN,
                new Character((char) 82));
        fontMap.put(PP,
                new Character((char) 185));
        fontMap.put(SOLIDNOTEHEAD2,
                new Character((char) 246));
        fontMap.put(PPP,
                new Character((char) 184));
        fontMap.put(BARLINE,
                new Character((char) 92));
        fontMap.put(EIGHTHREST,
                new Character((char) 228));
        fontMap.put(WHOLENOTE,
                new Character((char) 119));
        fontMap.put(COMMONTIME,
                new Character((char) 99));
        fontMap.put(QUARTERREST2,
                new Character((char) 206));
        fontMap.put(TWO,
                new Character((char) 50));
        fontMap.put(DC,
                new Character((char) 68));
        fontMap.put(CODA,
                new Character((char) 222));
        fontMap.put(STACCATO,
                new Character((char) 47));
        fontMap.put(THICKBAR,
                new Character((char) 91));
        fontMap.put(DOT,
                new Character((char) 46));
        fontMap.put(SIXTYFOURTHUP,
                new Character((char) 198));
        fontMap.put(SIXTYFOURTHDOWN,
                new Character((char) 239));
        fontMap.put(LEDGERLINE,
                new Character((char) 94));

    }

    static void initDurationMap() {
        durationMap
                .put(Duration.getDoubleDotted(Duration.DOUBLE_WHOLE_NOTE),
                        String.format("%c%c%c",
                                Symbols.getSymbol(Symbols.DOUBLEWHOLENOTE),
                                Symbols.getSymbol(Symbols.DOT),
                                Symbols.getSymbol(Symbols.DOT)));
        durationMap
                .put(Duration.getDotted(Duration.DOUBLE_WHOLE_NOTE),
                        String.format("%c%c",
                                Symbols.getSymbol(Symbols.DOUBLEWHOLENOTE),
                                Symbols.getSymbol(Symbols.DOT)));
        durationMap
                .put(Duration.DOUBLE_WHOLE_NOTE,
                        String.format("%c",
                                Symbols.getSymbol(Symbols.DOUBLEWHOLENOTE)));

        durationMap.put(Duration.getDoubleDotted(Duration.WHOLE_NOTE),
                String.format("%c%c%c",
                        Symbols.getSymbol(Symbols.WHOLENOTE),
                        Symbols.getSymbol(Symbols.DOT),
                        Symbols.getSymbol(Symbols.DOT)));
        durationMap.put(Duration.getDotted(Duration.WHOLE_NOTE),
                String.format("%c%c",
                        Symbols.getSymbol(Symbols.WHOLENOTE),
                        Symbols.getSymbol(Symbols.DOT)));
        durationMap.put(Duration.WHOLE_NOTE,
                String.format("%c",
                        Symbols.getSymbol(Symbols.WHOLENOTE)));

        durationMap.put(Duration.getDoubleDotted(Duration.HALF_NOTE),
                String.format("%c%c%c",
                        Symbols.getSymbol(Symbols.HALFUP),
                        Symbols.getSymbol(Symbols.DOT),
                        Symbols.getSymbol(Symbols.DOT)));
        durationMap.put(Duration.getDotted(Duration.HALF_NOTE),
                String.format("%c%c",
                        Symbols.getSymbol(Symbols.HALFUP),
                        Symbols.getSymbol(Symbols.DOT)));
        durationMap.put(Duration.HALF_NOTE,
                String.format("%c",
                        Symbols.getSymbol(Symbols.HALFUP)));

        durationMap.put(Duration.getDoubleDotted(Duration.QUARTER_NOTE),
                String.format("%c%c%c",
                        Symbols.getSymbol(Symbols.QUARTERUP),
                        Symbols.getSymbol(Symbols.DOT),
                        Symbols.getSymbol(Symbols.DOT)));
        durationMap.put(Duration.getDotted(Duration.QUARTER_NOTE),
                String.format("%c%c",
                        Symbols.getSymbol(Symbols.QUARTERUP),
                        Symbols.getSymbol(Symbols.DOT)));
        durationMap.put(Duration.QUARTER_NOTE,
                String.format("%c",
                        Symbols.getSymbol(Symbols.QUARTERUP)));

        durationMap.put(Duration.getDoubleDotted(Duration.EIGHTH_NOTE),
                String.format("%c%c%c",
                        Symbols.getSymbol(Symbols.EIGHTHUP),
                        Symbols.getSymbol(Symbols.DOT),
                        Symbols.getSymbol(Symbols.DOT)));
        durationMap.put(Duration.getDotted(Duration.EIGHTH_NOTE),
                String.format("%c%c",
                        Symbols.getSymbol(Symbols.EIGHTHUP),
                        Symbols.getSymbol(Symbols.DOT)));
        durationMap.put(Duration.EIGHTH_NOTE,
                String.format("%c",
                        Symbols.getSymbol(Symbols.EIGHTHUP)));

        // triplets
        durationMap.put(
                Duration.getDoubleDotted(Duration.QUARTER_TRIPLET_NOTE),
                String.format("%c%c%c%c",
                        Symbols.getSymbol(Symbols.THREE),
                        Symbols.getSymbol(Symbols.QUARTERUP),
                        Symbols.getSymbol(Symbols.DOT),
                        Symbols.getSymbol(Symbols.DOT)
                        ));
        // the dotted triplet is the unit i.e. dotted quarter triplet is a
        // quarter
        // durationMap.put(Duration.getDotted(Duration.QUARTER_TRIPLET_NOTE),
        // String.format("%c%c%c",
        // Symbols.getSymbol(Symbols.THREE),
        // Symbols.getSymbol(Symbols.QUARTERUP),
        // Symbols.getSymbol(Symbols.DOT)));
        durationMap.put(Duration.QUARTER_TRIPLET_NOTE,
                String.format("%c%c",
                        Symbols.getSymbol(Symbols.THREE),
                        Symbols.getSymbol(Symbols.QUARTERUP)
                        ));

        durationMap.put(Duration.getDoubleDotted(Duration.EIGHTH_TRIPLET_NOTE),
                String.format("%c%c%c%c",
                        Symbols.getSymbol(Symbols.THREE),
                        Symbols.getSymbol(Symbols.EIGHTHUP),
                        Symbols.getSymbol(Symbols.DOT),
                        Symbols.getSymbol(Symbols.DOT)
                        ));
        // durationMap.put(Duration.getDotted(Duration.EIGHTH_TRIPLET_NOTE),
        // String.format("%c%c%c",
        // Symbols.getSymbol(Symbols.THREE),
        // Symbols.getSymbol(Symbols.EIGHTHUP),
        // Symbols.getSymbol(Symbols.DOT)));
        durationMap.put(Duration.EIGHTH_TRIPLET_NOTE,
                String.format("%c%c",
                        Symbols.getSymbol(Symbols.THREE),
                        Symbols.getSymbol(Symbols.EIGHTHUP)
                        ));

        durationMap.put(
                Duration.getDoubleDotted(Duration.SIXTEENTH_TRIPLET_NOTE),
                String.format("%c%c%c%c",
                        Symbols.getSymbol(Symbols.THREE),
                        Symbols.getSymbol(Symbols.SIXTEENTHUP),
                        Symbols.getSymbol(Symbols.DOT),
                        Symbols.getSymbol(Symbols.DOT)
                        ));
        // durationMap.put(Duration.getDotted(Duration.SIXTEENTH_TRIPLET_NOTE),
        // String.format("%c%c%c",
        // Symbols.getSymbol(Symbols.THREE),
        // Symbols.getSymbol(Symbols.SIXTEENTHUP),
        // Symbols.getSymbol(Symbols.DOT)));
        durationMap.put(Duration.SIXTEENTH_TRIPLET_NOTE,
                String.format("%c%c",
                        Symbols.getSymbol(Symbols.THREE),
                        Symbols.getSymbol(Symbols.SIXTEENTHUP)
                        ));
        // ///////////

        durationMap.put(Duration.getDoubleDotted(Duration.SIXTEENTH_NOTE),
                String.format("%c%c%c",
                        Symbols.getSymbol(Symbols.SIXTEENTHUP),
                        Symbols.getSymbol(Symbols.DOT),
                        Symbols.getSymbol(Symbols.DOT)));
        durationMap.put(Duration.getDotted(Duration.SIXTEENTH_NOTE),
                String.format("%c%c",
                        Symbols.getSymbol(Symbols.SIXTEENTHUP),
                        Symbols.getSymbol(Symbols.DOT)));
        durationMap.put(Duration.SIXTEENTH_NOTE,
                String.format("%c",
                        Symbols.getSymbol(Symbols.SIXTEENTHUP)));

        durationMap
                .put(Duration.getDoubleDotted(Duration.THIRTY_SECOND_NOTE),
                        String.format("%c%c%c",
                                Symbols.getSymbol(Symbols.THIRTYSECONDUP),
                                Symbols.getSymbol(Symbols.DOT),
                                Symbols.getSymbol(Symbols.DOT)));
        durationMap
                .put(Duration.getDotted(Duration.THIRTY_SECOND_NOTE),
                        String.format("%c%c",
                                Symbols.getSymbol(Symbols.THIRTYSECONDUP),
                                Symbols.getSymbol(Symbols.DOT)));
        durationMap
                .put(Duration.THIRTY_SECOND_NOTE,
                        String.format("%c",
                                Symbols.getSymbol(Symbols.THIRTYSECONDUP)));

        durationMap.put(Duration.getDoubleDotted(Duration.SIXTY_FOURTH_NOTE),
                String.format("%c%c%c",
                        Symbols.getSymbol(Symbols.SIXTYFOURTHUP),
                        Symbols.getSymbol(Symbols.DOT),
                        Symbols.getSymbol(Symbols.DOT)));
        durationMap.put(Duration.getDotted(Duration.SIXTY_FOURTH_NOTE),
                String.format("%c%c",
                        Symbols.getSymbol(Symbols.SIXTYFOURTHUP),
                        Symbols.getSymbol(Symbols.DOT)));
        durationMap
                .put(Duration.SIXTY_FOURTH_NOTE,
                        String.format("%c",
                                Symbols.getSymbol(Symbols.SIXTYFOURTHUP)));

        durationMap
                .put(
                        Duration.getDoubleDotted(Duration.ONE_TWENTY_EIGHTH_NOTE),
                        String
                                .format(
                                        "%c%c%c",
                                        Symbols
                                                .getSymbol(Symbols.ONETWENTYEIGHTHUP),
                                        Symbols.getSymbol(Symbols.DOT),
                                        Symbols.getSymbol(Symbols.DOT)));
        durationMap
                .put(
                        Duration.getDotted(Duration.ONE_TWENTY_EIGHTH_NOTE),
                        String
                                .format(
                                        "%c%c",
                                        Symbols
                                                .getSymbol(Symbols.ONETWENTYEIGHTHUP),
                                        Symbols.getSymbol(Symbols.DOT)));
        durationMap
                .put(
                        Duration.ONE_TWENTY_EIGHTH_NOTE,
                        String
                                .format(
                                        "%c",
                                        Symbols
                                                .getSymbol(Symbols.ONETWENTYEIGHTHUP)));
    }

    static void initRestMap() {
        // restMap.put(Duration.getDoubleDotted(Duration.DOUBLE_WHOLE_NOTE),
        // String.format("%c%c%c",
        // Symbols.getSymbol(Symbols.WHOLENOTE),
        // Symbols.getSymbol(Symbols.DOT),
        // Symbols.getSymbol(Symbols.DOT)));
        // restMap.put(Duration.getDotted(Duration.DOUBLE_WHOLE_NOTE),
        // String.format("%c%c",
        // Symbols.getSymbol(Symbols.WHOLENOTE),
        // Symbols.getSymbol(Symbols.DOT)));
        // restMap.put(Duration.DOUBLE_WHOLE_NOTE,
        // String.format("%c",
        // Symbols.getSymbol(Symbols.WHOLENOTE)));

        restMap.put(Duration.getDoubleDotted(Duration.WHOLE_NOTE),
                String.format("%c%c%c",
                        Symbols.getSymbol(Symbols.WHOLEREST),
                        Symbols.getSymbol(Symbols.DOT),
                        Symbols.getSymbol(Symbols.DOT)));
        restMap.put(Duration.getDotted(Duration.WHOLE_NOTE),
                String.format("%c%c",
                        Symbols.getSymbol(Symbols.WHOLEREST),
                        Symbols.getSymbol(Symbols.DOT)));
        restMap.put(Duration.WHOLE_NOTE,
                String.format("%c",
                        Symbols.getSymbol(Symbols.WHOLEREST)));

        restMap.put(Duration.getDoubleDotted(Duration.HALF_NOTE),
                String.format("%c%c%c",
                        Symbols.getSymbol(Symbols.HALFREST),
                        Symbols.getSymbol(Symbols.DOT),
                        Symbols.getSymbol(Symbols.DOT)));
        restMap.put(Duration.getDotted(Duration.HALF_NOTE),
                String.format("%c%c",
                        Symbols.getSymbol(Symbols.HALFREST),
                        Symbols.getSymbol(Symbols.DOT)));
        restMap.put(Duration.HALF_NOTE,
                String.format("%c",
                        Symbols.getSymbol(Symbols.HALFREST)));

        restMap.put(Duration.getDoubleDotted(Duration.QUARTER_NOTE),
                String.format("%c%c%c",
                        Symbols.getSymbol(Symbols.QUARTERREST2),
                        Symbols.getSymbol(Symbols.DOT),
                        Symbols.getSymbol(Symbols.DOT)));
        restMap.put(Duration.getDotted(Duration.QUARTER_NOTE),
                String.format("%c%c",
                        Symbols.getSymbol(Symbols.QUARTERREST2),
                        Symbols.getSymbol(Symbols.DOT)));
        restMap.put(Duration.QUARTER_NOTE,
                String.format("%c",
                        Symbols.getSymbol(Symbols.QUARTERREST2)));

        restMap.put(Duration.getDoubleDotted(Duration.EIGHTH_NOTE),
                String.format("%c%c%c",
                        Symbols.getSymbol(Symbols.EIGHTHREST),
                        Symbols.getSymbol(Symbols.DOT),
                        Symbols.getSymbol(Symbols.DOT)));
        restMap.put(Duration.getDotted(Duration.EIGHTH_NOTE),
                String.format("%c%c",
                        Symbols.getSymbol(Symbols.EIGHTHREST),
                        Symbols.getSymbol(Symbols.DOT)));
        restMap.put(Duration.EIGHTH_NOTE,
                String.format("%c",
                        Symbols.getSymbol(Symbols.EIGHTHREST)));

        // triplet
        // durationMap.put(Duration.getDoubleDotted(Duration.TRIPLET_NOTE),
        // Symbols.EIGHTHUP + Symbols.DOT + Symbols.DOT);
        // durationMap.put(Duration.getDotted(Duration.TRIPLET_NOTE),
        // Symbols.EIGHTHUP + Symbols.DOT);
        // durationMap.put(Duration.TRIPLET_NOTE,
        // Symbols.EIGHTHUP);

        restMap.put(Duration.getDoubleDotted(Duration.SIXTEENTH_NOTE),
                String.format("%c%c%c",
                        Symbols.getSymbol(Symbols.SIXTEENTHREST),
                        Symbols.getSymbol(Symbols.DOT),
                        Symbols.getSymbol(Symbols.DOT)));
        restMap.put(Duration.getDotted(Duration.SIXTEENTH_NOTE),
                String.format("%c%c",
                        Symbols.getSymbol(Symbols.SIXTEENTHREST),
                        Symbols.getSymbol(Symbols.DOT)));
        restMap.put(Duration.SIXTEENTH_NOTE,
                String.format("%c",
                        Symbols.getSymbol(Symbols.SIXTEENTHREST)));

        restMap.put(Duration.getDoubleDotted(Duration.THIRTY_SECOND_NOTE),
                String.format("%c%c%c",
                        Symbols.getSymbol(Symbols.THIRTYSECONDREST),
                        Symbols.getSymbol(Symbols.DOT),
                        Symbols.getSymbol(Symbols.DOT)));
        restMap.put(Duration.getDotted(Duration.THIRTY_SECOND_NOTE),
                String.format("%c%c",
                        Symbols.getSymbol(Symbols.THIRTYSECONDREST),
                        Symbols.getSymbol(Symbols.DOT)));
        restMap.put(Duration.THIRTY_SECOND_NOTE,
                String.format("%c",
                        Symbols.getSymbol(Symbols.THIRTYSECONDREST)));

        restMap.put(Duration.getDoubleDotted(Duration.SIXTY_FOURTH_NOTE),
                String.format("%c%c%c",
                        Symbols.getSymbol(Symbols.SIXTYFOURTHREST),
                        Symbols.getSymbol(Symbols.DOT),
                        Symbols.getSymbol(Symbols.DOT)));
        restMap.put(Duration.getDotted(Duration.SIXTY_FOURTH_NOTE),
                String.format("%c%c",
                        Symbols.getSymbol(Symbols.SIXTYFOURTHREST),
                        Symbols.getSymbol(Symbols.DOT)));
        restMap.put(Duration.SIXTY_FOURTH_NOTE,
                String.format("%c",
                        Symbols.getSymbol(Symbols.SIXTYFOURTHREST)));

        restMap
                .put(
                        Duration.getDoubleDotted(Duration.ONE_TWENTY_EIGHTH_NOTE),
                        String
                                .format(
                                        "%c%c%c",
                                        Symbols
                                                .getSymbol(Symbols.ONETWENTYEIGHTHREST),
                                        Symbols.getSymbol(Symbols.DOT),
                                        Symbols.getSymbol(Symbols.DOT)));
        restMap
                .put(
                        Duration.getDotted(Duration.ONE_TWENTY_EIGHTH_NOTE),
                        String
                                .format(
                                        "%c%c",
                                        Symbols
                                                .getSymbol(Symbols.ONETWENTYEIGHTHREST),
                                        Symbols.getSymbol(Symbols.DOT)));
        restMap
                .put(
                        Duration.ONE_TWENTY_EIGHTH_NOTE,
                        String
                                .format(
                                        "%c",
                                        Symbols
                                                .getSymbol(Symbols.ONETWENTYEIGHTHREST)));
    }

    public static char getSymbol(String name) {
        return fontMap.get(name).charValue();
    }

    public static String getFlaggedSymbols(double d) {
        String s = durationMap.get(d);
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("returning '%s' for %f",
                    s,
                    d));

        }
        return s;
    }

    public static String getRestSymbols(double d) {
        String s = restMap.get(d);
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("returning '%s' for %f",
                    s,
                    d));

        }
        return s;
    }

    public static int getUnicodeSymbol(String name) {
        return unicodeNameMap.get(name);
    }

    // Character.UnicodeBlock.MUSICAL_SYMBOLS;

    static final Map<Integer, String> unicodeMap = new TreeMap<Integer, String>();
    static final Map<String, Integer> unicodeNameMap = new TreeMap<String, Integer>();
    static {
        unicodeMap.put(0x1D100,
                "SINGLE_BARLINE");
        unicodeMap.put(0x1D101,
                "DOUBLE_BARLINE");
        unicodeMap.put(0x1D102,
                "FINAL_BARLINE");
        unicodeMap.put(0x1D103,
                "REVERSE_FINAL_BARLINE");
        unicodeMap.put(0x1D104,
                "DASHED_BARLINE");
        unicodeMap.put(0x1D105,
                "SHORT_BARLINE");
        unicodeMap.put(0x1D106,
                "LEFT_REPEAT_SIGN");
        unicodeMap.put(0x1D107,
                "RIGHT_REPEAT_SIGN");
        unicodeMap.put(0x1D108,
                "REPEAT_DOTS");
        unicodeMap.put(0x1D109,
                "DAL_SEGNO");
        unicodeMap.put(0x1D10A,
                "DA_CAPO");
        unicodeMap.put(0x1D10B,
                "SEGNO");
        unicodeMap.put(0x1D10C,
                "CODA");
        unicodeMap.put(0x1D10D,
                "REPEATED_FIGURE_1");
        unicodeMap.put(0x1D10E,
                "REPEATED_FIGURE_2");
        unicodeMap.put(0x1D10F,
                "REPEATED_FIGURE_3");
        unicodeMap.put(0x1D110,
                "FERMATA");
        unicodeMap.put(0x1D111,
                "FERMATA_BELOW");
        unicodeMap.put(0x1D112,
                "BREATH_MARK");
        unicodeMap.put(0x1D113,
                "CAESURA");
        unicodeMap.put(0x1D114,
                "BRACE");
        unicodeMap.put(0x1D115,
                "BRACKET");
        unicodeMap.put(0x1D116,
                "ONE_LINE_STAFF");
        unicodeMap.put(0x1D117,
                "TWO_LINE_STAFF");
        unicodeMap.put(0x1D118,
                "THREE_LINE_STAFF");
        unicodeMap.put(0x1D119,
                "FOUR_LINE_STAFF");
        unicodeMap.put(0x1D11A,
                "FIVE_LINE_STAFF");
        unicodeMap.put(0x1D11B,
                "SIX_LINE_STAFF");
        unicodeMap.put(0x1D11C,
                "SIX_STRING_FRETBOARD");
        unicodeMap.put(0x1D11D,
                "FOUR_STRING_FRETBOARD");
        unicodeMap.put(0x1D11E,
                "G_CLEF");
        unicodeMap.put(0x1D11F,
                "G_CLEF_OTTAVA_ALTA");
        unicodeMap.put(0x1D120,
                "G_CLEF_OTTAVA_BASSA");
        unicodeMap.put(0x1D121,
                "C_CLEF");
        unicodeMap.put(0x1D122,
                "F_CLEF");
        unicodeMap.put(0x1D123,
                "F_CLEF_OTTAVA_ALTA");
        unicodeMap.put(0x1D124,
                "F_CLEF_OTTAVA_BASSA");
        unicodeMap.put(0x1D125,
                "DRUM_CLEF_1");
        unicodeMap.put(0x1D126,
                "DRUM_CLEF_2");
        unicodeMap.put(0x1D129,
                "MULTIPLE_MEASURE_REST");
        unicodeMap.put(0x1D12A,
                "DOUBLE_SHARP");
        unicodeMap.put(0x1D12B,
                "DOUBLE_FLAT");
        unicodeMap.put(0x1D12C,
                "FLAT_UP");
        unicodeMap.put(0x1D12D,
                "FLAT_DOWN");
        unicodeMap.put(0x1D12E,
                "NATURAL_UP");
        unicodeMap.put(0x1D12F,
                "NATURAL_DOWN");
        unicodeMap.put(0x1D130,
                "SHARP_UP");
        unicodeMap.put(0x1D131,
                "SHARP_DOWN");
        unicodeMap.put(0x1D132,
                "QUARTER_TONE_SHARP");
        unicodeMap.put(0x1D133,
                "QUARTER_TONE_FLAT");
        unicodeMap.put(0x1D134,
                "COMMON_TIME");
        unicodeMap.put(0x1D135,
                "CUT_TIME");
        unicodeMap.put(0x1D136,
                "OTTAVA_ALTA");
        unicodeMap.put(0x1D137,
                "OTTAVA_BASSA");
        unicodeMap.put(0x1D138,
                "QUINDICESIMA_ALTA");
        unicodeMap.put(0x1D139,
                "QUINDICESIMA_BASSA");
        unicodeMap.put(0x1D13A,
                "MULTI_REST");
        unicodeMap.put(0x1D13B,
                "WHOLE_REST");
        unicodeMap.put(0x1D13C,
                "HALF_REST");
        unicodeMap.put(0x1D13D,
                "QUARTER_REST");
        unicodeMap.put(0x1D13E,
                "EIGHTH_REST");
        unicodeMap.put(0x1D13F,
                "SIXTEENTH_REST");
        unicodeMap.put(0x1D140,
                "THIRTY_SECOND_REST");
        unicodeMap.put(0x1D141,
                "SIXTY_FOURTH_REST");
        unicodeMap.put(0x1D142,
                "ONE_HUNDRED_TWENTY_EIGHTH_REST");
        unicodeMap.put(0x1D143,
                "X_NOTEHEAD");
        unicodeMap.put(0x1D144,
                "PLUS_NOTEHEAD");
        unicodeMap.put(0x1D145,
                "CIRCLE_X_NOTEHEAD");
        unicodeMap.put(0x1D146,
                "SQUARE_NOTEHEAD_WHITE");
        unicodeMap.put(0x1D147,
                "SQUARE_NOTEHEAD_BLACK");
        unicodeMap.put(0x1D148,
                "TRIANGLE_NOTEHEAD_UP_WHITE");
        unicodeMap.put(0x1D149,
                "TRIANGLE_NOTEHEAD_UP_BLACK");
        unicodeMap.put(0x1D14A,
                "TRIANGLE_NOTEHEAD_LEFT_WHITE");
        unicodeMap.put(0x1D14B,
                "TRIANGLE_NOTEHEAD_LEFT_BLACK");
        unicodeMap.put(0x1D14C,
                "TRIANGLE_NOTEHEAD_RIGHT_WHITE");
        unicodeMap.put(0x1D14D,
                "TRIANGLE_NOTEHEAD_RIGHT_BLACK");
        unicodeMap.put(0x1D14E,
                "TRIANGLE_NOTEHEAD_DOWN_WHITE");
        unicodeMap.put(0x1D14F,
                "TRIANGLE_NOTEHEAD_DOWN_BLACK");
        unicodeMap.put(0x1D150,
                "TRIANGLE_NOTEHEAD_UP_RIGHT_WHITE");
        unicodeMap.put(0x1D151,
                "TRIANGLE_NOTEHEAD_UP_RIGHT_BLACK");
        unicodeMap.put(0x1D152,
                "MOON_NOTEHEAD_WHITE");
        unicodeMap.put(0x1D153,
                "MOON_NOTEHEAD_BLACK");
        unicodeMap.put(0x1D154,
                "TRIANGLE_ROUND_NOTEHEAD_DOWN_WHITE");
        unicodeMap.put(0x1D155,
                "TRIANGLE_ROUND_NOTEHEAD_DOWN_BLACK");
        unicodeMap.put(0x1D156,
                "PARENTHESIS_NOTEHEAD");
        unicodeMap.put(0x1D157,
                "VOID_NOTEHEAD");
        unicodeMap.put(0x1D158,
                "NOTEHEAD_BLACK");
        unicodeMap.put(0x1D159,
                "NULL_NOTEHEAD");
        unicodeMap.put(0x1D15A,
                "CLUSTER_NOTEHEAD_WHITE");
        unicodeMap.put(0x1D15B,
                "CLUSTER_NOTEHEAD_BLACK");
        unicodeMap.put(0x1D15C,
                "BREVE");
        unicodeMap.put(0x1D15D,
                "WHOLE_NOTE");
        unicodeMap.put(0x1D15E,
                "HALF_NOTE");
        unicodeMap.put(0x1D15F,
                "QUARTER_NOTE");
        unicodeMap.put(0x1D160,
                "EIGHTH_NOTE");
        unicodeMap.put(0x1D161,
                "SIXTEENTH_NOTE");
        unicodeMap.put(0x1D162,
                "THIRTY_SECOND_NOTE");
        unicodeMap.put(0x1D163,
                "SIXTY_FOURTH_NOTE");
        unicodeMap.put(0x1D164,
                "ONE_HUNDRED_TWENTY_EIGHTH_NOTE");
        unicodeMap.put(0x1D165,
                "COMBINING_STEM");
        unicodeMap.put(0x1D166,
                "COMBINING_SPRECHGESANG_STEM");
        unicodeMap.put(0x1D167,
                "COMBINING_TREMOLO_1");
        unicodeMap.put(0x1D168,
                "COMBINING_TREMOLO_2");
        unicodeMap.put(0x1D169,
                "COMBINING_TREMOLO_3");
        unicodeMap.put(0x1D16A,
                "FINGERED_TREMOLO_1");
        unicodeMap.put(0x1D16B,
                "FINGERED_TREMOLO_2");
        unicodeMap.put(0x1D16C,
                "FINGERED_TREMOLO_3");
        unicodeMap.put(0x1D16D,
                "COMBINING_AUGMENTATION_DOT");
        unicodeMap.put(0x1D16E,
                "COMBINING_FLAG_1");
        unicodeMap.put(0x1D16F,
                "COMBINING_FLAG_2");
        unicodeMap.put(0x1D170,
                "COMBINING_FLAG_3");
        unicodeMap.put(0x1D171,
                "COMBINING_FLAG_4");
        unicodeMap.put(0x1D172,
                "COMBINING_FLAG_5");
        unicodeMap.put(0x1D173,
                "BEGIN_BEAM");
        unicodeMap.put(0x1D174,
                "END_BEAM");
        unicodeMap.put(0x1D175,
                "BEGIN_TIE");
        unicodeMap.put(0x1D176,
                "END_TIE");
        unicodeMap.put(0x1D177,
                "BEGIN_SLUR");
        unicodeMap.put(0x1D178,
                "END_SLUR");
        unicodeMap.put(0x1D179,
                "BEGIN_PHRASE");
        unicodeMap.put(0x1D17A,
                "END_PHRASE");
        unicodeMap.put(0x1D17B,
                "COMBINING_ACCENT");
        unicodeMap.put(0x1D17C,
                "COMBINING_STACCATO");
        unicodeMap.put(0x1D17D,
                "COMBINING_TENUTO");
        unicodeMap.put(0x1D17E,
                "COMBINING_STACCATISSIMO");
        unicodeMap.put(0x1D17F,
                "COMBINING_MARCATO");
        unicodeMap.put(0x1D180,
                "COMBINING_MARCATO_STACCATO");
        unicodeMap.put(0x1D181,
                "COMBINING_ACCENT_STACCATO");
        unicodeMap.put(0x1D182,
                "COMBINING_LOURE");
        unicodeMap.put(0x1D183,
                "ARPEGGIATO_UP");
        unicodeMap.put(0x1D184,
                "ARPEGGIATO_DOWN");
        unicodeMap.put(0x1D185,
                "COMBINING_DOIT");
        unicodeMap.put(0x1D186,
                "COMBINING_RIP");
        unicodeMap.put(0x1D187,
                "COMBINING_FLIP");
        unicodeMap.put(0x1D188,
                "COMBINING_SMEAR");
        unicodeMap.put(0x1D189,
                "COMBINING_BEND");
        unicodeMap.put(0x1D18A,
                "COMBINING_DOUBLE_TONGUE");
        unicodeMap.put(0x1D18B,
                "COMBINING_TRIPLE_TONGUE");
        unicodeMap.put(0x1D18C,
                "RINFORZANDO");
        unicodeMap.put(0x1D18D,
                "SUBITO");
        unicodeMap.put(0x1D18E,
                "Z");
        unicodeMap.put(0x1D18F,
                "PIANO");
        unicodeMap.put(0x1D190,
                "MEZZO");
        unicodeMap.put(0x1D191,
                "FORTE");
        unicodeMap.put(0x1D192,
                "CRESCENDO");
        unicodeMap.put(0x1D193,
                "DECRESCENDO");
        unicodeMap.put(0x1D194,
                "GRACE_NOTE_SLASH");
        unicodeMap.put(0x1D195,
                "GRACE_NOTE_NO_SLASH");
        unicodeMap.put(0x1D196,
                "TR");
        unicodeMap.put(0x1D197,
                "TURN");
        unicodeMap.put(0x1D198,
                "INVERTED_TURN");
        unicodeMap.put(0x1D199,
                "TURN_SLASH");
        unicodeMap.put(0x1D19A,
                "TURN_UP");
        unicodeMap.put(0x1D19B,
                "ORNAMENT_STROKE_1");
        unicodeMap.put(0x1D19C,
                "ORNAMENT_STROKE_2");
        unicodeMap.put(0x1D19D,
                "ORNAMENT_STROKE_3");
        unicodeMap.put(0x1D19E,
                "ORNAMENT_STROKE_4");
        unicodeMap.put(0x1D19F,
                "ORNAMENT_STROKE_5");
        unicodeMap.put(0x1D1A0,
                "ORNAMENT_STROKE_6");
        unicodeMap.put(0x1D1A1,
                "ORNAMENT_STROKE_7");
        unicodeMap.put(0x1D1A2,
                "ORNAMENT_STROKE_8");
        unicodeMap.put(0x1D1A3,
                "ORNAMENT_STROKE_9");
        unicodeMap.put(0x1D1A4,
                "ORNAMENT_STROKE_10");
        unicodeMap.put(0x1D1A5,
                "ORNAMENT_STROKE_11");
        unicodeMap.put(0x1D1A6,
                "HAUPTSTIMME");
        unicodeMap.put(0x1D1A7,
                "NEBENSTIMME");
        unicodeMap.put(0x1D1A8,
                "END_OF_STIMME");
        unicodeMap.put(0x1D1A9,
                "DEGREE_SLASH");
        unicodeMap.put(0x1D1AA,
                "COMBINING_DOWN_BOW");
        unicodeMap.put(0x1D1AB,
                "COMBINING_UP_BOW");
        unicodeMap.put(0x1D1AC,
                "COMBINING_HARMONIC");
        unicodeMap.put(0x1D1AD,
                "COMBINING_SNAP_PIZZICATO");
        unicodeMap.put(0x1D1AE,
                "PEDAL_MARK");
        unicodeMap.put(0x1D1AF,
                "PEDAL_UP_MARK");
        unicodeMap.put(0x1D1B0,
                "HALF_PEDAL_MARK");
        unicodeMap.put(0x1D1B1,
                "GLISSANDO_UP");
        unicodeMap.put(0x1D1B2,
                "GLISSANDO_DOWN");
        unicodeMap.put(0x1D1B3,
                "WITH_FINGERNAILS");
        unicodeMap.put(0x1D1B4,
                "DAMP");
        unicodeMap.put(0x1D1B5,
                "DAMP_ALL");
        unicodeMap.put(0x1D1B6,
                "MAXIMA");
        unicodeMap.put(0x1D1B7,
                "LONGA");
        unicodeMap.put(0x1D1B8,
                "BREVIS");
        unicodeMap.put(0x1D1B9,
                "SEMIBREVIS_WHITE");
        unicodeMap.put(0x1D1BA,
                "SEMIBREVIS_BLACK");
        unicodeMap.put(0x1D1BB,
                "MINIMA");
        unicodeMap.put(0x1D1BC,
                "MINIMA_BLACK");
        unicodeMap.put(0x1D1BD,
                "SEMIMINIMA_WHITE");
        unicodeMap.put(0x1D1BE,
                "SEMIMINIMA_BLACK");
        unicodeMap.put(0x1D1BF,
                "FUSA_WHITE");
        unicodeMap.put(0x1D1C0,
                "FUSA_BLACK");
        unicodeMap.put(0x1D1C1,
                "LONGA_PERFECTA_REST");
        unicodeMap.put(0x1D1C2,
                "LONGA_IMPERFECTA_REST");
        unicodeMap.put(0x1D1C3,
                "BREVIS_REST");
        unicodeMap.put(0x1D1C4,
                "SEMIBREVIS_REST");
        unicodeMap.put(0x1D1C5,
                "MINIMA_REST");
        unicodeMap.put(0x1D1C6,
                "SEMIMINIMA_REST");
        unicodeMap.put(0x1D1C7,
                "TEMPUS_PERFECTUM_CUM_PROLATIONE_PERFECTA");
        unicodeMap.put(0x1D1C8,
                "TEMPUS_PERFECTUM_CUM_PROLATIONE_IMPERFECTA");
        unicodeMap.put(0x1D1C9,
                "TEMPUS_PERFECTUM_CUM_PROLATIONE_PERFECTA_DIMINUTION_1");
        unicodeMap.put(0x1D1CA,
                "TEMPUS_IMPERFECTUM_CUM_PROLATIONE_PERFECTA");
        unicodeMap.put(0x1D1CB,
                "TEMPUS_IMPERFECTUM_CUM_PROLATIONE_IMPERFECTA");
        unicodeMap
                .put(
                        0x1D1CC,
                        "TEMPUS_IMPERFECTUM_CUM_PROLATIONE_IMPERFECTA_DIMINUTION_1");
        unicodeMap
                .put(
                        0x1D1CD,
                        "TEMPUS_IMPERFECTUM_CUM_PROLATIONE_IMPERFECTA_DIMINUTION_2");
        unicodeMap
                .put(
                        0x1D1CE,
                        "TEMPUS_IMPERFECTUM_CUM_PROLATIONE_IMPERFECTA_DIMINUTION_3");
        unicodeMap.put(0x1D1CF,
                "CROIX");
        unicodeMap.put(0x1D1D0,
                "GREGORIAN_C_CLEF");
        unicodeMap.put(0x1D1D1,
                "GREGORIAN_F_CLEF");
        unicodeMap.put(0x1D1D2,
                "SQUARE_B");
        unicodeMap.put(0x1D1D3,
                "VIRGA");
        unicodeMap.put(0x1D1D4,
                "PODATUS");
        unicodeMap.put(0x1D1D5,
                "CLIVIS");
        unicodeMap.put(0x1D1D6,
                "SCANDICUS");
        unicodeMap.put(0x1D1D7,
                "CLIMACUS");
        unicodeMap.put(0x1D1D8,
                "TORCULUS");
        unicodeMap.put(0x1D1D9,
                "PORRECTUS");
        unicodeMap.put(0x1D1DA,
                "PORRECTUS_FLEXUS");
        unicodeMap.put(0x1D1DB,
                "SCANDICUS_FLEXUS");
        unicodeMap.put(0x1D1DC,
                "TORCULUS_RESUPINUS");
        unicodeMap.put(0x1D1DD,
                "PES_SUBPUNCTIS");

        // set up the "reverse" map for ease of use
        for (Map.Entry<Integer, String> me : unicodeMap.entrySet()) {
            unicodeNameMap.put(me.getValue(),
                    me.getKey());
        }
    }
}
