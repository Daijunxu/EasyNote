package notes.gui.workset.event;

import notes.bean.WorksetHome;
import notes.data.cache.Property;
import notes.gui.workset.component.ViewWorksetDialog;
import notes.utils.SoundFactory;
import notes.utils.SoundTheme;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Defines event listener of viewing a workset.
 *
 * @author Rui Du
 */
public class ViewWorksetActionListener implements ActionListener {

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        try {

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
                new ViewWorksetDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
