/**
 * TCSS 458 - Spring 2020
 * Assignment 3
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.*;

// TODO coloring is slightly different. (seems light a shading issue rather than anti-aliasing issue).
/**
 * Main class to handle Assignment 3
 *
 * @author John Mayer
 * @author Steven Tran
 * @version May 2020
 */
public class TCSS458Paint extends JPanel implements KeyListener
{
    private final Queue<double[]> scan = new PriorityQueue<>(25, (o1, o2) -> (int) (o1[1] - o2[1]));
    private final static int MAX_COLOR_RANGE = 255;
    private final static int ROTATION_INCREMENT = 3;
    private final static int IMAGE_SCALE = 2;
    private final static double LIGHT_SPLIT_VALUE = 0.5;
    private final static double[][] IDENTITY_MATRIX = {{1, 0, 0, 0},
                                                       {0, 1, 0, 0},
                                                       {0, 0, 1, 0},
                                                       {0, 0, 0, 1}};
    private final int[] colors = new int[3];
    private double[][] inputRotationMatrix = IDENTITY_MATRIX.clone();
    private double[][] projectionMatrix = IDENTITY_MATRIX.clone();
    private double[][] lookAtMatrix = IDENTITY_MATRIX.clone();
    double[][] CTM = IDENTITY_MATRIX.clone();
    private int rotateHorizontal = 0;
    private int rotateVertical = 0;
    private boolean rendering = true;
    private static int width; // real width
    private static int height; // real height
    private int innerWidth;
    private int innerHeight;
    int imageSize;
    int[] pixels;
    private int[] innerPixels;
    private double[] lightSource;
    double[][] zBuffer;


    void drawPixel(final int x, final int y, final double z, final int r, final int g, final int b)
    {
        if (x >= 0 && y >= 0 && x < innerWidth && y < innerHeight && z < zBuffer[y][x])
        {
            innerPixels[(innerHeight - y - 1) * innerWidth * 3 + x * 3] = r;
            innerPixels[(innerHeight - y - 1) * innerWidth * 3 + x * 3 + 1] = g;
            innerPixels[(innerHeight - y - 1) * innerWidth * 3 + x * 3 + 2] = b;
            zBuffer[y][x] = z;
        }
    }

    /**
     * Creates a solid or wireframe depending on the parameter
     *
     * @param isSolid parameter to determine if you want a solid or line cube
     */
    private void createCube(final boolean isSolid)
    {
        final double[][] CUBE = new double[][] {{0.5, 0.5, 0.5, 1},
                                                {0.5, 0.5, -0.5, 1},
                                                {0.5, -0.5, 0.5, 1},
                                                {0.5, -0.5, -0.5, 1},
                                                {-0.5, 0.5, 0.5, 1},
                                                {-0.5, 0.5, -0.5, 1},
                                                {-0.5, -0.5, 0.5, 1},
                                                {-0.5, -0.5, -0.5, 1}};
        if (isSolid)
        {
            // Making the triangles for the cube
            // front
            scanline(CUBE[0], CUBE[4], CUBE[6]);
            scanline(CUBE[0], CUBE[6], CUBE[2]);

            // right
            scanline(CUBE[0], CUBE[2], CUBE[3]);
            scanline(CUBE[0], CUBE[3], CUBE[1]);

            // left
            scanline(CUBE[4], CUBE[7], CUBE[6]);
            scanline(CUBE[4], CUBE[5], CUBE[7]);

            // up
            scanline(CUBE[0], CUBE[5], CUBE[4]);
            scanline(CUBE[0], CUBE[1], CUBE[5]);

            // down
            scanline(CUBE[2], CUBE[7], CUBE[3]);
            scanline(CUBE[2], CUBE[6], CUBE[7]);

            // back
            scanline(CUBE[7], CUBE[5], CUBE[1]);
            scanline(CUBE[7], CUBE[1], CUBE[3]);
        }
        else
        {

            // Making the lines for the cube
            createLine(CUBE[0], CUBE[1]);
            createLine(CUBE[1], CUBE[3]);
            createLine(CUBE[3], CUBE[2]);
            createLine(CUBE[2], CUBE[0]);
            createLine(CUBE[0], CUBE[4]);
            createLine(CUBE[1], CUBE[5]);
            createLine(CUBE[2], CUBE[6]);
            createLine(CUBE[3], CUBE[7]);
            createLine(CUBE[4], CUBE[5]);
            createLine(CUBE[5], CUBE[7]);
            createLine(CUBE[7], CUBE[6]);
            createLine(CUBE[6], CUBE[4]);
        }
    }

    private void createLine(final double[] p1, final double[] p2)
    {
        // projection * lookAt * interactive rotation * CTM
        final double[][] CTMTemp = matrixMultiply(projectionMatrix, matrixMultiply(lookAtMatrix, matrixMultiply(inputRotationMatrix, CTM)));

        // Transform points with CTM.
        double[] pOne = vectorMatrixMultiply(CTMTemp, p1);
        double[] pTwo = vectorMatrixMultiply(CTMTemp, p2);

        for(int i = 0; i < pOne.length; i++)
        {
            pOne[i] = pOne[i] / pOne[3];
            pTwo[i] = pTwo[i] / pTwo[3];
        }

        // Bresenham's Algorithm
        // Psuedocode taken from https://en.wikipedia.org/wiki/Bresenham%27s_line_algorithm
        bresenham(pOne, pTwo, colors);
    }

    /**
     * Rotate about some axis with some degree
     *
     * @param axis   the axis to rotate about
     * @param degree the degree to rotate
     */
    private void rotate(final char axis, final double degree)
    {
        if (axis == 'x')
        {
            CTM = matrixMultiply(new double[][] {{1, 0, 0, 0},
                                                 {0, Math.cos(Math.toRadians(degree)), -Math.sin(Math.toRadians(degree)), 0},
                                                 {0, Math.sin(Math.toRadians(degree)), Math.cos(Math.toRadians(degree)), 0},
                                                 {0, 0, 0, 1}}, CTM);
        }
        else if (axis == 'y')
        {
            CTM = matrixMultiply(new double[][] {{Math.cos(Math.toRadians(degree)), 0, Math.sin(Math.toRadians(degree)), 0},
                                                 {0, 1, 0, 0},
                                                 {-Math.sin(Math.toRadians(degree)), 0, Math.cos(Math.toRadians(degree)), 0},
                                                 {0, 0, 0, 1}}, CTM);
        }
        else if (axis == 'z')
        {
            CTM = matrixMultiply(new double[][] {{Math.cos(Math.toRadians(degree)), -Math.sin(Math.toRadians(degree)), 0, 0},
                                                 {Math.sin(Math.toRadians(degree)), Math.cos(Math.toRadians(degree)), 0, 0},
                                                 {0, 0, 1, 0},
                                                 {0, 0, 0, 1}}, CTM);
        }
        else
        {
            throw new IllegalArgumentException("Invalid axis");
        }
    }

    /**
     * Normalizes a 3D vector
     * If the vector is a zero vector, the zero vector will be returned
     * @param prenormVect vector to be normalized
     * @return normalized vector or zero vector
     */
    private static double[] normalize3(final double [] prenormVect)
    {
        final double length = Math.sqrt(Math.pow(prenormVect[0], 2) + Math.pow(prenormVect[1], 2) + Math.pow(prenormVect[2], 2));
        if (length > 0) return new double[] {prenormVect[0] / length, prenormVect[1] / length, prenormVect[2] / length};
        else return new double[] {0, 0, 0};
    }

    private static double[] crossproduct(final double[] firstVect, final double[] secondVect)
    {
        if (firstVect.length != 3 || secondVect.length != 3) throw new IllegalArgumentException("One (or more) vector are not length 3");
        return new double[] {firstVect[1] * secondVect[2] - firstVect[2] * secondVect[1],
                             firstVect[2] * secondVect[0] - firstVect[0] * secondVect[2],
                             firstVect[0] * secondVect[1] - firstVect[1] * secondVect[0]};
    }

    private static double dotproduct(final double[] firstVect, final double[] secondVect)
    {
        if (firstVect.length != secondVect.length) throw new IllegalArgumentException("The vectors are not the same length");
        double sum = 0;
        for (int i = 0; i < firstVect.length; i++) sum += firstVect[i] * secondVect[i];
        return sum;
    }

    private double[][] createLookAt(final double xEye, final double yEye, final double zEye,
                                    final double xCenter, final double yCenter, final double zCenter,
                                    final double xUp, final double yUp, final double zUp)
    {
        final double[] eye = new double[] {xEye, yEye, zEye};
        final double[] center = new double[] {xCenter, yCenter, zCenter};
        final double[] up = new double[] {xUp, yUp, zUp};

        // n = eye - center
        // n maps to z axis
        double[] n = new double[3];
        for (int i = 0; i < n.length; i++) n[i] = eye[i] - center[i];
        n = normalize3(n);

        // u maps to x axis
        double[] u = crossproduct(up, n);
        u = normalize3(u);

        // v maps to y axis
        double[] v = crossproduct(n, u);
        v = normalize3(v);

        final double[][] other = new double[][] {{u[0], u[1], u[2], 0},
                                                 {v[0], v[1], v[2], 0},
                                                 {n[0], n[1], n[2], 0},
                                                 {0, 0, 0, 1}};
        final double[][] eyes = new double[][] {{1, 0, 0, -eye[0]},
                                                {0, 1, 0, -eye[1]},
                                                {0, 0, 1, -eye[2]},
                                                {0, 0, 0, 1}};

        // left handed coordinated system
        return matrixMultiply(other, eyes);
    }

    // the top and bottom params are in different flipped
    private double[][] createOrthographic(final double left, final double right, final double top, final double bottom, final double near,
                                          final double far)
    {
        return new double[][] {{2 / (right - left), 0, 0, -(right + left) / (right - left)},
                               {0, 2 / (top - bottom), 0, -(top + bottom) / (top - bottom)},
                               {0, 0, -2 / (far - near), -(far + near) / (far - near)},
                               {0, 0, 0, 1}};
    }

    private double[][] createFrustum(final double left, final double right, final double top, final double bottom, final double near,
                                     final double far)
    {
        final double[] r1 = new double[] {2 * near / (right - left), 0, 0, -near * (right + left) / (right - left)};
        final double[] r2 = new double[] {0, 2 * near / (top - bottom), 0, -near * (top + bottom) / (top - bottom)};
        final double[] r3 = new double[] {0, 0, -(far + near) / (far - near), 2 * far * near / (near - far)};
        final double[] r4 = new double[] {0, 0, -1, 0};

        return new double[][] {r1, r2, r3, r4};
    }

    void createImage()
    {
        CTM = IDENTITY_MATRIX.clone();
        projectionMatrix = IDENTITY_MATRIX.clone();
        inputRotationMatrix = IDENTITY_MATRIX.clone();
        // Modify the user input rotation matrix with user input.
        // vertical rotation
        inputRotationMatrix = matrixMultiply(
                new double[][] {{1, 0, 0, 0},
                                {0, Math.cos(Math.toRadians(rotateVertical)), -Math.sin(Math.toRadians(rotateVertical)), 0},
                                {0, Math.sin(Math.toRadians(rotateVertical)), Math.cos(Math.toRadians(rotateVertical)), 0},
                                {0, 0, 0, 1}}, inputRotationMatrix);
        // horizontal rotation
        inputRotationMatrix = matrixMultiply(
                new double[][] {{Math.cos(Math.toRadians(rotateHorizontal)), 0, Math.sin(Math.toRadians(rotateHorizontal)), 0},
                                {0, 1, 0, 0},
                                {-Math.sin(Math.toRadians(rotateHorizontal)), 0, Math.cos(Math.toRadians(rotateHorizontal)), 0},
                                {0, 0, 0, 1}}, inputRotationMatrix);
        Scanner input = getFile();
        while (input.hasNext())
        {
            String command = input.next();
            switch (command)
            {
                case "DIM":

                    // Real image size
                    width = input.nextInt();
                    height = input.nextInt();
                    imageSize = width * height;
                    pixels = new int[imageSize * 3];

                    // 2 x (height, width) for AA
                    innerWidth = width * IMAGE_SCALE;
                    innerHeight = height * IMAGE_SCALE;
                    final int myInnerImageSize = innerWidth * innerHeight;
                    innerPixels = new int[myInnerImageSize * 3];

                    // Makes the background white
                    Arrays.fill(pixels, 255);
                    Arrays.fill(innerPixels, 255);

                    zBuffer = new double[innerHeight][innerWidth];
                    for (final double[] theDoubles : zBuffer) Arrays.fill(theDoubles, Double.POSITIVE_INFINITY);
                    break;

                case "LOOKAT":
                    lookAtMatrix = createLookAt(input.nextDouble(), input.nextDouble(), input.nextDouble(),
                                 input.nextDouble(), input.nextDouble(), input.nextDouble(),
                                 input.nextDouble(), input.nextDouble(), input.nextDouble());
                    break;
                case "FRUSTUM":
                    projectionMatrix = createFrustum(input.nextDouble(), input.nextDouble(), input.nextDouble(),
                                                     input.nextDouble(), input.nextDouble(), input.nextDouble());
                    break;
                case "ORTHO":
                    projectionMatrix = createOrthographic(input.nextDouble(), input.nextDouble(), input.nextDouble(),
                                                          input.nextDouble(), input.nextDouble(), input.nextDouble());
                    break;
                case "LIGHT_DIRECTION":
                    lightSource = new double[] {input.nextDouble(), input.nextDouble(), input.nextDouble()};
                    break;
                case "LOAD_IDENTITY_MATRIX":
                    CTM = IDENTITY_MATRIX.clone();
                    break;

                case "SOLID_CUBE":
                    createCube(true);
                    break;

                case "WIREFRAME_CUBE":
                    createCube(false);
                    break;

                case "SCALE":
                    CTM = matrixMultiply(new double[][] {{input.nextDouble(), 0, 0, 0},
                                                         {0, input.nextDouble(), 0, 0},
                                                         {0, 0, input.nextDouble(), 0},
                                                         {0, 0, 0, 1}}, CTM);
                    break;

                case "TRANSLATE":
                    CTM = matrixMultiply(new double[][] {{1, 0, 0, input.nextDouble()},
                                                         {0, 1, 0, input.nextDouble()},
                                                         {0, 0, 1, input.nextDouble()},
                                                         {0, 0, 0, 1}}, CTM);
                    break;

                case "ROTATEX":
                    rotate('x', input.nextDouble());
                    break;

                case "ROTATEY":
                    rotate('y', input.nextDouble());
                    break;

                case "ROTATEZ":
                    rotate('z', input.nextDouble());
                    break;

                case "LINE":
                    final double[] pOne = new double[] {input.nextDouble(), input.nextDouble(), input.nextDouble(), 1};
                    final double[] pTwo = new double[] {input.nextDouble(), input.nextDouble(), input.nextDouble(), 1};
                    createLine(pOne, pTwo);
                    break;

                case "RGB":
                    colors[0] = (int) Math.round(input.nextDouble() * MAX_COLOR_RANGE);
                    colors[1] = (int) Math.round(input.nextDouble() * MAX_COLOR_RANGE);
                    colors[2] = (int) Math.round(input.nextDouble() * MAX_COLOR_RANGE);
                    break;

                case "TRI":
                    final double[] p1 = new double[] {input.nextDouble(), input.nextDouble(), input.nextDouble(), 1};
                    final double[] p2 = new double[] {input.nextDouble(), input.nextDouble(), input.nextDouble(), 1};
                    final double[] p3 = new double[] {input.nextDouble(), input.nextDouble(), input.nextDouble(), 1};

                    scanline(p1, p2, p3);
                    break;
            }
        }
        convertToTrueSize();
        rendering = false;
    }

    /**
     * Creates a line using bresenham algorithm
     *
     * @param p1 initial point
     * @param p2 end point
     */
    private void bresenham(final double[] p1, final double[] p2, final int[] lightedColors)
    {
        final int[] screenP1 = convertWorldToScreen(p1[0], p1[1]);
        final int[] screenP2 = convertWorldToScreen(p2[0], p2[1]);
        final boolean steep = Math.abs(screenP2[1] - screenP1[1]) > Math.abs(screenP2[0] - screenP1[0]);
        // Check which dimension to perform the main math on
        if (steep)
        {
            // x: end point > initial point
            if (screenP2[1] >= screenP1[1])
            {
                bresenhamSteep(screenP1[0], screenP1[1], p1[2], screenP2[0], screenP2[1], p2[2], lightedColors);
            }
            else
            {
                bresenhamSteep(screenP2[0], screenP2[1], p2[2], screenP1[0], screenP1[1], p1[2], lightedColors);
            }
        }
        else
        {
            // Ensure least to greatest
            // y: end point > initial
            if (screenP2[0] >= screenP1[0])
            {
                bresenhamGentle(screenP1[0], screenP1[1], p1[2], screenP2[0], screenP2[1], p2[2], lightedColors);
            }
            else
            {
                bresenhamGentle(screenP2[0], screenP2[1], p2[2], screenP1[0], screenP1[1], p1[2], lightedColors);
            }
        }
    }

    private void bresenhamSteep(final int x1, final int y1, final double z1,
                                final int x2, final int y2, final double z2, final int[] lightedColors)
    {
        int deltaX = x2 - x1;
        final int deltaY = y2 - y1;
        int increment = 1;
        // Slope is going up (if x is horizontal and y is vertical)
        if (deltaX < 0)
        {
            increment = -1;
            deltaX = Math.abs(deltaX);
        }
        int p = 2 * deltaX - deltaY;
        int x = x1;
        final double zySlope = (z2 - z1) / deltaY;
        double z = z1;
        for (int y = y1; y <= y2; y++)
        {
            drawPixel(x, y, z, lightedColors[0], lightedColors[1], lightedColors[2]);
            scan.offer(new double[] {x, y, z});
            z += zySlope;
            if (p >= 0)
            {
                // Choose the x coordinate above
                x += increment;
                p = p - (2 * deltaY);
            }
            p = p + (2 * deltaX);
        }
    }

    private void bresenhamGentle(final int x1, final int y1, final double z1,
                                 final int x2, final int y2, final double z2, final int[] lightedColors)
    {
        final int deltaX = x2 - x1;
        int deltaY = y2 - y1;
        int increment = 1;

        if (deltaY < 0)
        {
            increment = -1;
            deltaY = Math.abs(deltaY);
        }
        int p = 2 * deltaY - deltaX;
        int y = y1;
        final double zxSlope = (z2 - z1) / deltaX;
        double z = z1;
        // Iterate through all the x's
        for (int x = x1; x <= x2; x++)
        {
            drawPixel(x, y, z, lightedColors[0], lightedColors[1], lightedColors[2]);
            scan.offer(new double[] {x, y, z});
            z += zxSlope;
            if (p >= 0)
            {
                // Choose the y coordinate above
                y += increment;
                p = p - (2 * deltaX);
            }
            p = p + (2 * deltaY);
        }
    }

    /**
     * Creates a triangle of a color using scanline algorithm
     *
     * @param v1 first point of triangle
     * @param v2 second point of triangle
     * @param v3 third point of triangle
     */
    private void scanline(final double[] v1, final double[] v2, final double[] v3)
    {
        scan.clear();

        // lookAt * interactive rotation * CTM
        double[][] CTMTemp = matrixMultiply(lookAtMatrix, matrixMultiply(inputRotationMatrix, CTM));
        double[] pOne = vectorMatrixMultiply(CTMTemp, v1);
        double[] pTwo = vectorMatrixMultiply(CTMTemp, v2);
        double[] pThree = vectorMatrixMultiply(CTMTemp, v3);

        // normalize with respect to w
        for (int i = 0; i < pOne.length; i++)
        {
            pOne[i] = pOne[i] / pOne[3];
            pTwo[i] = pTwo[i] / pTwo[3];
            pThree[i] = pThree[i] / pThree[3];
        }

        // Calculating shading factor and new color
        final double[] v2minusv1 = new double[] {pTwo[0] - pOne[0], pTwo[1] - pOne[1], pTwo[2] - pOne[2]};
        final double[] v3minusv1 = new double[] {pThree[0] - pOne[0], pThree[1] - pOne[1], pThree[2] - pOne[2]};
        final double[] normal = crossproduct(v2minusv1, v3minusv1);
        final double result = dotproduct(normalize3(lightSource), normalize3(normal));
        final double shadingFactor = result > 0 ? result : 0;
        final int[] newColors = new int[] {(int) Math.round(colors[0] * LIGHT_SPLIT_VALUE + (colors[0] * LIGHT_SPLIT_VALUE * shadingFactor)),
                                           (int) Math.round(colors[1] * LIGHT_SPLIT_VALUE + (colors[1] * LIGHT_SPLIT_VALUE * shadingFactor)),
                                           (int) Math.round(colors[2] * LIGHT_SPLIT_VALUE + (colors[2] * LIGHT_SPLIT_VALUE * shadingFactor))};


        // Multiply the points with the (projection * lookAt * interactive rotation * CTM)
        CTMTemp = matrixMultiply(projectionMatrix, CTMTemp);
        pOne = vectorMatrixMultiply(CTMTemp, v1);
        pTwo = vectorMatrixMultiply(CTMTemp, v2);
        pThree = vectorMatrixMultiply(CTMTemp, v3);

        for (int i = 0; i < pOne.length; i++)
        {
            pOne[i] = pOne[i] / pOne[3];
            pTwo[i] = pTwo[i] / pTwo[3];
            pThree[i] = pThree[i] / pThree[3];
        }

        bresenham(pOne, pTwo, newColors);
        bresenham(pTwo, pThree, newColors);
        bresenham(pThree, pOne, newColors);

        for (final double[] myDoubles : scan)
        {
            double minX = myDoubles[0], maxX = myDoubles[0];
            double minZ = myDoubles[2], maxZ = myDoubles[2];
            final double y = myDoubles[1];
            // y is within the bounds of 0 and innerHeight - 1
            if (y >= 0 && y < innerHeight)
            {
                // Get the min/max x and the associated z's for each y
                for (final double[] myDubs : scan)
                {
                    if (myDubs[1] == y)
                    {
                        if (myDubs[0] < minX)
                        {
                            minX = myDubs[0];
                            minZ = myDubs[2];
                        }
                        if (myDubs[0] > maxX)
                        {
                            maxX = myDubs[0];
                            maxZ = myDubs[2];
                        }
                    }
                }
                double z = minZ;
                final double zSlope = (maxZ - minZ) / (maxX - minX);
                for (double x = minX; x < maxX; x++)
                {
                    drawPixel((int) x, (int) y, z, newColors[0], newColors[1], newColors[2]);
                    z += zSlope;
                }
            }
        }
    }

    /**
     * Convert the world coordinates of -1 to 1 into the respective screen coordinate described in the specifications.
     *
     * @param x x component of the world coordinate
     * @param y y component of the world component
     * @return int[] where index 0 is x coordinate of the screen coordinate and index 0 is the y coordinate of the screen coordinate
     */
    private int[] convertWorldToScreen(final double x, final double y)
    {
        return new int[] {(int) Math.round((innerWidth - 1) * (x + 1) / 2), (int) Math.round((innerHeight - 1) * (y + 1) / 2)};
    }

    /** Converts the double sized pixel array into the real size. In other words, converting 2x object back to 1x */
    private void convertToTrueSize()
    {
        for (int row = 0; row < height; row++)
        {
            for (int col = 0; col < width; col++)
            {
                // Get average of 2x2 pixels in the inner pixel array and use it as a single pixel in real pixel array
                // row * width * 3 = in the right row
                // col * 3 = in the right column now
                // R
                pixels[row * width * 3 + col * 3] = (innerPixels[(IMAGE_SCALE * row) * innerWidth * 3 + (IMAGE_SCALE * col) * 3] +
                                                     innerPixels[(IMAGE_SCALE * row + 1) * innerWidth * 3 + (IMAGE_SCALE * col) * 3] +
                                                     innerPixels[(IMAGE_SCALE * row) * innerWidth * 3 + (IMAGE_SCALE * col + 1) * 3] +
                                                     innerPixels[(IMAGE_SCALE * row + 1) * innerWidth * 3 + (IMAGE_SCALE * col + 1) * 3]) / 4;
                // G
                pixels[row * width * 3 + col * 3 + 1] = (innerPixels[(IMAGE_SCALE * row) * innerWidth * 3 + (IMAGE_SCALE * col) * 3 + 1] +
                                                         innerPixels[(IMAGE_SCALE * row + 1) * innerWidth * 3 + (IMAGE_SCALE * col) * 3 + 1] +
                                                         innerPixels[(IMAGE_SCALE * row) * innerWidth * 3 + (IMAGE_SCALE * col + 1) * 3 + 1] +
                                                         innerPixels[(IMAGE_SCALE * row + 1) * innerWidth * 3 + (IMAGE_SCALE * col + 1) * 3 + 1]) / 4;
                // B
                pixels[row * width * 3 + col * 3 + 2] = (innerPixels[(IMAGE_SCALE * row) * innerWidth * 3 + (IMAGE_SCALE * col) * 3 + 2] +
                                                         innerPixels[(IMAGE_SCALE * row + 1) * innerWidth * 3 + (IMAGE_SCALE * col) * 3 + 2] +
                                                         innerPixels[(IMAGE_SCALE * row) * innerWidth * 3 + (IMAGE_SCALE * col + 1) * 3 + 2] +
                                                         innerPixels[(IMAGE_SCALE * row + 1) * innerWidth * 3 + (IMAGE_SCALE * col + 1) * 3 + 2]) / 4;
            }
        }
    }

    public void paintComponent(Graphics g)
    {
        createImage();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        WritableRaster wr_raster = image.getRaster();
        wr_raster.setPixels(0, 0, width, height, pixels);
        g.drawImage(image, 0, 0, null);
    }

    public static void main(final String[] args)
    {
        JFrame frame = new JFrame("STRAN ASSIGNMENT 3");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        selectFile();

        JPanel rootPane = new TCSS458Paint();
        getDim(rootPane);
        rootPane.setPreferredSize(new Dimension(width, height));
        rootPane.setFocusable(true);
        rootPane.addKeyListener((KeyListener) rootPane);

        frame.getContentPane().add(rootPane);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    static File selectedFile = null;

    static private void selectFile()
    {
        int approve; //return value from JFileChooser indicates if the user hit cancel

        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));

        approve = chooser.showOpenDialog(null);
        if (approve != JFileChooser.APPROVE_OPTION)
        {
            System.exit(0);
        }
        else
        {
            selectedFile = chooser.getSelectedFile();
        }
    }

    static private Scanner getFile()
    {
        Scanner input = null;
        try
        {
            input = new Scanner(selectedFile);
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,
                                          "There was an error with the file you chose.",
                                          "File Error", JOptionPane.ERROR_MESSAGE);
        }
        return input;
    }

    static void getDim(JPanel rootPane)
    {
        Scanner input = getFile();

        String command = input.next();
        if (command.equals("DIM"))
        {
            width = input.nextInt();
            height = input.nextInt();
            rootPane.setPreferredSize(new Dimension(width, height));
        }
    }

    @Override
    public void keyTyped(final KeyEvent e)
    {
        // Maybe use this one for key stroke
    }

    @Override
    public void keyPressed(final KeyEvent e)
    {
        if (!rendering)
        {
            switch (e.getKeyCode())
            {
                case 40: // down
                    rotateVertical += ROTATION_INCREMENT;
                    repaint();
                    break;
                case 39: // right
                    rotateHorizontal += ROTATION_INCREMENT;
                    repaint();
                    break;
                case 38: // up
                    rotateVertical -= ROTATION_INCREMENT;
                    repaint();
                    break;
                case 37: // left
                    rotateHorizontal -= ROTATION_INCREMENT;
                    repaint();
                    break;
            }
        }
    }

    @Override
    public void keyReleased(final KeyEvent e)
    {
        // Maybe use this one for key stroke
    }

    /**
     * Multiplies two matrices and returns a new matrix.
     * Let A = the First Matrix
     * Let B = the Second Matrix
     * C = A * B
     *
     * @param firstMatrix  the first matrix to be multipled
     * @param secondMatrix the second matrix to be multiplied
     * @return product of the matrix multiplication
     */
    static double[][] matrixMultiply(double[][] firstMatrix, final double[][] secondMatrix)
    {
        if (firstMatrix == null || secondMatrix == null)
        {
            throw new NullPointerException();
        }
        if (firstMatrix[0].length == secondMatrix.length)
        {
            final double[][] newMatrix = new double[firstMatrix.length][secondMatrix[0].length];
            for (var i = 0; i < firstMatrix.length; i++)
            {
                for (var j = 0; j < secondMatrix[i].length; j++)
                {
                    for (var k = 0; k < firstMatrix[i].length; k++)
                    {
                        // Matches (first) row, (second) column
                        // k iterates through each column value
                        newMatrix[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
                    }
                }
            }
            return newMatrix;
        }
        else
        {
            throw new IllegalArgumentException("First matrix row size must be equal to second matrix column size");
        }
    }

    /**
     * Multiplies a matrix with a vector.
     * Let A = some matrix
     * Let B = some vector
     * C = A * B
     * Example with 4 x 4 matrix and 4D vector
     * [[a, b, c ,d]   [x,
     * [e, f, g, h]     y,
     * [i, j, k, l]  *  z,
     * [m, n, o, p]]    w]
     *
     * @param mat  the matrix to multiply with the vector
     * @param vect the vector to multiply with the matrix
     * @return vector that is the product of the multiplication of the matrix and vector
     */
    static double[] vectorMatrixMultiply(double[][] mat, final double[] vect)
    {
        final double[][] vectorToMatrix = new double[vect.length][1];
        for (var i = 0; i < vectorToMatrix.length; i++) vectorToMatrix[i][0] = vect[i];
        final double[][] newMatrix = matrixMultiply(mat, vectorToMatrix);
        final double[] newVector = new double[newMatrix.length];
        for (var i = 0; i < newVector.length; i++) newVector[i] = newMatrix[i][0];
        return newVector;
    }
}