package multiplexer.lab.takeout;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import multiplexer.lab.takeout.Helper.EndPoints;

public class LogInActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    LinearLayout loginLayout;
    Snackbar snackbar;
    RelativeLayout rootLayout;
    RequestQueue queue;
    TextView forgotpass;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        etEmail = findViewById(R.id.et_login_email);
        etPassword = findViewById(R.id.et_login_password);
        loginLayout = findViewById(R.id.LL_input);
        rootLayout = findViewById(R.id.rootLayout);
        forgotpass = findViewById(R.id.forgotpass);
        queue = Volley.newRequestQueue(this);
        animation();

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new AlertDialog.Builder(LogInActivity.this).create();
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = inflater.inflate(R.layout.custom_dialog_forgot, null);
                dialog.setView(customView);
                dialog.setCancelable(true);

                Button btn = customView.findViewById(R.id.btn_forgot_password);
                final EditText editText = customView.findViewById(R.id.emailFotgotPassword);

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(editText.getText().toString().isEmpty()){
                            Toast.makeText(LogInActivity.this, "Please insert your email!", Toast.LENGTH_SHORT).show();
                        }else if(!editText.getText().toString().contains(".com")){
                            Toast.makeText(LogInActivity.this, "Email is not valid!", Toast.LENGTH_SHORT).show();
                        }else{
                            sendEmail(editText.getText().toString());
                        }
                    }
                });
                dialog.show();

            }
        });
    }

    private void sendEmail(final String email) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.FORGOT_PASSWORD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("EmailResponse", response.toString());
                        if(response.equals("")){
                            Toast.makeText(LogInActivity.this, "A email is sent please check.", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    Log.e("networkResponse", response.toString());
                    if (error instanceof ServerError && response != null) {
                        try {
                            String res = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers, "application/json"));
                            Log.i("resString", res);
                            if (res.contains("unsupported_grant_type")) {
                                Toast.makeText(LogInActivity.this, "Server Error!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (UnsupportedEncodingException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Email", email);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "myChannel";
            String description = "myDescription";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("123", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void animation() {
        YoYo.with(Techniques.FadeIn).duration(2000).playOn(findViewById(R.id.iv_b1));
        YoYo.with(Techniques.FadeIn).duration(2000).playOn(findViewById(R.id.iv_b2));
        YoYo.with(Techniques.FadeIn).duration(2000).playOn(findViewById(R.id.iv_b3));
        YoYo.with(Techniques.FadeIn).duration(2000).playOn(findViewById(R.id.iv_b4));
        YoYo.with(Techniques.FadeIn).duration(2000).playOn(findViewById(R.id.iv_b5));
        YoYo.with(Techniques.FadeIn).duration(2000).playOn(findViewById(R.id.iv_b6));
        YoYo.with(Techniques.FadeIn).duration(2000).playOn(findViewById(R.id.iv_b7));
        YoYo.with(Techniques.FadeIn).duration(2000).playOn(findViewById(R.id.iv_b8));
        YoYo.with(Techniques.FadeIn).duration(2000).playOn(findViewById(R.id.iv_b9));
        YoYo.with(Techniques.FadeIn).duration(2000).playOn(findViewById(R.id.iv_b10));
        YoYo.with(Techniques.FadeIn).duration(2000).playOn(findViewById(R.id.iv_b11));
    }

    public void btnLogIn(View view) {
        if (internetConnected()) {
            if (snackbar != null) {
                if (snackbar.isShown()) {
                    snackbar.dismiss();
                }
            }
            if (validation()) {
                /*Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                startActivity(intent);*/
                sendDataToServer();
            } else {
                YoYo.with(Techniques.Shake)
                        .duration(1000)
                        .repeat(0)
                        .playOn(loginLayout);
            }
        } else {
            showSnackBar();
        }
    }

    private void sendDataToServer() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.SIGNIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.i("ResponseObj", obj.toString());
                            String accessToken = obj.getString("access_token");
                            Log.i("accessTokenLogin", accessToken);
                            SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("accessToken", accessToken);
                            editor.apply();
                            Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            Log.e("LoginError", e.toString());
                        }

                        Toast.makeText(LogInActivity.this, "Welcome to TakeOut!", Toast.LENGTH_SHORT).show();
                        /*Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();*/
                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    Log.e("networkResponse", response.toString());
                    if (error instanceof ServerError && response != null) {
                        try {
                            String res = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers, "application/json"));
                            Log.i("resString", res);
                            if (res.contains("unsupported_grant_type")) {
                                Toast.makeText(LogInActivity.this, "Server Error!", Toast.LENGTH_SHORT).show();
                            } else if (res.contains("password")) {
                                Toast.makeText(LogInActivity.this, "Username or Password is incorrect!", Toast.LENGTH_SHORT).show();
                            }
                            JSONObject obj = new JSONObject(res);

                        } catch (UnsupportedEncodingException e1) {
                            e1.printStackTrace();
                        } catch (JSONException e2) {
                            e2.printStackTrace();
                        }
                    }
                }
            }
        }) /*{
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", etEmail.getText().toString());
                params.put("password", etPassword.getText().toString());
                params.put("grant_type", "password");
                return params;
                }
            }*/ {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", etEmail.getText().toString());
                params.put("password", etPassword.getText().toString());
                params.put("grant_type", "password");
                return params;
            }
        };
        queue.add(stringRequest);
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

    public void showSnackBar() {
        snackbar = Snackbar
                .make(rootLayout, "Internet is not connected!", Snackbar.LENGTH_INDEFINITE)
                .setAction("Connect", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent settingsIntent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        startActivityForResult(settingsIntent, 9003);
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

    public void btnSignUp(View view) {
        Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    public boolean validation() {
        boolean error = true;
        etEmail.setError(null);
        etPassword.setError(null);

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty()) {
            etEmail.setError("Email is missing!");
            error = false;
        }
        if (!email.contains(".com")) {
            etEmail.setError("Please insert valid email!");
            error = false;
        }

        if (password.isEmpty()) {
            etPassword.setError("Neeed a Password");
            error = false;
        } else if (password.length() < 8) {
            etPassword.setError("Password is too SHORT!!");
            error = false;
        }
        return error;

    }
}
