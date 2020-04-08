import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

/**
 * @author pham19
 * Implements drawing shapes homework 1
 *
 */
public class TCSS458Paint extends JPanel
{
	private static final long serialVersionUID = -6972067082022136019L;
	static int width;
    static int height;
    int imageSize;
    int[] pixels;        

    /**
     * Draws pixels in monitor
     * @param x coordinate x
     * @param y coordinate y
     * @param r value of red color
     * @param g value of green color
     * @param b value of blue color
     */
    void drawPixel(int x, int y, int r, int g, int b) {
        pixels[(height-y-1)*width*3+x*3] = r;
        pixels[(height-y-1)*width*3+x*3+1] = g;
        pixels[(height-y-1)*width*3+x*3+2] = b;                
    }
    
    
    /**
     * @param x0 coordinate x of Point P1
     * @param y0 coordinate y of Point P1
     * @param x1 coordinate x of Point P2
     * @param y1 coordinate y of Point P2
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
     * @param x0 coordinate x of Point P1
     * @param y0 coordinate y of Point P1
     * @param x1 coordinate x of Point P2
     * @param y1 coordinate y of Point P2
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
     * Reference https://en.wikipedia.org/wiki/Bresenham%27s_line_algorithm
     * @param x0 coordinate x of Point P1
     * @param y0 coordinate y of Point P1
     * @param x1 coordinate x of Point P2
     * @param y1 coordinate y of Point P2
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
     * Based on LINE or TRI to draw line or triangle 
     * Draws line using Bresenham's algorithm
     * Draws triangle 
     * 	+ Draws edges
     *  + Fill triangle
     */
    void createImage() {
    	double x0_world, y0_world, x1_world, y1_world, x2_world, y2_world;
    	double x0_screen, y0_screen, x1_screen, y1_screen, x2_screen, y2_screen;
    	int r, g, b;
    	
    	x0_world = y0_world = x1_world = y1_world = x2_world = y2_world = 0;
    	x0_screen = y0_screen = x1_screen = y1_screen = x2_screen = y2_screen = 0;
    	r = g = b = 0;    	
    	
        Scanner input = getFile();
        while (input.hasNext()) {
            String command = input.next();
            if (command.equals("DIM")){
                width = input.nextInt();
                height = input.nextInt();
                imageSize = width * height;
                pixels = new int[imageSize * 3];
                
                //Set background color 
                for (int i = 0; i < width; i++) {
                	for (int j = 0; j < height; j++) {
                		drawPixel( i, j, 255, 255, 255);
                	}                	
                }
            } else if (command.equals("LINE")){
            	//Point P1
            	x0_world = input.nextDouble();
            	y0_world = input.nextDouble();
            	
            	//Point P2
            	x1_world = input.nextDouble();
            	y1_world = input.nextDouble();
            	
            	//Convert P1 in read world to screen
            	x0_screen = (width - 1) * (x0_world + 1)/2;
                y0_screen = (height - 1) * (y0_world + 1)/2;                
                
                //Convert P2 in read world to screen
                x1_screen = (width - 1) * (x1_world + 1)/2;
                y1_screen = (height - 1) * (y1_world + 1)/2; 
                
                //Drawing a line with Bresenham's algorithm
                plotLine((int)x0_screen, (int)y0_screen, (int)x1_screen, (int)y1_screen, r, g, b);
            } else if (command.equals("RGB")) {
                r = (int) (input.nextDouble() * 255);
                g = (int) (input.nextDouble() * 255);
                b = (int) (input.nextDouble() * 255);
            } else if (command.equals("TRI")) {
            	//Vertex V1
            	x0_world = input.nextDouble();
            	y0_world = input.nextDouble();            	
            	
            	//Vertex V2
            	x1_world = input.nextDouble();
            	y1_world = input.nextDouble();
            	
            	//Vertex V3
            	x2_world = input.nextDouble();
            	y2_world = input.nextDouble();
            	
            	//Convert V1 in real world to screen
            	x0_screen = (width - 1) * (x0_world + 1)/2;
                y0_screen = (height - 1) * (y0_world + 1)/2; 
                
                //Convert V2 in real world to screen
                x1_screen = (width - 1) * (x1_world + 1)/2;
                y1_screen = (height - 1) * (y1_world + 1)/2; 
                
                //Convert V3 in real world to screen
                x2_screen = (width - 1) * (x2_world + 1)/2; 
                y2_screen = (height - 1) * (y2_world + 1)/2; 
                
                //Drawing a line with Bresenham's algorithm
                plotLine((int)x0_screen, (int)y0_screen, (int)x1_screen, (int)y1_screen, r, g, b);
                plotLine((int)x1_screen, (int)y1_screen, (int)x2_screen, (int)y2_screen, r, g, b);
                plotLine((int)x2_screen, (int)y2_screen, (int)x0_screen, (int)y0_screen, r, g, b);
                
                ArrayList<Vertex> vertexes = new ArrayList<Vertex>(); 
                vertexes.add(new Vertex(x0_screen, y0_screen));
                vertexes.add(new Vertex(x1_screen, y1_screen));
                vertexes.add(new Vertex(x2_screen, y2_screen));
                
                //Sorts vertexes based on y coordinate
                Collections.sort(vertexes);
                
                ArrayList<Edge> edges = new ArrayList<Edge>(); 
                edges.add(new Edge(vertexes.get(1), vertexes.get(0)));
                edges.add(new Edge(vertexes.get(1), vertexes.get(2)));
                edges.add(new Edge(vertexes.get(0), vertexes.get(2)));
                
                
                ArrayList<Double> coorY = new ArrayList<Double>();
                coorY.add(y0_screen);
                coorY.add(y1_screen);
                coorY.add(y2_screen);
                
                //Sorts coordinate y
                Collections.sort(coorY);
                
                double minY = coorY.get(0);
                double maxY = coorY.get(2);
                
                for (double y = minY; y < maxY; y++) {
                	double minX = 0;
                	double maxX = 0;                	
                	// V0 and V1 are in a horizontal line
                	if (edges.get(0).getSlope() == 0) {
                		
                		// V1 and V2 are in a vertical line
                		if (Double.isInfinite( edges.get(1).getSlope())) {
                			minX = vertexes.get(0).getX() + ((y - vertexes.get(0).getY()) / edges.get(2).getSlope());
                			maxX = vertexes.get(2).getX();                			
                		} else { // There are 2 cases y2 < y1 or y2 > y1
                			minX = vertexes.get(0).getX() + ((y - vertexes.get(0).getY()) / edges.get(2).getSlope());
                			maxX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(1).getSlope());
                		}                		
                		for (double x = minX; x < maxX; x++) {
                    		drawPixel((int)x, (int)y, r, g, b);
                    	}
                	} else if (Double.isInfinite(edges.get(0).getSlope())) {   
                		if (vertexes.get(0).getY() < vertexes.get(1).getY()) {
                			//Truong hop tam giac la tam giac co chua goc tu                			
                			if(vertexes.get(2).getY() <= maxY && vertexes.get(1).getY() < vertexes.get(2).getY()) {
                				//Chia lam 2 de ve
                				if (y <= vertexes.get(1).getY()) {
                					minX = vertexes.get(0).getX();
                					maxX = vertexes.get(0).getX() + ((y - vertexes.get(0).getY()) / edges.get(2).getSlope());
                				} else {
                					minX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(1).getSlope());
                					maxX = vertexes.get(0).getX() + ((y - vertexes.get(0).getY()) / edges.get(2).getSlope());
                				}
                			} else if (vertexes.get(2).getY() >= minY && vertexes.get(1).getY() > vertexes.get(2).getY()) { //Truong hop tam giac chua toan goc nhon
                				minX = vertexes.get(0).getX();
                				maxX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(1).getSlope());
                			} else if (edges.get(1).getSlope() == 0) {
                    			minX = vertexes.get(0).getX();
                    			maxX = vertexes.get(0).getX() + ((y - vertexes.get(0).getY()) / edges.get(2).getSlope());               			
                    		} 
                		} else if (vertexes.get(0).getY() > vertexes.get(1).getY()) {
                			//Truong hop tam giac la tam giac co chua goc tu                			
                			if(vertexes.get(2).getY() <= maxY && vertexes.get(0).getY() < vertexes.get(2).getY()) {
                				//Chia lam 2 de ve
                				if (y <= vertexes.get(0).getY()) {
                					minX = vertexes.get(1).getX();
                					maxX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(1).getSlope());
                				} else {
                					minX = vertexes.get(0).getX() + ((y - vertexes.get(0).getY()) / edges.get(2).getSlope());
                					maxX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(1).getSlope());
                				}
                			} else if (vertexes.get(2).getY() >= minY && vertexes.get(0).getY() > vertexes.get(2).getY()) { //Truong hop tam giac chua toan goc nhon
                				minX = vertexes.get(1).getX();
                				maxX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(1).getSlope());
                			} else if (edges.get(2).getSlope() == 0) {
                    			minX = vertexes.get(0).getX();
                    			maxX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(1).getSlope());               			
                    		} 
                		} 
                		for (double x = minX; x < maxX; x++) {
                    		drawPixel((int)x, (int)y, r, g, b);
                    	}
                	} else {
                		// Vy0 < Vy2 < Vy1
                		if ((vertexes.get(0).getY() < vertexes.get(2).getY()) && (vertexes.get(2).getY() < vertexes.get(1).getY())) {
                			if (y < vertexes.get(2).getY()) {
                				minX = vertexes.get(0).getX() + ((y - vertexes.get(0).getY()) / edges.get(0).getSlope());
                            	maxX = vertexes.get(0).getX() + ((y - vertexes.get(0).getY()) / edges.get(2).getSlope());
                			} else {
                				minX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(0).getSlope());
                            	maxX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(1).getSlope());
                			}                			
                		} else if ((vertexes.get(0).getY() < vertexes.get(1).getY()) && (vertexes.get(1).getY() < vertexes.get(2).getY())) { // Vy0 < vy1 < vy2
                			if (y < vertexes.get(1).getY()) {
                				minX = vertexes.get(0).getX() + ((y - vertexes.get(0).getY()) / edges.get(0).getSlope());
                            	maxX = vertexes.get(0).getX() + ((y - vertexes.get(0).getY()) / edges.get(2).getSlope());
                			} else {
                				minX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(0).getSlope());
                            	maxX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(1).getSlope());
                			}                 			
                		} else if ((vertexes.get(2).getY() < vertexes.get(0).getY()) && (vertexes.get(0).getY() < vertexes.get(1).getY())) { // Vy2 < Vy0 < Vy1
                			if (y < vertexes.get(0).getY()) {
                				minX = vertexes.get(2).getX() + ((y - vertexes.get(2).getY()) / edges.get(2).getSlope());
                            	maxX = vertexes.get(2).getX() + ((y - vertexes.get(2).getY()) / edges.get(1).getSlope());
                			} else {
                				minX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(0).getSlope());
                            	maxX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(1).getSlope());
                			}                			
                		} else if ((vertexes.get(1).getY() < vertexes.get(0).getY()) && (vertexes.get(0).getY() < vertexes.get(2).getY())) { // Vy1 < Vy0 > Vy2
                			if (y < vertexes.get(0).getY()) {
                				minX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(0).getSlope());
                            	maxX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(1).getSlope());
                			} else {
                				minX = vertexes.get(2).getX() + ((y - vertexes.get(2).getY()) / edges.get(2).getSlope());
                            	maxX = vertexes.get(2).getX() + ((y - vertexes.get(2).getY()) / edges.get(1).getSlope());
                			}                 			
                		} else if ((vertexes.get(2).getY() < vertexes.get(1).getY()) && (vertexes.get(1).getY() < vertexes.get(0).getY())) { // Vy2 < Vy1 < Vy0
                			if (y < vertexes.get(1).getY()) {
                				minX = vertexes.get(2).getX() + ((y - vertexes.get(2).getY()) / edges.get(2).getSlope());
                            	maxX = vertexes.get(2).getX() + ((y - vertexes.get(2).getY()) / edges.get(1).getSlope());
                			} else {
                				minX = vertexes.get(0).getX() + ((y - vertexes.get(0).getY()) / edges.get(2).getSlope());
                            	maxX = vertexes.get(0).getX() + ((y - vertexes.get(0).getY()) / edges.get(0).getSlope());
                			}                			
                		} else if((vertexes.get(1).getY() < vertexes.get(2).getY()) && (vertexes.get(2).getY() < vertexes.get(0).getY())) { // Vy1< Vy2 < Vy0
                			if (y < vertexes.get(2).getY()) {
                				minX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(0).getSlope());
                            	maxX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(1).getSlope());
                			} else {
                				minX = vertexes.get(0).getX() + ((y - vertexes.get(0).getY()) / edges.get(0).getSlope());
                            	maxX = vertexes.get(0).getX() + ((y - vertexes.get(0).getY()) / edges.get(2).getSlope());
                			}  
                			
                		}
                    	for (double x = minX; x < maxX; x++) {
                    		drawPixel((int)x, (int)y, r, g, b);
                    	}
                	}
                }

            }
        }
    }
    
    
    
	/**
	 * 
	 */
    public void paintComponent(Graphics g) {
        createImage();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        WritableRaster wr_raster = image.getRaster();
        wr_raster.setPixels(0, 0, width, height, pixels);        
        g.drawImage(image, 0, 0, null);
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

    static File selectedFile = null;

    /**
     * 
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

    /**
     * 
     * @return
     */
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
     * 
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
}
