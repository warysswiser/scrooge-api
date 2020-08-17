package com.warys.scrooge.domain.service.budget.attachement;

import com.warys.scrooge.domain.exception.InconsistentElementException;
import com.warys.scrooge.domain.model.builder.AttachmentBuilder;
import com.warys.scrooge.domain.model.user.Session;
import com.warys.scrooge.infrastructure.exception.ApiException;
import com.warys.scrooge.infrastructure.exception.technical.TechnicalException;
import com.warys.scrooge.infrastructure.repository.mongo.AttachmentRepository;
import com.warys.scrooge.infrastructure.repository.mongo.entity.AttachmentDocument;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class AttachmentService implements CrudAttachmentService {

    private final AttachmentRepository attachmentRepository;

    @Autowired
    public AttachmentService(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    public void check(AttachmentDocument attachment) throws ApiException {
        Objects.requireNonNull(attachment, "Non null attachment must be given");
        if (!attachment.hasBeenUploadedYet()) {
            throw new InconsistentElementException("AttachmentDocument provided have not been uploaded yet. " +
                    "AttachmentDocument must result from [POST /me/attachment] endpoint." +
                    "However AttachmentDocument is optional.");
        }
    }

    @Override
    public String createAttachment(Session me, MultipartFile file) throws TechnicalException {
        Objects.requireNonNull(file, "Non null MultipartFile must be given");
        try {
            Binary content = new Binary(BsonBinarySubType.BINARY, file.getBytes());
            final AttachmentDocument attachment = new AttachmentBuilder()
                    .with(
                            o -> {
                                o.creationDate = LocalDateTime.now();
                                o.filename = file.getOriginalFilename();
                                o.fileType = file.getContentType();
                                o.ownerId = me.getId();
                                o.content = content;
                            }
                    ).build();

            return attachmentRepository.insert(attachment).getId();

        } catch (IOException e) {
            throw new TechnicalException("could not get bytes", e);
        }
    }

}
