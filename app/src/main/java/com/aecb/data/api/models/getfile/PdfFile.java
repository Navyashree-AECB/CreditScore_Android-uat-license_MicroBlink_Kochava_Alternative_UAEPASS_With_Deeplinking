package com.aecb.data.api.models.getfile;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class PdfFile {

    @SerializedName("__formattedValues")
    private FormattedValues formattedValues;

    @SerializedName("filename")
    private String filename;

    @SerializedName("subject")
    private String subject;

    @SerializedName("mimetype")
    private String mimetype;

    @SerializedName("documentbody")
    private String documentbody;

    @SerializedName("annotationid")
    private String annotationid;

    @SerializedName("createdon")
    private String createdon;

    public FormattedValues getFormattedValues() {
        return formattedValues;
    }

    public void setFormattedValues(FormattedValues formattedValues) {
        this.formattedValues = formattedValues;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public String getDocumentbody() {
        return documentbody;
    }

    public void setDocumentbody(String documentbody) {
        this.documentbody = documentbody;
    }

    public String getAnnotationid() {
        return annotationid;
    }

    public void setAnnotationid(String annotationid) {
        this.annotationid = annotationid;
    }

    public String getCreatedon() {
        return createdon;
    }

    public void setCreatedon(String createdon) {
        this.createdon = createdon;
    }

    @Override
    public String toString() {
        return
                "File{" +
                        "__formattedValues = '" + formattedValues + '\'' +
                        ",filename = '" + filename + '\'' +
                        ",subject = '" + subject + '\'' +
                        ",mimetype = '" + mimetype + '\'' +
                        ",documentbody = '" + documentbody + '\'' +
                        ",annotationid = '" + annotationid + '\'' +
                        ",createdon = '" + createdon + '\'' +
                        "}";
    }
}