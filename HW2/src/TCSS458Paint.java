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
public class TCSS458Paint extends JPanel
{
	private static final long serialVersionUID = -6972067082022136019L;
	private static File selectedFile = null;
	private static int width;
	private static int height;
	private int imageSize; 
	private int[] pixels;       
    private double[][] zBuffer;
    private Map<String, int[]> colors;
    /**
     * Select a file to read data in current directory. 
     */
    static private void selectFile() {
        int approve; //return value from JFileChooser indicates if the user hit cancel
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
     * Reference at https://en.wikipedia.org/wiki/Bresenham%27s_line_algorithm
     * @param x0 of Point P0
     * @param y0 of Point P0
     * @param x1 of Point P1
     * @param y1 of Point P1
     * @param red value of red color
     * @param green value of green color
     * @param blue value of green color
     */
    public void plotLine(double[] p1, double[] p2, int red, int green, int blue ) {
    	// calculate dx & dy 
    	int r = 0;
    	int g = 0;
    	int b = 0;
    	double dx = p2[0] - p1[0]; 
    	double dy = p2[1] - p1[1]; 
    	double dz = p2[2] - p1[2];
    	
        // calculate steps required for generating pixels 
        double slopeyx = Math.abs(dx) >  Math.abs(dy) ?  Math.abs(dx) :  Math.abs(dy); 
      
        // calculate increment in x & y for each steps 
        double xInc = dx / (double) slopeyx; 
        double yInc = dy / (double) slopeyx; 
        double zInc = dz / (double) slopeyx;
     
        // Put pixel for each step 
        double x = p1[0];
        double y = p1[1]; 
        double z = p1[2];
        for (double i = 0; i <= slopeyx; i++) 
        { 
    		if (z <= zBuffer[(int)Math.round(x)][(int)Math.round(y)]) {
    			int[] color = new int[3];
    			color = colors.get((int)Math.round(x) +","+ (int)Math.round(y));
    			r = color[0];
    			g = color[1];
    			b = color[2];
			} else {
				int[] color = new int[3];
				color[0] = red;
				color[1] = green;
				color[2] = blue;
				r = red;
				g = green;
				b = blue;
				zBuffer[(int)Math.round(x)][(int)Math.round(y)] = z;
				colors.put((int)Math.round(x) +","+ (int)Math.round(y), color);
			
			}

        	drawPixel((int)Math.round(x), (int)Math.round(y), r, g, b); 
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
    	
    	// Load Identity Matrix
    	Matrix matrix = new Transformation().LoadIdentityMatrix();
    	
        Scanner input = getFile();
        while (input.hasNext()) {
            String command = input.next();                        
            if (command.equals("DIM")){
                width = input.nextInt();
                height = input.nextInt();
                imageSize = width * height;
                pixels = new int[imageSize * 3]; 
                zBuffer = new double[width][height];
                colors = new HashMap<String, int[]>();
                //Set white background 
                for (int i = 0; i < width; i++) {
                	for (int j = 0; j < height; j++) {
                		drawPixel( i, j, 255, 255, 255);
                		zBuffer[i][j] = Double.NEGATIVE_INFINITY;
                	}                	
                }
            }  else if (command.equals("RGB")) {
                r = (int) (input.nextDouble() * 255);
                g = (int) (input.nextDouble() * 255);                
                b = (int) (input.nextDouble() * 255);
            } else if (command.equals("LINE")){
            	double[] m1 = pointToMatrix(matrix, width, height, 
            										input.nextDouble(), 
            										input.nextDouble(), 
            										input.nextDouble());
            	double[] m2 = pointToMatrix(matrix, width, height, 
            										input.nextDouble(), 
            										input.nextDouble(), 
            										input.nextDouble());
                plotLine(m1, m2, r, g, b);                
            } else if (command.equals("LOAD_IDENTITY_MATRIX")) {
            	matrix = new Transformation().LoadIdentityMatrix();
            } else if (command.equals("SCALE")) {
            	matrix = new Transformation().Scaling(input.nextDouble(), 
            							input.nextDouble(),	input.nextDouble())
            								.multiply(matrix);
            } else if (command.equals("ROTATEZ")) {
            	matrix = new Transformation().RotationZ(input.nextDouble())
            								.multiply(matrix);
            } else if (command.equals("ROTATEY")) {
            	matrix = new Transformation().RotationY(input.nextDouble())
											.multiply(matrix);
            } else if (command.equals("ROTATEX")) {
            	matrix = new Transformation().RotationX(input.nextDouble())
											.multiply(matrix);
            } else if (command.equals("TRANSLATE")) {
            	matrix = new Transformation().Translation(input.nextDouble(), 
            							input.nextDouble(), input.nextDouble())
            								.multiply(matrix);
            } else if (command.equals("SOLID_CUBE")) {
            	
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
		double[] m1 = pointToMatrix(matrix, width, height, p1[0], p1[1], p1[2]);
		double[] m2 = pointToMatrix(matrix, width, height, p2[0], p2[1], p2[2]);
		double[] m3 = pointToMatrix(matrix, width, height, p3[0], p3[1], p3[2]);
		
		//Step 1: Draw the edges of a triangle first.
		plotLine(m1, m2, r, g, b);
		plotLine(m2, m3, r, g, b);
		plotLine(m3, m1, r, g, b);
		
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
	
	private void drawWireFrameCube(Matrix matrix, int r, int g, int b) {
		double[] p1 = new double[] {-1,-1,-1};
		double[] p2 = new double[] {1,-1,-1};
		double[] p3 = new double[] {-1,1,-1};
		double[] p4 = new double[] {1,1,-1};
		double[] p5 = new double[] {-1,-1,1};
		double[] p6 = new double[] {1,-1,1};
		double[] p7 = new double[] {-1,1,1};
		double[] p8 = new double[] {1,1,1};
		
//		drawTriangle(matrix, p1, p5, p7, r, g, b);
//		drawTriangle(matrix, p1, p3, p7, r, g, b);
		
//		drawTriangle(matrix, p1, p5, p6, r, g, b);
//		drawTriangle(matrix, p1, p2, p6, r, g, b);
//		
//		drawTriangle(matrix, p3, p7, p8, r, g, b);
//		drawTriangle(matrix, p3, p4, p8, r, g, b);
//				
//		drawTriangle(matrix, p5, p6, p7, r, g, b);
//		drawTriangle(matrix, p6, p7, p8, r, g, b);
//				
//		drawTriangle(matrix, p1, p2, p3, r, g, b);
//		drawTriangle(matrix, p2, p3, p4, r, g, b);
//		
//		drawTriangle(matrix, p2, p6, p8, r, g, b);
//		drawTriangle(matrix, p2, p4, p8, r, g, b);
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
		int r = 0;
    	int g = 0;
    	int b = 0;
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
        	
        	for (double x = minX; x < maxX; x++) {
        		double z = zValue(p0, p1, p2, x, y);
        		if (z <= zBuffer[(int)Math.round(x)][(int)Math.round(y)]) {
        			int[] color = new int[3];
        			color = colors.get((int)Math.round(x) +","+ (int)Math.round(y));
        			r = color[0];
        			g = color[1];
        			b = color[2];
        		} else {
        			int[] color = new int[3];
        			color[0] = red;
    				color[1] = green;
    				color[2] = blue; 
    				
    				r = red;
    				g = green;
    				b = blue;     
    				
        			zBuffer[(int)Math.round(x)][(int)Math.round(y)] = z;
        			colors.put((int)Math.round(x) +","+ (int)Math.round(y), color);
        		}
        		drawPixel((int)Math.round(x), (int)Math.round(y), r, g, b);
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
		crossProduct[0] = (p1.getY() - p0.getY()) * (p2.getZ() - p0.getZ()) - (p2.getY() - p0.getY()) * (p1.getZ() - p0.getZ());
		crossProduct[1] = (p1.getZ() - p0.getZ()) * (p2.getX() - p0.getX()) - (p1.getX() - p0.getX()) * (p2.getZ() - p0.getZ());
		crossProduct[2] = (p1.getX() - p0.getX()) * (p2.getY() - p0.getY()) - (p2.getX() - p0.getX()) * (p1.getY() - p0.getY());
		return p0.getZ() - (crossProduct[0] * (x - p0.getX()) + crossProduct[1] * (y - p0.getY())) / crossProduct[2];
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
        frame.getContentPane().add(rootPane);
        frame.pack();      
        frame.setLocationRelativeTo( null );
        frame.setVisible( true );
    }
}
