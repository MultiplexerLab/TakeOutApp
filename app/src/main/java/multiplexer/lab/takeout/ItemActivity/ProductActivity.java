package multiplexer.lab.takeout.ItemActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import multiplexer.lab.takeout.Adapter.ProductAdapter;
import multiplexer.lab.takeout.Helper.EndPoints;
import multiplexer.lab.takeout.Model.Product;
import multiplexer.lab.takeout.R;

public class ProductActivity extends AppCompatActivity {

    private List<Product> productList = new ArrayList<>();
    int countrycode=1, catid;
    private RecyclerView recyclerView;
    private ProductAdapter cAdapter;
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Intent intent = getIntent();
        catid= intent.getIntExtra("CatId",0);
        Log.i("Anikkk",String.valueOf(catid));
        queue = Volley.newRequestQueue(this);
        recyclerView = findViewById(R.id.recycler_view);
        cAdapter = new ProductAdapter(ProductActivity.this,productList);
        RecyclerView.LayoutManager cLayoutManager = new LinearLayoutManager(ProductActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(cAdapter);
        input();
    }

    public void input() {
        JsonArrayRequest catRequest = new JsonArrayRequest(Request.Method.GET, EndPoints.GET_PRODUCT_DATA+catid+'/'+countrycode, new Response.Listener<JSONArray>() {
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
                        image ="http://store.bdtakeout.com/images/categoryimage/"+response.getJSONObject(i).getString("Image");

                        description = response.getJSONObject(i).getString("Desc");
                        if(response.getJSONObject(i).isNull("Rating")){
                            rating = 0;
                        }else{
                            rating = response.getJSONObject(i).getInt("Rating");
                        }

                        if(response.getJSONObject(i).isNull("CustomerRating")){
                            customer_rating = 3;
                        }else{
                            customer_rating = response.getJSONObject(i).getInt("CustomerRating");
                        }

                        JSONObject finobj = response.getJSONObject(i).getJSONObject("FinishedMaterial");
                        price = finobj.getInt("price");
                        countryid = finobj.getInt("country");

                        Product product = new Product(id,rating,customer_rating,price,countryid,name,image,description);
                        productList.add(product);
                        cAdapter.notifyDataSetChanged();
                    }

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
}
