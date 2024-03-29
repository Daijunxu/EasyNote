package notes.gui.book.validation;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 * Verifies that the input text in {@link JTextField} is a valid year number.
 *
 * Author: Rui Du
 */
public class PublishedYearInputVerifier extends InputVerifier {

    /**
     * @see javax.swing.InputVerifier#verify(javax.swing.JComponent)
     */
    @Override
    public boolean verify(JComponent input) {
        try {
            String text = ((JTextField) input).getText();
            if (text == null || text.equals(""))
                return true;
            Integer year = Integer.parseInt(text);
            // Verify that the year is between 1800 and 2100.
            return (year >= 1800 && year <= 2100);
        } catch (Exception e) {
            return false;
        }
    }
}
