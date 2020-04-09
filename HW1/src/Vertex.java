/**
 * I want to arrange vertexes of a triangle with x coordinate first. 
 * @author pham19@uw.edu
 * Homework 1: Lines and Triangles
 * Due: April 10, 2020
 */
public class Vertex implements Comparable<Vertex> {
	private double x;
	private double y;    	
	
	/**
	 * Constructor of Vertex class
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