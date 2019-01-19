package multiplexer.lab.takeout.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;

import multiplexer.lab.takeout.ItemActivity.ProductActivity;
import multiplexer.lab.takeout.Model.Category;
import multiplexer.lab.takeout.R;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {
    List<Category> catList;
    Context context;

    public MenuAdapter(Context context, List<Category> List) {

        this.catList = List;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView cat_Logo;
        TextView cat_name;
        RelativeLayout relativeLayout;

        public MyViewHolder(View view) {
            super(view);
            cat_name = view.findViewById(R.id.cat_name);
            cat_Logo = view.findViewById(R.id.prod_logo);
            relativeLayout = view.findViewById(R.id.Rlayout_category);

        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.custom_layout_menu, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MenuAdapter.MyViewHolder holder, int position) {
        final Category category = catList.get(position);

        Picasso.with(context).load(category.getImage()).into(holder.cat_Logo);
        holder.cat_name.setText(category.getName());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra("CatId",category.getCatid());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return catList.size();
    }
}
