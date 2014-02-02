package edu.cwru.sepia.util;

/**
 * Useful for calculating distances between objects that take up
 * multiple tiles. java.awt.Rectangle does a lot of AWT-related stuff
 * that we don't need.
 * @author tim
 *
 */
public class Rectangle {
	private int tlx, tly, width, height;

	public Rectangle(int tlx, int tly, int width, int height) {
		this.tlx = tlx;
		this.tly = tly;
		this.width = width;
		this.height = height;
	}
	
	public Rectangle(int tlx, int tly) {
		this(tlx, tly, 1, 1);
	}

	public int getLeft() {
		return tlx;
	}

	public int getTop() {
		return tly;
	}
	
	public int getRight() {
		return tlx + width - 1;
	}
	
	public int getBottom() {
		return tly + height - 1;
	}

	public int distanceTo(Rectangle r2) {
		Rectangle r1 = this;
		int xDistance = 0;
		int rightToLeft = r1.getRight() - r2.getLeft();
		int leftToRight = r1.getLeft() - r2.getRight();
		if(leftToRight > 0) {
			xDistance = leftToRight;
		} else if (rightToLeft < 0) {
			xDistance = -rightToLeft;
		}

		int yDistance = 0;
		int topToBottom = r1.getTop() - r2.getBottom();
		int bottomToTop = r1.getBottom() - r2.getTop();
		if(topToBottom > 0) {
			yDistance = topToBottom;
		} else if(bottomToTop < 0) {
			yDistance = -bottomToTop;
		}
		
		return Math.max(xDistance, yDistance);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + height;
		result = prime * result + tlx;
		result = prime * result + tly;
		result = prime * result + width;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rectangle other = (Rectangle)obj;
		if (height != other.height)
			return false;
		if (tlx != other.tlx)
			return false;
		if (tly != other.tly)
			return false;
		if (width != other.width)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Rectangle [tlx=" + tlx + ", tly=" + tly + ", width=" + width + ", height=" + height + "]";
	}
	
}
