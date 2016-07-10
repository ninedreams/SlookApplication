package cn.panorama.slook.ui.fragment;

import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.TextView;

import com.etsy.android.grid.StaggeredGridView;

import java.lang.ref.WeakReference;

import cn.panorama.slook.adapter.StaggerAdapter;
import cn.panorama.slook.ui.PanoramaActivity;
import cn.panorama.slook.ui.R;

/**
 * Created by xingyaoma on 16-4-29.
 * 二级分类特色
 */
public class UnusualFragment extends Fragment implements AbsListView.OnScrollListener, AbsListView.OnItemClickListener {

    //stagger
    private StaggeredGridView sGridView;
    private StaggerAdapter mAdapter;
    private boolean mHasRequestedMore;

    public static final String TAG = UnusualFragment.class.getSimpleName();

    private static final int MOCK_LOAD_DATA_DELAYED_TIME = 2000;

    private static Handler sHandler = new Handler(Looper.getMainLooper());

    private WeakRunnable mRunnable = new WeakRunnable(this);

    private ProgressBar progressBar;

    private View view;

    public static final String EXTRA_TEXT = "extra_text";

    public static Fragment newInstance(){
        return new UnusualFragment();
    }

    public UnusualFragment() {
        // Required empty public constructor
    }

    public static UnusualFragment newInstance(String text) {
        UnusualFragment f = new UnusualFragment();

        Bundle args = new Bundle();

        args.putString(EXTRA_TEXT, text);
        f.setArguments(args);

        return (f);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        this.view = inflater.inflate(R.layout.fragment_u, container, false);

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar0);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();

        sGridView = (StaggeredGridView) view.findViewById(R.id.stagger_grid_view);

        if (savedInstanceState == null) {
            final LayoutInflater layoutInflater = getActivity().getLayoutInflater();

            View header = layoutInflater.inflate(R.layout.stagger_header_footer, null);
            View footer = layoutInflater.inflate(R.layout.stagger_header_footer, null);
            TextView txtHeaderTitle = (TextView) header.findViewById(R.id.txt_title);
            TextView txtFooterTitle = (TextView) footer.findViewById(R.id.txt_title);
            txtHeaderTitle.setText("THE HEADER!");
            txtFooterTitle.setText("THE FOOTER!");

            //sGridView.addHeaderView(header);
            //sGridView.addFooterView(footer);
        }

        if (mAdapter == null) {
            mAdapter = new StaggerAdapter(getActivity(), getActivity().getApplication(),R.id.tv_stagger);
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

        WeakReference<UnusualFragment> mFragmentReference;

        public WeakRunnable(UnusualFragment uFragment) {
            this.mFragmentReference = new WeakReference<UnusualFragment>(uFragment);
        }

        @Override
        public void run() {
            UnusualFragment uFragment = mFragmentReference.get();
            if (uFragment != null && !uFragment.isDetached()) {
                uFragment.showProgressBar(false);
                //uFragment.bindData();
            }
        }
    }

    @Override
    public void onScrollStateChanged(final AbsListView view, final int scrollState) {
        Log.d(TAG, "onScrollStateChanged:" + scrollState);
    }

    @Override
    public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {
        Log.d(TAG, "onScroll firstVisibleItem:" + firstVisibleItem +
                " visibleItemCount:" + visibleItemCount +
                " totalItemCount:" + totalItemCount);
        // our handling
        if (!mHasRequestedMore) {
            int lastInScreen = firstVisibleItem + visibleItemCount;
            if (lastInScreen >= totalItemCount) {
                Log.d(TAG, "onScroll lastInScreen - so load more");
                mHasRequestedMore = true;
               new LoadMoreTask().execute();
            }
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        switch(position){
            case 0:
                startActivity(new Intent(getActivity(), PanoramaActivity.class));
                break;
            case 1:
                startActivity(new Intent(getActivity(), PanoramaActivity.class));
                break;
            case 2:
                startActivity(new Intent(getActivity(), PanoramaActivity.class));
                break;
            case 3:
                startActivity(new Intent(getActivity(), PanoramaActivity.class));
                break;
            case 4:
                startActivity(new Intent(getActivity(), PanoramaActivity.class));
                break;
            case 5:
                startActivity(new Intent(getActivity(), PanoramaActivity.class));
                break;
            case 6:
                startActivity(new Intent(getActivity(), PanoramaActivity.class));
                break;
            case 7:
                startActivity(new Intent(getActivity(), PanoramaActivity.class));
                break;
            case 8:
                startActivity(new Intent(getActivity(), PanoramaActivity.class));
                break;
            case 9:
                startActivity(new Intent(getActivity(), PanoramaActivity.class));
                break;
            case 10:
                startActivity(new Intent(getActivity(), PanoramaActivity.class));
                break;
            case 11:
                startActivity(new Intent(getActivity(), PanoramaActivity.class));
                break;
            case 12:
                startActivity(new Intent(getActivity(), PanoramaActivity.class));
                break;
            case 13:
                startActivity(new Intent(getActivity(), PanoramaActivity.class));
                break;
            case 14:
                startActivity(new Intent(getActivity(), PanoramaActivity.class));
                break;
            case 15:
                startActivity(new Intent(getActivity(), PanoramaActivity.class));
                break;
            case 16:
                startActivity(new Intent(getActivity(), PanoramaActivity.class));
                break;
            case 17:
                startActivity(new Intent(getActivity(), PanoramaActivity.class));
                break;
            default:break;

        }
    }

    public class LoadMoreTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mAdapter.getMoreItem();
            mAdapter.notifyDataSetChanged();
            super.onPostExecute(result);
        }

    }
}
