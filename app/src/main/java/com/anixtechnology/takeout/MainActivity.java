package com.anixtechnology.takeout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.anixtechnology.takeout.Item.AboutUsActivity;
import com.anixtechnology.takeout.Item.AddReferralActivity;
import com.anixtechnology.takeout.Item.MenuActivity;
import com.anixtechnology.takeout.Item.ProfileActivity;
import com.anixtechnology.takeout.Item.ScanQRActivity;
import com.anixtechnology.takeout.Item.StoreLocatorActivity;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    NavigationView mNavigationView;
    View header;

    ImageView burgerslide;

    ArrayList<Integer> iconList;
    ArrayList<String> titleList;
    ArrayList<Integer> imageList;

    BoomMenuButton bmb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iconList = new ArrayList<>();
        titleList = new ArrayList<>();

        bmb = findViewById(R.id.bmb);
        SetInitBoom();
        BoomCustomize();

        imageList = new ArrayList<>();
        burgerslide =findViewById(R.id.IV_burger_pic_slide);
        InitSetPic();
        BurgerSlider();


        drawerLayout = findViewById(R.id.drawerlayout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mNavigationView = findViewById(R.id.navView);
        header = mNavigationView.getHeaderView(0);
    }

    private void BurgerSlider() {

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

    private void InitSetPic() {
        imageList.add(R.drawable.pic1);
        imageList.add(R.drawable.pic2);
        imageList.add(R.drawable.pic3);
    }

    private void SetInitBoom() {
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

    public void BoomCustomize(){
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder()
                    .normalImageRes(iconList.get(i))
                    .normalText(titleList.get(i))
                    .rippleEffect(true)
                    .normalColorRes(R.color.lightestyellow)
                    .highlightedColorRes(R.color.deepred)
                    .pieceColorRes(R.color.deepred)
                    .textGravity(Gravity.CENTER)
                    .typeface(Typeface.DEFAULT_BOLD)
                    .textSize(16)
                    .shadowEffect(true)
                    .rotateImage(true)
                    .rotateText(true)
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int i) {
                            Intent intent = null;
                            switch (i){
                                case 0: intent = new Intent(MainActivity.this, ProfileActivity.class);
                                break;
                                case 1: intent = new Intent(MainActivity.this, AboutUsActivity.class);
                                    break;
                                case 2: intent = new Intent(MainActivity.this, ScanQRActivity.class);
                                    break;
                                case 3: intent = new Intent(MainActivity.this, StoreLocatorActivity.class);
                                    break;
                                case 4: String url = "https://www.foodpanda.com.bd/";
                                    intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(url));
                                    break;
                                case 5: intent = new Intent(MainActivity.this, AddReferralActivity.class);
                                    break;
                                case 6: intent = new Intent(MainActivity.this, LogInActivity.class);
                                    break;
                            }
                            startActivity(intent);
                        }
                    });
            bmb.addBuilder(builder);
        }
    }

    public void BtnScanQR(MenuItem item) {
        Intent intent = new Intent(MainActivity.this,ScanQRActivity.class);
        startActivity(intent);
    }

    public void BtnProfile(MenuItem item) {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    public void BtnAboutUs(MenuItem item) {
        Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
        startActivity(intent);
    }

    public void BtnStoreLocator(MenuItem item) {
        Intent intent = new Intent(MainActivity.this, StoreLocatorActivity.class);
        startActivity(intent);
    }

    public void BtnHomeDelivery(MenuItem item) {
        String url = "https://www.foodpanda.com.bd/";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    public void BtnAddReferral(MenuItem item) {
        Intent intent = new Intent(MainActivity.this, AddReferralActivity.class);
        startActivity(intent);
    }

    public void BtnLogOut(MenuItem item) {
        Intent intent = new Intent(MainActivity.this, LogInActivity.class);
        startActivity(intent);
    }

    public void BtnMenu(View view) {
        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
        startActivity(intent);
    }
}
