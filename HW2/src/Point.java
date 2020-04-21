public class Point implements Comparable<Point>{
	private double x;
	private double y;
	private double z;
	private double w;
	
	public Point() {}
	
	public Point(double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public double getW() {
		return w;
	}

	public void setW(double w) {
		this.w = w;
	}

	@Override
	public String toString() {
		return "Point [x=" + this.x + ", y=" + this.y + ", z=" 
						+ this.z + ", w=" + this.w + "]";
	}

	@Override
	public int compareTo(Point o) {
		return (int)(this.x - o.x);
	}
}
