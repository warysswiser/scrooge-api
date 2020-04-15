package com.warys.scrooge.core.model.budget;

import com.warys.scrooge.core.model.GenericModel;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.Binary;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Document(collection = "attachments")
public class Attachment extends GenericModel {

    @NotEmpty
    private String filename;
    @NotEmpty
    private String fileType;
    @NotEmpty
    private Binary content;
    @NotEmpty
    private String ownerId;

    public boolean hasBeenUploadedYet() {
        return (getCreationDate() != null || getContent() != null);
    }
}
