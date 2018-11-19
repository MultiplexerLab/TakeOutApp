package multiplexer.lab.takeout;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.anixtechnology.takeout.R;

public class SplashScreenActivity extends AppCompatActivity {

    ImageView LogoPic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        Animation();


        final Intent intent = new Intent(SplashScreenActivity.this, LogInActivity.class);
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {

                    startActivity(intent);

                    finish();
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
