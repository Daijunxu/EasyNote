package notes.gui.workset.component;

import notes.bean.WorksetHome;
import notes.entity.NoteStatus;
import notes.entity.workset.WorksheetNote;
import notes.gui.main.component.MainPanel;
import notes.gui.workset.event.DeleteWorksheetNoteActionListener;
import notes.gui.workset.event.EditWorksheetNoteActionListener;
import notes.gui.workset.event.NewWorksheetNoteActionListener;
import notes.utils.SoundFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Pops up when right clicking a note in the worksheet note panel.
 *
 * @author Rui Du
 */
public class WorksheetNotePopupMenu extends JPopupMenu {

    private final JMenuItem newItem;
    private final JMenuItem editItem;
    private final JMenu setNoteStatusItem;
    private final JMenuItem deleteItem;

    /**
     * Creates an instance of {@code WorksheetNotePopupMenu}.
     */
    public WorksheetNotePopupMenu() {
        newItem = new JMenuItem("New");
        newItem.addActionListener(new NewWorksheetNoteActionListener());
        editItem = new JMenuItem("Edit");
        editItem.addActionListener(new EditWorksheetNoteActionListener());

        setNoteStatusItem = new JMenu("Set status to");
        for (NoteStatus noteStatus : NoteStatus.values()) {
            JMenuItem noteStatusItem = new JMenuItem(noteStatus.getDescription());
            noteStatusItem.addActionListener(new SetWorksheetNoteStatusActionListener());
            setNoteStatusItem.add(noteStatusItem);
        }

        deleteItem = new JMenuItem("Delete");
        deleteItem.addActionListener(new DeleteWorksheetNoteActionListener());
        if (WorksetHome.get().getCurrentWorksheetNote() == null) {
            editItem.setEnabled(false);
            setNoteStatusItem.setEnabled(false);
            deleteItem.setEnabled(false);
        }
        if (WorksetHome.get().getCurrentWorksheet() == null) {
            newItem.setEnabled(false);
        }
        add(newItem);
        add(editItem);
        add(setNoteStatusItem);
        add(deleteItem);
    }

    /**
     * Defines event listener of setting the worksheet note status.
     */
    private class SetWorksheetNoteStatusActionListener implements ActionListener {

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent event) {
            try {
                WorksheetNote currentWorksheetNote = WorksetHome.get().getCurrentWorksheetNote();
                if (currentWorksheetNote == null) {
                    SoundFactory.playError();
                    JOptionPane.showMessageDialog(null, "No note is selected!", "Input error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    JMenuItem source = (JMenuItem) event.getSource();
                    NoteStatus statusToSet = NoteStatus.getNoteStatusFromDescription(source.getText());
                    currentWorksheetNote.setNoteStatus(statusToSet);

                    // Update the note panel.
                    MainPanel.get().updateWorksheetNotePanel(WorksetHome.get().getCurrentWorksheet(), null);
                    SoundFactory.playUpdate();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
