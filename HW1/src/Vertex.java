import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * I want to arrange vertexes of a triangle with x coordinate first. 
 * @author pham19@uw.edu
 * Homework 1: Lines and Triangles
 * Due: April 10, 2020
 */
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


//void createImage() {
//	//The coordinates from the input file
//	double x0_world, y0_world, x1_world, y1_world, x2_world, y2_world;
//	x0_world = y0_world = x1_world = y1_world = x2_world = y2_world = 0;
//	
//	//The converted coordinates
//	double x0_screen, y0_screen, x1_screen, y1_screen, x2_screen, y2_screen;
//	x0_screen = y0_screen = x1_screen = y1_screen = x2_screen = y2_screen = 0;
//	
//	// RGB color
//	int r, g, b;
//	r = g = b = 0;    	
//	
//    Scanner input = getFile();
//    while (input.hasNext()) {
//        String command = input.next();
//        if (command.equals("DIM")){
//            width = input.nextInt();
//            height = input.nextInt();
//            imageSize = width * height;
//            pixels = new int[imageSize * 3]; 
//            
//            //Set white background 
//            for (int i = 0; i < width; i++) {
//            	for (int j = 0; j < height; j++) {
//            		drawPixel( i, j, 255, 255, 255);
//            	}                	
//            }
//        } else if (command.equals("RGB")) {
//            r = (int) (input.nextDouble() * 255);
//            g = (int) (input.nextDouble() * 255);
//            b = (int) (input.nextDouble() * 255);
//        }else if (command.equals("LINE")){
//        	//Point P0
//        	x0_world = input.nextDouble();
//        	y0_world = input.nextDouble();       
//        	
//        	//Convert P0 in read world to screen
//        	x0_screen = (width - 1) * (x0_world + 1)/2;
//            y0_screen = (height - 1) * (y0_world + 1)/2;  
//        	
//        	//Point P1
//        	x1_world = input.nextDouble();
//        	y1_world = input.nextDouble();
//        	
//            //Convert P1 in read world to screen
//            x1_screen = (width - 1) * (x1_world + 1)/2;
//            y1_screen = (height - 1) * (y1_world + 1)/2;
//            
//            //Draws a line with Bresenham's algorithm
//            plotLine((int)x0_screen, (int)y0_screen, (int)x1_screen, (int)y1_screen, r, g, b);
//        }  else if (command.equals("TRI")) {
//        	//Vertex V0
//        	x0_world = input.nextDouble();
//        	y0_world = input.nextDouble();
//        	
//        	//Convert V1 in real world to screen
//        	x0_screen = (width - 1) * (x0_world + 1)/2;
//            y0_screen = (height - 1) * (y0_world + 1)/2;
//        	
//        	//Vertex V1
//        	x1_world = input.nextDouble();
//        	y1_world = input.nextDouble();
//        	
//        	//Convert V1 in real world to screen
//            x1_screen = (width - 1) * (x1_world + 1)/2;
//            y1_screen = (height - 1) * (y1_world + 1)/2;
//        	
//        	//Vertex V2
//        	x2_world = input.nextDouble();
//        	y2_world = input.nextDouble();
//        	
//            //Convert V1 in real world to screen
//            x2_screen = (width - 1) * (x2_world + 1)/2; 
//            y2_screen = (height - 1) * (y2_world + 1)/2; 
//            
//            //Step 1: Draw the edges of a triangle first.
//            plotLine((int)x0_screen, (int)y0_screen, (int)x1_screen, (int)y1_screen, r, g, b);
//            plotLine((int)x1_screen, (int)y1_screen, (int)x2_screen, (int)y2_screen, r, g, b);
//            plotLine((int)x2_screen, (int)y2_screen, (int)x0_screen, (int)y0_screen, r, g, b);
//            
//            //Step 2: Use scanline algorithm to fill the triangle.
//            //Add vertexes to ArrayList to array them in x order.
//            ArrayList<Vertex> vertexes = new ArrayList<Vertex>(); 
//            vertexes.add(new Vertex(x0_screen, y0_screen));
//            vertexes.add(new Vertex(x1_screen, y1_screen));
//            vertexes.add(new Vertex(x2_screen, y2_screen));  
//            
//            //Sorts vertexes based on x
//            Collections.sort(vertexes);
//            
//            //Edge data are the information of P0 and P1 and its slope.
//            ArrayList<Edge> edges = new ArrayList<Edge>(); 
//            edges.add(new Edge(vertexes.get(1), vertexes.get(0)));
//            edges.add(new Edge(vertexes.get(1), vertexes.get(2)));
//            edges.add(new Edge(vertexes.get(0), vertexes.get(2)));
//                            
//            //Find the MinY and MaxY
//            ArrayList<Double> coorY = new ArrayList<Double>();
//            coorY.add(y0_screen);
//            coorY.add(y1_screen);
//            coorY.add(y2_screen);
//            
//            //Sorts coordinate y
//            Collections.sort(coorY);
//            
//            //Get MinY and MaxY
//            double minY = coorY.get(0);
//            double maxY = coorY.get(2);
//            double minX = 0;
//        	double maxX = 0;
//            
//        	//Draw lines from MinY to MaxY
//            for (double y = minY; y < maxY; y++) {
//            	if (edges.get(0).getSlope() == 0) {                		
//            		// V1 and V2 are in a vertical line
//            		if (Double.isInfinite( edges.get(1).getSlope())) {
//            			minX = vertexes.get(0).getX() + ((y - vertexes.get(0).getY()) / edges.get(2).getSlope());
//            			maxX = vertexes.get(2).getX();                			
//            		} else { // There are 2 cases y2 < y1 or y2 > y1
//            			minX = vertexes.get(0).getX() + ((y - vertexes.get(0).getY()) / edges.get(2).getSlope());
//            			maxX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(1).getSlope());
//            		}                		
//            		for (double x = minX; x < maxX; x++) {
//                		drawPixel((int)x, (int)y, r, g, b);
//                	}
//            	} else if (Double.isInfinite(edges.get(0).getSlope())) {   
//            		if (vertexes.get(0).getY() < vertexes.get(1).getY()) {
//            			//Truong hop tam giac la tam giac co chua goc tu                			
//            			if(vertexes.get(2).getY() <= maxY && vertexes.get(1).getY() < vertexes.get(2).getY()) {
//            				//Chia lam 2 de ve
//            				if (y <= vertexes.get(1).getY()) {
//            					minX = vertexes.get(0).getX();
//            					maxX = vertexes.get(0).getX() + ((y - vertexes.get(0).getY()) / edges.get(2).getSlope());
//            				} else {
//            					minX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(1).getSlope());
//            					maxX = vertexes.get(0).getX() + ((y - vertexes.get(0).getY()) / edges.get(2).getSlope());
//            				}
//            			} else if (vertexes.get(2).getY() >= minY && vertexes.get(1).getY() > vertexes.get(2).getY()) { //Truong hop tam giac chua toan goc nhon
//            				minX = vertexes.get(0).getX();
//            				maxX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(1).getSlope());
//            			} else if (edges.get(1).getSlope() == 0) {
//                			minX = vertexes.get(0).getX();
//                			maxX = vertexes.get(0).getX() + ((y - vertexes.get(0).getY()) / edges.get(2).getSlope());               			
//                		} 
//            		} else if (vertexes.get(0).getY() > vertexes.get(1).getY()) {
//            			//Truong hop tam giac la tam giac co chua goc tu                			
//            			if(vertexes.get(2).getY() <= maxY && vertexes.get(0).getY() < vertexes.get(2).getY()) {
//            				//Chia lam 2 de ve
//            				if (y <= vertexes.get(0).getY()) {
//            					minX = vertexes.get(1).getX();
//            					maxX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(1).getSlope());
//            				} else {
//            					minX = vertexes.get(0).getX() + ((y - vertexes.get(0).getY()) / edges.get(2).getSlope());
//            					maxX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(1).getSlope());
//            				}
//            			} else if (vertexes.get(2).getY() >= minY && vertexes.get(0).getY() > vertexes.get(2).getY()) { //Truong hop tam giac chua toan goc nhon
//            				minX = vertexes.get(1).getX();
//            				maxX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(1).getSlope());
//            			} else if (edges.get(2).getSlope() == 0) {
//                			minX = vertexes.get(0).getX();
//                			maxX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(1).getSlope());               			
//                		} 
//            		} 
//            		for (double x = minX; x < maxX; x++) {
//                		drawPixel((int)x, (int)y, r, g, b);
//                	}
//            	} else {
//            		// Vy0 < Vy2 < Vy1
//            		if ((vertexes.get(0).getY() < vertexes.get(2).getY()) && (vertexes.get(2).getY() < vertexes.get(1).getY())) {
//            			if (y < vertexes.get(2).getY()) {
//            				minX = vertexes.get(0).getX() + ((y - vertexes.get(0).getY()) / edges.get(0).getSlope());
//                        	maxX = vertexes.get(0).getX() + ((y - vertexes.get(0).getY()) / edges.get(2).getSlope());
//            			} else {
//            				minX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(0).getSlope());
//                        	maxX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(1).getSlope());
//            			}                			
//            		} else if ((vertexes.get(0).getY() < vertexes.get(1).getY()) && (vertexes.get(1).getY() < vertexes.get(2).getY())) { // Vy0 < vy1 < vy2
//            			if (y < vertexes.get(1).getY()) {
//            				minX = vertexes.get(0).getX() + ((y - vertexes.get(0).getY()) / edges.get(0).getSlope());
//                        	maxX = vertexes.get(0).getX() + ((y - vertexes.get(0).getY()) / edges.get(2).getSlope());
//            			} else {
//            				minX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(0).getSlope());
//                        	maxX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(1).getSlope());
//            			}                 			
//            		} else if ((vertexes.get(2).getY() < vertexes.get(0).getY()) && (vertexes.get(0).getY() < vertexes.get(1).getY())) { // Vy2 < Vy0 < Vy1
//            			if (y < vertexes.get(0).getY()) {
//            				minX = vertexes.get(2).getX() + ((y - vertexes.get(2).getY()) / edges.get(2).getSlope());
//                        	maxX = vertexes.get(2).getX() + ((y - vertexes.get(2).getY()) / edges.get(1).getSlope());
//            			} else {
//            				minX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(0).getSlope());
//                        	maxX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(1).getSlope());
//            			}                			
//            		} else if ((vertexes.get(1).getY() < vertexes.get(0).getY()) && (vertexes.get(0).getY() < vertexes.get(2).getY())) { // Vy1 < Vy0 > Vy2
//            			if (y < vertexes.get(0).getY()) {
//            				minX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(0).getSlope());
//                        	maxX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(1).getSlope());
//            			} else {
//            				minX = vertexes.get(2).getX() + ((y - vertexes.get(2).getY()) / edges.get(2).getSlope());
//                        	maxX = vertexes.get(2).getX() + ((y - vertexes.get(2).getY()) / edges.get(1).getSlope());
//            			}                 			
//            		} else if ((vertexes.get(2).getY() < vertexes.get(1).getY()) && (vertexes.get(1).getY() < vertexes.get(0).getY())) { // Vy2 < Vy1 < Vy0
//            			if (y < vertexes.get(1).getY()) {
//            				minX = vertexes.get(2).getX() + ((y - vertexes.get(2).getY()) / edges.get(2).getSlope());
//                        	maxX = vertexes.get(2).getX() + ((y - vertexes.get(2).getY()) / edges.get(1).getSlope());
//            			} else {
//            				minX = vertexes.get(0).getX() + ((y - vertexes.get(0).getY()) / edges.get(2).getSlope());
//                        	maxX = vertexes.get(0).getX() + ((y - vertexes.get(0).getY()) / edges.get(0).getSlope());
//            			}                			
//            		} else if((vertexes.get(1).getY() < vertexes.get(2).getY()) && (vertexes.get(2).getY() < vertexes.get(0).getY())) { // Vy1< Vy2 < Vy0
//            			if (y < vertexes.get(2).getY()) {
//            				minX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(0).getSlope());
//                        	maxX = vertexes.get(1).getX() + ((y - vertexes.get(1).getY()) / edges.get(1).getSlope());
//            			} else {
//            				minX = vertexes.get(0).getX() + ((y - vertexes.get(0).getY()) / edges.get(0).getSlope());
//                        	maxX = vertexes.get(0).getX() + ((y - vertexes.get(0).getY()) / edges.get(2).getSlope());
//            			}  
//            			
//            		}
//                	for (double x = minX; x < maxX; x++) {
//                		drawPixel((int)x, (int)y, r, g, b);
//                	}
//            	}
//            }
//
//        }
//    }
//}