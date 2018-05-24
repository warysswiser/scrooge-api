package com.warys.scrooge.core.model.budget;

import com.warys.scrooge.core.model.GenericModel;

import javax.validation.constraints.NotEmpty;

public class Attachment extends GenericModel {

    @NotEmpty
    private String filename;
    @NotEmpty
    private String fileType;
    @NotEmpty
    private String uri;

    public Attachment() {
        super();
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
