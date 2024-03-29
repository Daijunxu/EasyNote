package notes.gui.book.validation;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 * Verifies that the input text in {@link JTextField} is a valid edition number.
 *
 * Author: Rui Du
 */
public class EditionInputVerifier extends InputVerifier {

    /**
     * @see javax.swing.InputVerifier#verify(javax.swing.JComponent)
     */
    @Override
    public boolean verify(JComponent input) {
        try {
            String text = ((JTextField) input).getText();
            if (text == null || text.equals("")) {
                return true;
            }
            Integer edition = Integer.parseInt(text);
            return (edition >= 0);
        } catch (Exception e) {
            return false;
        }
    }
}
