package com.warys.scrooge.core.service.budget.attachement;

import com.warys.scrooge.core.model.ImageTextDetection;
import com.warys.scrooge.core.model.budget.Attachment;
import com.warys.scrooge.core.model.builder.AttachmentBuilder;
import com.warys.scrooge.core.model.user.SessionUser;
import com.warys.scrooge.core.repository.AttachmentRepository;
import com.warys.scrooge.infrastructure.exception.ApiException;
import com.warys.scrooge.infrastructure.exception.business.InconsistentElementException;
import com.warys.scrooge.infrastructure.exception.technical.TechnicalException;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class AttachmentService implements CrudAttachmentService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AttachmentService.class);

    private AttachmentRepository attachmentRepository;

    @Autowired
    public AttachmentService(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    public void check(Attachment attachment) throws ApiException {
        Objects.requireNonNull(attachment, "Non null attachment must be given");
        if (!attachment.hasBeenUploadedYet()) {
            throw new InconsistentElementException("Attachment provided have not been uploaded yet. " +
                    "Attachment must result from [POST /me/attachment] endpoint." +
                    "However Attachment is optional.");
        }
    }

    @Override
    public String createAttachment(SessionUser me, MultipartFile file) throws TechnicalException {
        Objects.requireNonNull(file, "Non null MultipartFile must be given");
        try {
            Binary content = new Binary(BsonBinarySubType.BINARY, file.getBytes());
            final Attachment attachment = new AttachmentBuilder()
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
            logger.error("could not get bytes", e);
            throw new TechnicalException(e);
        }
    }

    @Override
    public List<ImageTextDetection> detectText(SessionUser me, MultipartFile file) throws TechnicalException {
        try {
            return ImageDetector.detectText(file.getInputStream());
        } catch (Exception e) {
            logger.error("could not get bytes", e);
            throw new TechnicalException(e);
        }
    }

}
