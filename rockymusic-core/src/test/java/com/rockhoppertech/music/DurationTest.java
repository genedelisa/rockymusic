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

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class DurationTest {

    @Test
    public void testGetDottedDouble() {
        double actual = Duration.getDotted(1d);
        // assertThat("pattern is not null",
        // pattern, is(notNullValue()));
        double expected = 1.5;
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

        actual = Duration.getDotted(2d);
        expected = 3d;
        assertThat("the value is correct",
                actual, is(equalTo(expected)));
    }

    @Test
    public void testGetDoubleDotted() {
        double actual = Duration.getDoubleDotted(1d);
        double expected = 1.75;
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

        actual = Duration.getDoubleDotted(2d);
        expected = 3.5;
        assertThat("the value is correct",
                actual, is(equalTo(expected)));
    }

    @Test
    public void testGetDottedDoubleInt() {

        double actual = Duration.getDotted(1d, 1);
        double expected = 1.5;
        assertThat("the  is correct",
                actual, is(equalTo(expected)));

        actual = Duration.getDotted(2d, 1);
        expected = 3d;
        assertThat("the  is correct",
                actual, is(equalTo(expected)));
    }

    @Test
    public void testGetRestString1() {
        String actual = Duration.getRestString1(4);
        String expected = "4,";
        assertThat("the  is correct",
                actual, is(equalTo(expected)));
        
//        actual = Duration.getRestString1(6d);
//        expected = "6.0";
//        assertThat("the duration is correct",
//                actual, is(equalTo(expected)));
    }

    @Test
    public void testGetRestString() {
        String actual = Duration.getRestString(4d);
        String expected = "4.0";
        assertThat("the  is correct",
                actual, is(equalTo(expected)));
        
        actual = Duration.getRestString(6d);
        expected = "6.0";
        assertThat("the duration is correct",
                actual, is(equalTo(expected)));
        
        actual = Duration.getRestString(1.5);
        expected = "1.5";
        assertThat("the duration is correct",
                actual, is(equalTo(expected)));
        
        // 6 is defined, 6.5 is not, so we should
        // get two values.
        actual = Duration.getRestString(6.5);
        expected = "6.0,0.5";
        assertThat("the duration is correct",
                actual, is(equalTo(expected)));
    }

    @Test
    public void testGetDurationStringDouble() {
        String actual = Duration.getDurationString(4d);
        String expected = "4.0";
        assertThat("the  is correct",
                actual, is(equalTo(expected)));
        
        actual = Duration.getDurationString(6.5);
        expected = "6.0,0.5";
        assertThat("the duration is correct",
                actual, is(equalTo(expected)));
    }

    @Test
    public void testGetDurationStringDoubleBoolean() {
        // markers = true
        String actual = Duration.getDurationString(4d, true);
        String expected = "4.0";
        assertThat("the duration is correct",
                actual, is(equalTo(expected)));

        actual = Duration.getDurationString(6d, true);
        expected = "6.0";
        assertThat("the duration is correct",
                actual, is(equalTo(expected)));
        
        actual = Duration.getDurationString(6.5, true);
        expected = "ts=6.0,0.5";
        assertThat("the duration is correct",
                actual, is(equalTo(expected)));
        
        actual = Duration.getDurationString(6.5, false);
        expected = "6.0,0.5";
        assertThat("the duration is correct",
                actual, is(equalTo(expected)));
    }

}
