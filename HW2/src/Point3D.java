public class Point3D {
	private double x;
	private double y;
	private double z;
	private double w;
	
	public Point3D(double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Matrix convertFromPoint3D() {
		double[][] matrix = new double[4][1];
		matrix[0][0] = this.x;
		matrix[1][0] = this.y;
		matrix[2][0] = this.z;
		matrix[3][0] = this.w;
		return new Matrix(matrix);
	}
	
	public Point3D() {
		this(0, 0, 0, 1);			
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
		return "Point [x=" + x + ", y=" + y + ", z=" + z + ", w=" + w + "]";
	}
}
