/**
 *
 */
package notes.gui.workset.component;

import notes.bean.WorksetHome;
import notes.gui.workset.event.DeleteWorksheetNoteActionListener;
import notes.gui.workset.event.EditWorksheetNoteActionListener;
import notes.gui.workset.event.NewWorksheetNoteActionListener;
import notes.gui.workset.event.ViewWorksheetNoteActionListener;

import javax.swing.*;

/**
 * Pops up when right clicking a note in the worksheet note panel.
 *
 * @author Rui Du
 * @version 1.0
 */
public class WorksheetNotePopupMenu extends JPopupMenu {

    private final JMenuItem newItem;
    private final JMenuItem viewItem;
    private final JMenuItem editItem;
    private final JMenuItem deleteItem;

    /**
     * Creates an instance of {@code WorksheetNotePopupMenu}.
     */
    public WorksheetNotePopupMenu() {
        newItem = new JMenuItem("New");
        newItem.addActionListener(new NewWorksheetNoteActionListener());
        viewItem = new JMenuItem("View");
        viewItem.addActionListener(new ViewWorksheetNoteActionListener());
        editItem = new JMenuItem("Edit");
        editItem.addActionListener(new EditWorksheetNoteActionListener());
        deleteItem = new JMenuItem("Delete");
        deleteItem.addActionListener(new DeleteWorksheetNoteActionListener());
        if (WorksetHome.get().getCurrentWorksheetNote() == null) {
            viewItem.setEnabled(false);
            editItem.setEnabled(false);
            deleteItem.setEnabled(false);
        }
        if (WorksetHome.get().getCurrentWorksheet() == null) {
            newItem.setEnabled(false);
        }
        add(newItem);
        add(viewItem);
        add(editItem);
        add(deleteItem);
    }

}
