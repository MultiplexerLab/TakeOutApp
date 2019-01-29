package multiplexer.lab.takeout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import multiplexer.lab.takeout.Adapter.AdAdapterNew;
import multiplexer.lab.takeout.Helper.EndPoints;
import multiplexer.lab.takeout.ItemActivity.AboutUsActivity;
import multiplexer.lab.takeout.ItemActivity.AddReferralActivity;
import multiplexer.lab.takeout.ItemActivity.MenuActivity;
import multiplexer.lab.takeout.ItemActivity.ProfileActivity;
import multiplexer.lab.takeout.ItemActivity.ScanQRActivity;
import multiplexer.lab.takeout.ItemActivity.StoreLocatorActivity;

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
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;

import com.nightonke.boommenu.BoomMenuButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ArrayList<Integer> iconList;
    ArrayList<String> titleList;
    TextView numpoints, name, status;
    int value;
    ImageView pic;
    Snackbar snackbar;
    RelativeLayout rootLayout;
    private RecyclerView recyclerView;
    private List<String> adList = new ArrayList<>();
    AdAdapterNew adAdapter;
    RequestQueue queue;
    BoomMenuButton bmb;
    BoomMenuButton bmb1;
    AlertDialog dialog;
    Dialog dialogprog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialogprog = new Dialog(MainActivity.this);
        pic = findViewById(R.id.IV_avatar_main);
        name = findViewById(R.id.name);
        rootLayout = findViewById(R.id.mainRootLayout);
        numpoints = findViewById(R.id.numpoints);
        status = findViewById(R.id.activate_status);

        ActionBar mActionBar = getSupportActionBar();
        assert mActionBar != null;
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View actionBar = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView mTitleTextView = actionBar.findViewById(R.id.title_text);
        mTitleTextView.setText("Home");
        mActionBar.setCustomView(actionBar);
        mActionBar.setDisplayShowCustomEnabled(true);
        ((Toolbar) actionBar.getParent()).setContentInsetsAbsolute(0, 0);
        bmb1 = actionBar.findViewById(R.id.bmb1);
        iconList = new ArrayList<>();
        titleList = new ArrayList<>();
        setInitBoom();
        boomCustomizebmb1();
        queue = Volley.newRequestQueue(this);
        progressbarOpen();
        recyclerView = findViewById(R.id.recyclerview_ad);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adAdapter = new AdAdapterNew(MainActivity.this, adList);
        recyclerView.setAdapter(adAdapter);

        if (internetConnected()) {
            if (snackbar != null) {
                if (snackbar.isShown()) {
                    snackbar.dismiss();
                }
            }
            setAvatar();
            setStatus();
            getPoints();
            getAds();
            getProfileData();

        } else {
            progressbarClose();
            showSnackBar();
        }


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

    private void setStatus() {

        //here will be the code to check if the user already activated the account by using referral code

        //if activated
        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("status", "");
        //editor.putString("status", "activatedCode");
        editor.commit();
        //status.setText("Activated");
        //status.setTextColor(this.getResources().getColor(R.color.green));
    }

    private void setAvatar() {

        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
        String avatar = pref.getString("Avatar", "male");
        if (avatar.equalsIgnoreCase("male")) {
            pic.setImageResource(R.drawable.male);
        } else {
            pic.setImageResource(R.drawable.female);
        }

    }

    @Override
    protected void onResume() {
        if (internetConnected()) {
            if (snackbar != null) {
                if (snackbar.isShown()) {
                    snackbar.dismiss();
                }
            }
            setAvatar();
            setStatus();
            getPoints();
            getAds();
            getProfileData();
        } else {
            progressbarClose();
            showSnackBar();
        }
        super.onResume();
    }

    private void getProfileData() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, EndPoints.GET_PROFILE_DATA, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("responseprofile", response.toString());
                try {
                    SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("id", response.getString("Id"));
                    editor.commit();
                    editor.putString("fullname", response.getString("Fullname"));
                    editor.commit();
                    editor.putString("email", response.getString("Email"));
                    editor.commit();
                    editor.putString("phone", response.getString("Phone"));
                    editor.commit();
                    name.setText(response.getString("Fullname"));


                } catch (JSONException e) {
                    Log.e("JsonException", e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressbarClose();
                Toast.makeText(getApplicationContext(), getString(R.string.ToastError), Toast.LENGTH_LONG).show();
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

        queue.add(jsonObjectRequest);

    }

    private void getPoints() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, EndPoints.GET_POINT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("points", response.toString());
                numpoints.setText(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressbarClose();
                Toast.makeText(getApplicationContext(), getString(R.string.ToastError), Toast.LENGTH_LONG).show();
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

    private void getAds() {
        JsonObjectRequest adsRequest = new JsonObjectRequest(Request.Method.GET, EndPoints.GET_ADS_DATA, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("Ads", response.toString());
                //ArrayList<String> pics = new ArrayList<>();
                try {
                    JSONArray jsonArray = response.getJSONArray("fpaAdds");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        adList.add("http://store.bdtakeout.com/images/advertiseimage/" + jsonArray.getJSONObject(i).getString("image"));
                    }
                    adAdapter.notifyDataSetChanged();
                    //initSetPic(pics);
                    progressbarClose();
                } catch (JSONException e) {
                    Log.e("ParseError", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("AdError", error.toString());

                progressbarClose();
                Toast.makeText(getApplicationContext(), getString(R.string.ToastError), Toast.LENGTH_LONG).show();
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
        queue.add(adsRequest);
    }

   /* private void initSetPic(ArrayList<String> pics) {
        Log.i("data", pics.toString());
        ViewPager viewpager = (ViewPager) findViewById(R.id.viewpager_default);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator_default);
        AdAdapter mPageAdapter = new AdAdapter(this, pics);
        viewpager.setAdapter(mPageAdapter);
        viewpager.setOffscreenPageLimit(pics.size());
        indicator.setViewPager(viewpager);
    }*/

    private void setInitBoom() {
        iconList.add(R.drawable.profileicon);
        iconList.add(R.drawable.aboutuslogo);
        iconList.add(R.drawable.qr_icon);
        iconList.add(R.drawable.store_locator_icon);
        iconList.add(R.drawable.home_delivery_icon);
        iconList.add(R.drawable.referral_icon);
        iconList.add(R.drawable.logout_icon);

        titleList.add("Profile");
        titleList.add("About Us");
        titleList.add("Scan QR");
        titleList.add("Store Locator");
        titleList.add("Home Delivery");
        titleList.add("Add Referral");
        titleList.add("LogOut");

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       /* if (toggle.onOptionsItemSelected(item)) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }


    public void boomCustomizebmb1() {
        for (int i = 0; i < bmb1.getPiecePlaceEnum().pieceNumber(); i++) {
            TextInsideCircleButton.Builder builder = new TextInsideCircleButton.Builder()
                    .normalImageRes(iconList.get(i))
                    .normalText(titleList.get(i))
                    .rippleEffect(true)
                    .normalColorRes(R.color.yellow)
                    .highlightedColorRes(R.color.yellow)
                    .pieceColorRes(R.color.black)
                    .textGravity(Gravity.CENTER)
                    .typeface(Typeface.DEFAULT_BOLD)
                    .normalTextColorRes(R.color.lightblack)
                    .textSize(10)
                    .imagePadding(new Rect(30, 30, 30, 50))
                    .textPadding(new Rect(10, 20, 10, 0))
                    .shadowEffect(true)
                    .rotateImage(true)
                    .rotateText(true)
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int i) {
                            Intent intent = null;
                            switch (i) {
                                case 0:
                                    intent = new Intent(MainActivity.this, ProfileActivity.class);
                                    break;
                                case 1:
                                    intent = new Intent(MainActivity.this, AboutUsActivity.class);
                                    break;
                                case 2:
                                    Toast.makeText(getApplicationContext(),"Please wait...",Toast.LENGTH_SHORT).show();
                                    intent = new Intent(MainActivity.this, ScanQRActivity.class);
                                    break;
                                case 3:
                                    intent = new Intent(MainActivity.this, StoreLocatorActivity.class);
                                    break;
                                case 4:
                                    String url = "https://www.foodpanda.com.bd/";
                                    intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(url));
                                    break;
                                case 5:
                                    intent = new Intent(MainActivity.this, AddReferralActivity.class);
                                    break;
                                case 6:
                                    SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("accessToken", "");
                                    editor.apply();
                                    intent = new Intent(MainActivity.this, LogInActivity.class);
                                    finish();
                                    break;
                            }
                            startActivity(intent);
                        }
                    });
            bmb1.addBuilder(builder);
        }
    }


    public void btnScanQR(MenuItem item) {
        Intent intent = new Intent(MainActivity.this, ScanQRActivity.class);
        startActivity(intent);
    }

    public void btnProfile(MenuItem item) {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    public void btnAboutUs(MenuItem item) {
        Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
        startActivity(intent);
    }

    public void btnStoreLocator(MenuItem item) {
        Intent intent = new Intent(MainActivity.this, StoreLocatorActivity.class);
        startActivity(intent);
    }

    public void btnHomeDelivery(MenuItem item) {
        String url = "https://www.foodpanda.com.bd/";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    public void btnAddReferral(MenuItem item) {
        Intent intent = new Intent(MainActivity.this, AddReferralActivity.class);
        startActivity(intent);
    }

    public void btnLogOut(MenuItem item) {
        Intent intent = new Intent(MainActivity.this, LogInActivity.class);
        startActivity(intent);
    }

    public void btnMenu(View view) {
        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    public void btnQR(View view) {
        Toast.makeText(getApplicationContext(),"Please wait...",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, ScanQRActivity.class);
        startActivity(intent);
    }

    public void btnActivate(View view) {
        Intent intent = new Intent(MainActivity.this, AddReferralActivity.class);
        intent.putExtra("val", 1);
        startActivity(intent);

    }

    public void getPoints(View view) {
        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
        String status = pref.getString("status", "");

        if (!status.isEmpty()) {
            dialog = new AlertDialog.Builder(MainActivity.this).create();
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
                        Toast.makeText(MainActivity.this, "Please insert your invoice no!", Toast.LENGTH_SHORT).show();
                    } else {
                        sendInvoiceNo(editText.getText().toString());
                    }
                }
            });
            dialog.show();
        } else {
            Intent intent = new Intent(MainActivity.this, AddReferralActivity.class);
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
                            Toast.makeText(MainActivity.this, "Congrats! You have got " + invoiceNo + " points!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                progressbarClose();

                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    Log.e("networkResponse", response.toString());
                    if (error instanceof ServerError && response != null) {
                        try {
                            String res = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers, "application/json"));
                            Log.i("resStringA", res);
                            if (res.contains("account")) {
                                Toast.makeText(MainActivity.this, "Your account is not activated!", Toast.LENGTH_SHORT).show();
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
