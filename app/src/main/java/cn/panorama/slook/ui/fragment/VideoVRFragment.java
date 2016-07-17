package cn.panorama.slook.ui.fragment;

import android.content.Intent;
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

    private View view;
    private FloatingActionButton mFab;

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

        mFab = (FloatingActionButton) view.findViewById(R.id.fab_vrphoto);

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView = (ListView) view.findViewById(R.id.listview_vrvideo);
        if(mAdapter == null) {
            mAdapter = new VideoVRAdapter(getActivity(), getActivity().getApplication(), R.id.tv_title_videovr);
        }
        listView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                startActivity(new Intent(getActivity(), VideoVRActivity.class));
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }

}
