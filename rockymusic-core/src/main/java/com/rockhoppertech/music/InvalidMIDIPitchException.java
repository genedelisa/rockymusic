/**
 * 
 */
package com.rockhoppertech.music;

/**
 * If specify a pitch < Pitch.MIN or > Pitch.MAX.
 * 
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 *
 */
public class InvalidMIDIPitchException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     *
     * @param message
     */
    public InvalidMIDIPitchException(final String message) {
        super(message);
    }
}
