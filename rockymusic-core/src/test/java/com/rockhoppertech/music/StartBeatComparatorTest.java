/**
 * 
 */
package com.rockhoppertech.music;

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
