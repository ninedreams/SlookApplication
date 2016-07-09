package cn.panorama.slook.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.github.clans.fab.FloatingActionButton;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.panorama.slook.ui.R;

/**
 * Created by xingyaoma on 16-5-1.
 * 一级分类VR下的二级分类，第一个VRphoto
 */
public class PhotoVRFragment extends Fragment {

    public static final String TAG = PhotoVRFragment.class.getSimpleName();

    private static final int MOCK_LOAD_DATA_DELAYED_TIME = 2000;

    private static Handler sHandler = new Handler(Looper.getMainLooper());

    private WeakRunnable mRunnable = new WeakRunnable(this);

    private ProgressBar progressBar;

    private static final String KEY_TITLE = "title";

    private ListView mListView;
    private FloatingActionButton mFab;
    private int mPreviousVisibleItem;

    public PhotoVRFragment(){

    }

    public static PhotoVRFragment newInstance(String title) {
        PhotoVRFragment f = new PhotoVRFragment();

        Bundle args = new Bundle();

        args.putString(KEY_TITLE, title);
        f.setArguments(args);

        return (f);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_photovr, container, false);



        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar4);
        mListView = (ListView) view.findViewById(R.id.listvie_vrphoto);
        mFab = (FloatingActionButton) view.findViewById(R.id.fab_vrphoto);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();

        Locale[] availableLocales = Locale.getAvailableLocales();
        List<String> locales = new ArrayList<>();
        for (Locale locale : availableLocales) {
            locales.add(locale.getDisplayName());
        }

        mListView.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,
                android.R.id.text1, locales));

        mFab.hide(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mFab.show(true);
                mFab.setShowAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.show_from_bottom));
                mFab.setHideAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.hide_to_bottom));
            }
        }, 300);

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
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

        WeakReference<PhotoVRFragment> mFragmentReference;

        public WeakRunnable(PhotoVRFragment pFragment) {
            this.mFragmentReference = new WeakReference<PhotoVRFragment>(pFragment);
        }

        @Override
        public void run() {
            PhotoVRFragment pFragment = mFragmentReference.get();
            if (pFragment != null && !pFragment.isDetached()) {
                pFragment.showProgressBar(false);
                //pFragment.bindData();
            }
        }
    }

}
