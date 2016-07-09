package cn.panorama.slook.ui.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.panorama.slook.ui.R;

/**
 * Created by xingyaoma on 16-5-30.
 * 以及分类里面的社区交友
 */
public class CommunityFragment extends Fragment {
    public static final String TAG = CommunityFragment.class.getSimpleName();

    private static final int MOCK_LOAD_DATA_DELAYED_TIME = 2000;

    private static Handler sHandler = new Handler(Looper.getMainLooper());

    private WeakRunnable mRunnable = new WeakRunnable(this);

    private ProgressBar progressBar;

    private View view;

    public static final String EXTRA_TEXT = "extra_text";

    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;
    private FloatingActionMenu menuBlue;
    private List<FloatingActionMenu> menus = new ArrayList<>();
    private Handler mUiHandler = new Handler();

    public CommunityFragment() {
        // Required empty public constructor
    }

    public static CommunityFragment newInstance(){
        return new CommunityFragment();
    }

    public static CommunityFragment newInstance(String text) {
        CommunityFragment f = new CommunityFragment();

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
        this.view=inflater.inflate(R.layout.fragment_com, container, false);



        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar7);

        menuBlue = (FloatingActionMenu) view.findViewById(R.id.menu_blue);
        fab1 = (FloatingActionButton) view.findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) view.findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) view.findViewById(R.id.fab3);

        final FloatingActionButton programFab1 = new FloatingActionButton(getActivity());
        programFab1.setButtonSize(FloatingActionButton.SIZE_MINI);
        programFab1.setLabelText("slook");
        programFab1.setImageResource(R.mipmap.ic_edit);
        menuBlue.addMenuButton(programFab1);
        programFab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                programFab1.setLabelColors(ContextCompat.getColor(getActivity(), R.color.theme_default_primary),
                        ContextCompat.getColor(getActivity(), R.color.about_libraries_accent),
                        ContextCompat.getColor(getActivity(), R.color.theme_accent));
                programFab1.setLabelTextColor(ContextCompat.getColor(getActivity(), R.color.md_yellow_A200));
            }
        });

        fab1.setEnabled(false);
        menuBlue.setClosedOnTouchOutside(true);

        menuBlue.hideMenuButton(false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();
        menus.add(menuBlue);

        fab1.setOnClickListener(clickListener);
        fab2.setOnClickListener(clickListener);
        fab3.setOnClickListener(clickListener);

        int delay = 400;
        for (final FloatingActionMenu menu : menus) {
            mUiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    menu.showMenuButton(true);
                }
            }, delay);
            delay += 150;
        }


        menuBlue.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuBlue.isOpened()) {
                    Toast.makeText(getActivity(), menuBlue.getMenuButtonLabelText(), Toast.LENGTH_SHORT).show();
                }

                menuBlue.toggle(true);
            }
        });

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

        WeakReference<CommunityFragment> mFragmentReference;

        public WeakRunnable(CommunityFragment cFragment) {
            this.mFragmentReference = new WeakReference<CommunityFragment>(cFragment);
        }

        @Override
        public void run() {
            CommunityFragment cFragment = mFragmentReference.get();
            if (cFragment != null && !cFragment.isDetached()) {
                cFragment.showProgressBar(false);
                //tgFragment.bindData();
            }
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fab1:
                    break;
                case R.id.fab2:
                    fab2.setVisibility(View.GONE);
                    break;
                case R.id.fab3:
                    fab2.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };
}
