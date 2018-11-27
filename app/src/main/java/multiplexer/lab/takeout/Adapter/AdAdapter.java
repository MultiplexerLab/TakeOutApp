package multiplexer.lab.takeout.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import multiplexer.lab.takeout.R;

public class AdAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    ArrayList<String> arrayList;
    private static final int MAX_WIDTH = 1366;
    private static final int MAX_HEIGHT = 768;
    public AdAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        if(arrayList != null){
            return arrayList.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = layoutInflater.inflate(R.layout.viewpager_adapter, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.viewPagerItem_image1);

        int sizeBM = (int) Math.ceil(Math.sqrt(MAX_WIDTH * MAX_HEIGHT));
        Picasso.with(context)
                .load(arrayList.get(position))
                .skipMemoryCache()
                .resize(sizeBM, sizeBM)
                .onlyScaleDown()
                .centerInside()
                .into(imageView);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}