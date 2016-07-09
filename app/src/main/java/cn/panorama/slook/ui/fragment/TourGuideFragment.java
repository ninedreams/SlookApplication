package cn.panorama.slook.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.lang.ref.WeakReference;

import cn.panorama.slook.ui.R;

/**
 * Created by xingyaoma on 16-4-29.
 * 一级分类导游
 */
public class TourGuideFragment extends Fragment {

    public static final String TAG = TourGuideFragment.class.getSimpleName();

    private static final int MOCK_LOAD_DATA_DELAYED_TIME = 2000;

    private static Handler sHandler = new Handler(Looper.getMainLooper());

    private WeakRunnable mRunnable = new WeakRunnable(this);

    private ProgressBar progressBar;

    private View view;

    public static final String EXTRA_TEXT = "extra_text";

    public TourGuideFragment() {
        // Required empty public constructor
    }

    public static TourGuideFragment newInstance(){
        return new TourGuideFragment();
    }

    public static TourGuideFragment newInstance(String text) {
        TourGuideFragment f = new TourGuideFragment();

        Bundle args = new Bundle();

        args.putString(EXTRA_TEXT, text);
        f.setArguments(args);

        return (f);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mText = getArguments().getString(EXTRA_TEXT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        this.view=inflater.inflate(R.layout.fragment_tg, container, false);



        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar3);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();
        Log.i(TAG, "onActivityCreated");
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
        //tvText.setText(mText);
    }

    /**
     * mock load data
     */
    private void loadData() {
        showProgressBar(true);
        sHandler.postDelayed(mRunnable, MOCK_LOAD_DATA_DELAYED_TIME);
    }

    private static class WeakRunnable implements Runnable {

        WeakReference<TourGuideFragment> mFragmentReference;

        public WeakRunnable(TourGuideFragment tgFragment) {
            this.mFragmentReference = new WeakReference<TourGuideFragment>(tgFragment);
        }

        @Override
        public void run() {
            TourGuideFragment tgFragment = mFragmentReference.get();
            if (tgFragment != null && !tgFragment.isDetached()) {
                tgFragment.showProgressBar(false);
                //tgFragment.bindData();
            }
        }
    }

}
