package com.aecb.data.api.models.getfile;

import com.aecb.data.api.models.BaseResponse;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class GetFileResponse extends BaseResponse {

    @SerializedName("data")
    private FileItem data;

    public FileItem getData() {
        return data;
    }

    public void setData(FileItem data) {
        this.data = data;
    }
}