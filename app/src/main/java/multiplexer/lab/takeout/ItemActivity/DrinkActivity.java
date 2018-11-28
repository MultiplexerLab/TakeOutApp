package multiplexer.lab.takeout.ItemActivity;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import multiplexer.lab.takeout.Adapter.MenuAdapter;
import multiplexer.lab.takeout.Model.Menu;
import multiplexer.lab.takeout.R;

public class DrinkActivity extends AppCompatActivity {
    final Context context = this;
    private List<Menu> menuList = new ArrayList<>();

    private RecyclerView recyclerView;
    private MenuAdapter cAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        recyclerView = findViewById(R.id.recycler_view);
        cAdapter = new MenuAdapter(DrinkActivity.this,menuList);
        RecyclerView.LayoutManager cLayoutManager = new LinearLayoutManager(DrinkActivity.this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
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
    }

    public void btnRating(View view) {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_dialog);
        Button dialogButton = dialog.findViewById(R.id.btn_submit);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
