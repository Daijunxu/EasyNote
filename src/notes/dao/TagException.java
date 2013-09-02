/**
 * 
 */
package notes.dao;

/**
 * Thrown when two tags have the same ID or text.
 * 
 * @author Rui Du
 * @version 1.0
 * 
 */
public class TagException extends Exception {

	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = 35774987663493313L;

	/**
	 * Constructs a new exception with null as its detail message.
	 */
	public TagException() {
		super();
	}

	/**
	 * Constructs a new exception with the specified detail message.
	 */
	public TagException(String arg0) {
		super(arg0);
	}

	/**
	 * Constructs a new exception with the specified detail message and cause.
	 */
	public TagException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * Constructs a new exception with the specified cause and a detail message of (cause==null ?
	 * null : cause.toString()) (which typically contains the class and detail message of cause).
	 */
	public TagException(Throwable arg0) {
		super(arg0);
	}
}
