package cn.panorama.slook.model;

import com.amap.api.services.core.PoiItem;

import java.util.List;

/**
 * PoiSearchData
 * 
 * Created by xingyaoma on 16-8-5.
 */
public class PoiSearchData {

	private int mRadius;

	private List<PoiItem> mPois;

	public void setRadius(int radius) {
		mRadius = radius;
	}

	public void setPois(List<PoiItem> pois) {
		mPois = pois;
	}

	public int getRadius() {
		return mRadius;
	}

	public List<PoiItem> getPois() {
		return mPois;
	}

	public void clearPois() {
		if (null != mPois) {
			mPois.clear();
		}
		mPois = null;
	}

}
