package com.warys.scrooge.domain.service.budget.attachement;

import com.warys.scrooge.infrastructure.repository.mongo.entity.AttachmentDocument;
import com.warys.scrooge.domain.model.user.Session;
import com.warys.scrooge.domain.service.MyCrudService;
import com.warys.scrooge.infrastructure.exception.ApiException;
import com.warys.scrooge.infrastructure.exception.technical.TechnicalException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CrudAttachmentService extends MyCrudService<MultipartFile, AttachmentDocument> {

    void check(AttachmentDocument attachment) throws ApiException;

    @Override
    default AttachmentDocument retrieve(Session me, String itemId) {
        throw new UnsupportedOperationException();
    }

    @Override
    default AttachmentDocument create(Session me, MultipartFile payload) {
        throw new UnsupportedOperationException();
    }

    default String createAttachment(Session me, MultipartFile payload) throws TechnicalException {
        throw new UnsupportedOperationException();
    }

    @Override
    default void remove(Session me, String itemId) {
        throw new UnsupportedOperationException();
    }

    @Override
    default AttachmentDocument update(Session me, String itemId, MultipartFile payload) {
        throw new UnsupportedOperationException();
    }

    @Override
    default AttachmentDocument partialUpdate(Session me, String itemId, MultipartFile partialPayload) {
        throw new UnsupportedOperationException();
    }

    @Override
    default List<AttachmentDocument> getAll(Session me) {
        throw new UnsupportedOperationException();
    }

}
