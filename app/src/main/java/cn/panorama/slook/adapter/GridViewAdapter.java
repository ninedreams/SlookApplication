package cn.panorama.slook.adapter;

import android.app.Application;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cn.panorama.slook.ui.R;
import cn.panorama.slook.data.GridViewData;
import cn.panorama.slook.view.StaggerImageView;
import cn.panorama.slook.data.StaggerItem;

/**
 * Created by xingyaoma on 16-5-21.
 * 二级分类 分类的adapter
 */
public class GridViewAdapter extends ArrayAdapter<String> {


    private Context mContext;
    private Application mAppContext;
    private GridViewData mData = new GridViewData();
    private ArrayList<StaggerItem> mItems = new ArrayList<StaggerItem>();

    private int newPos = 17;

    public GridViewAdapter(final Context context, Application app, final int textViewResourceId) {
        super(context, textViewResourceId);
        mAppContext =app;
        mContext = context;
        getMoreItem();
    }

    public void getMoreItem() {
        for (int i = 0; i < 18; i++) {
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
        newPos = (newPos - 1) % 17;
    }

    @Override
    public int getCount() {
        return mItems == null ? 0 : mItems.size();
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public String getItem(int position) {
        return null;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        final StaggerItem item = mItems.get(position);

        String url = item.url;
        String text = item.text;

        if (convertView == null) {
            ViewHolder holder = new ViewHolder();
            view = View.inflate(mContext, R.layout.grid_item, null);
            holder.img_content = (StaggerImageView) view.findViewById(R.id.image_gridview);
            holder.tv_info = (TextView) view.findViewById(R.id.text_gridview);

            view.setTag(holder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();

        holder.tv_info.setText(text);
        if(holder.tv_info.getId() == 0){
            holder.tv_info.setBackgroundResource(R.color.yellow);
        }
        if(holder.tv_info.getId() == mItems.size()-1){
            holder.tv_info.setBackgroundResource(R.color.yellow);
        }

        holder.img_content.mHeight = item.height;
        holder.img_content.mWidth = item.width;

        Picasso.with(mAppContext).load(url).into(holder.img_content);
        return view;
    }

    class ViewHolder {
        StaggerImageView img_content;
        TextView tv_info;
    }
}
