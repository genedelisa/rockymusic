package com.rockhoppertech.music.modifiers;

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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Pitch;
import com.rockhoppertech.music.midi.js.MIDINote;
import com.rockhoppertech.music.midi.js.MIDITrack;
import com.rockhoppertech.music.midi.js.MIDITrackBuilder;
import com.rockhoppertech.music.modifiers.Modifier.Operation;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class AbstractMIDINoteModifierTest {
    private static final Logger logger = LoggerFactory
            .getLogger(AbstractMIDINoteModifierTest.class);

    @Test
    public void testAbstractMIDINoteModifier() {

        AbstractMIDINoteModifier mod = new AbstractMIDINoteModifier() {
            @Override
            public void modify(MIDINote n) {
            }
        };
        assertThat("mod is not null",
                mod, notNullValue());

    }

    @Test
    public void testAbstractMIDINoteModifierOperationNumberArray() {

        AbstractMIDINoteModifier mod = new AbstractMIDINoteModifier(
                Modifier.Operation.SET, 1, 2, 3) {
            @Override
            public void modify(MIDINote n) {
            }
        };
        assertThat("mod is not null",
                mod, notNullValue());

        Number[] na = { 1, 2, 3 };
        mod = new AbstractMIDINoteModifier(
                Modifier.Operation.SET, na) {
            @Override
            public void modify(MIDINote n) {
            }
        };
        assertThat("mod is not null",
                mod, notNullValue());
    }

    @Test
    public void testAbstractMIDINoteModifierNumberArray() {
        AbstractMIDINoteModifier mod = new AbstractMIDINoteModifier(
                1, 2, 3) {
            @Override
            public void modify(MIDINote n) {
            }
        };
        assertThat("mod is not null",
                mod, notNullValue());
        Number[] na = { 1 };
        mod = new AbstractMIDINoteModifier(na) {
            @Override
            public void modify(MIDINote n) {
                Number value = values.next();
                n.setMidiNumber(n.getMidiNumber() + value.intValue());
            }
        };
        assertThat("mod is not null",
                mod, notNullValue());
        MIDITrack track = MIDITrackBuilder.create()
                .noteString("C D E F")
                .build();
        logger.debug("track {}", track);
        track.map(mod);
        logger.debug("modified track {}", track);
        MIDINote note = track.get(0);
        assertThat("note is not null",
                note, notNullValue());
        int actual = note.getMidiNumber();
        int expected = Pitch.CS5;
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

        note = track.get(1);
        assertThat("note is not null",
                note, notNullValue());
        actual = note.getMidiNumber();
        expected = Pitch.DS5;
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

        note = track.get(2);
        assertThat("note is not null",
                note, notNullValue());
        actual = note.getMidiNumber();
        expected = Pitch.F5;
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

        note = track.get(3);
        assertThat("note is not null",
                note, notNullValue());
        actual = note.getMidiNumber();
        expected = Pitch.FS5;
        assertThat("the value is correct",
                actual, is(equalTo(expected)));

    }

    @Test
    public void testAbstractMIDINoteModifierOperationListOfNumber() {
        List<Number> list = new ArrayList<Number>();
        list.add(1);
        list.add(2);
        list.add(3);
        AbstractMIDINoteModifier mod = new AbstractMIDINoteModifier(
                list) {
            @Override
            public void modify(MIDINote n) {
            }
        };
        assertThat("mod is not null",
                mod,
                notNullValue());
    }

    @Test
    public void testAbstractMIDINoteModifierListOfNumber() {
        List<Number> list = new ArrayList<Number>();
        list.add(1);
        list.add(2);
        list.add(3);
        AbstractMIDINoteModifier mod = new AbstractMIDINoteModifier(
                Modifier.Operation.SET, list) {
            @Override
            public void modify(MIDINote n) {
            }
        };
        assertThat("mod is not null",
                mod,
                notNullValue());
    }

    @Test
    public void testGetDescription() {
        AbstractMIDINoteModifier mod = new AbstractMIDINoteModifier() {
            @Override
            public void modify(MIDINote n) {
            }

        };
        assertThat("mod is not null",
                mod,
                notNullValue());
        assertThat("description is correct",
                mod.getDescription(),
                equalTo("No description"));

        mod = new AbstractMIDINoteModifier() {
            @Override
            public void modify(MIDINote n) {
            }

            @Override
            public String getDescription() {
                return "test modifier";
            }
        };
        assertThat("mod is not null",
                mod,
                notNullValue());
        assertThat("description is correct",
                mod.getDescription(),
                equalTo("test modifier"));

    }

    @Test
    public void testGetName() {
        AbstractMIDINoteModifier mod = new AbstractMIDINoteModifier() {
            @Override
            public void modify(MIDINote n) {
            }
        };
        assertThat("mod is not null",
                mod,
                notNullValue());
        String name = mod.getName();
        assertThat("name is correct",
                name,
                equalTo("No name"));
        mod = new AbstractMIDINoteModifier() {
            @Override
            public void modify(MIDINote n) {
            }

            @Override
            public String getName() {
                return "test modifier";
            }
        };
        assertThat("mod is not null",
                mod,
                notNullValue());
        name = mod.getName();
        assertThat("name is correct",
                name,
                equalTo("test modifier"));
    }

    @Test
    public void testSetSuccessor() {
        AbstractMIDINoteModifier mod = new AbstractMIDINoteModifier() {
            @Override
            public void modify(MIDINote n) {
            }
        };
        assertThat("mod is not null",
                mod,
                notNullValue());
        // TODO
    }

    @Test
    public void testSetOperation() {
        AbstractMIDINoteModifier mod = new AbstractMIDINoteModifier() {
            @Override
            public void modify(MIDINote n) {
            }
        };
        assertThat("mod is not null",
                mod,
                notNullValue());
        mod.setOperation(Modifier.Operation.SET);
    }

    @Test
    public void testQuantize() {
        AbstractMIDINoteModifier mod = new AbstractMIDINoteModifier() {
            @Override
            public void modify(MIDINote n) {
            }
        };
        assertThat("mod is not null",
                mod,
                notNullValue());

        double result = AbstractMIDINoteModifier.quantize(1d, .5);
        double expected = 1d;
        assertThat("1 .5",
                result,
                equalTo(expected));

        result = AbstractMIDINoteModifier.quantize(1.4, .5);
        expected = 1.0;
        assertThat("1.4 .5",
                result,
                equalTo(expected));

        result = AbstractMIDINoteModifier.quantize(1.6, .5);
        expected = 1.5;
        assertThat("1.6 .5",
                result,
                equalTo(expected));

        result = AbstractMIDINoteModifier.quantize(2d, .5);
        expected = 2d;
        assertThat("2 .5",
                result,
                equalTo(expected));
        result = AbstractMIDINoteModifier.quantize(2.2, .5);
        expected = 2d;
        assertThat("2.2 .5",
                result,
                equalTo(expected));

        result = AbstractMIDINoteModifier.quantize(.5, .5);
        expected = .5;
        assertThat("= .5",
                result,
                equalTo(expected));

        result = AbstractMIDINoteModifier.quantize(.25, .5);
        expected = .5;
        assertThat("= .25",
                result, equalTo(expected));

        result = AbstractMIDINoteModifier.quantize(1d, 1d);
        expected = 1d;
        assertThat("= 1",
                result, equalTo(expected));

        // TODO more values
    }

    @Test
    public void testGetValues() {
        AbstractMIDINoteModifier mod = new AbstractMIDINoteModifier() {
            @Override
            public void modify(MIDINote n) {
            }
        };
        assertThat("mod is not null",
                mod,
                notNullValue());

        List<Number> values = mod.getValues();
        assertThat("values is not null",
                values,
                notNullValue());
    }

    @Test
    public void testSetValuesListOfNumber() {
        AbstractMIDINoteModifier mod = new AbstractMIDINoteModifier() {
            @Override
            public void modify(MIDINote n) {
            }
        };
        assertThat("mod is not null",
                mod,
                notNullValue());
        List<Number> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        mod.setValues(list);

        List<Number> values = mod.getValues();
        assertThat("values is not null",
                values,
                notNullValue());
    }

    @Test
    public void testSetValuesDoubleArray() {
        AbstractMIDINoteModifier mod = new AbstractMIDINoteModifier() {
            @Override
            public void modify(MIDINote n) {
                final int value = values.next().intValue();
                n.setMidiNumber(n.getMidiNumber() + value);
            }
        };
        assertThat("mod is not null",
                mod,
                notNullValue());

        // here's the test
        double[] array = new double[] { 1d, 3d };
        mod.setValues(array);

        MIDITrack track = MIDITrackBuilder.create()
                .noteString("C D E")
                .sequential()
                .build();
        track.map(mod);

        MIDINote note = track.get(0);
        int expected = Pitch.CS5;
        assertThat("pitch is c#5",
                note.getMidiNumber(),
                equalTo(expected));

        note = track.get(1);
        expected = Pitch.F5;
        assertThat("pitch is F5",
                note.getMidiNumber(),
                equalTo(expected));

        note = track.get(2);
        expected = Pitch.F5;
        assertThat("pitch is F5",
                note.getMidiNumber(),
                equalTo(expected));

    }

    @Test
    public void testGetOperation() {
        AbstractMIDINoteModifier mod = new AbstractMIDINoteModifier() {
            @Override
            public void modify(MIDINote n) {
            }

        };
        assertThat("mod is not null",
                mod,
                notNullValue());
        Operation operation = mod.getOperation();
        Operation expected = Operation.SET;
        assertThat("operation is SET",
                operation,
                equalTo(expected));

    }

    @Test
    public void testGetSuccessor() {
        AbstractMIDINoteModifier mod = new AbstractMIDINoteModifier() {
            @Override
            public void modify(MIDINote n) {
            }

            @Override
            public String getDescription() {
                return "test modifier";
            }
        };
        assertThat("mod is not null",
                mod,
                notNullValue());
        MIDINoteModifier suc = mod.getSuccessor();
        assertThat("succesor is  null",
                suc,
                nullValue());
    }

}
