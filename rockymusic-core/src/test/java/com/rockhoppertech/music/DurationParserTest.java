/**
 * 
 */
package com.rockhoppertech.music;

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

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class DurationParserTest {
    private static final Logger logger = LoggerFactory
            .getLogger(DurationParserTest.class);

    /**
     * Test method for
     * {@link com.rockhoppertech.music.DurationParser#couldBeDurationString(java.lang.String)}
     * .
     */
    @Test
    public void testCouldBeDurationString() {
        boolean actual = DurationParser.couldBeDurationString("f");
        logger.debug("actual: {}", actual);
        boolean expected = false;
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

        actual = DurationParser.couldBeDurationString("e");
        logger.debug("actual: {}", actual);
        expected = true;
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.DurationParser#getDuration(java.lang.String)}
     * .
     */
    @Test
    public void testGetDuration() {
        double actual = DurationParser.getDuration("q");
        logger.debug("actual: {}", actual);
        double expected = 1d;
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.DurationParser#getDurationString(double)}
     * .
     */
    @Test
    public void testGetDurationString() {
        
        String actual = DurationParser.getDurationString(4d);
        logger.debug("actual: {}", actual);
        String expected = "w";
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.DurationParser#getDurKeyMap()}.
     */
    @Test
    public void testGetDurKeyMap() {
        Map<String, Double> map = DurationParser.getDurKeyMap();
        double actual = map.get("w");
        logger.debug("actual: {}", actual);
        double expected = 4d;
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));
    }

    /**
     * Test method for
     * {@link com.rockhoppertech.music.DurationParser#setDurKeyMap(java.util.Map)}
     * .
     */
    @Test
    public void testSetDurKeyMap() {
        Map<String, Double> durKeyMap = new LinkedHashMap<String, Double>();
        durKeyMap = new LinkedHashMap<String, Double>();
        durKeyMap.put("dt", Duration.DOUBLE_WHOLE_TRIPLET_NOTE * 2d);
        durKeyMap.put("d", Duration.DOUBLE_WHOLE_NOTE * 2d);
        durKeyMap.put("wt", Duration.WHOLE_TRIPLET_NOTE * 2d);
        durKeyMap.put("w", Duration.WHOLE_NOTE * 2d);
        durKeyMap.put("ht", Duration.HALF_TRIPLET_NOTE * 2d);
        durKeyMap.put("h", Duration.HALF_NOTE * 2d);
        durKeyMap.put("qt", Duration.QUARTER_TRIPLET_NOTE * 2d);
        durKeyMap.put("q", Duration.QUARTER_NOTE * 2d);
        durKeyMap.put("et", Duration.EIGHTH_TRIPLET_NOTE * 2d);
        durKeyMap.put("e", Duration.EIGHTH_NOTE * 2d);
        durKeyMap.put("st", Duration.SIXTEENTH_TRIPLET_NOTE * 2d);
        durKeyMap.put("s", Duration.SIXTEENTH_NOTE * 2d);
        durKeyMap.put("tt", Duration.THIRTY_SECOND_TRIPLET_NOTE * 2d);
        durKeyMap.put("t", Duration.THIRTY_SECOND_NOTE * 2d);
        durKeyMap.put("xt", Duration.SIXTY_FOURTH_TRIPLET_NOTE * 2d);
        durKeyMap.put("x", Duration.SIXTY_FOURTH_NOTE * 2d);
        durKeyMap.put("ot", Duration.ONE_TWENTY_EIGHTH_TRIPLET_NOTE * 2d);
        durKeyMap.put("o", Duration.ONE_TWENTY_EIGHTH_NOTE * 2d);
        DurationParser.setDurKeyMap(durKeyMap);

        double actual = DurationParser.getDuration("q");
        logger.debug("actual: {}", actual);
        double expected = 2d;
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

        actual = DurationParser.getDuration("e");
        logger.debug("actual: {}", actual);
        expected = 1d;
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));
        
        DurationParser.useDefaultDurationMap();
    }
    /**
     * Test method for
     * {@link com.rockhoppertech.music.DurationParser#multiplyDurationMapByFactor(double)}
     * .
     */
    @Test
    public void testMultiplyDurationMapByFactor() {
       
        DurationParser.multiplyDurationMapByFactor(2d);

        double actual = DurationParser.getDuration("q");
        logger.debug("actual: {}", actual);
        double expected = 2d;
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

        actual = DurationParser.getDuration("e");
        logger.debug("actual: {}", actual);
        expected = 1d;
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));
        
        DurationParser.useDefaultDurationMap();
    }

}
