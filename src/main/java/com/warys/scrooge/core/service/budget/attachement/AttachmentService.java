package com.warys.scrooge.core.service.budget.attachement;

import com.warys.scrooge.core.model.budget.Attachment;
import com.warys.scrooge.core.model.builder.AttachmentBuilder;
import com.warys.scrooge.core.model.user.SessionUser;
import com.warys.scrooge.infrastructure.exception.ApiException;
import com.warys.scrooge.infrastructure.exception.business.InconsistentElementException;
import com.warys.scrooge.infrastructure.exception.technical.TechnicalException;
import lombok.AllArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@Service
public class AttachmentService implements CrudAttachmentService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AttachmentService.class);

    @Value("${app.attachments.directory}")
    private String uploadedFolder;

    public AttachmentService() {}

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
    public Attachment create(SessionUser me, MultipartFile file) throws TechnicalException {
        Objects.requireNonNull(file, "Non null MultipartFile must be given");
        try {
            var userDir = Paths.get(uploadedFolder, me.getId());

            if (!Files.exists(userDir)) {
                Files.createDirectories(userDir);
            }

            final Path destinationPath = userDir.resolve(Objects.requireNonNull(file.getOriginalFilename()));
            Path createdFile = Files.write(destinationPath, file.getBytes());
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
