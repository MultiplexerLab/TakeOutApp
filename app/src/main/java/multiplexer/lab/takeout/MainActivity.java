package multiplexer.lab.takeout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import multiplexer.lab.takeout.Helper.EndPoints;
import multiplexer.lab.takeout.ItemActivity.AboutUsActivity;
import multiplexer.lab.takeout.ItemActivity.AddReferralActivity;
import multiplexer.lab.takeout.ItemActivity.MenuActivity;
import multiplexer.lab.takeout.ItemActivity.ProfileActivity;
import multiplexer.lab.takeout.ItemActivity.ScanQRActivity;
import multiplexer.lab.takeout.ItemActivity.StoreLocatorActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;

import com.nightonke.boommenu.BoomMenuButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    NavigationView mNavigationView;
    View header;
    ImageView burgerslide;
    ArrayList<Integer> iconList;
    ArrayList<String> titleList;
    ArrayList<Integer> imageList;
    TextView points;
    int value;
    TextView notactivate;
    RequestQueue queue;
    BoomMenuButton bmb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notactivate = findViewById(R.id.TV_not_activated);
        ImageView pic = findViewById(R.id.IV_avatar_main);
        bmb = findViewById(R.id.bmb);
        burgerslide = findViewById(R.id.IV_burger_pic_slide);
        drawerLayout = findViewById(R.id.drawerlayout);
        points = findViewById(R.id.points);

        Intent intent = getIntent();
        value = intent.getIntExtra("val", 0);
        if (value == 1) {
            notactivate.setVisibility(View.INVISIBLE);
        }

        value = intent.getIntExtra("Avater", 0);

        if (value == 1) {

            pic.setImageResource(R.drawable.male);
        } else {
            pic.setImageResource(R.drawable.female);
        }

        iconList = new ArrayList<>();
        titleList = new ArrayList<>();


        setInitBoom();
        boomCustomize();

        imageList = new ArrayList<>();

        initSetPic();


        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mNavigationView = findViewById(R.id.navView);
        header = mNavigationView.getHeaderView(0);

        queue = Volley.newRequestQueue(this);
        getAds();
        getPoints();
        burgerSlider();
    }

    private void getPoints() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, EndPoints.GET_POINT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("points", response.toString());
                points.setText("Total Points: " + response);
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

        queue.add(stringRequest);

    }

    private void getAds() {
        JsonObjectRequest adsRequest = new JsonObjectRequest(Request.Method.GET, EndPoints.GET_ADS_DATA, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("Ads", response.toString());

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

        queue.add(adsRequest);

    }

    private void burgerSlider() {

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int i = 0;

            public void run() {
                burgerslide.setImageResource(imageList.get(i));
                i++;
                if (i > imageList.size() - 1) {
                    i = 0;
                }
                handler.postDelayed(this, 2000);
            }
        };
        handler.postDelayed(runnable, 2000);
    }

    private void initSetPic() {
        imageList.add(R.drawable.pic1);
        imageList.add(R.drawable.pic2);
        imageList.add(R.drawable.pic3);
    }

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
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void boomCustomize() {
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            TextInsideCircleButton.Builder builder = new TextInsideCircleButton.Builder()
                    .normalImageRes(iconList.get(i))
                    .normalText(titleList.get(i))
                    .rippleEffect(true)
                    .normalColorRes(R.color.lightyellow)

                    .highlightedColorRes(R.color.deepred)
                    .pieceColorRes(R.color.deepred)
                    .textGravity(Gravity.CENTER)
                    .typeface(Typeface.DEFAULT_BOLD)
                    .normalTextColorRes(R.color.black)
                    .textSize(10)
                    .imagePadding(new Rect(20, 20, 20, 20))
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
            bmb.addBuilder(builder);
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
        Intent intent = new Intent(MainActivity.this, ScanQRActivity.class);
        startActivity(intent);
    }

    public void btnActivate(View view) {
        Intent intent = new Intent(MainActivity.this, AddReferralActivity.class);
        intent.putExtra("val", 1);
        startActivity(intent);

    }
}
