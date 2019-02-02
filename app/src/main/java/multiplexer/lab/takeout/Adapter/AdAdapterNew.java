package multiplexer.lab.takeout.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import multiplexer.lab.takeout.AdShowActvity;
import multiplexer.lab.takeout.Model.Ad;
import multiplexer.lab.takeout.R;

public class AdAdapterNew extends RecyclerView.Adapter<AdAdapterNew.MyViewHolder> {
    List<Ad> adList;
    Context context;
    private static final int MAX_WIDTH = 1366;
    private static final int MAX_HEIGHT = 768;

    public AdAdapterNew(Context context, List<Ad> List) {
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
        final Ad ad = adList.get(position);
        int sizeBM = (int) Math.ceil(Math.sqrt(MAX_WIDTH * MAX_HEIGHT));
        Log.i("Adlink", ad.getPic());
        Picasso.with(context).load(ad.getPic())
                .skipMemoryCache()
                .resize(sizeBM, sizeBM)
                .onlyScaleDown()
                .centerInside()
                .into(holder.adPic);
        holder.message.setText(ad.getMessage());

        holder.adPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AdShowActvity.class);
                intent.putExtra("imageUrl", ad.getPic());
                intent.putExtra("message", ad.getMessage());
                context.startActivity(intent);            }
        });
    }

    @Override
    public int getItemCount() {
        return adList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView adPic;
        RelativeLayout relativeLayout;
        TextView message;

        public MyViewHolder(View view) {
            super(view);
            adPic = view.findViewById(R.id.IV_ad);
            message = view.findViewById(R.id.message);
            relativeLayout = view.findViewById(R.id.relativeLayout);
        }
    }
}
