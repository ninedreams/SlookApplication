package cn.panorama.slook.ui.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;

import java.lang.ref.WeakReference;

import cn.panorama.slook.adapter.GridViewAdapter;
import cn.panorama.slook.ui.R;

/**
 * Created by xingyaoma on 16-4-29.
 * 第一个分类特色里面的二级分类
 */
public class ClassifyFragment extends Fragment implements AbsListView.OnScrollListener, AbsListView.OnItemClickListener{

    //stagger
    private StaggeredGridView sGridView;
    private GridViewAdapter mAdapter;
    private boolean mHasRequestedMore;

    public static final String TAG = ClassifyFragment.class.getSimpleName();

    private static final int MOCK_LOAD_DATA_DELAYED_TIME = 2000;

    private static Handler sHandler = new Handler(Looper.getMainLooper());

    private WeakRunnable mRunnable = new WeakRunnable(this);

    private ProgressBar progressBar;

    private View view;


    public static final String EXTRA_TEXT = "extra_text";

    public ClassifyFragment() {
        // Required empty public constructor
    }

    public static ClassifyFragment newInstance() {
        return new ClassifyFragment();
    }

    public static ClassifyFragment newInstance(String text) {
        ClassifyFragment f = new ClassifyFragment();

        Bundle args = new Bundle();

        args.putString(EXTRA_TEXT, text);
        f.setArguments(args);

        return (f);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        this.view=inflater.inflate(R.layout.fragment_classify, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar2);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();
        sGridView = (StaggeredGridView) view.findViewById(R.id.image_gridview);

        if (mAdapter == null) {
            mAdapter = new GridViewAdapter(getActivity(),getActivity().getApplication() , R.id.text_gridview);
        }


        sGridView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        sGridView.setOnScrollListener(this);
        sGridView.setOnItemClickListener(this);

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

    }

    /**
     * mock load data
     */
    private void loadData() {
        showProgressBar(true);
        sHandler.postDelayed(mRunnable, MOCK_LOAD_DATA_DELAYED_TIME);
    }


    private static class WeakRunnable implements Runnable {

        WeakReference<ClassifyFragment> mFragmentReference;

        public WeakRunnable(ClassifyFragment cFragment) {
            this.mFragmentReference = new WeakReference<ClassifyFragment>(cFragment);
        }

        @Override
        public void run() {
            ClassifyFragment cFragment = mFragmentReference.get();
            if (cFragment != null && !cFragment.isDetached()) {
                cFragment.showProgressBar(false);
                cFragment.bindData();
            }
        }
    }

    @Override
    public void onScrollStateChanged(final AbsListView view, final int scrollState) {
        Log.d(TAG, "onScrollStateChanged:" + scrollState);
    }

    @Override
    public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {
        // our handling
        if (!mHasRequestedMore) {
            int lastInScreen = firstVisibleItem + visibleItemCount;
            if (lastInScreen >= totalItemCount) {
                Log.d(TAG, "onScroll lastInScreen - so load more");
                mHasRequestedMore = true;
                //onLoadMoreItems();
            }
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Toast.makeText(getContext(), "Item Clicked: " + position, Toast.LENGTH_SHORT).show();
    }


}
