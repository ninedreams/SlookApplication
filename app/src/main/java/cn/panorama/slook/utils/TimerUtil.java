package cn.panorama.slook.utils;

import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

/**
 * TimerUtil:计时器工厂类
 * 
 * @author xingyaoma
 * @date 2016/8/5
 */
public class TimerUtil {

	/**
	 * Handler延迟发送消息
	 * 
	 * @param handler
	 *            处理器
	 * @param event
	 *            事件标记
	 * @param delay
	 *            时间
	 */
	public static void schedule(Handler handler, int event, long delay) {
		final Handler mHandler = handler;
		final int mEvent = event;
		Timer timer = new Timer(true);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				mHandler.sendEmptyMessage(mEvent);
			}
		}, delay);
	}

}
