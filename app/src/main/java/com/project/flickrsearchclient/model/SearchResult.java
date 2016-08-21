package com.project.flickrsearchclient.model;

import com.google.gson.annotations.SerializedName;

public class SearchResult {
    private static final String SUCCESSFUL_STATUS = "ok";

    @SerializedName("photos")
    private Photos photos;
    @SerializedName("stat")
    private String stat;

    public Photos getPhotos() {
        return photos;
    }

    public boolean isSuccessful() {
        return SUCCESSFUL_STATUS.equals(stat);
    }
}
