package cn.panorama.slook.adapter;

import android.app.Application;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cn.panorama.slook.ui.R;
import cn.panorama.slook.utils.GridViewData;
import cn.panorama.slook.utils.stagger.Item;
import cn.panorama.slook.utils.stagger.STGVImageView;

/**
 * Created by xingyaoma on 16-5-21.
 * 二级分类 分类的adapter
 */
public class GridViewAdapter extends BaseAdapter {


    private Context mContext;
    private Application mAppContext;
    private GridViewData mData = new GridViewData();
    private ArrayList<Item> mItems = new ArrayList<Item>();

    private int newPos = 20;

    public GridViewAdapter(Context context, Application app) {
        mContext = context;
        mAppContext = app;
        getMoreItem();
    }

    public void getMoreItem() {
        for (int i = 0; i < 21; i++) {
            Item item = new Item();
            item.text = mData.text[i];
            item.url = mData.url[i];
            item.width = mData.width[i];
            item.height = mData.width[i];
            mItems.add(item);
        }
    }

    public void getNewItem() {
        Item item = new Item();
        item.text = mData.text[newPos];
        item.url = mData.url[newPos];
        item.width = mData.width[newPos];
        item.height = mData.width[newPos];
        mItems.add(0, item);
        newPos = (newPos - 1) % 20;
    }

    @Override
    public int getCount() {
        return mItems == null ? 0 : mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        final Item item = mItems.get(position);

        String url = item.url;
        String text = item.text;

        if (convertView == null) {
            Holder holder = new Holder();
            view = View.inflate(mContext, R.layout.grid_item, null);
            holder.img_content = (STGVImageView) view.findViewById(R.id.image_gridview);
            holder.tv_info = (TextView) view.findViewById(R.id.text_gridview);

            view.setTag(holder);
        } else {
            view = convertView;
        }

        Holder holder = (Holder) view.getTag();

        /**
         * StaggeredGridView has bugs dealing with child TouchEvent
         * You must deal TouchEvent in the child view itself
         **/
        holder.img_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        holder.tv_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.tv_info.setText(text);

        holder.img_content.mHeight = item.height;
        holder.img_content.mWidth = item.width;

        Picasso.with(mAppContext).load(url).into(holder.img_content);
        return view;
    }

    class Holder {
        STGVImageView img_content;
        TextView tv_info;
    }
}
