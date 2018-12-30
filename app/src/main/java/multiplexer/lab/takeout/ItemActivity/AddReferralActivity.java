package multiplexer.lab.takeout.ItemActivity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import multiplexer.lab.takeout.Helper.EndPoints;
import multiplexer.lab.takeout.R;


public class AddReferralActivity extends AppCompatActivity {

    TextView text;
    EditText coupon;
    RequestQueue queue;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_referral);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        text = findViewById(R.id.TV_referral);
        queue = Volley.newRequestQueue(this);
        coupon = findViewById(R.id.ET_coupon);
        submit = findViewById(R.id.submit);

        activationCheck();


    }

    private void activationCheck() {
        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
        String status = pref.getString("status", "");

        if (!status.isEmpty()) {
            coupon.setText("Your Account is already Activated");
            coupon.setEnabled(false);
            submit.setText("Go Back");

        }
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

    public void btnAddRef(View view) {

        if (submit.getText().toString().equalsIgnoreCase("Go Back")) {
            finish();
        } else {
            final String couponcode = coupon.getText().toString();

            JsonObjectRequest pointRequest = new JsonObjectRequest(Request.Method.GET, EndPoints.GET_USE_REFERRAL + couponcode, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Log.i("data", response.toString());
                    SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("status", couponcode);
                    editor.commit();
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
}
