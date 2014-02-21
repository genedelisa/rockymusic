package com.rockhoppertech.music.fx.cmn.model;

/*
 * #%L
 * Rocky Music FX
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

import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.midi.js.KeySignature;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class MeasureSymbolManagerTest {

    @Test
    public final void testIsTreble() {

    }

    @Test
    public final void testIsSpellingFlat() {

    }

    @Test
    public final void testIsSpellingSharp() {

    }

    @Test
    public final void testShouldDrawStem() {

    }

    @Test
    public final void testGetXofBeatN() {

    }

    @Test
    public final void testGetNAttacks() {

    }

    @Test
    public final void testIsAccidental() {

        KeySignature ks = KeySignature.CMAJOR;
        assertThat("key sig not null", ks, notNullValue());

        assertThat("note is correct",
                MeasureSymbolManager.isAccidental(ks, Pitch.C5),
                equalTo(true));
        assertThat("note is correct",
                MeasureSymbolManager.isAccidental(ks, Pitch.CS5),
                equalTo(false));
        assertThat("note is correct",
                MeasureSymbolManager.isAccidental(ks, Pitch.D5),
                equalTo(true));
        assertThat("note is correct",
                MeasureSymbolManager.isAccidental(ks, Pitch.EF5),
                equalTo(false));
        assertThat("note is correct",
                MeasureSymbolManager.isAccidental(ks, Pitch.E5),
                equalTo(true));
        assertThat("note is correct",
                MeasureSymbolManager.isAccidental(ks, Pitch.F5),
                equalTo(true));
        assertThat("note is correct",
                MeasureSymbolManager.isAccidental(ks, Pitch.FS5),
                equalTo(false));
        assertThat("note is correct",
                MeasureSymbolManager.isAccidental(ks, Pitch.G5),
                equalTo(true));
        assertThat("note is correct",
                MeasureSymbolManager.isAccidental(ks, Pitch.GS5),
                equalTo(false));
        assertThat("note is correct",
                MeasureSymbolManager.isAccidental(ks, Pitch.A5),
                equalTo(true));
        assertThat("note is correct",
                MeasureSymbolManager.isAccidental(ks, Pitch.AS5),
                equalTo(false));
        assertThat("note is correct",
                MeasureSymbolManager.isAccidental(ks, Pitch.B5),
                equalTo(true));
        
        ks = KeySignature.DMAJOR;
        assertThat("key sig not null", ks, notNullValue());

        assertThat("note is correct",
                MeasureSymbolManager.isAccidental(ks, Pitch.C5),
                equalTo(false));
        assertThat("note is correct",
                MeasureSymbolManager.isAccidental(ks, Pitch.CS5),
                equalTo(true));
        assertThat("note is correct",
                MeasureSymbolManager.isAccidental(ks, Pitch.D5),
                equalTo(true));
        assertThat("note is correct",
                MeasureSymbolManager.isAccidental(ks, Pitch.EF5),
                equalTo(false));
        assertThat("note is correct",
                MeasureSymbolManager.isAccidental(ks, Pitch.E5),
                equalTo(true));
        assertThat("note is correct",
                MeasureSymbolManager.isAccidental(ks, Pitch.F5),
                equalTo(false));
        assertThat("note is correct",
                MeasureSymbolManager.isAccidental(ks, Pitch.FS5),
                equalTo(true));
        assertThat("note is correct",
                MeasureSymbolManager.isAccidental(ks, Pitch.G5),
                equalTo(true));
        assertThat("note is correct",
                MeasureSymbolManager.isAccidental(ks, Pitch.GS5),
                equalTo(false));
        assertThat("note is correct",
                MeasureSymbolManager.isAccidental(ks, Pitch.A5),
                equalTo(true));
        assertThat("note is correct",
                MeasureSymbolManager.isAccidental(ks, Pitch.AS5),
                equalTo(false));
        assertThat("note is correct",
                MeasureSymbolManager.isAccidental(ks, Pitch.B5),
                equalTo(true));

    }

    @Test
    public final void testGetShapes() {

    }

    @Test
    public final void testAddTimeSignature() {

    }

}
