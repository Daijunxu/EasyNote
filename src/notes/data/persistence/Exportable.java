package notes.data.persistence;

import java.io.Writer;

/**
 * Identifies that a type can be exported as a serialized string.
 * <p/>
 * Author: Rui Du
 * Date: 4/14/14
 * Time: 9:24 AM
 */
public interface Exportable {

    /**
     * Exports the caller object.
     *
     * @param writer The writer for writing character streams.
     */
    public void export(Writer writer);
}
