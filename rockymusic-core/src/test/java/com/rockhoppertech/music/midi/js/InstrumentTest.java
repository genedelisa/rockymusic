package com.rockhoppertech.music.midi.js;

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

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

//import static org.junit.Assert.*;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class InstrumentTest {
    /**
     * Logger.
     */
    private static final Logger logger = LoggerFactory
            .getLogger(InstrumentTest.class);

    /**
	 * 
	 */
    @Test
    public void shouldHaveDefaultPitch() {

        List<Instrument> all = Instrument.getAll();

        assertThat("The list is not null.", all, notNullValue());
        // assertThat(
        // "there are 2 notes on the track",
        // track.getNotes().size(),
        // equalTo(2));
    }

}
