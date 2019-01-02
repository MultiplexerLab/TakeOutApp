package multiplexer.lab.takeout.ItemActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import multiplexer.lab.takeout.Helper.EndPoints;
import multiplexer.lab.takeout.R;

public class ProfileActivity extends AppCompatActivity {

    EditText activationcode, fullName, email, phoneno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        activationcode = findViewById(R.id.ET_activationcode);
        fullName = findViewById(R.id.fullName);
        email = findViewById(R.id.profileEmail);
        phoneno = findViewById(R.id.phoneNo);

        setInfo();


    }

    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        return true;
    }

    private void setInfo() {
        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
        String Name = pref.getString("fullname", "");
        String Email = pref.getString("email", "");
        String Phone = pref.getString("phone", "");
        String Status = pref.getString("status", "");

        fullName.setText(Name);
        email.setText(Email);
        phoneno.setText(Phone);
        if (Status.isEmpty()) {
            activationcode.setText("Your Account is not Activated yet");
            activationcode.setTextColor(this.getResources().getColor(R.color.red));
        } else {
            activationcode.setText(Status);

        }

    }

    public void btnShareCode(View view) {

        if (!activationcode.getText().toString().isEmpty()) {
            String code = activationcode.getText().toString();
            Uri uri = Uri.parse("smsto:");
            Intent it = new Intent(Intent.ACTION_SENDTO, uri);
            it.putExtra("sms_body", "To Activate your TakeOut account, Use this code: " + code);
            startActivity(it);
        } else {
            Toast.makeText(getApplicationContext(), "Please Activate Your Account First", Toast.LENGTH_LONG).show();
        }


    }


}
