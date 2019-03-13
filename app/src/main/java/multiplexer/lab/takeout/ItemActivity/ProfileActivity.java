package multiplexer.lab.takeout.ItemActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

    EditText email, phoneno;
    TextView activationcode, fullName;
    RequestQueue queue;
    ImageView proPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        queue = Volley.newRequestQueue(this);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        activationcode = findViewById(R.id.ET_activationcode);
        fullName = findViewById(R.id.fullName);
        email = findViewById(R.id.profileEmail);
        phoneno = findViewById(R.id.phoneNo);
        proPic = findViewById(R.id.IV_pro_pic);

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
        boolean isValid = pref.getBoolean("isValid", false);

        fullName.setText(Name);
        email.setText(Email);
        phoneno.setText(Phone);
        if (isValid == false) {
            activationcode.setText("Your Account is not Activated yet");
            activationcode.setTextColor(this.getResources().getColor(R.color.red));
        } else {
            getReferralCode();
        }
    }

    private void getReferralCode() {
        StringRequest pointRequest = new StringRequest(Request.Method.GET, EndPoints.GET_CHECK_REFERENCE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("referralCode", response);
                editor.commit();
                activationcode.setText(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ParseError", error.toString());
                Toast.makeText(getApplicationContext(), getString(R.string.ToastWait), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
                String accessToken = pref.getString("accessToken", "");
                Log.i("accessToken", accessToken);
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + accessToken);
                return params;
            }
        };
        queue.add(pointRequest);

    }

    private void setAvatar() {

        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
        String avatar = pref.getString("Avatar", "male");
        if (avatar.equalsIgnoreCase("male")) {
            proPic.setImageResource(R.drawable.male);
        } else {
            proPic.setImageResource(R.drawable.female);
        }

    }

    public void btnShareCode(View view) {
        String code = activationcode.getText().toString();
        if (code.contains("Your Account is not Activated yet")) {
            Toast.makeText(getApplicationContext(), "Please Activate Your Account First", Toast.LENGTH_LONG).show();
            return;
        } else {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "To Activate your TakeOut account, Use this code: " + code);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }
    }
}
