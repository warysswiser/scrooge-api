package com.warys.scrooge.core.service.budget.attachement;

import com.warys.scrooge.core.model.budget.Attachment;
import com.warys.scrooge.core.model.builder.AttachmentBuilder;
import com.warys.scrooge.core.model.user.User;
import com.warys.scrooge.infrastructure.exception.business.InconsistentElementException;
import com.warys.scrooge.infrastructure.exception.technical.TechnicalException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Service
public class AttachmentService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AttachmentService.class);

    @Value("${app.attachments.directory}")
    private String uploadedFolder;

    public void check(Attachment attachment) throws InconsistentElementException {
        if (attachment != null && attachment.getCreationDate() != null
                && attachment.getUri() != null) {
            File file = new File(attachment.getUri());
            if (!file.exists() || !file.isFile()) {
                throw new InconsistentElementException("Attachment provided have not been uploaded yet. " +
                        "Attachment must result from [POST /me/attachment] endpoint." +
                        "However Attachment is optional.");
            }
        }
    }

    public Attachment createAttachment(User me, MultipartFile file) throws TechnicalException {
        try {
            var userDir = Paths.get(uploadedFolder, me.getId());

            if (!Files.exists(userDir)) {
                Files.createDirectories(userDir);
            }

            Path createdFile = Files.write(userDir.resolve(file.getOriginalFilename()), file.getBytes());
            return new AttachmentBuilder()
                    .with(
                            o -> {
                                o.creationDate = LocalDateTime.now();
                                o.filename = file.getOriginalFilename();
                                o.fileType = file.getContentType();
                                o.uri = createdFile.toUri().getPath();
                            }
                    ).build();
        } catch (IOException e) {
            logger.error("could not get bytes", e);
            throw new TechnicalException(e);
        }
    }

}
