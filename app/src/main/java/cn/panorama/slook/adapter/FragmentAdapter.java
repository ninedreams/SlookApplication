package cn.panorama.slook.adapter;


import android.support.v4.app.Fragment;

import com.aspsine.fragmentnavigator.FragmentNavigatorAdapter;

import cn.panorama.slook.ui.fragment.ClassifyFragment;
import cn.panorama.slook.ui.fragment.TourGuideFragment;
import cn.panorama.slook.ui.fragment.UnusualFragment;
import cn.panorama.slook.ui.fragment.VRFragment;

/**
 * Created by aspsine on 16/3/31.
 */
public class FragmentAdapter implements FragmentNavigatorAdapter {

    private static final String TABS[] = {"特色", "分类", "VR", "导游"};

    @Override
    public Fragment onCreateFragment(int position) {
        if (position == 0)
            return UnusualFragment.newInstance(TABS[position]);

        if (position == 1)
            return ClassifyFragment.newInstance(TABS[position]);

        if (position == 2)
            return VRFragment.newInstance(position);

        if (position == 3)
            return TourGuideFragment.newInstance(TABS[position]);

        return null;

    }

    @Override
    public String getTag(int position) {

        if (position == 0)
            return UnusualFragment.TAG + TABS[position];

        if (position == 1)
            return ClassifyFragment.TAG + TABS[position];

        if (position == 2)
            return VRFragment.TAG;

        if (position == 3)
            return TourGuideFragment.TAG + TABS[position];

        return null;

    }

    @Override
    public int getCount() {
        return TABS.length;
    }
}
