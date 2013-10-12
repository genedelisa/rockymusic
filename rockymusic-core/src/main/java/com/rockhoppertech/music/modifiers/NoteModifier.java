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

import com.rockhoppertech.music.Note;

/**
 * Also see MusicVisitor for a more general GoF visitor.
 * 
 * interface <code>NoteModifier</code>
 *
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * @version $Revision$, $Date$
 * @since 1.0
 */
public interface NoteModifier extends Modifier {
    /**
     * <code>modify</code>
     *
     * @param n a <code>MIDINote</code> instance
     */
    public void modify(Note n);
   // public void modify(Note note, ModifierChain chain);
    
    public void setSuccessor(NoteModifier successor);
    
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
