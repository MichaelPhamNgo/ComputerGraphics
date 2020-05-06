/**
 * This class represents a 4D vector or homogenous point. 
 * 
 *
 */
public class Vector {

	double x, y, z, w; //values for the vector
	
	/**
	 * Creates a new vector with the given values
	 * 
	 * @param x x value of vector
	 * @param y y value of vector
	 * @param z z value of vector
	 * @param w w value of vector
	 */
	public Vector(double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	/**
	 * No arg constructor that makes a vector with 0s for each value
	 */
	public Vector() {
		this(0,0,0,0);
	}
	

	/**
	 * Computes the dot product of two vectors. The
	 * dot product is commutative.
	 *
	 * @param other the vector to be multiplied
	 * @return the value of the dot product
	 */
	public double dotProduct(Vector other) {
		return x*other.x + y*other.y+ z*other.z + w*other.w;
	}
	
	/**
	 * Computes subtract two vectors 
	 * @param v
	 * @return
	 */
	public Vector subtract(Vector v) {
		Vector newVector = new Vector();
		newVector.x = this.x - v.x;
		newVector.y = this.y - v.y;
		newVector.z = this.z - v.z;
		return newVector;
	}
	
	/**
	 * Computes cross product two vectors
	 * @param v1
	 * @param v2
	 * @return
	 */
	public Vector crossProduct(Vector v) {
		Vector newVector = new Vector();
		newVector.x = this.y * v.z - v.y * this.z;
		newVector.y = this.z * v.x - this.x * v.z;
		newVector.z = this.x * v.y - this.x * v.y;
		return newVector;
	}
	
	/**
	 * 
	 * @param v
	 * @return
	 */
	public double angleTwoVector(Vector v) {
		return this.dotProduct(v) / 
				(Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z) 
					* (Math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z)));
	}
	
	/**
	 * 
	 * @param v2
	 * @param v3
	 * @return
	 */
	public Vector normalTriangle(Vector v2, Vector v3) {
		return (v2.subtract(this).crossProduct(v3).subtract(this));
	}
	
	/**
	 * Computes normalize a vector
	 * @param v
	 * @return
	 */
	public Vector normalize() {
		Vector newVector = new Vector();
		newVector.x = this.x / lengthVector(this);
		newVector.y = this.y / lengthVector(this);
		newVector.z = this.z / lengthVector(this);
		return newVector;
	}
	
	/**
	 * 
	 * @param vector
	 * @return
	 */
	private double lengthVector(Vector v) {
		return Math.sqrt(v.x * v.x + v.y* v.y + v.z * v.z);
	}
	
	
	public String toString() {
		return "[" + x + " " + y + " " + z + " " + w + "]";
	}
}
