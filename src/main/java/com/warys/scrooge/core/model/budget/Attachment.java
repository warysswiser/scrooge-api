package com.warys.scrooge.core.model.budget;

import com.warys.scrooge.core.model.GenericModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.io.File;

@NoArgsConstructor
@Getter
@Setter
public class Attachment extends GenericModel {

    @NotEmpty
    private String filename;
    @NotEmpty
    private String fileType;
    @NotEmpty
    private String uri;

    public boolean hasBeenUploadedYet() {
        if (getCreationDate() == null || getUri() == null) {
            return false;
        }
        File file = new File(getUri());
        return file.exists() && file.isFile();
    }
}
