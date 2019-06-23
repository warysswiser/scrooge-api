package com.warys.scrooge.core.service.budget.attachement;

import com.warys.scrooge.core.model.budget.Attachment;
import com.warys.scrooge.core.model.user.SessionUser;
import com.warys.scrooge.core.service.MyCrudService;
import com.warys.scrooge.infrastructure.exception.ApiException;
import com.warys.scrooge.infrastructure.exception.technical.TechnicalException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CrudAttachmentService extends MyCrudService<MultipartFile, Attachment> {

    void check(Attachment attachment) throws ApiException;

    @Override
    default Attachment retrieve(SessionUser me, String itemId) {
        throw new UnsupportedOperationException();
    }

    @Override
    default Attachment create(SessionUser me, MultipartFile payload) throws TechnicalException {
        throw new UnsupportedOperationException();
    }

    @Override
    default void remove(SessionUser me, String itemId) {
        throw new UnsupportedOperationException();
    }

    @Override
    default Attachment update(SessionUser me, String itemId, MultipartFile payload) {
        throw new UnsupportedOperationException();
    }

    @Override
    default Attachment partialUpdate(SessionUser me, String itemId, MultipartFile partialPayload) {
        throw new UnsupportedOperationException();
    }

    @Override
    default List<Attachment> getAll(SessionUser me) {
        throw new UnsupportedOperationException();
    }
}
