package cn.panorama.slook.data;

import java.util.List;

import cn.panorama.slook.model.GeoPoint;


/**
 * GeoPoint数据访问接口模型
 * 
 * Created by xingyaoma on 16-8-5.
 */
public interface IGeoPointDao {

	/**
	 * 根据Id获得GeoPoint
	 * 
	 * @param pointId
	 * @return
	 */
	GeoPoint fetchGeoPointById(int pointId);

	/**
	 * 获得GeoPoint列表
	 * 
	 * @return
	 */
	List<GeoPoint> fetchAllGeoPoints();

	/**
	 * 添加GeoPoint
	 * 
	 * @param point
	 * @return
	 */
	boolean addGeoPoint(GeoPoint point);

	/**
	 * 添加GeoPoint列表
	 * 
	 * @param points
	 * @return
	 */
	boolean addGeoPoints(List<GeoPoint> points);

	/**
	 * 删除所有GeoPoint
	 * 
	 * @return
	 */
	boolean deleteAllGeoPoints();

	/**
	 * 删除GeoPoint
	 * 
	 * @param pointId
	 * @return
	 */
	boolean deleteGeoPoint(int pointId);

	/**
	 * 修改GeoPoint名
	 * 
	 * @param pointId
	 * @return
	 */
	boolean updateGeoPointName(int pointId, String pointName);

	/**
	 * 修改GeoPoint URL
	 * 
	 * @param pointId
	 * @return
	 */
	boolean updateGeoPointURL(int pointId, String url);
}
