package multiplexer.lab.takeout.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import multiplexer.lab.takeout.Model.Menu;
import multiplexer.lab.takeout.R;


import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {


    List<Menu> mList;
    Context context;

    public MenuAdapter(Context context, List<Menu> List) {
        this.mList = List;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView IVPic, IVLargepic;
        RelativeLayout rlayout;

        public MyViewHolder(View view) {
            super(view);
            IVPic = view.findViewById(R.id.IV_burger_menu_pic);
            rlayout = view.findViewById(R.id.RL_menu_item);
            IVLargepic = ((Activity) context).findViewById(R.id.IV_menu_detail);
        }
    }


    @Override
    public MenuAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_layout_menu, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MenuAdapter.MyViewHolder holder, int position) {
        final Menu menu = mList.get(position);
        holder.IVPic.setBackgroundResource(menu.getShortPicId());

        holder.rlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.IVLargepic.setImageResource(menu.getLongPicId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
