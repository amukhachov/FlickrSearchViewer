package com.project.flickrsearchclient.repo;

import android.content.ContentValues;
import android.provider.BaseColumns;

class SearchEntry {

    static final String TABLE_NAME = "search_story";

    static final String COLUMN_ID = BaseColumns._ID;
    static final String COLUMN_QUERY = "query";
    static final String COLUMN_TIMESTAMP = "timestamp";

    private final String mQuery;
    private final long mTimestamp;

    public SearchEntry(String query, long timestamp) {
        mQuery = query;
        mTimestamp = timestamp;
    }

    public String getQuery() {
        return mQuery;
    }

    public long getTimestamp() {
        return mTimestamp;
    }

    ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_QUERY, mQuery);
        contentValues.put(COLUMN_TIMESTAMP, mTimestamp);
        return contentValues;
    }
}
