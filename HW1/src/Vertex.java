/**
 * Compare vertexes in a collection.
 * @author pham19
 *
 */
public class Vertex implements Comparable<Vertex> {
	private double x;
	private double y;    	
	
	/**
	 * Initialize a vertex
	 * @param x coordinate x
	 * @param y coordinate y
	 */
	public Vertex(double x, double y) {
		this.x = x;
		this.y = y;
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
	public int compareTo(Vertex o) {
		return (int)(this.x - o.x);
	}

	@Override
	public String toString() {
		return "Vertex [x=" + x + ", y=" + y + "]";
	}
}