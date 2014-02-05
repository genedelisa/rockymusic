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
