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
import com.project.flickrsearchclient.ui.adapter.EndlessScrollListener;
import com.project.flickrsearchclient.ui.adapter.SearchResultAdapter;
import com.project.flickrsearchclient.ui.adapter.SpacingDecoration;

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

    private static final String STATE_LAYOUT_MANAGER = "layoutManager";
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

    private Unbinder mUnbinder;
    private SearchResultAdapter mSearchResultAdapter;
    private String mQuery;
    private int mCurrentPage;

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
        }

        setupRecyclerView(savedInstanceState);
    }

    private void setupRecyclerView(@Nullable Bundle savedInstanceState) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), COLUMNS_COUNT);
        if (savedInstanceState != null) {
            gridLayoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(STATE_LAYOUT_MANAGER));
        }
        mResultRecyclerView.setLayoutManager(gridLayoutManager);
        mResultRecyclerView.addItemDecoration(
                new SpacingDecoration(getResources().getDimensionPixelSize(R.dimen.item_spacing)));

        mSearchResultAdapter = new SearchResultAdapter();
        if (savedInstanceState != null) {
            List<Photo> photos = savedInstanceState.getParcelableArrayList(STATE_ITEMS);
            mSearchResultAdapter.addItems(photos);
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
                        }

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
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        searchView.setIconifiedByDefault(false);

        if (!TextUtils.isEmpty(mQuery)) {
            searchView.setQuery(mQuery, false);
        } else {
            searchView.requestFocus();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.history) {
            showHistoryFragment();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showHistoryFragment() {
        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.history_container, HistoryFragment.getInstance())
                .addToBackStack(null)
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .commitAllowingStateLoss();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(STATE_QUERY, mQuery);
        outState.putInt(STATE_CURRENT_PAGE, mCurrentPage);

        Parcelable listState = mResultRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(STATE_LAYOUT_MANAGER, listState);

        outState.putParcelableArrayList(STATE_ITEMS,
                new ArrayList<Parcelable>(mSearchResultAdapter.getPhotos()));
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

    private ApplicationComponent getApplicationComponent() {
        return ((FlickrSearchApplication) getActivity().getApplication()).getApplicationComponent();
    }
}
