package multiplexer.lab.takeout.ItemActivity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.hsalf.smilerating.SmileRating;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import multiplexer.lab.takeout.Helper.EndPoints;
import multiplexer.lab.takeout.R;

import static multiplexer.lab.takeout.R.color.green;

public class ProductShowActivity extends AppCompatActivity {
    ImageView foodPic;
    TextView foodRate, foodPrice, foodDescription, foodName, personalRate;
    RequestQueue queue;
    Intent intent;
    Dialog dialog;

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
        dialog = new Dialog(ProductShowActivity.this);
        dialog.setContentView(R.layout.custom_dialog);
        Button dialogButton = dialog.findViewById(R.id.btn_submit);
        final SmileRating smileRating = dialog.findViewById(R.id.smile_rating);
        smileRating.setSelectedSmile(intent.getIntExtra("rating", 0));

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int rating = smileRating.getRating();
                personalRate.setBackgroundResource(R.color.green);
                personalRate.setText("Your Rate: " + rating);

                postRating(intent.getIntExtra("prodid", 0), rating);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void postRating(final int productid, final int rating) {

        android.content.SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
        String personid = pref.getString("id", "");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.POST_PRODUCT_RATING + personid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Thanks for your rating!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        Log.i("Rate Response", response.toString());
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
                            Log.i("Rate String", res);

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

}
