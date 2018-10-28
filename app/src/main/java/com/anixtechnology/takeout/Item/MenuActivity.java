package com.anixtechnology.takeout.Item;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.anixtechnology.takeout.Adapter.CustomAdapter;
import com.anixtechnology.takeout.Model.Menu;
import com.anixtechnology.takeout.R;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {


    private List<Menu> menuList = new ArrayList<>();

    private RecyclerView recyclerView;
    private CustomAdapter cAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        recyclerView = findViewById(R.id.recycler_view);
        cAdapter = new CustomAdapter(MenuActivity.this,menuList);
        RecyclerView.LayoutManager cLayoutManager = new LinearLayoutManager(MenuActivity.this, LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(cLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL));
        recyclerView.setAdapter(cAdapter);

        input();

    }


    public void input() {
        menuList.add(new Menu(R.drawable.pic3, R.drawable.pic1b) );
        menuList.add(new Menu(R.drawable.pic2m, R.drawable.pic2b) );
        menuList.add(new Menu(R.drawable.pic3m, R.drawable.pic3b) );
        menuList.add(new Menu(R.drawable.pic3, R.drawable.pic1b) );
        menuList.add(new Menu(R.drawable.pic2m, R.drawable.pic2b) );
        menuList.add(new Menu(R.drawable.pic3m, R.drawable.pic3b) );
        menuList.add(new Menu(R.drawable.pic3, R.drawable.pic1b) );
        menuList.add(new Menu(R.drawable.pic2m, R.drawable.pic2b) );
        menuList.add(new Menu(R.drawable.pic3m, R.drawable.pic3b) );
        menuList.add(new Menu(R.drawable.pic3, R.drawable.pic1b) );
        menuList.add(new Menu(R.drawable.pic2m, R.drawable.pic2b) );
        menuList.add(new Menu(R.drawable.pic3m, R.drawable.pic3b) );
    }

}
