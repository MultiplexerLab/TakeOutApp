package multiplexer.lab.takeout.ItemActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import multiplexer.lab.takeout.Adapter.MenuAdapter;
import multiplexer.lab.takeout.Adapter.StoreAdapter;
import multiplexer.lab.takeout.Helper.EndPoints;
import multiplexer.lab.takeout.Model.Menu;
import multiplexer.lab.takeout.Model.Store;
import multiplexer.lab.takeout.R;


public class StoreLocatorActivity extends AppCompatActivity {
    final Context context = this;
    private List<Store> storeList = new ArrayList<>();
    RequestQueue queue;
    ArrayAdapter<String> adapter;
    ArrayList<String> arr = new ArrayList<String>();
    ArrayList<Integer> arrint = new ArrayList<Integer>();
    String country;
    int cCode;
    Spinner spinnerCountry2;
    private RecyclerView recyclerView;
    private StoreAdapter cAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_locator);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        queue = Volley.newRequestQueue(this);
        recyclerView = findViewById(R.id.recycler_view_store);
        spinnerCountry2 = findViewById(R.id.spinnerCountry2);

        RecyclerView.LayoutManager cLayoutManager = new LinearLayoutManager(StoreLocatorActivity.this);
        recyclerView.setLayoutManager(cLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        cAdapter = new StoreAdapter(StoreLocatorActivity.this, storeList);
        recyclerView.setAdapter(cAdapter);


        addCountry();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arr);
        spinnerCountry2.setAdapter(adapter);
        addStore();
       // Store store = new Store("an", "baN", "01010", "B", "33", "33");
       // storeList.add(store);
        //arr.add("India");
        spinnerCountry2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                country = arr.get(position);
                cCode = arrint.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void addCountry() {

        JsonObjectRequest countryRequest = new JsonObjectRequest(Request.Method.GET, EndPoints.GET_COUNTRY_DATA, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("countrydata", response.toString());
                try {
                    arr.add(response.getString("Name"));
                    arrint.add(response.getInt("Id"));

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
        queue.add(countryRequest);

    }


    public void addStore (){

        JsonObjectRequest storeRequest = new JsonObjectRequest(Request.Method.GET, EndPoints.GET_STORE_DATA, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String name, address, phone, latitude, longitude;
                Log.i("storedata", response.toString());

                try {
                    JSONArray jsonArray = response.getJSONArray("id");
                    for (int i = 0; i < jsonArray.length(); i++) {
                            name = jsonArray.getJSONObject(i).getString("Name");
                            address = jsonArray.getJSONObject(i).getString("Address");
                            phone = jsonArray.getJSONObject(i).getString("Phone");
                            latitude = jsonArray.getJSONObject(i).getString("Latitude");
                            longitude = jsonArray.getJSONObject(i).getString("Longitude");
                            Store store = new Store(name,address,phone,country,latitude,longitude);
                            storeList.add(store);
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
            public Map<String, String> getParams () {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", Integer.toString(cCode));
                return params;
            }
        };
        queue.add(storeRequest);
    }

}
