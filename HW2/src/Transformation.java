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
	 * translate a point with x, y, z
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
	 * 
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
}
