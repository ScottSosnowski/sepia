/**
 *  Strategy Engine for Programming Intelligent Agents (SEPIA)
    Copyright (C) 2012 Case Western Reserve University

    This file is part of SEPIA.

    SEPIA is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    SEPIA is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with SEPIA.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.cwru.sepia.util;

import edu.cwru.sepia.model.state.Unit;

public final class DistanceMetrics {

	private DistanceMetrics(){}
	
	public static double euclideanDistance(int x1, int y1, int x2, int y2) {
		return Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
	}
	/**
	 * The core distance metric used by sepia for attacks and such
	 */
	public static int chebyshevDistance(int x1, int y1, int x2, int y2) {
		return Math.max(Math.abs(x1-x2), Math.abs(y1-y2));
	}
	

	/**
	 * The core distance metric used by sepia for attacks and such
	 * @param testUnit
	 * @param unit
	 * @return
	 */
	public static int chebyshevDistance(Unit unit1, Unit unit2) {
		return DistanceMetrics.chebyshevDistance(unit1.getXPosition(), unit1.getYPosition(), unit2.getXPosition(), unit2.getYPosition());
	}
	
	/**
	 * Calculates distance from a rectangle (defined by the top left point and dimensions) to a point.
	 * Assumes that dimensions include top left and go to top left + dimension - 1.
	 * @param tlx
	 * @param tly
	 * @param width
	 * @param height
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static int chebyshevDistance(int tlx, int tly, int width, int height, int x2, int y2) {
		int x1 = closest(tlx, tlx + width - 1, x2);
		int y1 = closest(tly, tly + height - 1, y2);
		return chebyshevDistance(x1, y1, x2, y2);
	}
	
	private static int closest(int left, int right, int target) {
		if(target < left)
			return left;
		if(target > right)
			return right;
		return target;
	}
}
