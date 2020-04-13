import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
/**
 * Implements drawing shapes homework 1
 * @author pham19@uw.edu
 * Homework 1: Lines and Triangles
 * Due: April 10, 2020
 */
public class TCSS458Paint extends JPanel
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
    public void plotLineLow(int x0, int y0, int x1, int y1, int red, int green, int blue) {
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
    public void plotLineHigh(int x0, int y0, int x1, int y1, int red, int green, int blue) {
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
    	//The converted coordinates
    	double x0_screen, y0_screen, x1_screen, y1_screen, x2_screen, y2_screen;
    	x0_screen = y0_screen = x1_screen = y1_screen = x2_screen = y2_screen = 0;
    	
    	// RGB color
    	int r, g, b;
    	r = g = b = 0;    	
    	
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
            } else if (command.equals("RGB")) {
                r = (int) (input.nextDouble() * 255);
                g = (int) (input.nextDouble() * 255);
                b = (int) (input.nextDouble() * 255);
            }else if (command.equals("LINE")){
            	//Convert P0 in read world to screen
            	x0_screen = convertToScreen(width, input.nextDouble());
                y0_screen = convertToScreen(height, input.nextDouble());
            	
                //Convert P1 in read world to screen
                x1_screen = convertToScreen(width, input.nextDouble());
                y1_screen = convertToScreen(height, input.nextDouble());
                
                //Draws a line with Bresenham's algorithm
                plotLine((int)x0_screen, (int)y0_screen, (int)x1_screen, (int)y1_screen, r, g, b);
            }  else if (command.equals("TRI")) {
            	//Convert P0 in read world to screen
            	x0_screen = convertToScreen(width, input.nextDouble());
                y0_screen = convertToScreen(height, input.nextDouble());
            	
                //Convert P1 in read world to screen
                x1_screen = convertToScreen(width, input.nextDouble());
                y1_screen = convertToScreen(height, input.nextDouble()); 
                
                //Convert P1 in read world to screen
                x2_screen = convertToScreen(width, input.nextDouble());
                y2_screen = convertToScreen(height, input.nextDouble()); 
                
                //Step 1: Draw the edges of a triangle first.
                plotLine((int)x0_screen, (int)y0_screen, (int)x1_screen, (int)y1_screen, r, g, b);
                plotLine((int)x1_screen, (int)y1_screen, (int)x2_screen, (int)y2_screen, r, g, b);
                plotLine((int)x2_screen, (int)y2_screen, (int)x0_screen, (int)y0_screen, r, g, b);
                
                //Step 2: Use scanline algorithm to fill the triangle.
                //Add vertexes to ArrayList to array them in x order.
                ArrayList<Vertex> vertexes = new ArrayList<Vertex>(); 
                vertexes.add(new Vertex(x0_screen, y0_screen));
                vertexes.add(new Vertex(x1_screen, y1_screen));
                vertexes.add(new Vertex(x2_screen, y2_screen));  
                
                //Sorts vertexes based on x
                Collections.sort(vertexes);
               
                //Find the MinY and MaxY
                ArrayList<Double> coorY = new ArrayList<Double>();
                coorY.add(y0_screen);
                coorY.add(y1_screen);
                coorY.add(y2_screen);
                
                //Sorts coordinate y
                Collections.sort(coorY);
                
                //Get MinY and MaxY
                double minY = coorY.get(0);
                double maxY = coorY.get(2);
            	//Draw lines from MinY to MaxY
                for (double y = minY; y < maxY; y++) {
                	double[] arrX = fillTriangle(vertexes, minY, maxY , y);
                	double minX = arrX[0];
                	double maxX = arrX[1];
                	
                	double temp = 0;
                	if (maxX < minX) {
                		temp = minX;
                		minX = maxX;
                		maxX = temp;
                	}
                	
                	for (double x = minX; x < maxX; x++) {
                		drawPixel((int)x, (int)y, r, g, b);
                	}
                }
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
    private double slope(Vertex v0, Vertex v1) {
    	return (v0.getY() - v1.getY())/(v0.getX() - v1.getX());
    }
    
    /**
     * Find minX maxX 
     * @param vertexes v0, v1, v2
     * @param minY 
     * @param maxY
     * @param y
     * @return an array minx, maxx
     */
	private double[] fillTriangle(ArrayList<Vertex> vertexes, double minY, double maxY, double y) {
		double[] arrX = new double[2];
    	Vertex v0 = new Vertex();
    	Vertex v1 = new Vertex();
    	Vertex v2 = new Vertex();
    	int flag1 = 0;
    	int flag2 = 0;
    	
    	//Sort v0, v1, v2 
    	for (Vertex vertex : vertexes) {
    		if (vertex.getY() == minY 
    				&& flag1 == 0) {
    			v0 = vertex;
    			flag1 = 1;
    		} else if (vertex.getY() == maxY 
    				&& flag2 == 0) {
    			v1 = vertex;
    			flag2 = 1;
    		} else {
    			v2 = vertex;
    		}
    	}
    	
    	if (y < v2.getY()) {
			arrX[0] = v0.getX() + ((y - v0.getY()) / slope(v0, v1));
			arrX[1] = v0.getX() + ((y - v0.getY()) / slope(v0, v2));
		} else {
			arrX[0] = v1.getX() + ((y - v1.getY()) / slope(v1, v0));
			arrX[1] = v1.getX() + ((y - v1.getY()) / slope(v1, v2));
		}
		return arrX;
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

    public class Vertex implements Comparable<Vertex> {
    	private double x;
    	private double y;    	
    	
    	/**
    	 * Constructor of Vertex class
    	 * @param x coordinate x
    	 * @param y coordinate y
    	 */
    	public Vertex(double x, double y) {
    		this.x = x;
    		this.y = y;
    	}
    	
    	public Vertex() {
    		this(0,0);
    	}	

    	/**
    	 * @return x
    	 */
    	public double getX() {
    		return x;
    	}

    	/**
    	 * @return y
    	 */
    	public double getY() {
    		return y;
    	}

    	@Override
    	public int compareTo(Vertex o) {
    		return (int)(this.x - o.x);
    	}

    	@Override
    	public String toString() {
    		return "Vertex [x=" + x + ", y=" + y + "]";
    	}
    }
    
}
