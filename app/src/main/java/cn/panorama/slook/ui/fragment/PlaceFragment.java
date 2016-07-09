package cn.panorama.slook.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.fragmentnavigator.FragmentNavigator;

import cn.panorama.slook.adapter.PlaceChildFragmentAdapter;
import cn.panorama.slook.ui.R;
import cn.panorama.slook.utils.TabLayoutPlace;

/**
 * Created by xingyaoma on 16-5-30.
 * 第一级分类第一个
 */
public class PlaceFragment extends Fragment {
    public static final String TAG = PlaceFragment.class.getSimpleName();

    private FragmentNavigator mNavigator;

    private TabLayoutPlace tabLayout;

    private View view;

    private static final String KEY_TITLE = "title";

    public PlaceFragment() {
        // Required empty public constructor
    }

    public static PlaceFragment newInstance() {
        return new PlaceFragment();
    }

    public static PlaceFragment newInstance(String title) {
        PlaceFragment f = new PlaceFragment();

        Bundle args = new Bundle();

        args.putString(KEY_TITLE, title);
        f.setArguments(args);

        return (f);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNavigator = new FragmentNavigator(getChildFragmentManager(), new PlaceChildFragmentAdapter(), R.id.childContainer1);
        mNavigator.setDefaultPosition(0);
        mNavigator.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        this.view=inflater.inflate(R.layout.fragment_place, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = (TabLayoutPlace) view.findViewById(R.id.tabLayout1);
        tabLayout.setOnTabItemClickListener(new TabLayoutPlace.OnTabItemClickListener() {
            @Override
            public void onTabItemClick(int position, View view) {
                setCurrentTab(position);
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setCurrentTab(mNavigator.getCurrentPosition());
        Log.i(TAG, "onActivityCreated");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mNavigator.onSaveInstanceState(outState);
    }

    public void setCurrentTab(int position) {
        mNavigator.showFragment(position);
        tabLayout.select(position);
    }


    public static Fragment newInstance(int position) {
        Fragment fragment = new PlaceFragment();
        return fragment;
    }

}
