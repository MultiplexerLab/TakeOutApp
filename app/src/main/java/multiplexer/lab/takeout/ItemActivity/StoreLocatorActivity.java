package multiplexer.lab.takeout.ItemActivity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import multiplexer.lab.takeout.R;


public class StoreLocatorActivity extends AppCompatActivity {
    ArrayAdapter<String> adapter;
    String arr[] = {"Bangladesh", "Sri Lanka"};
    Spinner spinnerCountry2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_locator);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinnerCountry2 = findViewById(R.id.spinnerCountry2);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arr);
        spinnerCountry2.setAdapter(adapter);
    }

    public void mapJump(View view) {
        int id = view.getId();

        if(id==R.id.IV_direction){
            jumpToMap("Abedin Tower (1st floor), 35 Kemal Ataturk Avenue, Banani (2.83 mi)\n" +
                    "Dhaka, Bangladesh 1213");
        }else if(id==R.id.IV_direction1){
            jumpToMap("736 Rangs KB Square, Level - 3, Road - 9/a, Satmosjid Road, Dhanmondi, 1209 Dhaka, Bangladesh");
        }else if(id==R.id.IV_direction2){
            jumpToMap("17 Sonargao Janapath, Level - 3, Sector - 13, Uttara Model Town, 1230 Dhaka, Bangladesh");
        }
    }

    public void callStore(View view) {
        int id = view.getId();

        if(id==R.id.IV_call){
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "01847290010", null));
            startActivity(intent);
        }else if(id==R.id.IV_call1){
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "01847290010", null));
            startActivity(intent);
        }else if(id==R.id.IV_call2){
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "01847290010", null));
            startActivity(intent);
        }
    }

    public void jumpToMap(String location){
        Uri gmmIntentUri = Uri.parse("geo:0,0?q="+location);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }else{
            Toast.makeText(this, "You don't have the permission to open Map", Toast.LENGTH_SHORT).show();
        }
    }
}
