package cn.panorama.slook.ui.fragment;

import android.content.Context;
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
import android.widget.Button;
import android.widget.ProgressBar;

import java.lang.ref.WeakReference;

import cn.panorama.slook.ui.R;
import cn.panorama.slook.utils.stagger.STGVAdapter;
import cn.panorama.slook.utils.stagger.StaggeredGridView;

/**
 * Created by xingyaoma on 16-4-29.
 */
public class UnusualFragment extends Fragment  {

    //stagger
    StaggeredGridView stgv;
    STGVAdapter mAdapter;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        this.view = inflater.inflate(R.layout.fragment_u, container, false);

        stgv = (StaggeredGridView) view.findViewById(R.id.stgv);

        int margin = getResources().getDimensionPixelSize(R.dimen.stgv_margin);

        stgv.setItemMargin(margin);
        stgv.setPadding(margin, 0, margin, 0);

        stgv.setHeaderView(new Button(getContext()));
        View footerView;
        LayoutInflater inflater_stagger = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        footerView = inflater_stagger.inflate(R.layout.layout_loading_footer, null);
        stgv.setFooterView(footerView);

        mAdapter = new STGVAdapter(getActivity().getApplicationContext(), getActivity().getApplication());
        stgv.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        stgv.setOnLoadmoreListener(new StaggeredGridView.OnLoadmoreListener() {
            @Override
            public void onLoadmore() {
                new LoadMoreTask().execute();
            }
        });

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
