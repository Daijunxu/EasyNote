package notes.gui.workset.component;

import notes.businesslogic.WorksetBusinessLogic;
import notes.businessobjects.Note;
import notes.businessobjects.workset.WorksheetNoteStatus;
import notes.gui.main.event.note.MoveNoteDownActionListener;
import notes.gui.main.event.note.MoveNoteToBottomActionListener;
import notes.gui.main.event.note.MoveNoteToTopActionListener;
import notes.gui.main.event.note.MoveNoteUpActionListener;
import notes.gui.workset.event.DeleteWorksheetNoteActionListener;
import notes.gui.workset.event.EditWorksheetNoteActionListener;
import notes.gui.workset.event.NewWorksheetNoteActionListener;
import notes.gui.workset.event.SetWorksheetNoteStatusActionListener;

import javax.swing.*;

/**
 * Pops up when right clicking a note in the worksheet note panel.
 * <p/>
 * Author: Rui Du
 */
public class WorksheetNotePopupMenu extends JPopupMenu {

    /**
     * Creates an instance of {@code WorksheetNotePopupMenu}.
     */
    public WorksheetNotePopupMenu() {
        WorksetBusinessLogic logic = WorksetBusinessLogic.get();

        JMenuItem newItem = new JMenuItem("New");
        newItem.addActionListener(new NewWorksheetNoteActionListener());
        JMenuItem editItem = new JMenuItem("Edit");
        editItem.addActionListener(new EditWorksheetNoteActionListener());

        JMenuItem deleteItem = new JMenuItem("Delete");
        deleteItem.addActionListener(new DeleteWorksheetNoteActionListener());

        JMenu setNoteStatusItem = new JMenu("Set status to");
        for (WorksheetNoteStatus noteStatus : WorksheetNoteStatus.values()) {
            JMenuItem noteStatusItem = new JMenuItem(noteStatus.getDescription());
            noteStatusItem.addActionListener(new SetWorksheetNoteStatusActionListener());
            setNoteStatusItem.add(noteStatusItem);
        }

        JMenuItem moveUpItem = new JMenuItem("Move up");
        moveUpItem.addActionListener(new MoveNoteUpActionListener());
        JMenuItem moveDownItem = new JMenuItem("Move down");
        moveDownItem.addActionListener(new MoveNoteDownActionListener());
        JMenuItem moveToTopItem = new JMenuItem("Move to top");
        moveToTopItem.addActionListener(new MoveNoteToTopActionListener());
        JMenuItem moveToBottomItem = new JMenuItem("Move to bottom");
        moveToBottomItem.addActionListener(new MoveNoteToBottomActionListener());

        if (WorksetBusinessLogic.get().getCurrentWorksheet() == null) {
            newItem.setEnabled(false);
        }

        Note currentNote = WorksetBusinessLogic.get().getCurrentNote();
        if (currentNote == null) {
            editItem.setEnabled(false);
            deleteItem.setEnabled(false);

            setNoteStatusItem.setEnabled(false);
        }

        if (currentNote == null || !logic.canMoveCurrentNoteUp()) {
            moveUpItem.setEnabled(false);
            moveToTopItem.setEnabled(false);
        }

        if (currentNote == null || !logic.canMoveCurrentNoteDown()) {
            moveDownItem.setEnabled(false);
            moveToBottomItem.setEnabled(false);
        }


        add(newItem);
        add(editItem);
        add(deleteItem);
        addSeparator();
        add(setNoteStatusItem);
        addSeparator();
        add(moveUpItem);
        add(moveDownItem);
        add(moveToTopItem);
        add(moveToBottomItem);
    }
}
