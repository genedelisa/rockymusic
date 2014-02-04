/**
 * 
 */
package com.rockhoppertech.music.scale;

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
public class ScaleComparatorTest {

    private static final Logger logger = LoggerFactory
            .getLogger(ScaleComparatorTest.class);

    @Test
    public void same() {
        Scale scale = ScaleFactory.createFromName("Major");
        assertThat("scale is not null",
                scale, is(notNullValue()));
        Scale scale2 = ScaleFactory.createFromName("Major");

        ScaleComparator comparator = new ScaleComparator();
        int actual = comparator.compare(scale, scale2);
        int expected = 0;
        assertThat("comparison is correct",
                actual, is(equalTo(expected)));

    }

    @Test
    public void name() {
        Scale scale = ScaleFactory.createFromName("Major");
        assertThat("scale is not null",
                scale, is(notNullValue()));
        Scale scale2 = ScaleFactory.createFromName("Melodic Minor");

        // compare by specific property
        // also length and intervals
        ScaleComparator comparator = new ScaleComparator("name", true);
        int actual = comparator.compare(scale, scale2);
        int expected = 0;
        assertThat("comparison is correct",
                actual, is(lessThan(expected)));

        scale2 = ScaleFactory.createFromName("Chromatic");
        comparator = new ScaleComparator("name", true);
        actual = comparator.compare(scale, scale2);
        expected = 0;
        assertThat("comparison is correct",
                actual, is(greaterThan(expected)));
        
        scale2 = ScaleFactory.createFromName("Pelog");
        comparator = new ScaleComparator("length", true);
        actual = comparator.compare(scale, scale2);
        expected = 0;
        assertThat("comparison is correct",
                actual, is(greaterThan(expected)));
        
        scale2 = ScaleFactory.createFromName("Pelog");
        comparator = new ScaleComparator("intervals", true);
        actual = comparator.compare(scale, scale2);
        expected = 0;
        assertThat("comparison is correct",
                actual, is(greaterThan(expected)));

    }

}
