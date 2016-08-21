package com.project.flickrsearchclient.repo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class SearchDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "search.db";

    private static final String CREATE_SEARCH_ENTRY =
            "CREATE TABLE " + SearchEntry.TABLE_NAME + " (" +
            SearchEntry.COLUMN_ID + " INT PRIMARY KEY," +
            SearchEntry.COLUMN_QUERY + " TEXT, " +
            SearchEntry.COLUMN_TIMESTAMP + " INT )";

    public SearchDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SEARCH_ENTRY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    void insertSearchEntry(SearchEntry searchEntry) {
        getWritableDatabase().insert(SearchEntry.TABLE_NAME, null, searchEntry.toContentValues());
    }

    List<SearchEntry> getSearchEntries() {
        Cursor cursor = getReadableDatabase().query(SearchEntry.TABLE_NAME,
                new String[] {SearchEntry.COLUMN_QUERY, SearchEntry.COLUMN_TIMESTAMP},
                null, null, null, null, SearchEntry.COLUMN_TIMESTAMP + " DESC");
        if (cursor != null) {
            List<SearchEntry> searchEntries = new ArrayList<>(cursor.getColumnCount());
            try {
                int indexQuery = cursor.getColumnIndex(SearchEntry.COLUMN_QUERY);
                int indexTimestamp = cursor.getColumnIndex(SearchEntry.COLUMN_TIMESTAMP);
                while(cursor.moveToNext()) {
                    searchEntries.add(new SearchEntry(
                            cursor.getString(indexQuery),
                            cursor.getLong(indexTimestamp))
                    );
                }
                return searchEntries;
            } finally {
                cursor.close();
            }
        }

        return Collections.EMPTY_LIST;
    }

    void deleteSearchEntries() {
        getWritableDatabase().delete(SearchEntry.TABLE_NAME, null, null);
    }
}
