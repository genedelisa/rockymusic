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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ImmutableMap;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class DurationParser {

	// or using google collections
	public static final ImmutableMap<String, Double> DMAP = new ImmutableMap.Builder<String, Double>()
			.put("d", Duration.DOUBLE_WHOLE_NOTE).put("w", Duration.WHOLE_NOTE)
			.put("h", Duration.HALF_NOTE).put("q", Duration.QUARTER_NOTE)
			.put("e", Duration.EIGHTH_NOTE).put("s", Duration.SIXTEENTH_NOTE)
			.put("t", Duration.THIRTY_SECOND_NOTE)
			.put("x", Duration.SIXTY_FOURTH_NOTE)
			.put("o", Duration.ONE_TWENTY_EIGHTH_NOTE).build();

	private static Map<String, Double> durKeyMap;

	// not dotted
	static Pattern pattern = Pattern.compile("[dwhqestxo]+[^\\.]*");
	// it is dotted
	static Pattern dpattern = Pattern.compile("[dwhqestxo]+\\.+");
	static Pattern tripletPattern = Pattern.compile("[dwhqestxo]+t+");

	static {
		durKeyMap = new LinkedHashMap<String, Double>();
		durKeyMap.put("dt", Duration.DOUBLE_WHOLE_TRIPLET_NOTE);
		durKeyMap.put("d", Duration.DOUBLE_WHOLE_NOTE);
		durKeyMap.put("wt", Duration.WHOLE_TRIPLET_NOTE);
		durKeyMap.put("w", Duration.WHOLE_NOTE);
		durKeyMap.put("ht", Duration.HALF_TRIPLET_NOTE);
		durKeyMap.put("h", Duration.HALF_NOTE);
		durKeyMap.put("qt", Duration.QUARTER_TRIPLET_NOTE);
		durKeyMap.put("q", Duration.QUARTER_NOTE);
		durKeyMap.put("et", Duration.EIGHTH_TRIPLET_NOTE);
		durKeyMap.put("e", Duration.EIGHTH_NOTE);
		durKeyMap.put("st", Duration.SIXTEENTH_TRIPLET_NOTE);
		durKeyMap.put("s", Duration.SIXTEENTH_NOTE);
		durKeyMap.put("tt", Duration.THIRTY_SECOND_TRIPLET_NOTE);
		durKeyMap.put("t", Duration.THIRTY_SECOND_NOTE);
		durKeyMap.put("xt", Duration.SIXTY_FOURTH_TRIPLET_NOTE);
		durKeyMap.put("x", Duration.SIXTY_FOURTH_NOTE);
		durKeyMap.put("ot", Duration.ONE_TWENTY_EIGHTH_TRIPLET_NOTE);
		durKeyMap.put("o", Duration.ONE_TWENTY_EIGHTH_NOTE);

	}

	public static final Pattern allPattern = Pattern
			.compile("\\s*[dwhqestxo]+t*\\.*");

	public static boolean couldBeDurationString(String s) {
		// return allPattern.matcher(s).matches();
		return allPattern.matcher(s).find();
	}

	public static double getDuration(String durationString) {
		// is it dotted?
		// Pattern dot = Pattern.compile("\\.+");
		if (dpattern.matcher(durationString).matches()) {
			// Matcher m = dpattern.matcher(durationString);
			// String token = durationString.substring(m.start(),
			// m.end());
			return getDottedValue(durationString);
		}
		return durKeyMap.get(durationString);
	}

	public static void print() {
		for (String key : durKeyMap.keySet()) {
			System.out
					.println(String.format("%s = %f", key, durKeyMap.get(key)));

			key += ".";
			System.out.println(String.format("%s = %f", key, getDuration(key)));

			key += ".";
			System.out.println(String.format("%s = %f", key, getDuration(key)));
		}
	}

	/**
	 * <pre>
	 * String both = &quot;1 2 3 q q. e .5 a q.. q...&quot;;
	 * TimeSeries ts = DurationParser.getDurationAsTimeSeries(both);
	 * </pre>
	 * 
	 * @param s
	 * @return
	 */
	// public static TimeSeries getDurationAsTimeSeries(String s) {
	// TimeSeries ts = new TimeSeries();
	// return getDurationAsTimeSeries(ts, s);
	// }

	/**
	 * Copies new events into the TimeSeries parameter - which is also returned
	 * 
	 * @param ts
	 * @param s
	 * @return
	 */
	// public static TimeSeries getDurationAsTimeSeries(TimeSeries ts, String s)
	// {
	// String token = null;
	// Scanner scanner = new Scanner(s);
	//
	// if (s.indexOf(',') != -1) {
	// scanner.useDelimiter(",");
	// }
	//
	// while (scanner.hasNext()) {
	// if (scanner.hasNext(dpattern)) {
	// token = scanner.next(dpattern);
	// double d = getDottedValue(token);
	// ts.add(d);
	// System.out.println(String.format("'%s' is dotted value is %f",
	// token, d));
	//
	// } else if (scanner.hasNext(pattern)) {
	// token = scanner.next(pattern);
	// double d = durKeyMap.get(token);
	// ts.add(d);
	// System.out.println(String.format(
	// "'%s' is not dotted value is %f", token, d));
	// // } else if (scanner.hasNext(tripletPattern)) {
	// // token = scanner.next(tripletPattern);
	// // double d = durKeyMap.get(token);
	// // ts.add(d);
	// // System.out.println(String
	// // .format("'%s' is not dotted value is %f",
	// // token,
	// // d));
	// } else if (scanner.hasNextDouble()) {
	// double d = scanner.nextDouble();
	// ts.add(d);
	// System.out.println(String.format("%f is a double", d));
	// } else {
	// // just ignore it. or throw exception?
	// String skipped = scanner.next();
	// System.err.println(String.format("skipped '%s'", skipped));
	// }
	// }
	// return ts;
	// }

	private static double getDottedValue(String token) {
		String s = token.substring(0, token.indexOf('.'));
		double val = durKeyMap.get(s);
		int dots = StringUtils.countMatches(token, ".");
		val = Duration.getDotted(val, dots);
		return val;
	}

	private static String getKeyString(double duration) {
		for (Entry<String, Double> e : durKeyMap.entrySet()) {
			if (e.getValue() == duration) {
				return e.getKey();
			}
		}
		return null;
	}

	private static String getNearestKeyString(double duration) {
		double dotted = Duration.getDotted(duration, 1);
		String key = null;
		// String p = null;
		for (Entry<String, Double> e : durKeyMap.entrySet()) {
			if (e.getValue() >= (dotted - duration)) {
				key = e.getKey();
			} else {
			}
		}
		return key;
	}

	// public static String getDurationString(TimeSeries series) {
	// StringBuilder sb = new StringBuilder();
	// for (Timed t : series) {
	// double d = t.getDuration();
	// if (durKeyMap.containsValue(d)) {
	// sb.append(getKeyString(d)).append(' ');
	// System.err.printf("contains value %s %f\n", sb, d);
	// continue;
	// }
	//
	// String nearest = getNearestKeyString(d);
	// double nearestDuration = durKeyMap.get(nearest);
	// System.err.printf("%fs nearest value %s nd %f\n", d, nearest,
	// nearestDuration);
	//
	// boolean foundDotted = false;
	// for (int i = 1; i < 3; i++) {
	// double dotted = Duration.getDotted(nearestDuration, i);
	// System.err.printf("dotted %d %f = %f?\n", i, dotted, d);
	// if (d == dotted) {
	// foundDotted = true;
	// sb.append(nearest);
	// for (int j = 0; j < i; j++) {
	// sb.append('.');
	// }
	// sb.append(' ');
	// }
	// }
	//
	// // not in the map and not dotted, just give the value
	// if (foundDotted == false) {
	// sb.append(d).append(' ');
	// }
	//
	// }
	// return sb.toString();
	//
	// }

	// public static String getDurationString(TimeSeries series) {
	// StringBuilder sb = new StringBuilder();
	// for (Timed t : series) {
	// double d = t.getDuration();
	// sb.append(getDurationString(d)).append(' ');
	// }
	// return sb.toString().trim();
	// }

	public static String getDurationString(double d) {
		StringBuilder sb = new StringBuilder();
		boolean foundInMap = false;
		if (durKeyMap.containsValue(d)) {
			sb.append(getKeyString(d)).append(' ');
			// System.err.printf("contains value %s %f\n", sb, d);
			foundInMap = true;
		}

		String nearest = getNearestKeyString(d);
		double nearestDuration = durKeyMap.get(nearest);
		// System.err.printf("%fs nearest value %s nd %f\n", d, nearest,
		// nearestDuration);

		boolean foundDotted = false;
		for (int i = 1; i < 3; i++) {
			double dotted = Duration.getDotted(nearestDuration, i);
			// System.err.printf("dotted %d %f = %f?\n", i, dotted, d);
			if (d == dotted) {
				foundDotted = true;
				sb.append(nearest);
				for (int j = 0; j < i; j++) {
					sb.append('.');
				}
				sb.append(' ');
			}
		}

		// not in the map and not dotted, just give the value
		if (foundDotted == false && foundInMap == false) {
			sb.append(d).append(' ');
		}

		return sb.toString().trim();

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DurationParser.print();

		// String both = "h h. h.. h... h.... q q. q..";
		// String both = "1 2 3 q q. e .5 a q.. q...";
		// TimeSeries ts = DurationParser.getDurationAsTimeSeries(both);
		// System.err.println(ts);
		//
		// ts = DurationParser.getDurationAsTimeSeries("q q q q");
		// System.err.println(DurationParser.getDurationString(ts));

		// TimeSeries ts = DurationParser
		// .getDurationAsTimeSeries("q q. q.. e e. w w. 1.321");
		// System.err.println(DurationParser.getDurationString(ts));

		System.err.println();
		String s = null;
		double d = .5;
		s = DurationParser.getDurationString(d);
		System.err.println(String.format("'%s' for %f", s, d));

		s = DurationParser.getDurationString(d = 1.0);
		System.err.println(String.format("'%s' for %f", s, d));

		s = DurationParser.getDurationString(d = 1.5);
		System.err.println(String.format("'%s' for %f", s, d));

		s = DurationParser.getDurationString(d = 1.321);
		System.err.println(String.format("'%s' for %f", s, d));

		s = DurationParser.getDurationString(d = 2.0);
		System.err.println(String.format("'%s' for %f", s, d));

		s = DurationParser.getDurationString(d = 3.0);
		System.err.println(String.format("'%s' for %f", s, d));

		s = DurationParser.getDurationString(d = 3.5);
		System.err.println(String.format("'%s' for %f", s, d));

		s = DurationParser.getDurationString(d = 4.0);
		System.err.println(String.format("'%s' for %f", s, d));

		// q q. q.. e e. w w. 1.321

	}

	/**
	 * @return the durKeyMap
	 */
	public static Map<String, Double> getDurKeyMap() {
		return Collections.unmodifiableMap(durKeyMap);
	}

	/**
	 * @param durKeyMap
	 *            the durKeyMap to set
	 */
	public static void setDurKeyMap(Map<String, Double> durKeyMap) {
		DurationParser.durKeyMap = durKeyMap;
	}

}
