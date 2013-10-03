package notes.gui.book.event;

import notes.bean.BookHome;
import notes.data.cache.Property;
import notes.gui.book.component.ExportBookDialog;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The event listener for exporting current book.
 *
 * @author Rui Du
 */
public class ExportBookActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        if (BookHome.get().getCurrentBook() == null) {
            if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                SoundFactory.playError();
            }
            JOptionPane.showMessageDialog(null, "No book is selected!", "Input error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                SoundFactory.playPopup();
            }
            new ExportBookDialog();
        }
    }
}
