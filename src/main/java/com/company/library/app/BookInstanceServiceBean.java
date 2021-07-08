package com.company.library.app;

import com.company.library.entity.BookInstance;
import com.company.library.entity.BookPublication;
import com.company.library.entity.LibraryDepartment;
import io.jmix.core.*;
import io.jmix.core.accesscontext.CrudEntityContext;
import io.jmix.core.metamodel.model.MetaClass;
import io.jmix.core.security.AccessDeniedException;
import io.jmix.core.security.EntityOp;
import io.jmix.data.impl.NumberIdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

@Component(BookInstanceServiceBean.NAME)
public class BookInstanceServiceBean {
    public static final String NAME = "_BookInstanceServiceBean";
    @Autowired
    private AccessManager accessManager;
    @Autowired
    private Metadata metadata;
    @Autowired
    private DataManager dataManager;
    @Autowired
    private NumberIdWorker numberIdWorker;

    @Transactional
    public Collection<BookInstance> createBookInstances(BookPublication bookPublication, Integer amount){
        checkPermission(EntityOp.CREATE);
        // Due to the @Transactional annotation a new transaction is started right before the method is called and
        // committed after leaving the method
        Collection<BookInstance> bookInstances = new ArrayList<>();
        for (int i = 0; i < amount; ++i) {
            BookInstance bookInstance = metadata.create(BookInstance.class);
            bookInstance.setBookPublication(bookPublication);
            bookInstance.setInventoryNumber(numberIdWorker.createLongId("libr_BookInstance","inventory_number"));
            dataManager.save(bookInstance);

            bookInstances.add(bookInstance);
        }
        return bookInstances;
    }

    public Collection<BookInstance> assignLibraryDepartment(Collection<BookInstance> bookInstances, LibraryDepartment libraryDepartment, FetchPlan bookInstanceFetchPlan){
        checkPermission(EntityOp.UPDATE);
        Collection<BookInstance> mergedInstances = new ArrayList<>();

            for (BookInstance booksInstance : bookInstances) {
                Id<BookInstance> bookInstanceId = Id.ofNullable(booksInstance);
                BookInstance instance = dataManager.load(bookInstanceId).fetchPlan(bookInstanceFetchPlan).one();
                instance.setLibraryDepartment(libraryDepartment);
                // Return the updated instance
                dataManager.save(instance);
                mergedInstances.add(instance);
            }
        return mergedInstances;
    }

    private void checkPermission(EntityOp op){
        MetaClass metaClass = metadata.getClass(BookInstance.class);
        CrudEntityContext entityContext = new CrudEntityContext(metaClass);
        accessManager.applyRegisteredConstraints(entityContext);

        switch (op){
            case CREATE:
                if (!entityContext.isCreatePermitted()){
                    throw new AccessDeniedException("create", metaClass.getName());
                }
                break;
            case UPDATE:
                if (!entityContext.isUpdatePermitted()){
                    throw new AccessDeniedException("update", metaClass.getName());
                }
                break;
        }

    }
}