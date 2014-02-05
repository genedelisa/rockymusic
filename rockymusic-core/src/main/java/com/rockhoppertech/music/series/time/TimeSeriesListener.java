/*
 * $Id$
 *
 * Copyright 1998,1999,2000,2001 by Rockhopper Technologies, Inc., 75
 * Trueman Ave., Haddonfield, New Jersey, 08033-2529, U.S.A.  All
 * rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Rockhopper Technologies, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use it
 * only in accordance with the terms of the license agreement you
 * entered into with RTI.
 */

package com.rockhoppertech.music.series.time;
import java.util.EventListener;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 *
 */
public interface TimeSeriesListener extends EventListener {
    public void seriesChanged(TimeSeriesEvent e);
}