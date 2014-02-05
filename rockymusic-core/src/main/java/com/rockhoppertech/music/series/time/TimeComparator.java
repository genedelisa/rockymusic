package com.rockhoppertech.music.series.time;

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


import java.io.Serializable;
import java.util.Comparator;

/**
 * Sorts on start.
 * 
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * @since 1.0
 * @see Serializable
 */

public class TimeComparator implements Comparator<TimeEvent> {

    /*
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(TimeEvent o1, TimeEvent o2) {
        double t1 = o1.getStartBeat();
        double t2 = o2.getStartBeat();
        if (t1 < t2) {
            return -1;
        } else if (t1 == t2) {
            return 0;
        } else if (t1 > t2) {
            return 1;
        }
        return 0;
    }
}
