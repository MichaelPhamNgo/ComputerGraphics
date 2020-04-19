public class Point2D implements Comparable<Point2D> {
	private double x;
	private double y;    	

	/**
	 * Constructor of Vertex class
	 * @param x coordinate x
	 * @param y coordinate y
	 */
	public Point2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Point2D() {
		this(0,0);
	}	

	/**
	 * @return x
	 */
	public double getX() {
		return x;
	}

	/**
	 * @return y
	 */
	public double getY() {
		return y;
	}

	@Override
	public int compareTo(Point2D o) {
		return (int)(this.x - o.x);
	}

	@Override
	public String toString() {
		return "Vertex [x=" + x + ", y=" + y + "]";
	}
}