package com.rockhoppertech.music;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 *
 */
public class PitchFormat implements Serializable {
	/**
     * 
     */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory
			.getLogger(PitchFormat.class);
	
	protected static boolean isTraceEnabled;
	protected static boolean isDebugEnabled;
	protected static boolean isInfoEnabled;
	protected static boolean isWarnEnabled;
	protected static boolean isErrorEnabled;

	private boolean displayAsSharp = false;
	private int justification = LEFT_JUSTIFY;
	private int width = 3;

	public static final String[] FLATPITCHES = { "C", "Db", "D", "Eb", "E",
			"F", "Gb", "G", "Ab", "A", "Bb", "B" };

	// TODO allow this to be selectable
	// public static final String[] FLATPITCHES = { "C", "Df", "D", "Ef", "E",
	// "F", "Gf", "G", "Af", "A", "Bf", "B" };

	public static final String[] SHARPPITCHES = { "C", "C#", "D", "D#", "E",
			"F", "F#", "G", "G#", "A", "A#", "B" };
	private static Map<Integer, String> flatPitchMap;
	private static Map<Integer, String> sharpPitchMap;
	static {
		isTraceEnabled = logger.isTraceEnabled();
		isDebugEnabled = logger.isDebugEnabled();
		isInfoEnabled = logger.isInfoEnabled();
		isWarnEnabled = logger.isWarnEnabled();
		isErrorEnabled = logger.isErrorEnabled();
		flatPitchMap = new HashMap<Integer, String>();
		for (int i = 0; i < FLATPITCHES.length; i++) {
			flatPitchMap.put(new Integer(i), FLATPITCHES[i]);
		}
		sharpPitchMap = new HashMap<Integer, String>();
		for (int i = 0; i < SHARPPITCHES.length; i++) {
			sharpPitchMap.put(new Integer(i), SHARPPITCHES[i]);
		}

	}



	/**
	 * Justification constant
	 */
	public static final int LEFT_JUSTIFY = 0;
	/**
	 * Justification constant
	 */
	public static final int RIGHT_JUSTIFY = 1;
	/**
	 * Justification constant
	 */
	public static final int CENTER_JUSTIFY = 2;

	/**
	 * This is a singleton.
	 */
	private static PitchFormat instance;

	/**
	 * <p>
	 * The factory method to get the Singleton instance of this class.
	 * </p>
	 * 
	 * @return
	 */
	public static synchronized PitchFormat getInstance() {
		if (instance == null) {
			instance = new PitchFormat();
		}
		return instance;
	}

	private PitchFormat() {
		if (isTraceEnabled) {
			logger.trace(new Throwable().getStackTrace()[0].getMethodName());
		}
	}

	/**
	 * <p>
	 * If you want to use and alternative map.
	 * </p>
	 * 
	 * @param m
	 */
	public static void setFlatPitchMap(Map<Integer, String> m) {
		flatPitchMap = m;
	}

	/**
	 * <p>
	 * If you want to use and alternative map.
	 * </p>
	 * 
	 * @param m
	 */
	public static void setSharpPitchMap(Map<Integer, String> m) {
		sharpPitchMap = m;
	}

	/**
	 * <p>
	 * Displays string representation as sharps. You will see C# and not Df.
	 * </p>
	 */
	public void setDisplaySharps() {
		this.displayAsSharp = true;
	}

	/**
	 * <p>
	 * Displays string representation as flats. You will see Df and not C#.
	 * </p>
	 */
	public void setDisplayFlats() {
		this.displayAsSharp = false;
	}

	/**
	 * Justification of string representation
	 * 
	 * @param j
	 *            Can be one of <code>
	 * Pitch.LEFT_JUSTIFY,
	 * Pitch.RIGHT_JUSTIFY,
	 * Pitch.CENTER_JUSTIFY</code>
	 */
	public void setJustification(int j) {
		switch (j) {
		case PitchFormat.LEFT_JUSTIFY:
		case PitchFormat.RIGHT_JUSTIFY:
		case PitchFormat.CENTER_JUSTIFY:
			this.justification = j;
			break;
		default:
			logger.error("Invalid justification!" + j);
			throw new IllegalArgumentException("Invalid justification!" + j);
		}
	}

	/*
	 * <p> </p>
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o instanceof PitchFormat == false) {
			throw new IllegalArgumentException("Not a PitchFormat: " + o);
		}
		PitchFormat op = (PitchFormat) o;
		boolean val = false;
		if (this.width == op.width) {
			val = true;
		} else {
			val = false;
		}
		if (this.justification == op.justification) {
			val = true;
		} else {
			val = false;
		}
		if (this.displayAsSharp == op.displayAsSharp) {
			val = true;
		} else {
			val = false;
		}
		return val;
	}

	/*
	 * <p>The usual. </p>
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("PitchFormat[");
		sb.append(" width=").append(this.width);
		sb.append(" justification=").append(this.justification);
		sb.append(" displayAsSharp=").append(this.displayAsSharp);
		sb.append("]");
		return sb.toString();
	}

	/**
     *
     */
//	private String rightJustify(String p) {
//		if (isTraceEnabled) {
//			logger.trace(new Throwable().getStackTrace()[0].getMethodName());
//		}
//
//		StringBuilder b = new StringBuilder(width);
//
//		int index = width - p.length();
//		for (int i = 0; i < index; i++) {
//			b.insert(i, ' ');
//		}
//		b.insert(index, p);
//		return b.toString();
//	}

	/**
	 * Used internally to get a left justified string.
	 */
//	private String leftJustify(String p) {
//		if (isTraceEnabled) {
//			logger.trace(new Throwable().getStackTrace()[0].getMethodName());
//		}
//
//		StringBuilder b = new StringBuilder(width);
//		b.insert(0, p);
//		int index = p.length();
//		for (int i = index; i < width; i++) {
//			b.insert(i, ' ');
//		}
//		return b.toString();
//	}

	/**
	 * Used internally to get a center justified string.
	 */
//	private String centerJustify(String p) {
//		if (isTraceEnabled) {
//			logger.trace(new Throwable().getStackTrace()[0].getMethodName());
//		}
//
//		StringBuilder b = new StringBuilder(width);
//
//		int len = p.length();
//		int pad = (width - len) / 2;
//
//		if (isDebugEnabled) {
//			logger.debug("string length: " + len);
//			logger.debug("pad: " + pad);
//		}
//
//		for (int i = 0; i < pad; i++) {
//			b.insert(i, ' ');
//		}
//
//		b.insert(pad, p);
//		for (int i = pad + len; i < width; i++) {
//			b.insert(i, ' ');
//		}
//
//		return b.toString();
//	}

	/**
	 * Used internally to get a right justified string.
	 */
	private static String rightJustify(String p, int width) {
		StringBuilder b = new StringBuilder(width);

		int index = width - p.length();
		for (int i = 0; i < index; i++) {
			b.insert(i, ' ');
		}
		b.insert(index, p);
		return b.toString();
	}

	/**
	 * Used internall to get a left justified string.
	 */
	private static String leftJustify(String p, int width) {
		StringBuilder b = new StringBuilder(width);
		b.insert(0, p);
		int index = p.length();
		for (int i = index; i < width; i++) {
			b.insert(i, ' ');
		}
		return b.toString();
	}

	/**
	 * <p>
	 * Used internally to get a center justified string.
	 * </p>
	 * 
	 * @param p
	 * @param width
	 * @return
	 */
	private static String centerJustify(String p, int width) {
		if (isTraceEnabled) {
			logger.trace(new Throwable().getStackTrace()[0].getMethodName());
		}

		StringBuilder b = new StringBuilder(width);

		int len = p.length();
		int pad = (width - len) / 2;
		if (isDebugEnabled) {
			logger.debug("string length: " + len);
			logger.debug("width: " + width);
			logger.debug("pad: " + pad);
		}

		for (int i = 0; i < pad; i++) {
			b.insert(i, ' ');
		}
		b.insert(pad, p);
		for (int i = pad + len; i < width; i++) {
			b.insert(i, ' ');
		}

		return b.toString();
	}

	/**
	 * <p>
	 * Set the field width used when formatted.
	 * </p>
	 * 
	 * @param w
	 *            The field width of the string representation
	 */
	public void setWidth(int w) {
		if (w < 3) {
			throw new IllegalArgumentException("Invalid width. Must not be < 3"
					+ w);
		}
		this.width = w;
	}

	/**
	 * <p>
	 * flats are displayed, the justification is centered, and the width is 3
	 * </p>
	 * 
	 * @param midiNumber
	 *            the midi number. 60 is middle c (C5)
	 * @return a string representation of a midi number
	 */
	public static String midiNumberToString(int midiNumber) {
		return PitchFormat.midiNumberToString(midiNumber, false,
				PitchFormat.CENTER_JUSTIFY, 3);
	}

	/**
	 * 
	 * <p>
	 * The version for micromanaging control freaks.
	 * </p>
	 * 
	 * @param midiNumber
	 *            the midi number. 60 is middle c (C5)
	 * @param pdisplayAsSharp
	 *            use # or f for accidentals
	 * @param justification
	 *            can be any of the static constants
	 * @param width
	 *            the string length returned
	 * @return a string representation of a midi number
	 */
	public static String midiNumberToString(int midiNumber,
			boolean pdisplayAsSharp, int justification, int width) {

		if (isTraceEnabled) {
			logger.trace(new Throwable().getStackTrace()[0].getMethodName());
			logger.trace("justification: " + justification);
		}
		if (midiNumber < Pitch.MIN || midiNumber > Pitch.MAX) {
			throw new InvalidMIDIPitchException(
					"Number is out of MIDI pitch range " + midiNumber);
		}

		int pc = midiNumber % 12;
		int oct = midiNumber / 12;
		String str = null;
		if (pdisplayAsSharp) {
			str = ((String) sharpPitchMap.get(new Integer(pc))) + oct;
		} else {
			// str = FLATPITCHES[pc] + oct;
			str = ((String) flatPitchMap.get(new Integer(pc))) + oct;
		}
		switch (justification) {
		case PitchFormat.LEFT_JUSTIFY:
			str = PitchFormat.leftJustify(str, width);
			break;
		case PitchFormat.RIGHT_JUSTIFY:
			str = PitchFormat.rightJustify(str, width);
			break;
		case PitchFormat.CENTER_JUSTIFY:
			str = PitchFormat.centerJustify(str, width);
			break;
		}
		return (str);
	}

	public static int getOctave(Pitch p) {
		int oct = p.getMidiNumber() / 12;
		return oct;
	}

	/**
	 * <p>
	 * Return the MIDI pitch number for the given string which contains the
	 * octave number. <code>C5</code> is middle C.
	 * </p>
	 * 
	 * @param pitch
	 *            the string to convert
	 * @return the midinumber of the string parameter
	 */
	public static int stringToMidiNumber(String pitch) {
		pitch = pitch.toLowerCase().trim();
		char c = pitch.charAt(0);

		// the . is a dummy place holder just to yield the correct pitch class
		String s = "c.d.ef.g.a.b";
		int pc = s.indexOf(c);
		boolean wasCb = false;
		if (pitch.startsWith("cb") || pitch.startsWith("cf")) {
			pc = 11;
			wasCb = true;
		}
		int octave = 5;
		if (pitch.length() == 1) {
			// if just the pitch name is entered, e.g. C, default to middle C
			// octave
			return pc + (octave * 12);
		}

		int octIndex = 1;
		if (pitch.charAt(1) == '#') {
			pc++;
			octIndex++;
		} else if (pitch.charAt(1) == 'b' || pitch.charAt(1) == 'f') {
			if (!wasCb) {
				pc--;
			}
			octIndex++;
		}

		String ostr = pitch.substring(octIndex);
		if (ostr == null || ostr.equals(""))
			ostr = "5";
		
		try {
			octave = Integer.parseInt(ostr) * 12;
		} catch (NumberFormatException e) {
			throw e;
		}
		return (pc + octave);
	}

	/**
	 * Given the pitch return just the pitch class.
	 * 
	 * @param pitch
	 * @return
	 */
	public static int stringToPC(String pitch) {
		pitch = pitch.toLowerCase().trim();
		char c = pitch.charAt(0);

		// the . is a dummy place holder just to yield the correct pitch class
		String s = "c.d.ef.g.a.b";
		int pc = s.indexOf(c);

		if (pitch.length() > 1) {
			if (pitch.charAt(1) == '#') {
				pc++;
			} else if (pitch.charAt(1) == 'b' || pitch.charAt(1) == 'f') {
				pc--;
			}
		}

		return pc;
	}

	/**
	 * <p>
	 * Checks to see if the num mod 12 has an accidental (is not diatonic)
	 * </p>
	 * 
	 * @param num
	 * @return true if the num is not diatonic
	 */
	public static boolean isAccidental(int num) {
		boolean r = false;
		switch (num % 12) {
		case 1:
		case 3:
		case 6:
		case 8:
		case 10:
			r = true;
			break;
		}
		return r;
	}

	/**
	 * <p>
	 * This is the main method to use to format a Pitch. It uses the previously
	 * set intance variables for sharps/flats, justification and width
	 * </p>
	 * 
	 * @param p
	 * @return
	 */
	public String format(Pitch p) {
	    String preferred = p.getPreferredSpelling();
	    if(preferred != null) {
	        switch (justification) {
	        case PitchFormat.LEFT_JUSTIFY:
	            preferred = PitchFormat.leftJustify(preferred, width);
	            break;
	        case PitchFormat.RIGHT_JUSTIFY:
	            preferred = PitchFormat.rightJustify(preferred, width);
	            break;
	        case PitchFormat.CENTER_JUSTIFY:
	            preferred = PitchFormat.centerJustify(preferred, width);
	            break;
	        }
	        return preferred;
	    }
	   
		String s = PitchFormat.midiNumberToString(p.getMidiNumber(),
				this.displayAsSharp, this.justification, this.width);
		return s;
	}
	public String format(int midiNumber) {
		String s = PitchFormat.midiNumberToString(midiNumber,
				this.displayAsSharp, this.justification, this.width);
		return s;
	}

	/**
	 * just a test
	 */
	public static void main(String[] args) {
		Pitch[] a = new Pitch[12];
		PitchFormat pf = PitchFormat.getInstance();
		int num = 60;

		for (int i = Pitch.C0; i < Pitch.C1; i += Pitch.MINOR_SECOND) {
			a[i] = PitchFactory.getPitch(num++);
		}

		pf.setJustification(PitchFormat.CENTER_JUSTIFY);
		pf.setWidth(4);

		System.out.println("center justified width = 4");
		for (int i = Pitch.C0; i < Pitch.C1; i += Pitch.MINOR_SECOND) {
			System.out.print(pf.format(a[i]) + "|");
		}
		System.out.println();

		pf.setWidth(8);
		System.out.println("center justified width = 8");
		for (int i = Pitch.C0; i < Pitch.C1; i += Pitch.MINOR_SECOND) {
			System.out.print(pf.format(a[i]) + "|");
		}
		System.out.println();

		pf.setWidth(4);
		System.out.println("left justified");
		pf.setJustification(PitchFormat.LEFT_JUSTIFY);
		for (int i = 0; i < 12; i++) {
			System.out.print(pf.format(a[i]) + "|");
		}
		System.out.println();

		System.out.println("right justified");
		pf.setJustification(PitchFormat.RIGHT_JUSTIFY);
		for (int i = 0; i < 12; i++) {
			System.out.print(pf.format(a[i]) + "|");
		}
		System.out.println();

		System.out.println("right justified as sharps");
		pf.setJustification(PitchFormat.RIGHT_JUSTIFY);
		pf.setDisplaySharps();
		for (int i = 0; i < 12; i++) {
			System.out.print(pf.format(a[i]) + "|");
		}
		System.out.println();

		/*
		 * for (int i = 0; i < 12; i++) { System.out.print(
		 * PitchFormat.stringToMidiNumber(a[i].toString()) + " "); }
		 * System.out.println();
		 */

		System.out.println("midi numz 60 to 108");
		for (int i = 60; i < 108; i++) {
			/*
			 * System.out.print( Pitch.midiNumberToString(i,false,
			 * Pitch.CENTER_JUSTIFY, 3) + "|");
			 */
			System.out.print(PitchFormat.midiNumberToString(i) + "|");
		}
		System.out.println();
	}

	/**
	 * <p>
	 * You might want just the pitch name without the octave designation. This
	 * is handy when building chords for example.
	 * </p>
	 * <p>
	 * Given 60 you will get C, 61 is Df
	 * </p>
	 * 
	 * @param p
	 *            The pitch
	 * @return
	 */
	public static String getPitchString(Pitch p) {
		return getPitchString(p.getMidiNumber());
	}

	public static String getPitchString(Pitch p, boolean pdisplayAsSharp) {
		return getPitchString(p.getMidiNumber(), pdisplayAsSharp);
	}

	/**
	 * <p>
	 * You might want just the pitch name without the octave designation. This
	 * is handy when building chords or key signatures for example.
	 * </p>
	 * <p>
	 * Given 60 you will get C, 61 is Df
	 * </p>
	 * 
	 * @param midiNumber
	 * @return
	 */
	public static String getPitchString(int midiNumber) {
		return flatPitchMap.get(midiNumber % 12);
	}

	public static String getPitchString(int midiNumber, boolean pdisplayAsSharp) {
		if (pdisplayAsSharp) {
			return sharpPitchMap.get(midiNumber % 12);
		} else {
			return flatPitchMap.get(midiNumber % 12);
		}
	}

}
