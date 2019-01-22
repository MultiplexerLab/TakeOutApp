package multiplexer.lab.takeout.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import multiplexer.lab.takeout.ItemActivity.ProductActivity;
import multiplexer.lab.takeout.Model.Category;
import multiplexer.lab.takeout.R;

public class AdAdapterNew extends RecyclerView.Adapter<AdAdapterNew.MyViewHolder>{
    List<String> adList;
    Context context;
    private static final int MAX_WIDTH = 1366;
    private static final int MAX_HEIGHT = 768;
    public AdAdapterNew(Context context, List<String> List) {
        this.adList = List;
        this.context = context;
    }
    @Override
    public AdAdapterNew.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.custom_layout_ad, parent, false);
        return new AdAdapterNew.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AdAdapterNew.MyViewHolder holder, int position) {
        final String str = adList.get(position);
        int sizeBM = (int) Math.ceil(Math.sqrt(MAX_WIDTH * MAX_HEIGHT));
        Picasso.with(context).load(str)
                .skipMemoryCache()
                .resize(sizeBM, sizeBM)
                .onlyScaleDown()
                .centerInside()
                .into(holder.adPic);
    }

    @Override
    public int getItemCount() {
        return adList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView adPic;

        public MyViewHolder(View view) {
            super(view);
            adPic = view.findViewById(R.id.IV_ad);
        }
    }




}
