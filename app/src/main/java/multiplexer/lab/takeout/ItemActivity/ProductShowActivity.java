package multiplexer.lab.takeout.ItemActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hsalf.smilerating.SmileRating;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import multiplexer.lab.takeout.Helper.EndPoints;
import multiplexer.lab.takeout.R;

public class ProductShowActivity extends AppCompatActivity {
    ImageView foodPic;
    TextView foodRate, foodPrice, foodDescription, foodName, personalRate;
    RequestQueue queue;
    Intent intent;
    RelativeLayout rootLayout;
    Snackbar snackbar;
    AlertDialog dialog;
    Dialog dlog;
    Dialog dialogprog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_show);
        dialogprog = new Dialog(ProductShowActivity.this);
        foodPic = findViewById(R.id.foodpic_show);
        foodRate = findViewById(R.id.foodratenumber_show);
        foodPrice = findViewById(R.id.foodprice_show);
        foodDescription = findViewById(R.id.fooddescription_show);
        foodName = findViewById(R.id.foodname_show);
        personalRate = findViewById(R.id.foodratebtn_show);
        rootLayout = findViewById(R.id.prodShowRootLayout);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        intent = getIntent();
        Picasso.with(getApplicationContext()).load(intent.getStringExtra("picaddress")).into(foodPic);
        foodName.setText(intent.getStringExtra("name"));
        foodRate.setText(intent.getIntExtra("rating", 5) + ".0");
        foodPrice.setText("Price: " + intent.getIntExtra("price", 0) + " BDT");
        foodDescription.setText(intent.getStringExtra("description"));
        int customerrate = intent.getIntExtra("customerrate", 0);

        if (customerrate != 0) {
            personalRate.setBackgroundResource(R.color.green);
            personalRate.setText("Your Rate: " + customerrate + ".0");
        } else {
            personalRate.setBackgroundResource(R.color.red);
        }
        queue = Volley.newRequestQueue(getApplicationContext());
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

    public void btnRate(View view) {
        dlog = new Dialog(ProductShowActivity.this);
        dlog.setContentView(R.layout.custom_rating_dialog);
        Button dialogButton = dlog.findViewById(R.id.btn_submit);
        final SmileRating smileRating = dlog.findViewById(R.id.smile_rating);
        //smileRating.setSelectedSmile(intent.getIntExtra("rating", 0));

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rating = smileRating.getRating();
                if (internetConnected()) {
                    postRating(intent.getIntExtra("productId", 0), rating);
                    dlog.dismiss();
                } else {
                    showSnackBar();
                }
            }
        });
        dlog.show();
    }

    public void postRating(final int productid, final int rating) {

        String url = EndPoints.POST_PRODUCT_RATING + "/" + productid + "/"
                + rating;
        Log.i("rateUrl", url);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Thanks for your rating!", Toast.LENGTH_SHORT).show();
                        dlog.dismiss();
                        personalRate.setBackgroundResource(R.color.green);
                        personalRate.setText("Your Rate: " + rating);
                        Log.i("RateResponse", response.toString());
                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), getString(R.string.ToastError), Toast.LENGTH_LONG).show();
                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    Log.e("networkResponse", error.toString());
                    if (error instanceof ServerError && response != null) {
                        try {
                            String res = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers, "application/json"));
                            Log.i("RateString", res);

                        } catch (UnsupportedEncodingException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
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
        queue.add(stringRequest);
    }

    public void btnQR(View view) {
        progressbarOpen();
        Intent intent = new Intent(ProductShowActivity.this, ScanQRActivity.class);
        startActivity(intent);
        finish();
        MenuActivity.menuActivity.finish();
        ProductActivity.productActivity.finish();
    }

    private void progressbarOpen() {
        dialogprog.setContentView(R.layout.custom_dialog_progressbar);
        dialogprog.setCanceledOnTouchOutside(false);
        dialogprog.setCancelable(false);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        dialogprog.show();
    }

    private void progressbarClose() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        dialogprog.setCanceledOnTouchOutside(true);
        dialogprog.setCancelable(true);
        dialogprog.dismiss();
    }

    public void btnGetPoints(View view) {
        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
        boolean isValid = pref.getBoolean("isValid", false);

        if (isValid == true) {
            dialog = new AlertDialog.Builder(ProductShowActivity.this).create();
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View customView = inflater.inflate(R.layout.custom_dialog_points, null);
            dialog.setView(customView);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(true);

            Button btn = customView.findViewById(R.id.btn_bonus_points);
            final EditText editText = customView.findViewById(R.id.invoiceNo);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editText.getText().toString().isEmpty()) {
                        Toast.makeText(ProductShowActivity.this, "Please insert your invoice no!", Toast.LENGTH_SHORT).show();
                    } else {
                        sendInvoiceNo(editText.getText().toString());
                    }
                }
            });
            dialog.show();
        } else {
            Toast.makeText(this, "You need to activate your account first!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProductShowActivity.this, AddReferralActivity.class);
            startActivity(intent);
        }
    }

    private void sendInvoiceNo(final String invoiceNo) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, EndPoints.GET_USE_COUPON + invoiceNo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("PointsResponse", response.toString());
                        if (!response.equals("")) {
                            Toast.makeText(ProductShowActivity.this, "Congrats! You have got some bonus points!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                progressbarClose();
                int response = error.networkResponse.statusCode;
                Log.i("statusCode", response+"");
                if(response==400 || response==404){
                    Toast.makeText(ProductShowActivity.this, "This code is invalid!", Toast.LENGTH_SHORT).show();
                }
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
        queue.add(stringRequest);
    }
}
