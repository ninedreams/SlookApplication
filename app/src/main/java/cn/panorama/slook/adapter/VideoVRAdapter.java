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
import cn.panorama.slook.data.VideoVRData;
import cn.panorama.slook.data.VideoVRItem;

/**
 * Created by xingyaoma on 16-7-14.
 */
public class VideoVRAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private Application mAppContext;
    private VideoVRData mData = new VideoVRData();
    private ArrayList<VideoVRItem> mItems = new ArrayList<VideoVRItem>();

    private int val = 0;
    private int newPos = 7;

    public VideoVRAdapter(final Context context, Application app, int textViewResourceId) {
        super(context, textViewResourceId);
        mAppContext =app;
        mContext = context;
        getMoreItem();
    }


    public void getMoreItem() {
        for (int i = 0; i < 8; i++) {
            VideoVRItem item = new VideoVRItem();
            item.title_video = mData.title_place[i];
            item.url = mData.url[i];
            item.nummber = mData.nummber[i];
            item.content_video = mData.content_place[i];
            item.size_video = mData.size_place[i];
            mItems.add(item);
        }
    }

    public void getNewItem() {
        VideoVRItem item = new VideoVRItem();
        item.title_video = mData.title_place[newPos];
        item.url = mData.url[newPos];
        item.nummber = mData.nummber[newPos];
        item.content_video = mData.content_place[newPos];
        item.size_video = mData.size_place[newPos];
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
        final VideoVRItem item = mItems.get(position);

        String url = item.url;
        String title_video = item.title_video;
        String content_video = item.content_video;
        double size_video = item.size_video;
        int nummber = item.nummber;

        if (convertView == null) {
            ViewHolder holder = new ViewHolder();
            view = View.inflate(mContext, R.layout.layout_videovr_item, null);
            holder.tv_num_videovr = (TextView) view.findViewById(R.id.tv_num_videovr);
            holder.tv_size_videovr = (TextView) view.findViewById(R.id.tv_size_videovr);
            holder.tv_title_videovr = (TextView) view.findViewById(R.id.tv_title_videovr);
            holder.downloadProgressView = (DownloadProgressBar) view.findViewById(R.id.dpv_videovr);
            holder.img_videovr = (ImageView) view.findViewById(R.id.img_videovr);
            holder.subtitle_tv_videovr = (TextView) view.findViewById(R.id.subtitle_tv_videovr);
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
        holder.tv_title_videovr.setText(title_video);
        holder.tv_size_videovr.setText(String.valueOf(size_video)+"MB");
        holder.tv_num_videovr.setText(String.valueOf(nummber)+"次");
        holder.subtitle_tv_videovr.setText(content_video);
        Picasso.with(mAppContext).load(url).into(holder.img_videovr);
        return view;

    }

    static class ViewHolder{
        TextView subtitle_tv_videovr;
        ImageView img_videovr;
        TextView tv_title_videovr;
        TextView tv_size_videovr;
        TextView tv_num_videovr;
        DownloadProgressBar downloadProgressView;

    }
}
