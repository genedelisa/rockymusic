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

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
//import static org.junit.Assert.*;

import org.junit.Test;

import com.rockhoppertech.music.Pitch;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class MIDINoteTest {

    /**
     * 
     */
    @Test
    public void shouldHaveDefaultPitch() {
        MIDINote note = MIDINoteBuilder.create().build();
        assertThat("The note is not null.", note, notNullValue());
        assertThat("the default pitch is C5", note.getPitch().getMidiNumber(),
                equalTo(Pitch.C5));
    }

    /**
     * 
     */
    @Test
    public void shouldSetPitch() {
        MIDINote note = MIDINoteBuilder.create().pitch(Pitch.C4).build();
        assertThat("The note is not null.", note, notNullValue());
        assertThat("the default pitch is C4", note.getPitch().getMidiNumber(),
                equalTo(Pitch.C4));
    }

    /**
     * 
     */
    @Test
    public void shouldSetPitchStartAndDuration() {
        MIDINote note = MIDINoteBuilder.create()
                .pitch(Pitch.C4)
                .duration(3d)
                .startBeat(1d)
                .build();
        assertThat("The note is not null.", note, notNullValue());
        assertThat("the default pitch is C4", note.getPitch().getMidiNumber(),
                equalTo(Pitch.C4));
        assertThat("the duration is 3", note.getDuration(),
                equalTo(3d));
        assertThat("the start beat is 1", note.getStartBeat(),
                equalTo(1d));
    }

}
