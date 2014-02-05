package com.rockhoppertech.music.series.time;

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
