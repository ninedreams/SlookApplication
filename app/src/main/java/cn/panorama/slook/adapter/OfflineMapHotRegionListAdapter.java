package cn.panorama.slook.adapter;

import android.content.Context;

import com.amap.api.maps.offlinemap.OfflineMapCity;

import java.util.ArrayList;
import java.util.List;

import cn.panorama.slook.control.AOfflineMapManager;
import cn.panorama.slook.ui.R;
import cn.panorama.slook.AppManager;

/**
 * 离线地图热门城市列表适配器
 * 
 * Created by xingyaoma on 16-8-5.
 */
public class OfflineMapHotRegionListAdapter extends OfflineMapCityListAdapter {

	public OfflineMapHotRegionListAdapter(Context context,
			AOfflineMapManager offlineMapMgr) {
		super(context, offlineMapMgr);
		initCityList();
	}

	@Override
	public void notifyDataChange() {
		initCityList();
		notifyDataSetChanged();
	}

	/**
	 * 初始化城市列表
	 */
	private void initCityList() {
		String[] hotRegion = mContext.getResources().getStringArray(
				R.array.offlinmap_hot_region);
		if (null == hotRegion || 0 == hotRegion.length)
			return;

		List<OfflineMapCity> mHotRegions = new ArrayList<OfflineMapCity>();
		OfflineMapCity offlineCity = null;

		// 加入当前城市
		String locationCity = AppManager.getInstance().getLocationPoint()
				.getCity();
		offlineCity = mOfflineMapMgr.getItemByCityName(locationCity);
		if (null != offlineCity) {
			mHotRegions.add(offlineCity);
		}

		// 加入热门城市
		for (int i = 0; i < hotRegion.length; i++) {
			if (hotRegion[i].equals(locationCity))
				return;
			offlineCity = mOfflineMapMgr.getItemByCityName(hotRegion[i]);
			mHotRegions.add(offlineCity);
		}
		super.initCityList(mHotRegions);

	}

}
