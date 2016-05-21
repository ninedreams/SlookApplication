package cn.panorama.slook.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.panorama.slook.ui.R;

/**
 * Created by xingyaoma on 16-5-1.
 */
public class VideoVRFragment extends Fragment {

    public static final String TAG = VideoVRFragment.class.getSimpleName();

    private static final int MOCK_LOAD_DATA_DELAYED_TIME = 2000;

    private static Handler sHandler = new Handler(Looper.getMainLooper());

    private WeakRunnable mRunnable = new WeakRunnable(this);

    private String mText;

    private ProgressBar progressBar;

    private static final int ITEM_COUNT = 100;
    private static final String KEY_TITLE = "title";

    private List<Object> mContentItems = new ArrayList<>();

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
        return inflater.inflate(R.layout.fragment_videovr, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar5);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();
    }

    @Override
    public void onDestroyView() {
        sHandler.removeCallbacks(mRunnable);
        progressBar = null;
        super.onDestroyView();
    }

    private void showProgressBar(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void bindData() {

    }

    /**
     * mock load data
     */
    private void loadData() {
        showProgressBar(true);
        sHandler.postDelayed(mRunnable, MOCK_LOAD_DATA_DELAYED_TIME);
    }

    private static class WeakRunnable implements Runnable {

        WeakReference<VideoVRFragment> mFragmentReference;

        public WeakRunnable(VideoVRFragment vFragment) {
            this.mFragmentReference = new WeakReference<VideoVRFragment>(vFragment);
        }

        @Override
        public void run() {
            VideoVRFragment vFragment = mFragmentReference.get();
            if (vFragment != null && !vFragment.isDetached()) {
                vFragment.showProgressBar(false);
                //vFragment.bindData();
            }
        }
    }
}
