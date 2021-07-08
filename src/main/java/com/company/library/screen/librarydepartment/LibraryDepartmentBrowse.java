package com.company.library.screen.librarydepartment;

import io.jmix.ui.screen.*;
import com.company.library.entity.LibraryDepartment;

@UiController("libr_LibraryDepartment.browse")
@UiDescriptor("library-department-browse.xml")
@LookupComponent("libraryDepartmentsTable")
public class LibraryDepartmentBrowse extends StandardLookup<LibraryDepartment> {
}