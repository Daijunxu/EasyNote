/**
 *
 */
package notes.gui.book.verifier;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 * Verifies that the input text in {@link JTextField} is a valid ISBN number.
 *
 * @author Rui Du
 * @version 1.0
 */
public class ISBNInputVerifier extends InputVerifier {

    /**
     * Verifies that if a 10 digit ISBN is valid.
     *
     * @param isbn The ISBN to verify
     * @return {@code boolean} True if the ISBN is valid; false otherwise.
     */
    private boolean isISBN10Valid(String isbn) {
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += Integer.valueOf(isbn.substring(i, i + 1)) * (10 - i);
        }
        return sum % 11 == 0;
    }

    /**
     * Verifies that if a 13 digit ISBN is valid.
     *
     * @param isbn The ISBN to verify
     * @return {@code boolean} True if the ISBN is valid; false otherwise.
     */
    private boolean isISBN13Valid(String isbn) {
        int check = 0;
        for (int i = 0; i < 12; i += 2) {
            check += Integer.valueOf(isbn.substring(i, i + 1));
        }
        for (int i = 1; i < 12; i += 2) {
            check += Integer.valueOf(isbn.substring(i, i + 1)) * 3;
        }
        check += Integer.valueOf(isbn.substring(12));
        return check % 10 == 0;
    }

    /**
     * @see javax.swing.InputVerifier#verify(javax.swing.JComponent)
     */
    @Override
    public boolean verify(JComponent input) {
        try {
            String text = ((JTextField) input).getText();
            if (text == null || text.equals(""))
                return true;
            text = text.replaceAll("-", "");
            if (text.length() == 13) {
                return isISBN13Valid(text);
            } else if (text.length() == 10) {
                return isISBN10Valid(text);
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
