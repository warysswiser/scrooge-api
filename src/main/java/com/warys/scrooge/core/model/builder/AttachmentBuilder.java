package com.warys.scrooge.core.model.builder;

import com.warys.scrooge.core.model.budget.Attachment;

import java.time.LocalDateTime;
import java.util.function.Consumer;

public final class AttachmentBuilder implements ModelBuilder<AttachmentBuilder, Attachment> {
    public String filename;
    public String fileType;
    public String uri;
    public LocalDateTime creationDate;

    public AttachmentBuilder with(Consumer<AttachmentBuilder> builderFunction) {
        builderFunction.accept(this);
        return this;
    }

    public Attachment build() {
        Attachment attachment = new Attachment();
        attachment.setFilename(filename);
        attachment.setFileType(fileType);
        attachment.setUri(uri);
        attachment.setCreationDate(creationDate);
        return attachment;
    }
}
