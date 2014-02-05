package com.rockhoppertech.music.examples.series.time;

/*
 * #%L
 * Rocky Music Examples
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

import com.rockhoppertech.collections.CircularArrayList;
import com.rockhoppertech.collections.CircularList;
import com.rockhoppertech.music.Timed;
import com.rockhoppertech.music.series.time.TimeSeries;
import com.rockhoppertech.music.series.time.TimeSeriesFactory;

public class TimeSeriesFactoryExamples {
    /**
     * @param args
     */
    public static void main(String[] args) {
        TimeSeries timeSeries = TimeSeriesFactory.create(10);
        for (Timed e : timeSeries) {
            System.err.println(e);
        }

        System.err.println();
        timeSeries = TimeSeriesFactory.create(10, .5);
        for (Timed e : timeSeries) {
            System.err.println(e);
        }

        System.err.println();
        timeSeries = TimeSeriesFactory.create(10, .5, 5d);
        for (Timed e : timeSeries) {
            System.err.println(e);
        }

        CircularList<Double> durations = new CircularArrayList<Double>(
                new Double[] { .5, .25 });
//        System.err.println();
//        timeSeries = TimeSeriesFactory.create(10, durations);
//        for (Timed e : timeSeries) {
//            System.err.println(e);
//        }

        System.err.println();
        timeSeries = TimeSeriesFactory.create(10, durations, 10d);
        for (Timed e : timeSeries) {
            System.err.println(e);
        }

        /**
         * dwhqestxo and dots
         */
        timeSeries = TimeSeriesFactory.createFromDurationString("q q q q");
        System.err.println(timeSeries);

        timeSeries = TimeSeriesFactory
                .createFromDurationString("e s s e s s e s s e s s");
        System.err.println(timeSeries);
    }
}
