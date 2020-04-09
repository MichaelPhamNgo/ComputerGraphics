/**
 * Edges of a triangle with two vertexes. 
 * @author pham19@uw.edu
 * Homework 1: Lines and Triangles
 * Due: April 10, 2020
 */
public class Edge {
	private Vertex v1;
	private Vertex v2;
	private double slope;	
	/**
	 * Constructor of Edge class
	 * @param v1 vertex v1
	 * @param v2 vertex v2
	 * @param slope of vertex v1 and vertex v2
	 */
	public Edge(Vertex v1, Vertex v2) {
		this.v1 = v1;
		this.v2 = v2;
		this.slope = (v1.getY() - v2.getY())/(v1.getX() - v2.getX());
	}
	
	/**
	 * @return vertex v1
	 */
	public Vertex getV1() {
		return v1;
	}
	
	/**
	 * @return vertex v2
	 */
	public Vertex getV2() {
		return v2;
	}
	
	/**
	 * @return slope of vertexes v1 and v2
	 */
	public double getSlope() {
		return slope;
	}	
	
	@Override
	public String toString() {
		return "Edges [V1(" + v1.getX() + ", " + v1.getY() + "), V2(" 
							+ v2.getX() + ", " + v2.getY() + "), slope=" + slope + "]";
	}
}
