/**
 *
 */
package notes.gui.workset.event;

import notes.bean.WorksetHome;
import notes.data.cache.Property;
import notes.entity.workset.Worksheet;
import notes.gui.main.component.MainPanel;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Defines event listener of deleting a worksheet.
 *
 * @author Rui Du
 * @version 1.0
 */
public class DeleteWorksheetActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        WorksetHome home = WorksetHome.get();
        MainPanel frame = MainPanel.get();
        try {
            Worksheet worksheet = home.getCurrentWorksheet();
            if (worksheet == null) {
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playError();
                }
                JOptionPane.showMessageDialog(null, "No worksheet is selected!", "Input error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                SoundFactory.playNotify();
            }
            int result = JOptionPane.showConfirmDialog(null,
                    "Delete this worksheet and all the notes in it?", "Confirm Dialog",
                    JOptionPane.YES_NO_OPTION);
            // 0 for yes and 1 for no.
            if (result == 0) {
                // Delete the selected worksheet.
                home.getWorksheetNoteDAO().deleteWorksheet(worksheet, home.getCurrentWorkset().getDocumentId());
                // Update temporary data in the WorksetHome.
                home.updateTemporaryData(home.getCurrentWorkset().getDocumentId(), null, null);
                // Update the worksheet and note panel.
                frame.updateIndexPanel();
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playDelete();
                }
            } else {
                if (!Property.get().getSoundTheme().equals(SoundTheme.NONE.getDescription())) {
                    SoundFactory.playNavigation();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
