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
    private int[] colors;
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
    public void plotLine(double x0, double y0, double z0, double x1, double y1, 
    									double z1, int red, int green, int blue ) {
    	// calculate dx & dy 
    	double dx = x1 - x0; 
    	double dy = y1 - y0; 
    	double dz = z1 - z0;
      
        // calculate steps required for generating pixels 
        double slopeyx = Math.abs(dx) >  Math.abs(dy) ?  Math.abs(dx) :  Math.abs(dy); 
      
        // calculate increment in x & y for each steps 
        double xInc = dx / (double) slopeyx; 
        double yInc = dy / (double) slopeyx; 
        double zInc = dz / (double) slopeyx;
     
        // Put pixel for each step 
        double x = x0;
        double y = y0; 
        double z = z0;
        for (double i = 0; i <= slopeyx; i++) 
        { 
    		if (z <= zBuffer[(int)Math.round(x)][(int)Math.round(y)]) {
    			red = colors[0];
    			green = colors[1];
    			blue = colors[2];
			} else {
				colors[0] = red;
				colors[1] = green;
				colors[2] = blue;        			
				zBuffer[(int)Math.round(x)][(int)Math.round(y)] = z;
				System.out.println("(x, y, z) = (" + (int)Math.round(x) + ", " + (int)Math.round(y) + ", " + z + ")");
			}

        	drawPixel((int)Math.round(x), (int)Math.round(y), red, green, blue); 
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
                colors = new int[3];
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
            	Matrix matrix0 = new Matrix(4,1);
				matrix0 = matrix.multiply(new Matrix(input.nextDouble(), input.nextDouble(), input.nextDouble(), 1));
            	
            	Matrix matrix1 = new Matrix(4,1);
            	matrix1 = matrix.multiply(new Matrix(input.nextDouble(), input.nextDouble(), input.nextDouble(), 1));
            	
            	double x0 = convertToScreen(width,matrix0.getMatrix()[0][0]);
            	double y0 = convertToScreen(height, matrix0.getMatrix()[1][0]);
            	double z0 = matrix0.getMatrix()[2][0];
            	
            	double x1 = convertToScreen(width,matrix1.getMatrix()[0][0]);
            	double y1 = convertToScreen(height, matrix1.getMatrix()[1][0]);
            	double z1 = matrix1.getMatrix()[2][0];
                
                //Step 1: Draw the edges of a triangle first.
                plotLine((int)Math.round(x0), (int)Math.round(y0), z0,
                		(int)Math.round(x1), (int)Math.round(y1), z1, r, g, b);                
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
            }  else if (command.equals("TRI")) {            	
            	Matrix matrix0 = new Matrix(4,1);
            	matrix0 = matrix.multiply(new Matrix(input.nextDouble(), input.nextDouble(), input.nextDouble(), 1));
            	Matrix matrix1 = new Matrix(4,1);
            	matrix1 = matrix.multiply(new Matrix(input.nextDouble(), input.nextDouble(), input.nextDouble(), 1));
            	Matrix matrix2 = new Matrix(4,1);
            	matrix2 = matrix.multiply(new Matrix(input.nextDouble(), input.nextDouble(), input.nextDouble(), 1));
            	
            	double x0 = convertToScreen(width,matrix0.getMatrix()[0][0]);
            	double y0 = convertToScreen(height, matrix0.getMatrix()[1][0]);
            	double z0 = matrix0.getMatrix()[2][0];
            	double w0 = matrix0.getMatrix()[3][0];
            	
            	double x1 = convertToScreen(width,matrix1.getMatrix()[0][0]);
            	double y1 = convertToScreen(height, matrix1.getMatrix()[1][0]);
            	double z1 = matrix1.getMatrix()[2][0];
            	double w1 = matrix0.getMatrix()[3][0];
            	
            	double x2 = convertToScreen(width,matrix2.getMatrix()[0][0]);
            	double y2 = convertToScreen(height, matrix2.getMatrix()[1][0]);
            	double z2 = matrix2.getMatrix()[2][0];
            	double w2 = matrix0.getMatrix()[3][0];
            	                
                //Step 1: Draw the edges of a triangle first.
                plotLine((int)Math.round(x0), (int)Math.round(y0), z0, 
                		(int)Math.round(x1), (int)Math.round(y1), z1, r, g, b);
                plotLine((int)Math.round(x1), (int)Math.round(y1), z1,
                		(int)Math.round(x2), (int)Math.round(y2), z2, r, g, b);
                plotLine((int)Math.round(x2), (int)Math.round(y2), z2,
                		(int)Math.round(x0), (int)Math.round(y0), z0, r, g, b);
                
                //Step 2: Use scanline algorithm to fill the triangle.
                //Add vertexes to ArrayList to array them in x order.
                ArrayList<Point> points = new ArrayList<Point>(); 
                points.add(new Point(x0, y0, z0, w0));
                points.add(new Point(x1, y1, z1, w1));
                points.add(new Point(x2, y2, z2, w2));  
                
                //Sorts vertexes based on x
                Collections.sort(points);
                
                //Draw a triangle
                fillTriangle(points, r, g, b);

            }
        }
    }
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param xmin
	 * @param xmax
	 * @param p0
	 * @param p1
	 * @param p2
	 * @return
	 */
//	private double zValue(double x, double y, double xmin, double xmax, 
//										Point p0, Point p1, Point p2) {
//		double za = p2.getZ() - (p2.getZ() - p1.getZ()) 
//						* ((p2.getY() - y) / (p2.getY() - p1.getY()));
//		
//		double zb = p2.getZ() - (p2.getZ() - p0.getZ()) 
//				* ((p2.getY() - y) / (p2.getY() - p0.getY()));
//		
//		return zb - (zb - za) * ((xmax - x)/(xmax - xmin));
//	}
//	
	
 
    /**
     * Convert x or y in world to x or y in screen
     * @param size width or height
     * @param c x world or y world
     * @return x screen or y screen
     */
    private double convertToScreen(double size, double c) {
    	return (size - 1) * (c + 1)/2;
    }
    
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
	private void fillTriangle(ArrayList<Point> points, int r, int g, int b) {		
		//Sort vertices by Y
		sortByY(points);
		
		Point p0 = new Point();				
		Point p1 = new Point();				
		Point p2 = new Point();
		
		p0 = points.get(0);
		p1 = points.get(1);
		p2 = points.get(2);
    	
    	double minY = p0.getY();
    	double maxY = p2.getY();
    	double minX = 0;
    	double maxX = 0;
    	double minZ = 0;
    	double maxZ = 0;
    	
    	for (double y = minY; y < maxY; y++) {
    		if (y < points.get(1).getY()) {
    			minX = p0.getX() + (y - p0.getY()) / slope(p0.getX(), p0.getY(), p1.getX(), p1.getY());
    			maxX = p0.getX() + (y - p0.getY()) / slope(p0.getX(), p0.getY(), p2.getX(), p2.getY());
    			double temp = 0;
            	if (maxX < minX) {
            		temp = minX;
            		minX = maxX;
            		maxX = temp;
            	}
            	minZ = p0.getZ() + (minX - p0.getX()) / slope(p0.getX(), p0.getZ(), p1.getX(), p1.getZ());
            	maxZ = p0.getZ() + (maxX - p0.getX()) / slope(p0.getX(), p0.getZ(), p2.getX(), p2.getZ());    			
    		} else {
    			minX = p2.getX() + (y - p2.getY()) / slope(p2.getX(), p2.getY(), p1.getX(), p1.getY());
    			maxX = p2.getX() + (y - p2.getY()) / slope(p2.getX(), p2.getY(), p0.getX(), p0.getY());
    			double temp = 0;
            	if (maxX < minX) {
            		temp = minX;
            		minX = maxX;
            		maxX = temp;
            	}
            	minZ = p2.getZ() + (minX - p2.getX()) / slope(p2.getX(), p2.getZ(), p1.getX(), p1.getZ());
            	maxZ = p2.getZ() + (maxX - p2.getX()) / slope(p2.getX(), p2.getZ(), p0.getX(), p0.getZ()); 
    		}
    		
    		// We have x0 => minX, y0, z0 => minZ, x1 => maxX, y1, z1 => maxZ, 
    		plotLine(minX, y, minZ, maxX, y, maxZ, r, b, g);
    		
    		
    		
//    		double dy 
//        	double dz = (maxZ - minZ) / (maxX - minX);
//        	for (double x = minX; x < maxX; x++) {      		
//        		if (dz < zBuffer[(int)Math.round(x)][(int)Math.round(y)]) {
//        			r = colors[0];
//        			g = colors[1];
//        			b = colors[2];
//        		} else {
//        			colors[0] = r;
//        			colors[1] = g;
//        			colors[2] = b;        			
//        			zBuffer[(int)Math.round(x)][(int)Math.round(y)] = dz;
//        			System.out.println("(x, y, z) = (" + (int)Math.round(x) + ", " + (int)Math.round(y) + ", " + dz + ")");
//        		}
//
//        		drawPixel((int)Math.round(x), (int)Math.round(y), r, g, b);
//        	}
    	}
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
