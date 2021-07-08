package com.company.library.screen.book;

import io.jmix.ui.screen.*;
import com.company.library.entity.Book;

@UiController("libr_Book.edit")
@UiDescriptor("book-edit.xml")
@EditedEntityContainer("bookDc")
public class BookEdit extends StandardEditor<Book> {
}