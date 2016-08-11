package cn.panorama.slook.model;

/**
 * LabelModel:搜索标签适配模型 LabelApdater:标签适配器
 * Created by xingyaoma on 16-8-5.
 */
public class LabelModel {

	/* 标签图 . */
	private int imgId;

	/* 标签名 . */
	private String name;

	public LabelModel(int imgId, String name) {
		this.imgId = imgId;
		this.name = name;
	}

	public int getImgId() {
		return imgId;
	}

	public void setImgId(int imgId) {
		this.imgId = imgId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "ImgId : " + imgId + "    /  name :" + name;
	}

}
