package com.warys.scrooge.infrastructure.repository.mongo.entity;

import com.warys.scrooge.domain.model.GenericModel;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.Binary;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Document(collection = "attachments")
public class AttachmentDocument extends GenericModel {

    @NotEmpty
    private String ownerId;
    @NotEmpty
    private String filename;
    @NotEmpty
    private String fileType;
    @NotEmpty
    private Binary content;

    public boolean hasBeenUploadedYet() {
        return (getCreationDate() != null || getContent() != null);
    }
}
