/**
 * 
 */
package com.rockhoppertech.music.midi.parse;

/**
 * @author <a href="mailto:gene@rockhoppertech.com">Gene De Lisa</a>
 *
 */
public class MIDIParserException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 211965570330402738L;

	/**
	 * 
	 */
	public MIDIParserException() {

	}

	/**
	 * @param message
	 */
	public MIDIParserException(String message) {
		super(message);

	}

	/**
	 * @param cause
	 */
	public MIDIParserException(Throwable cause) {
		super(cause);

	}

	/**
	 * @param message
	 * @param cause
	 */
	public MIDIParserException(String message, Throwable cause) {
		super(message, cause);

	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public MIDIParserException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);

	}

}
