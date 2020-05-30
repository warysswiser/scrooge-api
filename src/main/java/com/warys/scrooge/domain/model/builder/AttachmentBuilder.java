package com.warys.scrooge.domain.model.builder;

import com.warys.scrooge.infrastructure.repository.mongo.entity.AttachmentDocument;
import org.bson.types.Binary;

import java.time.LocalDateTime;
import java.util.function.Consumer;

public final class AttachmentBuilder implements ModelBuilder<AttachmentBuilder, AttachmentDocument> {
    public String filename;
    public String fileType;
    public String ownerId;
    public Binary content;
    public LocalDateTime creationDate;

    public AttachmentBuilder with(Consumer<AttachmentBuilder> builderFunction) {
        builderFunction.accept(this);
        return this;
    }

    public AttachmentDocument build() {
        AttachmentDocument attachment = new AttachmentDocument();
        attachment.setFilename(filename);
        attachment.setFileType(fileType);
        attachment.setOwnerId(ownerId);
        attachment.setContent(content);
        attachment.setCreationDate(creationDate);
        return attachment;
    }
}
