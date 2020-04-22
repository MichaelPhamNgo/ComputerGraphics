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
     * Reference at https://en.wikipedia.org/wiki/Bresenham%27s_line_algorithm
     * @param x0 of Point P0
     * @param y0 of Point P0
     * @param x1 of Point P1
     * @param y1 of Point P1
     * @param red value of red color
     * @param green value of green color
     * @param blue value of green color
     */
    public void plotLine(double x0, double y0, double x1, double y1, int red, int green, int blue ) {
    	// calculate dx & dy 
    	double dx = x1 - x0; 
    	double dy = y1 - y0; 
      
        // calculate steps required for generating pixels 
        double steps = Math.abs(dx) >  Math.abs(dy) ?  Math.abs(dx) :  Math.abs(dy); 
      
        // calculate increment in x & y for each steps 
        double Xinc = dx / (double) steps; 
        double Yinc = dy / (double) steps; 
      
        // Put pixel for each step 
        double X = x0; 
        double Y = y0; 
        for (double i = 0; i <= steps; i++) 
        { 
        	drawPixel((int)Math.round(X), (int)Math.round(Y), red, green, blue); 
            X += Xinc;           // increment in x at each step 
            Y += Yinc;           // increment in y at each step 
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
            } else if (command.equals("LINE")){
            	//Convert P0 in read world to screen
            	x0_screen = convertToScreen(width, input.nextDouble());
                y0_screen = convertToScreen(height, input.nextDouble());
            	
                //Convert P1 in read world to screen
                x1_screen = convertToScreen(width, input.nextDouble());
                y1_screen = convertToScreen(height, input.nextDouble());
                
                //Draws a line with Bresenham's algorithm
                plotLine(x0_screen, y0_screen, 
                		x1_screen,y1_screen, r, g, b);
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
                plotLine(x0_screen, y0_screen, 
                		x1_screen, y1_screen, r, g, b);
                plotLine(x1_screen, y1_screen, 
                		x2_screen, y2_screen, r, g, b);
                plotLine(x2_screen, y2_screen, 
                		x0_screen, y0_screen, r, g, b);
                
                //Step 2: Use scanline algorithm to fill the triangle.
                //Add vertexes to ArrayList to array them in x order.
                ArrayList<Vertex> vertexes = new ArrayList<Vertex>(); 
                vertexes.add(new Vertex(x0_screen, y0_screen));
                vertexes.add(new Vertex(x1_screen, y1_screen));
                vertexes.add(new Vertex(x2_screen, y2_screen));  
                
                //Sorts vertexes based on x
                Collections.sort(vertexes);
                
                //Draw a triangle
                fillTriangle(vertexes, r, g, b);

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
     * fill a triangle with 3 vertexes
     * @param vertexes v0, v1, v2
     * @param minY 
     * @param maxY
     * @param y
     * @return an array minx, maxx
     */
	private void fillTriangle(ArrayList<Vertex> vertexes, int r, int g, int b) {		
		//Sort vertexes by Y
		sortByY(vertexes);
		
		Vertex v0 = new Vertex();				
		Vertex v1 = new Vertex();				
		Vertex v2 = new Vertex();
		
		v0 = vertexes.get(0);
		v1 = vertexes.get(1);
		v2 = vertexes.get(2);
    	
    	double minY = v0.getY();
    	double maxY = v2.getY();
    	double minX = 0;
    	double maxX = 0;
    	
    	for (double y = minY; y < maxY; y++) {
    		if (y < vertexes.get(1).getY()) {
    			minX = v0.getX() + ((y - v0.getY()) / slope(v0, v1));
    			maxX = v0.getX() + ((y - v0.getY()) / slope(v0, v2));
    		} else {
    			minX = v2.getX() + ((y - v2.getY()) / slope(v2, v1));
    			maxX = v2.getX() + ((y - v2.getY()) / slope(v2, v0));
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
	public void sortByY(ArrayList<Vertex> vertexes) {
		Vertex vtemp = new Vertex();
		for (int i = 0; i < vertexes.size() - 1; i++) {
			for(int j = i + 1; j < vertexes.size(); j++) {
				if (vertexes.get(i).getY() > vertexes.get(j).getY()) {
					vtemp = vertexes.get(i);
					vertexes.set(i, vertexes.get(j));
					vertexes.set(j, vtemp);
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
