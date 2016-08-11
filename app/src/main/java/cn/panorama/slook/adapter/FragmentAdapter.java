package cn.panorama.slook.adapter;


import android.support.v4.app.Fragment;

import com.aspsine.fragmentnavigator.FragmentNavigatorAdapter;

import cn.panorama.slook.ui.fragment.ARFragment;
import cn.panorama.slook.ui.fragment.CommunityFragment;
import cn.panorama.slook.ui.fragment.PlaceFragment;
import cn.panorama.slook.ui.fragment.VRFragment;

/**
 * Created by xingyaoma on 16/3/31.
 */
public class FragmentAdapter implements FragmentNavigatorAdapter {

    private static final String TABS[] = {"特色", "社区", "VR", "AR"};

    @Override
    public Fragment onCreateFragment(int position) {
        if (position == 0)
            return PlaceFragment.newInstance(position);

        if (position == 1)
            return CommunityFragment.newInstance(TABS[position]);

        if (position == 2)
            return VRFragment.newInstance(position);

        if (position == 3)
            return ARFragment.newInstance(TABS[position]);

        return null;

    }

    @Override
    public String getTag(int position) {

        if (position == 0)
            return PlaceFragment.TAG;

        if (position == 1)
            return CommunityFragment.TAG + TABS[position];

        if (position == 2)
            return VRFragment.TAG;

        if (position == 3)
            return ARFragment.TAG + TABS[position];

        return null;

    }

    @Override
    public int getCount() {
        return TABS.length;
    }
}
