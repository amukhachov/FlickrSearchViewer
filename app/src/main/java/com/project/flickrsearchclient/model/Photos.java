package com.project.flickrsearchclient.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Photos {
    @SerializedName("photo")
    private List<Photo> photo = new ArrayList<>();

    public List<Photo> getPhotoList() {
        return photo;
    }

}