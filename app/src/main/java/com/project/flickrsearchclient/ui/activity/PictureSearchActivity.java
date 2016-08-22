package com.project.flickrsearchclient.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.project.flickrsearchclient.R;
import com.project.flickrsearchclient.ui.fragment.SearchResultsFragment;


public class PictureSearchActivity extends AppCompatActivity {

    public static final String EXTRA_SEARCH_QUERY = "searchQuery";
    private static final int REQUEST_CODE = 73;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);
        setTitle(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.history) {
            showHistory();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showHistory() {
        startActivityForResult(new Intent(this, HistoryActivity.class), REQUEST_CODE);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            ((SearchResultsFragment) getSupportFragmentManager().
                    findFragmentById(R.id.search_results_fragment)).
                    query(data.getStringExtra(EXTRA_SEARCH_QUERY));
        }
    }
}
