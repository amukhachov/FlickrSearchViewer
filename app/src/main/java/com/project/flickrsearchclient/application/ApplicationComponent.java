package com.project.flickrsearchclient.application;

import android.content.Context;

import com.project.flickrsearchclient.network.ApiClient;
import com.project.flickrsearchclient.network.ApiModule;
import com.project.flickrsearchclient.ui.activity.PictureSearchActivity;
import com.project.flickrsearchclient.ui.fragment.HistoryFragment;
import com.project.flickrsearchclient.ui.fragment.SearchResultsFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, ApiModule.class})
public interface ApplicationComponent {

    void inject(SearchResultsFragment fragment);

    void inject(HistoryFragment fragment);

    Context context();

    ApiClient apiClient();
}
