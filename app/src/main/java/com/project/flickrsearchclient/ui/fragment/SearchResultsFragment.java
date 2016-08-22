package com.project.flickrsearchclient.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.project.flickrsearchclient.R;
import com.project.flickrsearchclient.application.ApplicationComponent;
import com.project.flickrsearchclient.application.FlickrSearchApplication;
import com.project.flickrsearchclient.model.Photo;
import com.project.flickrsearchclient.model.SearchQuery;
import com.project.flickrsearchclient.model.SearchResult;
import com.project.flickrsearchclient.network.ApiClient;
import com.project.flickrsearchclient.repo.SearchRepository;
import com.project.flickrsearchclient.ui.recycler.EndlessScrollListener;
import com.project.flickrsearchclient.ui.recycler.SearchResultAdapter;
import com.project.flickrsearchclient.ui.recycler.SpacingDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResultsFragment extends Fragment implements SearchView.OnQueryTextListener {

    private static final int COLUMNS_COUNT = 3;
    private static final int INITIAL_PAGE = 1;

    private static final String STATE_CURRENT_PAGE = "currentPage";
    private static final String STATE_ITEMS = "items";
    private static final String STATE_QUERY = "query";

    @Inject
    ApiClient mApiClient;

    @Inject
    SearchRepository mSearchRepository;

    @BindView(R.id.result_recyclerview)
    RecyclerView mResultRecyclerView;

    @BindView(R.id.progress)
    ProgressBar mProgressBar;

    private SearchResultAdapter mSearchResultAdapter;
    private SearchView mSearchView;

    private Unbinder mUnbinder;
    private String mQuery;
    private boolean mShouldSubmit;
    private int mCurrentPage;
    private List<Photo> mPhotos;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        getApplicationComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            mCurrentPage = INITIAL_PAGE;
        } else {
            mCurrentPage = savedInstanceState.getInt(STATE_CURRENT_PAGE);
            mQuery = savedInstanceState.getString(STATE_QUERY);
            mShouldSubmit = false;
            mPhotos = savedInstanceState.getParcelableArrayList(STATE_ITEMS);
        }

        setupRecyclerView();
    }


    private void setupRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), COLUMNS_COUNT);
        mResultRecyclerView.setLayoutManager(gridLayoutManager);
        mResultRecyclerView.addItemDecoration(
                new SpacingDecoration(getResources().getDimensionPixelSize(R.dimen.item_spacing)));

        mSearchResultAdapter = new SearchResultAdapter();
        if (mPhotos != null) {
            mSearchResultAdapter.addItems(mPhotos);
        }
        mResultRecyclerView.setAdapter(mSearchResultAdapter);
        mResultRecyclerView.addOnScrollListener(new EndlessScrollListener(gridLayoutManager, INITIAL_PAGE) {

            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                loadPictures(page);
                return true;
            }
        });
    }

    private void startSearch(String query) {
        mQuery = query;
        mSearchResultAdapter.clean();
        loadPictures(INITIAL_PAGE);
    }

    private void loadPictures(final int page) {
        showProgress(true);
        mApiClient.getSearchResult(mQuery, page).enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                showProgress(false);
                if (response.isSuccessful()) {
                    SearchResult searchResult = response.body();
                    if (searchResult.isSuccessful()) {
                        List<Photo> result = searchResult.getPhotos().getPhotoList();
                        if (page > INITIAL_PAGE) {
                            mSearchResultAdapter.addItems(result);
                        } else {
                            mSearchResultAdapter.setItems(result);

                        mPhotos = mSearchResultAdapter.getPhotos();       }

                    }
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                showProgress(false);
                Toast.makeText(getContext(), R.string.loading_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProgress(boolean shouldShow) {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(shouldShow ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setIconifiedByDefault(false);

        if (!TextUtils.isEmpty(mQuery)) {
            mSearchView.setQuery(mQuery, mShouldSubmit);
        } else {
            mSearchView.requestFocus();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(STATE_QUERY, mQuery);
        outState.putInt(STATE_CURRENT_PAGE, mCurrentPage);

        if (mPhotos != null) {
            outState.putParcelableArrayList(STATE_ITEMS, new ArrayList<Parcelable>(mPhotos));
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        startSearch(query);
        mSearchRepository.add(new SearchQuery(query));
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public void query(String query) {
        if (mSearchView != null) {
            mSearchView.setQuery(query, true);
        } else {
            mQuery = query;
            mShouldSubmit = true;
        }
    }

    private ApplicationComponent getApplicationComponent() {
        return ((FlickrSearchApplication) getActivity().getApplication()).getApplicationComponent();
    }
}
