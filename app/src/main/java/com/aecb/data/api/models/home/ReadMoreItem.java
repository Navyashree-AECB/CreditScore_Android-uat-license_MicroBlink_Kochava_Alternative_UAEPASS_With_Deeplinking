package com.aecb.data.api.models.home;

public class ReadMoreItem {
    private String readMoreDesc;
    private String readMoreLink;

    public ReadMoreItem(String readMoreDesc, String readMoreLink) {
        this.readMoreDesc = readMoreDesc;
        this.readMoreLink = readMoreLink;
    }

    public String getReadMoreDesc() {
        return readMoreDesc;
    }

    public void setReadMoreDesc(String readMoreDesc) {
        this.readMoreDesc = readMoreDesc;
    }

    public String getReadMoreLink() {
        return readMoreLink;
    }

    public void setReadMoreLink(String readMoreLink) {
        this.readMoreLink = readMoreLink;
    }
}
