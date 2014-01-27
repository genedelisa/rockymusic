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

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 *
 */
public class NoteTest {
    private static final Logger logger = LoggerFactory
            .getLogger(NoteTest.class);

    //  private Logger logger = LoggerFactory.getLogger(NoteTest.class);

    @Test
    public void defaultCtor() {
        Note note = new Note();
        assertNotNull(note);
        Pitch pitch = PitchFactory.getPitch(Pitch.C5);
        assertEquals(pitch, note.getPitch());
    }

    @Test
    public void overlapping() {
        Pitch pitch = PitchFactory.getPitch(Pitch.C5);
        Note n1 = new Note(pitch, 1.0, 2.0);
        Note n2 = new Note(pitch, 1.0, 2.0);
        assertNotNull(n1);
        assertNotNull(n2);
       // assertTrue(n1.isOverlapping(n2));

        n2 = new Note(pitch, 2.0, 1.0);
       // assertTrue(n1.isOverlapping(n2));

        // the ranges are inclusive
        n2 = new Note(pitch, 3.0001, 1.0);
      //  assertFalse(n1.isOverlapping(n2));

    }

}
