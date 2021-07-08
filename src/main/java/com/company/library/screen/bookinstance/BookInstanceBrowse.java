package com.company.library.screen.bookinstance;

import com.company.library.action.AssignLibraryDepartmentAction;
import com.company.library.entity.BookPublication;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.component.Button;
import io.jmix.ui.component.GroupTable;
import io.jmix.ui.component.Label;
import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.screen.*;
import com.company.library.entity.BookInstance;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("libr_BookInstance.browse")
@UiDescriptor("book-instance-browse.xml")
@LookupComponent("bookInstancesTable")
public class BookInstanceBrowse extends StandardLookup<BookInstance> {
    @Autowired
    private ScreenBuilders screenBuilders;

    @Autowired
    private GroupTable<BookInstance> bookInstancesTable;

    @Autowired
    private Label<String> bookTitleLabel;

    private BookPublication publication;

    @Autowired
    private CollectionLoader<BookInstance> bookInstancesDl;

    @Autowired
    private MessageBundle messageBundle;
    @Autowired
    private Button assignLibraryDepartmentBtn;

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        bookTitleLabel.setValue(String.format("%s: %s",messageBundle.getMessage("book"),
                publication.getInstanceName()));
        bookInstancesDl.setParameter("property", publication);
        bookInstancesDl.load();
        assignLibraryDepartmentBtn.setAction(new AssignLibraryDepartmentAction(bookInstancesTable, screenBuilders));
    }

    public void setPublication(BookPublication bookPublication) {
        publication = bookPublication;
    }

}