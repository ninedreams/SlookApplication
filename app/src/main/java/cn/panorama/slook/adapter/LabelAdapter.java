package cn.panorama.slook.adapter;

import android.content.Context;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.panorama.slook.model.LabelModel;
import cn.panorama.slook.ui.R;
import cn.panorama.slook.Constants;

/**
 * LabelApdater:搜索标签适配器
 * 
 * Created by xingyaoma on 16-8-5.
 */
public class LabelAdapter extends SimpleAdapter {

	private static String[] from = { Constants.KEY_ITEM_LABEL_IMG,
			Constants.KEY_ITEM_LABLE_NAME };

	private static int[] to = { R.id.id_imageView_mgridView_item,
			R.id.id_textView_mgridView_item };

	private List<Map<String, Object>> data;

	public LabelAdapter(Context context, List<Map<String, Object>> data) {
		this(context, data, R.layout.item_mgridview, from, to);
		this.data = data;
	}

	private LabelAdapter(Context context, List<? extends Map<String, ?>> data,
			int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
	}

	/**
	 * 取出LabelModel数据集合重新封装
	 * 
	 * @param list
	 * @return data
	 */
	public static List<Map<String, Object>> unpack(List<LabelModel> list) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		for (LabelModel model : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(Constants.KEY_ITEM_LABEL_IMG, model.getImgId());
			map.put(Constants.KEY_ITEM_LABLE_NAME, model.getName());
			data.add(map);
		}
		return data;
	}

	/**
	 * 添加LabelModel
	 * 
	 * @param model
	 */
	public void addLabelModel(LabelModel model) {
		if (null == data)
			return;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Constants.KEY_ITEM_LABEL_IMG, model.getImgId());
		map.put(Constants.KEY_ITEM_LABLE_NAME, model.getName());
		data.add(map);
	}

	/**
	 * 添加LabelModel集合
	 * 
	 * @param list
	 */
	public void addLabelModelList(List<LabelModel> list) {
		for (LabelModel model : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(Constants.KEY_ITEM_LABEL_IMG, model.getImgId());
			map.put(Constants.KEY_ITEM_LABLE_NAME, model.getName());
			data.add(map);
		}
	}

	/**
	 * 删除LabelModel
	 * 
	 * @param index
	 */
	public void removeLabelModel(int index) {
		data.remove(index);
	}

}
