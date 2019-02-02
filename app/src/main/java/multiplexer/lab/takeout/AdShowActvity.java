package multiplexer.lab.takeout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class AdShowActvity extends AppCompatActivity {

    ImageView adImage;
    TextView adMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adshow);

        adImage = findViewById(R.id.adImage);
        adMessage = findViewById(R.id.adMessage);

        String imageUrl = getIntent().getStringExtra("imageUrl");
        Picasso.with(this).load(imageUrl).into(adImage);
        adMessage.setText(getIntent().getStringExtra("message"));
    }
    public void btnFbPage(View view) {

        String url = "https://www.facebook.com/bdtakeout/";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}
