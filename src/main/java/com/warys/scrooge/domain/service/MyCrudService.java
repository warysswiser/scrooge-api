package com.warys.scrooge.domain.service;

import com.warys.scrooge.domain.model.user.Session;
import com.warys.scrooge.infrastructure.exception.ApiException;

import java.util.List;

public interface MyCrudService<I, O> {

    O retrieve(Session me, String itemId) throws ApiException;

    O create(Session me, I payload) throws ApiException;

    void remove(Session me, String itemId) throws ApiException;

    O partialUpdate(Session me, String itemId, I partialPayload) throws ApiException;

    O update(Session me, String itemId, I payload) throws ApiException;

    List<O> getAll(Session me) throws ApiException;
}
