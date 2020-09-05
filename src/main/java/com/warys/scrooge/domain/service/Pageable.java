package com.warys.scrooge.domain.service;

import com.warys.scrooge.domain.model.user.Session;
import com.warys.scrooge.infrastructure.exception.ApiException;

import java.util.List;

public interface Pageable<I, O> {

    List<O> getPagedData(Session me, I from, I to) throws ApiException;
}
