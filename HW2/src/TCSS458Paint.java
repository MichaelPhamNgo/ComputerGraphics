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
    public void plotLine(double x0, double y0, double z0, double x1, double y1, 
    									double z1, int red, int green, int blue ) {
    	// calculate dx & dy 
    	double dx = x1 - x0; 
    	double dy = y1 - y0; 
    	double dz = z1 - z0;
    	
    	int r = 0;
    	int g = 0;
    	int b = 0;
    	
      
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
    			int[] color = new int[3];
    			color = colors.get((int)Math.round(x) +","+ (int)Math.round(y));
    			r = color[0];
    			g = color[1];
    			b = color[2];
			} else {
				int[] color = new int[3];
				color[0] = red;
				color[1] = green;
				color[2] = blue;r = red;
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
                plotLine(x0, y0, z0, x1, y1, z1, r, g, b);                
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
                plotLine(x0, y0, z0, x1, y1, z1, r, g, b);
                plotLine(x1, y1, z1, x2, y2, z2, r, g, b);
                plotLine(x2, y2, z2, x0, y0, z0, r, g, b);
                
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
    	
    	int r = 0;
    	int g = 0;
    	int b = 0;
    	
    	
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
