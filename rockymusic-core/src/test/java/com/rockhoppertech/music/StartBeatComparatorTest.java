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

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class StartBeatComparatorTest {
    private static final Logger logger = LoggerFactory
            .getLogger(StartBeatComparatorTest.class);

    /**
     * Test method for
     * {@link com.rockhoppertech.music.StartBeatComparator#compare(com.rockhoppertech.music.Note, com.rockhoppertech.music.Note)}
     * .
     */
    @Test
    public void testCompare() {
        StartBeatComparator c = new StartBeatComparator();
        Note note1 = new Note(Pitch.C5, 1d, Duration.Q);
        Note note2 = new Note(Pitch.C5, 1d, Duration.Q);
        int actual = c.compare(note1, note2);
        logger.debug("actual: {}", actual);
        int expected = 0;
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

        note1 = new Note(Pitch.C5, 1d, Duration.Q);
        note2 = new Note(Pitch.C5, 2d, Duration.Q);
        actual = c.compare(note1, note2);
        logger.debug("actual: {}", actual);
        expected = -1;
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

        note1 = new Note(Pitch.C5, 2d, Duration.Q);
        note2 = new Note(Pitch.C5, 1d, Duration.Q);
        actual = c.compare(note1, note2);
        logger.debug("actual: {}", actual);
        expected = 1;
        assertThat("is not null",
                actual, is(notNullValue()));
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

    }

}
