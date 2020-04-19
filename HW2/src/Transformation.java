
public class Transformation {
	public Matrix LoadIdentityMatrix() {
		double[][] matrix = new double[4][4];
		matrix[0][0] = 1;
		matrix[1][1] = 1;
		matrix[2][2] = 1;
		matrix[3][3] = 1;
		return new Matrix(matrix);
	}
	
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
	
	public Matrix Scaling(double x, double y, double z) {
		double[][] matrix = new double[4][4];
		matrix[0][0] = x;
		matrix[1][1] = y;
		matrix[2][2] = z;		
		matrix[3][3] = 1;
		return new Matrix(matrix); 
	}
	
	public Matrix paralleProjection() {
		double[][] matrix = new double[4][4];
		matrix[0][0] = 1;
		matrix[1][1] = 1;
		matrix[2][2] = 0;		
		matrix[3][3] = 1;
		return new Matrix(matrix);
	}
	
//	/**
//	 * A parallel projection is one where all the lines that are
//	 * parallel in 3d space remain when projected.
//	 * @param p for any point (x, y, z, 1) the parallel projection is to screen coordinated (xp, yp)
//	 * @return a matrix [xp yp 0 1]^T
//	 * @throws Exception
//	 */
//	public Point perspectiveProjection() {
//		Matrix matrix = new Matrix(4,4);
//		matrix.getMatrix()[0][0] = 1;
//		matrix.getMatrix()[1][1] = 1;
//		matrix.getMatrix()[2][2] = 0;		
//		matrix.getMatrix()[3][3] = 1;
//		
//		Matrix newMatrix = new Matrix(4, 1);
//		newMatrix.getMatrix()[0][0] = this.getX();
//		newMatrix.getMatrix()[1][0] = this.getY();
//		newMatrix.getMatrix()[2][0] = this.getZ();
//		newMatrix.getMatrix()[3][0] = this.getW();
//		
//		Matrix converted = new Matrix(4, 1);
//		converted = matrix.multiply(newMatrix);		
//		return new Point(converted.getMatrix()[0][0], converted.getMatrix()[1][0], 
//						 converted.getMatrix()[2][0], converted.getMatrix()[3][0]); 
//	}
//	
//	/**
//	 * A parallel projection is one where all the lines that are
//	 * parallel in 3d space remain when projected.
//	 * @param p for any point (x, y, z, 1) the parallel projection is to screen coordinated (xp, yp)
//	 * @return a matrix [xp yp 0 1]^T
//	 * @throws Exception
//	 */
//	public Point obliqueProjection() {
//		Matrix matrix = new Matrix(4,4);
//		matrix.getMatrix()[0][0] = 1;
//		matrix.getMatrix()[1][1] = 1;
//		matrix.getMatrix()[2][2] = 0;		
//		matrix.getMatrix()[3][3] = 1;
//		
//		Matrix newMatrix = new Matrix(4, 1);
//		newMatrix.getMatrix()[0][0] = this.getX();
//		newMatrix.getMatrix()[1][0] = this.getY();
//		newMatrix.getMatrix()[2][0] = this.getZ();
//		newMatrix.getMatrix()[3][0] = this.getW();
//		
//		Matrix converted = new Matrix(4, 1);
//		converted = matrix.multiply(newMatrix);		
//		return new Point(converted.getMatrix()[0][0], converted.getMatrix()[1][0], 
//						 converted.getMatrix()[2][0], converted.getMatrix()[3][0]); 
//	}
}
