package notes.dao;

/**
 * Thrown when two records have the same ID.
 *
 * Author: Rui Du
 */
public class DuplicateRecordException extends Exception {

    /**
     * Constructs a new exception with null as its detail message.
     */
    public DuplicateRecordException() {
        super();
    }

    /**
     * Constructs a new exception with the specified detail message.
     */
    public DuplicateRecordException(String arg0) {
        super(arg0);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     */
    public DuplicateRecordException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * Constructs a new exception with the specified cause and a detail message of (cause==null ?
     * null : cause.toString()) (which typically contains the class and detail message of cause).
     */
    public DuplicateRecordException(Throwable arg0) {
        super(arg0);
    }
}
