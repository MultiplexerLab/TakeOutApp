package multiplexer.lab.takeout.ItemActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import multiplexer.lab.takeout.Adapter.CustomAdapter;
import multiplexer.lab.takeout.Model.Menu;
import multiplexer.lab.takeout.R;


import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }




    public void btnChefSpecial(View view) {
        Intent intent = new Intent(MenuActivity.this, ChefSpecialActivity.class);
        startActivity(intent);
    }

    public void btnBurger(View view) {
        Intent intent = new Intent(MenuActivity.this, BurgerActivity.class);
        startActivity(intent);
    }

    public void btnSides(View view) {
        Intent intent = new Intent(MenuActivity.this, SidesActivity.class);
        startActivity(intent);
    }

    public void btnDrinks(View view) {
        Intent intent = new Intent(MenuActivity.this, DrinkActivity.class);
        startActivity(intent);
    }
}
