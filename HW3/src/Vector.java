public class Vector {
	private double x;
	private double y;
	private double z;	
	public Vector(double x, double y, double z) {		
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector() {
		this(0, 0, 0);
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
	
	/**
	 * 
	 * @param v
	 * @return
	 */
	public Vector substract(Vector v) {
		Vector newVector = new Vector();
		newVector.setX(this.x - v.x);
		newVector.setX(this.y - v.y);
		newVector.setX(this.z - v.z);
		return newVector;
	}

	@Override
	public String toString() {
		return "Vector [x=" + x + ", y=" + y + ", z=" + z + "]";
	}
}
