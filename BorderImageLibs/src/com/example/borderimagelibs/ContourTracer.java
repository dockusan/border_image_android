/**
 * This sample code is made available as part of the book "Digital Image
 * Processing - An Algorithmic Introduction using Java" by Wilhelm Burger
 * and Mark J. Burge, Copyright (C) 2005-2008 Springer-Verlag Berlin, 
 * Heidelberg, New York.
 * Note that this code comes with absolutely no warranty of any kind.
 * See http://www.imagingbook.com for details and licensing conditions.
 * 
 * Date: 2010/08/01
 */

package com.example.borderimagelibs;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Region;

/*
 * Modified 2010-08-01
 */

public class ContourTracer {
	static final byte FOREGROUND = 1;
	static final byte BACKGROUND = 0;
	static boolean beVerbose = true;

	List<Contour> outerContours = null;
	List<Contour> innerContours = null;
	List<Contour> randomInnerContours = null;
	List<Contour> randomInnerContoursInside = null;

	List<Contour> randomOuterContours = null;
	List<Contour> randomOuterContoursInside = null;

	List<Region> allRegions = null;
	int regionId = 0;

	Bitmap ip = null;
	int width;
	int height;
	byte[][] pixelArray;
	int[][] labelArray;

	// label values in labelArray can be:
	// 0 ... unlabeled
	// -1 ... previously visited background pixel
	// >0 ... valid label

	// constructor method
	public ContourTracer(Bitmap ip) {
		this.ip = ip;
		this.width = ip.getWidth();
		this.height = ip.getHeight();
		makeAuxArrays();
		findAllContours();
	}

	public static void setVerbose(boolean verbose) {
		beVerbose = verbose;
	}

	public List<Contour> getOuterContours() {
		return outerContours;
	}

	public List<Contour> getInnerContours() {
		return innerContours;
	}

	public List<Contour> getRandomInnerContours() {
		return randomInnerContours;
	}

	public List<Contour> getRandomOuterContours() {
		return randomOuterContours;
	}

	public List<Contour> getRandomInnerInsideContours() {
		return randomInnerContoursInside;
	}

	public List<Contour> getRandomOuterInsideContours() {
		return randomOuterContoursInside;
	}

	public List<Region> getRegions() {
		return allRegions;
	}

	// Return the region label (if existent) at position
	// (x,y).
	public int getLabel(int x, int y) {
		if (x >= 0 && x < width && y >= 0 && y < height)
			return labelArray[y][x];
		else
			return BACKGROUND;
	}

	// non-public methods -----------------------------------------------

	// Create auxil. arrays, which are "padded", i.e.,
	// are 2 rows and 2 columns larger than the image:
	void makeAuxArrays() {
		int h = ip.getHeight();
		int w = ip.getWidth();
		pixelArray = new byte[h + 2][w + 2];
		labelArray = new int[h + 2][w + 2]; // initialized to zero (0)
		// initialize pixelArray[][]:
		// for (int j = 0; j < pixelArray.length; j++) {
		// for (int i = 0; i < pixelArray[j].length; i++) {
		// pixelArray[j][i] = BACKGROUND;
		//
		// }
		// }
		// copy the contents of the binary image to pixelArray,
		// starting at array coordinate [1][1], i.e., centered:
		for (int v = 0; v < h; v++) {
			for (int u = 0; u < w; u++) {
				// Log.e("Vao day ngoai nay"," Vao day ngoai nay");
				int alpha = (int) (Color.alpha(ip.getPixel(u, v)));
				if (alpha > 125) {
					// Log.e("Vao day"," Vao day ");
					pixelArray[v + 1][u + 1] = FOREGROUND;
				}
			}
		}
	}

	Contour traceOuterContour(int cx, int cy, int label) {
		Contour cont = new Contour(label);
		traceContour(cx, cy, label, 0, cont);
		return cont;
	}

	Contour traceInnerContour(int cx, int cy, int label) {
		Contour cont = new Contour(label);
		traceContour(cx, cy, label, 1, cont);
		return cont;
	}

	// Trace one contour starting at (xS,yS)
	// in direction dS with label label
	// trace one contour starting at (xS,yS) in direction dS
	Contour traceContour(int xS, int yS, int label, int dS, Contour cont) {
		int xT, yT; // T = successor of starting point (xS,yS)
		int xP, yP; // P = previous contour point
		int xC, yC; // C = current contour point
		Point pt = new Point(xS, yS);
		int dNext = findNextPoint(pt, dS);
		cont.addPoint(pt);
		xP = xS;
		yP = yS;
		xC = xT = pt.x;
		yC = yT = pt.y;

		boolean done = (xS == xT && yS == yT); // true if isolated pixel

		while (!done) {
			labelArray[yC][xC] = label;
			pt = new Point(xC, yC);
			int dSearch = (dNext + 6) % 8;
			dNext = findNextPoint(pt, dSearch);
			xP = xC;
			yP = yC;
			xC = pt.x;
			yC = pt.y;
			// are we back at the starting position?
			done = (xP == xS && yP == yS && xC == xT && yC == yT);
			if (!done) {
				cont.addPoint(pt);
			}
		}
		return cont;
	}

	// Starts at Point pt in direction dir
	// returns the final tracing direction
	// and modifies pt
	int findNextPoint(Point pt, int dir) {

		final int[][] delta = { { 1, 0 }, { 1, 1 }, { 0, 1 }, { -1, 1 },
				{ -1, 0 }, { -1, -1 }, { 0, -1 }, { 1, -1 } };
		for (int i = 0; i < 7; i++) {
			int x = pt.x + delta[dir][0];
			int y = pt.y + delta[dir][1];
			if (pixelArray[y][x] == BACKGROUND) {
				labelArray[y][x] = -1; // mark surrounding background pixels
				dir = (dir + 1) % 8;
			} else { // found non-background pixel
				pt.x = x;
				pt.y = y;
				break;
			}
		}
		return dir;
	}

	void findAllContours() {
		outerContours = new ArrayList<Contour>();
		innerContours = new ArrayList<Contour>();
		randomInnerContours = new ArrayList<Contour>();
		randomOuterContours = new ArrayList<Contour>();
		randomInnerContoursInside = new ArrayList<Contour>();
		randomOuterContoursInside = new ArrayList<Contour>();

		int label = 0; // current label

		// scan top to bottom, left to right
		for (int v = 1; v < pixelArray.length - 1; v++) {
			label = 0; // no label
			for (int u = 1; u < pixelArray[v].length - 1; u++) {

				if (pixelArray[v][u] == FOREGROUND) {
					if (label != 0) { // keep using same label
						labelArray[v][u] = label;
					} else {
						label = labelArray[v][u];
						if (label == 0) { // unlabeled - new outer contour
							regionId = regionId + 1;
							label = regionId;
							Contour oc = traceOuterContour(u, v, label);
							outerContours.add(oc);

							labelArray[v][u] = label;
						}
					}
				} else { // BACKGROUND pixel
					if (label != 0) {
						if (labelArray[v][u] == 0) { // unlabeled - new inner
														// contour
							Contour ic = traceInnerContour(u - 1, v, label);
							innerContours.add(ic);

						}
						label = 0;
					}
				}
			}
		}
		// shift back to original coordinates
		Contour.moveContoursBy(outerContours, -1, -1);
		Contour.moveContoursBy(innerContours, -1, -1);

	}

}
