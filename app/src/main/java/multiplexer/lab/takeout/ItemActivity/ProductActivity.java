package multiplexer.lab.takeout.ItemActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import multiplexer.lab.takeout.Adapter.ProductAdapter;
import multiplexer.lab.takeout.Helper.EndPoints;
import multiplexer.lab.takeout.LogInActivity;
import multiplexer.lab.takeout.Model.Product;
import multiplexer.lab.takeout.R;

public class ProductActivity extends AppCompatActivity {

    private List<Product> productList = new ArrayList<>();
    int countrycode = 1, catid;
    private RecyclerView recyclerView;
    private ProductAdapter cAdapter;
    public static Activity productActivity;
    RequestQueue queue;
    AlertDialog dialog;
    Dialog dialogprog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        dialogprog = new Dialog(ProductActivity.this);
        productActivity = this;
        progressbarOpen();
        Intent intent = getIntent();
        catid = intent.getIntExtra("CatId", 0);
        Log.i("catId", String.valueOf(catid));
        queue = Volley.newRequestQueue(this);
        recyclerView = findViewById(R.id.recycler_view);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        cAdapter = new ProductAdapter(ProductActivity.this, productList);
        RecyclerView.LayoutManager cLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(cLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(cAdapter);
        input();
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

    public void input() {
        JsonArrayRequest catRequest = new JsonArrayRequest(Request.Method.GET, EndPoints.GET_PRODUCT_DATA + catid + '/' + countrycode, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i("Product", response.toString());
                int id, rating, customer_rating, price, countryid;
                String name, image, description;
                try {
                    for (int i = 0; i < response.length(); i++) {
                        name = response.getJSONObject(i).getString("Name");
                        id = response.getJSONObject(i).getInt("FinId");
                        // need to change the url
                        image = "http://store.bdtakeout.com/images/categoryimage/" + response.getJSONObject(i).getString("Image");

                        description = response.getJSONObject(i).getString("Desc");
                        if (response.getJSONObject(i).isNull("Rating")) {
                            rating = 0;
                        } else {
                            rating = response.getJSONObject(i).getInt("Rating");
                        }

                        if (response.getJSONObject(i).isNull("CustomerRating")) {
                            customer_rating = 5;
                        } else {
                            customer_rating = response.getJSONObject(i).getInt("CustomerRating");
                        }

                        JSONObject finobj = response.getJSONObject(i).getJSONObject("FinishedMaterial");
                        price = finobj.getInt("price");
                        countryid = finobj.getInt("country");

                        Product product = new Product(id, rating, customer_rating, price, countryid, name, image, description);
                        productList.add(product);
                        cAdapter.notifyDataSetChanged();
                    }
                    progressbarClose();

                } catch (JSONException e) {
                    Log.e("ParseError", e.toString());
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
        queue.add(catRequest);
    }

    public void btngetPoints(View view) {
        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
        String status = pref.getString("status", "");

        if (!status.isEmpty()) {

            dialog = new AlertDialog.Builder(ProductActivity.this).create();
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
                        Toast.makeText(ProductActivity.this, "Please insert your invoice no!", Toast.LENGTH_SHORT).show();
                    } else {
                        sendInvoiceNo(editText.getText().toString());
                    }
                }
            });
            dialog.show();
        } else {
            Intent intent = new Intent(ProductActivity.this, AddReferralActivity.class);
            startActivity(intent);
            finish();
            MenuActivity.menuActivity.finish();
        }
    }

    private void sendInvoiceNo(final String invoiceNo) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, EndPoints.GET_USE_COUPON + invoiceNo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("PointsResponse", response.toString());
                        if (!response.equals("")) {
                            Toast.makeText(ProductActivity.this, "Congrats! You have got " + invoiceNo + " points!", Toast.LENGTH_SHORT).show();
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
                            if (res.contains("account")) {
                                Toast.makeText(ProductActivity.this, "Your account is not activated!", Toast.LENGTH_SHORT).show();
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

    public void btnQR(View view) {
        Intent intent = new Intent(ProductActivity.this, ScanQRActivity.class);
        startActivity(intent);
        finish();
        MenuActivity.menuActivity.finish();
    }
}
