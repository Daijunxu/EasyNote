package notes.gui.book.event;

import notes.data.cache.Property;
import notes.gui.book.component.NewBookDialog;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Defines event listener of creating a new book.
 *
 * @author Rui Du
 */
public class NewBookActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                SoundFactory.playPopup();
            }
            new NewBookDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
