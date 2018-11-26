package multiplexer.lab.takeout;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreenActivity extends AppCompatActivity {

    ImageView LogoPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Animation();

        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
                    String accessToken = pref.getString("accessToken", "");
                    Log.i("accessToken", accessToken);
                    if(accessToken.length()>20) {
                        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Intent intent = new Intent(SplashScreenActivity.this, LogInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        };
        timer.start();
    }

    public void Animation() {
        LogoPic = findViewById(R.id.IV_splash_logo);

        Animation myanime = AnimationUtils.loadAnimation(this, R.anim.transition);
        LogoPic.startAnimation(myanime);

    }
}
