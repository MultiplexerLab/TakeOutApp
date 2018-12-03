package multiplexer.lab.takeout.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import multiplexer.lab.takeout.Model.Product;
import multiplexer.lab.takeout.R;


import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {

    List<Product> mList;
    Context context;

    public MenuAdapter(Context context, List<Product> List) {
        this.mList = List;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView smallpic, bigpic;
        TextView name, description, price;
        RelativeLayout rlayout,bigrlayout;

        public MyViewHolder(View view) {
            super(view);
            smallpic = view.findViewById(R.id.IV_burger_menu_pic);
            bigpic = ((Activity) context).findViewById(R.id.bigpic);
            name = view.findViewById(R.id.prod_name);
            rlayout = view.findViewById(R.id.RL_menu_item);
            description = ((Activity) context).findViewById(R.id.prod_desc);
            bigrlayout = ((Activity) context).findViewById(R.id.RL_menu_detail);
            price = ((Activity) context).findViewById(R.id.prod_price);
        }
    }


    @Override
    public MenuAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_layout_menu, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MenuAdapter.MyViewHolder holder, final int position) {
        final Product product = mList.get(position);
        Picasso.with(context).load(product.getImage()).into(holder.smallpic);

        holder.rlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.with(context).load(product.getImage()).into(holder.bigpic);
                holder.name.setText("Name: "+product.getName());
                holder.description.setText("Description: "+product.getDescription());
                holder.price.setText("Price: "+product.getPrice());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
