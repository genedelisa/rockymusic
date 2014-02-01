package com.rockhoppertech.music.examples;

/*
 * #%L
 * Rocky Music Examples
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

import com.rockhoppertech.music.Duration;
import static com.rockhoppertech.music.Duration.*;

public class DurationExample {
    public static void main(final String[] args) {
        // for (double d = Duration.WHOLE_NOTE; d > Duration.THIRTY_SECOND_NOTE;
        // d -= Duration.SIXTEENTH_NOTE) {
        // String s = Duration.getDurationString(d);
        // System.out.println(String.format("%f: %s",
        // d,
        // s));
        // printScannedDuration(s);
        // System.out.println();
        // }

        for (double d = Duration.MIN; d < Duration.MAX; d += Duration.MIN) {
            String s = Duration.getDurationString(d);
            System.out.println(String.format("%f: %s",
                    d,
                    s));
            System.out.println();
        }

        double x = 1;

        if (x == Duration.getDoubleDotted(DOUBLE_WHOLE_NOTE)) {

        }
        if (x == Duration.getDotted(DOUBLE_WHOLE_NOTE)) {

        }
        if (x == Duration.DOUBLE_WHOLE_NOTE) {

        }

        // but with enums you can do this
        Dur dur = Dur.DOUBLE_WHOLE_NOTE_DD;
        switch (dur) {
        case DOUBLE_WHOLE_NOTE_DD:
            break;
        case DOUBLE_WHOLE_NOTE_D:
            break;
        case DOUBLE_WHOLE_NOTE:
            break;
        case EIGHTH_NOTE:
            break;
        case EIGHTH_NOTE_D:
            break;
        case EIGHTH_NOTE_DD:
            break;
        case HALF_NOTE:
            break;
        case HALF_NOTE_D:
            break;
        case HALF_NOTE_DD:
            break;
        case ONE_TWENTY_EIGHTH_NOTE:
            break;
        case ONE_TWENTY_EIGHTH_NOTE_D:
            break;
        case ONE_TWENTY_EIGHTH_NOTE_DD:
            break;
        case QUARTER_NOTE:
            break;
        case QUARTER_NOTE_D:
            break;
        case QUARTER_NOTE_DD:
            break;
        case SIXTEENTH_NOTE:
            break;
        case SIXTEENTH_NOTE_D:
            break;
        case SIXTEENTH_NOTE_DD:
            break;
        case SIXTY_FOURTH_NOTE:
            break;
        case SIXTY_FOURTH_NOTE_D:
            break;
        case SIXTY_FOURTH_NOTE_DD:
            break;
        case THIRTY_SECOND_NOTE:
            break;
        case THIRTY_SECOND_NOTE_D:
            break;
        case THIRTY_SECOND_NOTE_DD:
            break;
        case WHOLE_NOTE:
            break;
        case WHOLE_NOTE_D:
            break;
        case WHOLE_NOTE_DD:
            break;
        default:
            break;
        }

        //
        // String s = Duration.getRestString(8);
        // System.out.println(s);
        // Duration.printScanned(s);
        //
        // s = Duration.getRestString(7.5);
        // System.out.println(s);
        // Duration.printScanned(s);
        //
        // s = Duration.getRestString(5);
        // System.out.println(s);
        // Duration.printScanned(s);
        //
        // s = Duration.getRestString(3);
        // System.out.println(s);
        // Duration.printScanned(s);
        //
        // s = Duration.getRestString(.5);
        // System.out.println(s);
        // Duration.printScanned(s);
        //
        // s = Duration.getRestString(.25);
        // System.out.println(s);
        // Duration.printScanned(s);
        System.err.println(durations);
    }
}
