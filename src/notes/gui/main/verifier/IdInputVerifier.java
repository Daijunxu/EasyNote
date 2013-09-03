/**
 *
 */
package notes.gui.main.verifier;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 * Verifies that the input text in {@link JTextField} is a valid Long type ID.
 *
 * @author Rui Du
 * @version 1.0
 */
public class IdInputVerifier extends InputVerifier {

    /**
     * @see javax.swing.InputVerifier#verify(javax.swing.JComponent)
     */
    @Override
    public boolean verify(JComponent input) {
        try {
            String text = ((JTextField) input).getText();
            if (text == null || text.equals(""))
                return false;
            Long id = Long.parseLong(text);
            if (id < 0L) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
