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
	
	// Use these code for next homework
//	public Matrix paralleProjection() {
//		double[][] matrix = new double[4][4];
//		matrix[0][0] = 1;
//		matrix[1][1] = 1;
//		matrix[2][2] = 0;		
//		matrix[3][3] = 1;
//		return new Matrix(matrix);
//	}
//	
//	public Matrix orthogonalProjection(double left, double right, double bottom, 
//			double top, double near, double far) {
//		double[][] matrix = new double[4][4];
//		
//		matrix[0][0] = 2 / (right - left);
//		matrix[1][1] = 2 / (top - bottom);
//		matrix[2][2] = -2 / (far - near);
//		matrix[3][3] = 1;
//		matrix[0][3] = ((-1) * (right + left)) / (right - left);
//		matrix[1][3] = ((-1) * (top + bottom)) / (top - bottom);
//		matrix[2][3] = ((-1) * (far + near)) / (near - far);
//		return new Matrix(matrix);
//	}
//	
//	public Matrix frustumProjection(double left, double right, double bottom, 
//										double top, double near, double far) {
//		double[][] matrix = new double[4][4];
//		
//		matrix[0][0] = (2 * near) / (right - left);
//		matrix[1][1] = (2 * near) / (top - bottom);
//		matrix[2][2] = -(far + near) / (far - near);
//		matrix[3][2] = -1;
//		matrix[0][3] = ((-1)*(near * (right + left))) / (right - left);
//		matrix[1][3] = ((-1)*(near * (top + bottom))) / (top - bottom);
//		matrix[2][3] = (2 * far * near) / (near - far);
//		return new Matrix(matrix);
//	}
}
