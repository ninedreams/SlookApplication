package cn.panorama.slook.adapter;

import android.support.v4.app.Fragment;

import com.aspsine.fragmentnavigator.FragmentNavigatorAdapter;

import cn.panorama.slook.ui.fragment.PhotoVRFragment;
import cn.panorama.slook.ui.fragment.UnusualVRFragment;
import cn.panorama.slook.ui.fragment.VideoVRFragment;

/**
 * Created by xingyaoma on 16/4/3.
 */
public class VRChildFragmentAdapter implements FragmentNavigatorAdapter {

    public static final String[] TABS = {"VRPhoto", "VRVideo", "VRUnusual"};

    @Override
    public Fragment onCreateFragment(int position) {

        if(position == 0)
            return PhotoVRFragment.newInstance(TABS[position]);
        if(position == 1)
            return VideoVRFragment.newInstance(TABS[position]);
        if(position == 2)
            return UnusualVRFragment.newInstance(TABS[position]);

        return null;
    }

    @Override
    public String getTag(int position) {

        if(position == 0)
            return PhotoVRFragment.TAG + TABS[position];
        if(position == 1)
            return VideoVRFragment.TAG + TABS[position];
        if(position == 2)
            return UnusualVRFragment.TAG + TABS[position];

        return null;
    }

    @Override
    public int getCount() {
        return TABS.length;
    }
}
