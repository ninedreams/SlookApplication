package cn.panorama.slook;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import cn.panorama.slook.control.AOfflineMapManager;
import cn.panorama.slook.utils.ToastUtil;


/**
 * 网络连接状态监听
 * 
 * @author xingyaoma
 * @date 2016/8/5
 */
public class NetConnectionReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager mConnectivityMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo mobNetInfo = mConnectivityMgr
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifiNetInfo = mConnectivityMgr
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (!wifiNetInfo.isConnected()) {
			AOfflineMapManager.getInstance(context).stop();
			AppManager.getInstance().setWifiConnectedState(false);
		} else {
			AppManager.getInstance().setWifiConnectedState(true);
		}

		if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
			ToastUtil.showShort("当前网络不可用,请检查网络连接");
			AppManager.getInstance().setNetConnectedState(false);
		} else {
			AppManager.getInstance().setNetConnectedState(true);
		}

	}

}
