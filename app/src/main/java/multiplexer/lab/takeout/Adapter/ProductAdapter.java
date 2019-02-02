package multiplexer.lab.takeout.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hsalf.smilerating.SmileRating;
import com.squareup.picasso.Picasso;

import multiplexer.lab.takeout.Helper.EndPoints;
import multiplexer.lab.takeout.ItemActivity.ProductShowActivity;
import multiplexer.lab.takeout.Model.Product;
import multiplexer.lab.takeout.R;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    List<Product> productList;
    Context context;
    RequestQueue queue;
    String personId;

    public ProductAdapter(Context context, List<Product> productList) {
        this.productList = productList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView smallpic;
        TextView foodname, rate, price;
        RelativeLayout layout;

        public MyViewHolder(View view) {
            super(view);
            smallpic = view.findViewById(R.id.IV_burger_menu_pic);
            foodname = view.findViewById(R.id.foodname);
            rate = view.findViewById(R.id.foodrate);
            price = view.findViewById(R.id.foodprice);
            layout = view.findViewById(R.id.RL_menu_item);
            queue = Volley.newRequestQueue(context);
        }
    }

    @Override
    public ProductAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_layout_product, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ProductAdapter.MyViewHolder holder, final int position) {
        final Product product = productList.get(position);
        Picasso.with(context).load(product.getImage()).into(holder.smallpic);

        holder.foodname.setText(product.getName());
        holder.rate.setText("Rating: " + product.getCustomer_rating());
        holder.price.setText("Price: " + product.getPrice() + " BDT");

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductShowActivity.class);
                intent.putExtra("name", product.getName());
                intent.putExtra("rating", product.getRating());
                intent.putExtra("price", product.getPrice());
                intent.putExtra("description", product.getDescription());
                intent.putExtra("picaddress", product.getImage());
                intent.putExtra("prodid", product.getId());
                intent.putExtra("customerrate", product.getCustomer_rating());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
