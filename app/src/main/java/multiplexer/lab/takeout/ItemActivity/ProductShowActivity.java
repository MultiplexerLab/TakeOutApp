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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_show);

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
        foodRate.setText(intent.getIntExtra("customerrate", 5) + ".0");
        foodPrice.setText("Price: " + intent.getIntExtra("price", 0) + " BDT");
        foodDescription.setText(intent.getStringExtra("description"));
        int rate = intent.getIntExtra("rating", 0);

        if (rate != 0) {
            personalRate.setBackgroundResource(R.color.green);
            personalRate.setText("Your Rate: " + intent.getIntExtra("rating", 5) + ".0");
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
        smileRating.setSelectedSmile(intent.getIntExtra("rating", 0));

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rating = smileRating.getRating();
                if(internetConnected()){
                    postRating(intent.getIntExtra("prodid", 0), rating);
                    dlog.dismiss();
                }else {
                    showSnackBar();
                }

            }
        });
        dlog.show();
    }

    public void postRating(final int productid, final int rating) {

        android.content.SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
        String personid = pref.getString("id", "");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.POST_PRODUCT_RATING + personid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Thanks for your rating!", Toast.LENGTH_SHORT).show();
                        dlog.dismiss();
                        personalRate.setBackgroundResource(R.color.green);
                        personalRate.setText("Your Rate: " + rating);
                        Log.i("Rate Response", response.toString());
                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), getString(R.string.ToastError), Toast.LENGTH_LONG).show();
                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    Log.e("networkResponse", response.toString());
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
        })

        {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("Prodctid", String.valueOf(productid));
                params.put("Rating", String.valueOf(rating));

                return params;
            }
        };
        queue.add(stringRequest);
    }

    public void btnQR(View view) {
        Toast.makeText(getApplicationContext(), "Please wait...", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ProductShowActivity.this, ScanQRActivity.class);
        startActivity(intent);
        finish();
        MenuActivity.menuActivity.finish();
        ProductActivity.productActivity.finish();
    }

    public void btngetPoints(View view) {
        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
        String status = pref.getString("status", "");

        if (!status.isEmpty()) {

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
            Intent intent = new Intent(ProductShowActivity.this, AddReferralActivity.class);
            startActivity(intent);
            finish();
            MenuActivity.menuActivity.finish();
            ProductActivity.productActivity.finish();
        }
    }

    private void sendInvoiceNo(final String invoiceNo) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, EndPoints.GET_USE_COUPON + invoiceNo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("PointsResponse", response.toString());
                        if (!response.equals("")) {
                            Toast.makeText(ProductShowActivity.this, "Congrats! You have got " + invoiceNo + " points!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), getString(R.string.ToastError), Toast.LENGTH_LONG).show();
                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    Log.e("networkResponse", response.toString());
                    if (error instanceof ServerError && response != null) {
                        try {
                            String res = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers, "application/json"));
                            Log.i("resString", res);
                            if (res.contains("account")) {
                                Toast.makeText(ProductShowActivity.this, "Your account is not activated!", Toast.LENGTH_SHORT).show();
                            }
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
}
