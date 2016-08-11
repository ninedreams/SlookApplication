package cn.panorama.slook.adapter;

import android.app.Application;
import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.etsy.android.grid.util.DynamicHeightTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import cn.panorama.slook.ui.R;
import cn.panorama.slook.view.StaggerImageView;
import cn.panorama.slook.data.StaggerItem;
import cn.panorama.slook.data.StaggerViewData;


/**
 * 二级分类 特色 的 adapter
 */
public class StaggerAdapter extends ArrayAdapter<String> {
    private static final String TAG = "StaggerAdapter";
    private Application mAppContext;
    private Context mContext;
    private StaggerViewData mData = new StaggerViewData();
    private ArrayList<StaggerItem> mItems = new ArrayList<StaggerItem>();

    private int newPos = 19;

    static class ViewHolder {
        DynamicHeightTextView txinfo;
        StaggerImageView img;
    }

    private final LayoutInflater mLayoutInflater;
    private final Random mRandom;

    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();

    public StaggerAdapter(final Context context, Application app, final int textViewResourceId) {
        super(context, textViewResourceId);
        mAppContext =app;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mRandom = new Random();
        getMoreItem();
    }

    public void getMoreItem() {
        for (int i = 0; i < 20; i++) {
            StaggerItem item = new StaggerItem();
            item.text = mData.text[i];
            item.url = mData.url[i];
            item.width = mData.width[i];
            item.height = mData.width[i];
            mItems.add(item);
        }
    }

    public void getNewItem() {
        StaggerItem item = new StaggerItem();
        item.text = mData.text[newPos];
        item.url = mData.url[newPos];
        item.width = mData.width[newPos];
        item.height = mData.width[newPos];
        mItems.add(0, item);
        newPos = (newPos - 1) % 19;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent) {

        final StaggerItem item = mItems.get(position);

        String url = item.url;
        String text = item.text;

        ViewHolder vh;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.stagger_item, parent, false);
            vh = new ViewHolder();
            vh.txinfo = (DynamicHeightTextView) convertView.findViewById(R.id.tv_stagger);
            vh.img = (StaggerImageView) convertView.findViewById(R.id.img_stagger);

            convertView.setTag(vh);
        }
        else {
            vh = (ViewHolder) convertView.getTag();
        }

        double positionHeight = getPositionRatio(position);
        //Log.d(TAG, "getView position:" + position + " h:" + positionHeight);
        //vh.txinfo.setHeightRatio(positionHeight);
        vh.img.mWidth = item.width;
        vh.img.mHeight = (int)(item.width * (mRandom.nextDouble() /2 +1));
        vh.txinfo.setText(text);
        Picasso.with(mAppContext).load(url).into(vh.img);

        return convertView;
    }

    @Override
    public int getCount() {
        return mItems == null ? 0 : mItems.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        // if not yet done generate and stash the columns height
        // in our real world scenario this will be determined by
        // some match based on the known height and width of the image
        // and maybe a helpful way to get the column height!
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
            //Log.d(TAG, "getPositionRatio:" + position + " ratio:" + ratio);
        }
        return ratio;
    }


    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5 the width
    }
}
