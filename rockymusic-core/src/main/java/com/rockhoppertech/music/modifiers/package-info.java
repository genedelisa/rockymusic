/**
 * MIDITrack manipulation classes.
 * <p>
 * These are essentially GoF Visitor patterns. 
 * <p>
 * Each NoteModifier has a  modify(Note n); method.
 * <p>
 * Each MIDINoteModifier has a modify(MIDINote n); method.
 * <p>
 * The MIDITrack class has map methods that will use a Modifier to change
 * some parameter on each MIDINote in the track.
 * <p>
 * The Abstract classes provide default implementations, including
 * managing a Circular list of values.
 * 
 *
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 * @version 1.0
 */
package com.rockhoppertech.music.modifiers;


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
