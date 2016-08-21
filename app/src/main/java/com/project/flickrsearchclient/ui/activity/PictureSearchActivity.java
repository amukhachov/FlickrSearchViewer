package com.project.flickrsearchclient.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.project.flickrsearchclient.R;

public class PictureSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);
        setTitle(null);
    }

}
