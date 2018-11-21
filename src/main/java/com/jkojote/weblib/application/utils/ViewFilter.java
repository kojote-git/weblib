package com.jkojote.weblib.application.utils;

import com.jkojote.library.domain.shared.domain.ViewObject;

import java.util.List;

public interface ViewFilter<T extends ViewObject> {

    List<T> findAll(String queryString);

}
