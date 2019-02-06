package multiplexer.lab.takeout.ItemActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import multiplexer.lab.takeout.Helper.EndPoints;
import multiplexer.lab.takeout.MainActivity;
import multiplexer.lab.takeout.R;

public class AddReferralActivity extends AppCompatActivity {

    TextView text;
    EditText coupon;
    RequestQueue queue;
    Button submit;
    Snackbar snackbar;
    RelativeLayout rootLayout;
    Dialog dialogprog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_referral);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        dialogprog = new Dialog(AddReferralActivity.this);
        progressbarOpen();
        rootLayout = findViewById(R.id.adRefRootLayout);
        text = findViewById(R.id.TV_referral);
        queue = Volley.newRequestQueue(this);
        coupon = findViewById(R.id.ET_coupon);
        submit = findViewById(R.id.submit);

        activationCheck();
        progressbarClose();
    }

    public void showSnackBar() {
        snackbar = Snackbar
                .make(rootLayout, "Internet is not connected!", Snackbar.LENGTH_INDEFINITE)
                .setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 9003) {
            if (internetConnected()) {

            } else {
                showSnackBar();
            }
        }
    }

    private boolean internetConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    private void progressbarClose() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        dialogprog.setCanceledOnTouchOutside(true);
        dialogprog.setCancelable(true);
        dialogprog.dismiss();
    }

    private void progressbarOpen() {
        dialogprog.setContentView(R.layout.custom_dialog_progressbar);
        dialogprog.setCanceledOnTouchOutside(false);
        dialogprog.setCancelable(false);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        dialogprog.show();
    }

    private void activationCheck() {
        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
        boolean status = pref.getBoolean("isValid", false);

        if (status) {
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

        progressbarOpen();
        if (submit.getText().toString().equalsIgnoreCase("Go Back")) {
            progressbarClose();
            finish();
        } else {
            final String couponcode = coupon.getText().toString();
            if (couponcode.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please enter the code to activate", Toast.LENGTH_SHORT).show();
                progressbarClose();
                return;
            }
            if (!internetConnected()) {
                progressbarClose();
                showSnackBar();
                return;
            } else if (snackbar != null) {
                if (snackbar.isShown()) {
                    snackbar.dismiss();
                }
            }

            StringRequest pointRequest = new StringRequest(Request.Method.GET, EndPoints.GET_USE_REFERRAL + couponcode, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(getApplicationContext(), "Congrats! Activation Successful.", Toast.LENGTH_SHORT).show();
                    Log.i("referralData", response.toString());
                    SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("isValid", true);
                    progressbarClose();
                    editor.commit();
                    finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("ParseError", error.toString());
                    progressbarClose();
                    Toast.makeText(getApplicationContext(), getString(R.string.ToastError), Toast.LENGTH_SHORT).show();
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

    public void btnQR(View view) {
        progressbarOpen();
        Intent intent = new Intent(AddReferralActivity.this, ScanQRActivity.class);
        startActivity(intent);
        finish();
    }

    public void btnMenu(View view) {
        progressbarOpen();
        Intent intent = new Intent(AddReferralActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }
}
