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

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class PatternFactoryTest {
    private static final Logger logger = LoggerFactory
            .getLogger(PatternFactoryTest.class);

    @Test
    public void testGetPatternsIntIntBoolean() {
        List<int[]> pattern = PatternFactory.getPatterns(3, 2, true);
        logger.debug("pattern {}", pattern);

        assertThat("pattern is not null",
                pattern, is(notNullValue()));
        assertThat("the pattern size is correct",
                pattern.size(), is(equalTo(6)));
        for (int i = 0; i < 6; i++) {
            logger.debug("array {}", Arrays.toString(pattern.get(i)));
        }

        int[] array = pattern.get(0);
        logger.debug("array {}", Arrays.toString(array));
        assertThat("array is not null",
                array, is(notNullValue()));
        assertThat("the array size is correct",
                array.length, is(equalTo(2)));
        assertThat("the array index 0 is correct",
                array[0], is(equalTo(0)));
        assertThat("the array index 1 is correct",
                array[1], is(equalTo(1)));

        array = pattern.get(1);
        logger.debug("array {}", Arrays.toString(array));
        assertThat("array is not null",
                array, is(notNullValue()));
        assertThat("the array size is correct",
                array.length, is(equalTo(2)));
        assertThat("the array index 0 is correct",
                array[0], is(equalTo(0)));
        assertThat("the array index 1 is correct",
                array[1], is(equalTo(2)));

        array = pattern.get(2);
        logger.debug("array {}", Arrays.toString(array));
        assertThat("array is not null",
                array, is(notNullValue()));
        assertThat("the array size is correct",
                array.length, is(equalTo(2)));
        assertThat("the array index 0 is correct",
                array[0], is(equalTo(1)));
        assertThat("the array index 1 is correct",
                array[1], is(equalTo(0)));

        array = pattern.get(3);
        logger.debug("array {}", Arrays.toString(array));
        assertThat("array is not null",
                array, is(notNullValue()));
        assertThat("the array size is correct",
                array.length, is(equalTo(2)));
        assertThat("the array index 0 is correct",
                array[0], is(equalTo(1)));
        assertThat("the array index 1 is correct",
                array[1], is(equalTo(2)));

        array = pattern.get(4);
        logger.debug("array {}", Arrays.toString(array));
        assertThat("array is not null",
                array, is(notNullValue()));
        assertThat("the array size is correct",
                array.length, is(equalTo(2)));
        assertThat("the array index 0 is correct",
                array[0], is(equalTo(2)));
        assertThat("the array index 1 is correct",
                array[1], is(equalTo(0)));

        array = pattern.get(5);
        logger.debug("array {}", Arrays.toString(array));
        assertThat("array is not null",
                array, is(notNullValue()));
        assertThat("the array size is correct",
                array.length, is(equalTo(2)));
        assertThat("the array index 0 is correct",
                array[0], is(equalTo(2)));
        assertThat("the array index 1 is correct",
                array[1], is(equalTo(1)));
    }

    @Test
    public void testGetPatternsIntInt() {
        // will have repeats
        List<int[]> pattern = PatternFactory.getPatterns(3, 2);
        logger.debug("pattern {}", pattern);

        assertThat("pattern is not null",
                pattern, is(notNullValue()));
        assertThat("the pattern size is correct",
                pattern.size(), is(equalTo(9)));
        for (int i = 0; i < 9; i++) {
            logger.debug("array {}", Arrays.toString(pattern.get(i)));
        }

        int[] array = pattern.get(0);
        logger.debug("array {}", Arrays.toString(array));
        assertThat("array is not null",
                array, is(notNullValue()));
        assertThat("the array size is correct",
                array.length, is(equalTo(2)));
        assertThat("the array index 0 is correct",
                array[0], is(equalTo(0)));
        assertThat("the array index 1 is correct",
                array[1], is(equalTo(0)));

        array = pattern.get(1);
        logger.debug("array {}", Arrays.toString(array));
        assertThat("array is not null",
                array, is(notNullValue()));
        assertThat("the array size is correct",
                array.length, is(equalTo(2)));
        assertThat("the array index 0 is correct",
                array[0], is(equalTo(0)));
        assertThat("the array index 1 is correct",
                array[1], is(equalTo(1)));

        array = pattern.get(2);
        logger.debug("array {}", Arrays.toString(array));
        assertThat("array is not null",
                array, is(notNullValue()));
        assertThat("the array size is correct",
                array.length, is(equalTo(2)));
        assertThat("the array index 0 is correct",
                array[0], is(equalTo(0)));
        assertThat("the array index 1 is correct",
                array[1], is(equalTo(2)));

        array = pattern.get(3);
        logger.debug("array {}", Arrays.toString(array));
        assertThat("array is not null",
                array, is(notNullValue()));
        assertThat("the array size is correct",
                array.length, is(equalTo(2)));
        assertThat("the array index 0 is correct",
                array[0], is(equalTo(1)));
        assertThat("the array index 1 is correct",
                array[1], is(equalTo(0)));

        array = pattern.get(4);
        logger.debug("array {}", Arrays.toString(array));
        assertThat("array is not null",
                array, is(notNullValue()));
        assertThat("the array size is correct",
                array.length, is(equalTo(2)));
        assertThat("the array index 0 is correct",
                array[0], is(equalTo(1)));
        assertThat("the array index 1 is correct",
                array[1], is(equalTo(1)));

        array = pattern.get(5);
        logger.debug("array {}", Arrays.toString(array));
        assertThat("array is not null",
                array, is(notNullValue()));
        assertThat("the array size is correct",
                array.length, is(equalTo(2)));
        assertThat("the array index 0 is correct",
                array[0], is(equalTo(1)));
        assertThat("the array index 1 is correct",
                array[1], is(equalTo(2)));
    }

    @Test
    public void testHasRepeats() {
        // will have repeats
        List<int[]> pattern = PatternFactory.getPatterns(3, 2);
        logger.debug("pattern {}", pattern);

        assertThat("pattern is not null",
                pattern, is(notNullValue()));
        assertThat("the pattern size is correct",
                pattern.size(), is(equalTo(9)));
        for (int i = 0; i < 9; i++) {
            logger.debug("array {}", Arrays.toString(pattern.get(i)));
        }

        int[] array = pattern.get(0);
        logger.debug("array {}", Arrays.toString(array));
        assertThat("array is not null",
                array, is(notNullValue()));
        assertThat("the array size is correct",
                array.length, is(equalTo(2)));
        assertThat("the array index 0 is correct",
                array[0], is(equalTo(0)));
        assertThat("the array index 1 is correct",
                array[1], is(equalTo(0)));
        assertThat("the array has repeats",
                PatternFactory.hasRepeats(array), is(equalTo(true)));
        
        
    }

}
