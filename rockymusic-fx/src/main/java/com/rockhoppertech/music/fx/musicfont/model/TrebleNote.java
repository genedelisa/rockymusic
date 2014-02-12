package com.rockhoppertech.music.fx.musicfont.model;

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


import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rockhoppertech.music.Pitch;


/**
 * @author <a href="http://genedelisa.com/">Gene De Lisa</a>
 *
 */
public class TrebleNote {
    final static Logger logger = LoggerFactory.getLogger(TrebleNote.class);

    public static void main(String[] args) {
        for (int i = 0; i < 128; i++)
            System.out.println("note " + i + " ledgers "
                    + TrebleNote.trebleLedgersFlat[i]);
    }

    private static int pat[] = { 3, 4, 3, 4, 3 }; // sharps
    private static int pat2[] = { 4, 3, 3, 4, 3 }; // flats
    private static int pat3[] = { 3, 4, 3, 3, 4 };
    public static final int[] trebleLedgersFlat = new int[128];
    public static final int[] trebleLedgersSharp = new int[128];
    static {
        for (int i = 0; i < 128; i++)
            trebleLedgersFlat[i] = trebleledgers(i, false);
        for (int i = 0; i < 128; i++)
            trebleLedgersSharp[i] = trebleledgers(i, true);
    }

    final Map<Integer, Integer> flatLedgersMap = new HashMap<Integer, Integer>();

    public TrebleNote() {
        flatLedgersMap.put(80, 1);
        flatLedgersMap.put(81, 1);
        flatLedgersMap.put(82, 1);
        flatLedgersMap.put(83, 1);
        flatLedgersMap.put(84, 2);
        flatLedgersMap.put(85, 2);
        flatLedgersMap.put(86, 2);
        flatLedgersMap.put(87, 3);
        flatLedgersMap.put(88, 3);
        flatLedgersMap.put(89, 3);
        flatLedgersMap.put(90, 4);
        flatLedgersMap.put(91, 4);
        flatLedgersMap.put(92, 4);
        flatLedgersMap.put(93, 4);
        flatLedgersMap.put(94, 5); // bf7
        flatLedgersMap.put(95, 5);
        flatLedgersMap.put(96, 1);
        flatLedgersMap.put(97, 1);
        flatLedgersMap.put(98, 1);
        flatLedgersMap.put(99, 1);
        flatLedgersMap.put(100, 1);
        flatLedgersMap.put(101, 1);
        flatLedgersMap.put(102, 1);
        flatLedgersMap.put(103, 1);
        flatLedgersMap.put(104, 1);
        flatLedgersMap.put(105, 1);
        flatLedgersMap.put(106, 1);
        flatLedgersMap.put(107, 1);
        flatLedgersMap.put(108, 1);
        flatLedgersMap.put(109, 1);
        flatLedgersMap.put(110, 1);
        flatLedgersMap.put(111, 1);
        flatLedgersMap.put(112, 1);
        flatLedgersMap.put(113, 1);
        flatLedgersMap.put(114, 1);
        flatLedgersMap.put(115, 1);
        flatLedgersMap.put(116, 1);
        flatLedgersMap.put(117, 1);
        flatLedgersMap.put(118, 1);
        flatLedgersMap.put(119, 1);
        flatLedgersMap.put(120, 1);
        flatLedgersMap.put(121, 1);
    }

    static int trebleledgers(int num, boolean sharps) {
        int ledgers;

        if (num >= Pitch.G6)
            ledgers = aboveTrebleLedgers(num, sharps);
        else if (num <= Pitch.D5)
            ledgers = belowTrebleLedgers(num, sharps);
        else
            return (0);
        return (ledgers);
    }

    static int aboveTrebleLedgers(int num, boolean sharps) {
        int j, k, n;
        int[] trans = new int[70]; // transition notes where a new line is added
        int ledgers = 0;
        int gs = 80; // g sharp
        int g = 79; // g natural

        if (sharps) {
            trans[0] = gs;
            for (k = 1, j = gs; j < 127; j++, k++) {
                trans[k] = trans[k - 1] + pat[(k - 1) % 5];
            }
            for (j = gs; j < num; j++) {
                for (n = 0; n < k; n++)
                    if (j == trans[n]) {
                        ledgers++;
                    }
            }
        } else {
            trans[0] = g;
            for (k = 1, j = g; j < 127; j++, k++) {
                trans[k] = trans[k - 1] + pat2[(k - 1) % 5];
            }
            for (j = g; j < num; j++) {
                for (n = 0; n < k; n++)
                    if (j == trans[n]) {
                        ledgers++;
                    }
            }
        }
        return (ledgers);
    }

    static int belowTrebleLedgers(int num, boolean sharps) {
        int j, k, n;
        int[] trans = new int[70];
        int ledgers = 0;
        int d = 62;
        int df = 61;

        if (sharps) {
            trans[0] = d;
            for (k = 1, j = d; j > num; j--, k++) {
                trans[k] = trans[k - 1] - pat[(k - 1) % 5];
            }

            for (j = d; j > num; j--) {
                for (n = 0; n < k; n++)
                    if (j == trans[n]) {
                        ledgers++;
                    }
            }
        } else {
            trans[0] = df;
            for (k = 1, j = df; j > num; j--, k++) {
                trans[k] = trans[k - 1] - pat3[(k - 1) % 5];
            }

            for (j = d; j > num; j--) {
                for (n = 0; n < k; n++)
                    if (j == trans[n]) {
                        ledgers++;
                    }
            }
        }
        return (ledgers);
    }
}
