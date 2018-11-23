package multiplexer.lab.takeout.ItemActivity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import multiplexer.lab.takeout.R;

public class ProfileActivity extends AppCompatActivity {

    EditText activationcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        activationcode = findViewById(R.id.ET_activationcode);
    }

    public void btnShareCode(View view) {
        String code = activationcode.getText().toString();
        Uri uri = Uri.parse("smsto:");
        Intent it = new Intent(Intent.ACTION_SENDTO,uri);
        it.putExtra("sms_body", "To Activate your TakeOut account, Use this code: "+code);
        startActivity(it);
    }
}
