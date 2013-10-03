package notes.gui.workset.component;

import notes.bean.WorksetHome;
import notes.gui.workset.event.DeleteWorksheetActionListener;
import notes.gui.workset.event.EditWorksheetActionListener;
import notes.gui.workset.event.NewWorksheetActionListener;

import javax.swing.*;

/**
 * Pops up when right clicking a Workset in the worksheet panel.
 *
 * @author Rui Du
 */
public class WorksheetPopupMenu extends JPopupMenu {

    private final JMenuItem newItem;
    private final JMenuItem editItem;
    private final JMenuItem deleteItem;

    /**
     * Creates an instance of {@code WorksheetPopupMenu}.
     */
    public WorksheetPopupMenu() {
        newItem = new JMenuItem("New");
        newItem.addActionListener(new NewWorksheetActionListener());
        editItem = new JMenuItem("Edit");
        editItem.addActionListener(new EditWorksheetActionListener());
        deleteItem = new JMenuItem("Delete");
        deleteItem.addActionListener(new DeleteWorksheetActionListener());
        if (WorksetHome.get().getCurrentWorkset() == null) {
            editItem.setEnabled(false);
            deleteItem.setEnabled(false);
        }
        add(newItem);
        add(editItem);
        add(deleteItem);
    }
}
