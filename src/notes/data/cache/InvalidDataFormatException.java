/**
 *
 */
package notes.data.cache;

/**
 * Thrown when the data file's format is invalid when loading caches.
 *
 * @author Rui Du
 * @version 1.0
 */
public class InvalidDataFormatException extends Exception {

    /**
     * Constructs a new exception with null as its detail message.
     */
    public InvalidDataFormatException() {
        super();
    }

    /**
     * Constructs a new exception with the specified detail message.
     */
    public InvalidDataFormatException(String arg0) {
        super(arg0);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     */
    public InvalidDataFormatException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * Constructs a new exception with the specified cause and a detail message of (cause==null ?
     * null : cause.toString()) (which typically contains the class and detail message of cause).
     */
    public InvalidDataFormatException(Throwable arg0) {
        super(arg0);
    }
}
