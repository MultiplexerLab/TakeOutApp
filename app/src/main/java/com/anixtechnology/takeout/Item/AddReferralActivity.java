package com.anixtechnology.takeout.Item;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.anixtechnology.takeout.LogInActivity;
import com.anixtechnology.takeout.MainActivity;
import com.anixtechnology.takeout.R;

public class AddReferralActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_referral);
    }

    public void BtnAddRef(View view) {
        Intent intent = new Intent(AddReferralActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
