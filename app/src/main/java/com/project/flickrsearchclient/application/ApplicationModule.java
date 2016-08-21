package com.project.flickrsearchclient.application;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module that provides objects which will live during the application lifecycle.
 */
@Module
public class ApplicationModule {
    private final FlickrSearchApplication application;

    public ApplicationModule(FlickrSearchApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return this.application;
    }

    @Provides
    @Singleton
    ExecutorService provideThreadExecutor() {
        return Executors.newSingleThreadExecutor();
    }
}
