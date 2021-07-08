package com.company.library.screen.bookpublication;

import com.company.library.screen.bookinstance.BookInstanceBrowse;
import io.jmix.ui.Notifications;
import io.jmix.ui.Screens;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.GroupTable;
import io.jmix.ui.screen.*;
import com.company.library.entity.BookPublication;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Inject;

@UiController("libr_BookPublication.browse")
@UiDescriptor("book-publication-browse.xml")
@LookupComponent("bookPublicationsTable")
public class BookPublicationBrowse extends StandardLookup<BookPublication> {
    @Autowired
    private Notifications notifications;

    @Autowired
    private GroupTable<BookPublication> bookPublicationsTable;

    @Autowired
    private MessageBundle messageBundle;

    @Autowired
    private Screens screens;


    @Subscribe("bookPublicationsTable.browseInstances")
    protected void onBookPublicationsTableBrowseInstances(Action.ActionPerformedEvent event) {
        BookPublication bookPublication = bookPublicationsTable.getSingleSelected();
        if (bookPublication != null) {
            BookInstanceBrowse screen = screens.create(BookInstanceBrowse.class, OpenMode.THIS_TAB);
            screen.setPublication(bookPublication);
            screens.show(screen);
        } else {
            notifications.create()
                    .withCaption(messageBundle.getMessage("selectBookPublicationMessage.text"))
                    .withType(Notifications.NotificationType.HUMANIZED)
                    .show();
        }
    }
}