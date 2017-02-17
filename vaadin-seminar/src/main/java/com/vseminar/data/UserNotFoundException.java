package com.vseminar.data;

public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -8143524516141273449L;

	public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundException(final String message) {
        super(message);
    }

    public UserNotFoundException(final Throwable cause) {
        super(cause);
    }

}