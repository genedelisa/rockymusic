package com.rockhoppertech.music.series.time;

import java.io.Serializable;

/**
 * Class <code>Sound</code> is a {@code TimeEvent}.
 * 
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * @since 1.0
 * @see Serializable
 */

public class Sound extends TimeEvent {
    /**
     * Serialization.
     */
    private static final long serialVersionUID = -3935929411305500089L;

    public Sound(double t, double d) {
        super(t, d);
    }
}
