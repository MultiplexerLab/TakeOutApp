package multiplexer.lab.takeout.ItemActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

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

import multiplexer.lab.takeout.Adapter.StoreAdapter;
import multiplexer.lab.takeout.Helper.EndPoints;
import multiplexer.lab.takeout.Model.Store;
import multiplexer.lab.takeout.R;


public class StoreLocatorActivity extends AppCompatActivity {
    final Context context = this;
    private List<Store> storeList = new ArrayList<>();
    RequestQueue queue;
    ArrayAdapter<String> adapter;
    ArrayList<String> countryArrList = new ArrayList<String>();
    ArrayList<Integer> countryIdList = new ArrayList<Integer>();
    String country;
    Spinner spinnerCountry2;
    private RecyclerView recyclerView;
    private StoreAdapter storeAdapter;
    Dialog dialogprog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_locator);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        dialogprog = new Dialog(StoreLocatorActivity.this);
        queue = Volley.newRequestQueue(this);
        recyclerView = findViewById(R.id.recycler_view_store);
        spinnerCountry2 = findViewById(R.id.spinnerCountry2);

        RecyclerView.LayoutManager cLayoutManager = new LinearLayoutManager(StoreLocatorActivity.this);
        recyclerView.setLayoutManager(cLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        storeAdapter = new StoreAdapter(StoreLocatorActivity.this, storeList);
        recyclerView.setAdapter(storeAdapter);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, countryArrList);
        spinnerCountry2.setAdapter(adapter);
        getAllCountries();
        getStoresByCountry(1);

        spinnerCountry2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int countryId = countryIdList.get(position);
                Log.i("countryId", countryId + "");

                getStoresByCountry(countryId);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

    public void getAllCountries() {
        progressbarOpen();
        JsonArrayRequest countryRequest = new JsonArrayRequest(Request.Method.GET, EndPoints.GET_COUNTRY_DATA, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Log.i("countrydata", response.toString());
                try {
                    for (int i = 0; i < response.length(); i++) {
                        countryArrList.add(response.getJSONObject(i).getString("Name"));
                        countryIdList.add(response.getJSONObject(i).getInt("Id"));
                    }
                    adapter.notifyDataSetChanged();
                    progressbarClose();
                } catch (JSONException e) {
                    Log.e("ParseError", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ParseError", error.toString());
                progressbarClose();
                Toast.makeText(getApplicationContext(),"An Error occurred! Try again later.",Toast.LENGTH_LONG).show();
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


    public void getStoresByCountry(int countryId) {
        progressbarOpen();
        JsonArrayRequest storeRequest = new JsonArrayRequest(Request.Method.GET, EndPoints.GET_STORE_DATA + countryId, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                String name, address, phone, latitude, longitude;
                Log.i("storedata", response.toString());
                storeList.clear();
                if (response.length() == 0) {
                    adapter.notifyDataSetChanged();
                }
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject mainObject = response.getJSONObject(i);
                        name = mainObject.getString("Name");
                        address = mainObject.getString("Address");
                        phone = mainObject.getString("Phone");

                        JSONObject geoLocation = mainObject.getJSONObject("GeoLocation");
                        latitude = geoLocation.getString("Latitude");
                        longitude = geoLocation.getString("Longitude");
                        Store store = new Store(name, address, phone, country, latitude, longitude);
                        storeList.add(store);
                        storeAdapter.notifyDataSetChanged();
                    }
                    progressbarClose();
                } catch (JSONException e) {
                    Log.e("ParseError", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ParseError", error.toString());
                progressbarClose();
                Toast.makeText(getApplicationContext(),"An Error occurred! Try again later.",Toast.LENGTH_LONG).show();
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
        queue.add(storeRequest);
    }

}
