package cn.panorama.slook.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;

import com.amap.api.location.AMapLocation;

import cn.panorama.slook.control.ALocationController;
import cn.panorama.slook.control.ALocationController.ALocationListener;
import cn.panorama.slook.control.XFVoiceController;
import cn.panorama.slook.control.XFVoiceController.OnSpeechEndedListener;
import cn.panorama.slook.model.GeoPoint;
import cn.panorama.slook.model.PoiSearchData;
import cn.panorama.slook.AppManager;

/**
 * OriginActivity
 * 
 * 实现定位及语音识别监听<br />
 * 获得屏幕信息
 * 
 * @author xingyaoma
 * @date 2016/8/5
 */
public abstract class OriginActivity extends FragmentActivity implements
		ALocationListener, OnSpeechEndedListener {

	/* 定位控制器. */
	protected ALocationController mLocationController;

	/* 当前位置信息. */
	protected AMapLocation mALocation;

	/* 当前位置信息点. */
	protected GeoPoint mLocationPoint;

	/* 当前目的地信息 . */
	protected GeoPoint mDestinationPoint;

	/* 记录Poi搜索数据. */
	protected PoiSearchData mPoiSearchData;

	/* 语音组件. */
	protected XFVoiceController mVoiceController;

	// ------------------------ 生命周期 ------------------------
	//
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initConfig();

		// 获取全局数据信息
		mLocationPoint = AppManager.getInstance().getLocationPoint();
		mDestinationPoint = AppManager.getInstance().getDestinationPoint();
		mPoiSearchData = AppManager.getInstance().getPoiSearchData();

		// 定位控制
		mLocationController = new ALocationController(getApplicationContext());
		mLocationController.setALocationListener(this);

		// 语音控制
		mVoiceController = new XFVoiceController(getApplicationContext());
		mVoiceController.setOnSpeechEndedListener(this);
		//
		AppManager.getInstance().addActivity(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		// 启动定位
		mLocationController.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
		// 停止定位
		mLocationController.onStop();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 销毁定位客服端
		mLocationController.onDestroy();
		// 销毁语音组件
		mVoiceController.onDestroy();

	}

	// ---------------------接口回调-----------------------------
	/**
	 * 语音识别结果回调
	 */
	@Override
	public void onResult(String result) {
		if (null == result || "".equals(result))
			return;
	}

	/**
	 * 定位信息回调
	 */
	@Override
	public void onLocationSucceeded(AMapLocation amapLocation) {
		mALocation = amapLocation;
		GeoPoint.AMapLocationToGeoPoint(amapLocation, mLocationPoint);
	}

	// --------------------------------------------------
	/**
	 * 初始化配置
	 */
	protected void initConfig() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		// 配置屏幕宽高
		int screenWidth = metrics.widthPixels;
		int screenHeight = metrics.heightPixels;
		setScreenWidth(screenWidth);
		setScreenHeight(screenHeight);
	}

	/**
	 * 获取屏幕宽度
	 * 
	 * @return 屏幕宽度
	 */
	public int getScreenWidth() {
		return AppManager.getInstance().getScreenWidth();
	}

	/**
	 * 设置屏幕宽度
	 * 
	 * @param screenWidth
	 *            待设置的屏幕宽度
	 */
	public void setScreenWidth(int screenWidth) {
		AppManager.getInstance().setScreenWidth(screenWidth);
	}

	/**
	 * 获取屏幕高度
	 * 
	 * @return 屏幕高度
	 */
	public int getScreenHeight() {
		return AppManager.getInstance().getScreenHeight();
	}

	/**
	 * 设置屏幕高度
	 * 
	 * @param screenHeight
	 *            待设置的屏幕高度
	 */
	public void setScreenHeight(int screenHeight) {
		AppManager.getInstance().setScreenHeight(screenHeight);
	}

	/**
	 * 交换屏幕宽高
	 */
	public void exchangeScreenWidth2Height() {
		int screenHeight = AppManager.getInstance().getScreenHeight();
		int screenWidth = AppManager.getInstance().getScreenWidth();
		screenHeight += screenWidth;
		screenWidth = screenHeight - screenWidth;
		screenHeight -= screenWidth;
		AppManager.getInstance().setScreenHeight(screenHeight);
		AppManager.getInstance().setScreenWidth(screenWidth);
	}

	/**
	 * 获得屏幕方向
	 * 
	 * @return 屏幕方向
	 */
	public int getRotation() {
		return getWindowManager().getDefaultDisplay().getRotation();
	}

	/**
	 * 启动Activity
	 */
	public void startActivity(Class<? extends Activity> activity) {
		Intent intent = new Intent(getApplicationContext(), activity);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		AppManager.getInstance().delActivity(this);
		// this.finish();
	}

}
