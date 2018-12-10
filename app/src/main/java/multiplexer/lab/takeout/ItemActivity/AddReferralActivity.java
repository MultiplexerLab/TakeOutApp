package multiplexer.lab.takeout.ItemActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import multiplexer.lab.takeout.Helper.EndPoints;
import multiplexer.lab.takeout.MainActivity;
import multiplexer.lab.takeout.R;


public class AddReferralActivity extends AppCompatActivity {

    static int value;
    TextView text;
    EditText coupon;
    String couponcode;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_referral);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        text = findViewById(R.id.TV_referral);
        queue = Volley.newRequestQueue(this);
        coupon = findViewById(R.id.ET_coupon);
        couponcode = coupon.getText().toString();

        Intent intent = getIntent();
        value = intent.getIntExtra("val",0);
        if(value == 1){
            text.setText("Use Code to Activate your Account.");
        }
    }

    public void btnAddRef(View view) {

        JsonObjectRequest pointRequest = new JsonObjectRequest(Request.Method.GET, EndPoints.GET_USE_REFERRAL+couponcode, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.i("data", response.toString());
                Intent intent = new Intent(AddReferralActivity.this, MainActivity.class);
                intent.putExtra("val",value);
                startActivity(intent);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ParseError", error.toString());
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
}
