package com.company.library.screen.publisher;

import io.jmix.ui.screen.*;
import com.company.library.entity.Publisher;

@UiController("libr_Publisher.browse")
@UiDescriptor("publisher-browse.xml")
@LookupComponent("publishersTable")
public class PublisherBrowse extends StandardLookup<Publisher> {
}