package multiplexer.lab.takeout.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;
import multiplexer.lab.takeout.Model.Store;
import multiplexer.lab.takeout.R;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.MyViewHolder> {


    List<Store> sList;
    Context context;

    public StoreAdapter(Context context, List<Store> List) {
        this.sList = List;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, address;
        ImageView call, location;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.TV_place_name);
            address = view.findViewById(R.id.TV_store_address);
            call = view.findViewById(R.id.IV_call);
            location = view.findViewById(R.id.IV_direction);

            //IVLargepic = ((Activity) context).findViewById(R.id.IV_menu_detail);
        }
    }


    @Override
    public StoreAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_layout_store, parent, false);

        return new StoreAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final StoreAdapter.MyViewHolder holder, int position) {
        final Store store = sList.get(position);
        holder.name.setText(store.getName());
        holder.address.setText(store.getAddress());

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", store.getPhone(), null));
                context.startActivity(intent);
            }
        });

        holder.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gmmIntentUri = String.format(Locale.ENGLISH, "geo:%f,%f", store.getLatitude(), store.getLongitude());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(gmmIntentUri));
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(mapIntent);
                }else{
                    Toast.makeText(context, "You don't have the permission to open Map", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return sList.size();
    }
}
