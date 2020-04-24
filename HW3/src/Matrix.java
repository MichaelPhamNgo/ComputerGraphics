import java.util.Arrays;
/**
 * Matrix library
 * @author pham19@uw.edu
 * Homework 2: Draw graphs in 3D
 * Due: April 24, 2020
 */
public class Matrix {
	private int rows;
	private int columns;
	private double[][] matrix;
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getColumns() {
		return columns;
	}
	public void setColumns(int columns) {
		this.columns = columns;
	}
	public double[][] getMatrix() {
		return matrix;
	}
	public void setMatrix(double[][] matrix) {
		this.matrix = matrix;
	}
	
	/**
	 * Construction a matrix with n rows and m columns
	 * @param rows
	 * @param columns
	 */
	public Matrix(int rows, int columns) {		
		this.rows = rows;
		this.columns = columns;
		this.matrix = new double[rows][columns];
	}
	
	/**
	 * @param matrix a 2D matrix
	 */
	public Matrix(double[][] matrix) {
		this.rows = matrix.length;
		this.columns = matrix[0].length;
		this.matrix = new double[this.rows][this.columns];
		this.matrix = matrix;
	}
	
	/**
	 * @param matrix a 2D matrix
	 */
	public Matrix(double x, double y, double z, double w) {
		this.rows = 4;
		this.columns = 1;
		this.matrix = new double[this.rows][this.columns];
		this.matrix[0][0] = x;
		this.matrix[1][0] = y;
		this.matrix[2][0] = z;
		this.matrix[3][0] = w;
	}
	
	/** 
	 * @param rows
	 * @param columns
	 * @return a 2D matrix 
	 */
	public Matrix createIdentityMatrix(int rows, int columns) {
		if (rows != columns) {
			throw new IllegalArgumentException ("Cannot compute this operation");
		}	
		double[][] identityMatrix = new double[rows][columns];	
		for(int i = 0; i < rows; i++) {			
			identityMatrix[i][i] = 1;			
		}
		return new Matrix(identityMatrix);
	}
	
	/**
	 * Addition two matrices
	 * @param anotherMatrix a matrix 
	 * @return a matrix after addition
	 * @throws Exception
	 */
	public Matrix add(Matrix anotherMatrix) {
		if (anotherMatrix.rows != this.rows && anotherMatrix.columns != this.columns) {
			throw new IllegalArgumentException ("Cannot compute this operation");
		}	
		double[][] sum = new double[this.rows][this.columns];	
		for(int i = 0; i < this.rows; i++) {
			for(int j = 0; j < this.columns; j++) {
				sum[i][j] = this.matrix[i][j] + anotherMatrix.getMatrix()[i][j];
			}
		}		
		return new Matrix(sum);
	}
	
	/**
	 * Subtraction two matrices
	 * @param anotherMatrix a matrix 
	 * @return a matrix after subtraction
	 * @throws Exception
	 */
	public Matrix minus(Matrix anotherMatrix) {
		if (anotherMatrix.rows != this.rows && anotherMatrix.columns != this.columns) {
			throw new IllegalArgumentException("Cannot compute this calculation");
		}		
		double[][] subtract = new double[this.rows][this.columns];
		for(int i = 0; i < this.rows; i++) {
			for(int j = 0; j < this.columns; j++) {
				subtract[i][j] = this.matrix[i][j] - anotherMatrix.getMatrix()[i][j];
			}
		}		
		return new Matrix(subtract);
	}
	
	/**
	 * Transpose a matrix
	 * @return a matrix after transpose
	 * @throws Exception
	 */
	public Matrix transpose() {
		if (this.rows != this.columns) {
			throw new IllegalArgumentException("Cannot compute this calculation");
		}
		double[][] transpose = new double[this.rows][this.columns];
		for(int i = 0; i < rows; i++) 
            for(int j = 0; j < columns; j++) 
            	this.getMatrix()[i][j] = this.getMatrix()[j][i];
		return new Matrix(transpose);
	}
	
	/**
	 * Multiplication two matrices
	 * @param anotherMatrix a matrix 
	 * @return a matrix after multiplication
	 * @throws Exception
	 */
	public Matrix multiply(Matrix anotherMatrix) {
		if (this.columns != anotherMatrix.rows) { 
			throw new IllegalArgumentException("Cannot compute this calculation");
		}
		
		double[][] product = new double[this.rows][anotherMatrix.columns];
		for (int i = 0; i < this.rows; i++) { 
			for (int j = 0; j < anotherMatrix.columns; j++) { 
				for (int k = 0; k < anotherMatrix.rows; k++) { 
					product[i][j] +=  this.getMatrix()[i][k] * anotherMatrix.getMatrix()[k][j]; 
				} 
			} 
		} 
		return new Matrix(product);
	}
	
	@Override
	public String toString() {
		return "Matrix [rows=" + rows + ", columns=" + columns + ", matrix=" + Arrays.toString(matrix) + "]";
	}
}
