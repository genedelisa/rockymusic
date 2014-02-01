package com.rockhoppertech.music;

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
 * Class <code>StartBeatComparator</code> compares the start beats of two
 * {@code Timed} objects.
 * 
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * @since 1.0
 * @see Comparator
 * @see Timed
 */
class StartBeatComparator implements Comparator<Timed>, Serializable {
    /**
     * Serialization.
     */
    private static final long serialVersionUID = 1701446492454684836L;

    @Override
    public int compare(final Timed o, final Timed o2) {
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
