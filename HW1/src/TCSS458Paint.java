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
     * - Draw a line
     * 		+ We need two points to draw a line (Using Bresenham's algorithm to draw a line)
     * - Draw a triangle
     * 		+ We need three points to draw a triangle.
     * 		+ Step 1: Draw edges of a triangle
     * 		+ Step 2: For y from MinY to MaxY. Use scanline algorithm to fill the triangle 			
     * 			- Situation 1: Horizontal lines and vertical lines
     * 			- Situation 2: Draw from MinX to MaxX
     */
    void createImage() {
    	//The coordinates from the input file
    	double x0_world, y0_world, x1_world, y1_world, x2_world, y2_world;
    	x0_world = y0_world = x1_world = y1_world = x2_world = y2_world = 0;
    	
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
            	//Point P0
            	x0_world = input.nextDouble();
            	y0_world = input.nextDouble();       
            	
            	//Convert P0 in read world to screen
            	x0_screen = (width - 1) * (x0_world + 1)/2;
                y0_screen = (height - 1) * (y0_world + 1)/2;  
            	
            	//Point P1
            	x1_world = input.nextDouble();
            	y1_world = input.nextDouble();
            	
                //Convert P1 in read world to screen
                x1_screen = (width - 1) * (x1_world + 1)/2;
                y1_screen = (height - 1) * (y1_world + 1)/2;
                
                //Draws a line with Bresenham's algorithm
                plotLine((int)x0_screen, (int)y0_screen, (int)x1_screen, (int)y1_screen, r, g, b);
            }  else if (command.equals("TRI")) {
            	//Vertex V0
            	x0_world = input.nextDouble();
            	y0_world = input.nextDouble();
            	
            	//Convert V1 in real world to screen
            	x0_screen = (width - 1) * (x0_world + 1)/2;
                y0_screen = (height - 1) * (y0_world + 1)/2;
            	
            	//Vertex V1
            	x1_world = input.nextDouble();
            	y1_world = input.nextDouble();
            	
            	//Convert V1 in real world to screen
                x1_screen = (width - 1) * (x1_world + 1)/2;
                y1_screen = (height - 1) * (y1_world + 1)/2;
            	
            	//Vertex V2
            	x2_world = input.nextDouble();
            	y2_world = input.nextDouble();
            	
                //Convert V1 in real world to screen
                x2_screen = (width - 1) * (x2_world + 1)/2; 
                y2_screen = (height - 1) * (y2_world + 1)/2; 
                
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
                
                //Edge data are the information of P0 and P1 and its slope.
                ArrayList<Edge> edges = new ArrayList<Edge>(); 
                edges.add(new Edge(vertexes.get(1), vertexes.get(0)));
                edges.add(new Edge(vertexes.get(1), vertexes.get(2)));
                edges.add(new Edge(vertexes.get(0), vertexes.get(2)));
                                
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
                double minX = 0;
            	double maxX = 0;
                
            	//Draw lines from MinY to MaxY
                for (double y = minY; y < maxY; y++) {        
            		if ((vertexes.get(0).getY() <= vertexes.get(2).getY()) && (vertexes.get(2).getY() <= vertexes.get(1).getY())) { // Vy0 <= Vy2 <= Vy1
            			if (y < vertexes.get(2).getY()) {
            				minX = vertexes.get(0).getX() + ((y - vertexes.get(0).getY()) / edges.get(0).getSlope());
                        	maxX = vertexes.get(0).getX() + ((y - vertexes.get(0).getY()) / edges.get(2).getSlope());
            			} else {
            				minX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(0).getSlope());
                        	maxX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(1).getSlope());
            			}                			
            		} else if ((vertexes.get(0).getY() <= vertexes.get(1).getY()) && (vertexes.get(1).getY() <= vertexes.get(2).getY())) { // Vy0 <= vy1 <= vy2
            			if (y < vertexes.get(1).getY()) {
            				minX = vertexes.get(0).getX() + ((y - vertexes.get(0).getY()) / edges.get(0).getSlope());
                        	maxX = vertexes.get(0).getX() + ((y - vertexes.get(0).getY()) / edges.get(2).getSlope());
            			} else {
            				minX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(0).getSlope());
                        	maxX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(1).getSlope());
            			}                 			
            		} else if ((vertexes.get(2).getY() <= vertexes.get(0).getY()) && (vertexes.get(0).getY() <= vertexes.get(1).getY())) { // Vy2 <= Vy0 <= Vy1
            			if (y < vertexes.get(0).getY()) {
            				minX = vertexes.get(2).getX() + ((y - vertexes.get(2).getY()) / edges.get(2).getSlope());
                        	maxX = vertexes.get(2).getX() + ((y - vertexes.get(2).getY()) / edges.get(1).getSlope());
            			} else {
            				minX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(0).getSlope());
                        	maxX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(1).getSlope());
            			}                			
            		} else if ((vertexes.get(1).getY() <= vertexes.get(0).getY()) && (vertexes.get(0).getY() <= vertexes.get(2).getY())) { // Vy1 <= Vy0 <= Vy2
            			if (y < vertexes.get(0).getY()) {
            				minX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(0).getSlope());
                        	maxX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(1).getSlope());
            			} else {
            				minX = vertexes.get(2).getX() + ((y - vertexes.get(2).getY()) / edges.get(2).getSlope());
                        	maxX = vertexes.get(2).getX() + ((y - vertexes.get(2).getY()) / edges.get(1).getSlope());
            			}                 			
            		} else if ((vertexes.get(2).getY() <= vertexes.get(1).getY()) && (vertexes.get(1).getY() <= vertexes.get(0).getY())) { // Vy2 <= Vy1 <= Vy0
            			if (y < vertexes.get(1).getY()) {
            				minX = vertexes.get(2).getX() + ((y - vertexes.get(2).getY()) / edges.get(2).getSlope());
                        	maxX = vertexes.get(2).getX() + ((y - vertexes.get(2).getY()) / edges.get(1).getSlope());
            			} else {
            				minX = vertexes.get(0).getX() + ((y - vertexes.get(0).getY()) / edges.get(2).getSlope());
                        	maxX = vertexes.get(0).getX() + ((y - vertexes.get(0).getY()) / edges.get(0).getSlope());
            			}                			
            		} else if((vertexes.get(1).getY() <= vertexes.get(2).getY()) && (vertexes.get(2).getY() <= vertexes.get(0).getY())) { // Vy1 <= Vy2 <= Vy0
            			if (y < vertexes.get(2).getY()) {
            				minX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(0).getSlope());
                        	maxX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(1).getSlope());
            			} else {
            				minX = vertexes.get(0).getX() + ((y - vertexes.get(0).getY()) / edges.get(0).getSlope());
                        	maxX = vertexes.get(0).getX() + ((y - vertexes.get(0).getY()) / edges.get(2).getSlope());
            			}  
            		}
                	
            		// Check if slope is zero
                	if (edges.get(0).getSlope() == 0) {
                		minX =  vertexes.get(0).getX() + ((y - vertexes.get(0).getY()) / edges.get(2).getSlope());
                	} else if (edges.get(1).getSlope() == 0){
                		maxX = vertexes.get(2).getX() + ((y - vertexes.get(2).getY()) / edges.get(2).getSlope());
                	} 
                	for (double x = minX; x < maxX; x++) {
                		drawPixel((int)x, (int)y, r, g, b);
                	}
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
