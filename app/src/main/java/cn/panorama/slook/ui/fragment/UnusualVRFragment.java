package cn.panorama.slook.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.panorama.slook.ui.R;

/**
 * Created by xingyaoma on 16-5-1.
 * 二级分类特色VR
 */
public class UnusualVRFragment extends Fragment {

    public static final String TAG = UnusualVRFragment.class.getSimpleName();

    private static final String KEY_TITLE = "title";

    public UnusualVRFragment(){

    }

    public static UnusualVRFragment newInstance(String title) {
        UnusualVRFragment f = new UnusualVRFragment();

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
        return inflater.inflate(R.layout.fragment_unusualvr, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }




}
