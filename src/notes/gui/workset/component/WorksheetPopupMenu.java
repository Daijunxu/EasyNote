package notes.gui.workset.component;

import notes.businesslogic.WorksetBusinessLogic;
import notes.gui.workset.event.DeleteWorksheetActionListener;
import notes.gui.workset.event.EditWorksheetActionListener;
import notes.gui.workset.event.MoveWorksheetDownActionListener;
import notes.gui.workset.event.MoveWorksheetToBottomActionListener;
import notes.gui.workset.event.MoveWorksheetToTopActionListener;
import notes.gui.workset.event.MoveWorksheetUpActionListener;
import notes.gui.workset.event.NewWorksheetActionListener;

import javax.swing.*;

/**
 * Pops up when right clicking a Workset in the worksheet panel.
 * <p/>
 * Author: Rui Du
 */
public class WorksheetPopupMenu extends JPopupMenu {

    private final JMenuItem newItem;
    private final JMenuItem editItem;
    private final JMenuItem deleteItem;
    private final JMenuItem moveUpItem;
    private final JMenuItem moveDownItem;
    private final JMenuItem moveToTopItem;
    private final JMenuItem moveToBottomItem;

    private final WorksetBusinessLogic logic = WorksetBusinessLogic.get();

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

        moveUpItem = new JMenuItem("Move up");
        moveUpItem.addActionListener(new MoveWorksheetUpActionListener());
        moveDownItem = new JMenuItem("Move down");
        moveDownItem.addActionListener(new MoveWorksheetDownActionListener());
        moveToTopItem = new JMenuItem("Move to top");
        moveToTopItem.addActionListener(new MoveWorksheetToTopActionListener());
        moveToBottomItem = new JMenuItem("Move to bottom");
        moveToBottomItem.addActionListener(new MoveWorksheetToBottomActionListener());

        if (logic.getCurrentWorksheet() == null) {
            editItem.setEnabled(false);
            deleteItem.setEnabled(false);
        }

        if (!logic.canMoveCurrentWorksheetUp()) {
            moveUpItem.setEnabled(false);
            moveToTopItem.setEnabled(false);
        }

        if (!logic.canMoveCurrentWorksheetDown()) {
            moveDownItem.setEnabled(false);
            moveToBottomItem.setEnabled(false);
        }

        add(newItem);
        add(editItem);
        add(deleteItem);
        addSeparator();
        add(moveUpItem);
        add(moveDownItem);
        add(moveToTopItem);
        add(moveToBottomItem);
    }


}
