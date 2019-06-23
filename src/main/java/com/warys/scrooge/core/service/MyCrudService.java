package com.warys.scrooge.core.service;

import com.warys.scrooge.core.model.user.SessionUser;
import com.warys.scrooge.infrastructure.exception.ApiException;

import java.util.List;

public interface MyCrudService<I, O> {

    O retrieve(SessionUser me, String itemId) throws ApiException;

    O create(SessionUser me, I payload) throws ApiException;

    void remove(SessionUser me, String itemId) throws ApiException;

    O partialUpdate(SessionUser me, String itemId, I partialPayload) throws ApiException;

    O update(SessionUser me, String itemId, I payload) throws ApiException;

    List<O> getAll(SessionUser me) throws ApiException;
}
