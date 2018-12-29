package multiplexer.lab.takeout;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import multiplexer.lab.takeout.Helper.EndPoints;
import multiplexer.lab.takeout.Model.RegisterBindingModel;
import multiplexer.lab.takeout.R;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    final Context context = this;
    EditText etFullname, etEmail, etPassword, etConPassword, etPhone;
    RadioButton rMale, rFemale;
    Snackbar snackbar;
    RelativeLayout rootLayout;
    ArrayAdapter<String> adapter;
    String arr[] = {"Bangladesh", "Sri Lanka"};
    Spinner spinnerCountry;
    RequestQueue queue;
    //int value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etFullname = findViewById(R.id.et_signup_fullname);
        etEmail = findViewById(R.id.et_signup_email);
        etPassword = findViewById(R.id.et_signup_password);
        etConPassword = findViewById(R.id.et_signup_conpassword);
        etPhone = findViewById(R.id.et_signup_phoneno);
        rootLayout = findViewById(R.id.rootLayout);
        spinnerCountry = findViewById(R.id.spinnerCountry);
        queue = Volley.newRequestQueue(this);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arr);
        spinnerCountry.setAdapter(adapter);

    }



    public void btnSignUp(View view) {

        if (internetConnected()) {
            if (validation()) {
               // selectAvater();
                sendDataToServer();

            } else {
                YoYo.with(Techniques.Shake)
                        .duration(1000)
                        .repeat(0)
                        .playOn(findViewById(R.id.ll_signup));
            }
        } else {
            showSnackBar();
        }
    }

    public void selectAvater(){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_avatar);
        ImageView dialogmale = dialog.findViewById(R.id.IV_male);
        ImageView dialogfemale = dialog.findViewById(R.id.IV_female);
        // if button is clicked, close the custom dialog
        final SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
        dialogmale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("Avatar", "male");
                editor.apply();
                dialog.dismiss();
                sendDataToServer();
            }
        });
        dialogfemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("Avatar", "female");
                editor.apply();
                dialog.dismiss();
                sendDataToServer();
            }
        });

        dialog.show();

    }

    private void sendDataToServer() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.SIGNUP_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(SignUpActivity.this, "Thanks for being registered!", Toast.LENGTH_SHORT).show();
                        final SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("fullname", etFullname.getText().toString());
                        editor.apply();
                        editor.putString("email", etEmail.getText().toString());
                        editor.apply();
                        editor.putString("phoneNumber", etPhone.getText().toString());
                        editor.apply();
                        editor.putString("countryName", spinnerCountry.getSelectedItem().toString());
                        editor.apply();
                        Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
                        startActivity(intent);
                        finish();
                        Log.i("Response", response.toString());
                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if(response!=null) {
                    Log.e("networkResponse", response.toString());
                    if (error instanceof ServerError && response != null) {
                        try {
                            String res = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers, "application/json"));
                            Log.i("resString", res);
                            if(res.contains("Phone")){
                                Toast.makeText(SignUpActivity.this, "This phone number is already taken!", Toast.LENGTH_SHORT).show();
                            }else if(res.contains("Email")){
                                Toast.makeText(SignUpActivity.this, "This email Id is already registered!", Toast.LENGTH_SHORT).show();
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
        }) {
            @Override
            protected Map<String, String> getParams() {
                RegisterBindingModel obj = new RegisterBindingModel("ASDFA", "testUser1@gmail.com",
                        "01994654654", "Bangladesh", "asddsaa123", "asddsaa123");
                Map<String, String> params = new HashMap<String, String>();
                params.put("fullName", etFullname.getText().toString());
                params.put("email", etEmail.getText().toString());
                params.put("phoneNumber", etPhone.getText().toString());
                params.put("countryName", spinnerCountry.getSelectedItem().toString());
                params.put("password", etPassword.getText().toString());
                params.put("confirmPassword", etConPassword.getText().toString());
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private boolean validation() {
        boolean error = true;
        etFullname.setError(null);
        etEmail.setError(null);
        etPassword.setError(null);
        etConPassword.setError(null);
        etPhone.setError(null);

        String fullname = etFullname.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String conpassword = etConPassword.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (fullname.isEmpty()) {
            etFullname.setError("Name is missing");
            error = false;
        }
        if (email.isEmpty()) {
            etEmail.setError("Email Address is missing");
            error = false;
        } else if (!email.endsWith(".com")) {
            etEmail.setError("Enter a valid Email Address");
            error = false;
        }
        if (password.isEmpty()) {
            etPassword.setError("Neeed a Password");
            error = false;
        } else if (password.length() < 8) {
            etPassword.setError("password length needs to be more than 8");
            error = false;
        }
        if (conpassword.isEmpty()) {
            etConPassword.setError("Confirm Password is Empty");
            error = false;
        }
        if (phone.isEmpty()) {
            etPhone.setError("Need your Phoneno");
            error = false;
        } else if (phone.length() != 11) {
            etPhone.setError("Enter a valid Phone No.");
            error = false;
        }
        if (!password.equalsIgnoreCase(conpassword)) {
            etConPassword.setError("Password did not match");
            error = false;
        }
        return error;
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

}
