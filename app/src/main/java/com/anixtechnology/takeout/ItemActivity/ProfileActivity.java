package com.anixtechnology.takeout.ItemActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.anixtechnology.takeout.R;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
