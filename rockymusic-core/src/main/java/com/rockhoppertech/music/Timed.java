package com.rockhoppertech.music;

/*
 * #%L
 * Rocky Music Core
 * %%
 * Copyright (C) 1996 - 2013 Rockhopper Technologies
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

/**
 * A class with a start time and a duration.
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * @see Note
 * @see MIDINote
 * @see TimeEvent
 */
public interface Timed {

    /**
     * @return the start beat
     */
    double getStartBeat();

    /**
     * @param d
     *            a start beat
     */
    void setStartBeat(double d);

    /**
     * @return the duration
     */
    double getDuration();

    /**
     * @return the end best
     */
    double getEndBeat();

    /**
     * @param d
     *            the new duration
     */
    void setDuration(double d);

    /**
     * Clone is evil.
     * 
     * @return a new {@code Timed} instance
     */
    Timed duplicate();

}
