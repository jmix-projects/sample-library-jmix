package com.company.library.screen.departmentassign;

import com.company.library.app.BookInstanceServiceBean;
import com.company.library.entity.BookInstance;
import com.company.library.entity.LibraryDepartment;
import io.jmix.core.FetchPlan;
import io.jmix.ui.Notifications;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.ComboBox;
import io.jmix.ui.component.EntityComboBox;
import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

@UiController("libr_DepartmentAssigning")
@UiDescriptor("department-assigning.xml")
public class DepartmentAssigning extends Screen {

    public static CloseAction SUCCESS_ACTION = new StandardCloseAction("SUCCESS_ACTION");

    @Autowired
    private BookInstanceServiceBean bookInstanceServiceBean;

    @Autowired
    private EntityComboBox<LibraryDepartment> comboBox;

    @Autowired
    private CollectionLoader<LibraryDepartment> libraryDepartmentsDl;
    @Autowired
    private Notifications notifications;

    private FetchPlan bookInstanceFetchPlan;

    public Collection<BookInstance> assignedInstances;
    private Collection<BookInstance> bookInstances;


    @Subscribe("assignAction")
    protected void onAssignAction(Action.ActionPerformedEvent event) {
        LibraryDepartment libraryDepartment = comboBox.getValue();
        if (libraryDepartment != null) {
            assignedInstances = bookInstanceServiceBean.assignLibraryDepartment(
                    bookInstances, libraryDepartment, bookInstanceFetchPlan);
            close(SUCCESS_ACTION);
        } else {
            notifications.create()
                    .withCaption("Select library department")
                    .withType(Notifications.NotificationType.HUMANIZED)
                    .show();
        }
    }

    @Subscribe("clearAction")
    protected void onClearAction(Action.ActionPerformedEvent event) {
        close(WINDOW_CLOSE_ACTION);
    }

    public Collection<BookInstance> getAssignedInstances() {
        return assignedInstances;
    }

    public void setBookInstances(Collection<BookInstance> bookInstances) {
        this.bookInstances = bookInstances;
    }

    public void setBookInstanceFetchPlan(FetchPlan bookInstanceFetchPlan) {
        this.bookInstanceFetchPlan = bookInstanceFetchPlan;
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        libraryDepartmentsDl.load();
    }


}