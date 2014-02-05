package com.rockhoppertech.music.series.time;

import java.util.EventObject;

/**
 * A JavaBean event. Fired by TimeSeries.
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * 
 */
public class TimeSeriesEvent extends EventObject {
    /**
     * 
     */
    private static final long serialVersionUID = -668174140343479401L;

    /**
     * Constructs a TimeSeriesEvent object.
     * 
     * @param source
     *            the Object that is the source of the event (typically
     *            <code>this</code>)
     */
    public TimeSeriesEvent(Object source) {
        super(source);
    }
}
