package com.jkojote.weblib.application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public interface ViewTranslator<From, To> {

    To translate(From obj);

    default List<To> batchTranslate(Collection<From> c) {
        List<To> list = new ArrayList<>();
        for (From t : c)
            list.add(translate(t));
        return list;
    }
}
