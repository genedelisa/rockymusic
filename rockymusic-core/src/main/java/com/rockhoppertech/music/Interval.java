package com.rockhoppertech.music;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.scale.Scale;
import com.rockhoppertech.music.scale.ScaleFactory;

/**
 * Utilities to convert between scale or chord spellings, intervals, degrees.
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class Interval {
	private final static Logger logger = LoggerFactory
			.getLogger(Interval.class);
	public static final int UNISON = 0;
	public static final int MINOR_SECOND = 1;
	public static final int MAJOR_SECOND = 2;
	public static final int MINOR_THIRD = 3;
	public static final int MAJOR_THIRD = 4;
	public static final int PERFECT_FOURTH = 5;
	public static final int AUGMENTED_FOURTH = 6;
	public static final int DIMINISHED_FIFTH = 6;
	public static final int PERFECT_FIFTH = 7;
	public static final int AUGMENTED_FIFTH = 8;
	public static final int MINOR_SIXTH = 8;
	public static final int MAJOR_SIXTH = 9;
	public static final int DIMINISHED_SEVENTH = 9;
	public static final int MINOR_SEVENTH = 10;
	public static final int MAJOR_SEVENTH = 11;
	public static final int OCTAVE = 12;

	public static final int MINOR_NINTH = 13;
	public static final int MAJOR_NINTH = 14;
	// public static final int MINOR_ELEVENTH = 13;
	public static final int MAJOR_ELEVENTH = 17;
	public static final int MINOR_THIRTEENTH = 20;
	public static final int MAJOR_THIRTEENTH = 21;

	private static Map<String, Integer> spellings = new HashMap<String, Integer>();
	private static Map<String, Integer> scaleSpellings = new LinkedHashMap<String, Integer>();
	private static List<String> names = new ArrayList<String>();
	private static List<String> roman = new ArrayList<String>();
	static {
		// chord spellings
		Interval.spellings.put("1", 0);
		Interval.spellings.put("b2", 1);
		Interval.spellings.put("2", 2);
		Interval.spellings.put("b3", 3);
		Interval.spellings.put("#2", 3);
		Interval.spellings.put("3", 4);
		Interval.spellings.put("4", 5);
		Interval.spellings.put("#4", 6);
		Interval.spellings.put("b5", 6);
		Interval.spellings.put("5", 7);
		Interval.spellings.put("#5", 8);
		Interval.spellings.put("b6", 8);
		Interval.spellings.put("6", 9);
		Interval.spellings.put("#6", 10);
		Interval.spellings.put("bb7", 9);
		Interval.spellings.put("b7", 10);
		Interval.spellings.put("7", 11);
		Interval.spellings.put("b8", 11);
		Interval.spellings.put("12", 12); // TODO not used in chords but in
											// scales. huh? these are chord
											// spellings.
		Interval.spellings.put("b9", 13);
		Interval.spellings.put("9", 14);
		Interval.spellings.put("#9", 15);
		Interval.spellings.put("b11", 16);
		Interval.spellings.put("11", 17);
		Interval.spellings.put("#11", 18);
		Interval.spellings.put("b13", 20);
		Interval.spellings.put("13", 21);

		// scaleSpellings.put("1", 0);
		Interval.scaleSpellings.put("1", 0);
		Interval.scaleSpellings.put("b2", 1);
		Interval.scaleSpellings.put("2", 2);
		Interval.scaleSpellings.put("b3", 3);
		Interval.scaleSpellings.put("#2", 3);
		Interval.scaleSpellings.put("3", 4);
		Interval.scaleSpellings.put("4", 5);
		Interval.scaleSpellings.put("#4", 6);
		Interval.scaleSpellings.put("b5", 6);
		Interval.scaleSpellings.put("5", 7);
		Interval.scaleSpellings.put("#5", 8);
		Interval.scaleSpellings.put("b6", 8);
		Interval.scaleSpellings.put("6", 9);
		Interval.scaleSpellings.put("#6", 10);
		Interval.scaleSpellings.put("bb7", 9);
		Interval.scaleSpellings.put("b7", 10);
		Interval.scaleSpellings.put("7", 11);
		Interval.scaleSpellings.put("8", 12);
		Interval.scaleSpellings.put("b9", 13);
		Interval.scaleSpellings.put("9", 14);
		Interval.scaleSpellings.put("#9", 15);
		Interval.scaleSpellings.put("b11", 16);
		Interval.scaleSpellings.put("11", 17);
		Interval.scaleSpellings.put("#11", 18);
		Interval.scaleSpellings.put("b13", 20);
		Interval.scaleSpellings.put("13", 21);

		// not used yet
		Interval.names.add("Unison");
		Interval.names.add("Minor second");
		Interval.names.add("Major second");
		Interval.names.add("Minor third");
		Interval.names.add("Major third");
		Interval.names.add("Perfect fourth");
		Interval.names.add("Tritone");
		Interval.names.add("Perfect fifth");
		Interval.names.add("Minor sixth");
		Interval.names.add("Major sixth");
		Interval.names.add("Minor seventh");
		Interval.names.add("Major seventh");
		Interval.names.add("Octave");
		Interval.names.add("Minor ninth");
		Interval.names.add("Major ninth");
		Interval.names.add("Minor tenth");
		Interval.names.add("Major tenth");
		Interval.names.add("Eleventh");
		Interval.names.add("Augmented eleventh");
		// made this up
		Interval.names.add("'Twelfth'");
		Interval.names.add("Minor thirteenth");
		Interval.names.add("Major thirteenth");
		// there's no real name for the next two
		Interval.names.add("Augmented thirteenth");
		Interval.names.add("'Fourteenth'");
		Interval.names.add("2 Octaves");

		Interval.roman.add("I");
		Interval.roman.add("ii");
		Interval.roman.add("II");
		Interval.roman.add("iii");
		Interval.roman.add("III");
		Interval.roman.add("IV");
		Interval.roman.add("vo");
		Interval.roman.add("V");
		Interval.roman.add("V+");
		Interval.roman.add("VI");
		Interval.roman.add("vii");
		Interval.roman.add("VII");
		Interval.roman.add("I");
	}

	public static String getName(final int i) {
		if (i >= names.size()) {
			return "Unknown";
		}
		return names.get(i);
	}

	static String arrayToString(final int[] a) {
		final StringBuilder sb = new StringBuilder();
		for (int j = 0; j < a.length; j++) {
			sb.append(a[j]);
			if (j < a.length - 1) {
				sb.append(',');
			}
		}
		return sb.toString();
	}

	/**
	 * 
	 * @param spelling
	 *            is a chord spelling
	 * @return an array of intervals
	 */
	public static int[] degreesToIntervals(final String spelling) {
		Interval.logger.debug("spelling: \'" + spelling + "\'");
		// \s is A whitespace character: [ \t\n\x0B\f\r]
		// see java.util.regex.Pattern
		final String[] degrees = spelling.split("\\s");
		for (final String degree : degrees) {
			Interval.logger.debug("degrees: " + degree);
		}
		// given "1 3 5" split will give an array
		// with 1 in index 0 3 in index 2 etc

		final int[] intervals = new int[degrees.length - 1];
		// all of the degrees start with 1 so we skip that one
		// for 1 3 5 we get 4 7 for the intervals
		for (int i = 0; i < intervals.length; i++) {
			intervals[i] = Interval.getSpelling(degrees[i + 1]);
			Interval.logger.debug("intervals {}", intervals[i]);
		}
		return intervals;
	}

	/**
	 * input can be comma or whitespace delimited
	 * 
	 * @param s
	 *            a scale spelling
	 * @return
	 */
	public static int[] getIntervalsFromDegreeString(final String s) {

		final Scanner scanner = new Scanner(s);
		if (s.indexOf(',') != -1) {
			scanner.useDelimiter(",");
			Interval.logger.debug("comma delimited");
		}

		final List<Integer> pitches = new ArrayList<Integer>();
		while (scanner.hasNext()) {
			final String string = scanner.next().trim();
			Interval.logger.debug("degreestring: " + string);

			int pc = Interval.getScaleSpelling(string);
			pc++;
			pitches.add(pc);
		}
		final int size = pitches.size();
		final int[] array = new int[size - 1];
		for (int i = 1; i < size; i++) {
			final int val = pitches.get(i) - pitches.get(i - 1);
			Interval.logger.debug("val {}", val);
			array[i - 1] = val;
		}
		scanner.close();
		return array;
	}

	/**
	 * 
	 * @param s
	 * @return an array of intervals
	 */
	public static int[] getIntervalsFromPitchString(final String s) {

		final Scanner scanner = new Scanner(s);
		if (s.indexOf(',') != -1) {
			scanner.useDelimiter(",");
			Interval.logger.debug("comma delimited");
		}

		final List<Integer> pitches = new ArrayList<Integer>();
		while (scanner.hasNext()) {
			final String string = scanner.next().trim();
			Interval.logger.debug("pitchstring: " + string);
			final int pc = PitchFormat.stringToPC(string);
			pitches.add(pc);
		}
		final int size = pitches.size();
		final int[] array = new int[size - 1];
		for (int i = 1; i < size; i++) {
			final int val = pitches.get(i) - pitches.get(i - 1);
			Interval.logger.debug("val {}", val);
			array[i - 1] = val;
		}
		scanner.close();
		return array;
	}

	/**
	 * 
	 * @param text
	 * @return
	 */
	public static int[] getIntervalsFromScaleSpelling(final String s) {
		final Scanner scanner = new Scanner(s);
		if (s.indexOf(',') != -1) {
			scanner.useDelimiter(",");
		}

		final List<Integer> intervals = new ArrayList<Integer>();
		while (scanner.hasNext()) {
			final String interval = scanner.next().trim();
			intervals.add(Interval.getScaleSpelling(interval));
		}
		final int size = intervals.size();
		final int[] array = new int[size - 1];
		for (int i = 1; i < size; i++) {
			final int val = intervals.get(i) - intervals.get(i - 1);
			Interval.logger.debug("val {}", val);
			array[i - 1] = val;
		}
		scanner.close();
		return array;
	}

	/**
	 * Degrees to intervals. e.g. Input 1 3 5 will give you 4,3
	 * 
	 * 
	 * @param s
	 * @return
	 */
	public static int[] getIntervalsFromSpelling(final String s) {

		// if the string has commas as delimiters
		final Scanner scanner = new Scanner(s);
		if (s.indexOf(',') != -1) {
			scanner.useDelimiter(",");
		}

		final List<Integer> intervals = new ArrayList<Integer>();
		while (scanner.hasNext()) {
			final String interval = scanner.next().trim();
			intervals.add(Interval.getSpelling(interval));
		}
		final int size = intervals.size();
		final int[] array = new int[size - 1];
		for (int i = 1; i < size; i++) {
			final int val = intervals.get(i) - intervals.get(i - 1);
			logger.debug("val {}", val);
			array[i - 1] = val;
		}
		scanner.close();
		return array;
	}

	/**
	 * Relative intervals.
	 * 
	 * Wraps integers in a string into an array Non specific. the input integers
	 * could be midinumbers, durations etc.
	 * 
	 * @param s
	 *            a string containing integers
	 * @return an array of the same integers
	 */
	public static int[] getIntervalsFromString(final String s) {

		final Scanner scanner = new Scanner(s);
		if (s.indexOf(',') != -1) {
			scanner.useDelimiter(",");
		}

		final List<Integer> ints = new ArrayList<Integer>();
		while (scanner.hasNext()) {
			final String interval = scanner.next().trim();
			ints.add(Integer.parseInt(interval));
		}
		final int size = ints.size();
		// final int[] array = new int[size];
		// for (int i = 0; i < array.length; i++) {
		// array[i] = ints.get(i).intValue();
		// }

		final int[] array = new int[size - 1];
		for (int i = 1; i < size; i++) {
			final int val = ints.get(i) - ints.get(i - 1);
			logger.debug("val {}", val);
			array[i - 1] = val;
		}
		scanner.close();
		return array;
	}

	/**
	 * Return intervals in relation to first int. Can be comma delimted or not.
	 * Non specific: can be ints, midinumbers etc.
	 * 
	 * @param s
	 *            a string that can be parsed into an int
	 * @return an int[] array of absolute intervals
	 */
	public static int[] getAbsoluteIntervalsFromString(final String s) {

		final Scanner scanner = new Scanner(s);
		if (s.indexOf(',') != -1) {
			scanner.useDelimiter(",");
		}

		final List<Integer> ints = new ArrayList<Integer>();
		while (scanner.hasNext()) {
			final String interval = scanner.next().trim();
			ints.add(Integer.parseInt(interval));
		}
		final int size = ints.size();
		final int[] array = new int[size - 1];
		int root = ints.get(0);
		for (int i = 1; i < size; i++) {
			final int val = ints.get(i) - root;
			array[i - 1] = val;
		}
		scanner.close();

		return array;
	}

	/**
	 * Get the key from the map.
	 * 
	 * @param interval
	 * @return
	 */
	public static String getScaleSpelling(final int interval) {
		// TODO not really.
		for (final Entry<String, Integer> e : Interval.scaleSpellings
				.entrySet()) {
			if (e.getValue() == interval) {
				return e.getKey();
			}
		}
		return null;
	}

	/**
	 * 
	 * @param s
	 *            a scale spelling
	 * @return the number of half steps in the spelling
	 */
	public static int getScaleSpelling(final String s) {
		Interval.logger.debug(s);
		return Interval.scaleSpellings.get(s);
	}

	/**
	 * 
	 * @param interval
	 *            an interval in half steps
	 * @return null or the chord spelling for an interval
	 */
	public static String getSpelling(final int interval) {
		for (final Entry<String, Integer> e : Interval.spellings.entrySet()) {
			if (e.getValue() == interval) {
				return e.getKey();
			}
		}
		return null;
	}

	/**
	 * <code>getSpelling("13")</code> would yield the number of half steps in a
	 * thirteenth.
	 * 
	 * @param s
	 *            A chord spelling
	 * @return the number of half steps
	 */
	public static int getSpelling(final String s) {
		Interval.logger.debug(s);
		return Interval.spellings.get(s);
	}

	/**
	 * 
	 * @see Scale
	 * @param intervals
	 * @return
	 */
	public static int[] intervalsToDegrees(final int[] intervals) {
		final int[] degrees = new int[intervals.length + 1];
		// degrees[0] = 1;
		for (int i = 0; i < intervals.length; i++) {
			degrees[i + 1] = degrees[i] + intervals[i];
		}
		return degrees;
	}

	/**
	 * Can turn the intervals in a scale into their scale degrees. You can
	 * generate patterns.
	 * 
	 * @see ScaleFactory#getNoteListPattern(Scale, int[], int, int, int, double,
	 *      boolean, double)
	 * @param intervals
	 * @param nOctaves
	 * @return an array of degrees
	 */
	public static int[] intervalsToDegrees(final int[] intervals,
			final int nOctaves) {
		final int[] roots = { 0, 12, 24, 36, 48, 60, 72, 84, 96, 108, 120 };
		final List<Integer> list = new ArrayList<Integer>();
		list.add(0);
		for (int octave = 0; octave < nOctaves; octave++) {
			int previous = roots[octave];
			Interval.logger.debug("p " + previous);
			for (final int interval : intervals) {
				final int n = previous + interval;
				list.add(n);
				previous = n;
				Interval.logger.debug("pp " + previous);
				Interval.logger.debug("int " + interval);
			}
		}

		Integer[] ia = new Integer[list.size()];
		ia = list.toArray(ia);
		final int[] degrees = new int[ia.length];
		for (int i = 0; i < ia.length; i++) {
			Interval.logger.debug("degree " + ia[i]);
			degrees[i] = ia[i].intValue();
		}
		return degrees;
	}

	public static void main(final String[] args) {
		final String[] s = { "getIntervalsFromScaleSpelling",
				"degreesToIntervals",
				"getIntervalsFromDegreeString", "getIntervalsFromPitchString",
				"getIntervalsFromString", "intervalsToDegrees" };
		String v = null;

		v = (String) JOptionPane.showInputDialog(null, "Choose an operation",
				"Possible Operations", JOptionPane.QUESTION_MESSAGE, null, s,
				s[0]);

		if (v.equals(s[0])) {
			v = JOptionPane.showInputDialog("Enter a scale spelling");
			final int[] a = Interval.getIntervalsFromScaleSpelling(v);
			JOptionPane.showMessageDialog(null, Interval.arrayToString(a));
		} else if (v.equals(s[1])) {
			v = JOptionPane
					.showInputDialog("Enter a chord spelling in degrees");
			final int[] a = Interval.degreesToIntervals(v);
			JOptionPane.showMessageDialog(null, Interval.arrayToString(a));
		} else if (v.equals(s[2])) {
			v = JOptionPane
					.showInputDialog("Enter a chord spelling in degrees");
			final int[] a = Interval.getIntervalsFromDegreeString(v);
			JOptionPane.showMessageDialog(null, Interval.arrayToString(a));
		} else if (v.equals(s[3])) {
			v = JOptionPane.showInputDialog("Enter pitches");
			final int[] a = Interval.getIntervalsFromPitchString(v);
			JOptionPane.showMessageDialog(null, Interval.arrayToString(a));
		} else if (v.equals(s[4])) {
			v = JOptionPane.showInputDialog("Enter a series of ints");
			final int[] a = Interval.getIntervalsFromString(v);
			JOptionPane.showMessageDialog(null, Interval.arrayToString(a));
		} else if (v.equals(s[5])) {
			v = JOptionPane.showInputDialog("Enter a series of ints");

			final int[] a = Interval.intervalsToDegrees(
					Interval.getIntervalsFromString(v),
					1);
			JOptionPane.showMessageDialog(null, Interval.arrayToString(a));
		}

	}

	/**
	 * <p>
	 * given a string like 1, 2, 3, 4, b5, b6, b7 return the intervals like
	 * 2,2,1,1,2,2
	 * </p>
	 * 
	 * @param args
	 */
	public static void oldmain(final String[] args) {
		final String s = JOptionPane.showInputDialog("Enter pitches");
		final int[] array = Interval.getIntervalsFromSpelling(s);
		Interval.logger.debug("array {}", array);

		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < array.length; i++) {
			sb.append(array[i]);
			if (i < array.length - 1) {
				sb.append(',');
			}
			Interval.logger.debug("array i {}", array[i]);
		}
		/*
		 * 
		 * = new Scanner(s); scanner.useDelimiter(","); List<Integer> intervals
		 * = new ArrayList<Integer>(); while (scanner.hasNext()) { String
		 * interval = scanner.next().trim(); logger.debug("interval " +
		 * interval); logger.debug("spelling " + getSpelling(interval));
		 * intervals.add(getSpelling(interval)); } int size = intervals.size();
		 * for (int i = 1; i < size; i++) { int val = intervals.get(i) -
		 * intervals.get(i - 1); sb.append(val); if (i < size - 1)
		 * sb.append(','); logger.debug(val); }
		 */
		JOptionPane.showMessageDialog(null, sb.toString());
		Interval.logger.debug(sb.toString());

		/*
		 * sb = new StringBuilder(); for (Integer i : intervals) {
		 * sb.append(i).append(' '); } logger.debug(sb.toString());
		 */
	}

	/**
	 * <p>
	 * Spell a chord
	 * </p>
	 * 
	 * @param intervals
	 * @return
	 */
	public static String spell(final int[] intervals) {
		final StringBuffer spelling = new StringBuffer();
		for (final int interval : intervals) {
			spelling.append(Interval.getSpelling(interval));
			spelling.append(' ');
		}
		return spelling.toString();
	}

	/**
	 * 
	 * @param intervals
	 * @return a String with the scale spelling
	 */
	public static String spellScale(final int[] intervals) {
		final StringBuffer spelling = new StringBuffer();
		for (final int interval : intervals) {
			spelling.append(Interval.getScaleSpelling(interval));
			spelling.append(' ');
		}
		return spelling.toString();
	}

	public Interval() {

	}

}