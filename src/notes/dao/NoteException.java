/**
 *
 */
package notes.dao;

/**
 * Thrown when two notes have the same ID.
 *
 * @author Rui Du
 * @version 1.0
 */
public class NoteException extends Exception {

    /**
     * Generated serial version UID.
     */
    private static final long serialVersionUID = 8345690329455002833L;

    /**
     * Constructs a new exception with null as its detail message.
     */
    public NoteException() {
        super();
    }

    /**
     * Constructs a new exception with the specified detail message.
     */
    public NoteException(String arg0) {
        super(arg0);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     */
    public NoteException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    /**
     * Constructs a new exception with the specified cause and a detail message of (cause==null ?
     * null : cause.toString()) (which typically contains the class and detail message of cause).
     */
    public NoteException(Throwable arg0) {
        super(arg0);
    }
}
