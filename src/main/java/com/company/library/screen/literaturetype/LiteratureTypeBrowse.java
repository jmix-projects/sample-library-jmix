package com.company.library.screen.literaturetype;

import io.jmix.ui.component.Button;
import io.jmix.ui.component.HasValue;
import io.jmix.ui.component.TextField;
import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.screen.*;
import com.company.library.entity.LiteratureType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("libr_LiteratureType.browse")
@UiDescriptor("literature-type-browse.xml")
@LookupComponent("literatureTypesTable")
public class LiteratureTypeBrowse extends StandardLookup<LiteratureType> {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(LiteratureTypeBrowse.class);
    @Autowired
    private CollectionLoader<LiteratureType> literatureTypesDl;

    @Autowired
    private TextField<String> filterField;

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        loadLiteratureTypes();
    }

    @Subscribe("filterField")
    public void onFilterFieldValueChange(HasValue.ValueChangeEvent event) {
        loadLiteratureTypes();
    }

    @Subscribe("applyBtn")
    public void onApplyBtnClick(Button.ClickEvent event) {
        loadLiteratureTypes();
    }

    private void loadLiteratureTypes(){
        String value = filterField.getValue();

        if (value != null) {
            literatureTypesDl.setParameter("property", "(?i)%" + value + "%");
        } else {
            log.info("String is null");
            literatureTypesDl.setParameter("property", "%");
        }

        literatureTypesDl.load();
        getScreenData().loadAll();
    }

}