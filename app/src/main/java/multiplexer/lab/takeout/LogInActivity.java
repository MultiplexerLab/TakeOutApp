package multiplexer.lab.takeout;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class LogInActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    LinearLayout loginLayout;
    Snackbar snackbar;
    RelativeLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        etEmail = findViewById(R.id.et_login_email);
        etPassword = findViewById(R.id.et_login_password);
        loginLayout = findViewById(R.id.LL_input);
        rootLayout = findViewById(R.id.rootLayout);
        //createNotificationChannel();
        animation();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "myChannel";
            String description = "myDescription";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("123", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void animation() {
        YoYo.with(Techniques.FadeIn).duration(2000).playOn(findViewById(R.id.iv_b1));
        YoYo.with(Techniques.FadeIn).duration(2000).playOn(findViewById(R.id.iv_b2));
        YoYo.with(Techniques.FadeIn).duration(2000).playOn(findViewById(R.id.iv_b3));
        YoYo.with(Techniques.FadeIn).duration(2000).playOn(findViewById(R.id.iv_b4));
        YoYo.with(Techniques.FadeIn).duration(2000).playOn(findViewById(R.id.iv_b5));
        YoYo.with(Techniques.FadeIn).duration(2000).playOn(findViewById(R.id.iv_b6));
        YoYo.with(Techniques.FadeIn).duration(2000).playOn(findViewById(R.id.iv_b7));
        YoYo.with(Techniques.FadeIn).duration(2000).playOn(findViewById(R.id.iv_b8));
        YoYo.with(Techniques.FadeIn).duration(2000).playOn(findViewById(R.id.iv_b9));
        YoYo.with(Techniques.FadeIn).duration(2000).playOn(findViewById(R.id.iv_b10));
        YoYo.with(Techniques.FadeIn).duration(2000).playOn(findViewById(R.id.iv_b11));
    }

    public void btnLogIn(View view) {
        if (internetConnected()) {
            if (snackbar != null) {
                if (snackbar.isShown()) {
                    snackbar.dismiss();
                }
            }
            if (validation()) {
                Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                YoYo.with(Techniques.Shake)
                        .duration(1000)
                        .repeat(0)
                        .playOn(loginLayout);
            }
        } else {
            showSnackBar();
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

    public void showSnackBar() {
        snackbar = Snackbar
                .make(rootLayout, "Internet is not connected!", Snackbar.LENGTH_INDEFINITE)
                .setAction("Connect", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent settingsIntent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        startActivityForResult(settingsIntent, 9003);
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

    public void btnSignUp(View view) {
        Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    public boolean validation() {
        boolean error = true;
        etEmail.setError(null);
        etPassword.setError(null);

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty()) {
            etEmail.setError("Email Address is missing!");
            error = false;
        } else if (!email.endsWith(".com")) {
            etEmail.setError("Enter a valid Email Address");
            error = false;
        }
        if (password.isEmpty()) {
            etPassword.setError("Neeed a Password");
            error = false;
        } else if (password.length() < 8) {
            etPassword.setError("Password is too SHORT!!");
            error = false;
        }
        return error;

    }
}
