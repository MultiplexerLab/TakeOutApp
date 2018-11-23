package multiplexer.lab.takeout.ItemActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import multiplexer.lab.takeout.MainActivity;
import multiplexer.lab.takeout.R;


public class AddReferralActivity extends AppCompatActivity {

    static int value;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_referral);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        text = findViewById(R.id.TV_referral);
        Intent intent = getIntent();
        value= intent.getIntExtra("val",0);
        if(value==1){
            text.setText("Use Code to Activate your Account.");
        }else{
            text.setText("Use Code to Earn Points.");
        }
    }

    public void btnAddRef(View view) {
        Intent intent = new Intent(AddReferralActivity.this, MainActivity.class);
        intent.putExtra("val",value);
        startActivity(intent);
        finish();
    }
}
