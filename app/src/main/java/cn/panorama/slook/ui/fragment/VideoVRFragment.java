package cn.panorama.slook.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.clans.fab.FloatingActionButton;
import com.google.vr.sdk.widgets.video.VrVideoView;

import cn.panorama.slook.adapter.VideoVRAdapter;
import cn.panorama.slook.ui.R;
import cn.panorama.slook.ui.VideoVRActivity;

/**
 * Created by xingyaoma on 16-5-1.
 * 二级分类视频VR
 */
public class VideoVRFragment extends Fragment {

    public static final String TAG = VideoVRFragment.class.getSimpleName();

    private static final String KEY_TITLE = "title";

    private VideoVRAdapter mAdapter;
    private ListView listView;
    private Uri fileUri;   //传递资源坐在的路径

    private View view;
    private FloatingActionButton mFab;
    private int mPreviousVisibleItem;

    public VideoVRFragment(){

    }

    public static VideoVRFragment newInstance(String title) {

        VideoVRFragment f = new VideoVRFragment();

        Bundle args = new Bundle();

        args.putString(KEY_TITLE, title);
        f.setArguments(args);

        return (f);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mText = getArguments().getString(EXTRA_TEXT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view =  inflater.inflate(R.layout.fragment_videovr, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mFab = (FloatingActionButton) view.findViewById(R.id.fab_vrvideo);
        listView = (ListView) view.findViewById(R.id.listview_vrvideo);

        if(mAdapter == null) {
            mAdapter = new VideoVRAdapter(getActivity(), getActivity().getApplication(), R.id.tv_title_videovr);
        }
        listView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent =null;
                switch(position){
                    case 0:
                        fileUri = Uri.parse("http://dl.bgc.mojing.cn/quanjing/160805/1470381389_37_3840HD.mp4");
                        intent = new Intent(getActivity(), VideoVRActivity.class);
                        intent.setData(fileUri);
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.putExtra("inputType", VrVideoView.Options.TYPE_STEREO_OVER_UNDER);
                        intent.putExtra("inputFormat", VrVideoView.Options.FORMAT_DEFAULT);
                        break;
                    case 1:
                        fileUri = Uri.parse("http://dl.mojing.cn/xianchang/160620/1466393969_37_3840HD.mp4");
                        intent = new Intent(getActivity(), VideoVRActivity.class);
                        intent.setData(fileUri);
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.putExtra("inputType", VrVideoView.Options.TYPE_MONO);
                        intent.putExtra("inputFormat", VrVideoView.Options.FORMAT_DEFAULT);
                        break;
                    case 2:
                        fileUri = Uri.parse("http://www.ninedreams.cn/images/videos/congo.mp4");
                        intent = new Intent(getActivity(), VideoVRActivity.class);
                        intent.setData(fileUri);
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.putExtra("inputType", VrVideoView.Options.TYPE_MONO);
                        intent.putExtra("inputFormat", VrVideoView.Options.FORMAT_DEFAULT);
                        break;
                    case 3:
                        intent = new Intent(getActivity(), VideoVRActivity.class);
                        break;
                    case 4:
                        intent = new Intent(getActivity(), VideoVRActivity.class);
                        break;
                    case 5:
                        intent = new Intent(getActivity(), VideoVRActivity.class);
                        break;
                    case 6:
                        intent = new Intent(getActivity(), VideoVRActivity.class);
                        break;
                    case 7:
                        intent = new Intent(getActivity(), VideoVRActivity.class);
                        break;
                    case 8:
                        intent = new Intent(getActivity(), VideoVRActivity.class);
                        break;
                    case 9:
                        intent = new Intent(getActivity(), VideoVRActivity.class);
                        break;
                    case 10:
                        intent = new Intent(getActivity(), VideoVRActivity.class);
                        break;
                    default:break;
                }

                startActivity(intent);
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem > mPreviousVisibleItem) {
                    mFab.hide(true);
                } else if (firstVisibleItem < mPreviousVisibleItem) {
                    mFab.show(true);
                }
                mPreviousVisibleItem = firstVisibleItem;
            }
        });

        mFab.hide(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mFab.show(true);
                mFab.setShowAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.show_from_bottom));
                mFab.setHideAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.hide_to_bottom));
            }
        }, 300);

    }

}
