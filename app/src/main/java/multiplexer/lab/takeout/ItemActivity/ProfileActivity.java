package multiplexer.lab.takeout.ItemActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

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

    EditText activationcode, fullName, email, phoneno, address;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        queue = Volley.newRequestQueue(this);

        activationcode = findViewById(R.id.ET_activationcode);
        fullName = findViewById(R.id.fullName);
        email = findViewById(R.id.profileEmail);
        phoneno = findViewById(R.id.phoneNo);
        address = findViewById(R.id.address);

        getProfileData();
    }

    public void btnShareCode(View view) {
        String code = activationcode.getText().toString();
        Uri uri = Uri.parse("smsto:");
        Intent it = new Intent(Intent.ACTION_SENDTO,uri);
        it.putExtra("sms_body", "To Activate your TakeOut account, Use this code: "+code);
        startActivity(it);
    }

    private void getProfileData() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, EndPoints.GET_PROFILE_DATA, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("responseprofile",response.toString());
                try {
                    fullName.setText(response.getString("Fullname"));
                    email.setText(response.getString("Email"));
                    phoneno.setText(response.getString("Phone"));
                    phoneno.setText(response.getString("Address"));

                } catch (JSONException e) {
                    Log.e("JsonException", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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

        queue.add(jsonObjectRequest);

    }
}
