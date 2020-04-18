public class Point {
		private double x;
		private double y;
		private double z;
		private double w;
		
		public Point(double x, double y, double z, double w) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.w = w;
		}
		
		public Point() {
			this(0, 0, 0, 1);			
		}
		
		public double getX() {
			return x;
		}

		public void setX(double x) {
			this.x = x;
		}
		
		public double getY() {
			return y;
		}

		public void setY(double y) {
			this.y = y;
		}

		public double getZ() {
			return z;
		}

		public void setZ(double z) {
			this.z = z;
		}

		public double getW() {
			return w;
		}

		public void setW(double w) {
			this.w = w;
		}
				
		/**
		 * p' = T * p
		 * The translation algorithm.
		 * @param x translate with a distance x
		 * @param y translate with a distance y
		 * @param z translate with a distance z
		 * @param p a point want to translate
		 * @return p' a point after translation
		 * @throws Exception 
		 */
		public Point Translation(double x, double y, double z, Point p) {
			Matrix matrix = new Matrix(4,4);
			matrix.getMatrix()[0][0] = 1;
			matrix.getMatrix()[1][1] = 1;
			matrix.getMatrix()[2][2] = 1;
			matrix.getMatrix()[3][3] = 1;		
			matrix.getMatrix()[0][3] = x;
			matrix.getMatrix()[1][3] = y;
			matrix.getMatrix()[2][3] = z;
			
			Matrix newMatrix = new Matrix(4, 1);
			newMatrix.getMatrix()[0][0] = p.getX();
			newMatrix.getMatrix()[1][0] = p.getY();
			newMatrix.getMatrix()[2][0] = p.getZ();
			newMatrix.getMatrix()[3][0] = p.getW();
		
			Matrix converted = new Matrix(4, 1);
			converted = matrix.multiply(newMatrix);		
			return new Point(converted.getMatrix()[0][0], converted.getMatrix()[1][0], 
							 converted.getMatrix()[2][0], converted.getMatrix()[3][0]); 
		}
		
		/**
		 * The rotation about Z axis
		 * @param theta rotate a theta
		 * @param p a point want to rotate
		 * @return p' a point after z rotation
		 * @throws Exception 
		 */
		public Point RotationZ(double theta, Point p) {
			Matrix matrix = new Matrix(4,4);
			matrix.getMatrix()[0][0] = Math.cos(theta);
			matrix.getMatrix()[0][1] = -Math.sin(theta);
			matrix.getMatrix()[1][0] = Math.sin(theta);
			matrix.getMatrix()[1][1] = Math.cos(theta);
			matrix.getMatrix()[2][2] = 1;
			matrix.getMatrix()[3][3] = 1;
			
			Matrix newMatrix = new Matrix(4, 1);
			newMatrix.getMatrix()[0][0] = p.getX();
			newMatrix.getMatrix()[1][0] = p.getY();
			newMatrix.getMatrix()[2][0] = p.getZ();
			newMatrix.getMatrix()[3][0] = p.getW();
		
			Matrix converted = new Matrix(4, 1);
			converted = matrix.multiply(newMatrix);		
			return new Point(converted.getMatrix()[0][0], converted.getMatrix()[1][0], 
							 converted.getMatrix()[2][0], converted.getMatrix()[3][0]); 
		}
		
		/**
		 * The rotation about X axis
		 * @param theta rotate a theta
		 * @param p a point want to rotate
		 * @return p' a point after x rotation
		 * @throws Exception 
		 */
		public Point RotationX(double theta, Point p) {
			Matrix matrix = new Matrix(4,4);
			matrix.getMatrix()[0][0] = 1;
			matrix.getMatrix()[1][1] = Math.cos(theta);
			matrix.getMatrix()[1][2] = -Math.sin(theta);
			matrix.getMatrix()[2][1] = Math.sin(theta);
			matrix.getMatrix()[2][2] = Math.cos(theta);		
			matrix.getMatrix()[3][3] = 1;
			
			Matrix newMatrix = new Matrix(4, 1);
			newMatrix.getMatrix()[0][0] = p.getX();
			newMatrix.getMatrix()[1][0] = p.getY();
			newMatrix.getMatrix()[2][0] = p.getZ();
			newMatrix.getMatrix()[3][0] = p.getW();
		
			Matrix converted = new Matrix(4, 1);
			converted = matrix.multiply(newMatrix);		
			return new Point(converted.getMatrix()[0][0], converted.getMatrix()[1][0], 
							 converted.getMatrix()[2][0], converted.getMatrix()[3][0]); 
		}
		
		/**
		 * The rotation about Y axis
		 * @param theta rotate a theta
		 * @param p a point want to rotate
		 * @return p' a point after y rotation
		 * @throws Exception 
		 */
		public Point RotationY(double theta, Point p) {
			Matrix matrix = new Matrix(4,4);
			matrix.getMatrix()[0][0] = Math.cos(theta);
			matrix.getMatrix()[0][2] = Math.sin(theta);
			matrix.getMatrix()[1][1] = 1;
			matrix.getMatrix()[2][0] = -Math.sin(theta);
			matrix.getMatrix()[2][2] = Math.cos(theta);		
			matrix.getMatrix()[3][3] = 1;
			
			Matrix newMatrix = new Matrix(4, 1);
			newMatrix.getMatrix()[0][0] = p.getX();
			newMatrix.getMatrix()[1][0] = p.getY();
			newMatrix.getMatrix()[2][0] = p.getZ();
			newMatrix.getMatrix()[3][0] = p.getW();
		
			Matrix converted = new Matrix(4, 1);
			converted = matrix.multiply(newMatrix);		
			return new Point(converted.getMatrix()[0][0], converted.getMatrix()[1][0], 
							 converted.getMatrix()[2][0], converted.getMatrix()[3][0]); 
		}
		
		/**
		 * p' = S*p
		 * The scaling algorithm.
		 * @param x scaling with a distance x
		 * @param y scaling with a distance y
		 * @param z scaling with a distance z
		 * @param p a point want to scale
		 * @return p' a point after scaling
		 * @throws Exception 
		 */
		public Point Scaling(double x, double y, double z, Point p) {
			Matrix matrix = new Matrix(4,4);
			matrix.getMatrix()[0][0] = x;
			matrix.getMatrix()[1][1] = y;
			matrix.getMatrix()[2][2] = z;		
			matrix.getMatrix()[3][3] = 1;
			
			Matrix newMatrix = new Matrix(4, 1);
			newMatrix.getMatrix()[0][0] = p.getX();
			newMatrix.getMatrix()[1][0] = p.getY();
			newMatrix.getMatrix()[2][0] = p.getZ();
			newMatrix.getMatrix()[3][0] = p.getW();
		
			Matrix converted = new Matrix(4, 1);
			converted = matrix.multiply(newMatrix);		
			return new Point(converted.getMatrix()[0][0], converted.getMatrix()[1][0], 
							 converted.getMatrix()[2][0], converted.getMatrix()[3][0]); 
		}
		
		/**
		 * A parallel projection is one where all the lines that are
		 * parallel in 3d space remain when projected.
		 * @param p for any point (x, y, z, 1) the parallel projection is to screen coordinated (xp, yp)
		 * @return a matrix [xp yp 0 1]^T
		 * @throws Exception
		 */
		public Point paralleProjection() {
			Matrix matrix = new Matrix(4,4);
			matrix.getMatrix()[0][0] = 1;
			matrix.getMatrix()[1][1] = 1;
			matrix.getMatrix()[2][2] = 0;		
			matrix.getMatrix()[3][3] = 1;
			
			Matrix newMatrix = new Matrix(4, 1);
			newMatrix.getMatrix()[0][0] = this.getX();
			newMatrix.getMatrix()[1][0] = this.getY();
			newMatrix.getMatrix()[2][0] = this.getZ();
			newMatrix.getMatrix()[3][0] = this.getW();
			
			Matrix converted = new Matrix(4, 1);
			converted = matrix.multiply(newMatrix);		
			return new Point(converted.getMatrix()[0][0], converted.getMatrix()[1][0], 
							 converted.getMatrix()[2][0], converted.getMatrix()[3][0]); 
		}
		
		/**
		 * A parallel projection is one where all the lines that are
		 * parallel in 3d space remain when projected.
		 * @param p for any point (x, y, z, 1) the parallel projection is to screen coordinated (xp, yp)
		 * @return a matrix [xp yp 0 1]^T
		 * @throws Exception
		 */
		public Point perspectiveProjection() {
			Matrix matrix = new Matrix(4,4);
			matrix.getMatrix()[0][0] = 1;
			matrix.getMatrix()[1][1] = 1;
			matrix.getMatrix()[2][2] = 0;		
			matrix.getMatrix()[3][3] = 1;
			
			Matrix newMatrix = new Matrix(4, 1);
			newMatrix.getMatrix()[0][0] = this.getX();
			newMatrix.getMatrix()[1][0] = this.getY();
			newMatrix.getMatrix()[2][0] = this.getZ();
			newMatrix.getMatrix()[3][0] = this.getW();
			
			Matrix converted = new Matrix(4, 1);
			converted = matrix.multiply(newMatrix);		
			return new Point(converted.getMatrix()[0][0], converted.getMatrix()[1][0], 
							 converted.getMatrix()[2][0], converted.getMatrix()[3][0]); 
		}
		
		/**
		 * A parallel projection is one where all the lines that are
		 * parallel in 3d space remain when projected.
		 * @param p for any point (x, y, z, 1) the parallel projection is to screen coordinated (xp, yp)
		 * @return a matrix [xp yp 0 1]^T
		 * @throws Exception
		 */
		public Point obliqueProjection() {
			Matrix matrix = new Matrix(4,4);
			matrix.getMatrix()[0][0] = 1;
			matrix.getMatrix()[1][1] = 1;
			matrix.getMatrix()[2][2] = 0;		
			matrix.getMatrix()[3][3] = 1;
			
			Matrix newMatrix = new Matrix(4, 1);
			newMatrix.getMatrix()[0][0] = this.getX();
			newMatrix.getMatrix()[1][0] = this.getY();
			newMatrix.getMatrix()[2][0] = this.getZ();
			newMatrix.getMatrix()[3][0] = this.getW();
			
			Matrix converted = new Matrix(4, 1);
			converted = matrix.multiply(newMatrix);		
			return new Point(converted.getMatrix()[0][0], converted.getMatrix()[1][0], 
							 converted.getMatrix()[2][0], converted.getMatrix()[3][0]); 
		}
		

		@Override
		public String toString() {
			return "Point [x=" + x + ", y=" + y + ", z=" + z + ", w=" + w + "]";
		}
	}