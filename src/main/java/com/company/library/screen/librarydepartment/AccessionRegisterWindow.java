package com.company.library.screen.librarydepartment;

import com.company.library.action.AssignLibraryDepartmentAction;
import com.company.library.app.BookInstanceServiceBean;
import com.company.library.entity.Book;
import com.company.library.entity.BookInstance;
import com.company.library.entity.BookPublication;
import com.company.library.screen.bookinstance.BookInstanceEdit;
import com.company.library.screen.bookpublication.BookPublicationEdit;
import io.jmix.ui.Notifications;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.*;
import io.jmix.ui.model.CollectionContainer;
import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.screen.*;
import com.company.library.entity.LibraryDepartment;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Objects;

@UiController("libr_AccessionRegister.window")
@UiDescriptor("accession-register.xml")
public class AccessionRegisterWindow extends Screen {

    @Autowired
    private EntityComboBox<Book> bookField;

    @Autowired
    private CollectionLoader<BookPublication> bookPublicationsDl;

    @Autowired
    private Notifications notifications;
    @Autowired
    private MessageBundle messageBundle;

    @Autowired
    private TextField<Integer> bookInstancesAmountField;

    @Autowired
    private Table<BookInstance> bookInstancesTable;

    @Autowired
    private CollectionContainer<BookInstance> bookInstancesDc;
    @Autowired
    private CollectionLoader<Book> booksDl;

    @Autowired
    private ScreenBuilders screenBuilders;
    @Autowired
    private Button assignLibraryDepartmentBtn;
    @Autowired
    private Table<BookPublication> bookPublicationsTable;
    @Autowired
    private BookInstanceServiceBean bookInstanceServiceBean;

    @Subscribe("bookField")
    public void onBookFieldValueChange(HasValue.ValueChangeEvent<Book> event) {
        bookPublicationsDl.setParameter("property", Objects.requireNonNull(bookField.getValue()));
        bookPublicationsDl.load();
        assignLibraryDepartmentBtn.setAction(new AssignLibraryDepartmentAction(bookInstancesTable, screenBuilders));
    }

    @Subscribe("createBookInstances")
    public void onCreateBookInstances(Action.ActionPerformedEvent event) {
        BookPublication bookPublication = bookPublicationsTable.getSingleSelected();
        if (bookPublication == null) {
            notifications.create()
                    .withCaption(messageBundle.getMessage("selectBookPublicationMessage.text"))
                    .withType(Notifications.NotificationType.HUMANIZED)
                    .show();
            return;
        }

        Integer bookInstancesAmount =  bookInstancesAmountField.getValue();

        if (bookInstancesAmount == null || bookInstancesAmount == 0) {
            notifications.create()
                    .withCaption(messageBundle.getMessage("setBookInstancesAmountMessage.text"))
                    .withType(Notifications.NotificationType.HUMANIZED)
                    .show();
            return;
        }

        if (bookInstancesAmount > 100) {
            notifications.create()
                    .withCaption(messageBundle.getMessage("bigBookInstancesAmountMessage.text"))
                    .withType(Notifications.NotificationType.HUMANIZED)
                    .show();
            return;
        }

        // Create book instances in the middleware service
        Collection<BookInstance> bookInstances = bookInstanceServiceBean.createBookInstances(
                bookPublication, bookInstancesAmount);

        // Add created book instances to the datasource

        bookInstancesDc.setItems(bookInstances);
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        booksDl.load();
    }

    @Subscribe("bookPublicationsTable.createBookPublication")
    public void onBookPublicationsTableCreateBookPublication(Action.ActionPerformedEvent event) {
        Book book = bookField.getValue();
        if (book == null) {
            notifications.create()
                    .withCaption(messageBundle.getMessage("selectBookMessage.text"))
                    .withType(Notifications.NotificationType.HUMANIZED)
                    .show();
            return;
        }

        screenBuilders.editor(bookPublicationsTable)
                .newEntity()
                .withInitializer(bookPublication -> {          // lambda to initialize new instance
                    bookPublication.setBook(book);
                })
                .withScreenClass(BookPublicationEdit.class)
                // specific editor screen
                .withOpenMode(OpenMode.THIS_TAB)
                .build()
                .show();
    }

    @Subscribe("bookInstancesTableEditBtn")
    public void onBookInstancesTableEditBtnClick(Button.ClickEvent event) {
        if (bookInstancesTable.getSingleSelected() != null) {
            screenBuilders.editor(BookInstance.class,bookInstancesTable.getFrame().getFrameOwner())
                    .withScreenClass(BookInstanceEdit.class)
                    .withOpenMode(OpenMode.DIALOG)
                    .editEntity(bookInstancesTable.getSingleSelected())
                    .withAfterCloseListener(afterCloseEvent -> {
                        if (afterCloseEvent.closedWith(StandardOutcome.COMMIT)) {
                            getScreenData().loadAll();
                        }
                    }).show();

        } else{
            notifications.create()
                    .withCaption(messageBundle.getMessage("selectBookInstance.text"))
                    .withType(Notifications.NotificationType.HUMANIZED)
                    .show();
        }

    }
}
