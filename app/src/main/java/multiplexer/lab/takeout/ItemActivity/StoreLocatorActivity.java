package multiplexer.lab.takeout.ItemActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import multiplexer.lab.takeout.R;


public class StoreLocatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_locator);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
