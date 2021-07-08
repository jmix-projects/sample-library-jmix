package com.company.library.action;

import com.company.library.screen.departmentassign.DepartmentAssigning;
import com.company.library.entity.BookInstance;
import io.jmix.ui.Notifications;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.action.BaseAction;
import io.jmix.ui.component.Component;
import io.jmix.ui.component.ComponentsHelper;
import io.jmix.ui.component.Frame;
import io.jmix.ui.component.Table;
import io.jmix.ui.component.data.TableItems;
import io.jmix.ui.component.data.meta.ContainerDataUnit;
import io.jmix.ui.model.CollectionContainer;
import io.jmix.ui.model.DataLoader;
import io.jmix.ui.model.HasLoader;
import io.jmix.ui.screen.OpenMode;
import io.jmix.ui.screen.ScreenContext;

import java.util.Set;

import static io.jmix.core.common.util.Preconditions.checkNotNullArgument;

/**
 * Action that allows to assign library department to book instances, selected in a table.
 * <p>
 * This action is used by BookInstanceBrowse and AccessionRegisterWindow.
 */
public class AssignLibraryDepartmentAction extends BaseAction {

    private Table<BookInstance> booksInstancesTable;

    private ScreenBuilders screenBuilders;

    public AssignLibraryDepartmentAction(Table<BookInstance> booksInstancesTable, ScreenBuilders screenBuilders) {
        super("assignLibraryDepartment");
        this.caption = "Send to department";
        this.booksInstancesTable = booksInstancesTable;
        this.screenBuilders = screenBuilders;
    }

    @Override
    public void actionPerform(Component component) {
        Frame frame = booksInstancesTable.getFrame();
        ScreenContext screenContext = ComponentsHelper.getScreenContext(booksInstancesTable);
        Set<BookInstance> bookInstances = booksInstancesTable.getSelected();

        if (!bookInstances.isEmpty()) {

            DepartmentAssigning screen = screenBuilders
                    .screen(frame.getFrameOwner())
                    .withScreenClass(DepartmentAssigning.class)
                    .withOpenMode(OpenMode.DIALOG)
                    .withAfterCloseListener(closeEvent -> {
                        if (closeEvent.getCloseAction() == DepartmentAssigning.SUCCESS_ACTION) {

                            TableItems<BookInstance> tableItems = booksInstancesTable.getItems();

                            CollectionContainer<BookInstance> container = ((ContainerDataUnit) tableItems).getContainer();
                            DataLoader dataLoader = ((HasLoader) container).getLoader();
                            checkNotNullArgument(dataLoader);
                            dataLoader.load();
                        }
                    })
                    .build();
            screen.setBookInstances(bookInstances);
            screen.setBookInstanceFetchPlan(((ContainerDataUnit) booksInstancesTable.getItems()).getContainer().getFetchPlan());
            screen.show();
        } else {
            screenContext.getNotifications().create()
                    .withCaption("Choose book instance")
                    .withType(Notifications.NotificationType.HUMANIZED)
                    .show();
        }
    }
}
