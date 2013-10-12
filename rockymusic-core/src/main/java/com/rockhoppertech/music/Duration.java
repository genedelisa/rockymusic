/**
 * 
 */
package com.rockhoppertech.music;

import java.util.ArrayList;
import java.util.List;

/**
 * Handy constants for durations along with
 * methods for calculating durations.
 * 
 *
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 *
 */
public class Duration {
    public static final List<Double> durations;
    

    // of course this is only where the ts denom is 4
    public static final double DOUBLE_WHOLE_NOTE = 8d;
    public static final double DOUBLE_WHOLE_TRIPLET_NOTE = 16D / 3D;
    public static final double WHOLE_NOTE = 4D;
    public static final double WHOLE_TRIPLET_NOTE = DOUBLE_WHOLE_NOTE / 3D;
    public static final double HALF_NOTE = 2D;
    public static final double HALF_TRIPLET_NOTE = WHOLE_NOTE / 3D;
    public static final double QUARTER_NOTE = 1D;
    public static final double QUARTER_TRIPLET_NOTE = HALF_NOTE / 3D;
    public static final double EIGHTH_NOTE = QUARTER_NOTE / 2D;
    public static final double EIGHTH_TRIPLET_NOTE = 1D / 3D;
    public static final double SIXTEENTH_NOTE = 1D / 4D;
    public static final double SIXTEENTH_TRIPLET_NOTE = EIGHTH_NOTE / 3D;
    public static final double QUINT_NOTE = 1D / 5D;
    public static final double SEXT_NOTE = 1D / 6D;
    public static final double SEPT_NOTE = 1D / 7D;
    public static final double THIRTY_SECOND_NOTE = 1D / 8D;
    public static final double THIRTY_SECOND_TRIPLET_NOTE = SIXTEENTH_NOTE / 3d;
    public static final double SIXTY_FOURTH_NOTE = 1D / 16D;
    public static final double SIXTY_FOURTH_TRIPLET_NOTE = THIRTY_SECOND_NOTE / 3d;
    public static final double ONE_TWENTY_EIGHTH_NOTE = 1D / 32D;
    public static final double ONE_TWENTY_EIGHTH_TRIPLET_NOTE = SIXTY_FOURTH_NOTE / 3d;
    
    //dwhqestxo
    // just some abbreviations
    public static final double D = DOUBLE_WHOLE_NOTE;
    public static final double W = WHOLE_NOTE;
    public static final double H = HALF_NOTE;
    public static final double Q = QUARTER_NOTE;
    public static final double E = EIGHTH_NOTE;
    public static final double S = SIXTEENTH_NOTE;
    public static final double T = THIRTY_SECOND_NOTE;
    public static final double X = SIXTY_FOURTH_NOTE;
    public static final double O = ONE_TWENTY_EIGHTH_NOTE;
    
    
    static boolean weird = false;
    // [14.0, 12.0, 8.0, 7.0, 6.0, 4.0, 3.5, 3.0, 2.0, 1.75, 1.5, 1.0,
    // 1.1666666666666667,
    // 0.6666666666666666, 0.875, 0.75, 0.5, 0.5833333333333334,
    // 0.3333333333333333,
    // 0.4375, 0.375, 0.25, 0.2916666666666667, 0.16666666666666666, 0.21875,
    // 0.1875, 0.125,
    // 0.109375, 0.09375, 0.0625, 0.0546875, 0.046875, 0.03125]
    static {
        durations = new ArrayList<Double>();
        durations.add(Duration.getDoubleDotted(DOUBLE_WHOLE_NOTE));
        durations.add(Duration.getDotted(DOUBLE_WHOLE_NOTE));
        durations.add(DOUBLE_WHOLE_NOTE);
        durations.add(Duration.getDoubleDotted(WHOLE_NOTE));
        durations.add(Duration.getDotted(WHOLE_NOTE));
        durations.add(WHOLE_NOTE);
        durations.add(Duration.getDoubleDotted(HALF_NOTE));
        durations.add(Duration.getDotted(HALF_NOTE));
        durations.add(HALF_NOTE);
        durations.add(Duration.getDoubleDotted(QUARTER_NOTE));
        durations.add(Duration.getDotted(QUARTER_NOTE));
        durations.add(QUARTER_NOTE);
        // 1.1666666666666667
        // 1.0 a dotted triplet is the unit : .66 + .66/2 = 1
        // 0.6666666666666666
        durations.add(Duration.getDoubleDotted(QUARTER_TRIPLET_NOTE));
        // durations.add(Duration.getDotted(QUARTER_TRIPLET_NOTE));
        durations.add(QUARTER_TRIPLET_NOTE);
        durations.add(Duration.getDoubleDotted(EIGHTH_NOTE));
        durations.add(Duration.getDotted(EIGHTH_NOTE));
        durations.add(EIGHTH_NOTE);
        // 0.5833333333333334
        // 0.5
        // 0.3333333333333333
        durations.add(Duration.getDoubleDotted(EIGHTH_TRIPLET_NOTE));
        // durations.add(Duration.getDotted(EIGHTH_TRIPLET_NOTE));
        durations.add(EIGHTH_TRIPLET_NOTE);
        durations.add(Duration.getDoubleDotted(SIXTEENTH_NOTE));
        durations.add(Duration.getDotted(SIXTEENTH_NOTE));
        durations.add(SIXTEENTH_NOTE);
        // 0.2916666666666667
        // 0.25
        // 0.16666666666666666
        durations.add(Duration.getDoubleDotted(SIXTEENTH_TRIPLET_NOTE));
        // durations.add(Duration.getDotted(SIXTEENTH_TRIPLET_NOTE));
        durations.add(SIXTEENTH_TRIPLET_NOTE);
        if (weird) {
            durations.add(Duration.getDoubleDotted(QUINT_NOTE));
            durations.add(Duration.getDotted(QUINT_NOTE));
            durations.add(QUINT_NOTE);
            durations.add(Duration.getDoubleDotted(SEXT_NOTE));
            durations.add(Duration.getDotted(SEXT_NOTE));
            durations.add(SEXT_NOTE);
            durations.add(Duration.getDoubleDotted(SEPT_NOTE));
            durations.add(Duration.getDotted(SEPT_NOTE));
            durations.add(SEPT_NOTE);
        }
        durations.add(Duration.getDoubleDotted(THIRTY_SECOND_NOTE));
        durations.add(Duration.getDotted(THIRTY_SECOND_NOTE));
        durations.add(THIRTY_SECOND_NOTE);
        durations.add(Duration.getDoubleDotted(SIXTY_FOURTH_NOTE));
        durations.add(Duration.getDotted(SIXTY_FOURTH_NOTE));
        durations.add(SIXTY_FOURTH_NOTE);
        durations.add(Duration.getDoubleDotted(ONE_TWENTY_EIGHTH_NOTE));
        durations.add(Duration.getDotted(ONE_TWENTY_EIGHTH_NOTE));
        durations.add(ONE_TWENTY_EIGHTH_NOTE);

    }
    public static final double MIN = ONE_TWENTY_EIGHTH_NOTE;
    public static final double MAX = Duration
            .getDoubleDotted(DOUBLE_WHOLE_NOTE);

    public static double getDotted(final double val) {
        return val + val / 2d;
    }

    public static double getDoubleDotted(final double val) {
        return val + val / 2d + val / 4d;
    }

    /**
     * @param val the duration
     * @param n the number of dots
     * @return
     */
    public static double getDotted(final double val, final int n) {
        if (n < 1) {
            throw new IllegalArgumentException(String
                    .format("value must be >= 1 (%d)",
                            n));
        }
        double r = val;
        // for (int i = 1; i <= n; i++) {
        // double d = Math.pow(2d,
        // i);
        // r += (val / d);
        // }

        // or simpler
        r = 2d * val - val / Math.pow(2d,
                                      n);
        return r;
    }

    /**
     * In a music notation program you need to know what the sequence
     * of rests are given a rest duration.
     * @param d
     * @return a comma delimited String with rest values
     */
    public static String getRestString1(double d) {
        final StringBuilder sb = new StringBuilder();
        while (d >= 4d) {
            sb.append("4,");
            d -= 4d;
        }
        while (d >= 2d) {
            sb.append("2,");
            d -= 2d;
        }
        while (d >= 1d) {
            sb.append("1,");
            d -= 1d;
        }
        while (d >= .5) {
            sb.append(".5,");
            d -= .5;
        }
        // punt on smaller values
        while (d > 0d) {
            sb.append(".25,");
            d -= .25;
        }
        return sb.toString();
    }

    // TODO fix this to recognize start time and the fraction to fill out the
    // beat
    public static String getRestString(double numBeats) {
        final StringBuilder sb = new StringBuilder();
        if (durations.contains(numBeats)) {
            sb.append(numBeats);
        } else {
            for (Double d : durations) {
                if (numBeats - d >= 0d) {
                    numBeats -= d;
                    sb.append(d);
                    if (numBeats != 0d) {
                        sb.append(',');
                    }
                    if (numBeats == 0d) {
                        break;
                    }
                }
            }
        }
        return sb.toString();
    }

    public static String getDurationString(double numBeats) {
        final StringBuilder sb = new StringBuilder();
        if (durations.contains(numBeats)) {
            sb.append(numBeats);
        } else {
            for (Double d : durations) {
                if (numBeats - d >= 0d) {
                    numBeats -= d;
                    sb.append(d);
                    if (numBeats != 0d) {
                        sb.append(',');
                    }
                    if (numBeats == 0d) {
                        break;
                    }
                }
            }
        }

        // remove the trailing comma
        // int i = sb.lastIndexOf(",");
        // if (i != -1)
        // sb.deleteCharAt(i);
        return sb.toString();
    }

    public static String getDurationString(double numBeats, boolean markers) {
        final StringBuilder sb = new StringBuilder();
        if (durations.contains(numBeats)) {
            sb.append(numBeats);
        } else {
            // don't really need the markers
            if (markers)
                sb.append("ts=");
            for (Double d : durations) {
                if (numBeats - d >= 0d) {
                    numBeats -= d;
                    sb.append(d);
                    if (numBeats != 0d) {
                        sb.append(',');
                    }
                    if (numBeats == 0d) {
                        break;
                    }
                }
            }
            // sb.append("te");
        }
        return sb.toString();
    }

    public static void main(final String[] args) {
        // for (double d = Duration.WHOLE_NOTE; d > Duration.THIRTY_SECOND_NOTE;
        // d -= Duration.SIXTEENTH_NOTE) {
        // String s = Duration.getDurationString(d);
        // System.out.println(String.format("%f: %s",
        // d,
        // s));
        // printScannedDuration(s);
        // System.out.println();
        // }

        for (double d = Duration.MIN; d < Duration.MAX; d += Duration.MIN) {
            String s = Duration.getDurationString(d);
            System.out.println(String.format("%f: %s",
                                             d,
                                             s));
            System.out.println();
        }

        double x = 1;

        if (x == Duration.getDoubleDotted(DOUBLE_WHOLE_NOTE)) {

        }
        if (x == Duration.getDotted(DOUBLE_WHOLE_NOTE)) {

        }
        if (x == Duration.DOUBLE_WHOLE_NOTE) {

        }

        // but with enums you can do this
        Dur dur = Dur.DOUBLE_WHOLE_NOTE_DD;
        switch (dur) {
        case DOUBLE_WHOLE_NOTE_DD:
            break;
        case DOUBLE_WHOLE_NOTE_D:
            break;
        case DOUBLE_WHOLE_NOTE:
            break;
		case EIGHTH_NOTE:
			break;
		case EIGHTH_NOTE_D:
			break;
		case EIGHTH_NOTE_DD:
			break;
		case HALF_NOTE:
			break;
		case HALF_NOTE_D:
			break;
		case HALF_NOTE_DD:
			break;
		case ONE_TWENTY_EIGHTH_NOTE:
			break;
		case ONE_TWENTY_EIGHTH_NOTE_D:
			break;
		case ONE_TWENTY_EIGHTH_NOTE_DD:
			break;
		case QUARTER_NOTE:
			break;
		case QUARTER_NOTE_D:
			break;
		case QUARTER_NOTE_DD:
			break;
		case SIXTEENTH_NOTE:
			break;
		case SIXTEENTH_NOTE_D:
			break;
		case SIXTEENTH_NOTE_DD:
			break;
		case SIXTY_FOURTH_NOTE:
			break;
		case SIXTY_FOURTH_NOTE_D:
			break;
		case SIXTY_FOURTH_NOTE_DD:
			break;
		case THIRTY_SECOND_NOTE:
			break;
		case THIRTY_SECOND_NOTE_D:
			break;
		case THIRTY_SECOND_NOTE_DD:
			break;
		case WHOLE_NOTE:
			break;
		case WHOLE_NOTE_D:
			break;
		case WHOLE_NOTE_DD:
			break;
		default:
			break;
        }

        //
        // String s = Duration.getRestString(8);
        // System.out.println(s);
        // Duration.printScanned(s);
        //
        // s = Duration.getRestString(7.5);
        // System.out.println(s);
        // Duration.printScanned(s);
        //
        // s = Duration.getRestString(5);
        // System.out.println(s);
        // Duration.printScanned(s);
        //
        // s = Duration.getRestString(3);
        // System.out.println(s);
        // Duration.printScanned(s);
        //
        // s = Duration.getRestString(.5);
        // System.out.println(s);
        // Duration.printScanned(s);
        //
        // s = Duration.getRestString(.25);
        // System.out.println(s);
        // Duration.printScanned(s);
        System.err.println(durations);
    }

//    private static void printScannedDuration(final String s) {
//        final Scanner scanner = new Scanner(s);
//        scanner.useDelimiter(",");
//        System.out.println("scanned");
//        while (scanner.hasNext()) {
//            final String token = scanner.next().trim();
//            System.out.println("token:" + token);
//            if (token.startsWith("ts")) {
//                System.out.println("Tie start");
//            } else if (token.startsWith("te")) {
//                System.out.println("Tie end");
//            } else {
//                double d = Double.parseDouble(token);
//                System.out.println("d:" + d);
//            }
//
//        }
//        scanner.close();
//        System.out.println();
//    }
//
//    private static void printScanned(final String s) {
//        final Scanner scanner = new Scanner(s);
//        scanner.useDelimiter(",");
//        System.out.println("scanned");
//        while (scanner.hasNext()) {
//            final String rest = scanner.next().trim();
//            System.out.println(rest);
//        }
//        System.out.println();
//        scanner.close();
//    }

    /*
     * durations.add(Duration.getDoubleDotted(DOUBLE_WHOLE_NOTE));
        durations.add(Duration.getDotted(DOUBLE_WHOLE_NOTE));
        durations.add(DOUBLE_WHOLE_NOTE);
        durations.add(Duration.getDoubleDotted(WHOLE_NOTE));
        durations.add(Duration.getDotted(WHOLE_NOTE));
        durations.add(WHOLE_NOTE);
        durations.add(Duration.getDoubleDotted(HALF_NOTE));
        durations.add(Duration.getDotted(HALF_NOTE));
        durations.add(HALF_NOTE);
        durations.add(Duration.getDoubleDotted(QUARTER_NOTE));
        durations.add(Duration.getDotted(QUARTER_NOTE));
        durations.add(QUARTER_NOTE);
     */
    enum Dur {

        DOUBLE_WHOLE_NOTE_DD(Duration
                .getDoubleDotted(Duration.DOUBLE_WHOLE_NOTE)),
        DOUBLE_WHOLE_NOTE_D(Duration.getDotted(Duration.DOUBLE_WHOLE_NOTE)),
        DOUBLE_WHOLE_NOTE(Duration.DOUBLE_WHOLE_NOTE),

        WHOLE_NOTE_DD(Duration.getDoubleDotted(Duration.WHOLE_NOTE)),
        WHOLE_NOTE_D(Duration.getDotted(Duration.WHOLE_NOTE)),
        WHOLE_NOTE(Duration.WHOLE_NOTE),

        HALF_NOTE_DD(Duration.getDoubleDotted(Duration.HALF_NOTE)),
        HALF_NOTE_D(Duration.getDotted(Duration.HALF_NOTE)),
        HALF_NOTE(Duration.HALF_NOTE),

        QUARTER_NOTE_DD(Duration.getDoubleDotted(Duration.QUARTER_NOTE)),
        QUARTER_NOTE_D(Duration.getDotted(Duration.QUARTER_NOTE)),
        QUARTER_NOTE(Duration.QUARTER_NOTE),

        EIGHTH_NOTE_DD(Duration.getDoubleDotted(Duration.EIGHTH_NOTE)),
        EIGHTH_NOTE_D(Duration.getDotted(Duration.EIGHTH_NOTE)),
        EIGHTH_NOTE(Duration.EIGHTH_NOTE),

        SIXTEENTH_NOTE_DD(Duration.getDoubleDotted(Duration.SIXTEENTH_NOTE)),
        SIXTEENTH_NOTE_D(Duration.getDotted(Duration.SIXTEENTH_NOTE)),
        SIXTEENTH_NOTE(Duration.SIXTEENTH_NOTE),

        THIRTY_SECOND_NOTE_DD(Duration
                .getDoubleDotted(Duration.THIRTY_SECOND_NOTE)),
        THIRTY_SECOND_NOTE_D(Duration.getDotted(Duration.THIRTY_SECOND_NOTE)),
        THIRTY_SECOND_NOTE(Duration.THIRTY_SECOND_NOTE),

        SIXTY_FOURTH_NOTE_DD(Duration
                .getDoubleDotted(Duration.SIXTY_FOURTH_NOTE)),
        SIXTY_FOURTH_NOTE_D(Duration.getDotted(Duration.SIXTY_FOURTH_NOTE)),
        SIXTY_FOURTH_NOTE(Duration.SIXTY_FOURTH_NOTE),

        ONE_TWENTY_EIGHTH_NOTE_DD(Duration
                .getDoubleDotted(Duration.ONE_TWENTY_EIGHTH_NOTE)),
        ONE_TWENTY_EIGHTH_NOTE_D(Duration
                .getDotted(Duration.ONE_TWENTY_EIGHTH_NOTE)),
        ONE_TWENTY_EIGHTH_NOTE(Duration.ONE_TWENTY_EIGHTH_NOTE);

        // public static final double QUARTER_TRIPLET_NOTE = 2D / 3D;
        // public static final double EIGHTH_TRIPLET_NOTE = 1D / 3D;
        // public static final double SIXTEENTH_TRIPLET_NOTE = .5 / 3D;

        double duration;

        Dur(double d) {
            this.duration = d;
        }
    }
}
