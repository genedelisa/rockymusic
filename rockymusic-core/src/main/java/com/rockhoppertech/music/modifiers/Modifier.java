/*
 * $Id$
 *
 * Copyright 1998,1999,2000,2001 by Rockhopper Technologies, Inc.,
 * 75 Trueman Ave., Haddonfield, New Jersey, 08033-2529, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Rockhopper Technologies, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with RTI.
 */

package com.rockhoppertech.music.modifiers;

/**
 * Also see MusicVisitor for a more general GoF visitor.
 * 
 * interface <code>Modifier</code>
 *
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * @since 1.0
 */
public interface Modifier {
    /**
     * <code>getName</code>
     *
     * @return a <code>String</code> value
     */
    public String getName();

    /**
     * <code>getDescription</code>
     *
     * @return a <code>String</code> value
     */
    public String getDescription();

    public enum Operation {
        SET,
        ADD,
        SUBTRACT,
        MULTIPLY,
        DIVIDE,
        MOD,
        QUANTIZE
    }

    public void setOperation(Operation operation);

    public void setValues(double[] values);
}
/*
  maybe a modifier info class? kinda like beaninfo? could describe
  inputs,params etc
*/

/*
 * History:
 *
 * $Log$
 *
 * This version: $Revision$
 * Last modified: $Date$
 * Last modified by: $Author$
 */
