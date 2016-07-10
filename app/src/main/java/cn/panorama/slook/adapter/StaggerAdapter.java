package cn.panorama.slook.adapter;

import android.app.Application;
import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.etsy.android.grid.util.DynamicHeightTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import cn.panorama.slook.ui.R;
import cn.panorama.slook.utils.stagger.GridViewData;
import cn.panorama.slook.utils.stagger.Item;


/**
 * 二级分类 特色 的 adapter
 */
public class StaggerAdapter extends ArrayAdapter<String> {
    private static final String TAG = "SampleAdapter";
    private Application mAppContext;
    private Context mContext;
    private GridViewData mData = new GridViewData();
    private ArrayList<Item> mItems = new ArrayList<Item>();

    static class ViewHolder {
        DynamicHeightTextView txtLineOne;
        ImageView img;
    }

    private final LayoutInflater mLayoutInflater;
    private final Random mRandom;
    private final ArrayList<Integer> mBackgroundColors;

    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();

    public StaggerAdapter(final Context context, Application app, final int textViewResourceId) {
        super(context, textViewResourceId);
        mAppContext =app;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mRandom = new Random();
        mBackgroundColors = new ArrayList<Integer>();
        mBackgroundColors.add(R.color.orange);
        mBackgroundColors.add(R.color.baby_green);
        mBackgroundColors.add(R.color.baby_blue);
        mBackgroundColors.add(R.color.yellow);
        mBackgroundColors.add(R.color.grey);
    }

    public void getMoreItem() {
        for (int i = 0; i < 18; i++) {
            Item item = new Item();
            item.text = mData.text[i];
            item.url = mData.url[i];
            item.width = mData.width[i];
            item.height = mData.width[i];
            mItems.add(item);
        }
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final Item item = mItems.get(position);

        String url = item.url;
        String text = item.text;

        ViewHolder vh;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.stagger_view, parent, false);
            vh = new ViewHolder();
            vh.txtLineOne = (DynamicHeightTextView) convertView.findViewById(R.id.txt_line1);
            vh.img = (ImageView) convertView.findViewById(R.id.stagger_img);

            convertView.setTag(vh);
        }
        else {
            vh = (ViewHolder) convertView.getTag();
        }

        double positionHeight = getPositionRatio(position);
        int backgroundIndex = position >= mBackgroundColors.size() ?
                position % mBackgroundColors.size() : position;

        convertView.setBackgroundResource(mBackgroundColors.get(backgroundIndex));

        //Log.d(TAG, "getView position:" + position + " h:" + positionHeight);

        vh.txtLineOne.setHeightRatio(positionHeight);
        vh.txtLineOne.setText(text);
        Picasso.with(mAppContext).load(url).into(vh.img);

        return convertView;
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
