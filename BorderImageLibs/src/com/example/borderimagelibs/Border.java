package com.example.borderimagelibs;

import java.util.List;

import android.graphics.Color;
import android.graphics.Path;

public class Border {
	// borderSize From 0-50
	private float borderSize = 12;
	private int color;
	private boolean isGetPath;
	private Path[] outerPath;
	private Path[] innerPath;
	private List<Contour> outerContours;
	private List<Contour> innerContours;

	public Border() {
		borderSize = 12;
		color = Color.RED;
	}

	public float getBorderSize() {
		return borderSize;
	}

	public void setBorderSize(float borderSize) {
		this.borderSize = borderSize;
	}

	public boolean isGetPath() {
		return isGetPath;
	}

	public void setGetPath(boolean isGetPath) {
		this.isGetPath = isGetPath;
	}

	public Path[] getOuterPath() {
		return outerPath;
	}

	public void setOuterPath(Path[] outerPath) {
		this.outerPath = outerPath;
	}

	public Path[] getInnerPath() {
		return innerPath;
	}

	public void setInnerPath(Path[] innerPath) {
		this.innerPath = innerPath;
	}

	public List<Contour> getOuterContours() {
		return outerContours;
	}

	public void setOuterContours(List<Contour> outerContours) {
		this.outerContours = outerContours;
	}

	public List<Contour> getInnerContours() {
		return innerContours;
	}

	public void setInnerContours(List<Contour> innerContours) {
		this.innerContours = innerContours;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}
}
