/**
 *
 */
package notes.gui.workset.component;

import notes.bean.WorksetHome;
import notes.gui.workset.event.DeleteWorksetActionListener;
import notes.gui.workset.event.EditWorksetActionListener;
import notes.gui.workset.event.NewWorksetActionListener;

import javax.swing.*;

/**
 * Pops up when right clicking a Workset in the worksheet panel.
 *
 * @author Rui Du
 * @version 1.0
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
        newItem.addActionListener(new NewWorksetActionListener());
        editItem = new JMenuItem("Edit");
        editItem.addActionListener(new EditWorksetActionListener());
        deleteItem = new JMenuItem("Delete");
        deleteItem.addActionListener(new DeleteWorksetActionListener());
        if (WorksetHome.get().getCurrentWorkset() == null) {
            editItem.setEnabled(false);
            deleteItem.setEnabled(false);
        }
        add(newItem);
        add(editItem);
        add(deleteItem);
    }
}
