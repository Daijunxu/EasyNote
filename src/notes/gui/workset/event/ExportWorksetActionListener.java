/**
 *
 */
package notes.gui.workset.event;

import notes.bean.WorksetHome;
import notes.data.cache.Property;
import notes.gui.workset.component.ExportWorksetDialog;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The event listener for exporting current workset.
 *
 * @author Rui Du
 * @version 1.0
 */
public class ExportWorksetActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        if (WorksetHome.get().getCurrentWorkset() == null) {
            if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                SoundFactory.playError();
            }
            JOptionPane.showMessageDialog(null, "No workset is selected!", "Input error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                SoundFactory.playPopup();
            }
            new ExportWorksetDialog();
        }
    }
}
