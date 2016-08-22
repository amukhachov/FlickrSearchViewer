package com.project.flickrsearchclient.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.project.flickrsearchclient.application.ApplicationComponent;
import com.project.flickrsearchclient.application.FlickrSearchApplication;
import com.project.flickrsearchclient.model.SearchQuery;
import com.project.flickrsearchclient.repo.SearchRepository;
import com.project.flickrsearchclient.ui.activity.PictureSearchActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class HistoryFragment extends ListFragment {

    private static final String STATE_RECENT_LIST = "recent_list";

    private static final String TITLE = "title";
    private static final String DATE = "date";

    @Inject
    SearchRepository mSearchRepository;

    List<SearchQuery> mSearchQueries;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        getApplicationComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            mSearchQueries = mSearchRepository.getAll();
        } else {
            mSearchQueries = savedInstanceState.getParcelableArrayList(STATE_RECENT_LIST);
        }

        getListView().setAdapter(createAdapter(mSearchQueries));
        setListShown(true);
    }

    private ListAdapter createAdapter(List<SearchQuery> searchQueries) {
        List<Map<String, String>> data = new ArrayList<>(searchQueries.size());
        for (SearchQuery searchQuery : searchQueries) {
            Map<String, String> dataMap = new HashMap<>(2);
            dataMap.put(TITLE, searchQuery.getQuery());
            dataMap.put(DATE, searchQuery.getReadableDate());
            data.add(dataMap);
        }
        return new SimpleAdapter(getContext(), data,
                android.R.layout.simple_list_item_2,
                new String[] {TITLE, DATE},
                new int[] {android.R.id.text1,
                        android.R.id.text2});
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Activity activity = getActivity();
        activity.setResult(Activity.RESULT_OK,
                new Intent().
                        putExtra(PictureSearchActivity.EXTRA_SEARCH_QUERY,
                                mSearchQueries.get(position).getQuery())
        );
        activity.finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(STATE_RECENT_LIST,
                new ArrayList<>(mSearchQueries));
    }

    private ApplicationComponent getApplicationComponent() {
        return ((FlickrSearchApplication) getActivity().getApplication()).getApplicationComponent();
    }
}
