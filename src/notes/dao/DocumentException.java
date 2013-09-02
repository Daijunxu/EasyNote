/**
 * 
 */
package notes.dao;

/**
 * Thrown when two documents have the same ID or title.
 * 
 * @author Rui Du
 * @version 1.0
 * 
 */
public class DocumentException extends Exception {

	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = 2483244007625339745L;

	/**
	 * Constructs a new exception with null as its detail message.
	 */
	public DocumentException() {
		super();
	}

	/**
	 * Constructs a new exception with the specified detail message.
	 */
	public DocumentException(String arg0) {
		super(arg0);
	}

	/**
	 * Constructs a new exception with the specified detail message and cause.
	 */
	public DocumentException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * Constructs a new exception with the specified cause and a detail message of (cause==null ?
	 * null : cause.toString()) (which typically contains the class and detail message of cause).
	 */
	public DocumentException(Throwable arg0) {
		super(arg0);
	}
}
