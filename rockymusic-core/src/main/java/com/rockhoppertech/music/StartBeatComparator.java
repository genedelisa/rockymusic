package com.rockhoppertech.music;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Class <code>BeatComparator</code> compares start beats.
 * 
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * @since 1.0
 * @see Comparator
 */
class StartBeatComparator implements Comparator<Note>, Serializable {
    /**
     * Serialization.
     */
    private static final long serialVersionUID = 1701446492454684836L;

    @Override
    public int compare(final Note o, final Note o2) {
        final double t1 = o.getStartBeat();
        final double t2 = o2.getStartBeat();
        if (t1 < t2) {
            return -1;
        } else if (Math.abs(t1 - t2) < .0000001) {
            return 0;
        } else if (t1 > t2) {
            return 1;
        }
        return 0;
    }
}
