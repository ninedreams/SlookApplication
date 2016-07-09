package cn.panorama.slook.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import cn.panorama.slook.ui.R;

/**
 * Created by xingyaoma on 16-5-1.
 * 二级分类视频VR
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

    private int mScrollOffset = 4;
    private int mMaxProgress = 100;
    private LinkedList<ProgressType> mProgressTypes;
    private Handler mUiHandler = new Handler();

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

        Locale[] availableLocales = Locale.getAvailableLocales();
        mProgressTypes = new LinkedList<>();
        for (ProgressType type : ProgressType.values()) {
            mProgressTypes.offer(type);
        }

        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_vrvideo);
        fab.setMax(mMaxProgress);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_vrvideo);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new LanguageAdapter(availableLocales));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressType type = mProgressTypes.poll();
                switch (type) {
                    case INDETERMINATE:
                        fab.setShowProgressBackground(true);
                        fab.setIndeterminate(true);
                        mProgressTypes.offer(ProgressType.INDETERMINATE);
                        break;
                    case PROGRESS_POSITIVE:
                        fab.setIndeterminate(false);
                        fab.setProgress(70, true);
                        mProgressTypes.offer(ProgressType.PROGRESS_POSITIVE);
                        break;
                    case PROGRESS_NEGATIVE:
                        fab.setProgress(30, true);
                        mProgressTypes.offer(ProgressType.PROGRESS_NEGATIVE);
                        break;
                    case HIDDEN:
                        fab.hideProgress();
                        mProgressTypes.offer(ProgressType.HIDDEN);
                        break;
                    case PROGRESS_NO_ANIMATION:
                        increaseProgress(fab, 0);
                        break;
                    case PROGRESS_NO_BACKGROUND:
                        fab.setShowProgressBackground(false);
                        fab.setIndeterminate(true);
                        mProgressTypes.offer(ProgressType.PROGRESS_NO_BACKGROUND);
                        break;
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (Math.abs(dy) > mScrollOffset) {
                    if (dy > 0) {
                        fab.hide(true);
                    } else {
                        fab.show(true);
                    }
                }
            }
        });

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

    private void increaseProgress(final FloatingActionButton fab, int i) {
        if (i <= mMaxProgress) {
            fab.setProgress(i, false);
            final int progress = ++i;
            mUiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    increaseProgress(fab, progress);
                }
            }, 30);
        } else {
            mUiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fab.hideProgress();
                }
            }, 200);
            mProgressTypes.offer(ProgressType.PROGRESS_NO_ANIMATION);
        }
    }


    private class LanguageAdapter extends RecyclerView.Adapter<ViewHolder> {

        private Locale[] mLocales;

        private LanguageAdapter(Locale[] mLocales) {
            this.mLocales = mLocales;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView tv = (TextView) LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);

            return new ViewHolder(tv);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.mTextView.setText(mLocales[position].getDisplayName());
        }

        @Override
        public int getItemCount() {
            return mLocales.length;
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;

        public ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
    }

    private enum ProgressType {
        INDETERMINATE, PROGRESS_POSITIVE, PROGRESS_NEGATIVE, HIDDEN, PROGRESS_NO_ANIMATION, PROGRESS_NO_BACKGROUND
    }

}
