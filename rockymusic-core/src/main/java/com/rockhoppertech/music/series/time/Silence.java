package com.rockhoppertech.music.series.time;

import java.io.Serializable;

/**
 * Class <code>Silence</code> is a {@code TimeEvent}.
 * 
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * @since 1.0
 * @see Serializable
 */

public class Silence extends TimeEvent {
    /**
     * Serialization.
     */
    private static final long serialVersionUID = -7720242349717723947L;

    public Silence(double t, double d) {
        super(t, d);
    }
}
