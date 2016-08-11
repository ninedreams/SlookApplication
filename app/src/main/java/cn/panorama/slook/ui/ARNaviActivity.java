package cn.panorama.slook.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.TextureView;

import com.amap.api.navi.model.AMapNaviGuide;

import java.util.ArrayList;
import java.util.List;

import cn.panorama.slook.ar.ARData;
import cn.panorama.slook.ar.ARMarker;
import cn.panorama.slook.ar.ARNaviMarker;
import cn.panorama.slook.ar.AugmentedNaviView;
import cn.panorama.slook.ar.RadarView;
import cn.panorama.slook.control.CameraController;

import static cn.panorama.slook.Constants.IS_DEBUG;

/**
 * AR导航层<br/>
 * AR控件处理
 * 
 * @author xingyaoma
 * @date 2016/8/5
 */
public class ARNaviActivity extends ARCoordActivity {
	private static final String TAG = ARNaviActivity.class.getSimpleName();

	// ----------------界面相关-------------
	/* 摄像层浏览控件 . */
	protected TextureView mCameraTextureView;

	/* 增强导航控件 . */
	protected AugmentedNaviView mNaviView;

	/* 雷达图 . */
	protected RadarView mRadarView;

	// ----------------控制相关-------------
	/* 摄像层控制 . */
	private CameraController mCameraController;

	/* 屏幕控制 . */
	private WakeLock mWakeLock;

	// ------------------------ 初始化视图 ------------------------
	/**
	 * 初始化视图组件
	 */
	private void initView() {
		// 实景层
		mCameraTextureView = (TextureView) findViewById(R.id.id_textureView_camera);
		// 增强层
		mNaviView = (AugmentedNaviView) findViewById(R.id.id_augmentedView_ar_navi);
		mNaviView.setScreenWidth(getScreenWidth());
		mNaviView.setScreenHeight(getScreenHeight());

		// 雷达图
		mRadarView = (RadarView) findViewById(R.id.id_radarRouteView_radar);
		mRadarView
				.setLocation(getScreenWidth() - RadarView.RADIUS * 2 - 10, 30);
		mRadarView.setOnNaviMode(true);
	}

	/**
	 * 更新视图
	 */
	protected void updateView() {
		mRadarView.postInvalidate();
		mNaviView.postInvalidate();
		mCameraTextureView.postInvalidate();
	}

	/**
	 * 更新NaviMarker数据
	 * 
	 * @param guides
	 */
	protected void updateARNaviMarkers(List<AMapNaviGuide> guides) {
		List<ARMarker> markers = new ArrayList<ARMarker>();
		for (AMapNaviGuide guide : guides) {
			ARMarker m = new ARNaviMarker(guide);
			markers.add(m);
		}
		ARData.getInstance().addNaviMarkers(markers);
	}

	// ------------------------ 生命周期 ------------------------
	//
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 自定义标题栏

		setContentView(R.layout.activity_ar_navi);


		initView();

		// 摄像头控制
		mCameraController = new CameraController(this, mCameraTextureView);
		mCameraController.initPreview(getScreenWidth(), getScreenHeight(),
				getRotation());
		mCameraController
				.setCameraTextureListener(mCameraController.new CameraTextureListener());

		// 屏幕控制
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK,
				"DimScreen");

		if (IS_DEBUG) {
			Log.d(TAG, " --onCreated()--");
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (IS_DEBUG) {
			Log.d(TAG, "--OnStared()--");
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		mWakeLock.acquire();

		if (IS_DEBUG) {
			Log.d(TAG, "--OnResumed()--");
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		mCameraController.onPause();
		mWakeLock.release();

		if (IS_DEBUG) {
			Log.d(TAG, "--OnPaused()--");
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (IS_DEBUG) {
			Log.d(TAG, "--OnDestroyed()--");
		}
	}

	// ------------------------ 传感器数据处理------------------------
	/**
	 * 传感器数据处理
	 */
	@Override
	protected synchronized void onSensorAccess() {
		super.onSensorAccess();
		updateView();
	}

}
