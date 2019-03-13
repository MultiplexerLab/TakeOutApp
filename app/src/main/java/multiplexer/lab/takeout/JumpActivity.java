package multiplexer.lab.takeout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class JumpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jump);
    }

    public void jumpToWebsite(View view) {
        int id = view.getId();
        String url = "";
        if (id == R.id.foodpanda) {
            url = "https://www.foodpanda.com.bd/";
        } /*else if (id == R.id.hungrynaki) {
            url = "https://hungrynaki.com/";
        } else if (id == R.id.ubereats) {
            url = "https://www.uber.com/en-BD/cities/dhaka/";
        } else if (id == R.id.pathaofood) {
            url = "https://pathao.com/food";
        }*/
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}
