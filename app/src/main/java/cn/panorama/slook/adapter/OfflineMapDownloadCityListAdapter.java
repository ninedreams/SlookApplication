package cn.panorama.slook.adapter;

import android.content.Context;

import com.amap.api.maps.offlinemap.OfflineMapCity;

import java.util.ArrayList;
import java.util.List;

import cn.panorama.slook.control.AOfflineMapManager;

/**
 * 离线城市下载管理适配器
 * 
 * Created by xingyaoma on 16-8-5.
 */
public class OfflineMapDownloadCityListAdapter extends
		OfflineMapCityListAdapter {

	public OfflineMapDownloadCityListAdapter(Context context,
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

		List<OfflineMapCity> mCities = new ArrayList<OfflineMapCity>();
		mCities.addAll(mOfflineMapMgr.getDownloadingCityList());
		mCities.addAll(mOfflineMapMgr.getDownloadOfflineMapCityList());

		super.initCityList(mCities);

	}

}
