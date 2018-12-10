package multiplexer.lab.takeout.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
        ImageView smallpic, bigpic;
        TextView name, description, price;
        RelativeLayout rlayout,bigrlayout;

        public MyViewHolder(View view) {
            super(view);
            smallpic = view.findViewById(R.id.IV_burger_menu_pic);
            bigpic = ((Activity) context).findViewById(R.id.bigpic);
            name = ((Activity) context).findViewById(R.id.prod_name);
            rlayout = view.findViewById(R.id.RL_menu_item);
            description = ((Activity) context).findViewById(R.id.prod_desc);
            bigrlayout = ((Activity) context).findViewById(R.id.RL_menu_detail);
            price = ((Activity) context).findViewById(R.id.prod_price);
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

        if(position==0){
            holder.bigrlayout.setVisibility(View.VISIBLE);
            Picasso.with(context).load(product.getImage()).into(holder.bigpic);
            holder.name.setText("Name: "+product.getName());
            holder.description.setText("Description: "+product.getDescription());
            holder.price.setText("Price: "+product.getPrice());
        }

        holder.rlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.with(context).load(product.getImage()).into(holder.bigpic);
                holder.name.setText("Name: "+product.getName());
                holder.description.setText("Description: "+product.getDescription());
                holder.price.setText("Price: "+product.getPrice());
            }
        });
        holder.bigrlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.custom_dialog);
                Button dialogButton = dialog.findViewById(R.id.btn_submit);
                SmileRating globalsmileRating= dialog.findViewById(R.id.smile_rating_global);
                final SmileRating smileRating= dialog.findViewById(R.id.smile_rating);
                globalsmileRating.setSelectedSmile(product.getCustomer_rating());
                smileRating.setSelectedSmile(product.getRating());

                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int rating = smileRating.getRating();
                        product.setRating(rating);
                        postRating(product.getId(),rating);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void postRating(final int productid, final int rating){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EndPoints.POST_PRODUCT_RATING+personId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, "Thanks for your rating!", Toast.LENGTH_SHORT).show();

                        Log.i("Rate Response", response.toString());
                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if(response!=null) {
                    Log.e("networkResponse", response.toString());
                    if (error instanceof ServerError && response != null) {
                        try {
                            String res = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers, "application/json"));
                            Log.i("Rate String", res);

                        } catch (UnsupportedEncodingException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        })

        {
            @Override
            protected Map<String, String> getParams() {


                Map<String, String> params = new HashMap<String, String>();
                params.put("Prodctid", String.valueOf(productid));
                params.put("Rating", String.valueOf(rating));

                return params;
            }
        };
        queue.add(stringRequest);
    }

}
