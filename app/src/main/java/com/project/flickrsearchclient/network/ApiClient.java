package com.project.flickrsearchclient.network;

import com.project.flickrsearchclient.model.SearchResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiClient {

    @GET("services/rest/?method=flickr.photos.search&format=json&nojsoncallback=1&safe_search=1")
    Call<SearchResult> getSearchResult(@Query("text") String text, @Query("page") int page);
}
