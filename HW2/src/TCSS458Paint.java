import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
/**
 * Implements drawing shapes homework 2
 * @author pham19@uw.edu
 * Homework 2: Draw graphs in 3D
 * Due: April 24, 2020
 */
public class TCSS458Paint extends JPanel implements KeyListener 
{
	private static final long serialVersionUID = -6972067082022136019L;
	private static File selectedFile = null;
	private static int width;
	private static int height;
	private int imageSize; 
	private int[] pixels; 
	
	// 2D Array containing z value after computing 
    private double[][] zBuffer;
    
    // A Hash Map containing a string (x, y) and its colors
    private Map<String, int[]> colors;
    
    /**
     * Select a file to read data in current directory. 
     */
    static private void selectFile() {
        int approve;
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        approve = chooser.showOpenDialog(null);
        if (approve != JFileChooser.APPROVE_OPTION) {
            System.exit(0);
        } else {
            selectedFile = chooser.getSelectedFile();
        }
    }

    static private Scanner getFile() {
        Scanner input = null; 
        try {            
   	       input = new Scanner(selectedFile);
        } catch (Exception e) {
    	       JOptionPane.showMessageDialog(null, 
                "There was an error with the file you chose.", 
                "File Error", JOptionPane.ERROR_MESSAGE);	
    	   }
    	   return input;
    }

    /**
     * Read dimension for panel. 
     * @param rootPane
     */
    static void getDim(JPanel rootPane) {
        Scanner input = getFile();        
        String command = input.next();
        if (command.equals("DIM")){
            width = input.nextInt();
            height = input.nextInt();
            rootPane.setPreferredSize(new Dimension(width,height));
        }
    }
    
    /**
     * Draw a pixel at coordinate P(x, y) with color in RGB
     */
    void drawPixel(int x, int y, int r, int g, int b) {
        pixels[(height-y-1)*width*3+x*3] = r;
        pixels[(height-y-1)*width*3+x*3+1] = g;
        pixels[(height-y-1)*width*3+x*3+2] = b;                
    }
    
    /**
     * Draw a line from p1 (x1, y1, z1) to p2 (x1, y1, z1) with DDA algorithm
     * @param matrix transformed matrix
     * @param p1 point p1
     * @param p2 point p2
     * @param red 
     * @param green
     * @param blue
     */
    public void plotLine(Matrix matrix, double[] p1, double[] p2, int red, int green, int blue ) {
    	// After transformation, converts p1 in world to screen
    	double[] m1 = pointToMatrix(matrix, width, height, p1[0], p1[1], p1[2]);
		
    	// After transformation, converts p2 in world to screen
    	double[] m2 = pointToMatrix(matrix, width, height, p2[0], p2[1], p2[2]);
    	
		double dx = m2[0] - m1[0]; 
    	double dy = m2[1] - m1[1]; 
    	double dz = m2[2] - m1[2];
    	
        // calculate steps required for generating pixels 
        double slope = Math.abs(dx) >  Math.abs(dy) ?  Math.abs(dx) :  Math.abs(dy); 
      
        // calculate increment in x & y & z for each steps 
        double xInc = dx / (double) slope; 
        double yInc = dy / (double) slope; 
        double zInc = dz / (double) slope;
     
        // Put pixel for each step 
        double x = m1[0];
        double y = m1[1]; 
        double z = m1[2];
        
        // Array color
        int[] color = new int[3];
        
        // Draw a line
        for (double i = 0; i <= slope; i++) 
        { 
        	// how to calculate z value, watch video week2 lecture 2 drawing a line
        	// zBuffer containing z value. If z value at (x, y) <= zBuffer(x, y) in last step
        	// get color value of z last step to draw
    		if (z <= zBuffer[(int)Math.round(x)][(int)Math.round(y)]) {
    			color = new int[3];
    			color = colors.get((int)Math.round(x) +","+ (int)Math.round(y));
			} else {
				// else if z value > z zBuffer(x, y) in last step
				// use current color to draw and update zBuffer with a new z 
				// and update colors map at (x, y) with a new color
				color = new int[3];
				color[0] = red;
				color[1] = green;
				color[2] = blue;
				
				colors.put((int)Math.round(x) +","+ (int)Math.round(y), color);
				zBuffer[(int)Math.round(x)][(int)Math.round(y)] = z;
			}

        	drawPixel((int)Math.round(x), (int)Math.round(y), color[0], color[1], color[2]); 
            x += xInc;           // increment in x at each step 
            y += yInc;           // increment in y at each step 
            z += zInc;			// increment in y at each step 
        } 
    }
    
    /**
	 * Use graphic in Java library to draw pixel 
	 */
    public void paintComponent(Graphics g) {
        createImage();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        WritableRaster wr_raster = image.getRaster();
        wr_raster.setPixels(0, 0, width, height, pixels);        
        g.drawImage(image, 0, 0, null);
    }
    
    /**
     * Draw lines or triangles.
     */
	void createImage() {
    	// RGB color
    	int r, g, b;
    	r = g = b = 0;  
    	
    	// Setup Identity Matrix to ready
    	Matrix matrix = new Transformation().LoadIdentityMatrix();
    	
        Scanner input = getFile();
        while (input.hasNext()) {
            String command = input.next();                        
            if (command.equals("DIM")){
                width = input.nextInt();
                height = input.nextInt();
                imageSize = width * height;
                pixels = new int[imageSize * 3];
                
                // 2D Array containing z value after computing 
                zBuffer = new double[width][height];
                
                // A Hash Map containing a string (x, y) and its colors
                colors = new HashMap<String, int[]>();
                
                //Set background 
                for (int i = 0; i < width; i++) {
                	for (int j = 0; j < height; j++) {
                		drawPixel( i, j, 255, 255, 255);
                		zBuffer[i][j] = Double.NEGATIVE_INFINITY;	//Initial zBuffer array
                	}                	
                }
            }  else if (command.equals("RGB")) {
                r = (int) (input.nextDouble() * 255);
                g = (int) (input.nextDouble() * 255);                
                b = (int) (input.nextDouble() * 255);
            } else if (command.equals("LINE")){            	
            	// Point 1
            	double[] p1 = new double[] {input.nextDouble(), 
            									input.nextDouble(), 
            										input.nextDouble()};
            	// Point 2
            	double[] p2 = new double[] {input.nextDouble(), 
            									input.nextDouble(), 
            										input.nextDouble()};            	
            	// Draw a line through two points with a transformation matrix and its color 
                plotLine(matrix , p1, p2, r, g, b);                
            } else if (command.equals("LOAD_IDENTITY_MATRIX")) { // load identity matrix
            	matrix = new Transformation().LoadIdentityMatrix();	
            } else if (command.equals("SCALE")) { // scale an object
            	matrix = new Transformation().Scaling(input.nextDouble(), 
							input.nextDouble(),	input.nextDouble()).multiply(matrix);
            } else if (command.equals("ROTATEZ")) {	// rotate an object via z axis
            	matrix = new Transformation().RotationZ(input.nextDouble()).multiply(matrix);
            } else if (command.equals("ROTATEY")) { // rotate an object via y axis
            	matrix = new Transformation().RotationY(input.nextDouble()).multiply(matrix);
            } else if (command.equals("ROTATEX")) { // rotate an object via x axis
            	matrix = new Transformation().RotationX(input.nextDouble())
											.multiply(matrix);
            } else if (command.equals("TRANSLATE")) {
            	matrix = new Transformation().Translation(input.nextDouble(), 
            							input.nextDouble(), input.nextDouble())
            								.multiply(matrix);
            } else if (command.equals("SOLID_CUBE")) {
            	drawSolidCube(matrix, r, g, b);
            } else if (command.equals("WIREFRAME_CUBE")) {
            	drawWireFrameCube(matrix, r, g, b);
            } else if (command.equals("TRI")) { 
            	double[] p1 = new double[] {input.nextDouble(), input.nextDouble(), input.nextDouble()};
            	double[] p2 = new double[] {input.nextDouble(), input.nextDouble(), input.nextDouble()};
            	double[] p3 = new double[] {input.nextDouble(), input.nextDouble(), input.nextDouble()};
            	drawTriangle(matrix, p1, p2, p3, r, g, b);
            }
        }
    }
	
	private void drawTriangle(Matrix matrix, double[] p1, double[] p2, double[] p3, int r, int g, int b) {
		//Step 1: Draw the edges of a triangle first.
		plotLine(matrix, p1, p2, r, g, b);
		plotLine(matrix, p2, p3, r, g, b);
		plotLine(matrix, p3, p1, r, g, b);
		
		double[] m1 = pointToMatrix(matrix, width, height, p1[0], p1[1], p1[2]);
		double[] m2 = pointToMatrix(matrix, width, height, p2[0], p2[1], p2[2]);
		double[] m3 = pointToMatrix(matrix, width, height, p3[0], p3[1], p3[2]);
		
		//Step 2: Use scanline algorithm to fill the triangle.
		//Add vertexes to ArrayList to array them in x order.
		ArrayList<Point> points = new ArrayList<Point>(); 
		points.add(new Point(m1[0], m1[1], m1[2], m1[3]));
		points.add(new Point(m2[0], m2[1], m2[2], m2[3]));
		points.add(new Point(m3[0], m3[1], m3[2], m3[3]));  
		
		//Sorts vertexes based on x
		Collections.sort(points);
		
		//Draw a triangle
		fillTriangle(points, r, g, b);
	}
	
	private void drawSolidCube(Matrix matrix, int r, int g, int b) {
		double[] p1 = new double[] {-0.5,-0.5,-0.5};
		double[] p2 = new double[] {0.5,-0.5,-0.5};
		double[] p3 = new double[] {-0.5,0.5,-0.5};
		double[] p4 = new double[] {0.5,0.5,-0.5};
		double[] p5 = new double[] {-0.5,-0.5,0.5};
		double[] p6 = new double[] {0.5,-0.5,0.5};
		double[] p7 = new double[] {-0.5,0.5,0.5};
		double[] p8 = new double[] {0.5,0.5,0.5};
		
		plotLine(matrix, p1, p3, r, g, b);
		plotLine(matrix, p1, p5, r, g, b);
		plotLine(matrix, p3, p7, r, g, b);
		plotLine(matrix, p5, p7, r, g, b);
				
		plotLine(matrix, p1, p2, r, g, b);
		plotLine(matrix, p2, p6, r, g, b);
		plotLine(matrix, p5, p6, r, g, b);
		plotLine(matrix, p7, p8, r, g, b);
				
		plotLine(matrix, p3, p4, r, g, b);
		plotLine(matrix, p4, p8, r, g, b);
		plotLine(matrix, p6, p8, r, g, b);
		plotLine(matrix, p2, p4, r, g, b);
	}
	
	/**
	 * 
	 * @param matrix
	 * @param r
	 * @param g
	 * @param b
	 */
	private void drawWireFrameCube(Matrix matrix, int r, int g, int b) {
		double[] p1 = new double[] {-0.5,-0.5,-0.5};
		double[] p2 = new double[] {0.5,-0.5,-0.5};
		double[] p3 = new double[] {-0.5,0.5,-0.5};
		double[] p4 = new double[] {0.5,0.5,-0.5};
		double[] p5 = new double[] {-0.5,-0.5,0.5};
		double[] p6 = new double[] {0.5,-0.5,0.5};
		double[] p7 = new double[] {-0.5,0.5,0.5};
		double[] p8 = new double[] {0.5,0.5,0.5};
		
		drawTriangle(matrix, p1, p5, p7, r, g, b);
		drawTriangle(matrix, p1, p3, p7, r, g, b);
		
		drawTriangle(matrix, p1, p5, p6, r, g, b);
		drawTriangle(matrix, p1, p2, p6, r, g, b);
		
		drawTriangle(matrix, p3, p7, p8, r, g, b);
		drawTriangle(matrix, p3, p4, p8, r, g, b);
				
		drawTriangle(matrix, p5, p6, p7, r, g, b);
		drawTriangle(matrix, p6, p7, p8, r, g, b);
				
		drawTriangle(matrix, p1, p2, p3, r, g, b);
		drawTriangle(matrix, p2, p3, p4, r, g, b);
		
		drawTriangle(matrix, p2, p6, p8, r, g, b);
		drawTriangle(matrix, p2, p4, p8, r, g, b);
	}
	
	/**
	 * 
	 * @param matrix
	 * @param width
	 * @param height
	 * @param v1
	 * @param v2
	 * @param v3
	 * @return
	 */
	private double[] pointToMatrix(Matrix matrix, int width, int height, 
											double v1, double v2, double v3) {
		double[] data = new double[4];
		Matrix matrix0 = new Matrix(4,1);
    	matrix0 = matrix.multiply(new Matrix(v1, v2, v3, 1));
    	data[0] = convertToScreen(width,matrix0.getMatrix()[0][0]);
    	data[1] = convertToScreen(height, matrix0.getMatrix()[1][0]);
    	data[2] = matrix0.getMatrix()[2][0];
    	data[3] = matrix0.getMatrix()[3][0];
    	return data;
    	
	}
 
    /**
     * Convert x or y in world to x or y in screen
     * @param size width or height
     * @param c x world or y world
     * @return x screen or y screen
     */
    private double convertToScreen(double size, double c) {
    	return (size - 1) * (c + 1)/2;
    }
    
    /**
     * 
     * @param x0
     * @param y0
     * @param x1
     * @param y1
     * @return
     */
    private double slope(double x0, double y0, double x1, double y1) {
    	return (y0 - y1)/(x0 - x1);
    }
    
    /**
     * fill a triangle with 3 vertexes
     * @param vertexes v0, v1, v2
     * @param minY 
     * @param maxY
     * @param y
     * @return an array minx, maxx
     */
	private void fillTriangle(ArrayList<Point> points, int red, int green, int blue ) {			
    	double minX = 0;
    	double maxX = 0;
    	Point p0 = new Point();				
		Point p1 = new Point();				
		Point p2 = new Point();
    	
    	//Sort vertices by Y
		sortByY(points);
		p0 = points.get(0);
		p1 = points.get(1);
		p2 = points.get(2);
		
		
    	double minY = p0.getY();
    	double maxY = p2.getY();
    	
    	for (double y = minY; y < maxY; y++) {
    		if (y < points.get(1).getY()) {
    			minX = p0.getX() + (y - p0.getY()) / slope(p0.getX(), p0.getY(), p1.getX(), p1.getY());
    			maxX = p0.getX() + (y - p0.getY()) / slope(p0.getX(), p0.getY(), p2.getX(), p2.getY());
    		} else {
    			minX = p2.getX() + (y - p2.getY()) / slope(p2.getX(), p2.getY(), p1.getX(), p1.getY());
     			maxX = p2.getX() + (y - p2.getY()) / slope(p2.getX(), p2.getY(), p0.getX(), p0.getY());
    		}    		
    		
    		double temp = 0;
        	if (maxX < minX) {
        		temp = minX;
        		minX = maxX;
        		maxX = temp;
        	}
        	int[] color = new int[3];
        	for (double x = minX; x < maxX; x++) {
        		double z = zValue(p0, p1, p2, x, y);
        		if (z <= zBuffer[(int)Math.round(x)][(int)Math.round(y)]) {
        			color = new int[3];
        			color = colors.get((int)Math.round(x) +","+ (int)Math.round(y));
        		} else {
        			color = new int[3];
        			color[0] = red;
    				color[1] = green;
    				color[2] = blue; 
        			zBuffer[(int)Math.round(x)][(int)Math.round(y)] = z;
        			colors.put((int)Math.round(x) +","+ (int)Math.round(y), color);
        		}
        		drawPixel((int)Math.round(x), (int)Math.round(y), color[0], color[1], color[2]);
        	}
    	}
    }
	
	/**
	 * 
	 * @param p0
	 * @param p1
	 * @param p2
	 * @param x
	 * @param y
	 * @return
	 */
	private double zValue(Point p0, Point p1, Point p2, double x, double y) {
		double[] crossProduct = new double[3];
		crossProduct[0] = (p1.getY() - p0.getY()) * (p2.getZ() - p0.getZ()) 
							- (p2.getY() - p0.getY()) * (p1.getZ() - p0.getZ());
		crossProduct[1] = (p1.getZ() - p0.getZ()) * (p2.getX() - p0.getX()) 
							- (p1.getX() - p0.getX()) * (p2.getZ() - p0.getZ());
		crossProduct[2] = (p1.getX() - p0.getX()) * (p2.getY() - p0.getY()) 
							- (p2.getX() - p0.getX()) * (p1.getY() - p0.getY());
		return p0.getZ() - (crossProduct[0] * (x - p0.getX()) 
							+ crossProduct[1] * (y - p0.getY())) / crossProduct[2];
	}
	
	/**
	 * Sorts vertexes by y.
	 * @param vertexes
	 */
	public void sortByY(ArrayList<Point> points) {
		Point point = new Point();
		for (int i = 0; i < points.size() - 1; i++) {
			for(int j = i + 1; j < points.size(); j++) {
				if (points.get(i).getY() > points.get(j).getY()) {
					point = points.get(i);
					points.set(i, points.get(j));
					points.set(j, point);
				}
			}
		}
	}
	
    /**
     * 
     * @param args
     */
    public static void main(String args[]) {
        JFrame frame = new JFrame("LINE DEMO");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        selectFile();
        JPanel rootPane = new TCSS458Paint();    
        getDim(rootPane);
        rootPane.setPreferredSize(new Dimension(width,height));
        frame.addKeyListener((KeyListener) rootPane);
        frame.getContentPane().add(rootPane);
        frame.pack();      
        frame.setLocationRelativeTo( null );
        frame.setVisible( true );
    }
    
    public class Point implements Comparable<Point>{
    	private double x;
    	private double y;
    	private double z;
    	private double w;
    	
    	public Point() {}
    	
    	public Point(double x, double y, double z, double w) {
    		this.x = x;
    		this.y = y;
    		this.z = z;
    		this.w = w;
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

    	@Override
    	public String toString() {
    		return "Point [x=" + this.x + ", y=" + this.y + ", z=" 
    						+ this.z + ", w=" + this.w + "]";
    	}

    	@Override
    	public int compareTo(Point o) {
    		return (int)(this.x - o.x);
    	}
    }

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
	    switch( keyCode ) { 
	        case KeyEvent.VK_UP:
	            System.out.println("Key up");
	            break;
	        case KeyEvent.VK_DOWN:
	            System.out.println("Key down");
	            // handle down 
	            break;
	        case KeyEvent.VK_LEFT:
	            System.out.println("Key left");
	            // handle left
	            break;
	        case KeyEvent.VK_RIGHT :
	            System.out.println("Key right");
	            // handle right
	            break;
	     }
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
