package com.aecb.data.api.models.getfile;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class FileItem {

    @SerializedName("file")
    private PdfFile pdfFile;

    public PdfFile getFile() {
        return pdfFile;
    }

    public void setFile(PdfFile file) {
        this.pdfFile = file;
    }

}