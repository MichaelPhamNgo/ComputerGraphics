/**
 * Tranformation class
 * @author pham19@uw.edu
 * Homework 2: Draw graphs in 3D
 * Due: April 24, 2020
 */
public class Transformation {
	/**
	 * @return a identity matrix
	 */
	public Matrix LoadIdentityMatrix() {
		double[][] matrix = new double[4][4];
		matrix[0][0] = 1;
		matrix[1][1] = 1;
		matrix[2][2] = 1;
		matrix[3][3] = 1;
		return new Matrix(matrix);
	}
	
	/**
	 * @param x
	 * @param y
	 * @param z
	 * @return a translated matrix
	 */
	public Matrix Translation(double x, double y, double z) {
		double[][] matrix = new double[4][4];
		matrix[0][0] = 1;
		matrix[1][1] = 1;
		matrix[2][2] = 1;
		matrix[3][3] = 1;		
		matrix[0][3] = x;
		matrix[1][3] = y;
		matrix[2][3] = z;
		return new Matrix(matrix); 
	}
	
	/**
	 * @param theta rotate in z axis 
	 * @return a rotated matrix 
	 */
	public Matrix RotationZ(double theta) {
		double[][] matrix = new double[4][4];
		matrix[0][0] = Math.cos(Math.toRadians(theta));
		matrix[0][1] = -Math.sin(Math.toRadians(theta));
		matrix[1][0] = Math.sin(Math.toRadians(theta));
		matrix[1][1] = Math.cos(Math.toRadians(theta));
		matrix[2][2] = 1;
		matrix[3][3] = 1;
		return new Matrix(matrix);
	}
	
	/**
	 * @param theta rotate in z axis
	 * @return a rotated matrix
	 */
	public Matrix RotationX(double theta) {
		double[][] matrix = new double[4][4];
		matrix[0][0] = 1;
		matrix[1][1] = Math.cos(Math.toRadians(theta));
		matrix[1][2] = -Math.sin(Math.toRadians(theta));
		matrix[2][1] = Math.sin(Math.toRadians(theta));
		matrix[2][2] = Math.cos(Math.toRadians(theta));
		matrix[3][3] = 1;
		return new Matrix(matrix); 
	}
	
	/**
	 * @param theta rotate in y axis
	 * @return a rotated matrix
	 */
	public Matrix RotationY(double theta) {
		double[][] matrix = new double[4][4];
		matrix[0][0] = Math.cos(Math.toRadians(theta));
		matrix[0][2] = Math.sin(Math.toRadians(theta));
		matrix[1][1] = 1;
		matrix[2][0] = -Math.sin(Math.toRadians(theta));
		matrix[2][2] = Math.cos(Math.toRadians(theta));
		matrix[3][3] = 1;
		return new Matrix(matrix); 
	}
	
	/**
	 * @param x
	 * @param y
	 * @param z
	 * return a scaled matrix
	 */
	public Matrix Scaling(double x, double y, double z) {
		double[][] matrix = new double[4][4];
		matrix[0][0] = x;
		matrix[1][1] = y;
		matrix[2][2] = z;		
		matrix[3][3] = 1;
		return new Matrix(matrix); 
	}
	
	/**
	 * 
	 * @param left
	 * @param right
	 * @param bottom
	 * @param top
	 * @param near
	 * @param far
	 * @return
	 */
	public Matrix OrthographicProjection(double left, double right, double bottom, double top, double near, double far) {
		double[][] matrix = new double[4][4];
		matrix[0][0] = 2/(right - left);
		matrix[1][1] = 2/(top - bottom);
		matrix[2][2] = -2/(far - near);
		matrix[0][3] = -(right + left)/(right - left);
		matrix[1][3] = -(top + bottom)/(top - bottom);
		matrix[2][3] = -(far + near)/(far - near);
		matrix[3][3] = 1;
		
		return new Matrix(matrix);
	}
	
	public Matrix FrustumProjection(double left, double right, double bottom, double top, double near, double far) 
	{ 
		double[][] matrix = new double[4][4];
		
		matrix[0][0] = 2 * near / (right - left); 
		matrix[0][1] = 0; 
		matrix[0][2] = 0; 
		matrix[0][3] = 0; 
	 
		matrix[1][0] = 0; 
		matrix[1][1] = 2 * near / (top - bottom); 
		matrix[1][2] = 0; 
		matrix[1][3] = 0; 
	 
		matrix[2][0] = (right + left) / (right - left); 
		matrix[2][1] = (top + bottom) / (top - bottom); 
		matrix[2][2] = -(far + near) / (far - near); 
		matrix[2][3] = -1; 
	 
		matrix[3][0] = 0; 
		matrix[3][1] = 0; 
		matrix[3][2] = -2 * far * near / (far - near); 
		matrix[3][3] = 0; 
		return new Matrix(matrix);
	} 
	
	/**
	 * 
	 * @param eye
	 * @param target
	 * @param up
	 * @return
	 */
	public Matrix LootAt(Vector eye, Vector target, Vector up) {
		
		Vector zaxis = new Vector();
		zaxis = normalize(eye.substract(target));
		
		Vector xaxis = new Vector();
		xaxis = normalize(crossProduct(up, zaxis));
		
		Vector yaxis = new Vector();
		yaxis = crossProduct(zaxis, xaxis);
				
		double[][] matrix = new double[4][4];
		
		matrix[0][0] = xaxis.getX();	
		matrix[1][0] = xaxis.getY();
		matrix[2][0] = xaxis.getZ();
		matrix[3][0] = -dotProduct(xaxis, eye);
				
		matrix[0][1] = yaxis.getX();
		matrix[1][1] = yaxis.getY();
		matrix[2][1] = yaxis.getZ();
		matrix[3][1] = -dotProduct(yaxis, eye);

		matrix[0][2] = zaxis.getX();
		matrix[1][2] = zaxis.getY();
		matrix[2][2] = zaxis.getZ();
		matrix[3][2] = -dotProduct(zaxis, eye);
		
		matrix[0][3] = 0;
		matrix[1][3] = 0;
		matrix[2][3] = 0;
		matrix[3][3] = 1;
		
		
		return new Matrix(matrix);		
	}
	
	/**
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	private Vector crossProduct(Vector v1, Vector v2) {
		Vector newVector = new Vector();
		newVector.setX(v1.getY() * v2.getZ() - v2.getY() * v1.getZ());
		newVector.setX(v1.getZ() * v2.getX() - v1.getX() * v2.getZ());
		newVector.setX(v1.getX() * v2.getY() - v2.getX() * v1.getY());
		return newVector;
	}
	
	/**
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	private double dotProduct(Vector v1, Vector v2) {
		return v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.getZ() * v2.getZ();
	}
	
	/**
	 * 
	 * @param v
	 * @return
	 */
	private Vector normalize(Vector v) {
		Vector newVector = new Vector();
		newVector.setX(v.getX() / lengthVector(v));
		newVector.setX(v.getY() / lengthVector(v));
		newVector.setX(v.getZ() / lengthVector(v));
		return newVector;
	}
	
	/**
	 * 
	 * @param vector
	 * @return
	 */
	private double lengthVector(Vector v) {
		return Math.sqrt(v.getX() * v.getX() + v.getY() * v.getY() + v.getZ() * v.getZ());
	}
}
