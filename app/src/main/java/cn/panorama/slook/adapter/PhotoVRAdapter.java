package cn.panorama.slook.adapter;

import android.app.Application;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.panwrona.downloadprogressbar.library.DownloadProgressBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cn.panorama.slook.ui.R;
import cn.panorama.slook.data.PhotoVRData;
import cn.panorama.slook.data.PhotoVRItem;

/**
 * Created by xingyaoma on 16-7-14.
 */
public class PhotoVRAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private Application mAppContext;
    private PhotoVRData mData = new PhotoVRData();
    private ArrayList<PhotoVRItem> mItems = new ArrayList<PhotoVRItem>();

    private int val = 0;


    private int newPos = 7;

    public PhotoVRAdapter(final Context context, Application app, int textViewResourceId) {
        super(context, textViewResourceId);
        mAppContext =app;
        mContext = context;
        getMoreItem();
    }

    public void getMoreItem() {
        for (int i = 0; i < 8; i++) {
            PhotoVRItem item = new PhotoVRItem();
            item.title_place = mData.title_place[i];
            item.url = mData.url[i];
            item.nummber = mData.nummber[i];
            item.content_place = mData.content_place[i];
            item.size_place = mData.size_place[i];
            mItems.add(item);
        }
    }

    public void getNewItem() {
        PhotoVRItem item = new PhotoVRItem();
        item.title_place = mData.title_place[newPos];
        item.url = mData.url[newPos];
        item.nummber = mData.nummber[newPos];
        item.content_place = mData.content_place[newPos];
        item.size_place = mData.size_place[newPos];
        mItems.add(0, item);
        newPos = (newPos - 1) % 7;
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
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;
        final PhotoVRItem item = mItems.get(position);

        String url = item.url;
        String title_place = item.title_place;
        String content_place = item.content_place;
        double size_place = item.size_place;
        int nummber = item.nummber;

        if (convertView == null) {
            ViewHolder holder = new ViewHolder();
            view = View.inflate(mContext, R.layout.layout_photovr_item, null);
            holder.tv_num_photovr = (TextView) view.findViewById(R.id.tv_num_photovr);
            holder.tv_size_photovr = (TextView) view.findViewById(R.id.tv_size_photovr);
            holder.tv_title_photovr = (TextView) view.findViewById(R.id.tv_title_photovr);
            holder.downloadProgressView = (DownloadProgressBar) view.findViewById(R.id.dpv_photovr);
            holder.img_photovr = (ImageView) view.findViewById(R.id.img_photovr);
            holder.subtitle_tv_photovr = (TextView) view.findViewById(R.id.subtitle_tv_photovr);
            view.setTag(holder);
        } else {
            view = convertView;
        }

        final ViewHolder holder = (ViewHolder) view.getTag();

        holder.downloadProgressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.downloadProgressView.playManualProgressAnimation();
            }
        });
        holder.downloadProgressView.setOnProgressUpdateListener(new DownloadProgressBar.OnProgressUpdateListener() {
            @Override
            public void onProgressUpdate(float v) {

            }

            @Override
            public void onAnimationStarted() {
                holder.downloadProgressView.setEnabled(false);
            }

            @Override
            public void onAnimationEnded() {
                val = 0;
                holder.downloadProgressView.setEnabled(true);
            }

            @Override
            public void onAnimationSuccess() {

            }

            @Override
            public void onAnimationError() {

            }

            @Override
            public void onManualProgressStarted() {

            }

            @Override
            public void onManualProgressEnded() {

            }
        });

        holder.tv_title_photovr.setText(title_place);
        holder.tv_size_photovr.setText(String.valueOf(size_place)+"MB");
        holder.tv_num_photovr.setText(String.valueOf(nummber)+"次");
        holder.subtitle_tv_photovr.setText(content_place);
        Picasso.with(mAppContext).load(url).into(holder.img_photovr);
        return view;

    }

    static class ViewHolder{
        TextView subtitle_tv_photovr;
        ImageView img_photovr;
        TextView tv_title_photovr;
        TextView tv_size_photovr;
        TextView tv_num_photovr;
        DownloadProgressBar downloadProgressView;

    }
}
