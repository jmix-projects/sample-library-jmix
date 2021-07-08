package com.company.library.entity;

import com.google.common.base.Strings;
import io.jmix.core.metamodel.annotation.JavaClass;
import io.jmix.core.metamodel.datatype.Datatype;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Locale;

@JavaClass(Integer.class)
public class YearDatatype implements Datatype<Integer> {

    private static final String PATTERN = "##00";

    @Override
    public String format(@Nullable Object value) {
        if (value == null)
            return "";

        DecimalFormat format = new DecimalFormat(PATTERN);
        return format.format(value);
    }

    @Override
    public String format(@Nullable Object value, Locale locale) {
        return format(value);
    }

    @Nullable
    @Override
    public Integer parse(@Nullable String value) throws ParseException {
        if (Strings.isNullOrEmpty(value))
            return null;

        DecimalFormat format = new DecimalFormat(PATTERN);
        int year = format.parse(value).intValue();
        if (year > 2100 || year < 0)
            throw new ParseException("Invalid year", 0);
        if (year < 100)
            year += 2000;
        return year;
    }

    @Nullable
    @Override
    public Integer parse(@Nullable String value, Locale locale) throws ParseException {
        return parse(value);
    }
}
