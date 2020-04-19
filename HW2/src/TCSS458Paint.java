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
     * Draw line in horizontal
     * @param x0 of Point P0
     * @param y0 of Point P0
     * @param x1 of Point P1
     * @param y1 of Point P1
     * @param red value of red color
     * @param green value of green color
     * @param blue value of green color
     */
    public void plotLineLow(int x0, int y0, int x1, int y1, 
    						int red, int green, int blue) {
    	int dx = x1 - x0;
    	int dy = y1 - y0;
    	int yi = 1;
    	if (dy < 0) {
    		yi = -1;
    		dy = -dy;    		
    	}
    	int D = 2*dy - dx;
    	int y = y0;
    	for (int x = x0; x < x1; x++) {
    		drawPixel(x, y, red, green, blue);
    		if (D > 0) {
    			y = y + yi;
    			D = D - 2 * dx;
    		}
    		D = D + 2*dy;
    	}    	
    }
    
    /**
     * Draw line in vertical 
     * @param x0 of Point P0
     * @param y0 of Point P0
     * @param x1 of Point P1
     * @param y1 of Point P1
     * @param red value of red color
     * @param green value of green color
     * @param blue value of green color
     */
    public void plotLineHigh(int x0, int y0, int x1, int y1, 
    							int red, int green, int blue) {
    	int dx = x1 - x0;
    	int dy = y1 - y0;
    	int xi = 1;
	    if (dx < 0) {
	    	xi = -1;
	        dx = -dx;
	    }
	    int D = 2*dx - dy;
	    int x = x0;
	    for (int y = y0; y < y1; y++) {
	    	drawPixel(x, y, red, green, blue);
	        if (D > 0) {
	        	x = x + xi;
	        	D = D - 2*dy;
	        }
	        D = D + 2*dx;
	    }    		        
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
    public void plotLine(int x0, int y0, int x1, int y1, int red, int green, int blue ) {
    	if (Math.abs(y1 - y0) < Math.abs(x1 - x0)) {
    		if (x0 > x1) {
    			plotLineLow(x1, y1, x0, y0, red, green, blue);
    		} else {
    			plotLineLow(x0, y0, x1, y1, red, green, blue);
    		}            
    	} else {
    		if (y0 > y1) {
    			plotLineHigh(x1, y1, x0, y0, red, green, blue);
    		}else {
    			plotLineHigh(x0, y0, x1, y1, red, green, blue);
    		}
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
    	Matrix tranformationMatrix = new Transformation().LoadIdentityMatrix();    	
    	
        Scanner input = getFile();
        while (input.hasNext()) {
            String command = input.next();
            if (command.equals("DIM")){
                width = input.nextInt();
                height = input.nextInt();
                imageSize = width * height;
                pixels = new int[imageSize * 3]; 
                
                //Set white background 
                for (int i = 0; i < width; i++) {
                	for (int j = 0; j < height; j++) {
                		drawPixel( i, j, 255, 255, 255);
                	}                	
                }
            }  else if (command.equals("RGB")) {
                r = (int) (input.nextDouble() * 255);
                g = (int) (input.nextDouble() * 255);
                b = (int) (input.nextDouble() * 255);
            } else if (command.equals("LOAD_IDENTITY_MATRIX")) {
            	tranformationMatrix = new Transformation().LoadIdentityMatrix();
            } else if (command.equals("SCALE")) {
            	tranformationMatrix = new Transformation().Scaling(input.nextDouble(), 
            							input.nextDouble(),	input.nextDouble())
            								.multiply(tranformationMatrix);
            } else if (command.equals("ROTATEZ")) {
            	tranformationMatrix = new Transformation().RotationZ(input.nextDouble())
            								.multiply(tranformationMatrix);
            } else if (command.equals("ROTATEY")) {
            	tranformationMatrix = new Transformation().RotationY(input.nextDouble())
											.multiply(tranformationMatrix);
            } else if (command.equals("ROTATEX")) {
            	tranformationMatrix = new Transformation().RotationX(input.nextDouble())
											.multiply(tranformationMatrix);
            } else if (command.equals("TRANSLATE")) {
            	tranformationMatrix = new Transformation().Translation(input.nextDouble(), 
            							input.nextDouble(), input.nextDouble())
            								.multiply(tranformationMatrix);
            }  else if (command.equals("TRI")) {
            	Point3D p0 = new Point3D(input.nextDouble(), 
            								input.nextDouble(), 
            									input.nextDouble(), 1);
            	Point3D p1 = new Point3D(input.nextDouble(), 
            								input.nextDouble(), 
            									input.nextDouble(), 1);
            	Point3D p2 = new Point3D(input.nextDouble(), 
            								input.nextDouble(), 
            									input.nextDouble(), 1);
            	
            	//TransformedVector = TranslationMatrix * RotationMatrix * ScaleMatrix * OriginalVector;
            	tranformationMatrix = tranformationMatrix.multiply(
            							new Transformation().paralleProjection());
            	
            	Matrix matrix0 = new Matrix(4,1);
            	matrix0 = tranformationMatrix.multiply(p0.convertFromPoint3D());
            	
            	Matrix matrix1 = new Matrix(4,1);
            	matrix1 = tranformationMatrix.multiply(p1.convertFromPoint3D());
            	
            	Matrix matrix2 = new Matrix(4,1);
            	matrix2 = tranformationMatrix.multiply(p2.convertFromPoint3D());
            	
            	double x0 = convertToScreen(width,matrix0.getMatrix()[0][0]);
            	double y0 = convertToScreen(height, matrix0.getMatrix()[1][0]);
            	
            	double x1 = convertToScreen(width,matrix1.getMatrix()[0][0]);
            	double y1 = convertToScreen(height, matrix1.getMatrix()[1][0]);
            	
            	double x2 = convertToScreen(width,matrix2.getMatrix()[0][0]);
            	double y2 = convertToScreen(height, matrix2.getMatrix()[1][0]);
            	                
                //Step 1: Draw the edges of a triangle first.
                plotLine((int)Math.round(x0), (int)Math.round(y0), 
                		(int)Math.round(x1), (int)Math.round(y1), r, g, b);
                plotLine((int)Math.round(x1), (int)Math.round(y1), 
                		(int)Math.round(x2), (int)Math.round(y2), r, g, b);
                plotLine((int)Math.round(x2), (int)Math.round(y2), 
                		(int)Math.round(x0), (int)Math.round(y0), r, g, b);
                
                //Step 2: Use scanline algorithm to fill the triangle.
                //Add vertexes to ArrayList to array them in x order.
                ArrayList<Point2D> points = new ArrayList<Point2D>(); 
                points.add(new Point2D(x0, y0));
                points.add(new Point2D(x1, y1));
                points.add(new Point2D(x2, y2));  
                
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
     * Compute the slope of vertex v0 and Vertex v1
     * @param v0
     * @param v1
     * @return
     */
    private double slope(Point2D v0, Point2D v1) {
    	return (v0.getY() - v1.getY())/(v0.getX() - v1.getX());
    }
    
    /**
     * fill a triangle with 3 vertexes
     * @param vertexes v0, v1, v2
     * @param minY 
     * @param maxY
     * @param y
     * @return an array minx, maxx
     */
	private void fillTriangle(ArrayList<Point2D> points, int r, int g, int b) {		
		//Sort vertices by Y
		sortByY(points);
		
		Point2D p0 = new Point2D();				
		Point2D p1 = new Point2D();				
		Point2D p2 = new Point2D();
		
		p0 = points.get(0);
		p1 = points.get(1);
		p2 = points.get(2);
    	
    	double minY = p0.getY();
    	double maxY = p2.getY();
    	double minX = 0;
    	double maxX = 0;
    	
    	for (double y = minY; y < maxY; y++) {
    		if (y < points.get(1).getY()) {
    			minX = p0.getX() + ((y - p0.getY()) / slope(p0, p1));
    			maxX = p0.getX() + ((y - p0.getY()) / slope(p0, p2));
    		} else {
    			minX = p2.getX() + ((y - p2.getY()) / slope(p2, p1));
    			maxX = p2.getX() + ((y - p2.getY()) / slope(p2, p0));
    		}
    		
    		double temp = 0;
        	if (maxX < minX) {
        		temp = minX;
        		minX = maxX;
        		maxX = temp;
        	}
        	
        	for (double x = minX; x < maxX; x++) {
        		drawPixel((int)Math.round(x), (int)Math.round(y), r, g, b);
        	}
    	}
    }
	
	/**
	 * Sorts vertexes by y.
	 * @param vertexes
	 */
	public void sortByY(ArrayList<Point2D> points) {
		Point2D point = new Point2D();
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

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
