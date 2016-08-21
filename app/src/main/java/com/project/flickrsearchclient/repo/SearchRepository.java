package com.project.flickrsearchclient.repo;

import android.content.Context;

import com.project.flickrsearchclient.model.SearchQuery;

import java.util.ArrayList;
import java.util.List;

public class SearchRepository {

    private final SearchDatabaseHelper mSearchDatabaseHelper;

    public static SearchRepository getInstance(Context context) {
        SearchDatabaseHelper searchDatabaseHelper = new SearchDatabaseHelper(context);
        return new SearchRepository(searchDatabaseHelper);
    }

    private SearchRepository(SearchDatabaseHelper searchDatabaseHelper) {
        mSearchDatabaseHelper = searchDatabaseHelper;
    }

    public void add(SearchQuery searchQuery) {
        mSearchDatabaseHelper.insertSearchEntry(toSearchEntry(searchQuery));
    }

    public List<SearchQuery> getAll() {
        List<SearchEntry> searchEntries = mSearchDatabaseHelper.getSearchEntries();
        List<SearchQuery> searchQueries = new ArrayList<>(searchEntries.size());
        for (SearchEntry searchEntry : searchEntries) {
            searchQueries.add(toSearchQuery(searchEntry));
        }
        return searchQueries;
    }

    private static SearchEntry toSearchEntry(SearchQuery searchQuery) {
        return new SearchEntry(searchQuery.getQuery(), searchQuery.getTimestamp());
    }

    private static SearchQuery toSearchQuery(SearchEntry searchEntry) {
        return new SearchQuery(searchEntry.getQuery(), searchEntry.getTimestamp());
    }
}
