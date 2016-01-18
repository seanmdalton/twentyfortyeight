package com.dalton.twentyfortyeight;

public class GameOverException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GameOverException () {

    }

    public GameOverException (String message) {
        super (message);
    }

    public GameOverException (Throwable cause) {
        super (cause);
    }

    public GameOverException (String message, Throwable cause) {
        super (message, cause);
    }
}
