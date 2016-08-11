package cn.panorama.slook.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import cn.panorama.slook.view.WeatherFragment;

/*
** Created by xingyaoma on 16-8-5.
 */

public class WeatherPagerAdapter extends FragmentPagerAdapter {
	private List<WeatherFragment> fragments;

	public WeatherPagerAdapter(FragmentManager fm,
			List<WeatherFragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

}
