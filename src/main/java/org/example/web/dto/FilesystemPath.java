package org.example.web.dto;

import javax.validation.constraints.NotEmpty;

public class FilesystemPath {

    @NotEmpty
    private String path;

    public FilesystemPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
