package cn.panorama.slook.ar.model;

/**
 * 平面坐标点<br/>
 * 使用在雷达导航图
 * 
 * Created by xingyaoma on 16-8-5.
 */
public class Point {

	private float x;
	private float y;

	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	@Override
	public boolean equals(Object o) {
		if (null == o)
			return false;
		if (this == o)
			return true;
		if (o instanceof Point) {
			Point p = (Point) o;
			return this.x == p.x && this.y == p.y;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public String toString() {
		return "Point ( x=" + x + ",y=" + y + " )";
	}

}
