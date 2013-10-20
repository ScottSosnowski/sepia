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
package edu.cwru.sepia.model.state;

import java.util.ArrayList;
import java.util.List;

import edu.cwru.sepia.util.Pair;

/**
 * A basic enumeration of the eight directions in which a primitive action can
 * be taken. Directions are treated the same as screen coordinates: East is
 * positive x, South is positive y.
 * 
 * @author Tim
 * 
 */
public enum Direction {
	NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST;

	/**
	 * @return x component of the direction: -1, 0, 1
	 */
	public int xComponent() {
		switch(this) {
			case NORTHEAST:
			case EAST:
			case SOUTHEAST:
				return 1;
			case NORTHWEST:
			case WEST:
			case SOUTHWEST:
				return -1;
			default:
				return 0;
		}
	}

	/**
	 * @return y component of the direction: -1, 0, 1
	 */
	public int yComponent() {
		switch(this) {
			case NORTHWEST:
			case NORTH:
			case NORTHEAST:
				return -1;
			case SOUTHWEST:
			case SOUTH:
			case SOUTHEAST:
				return 1;
			default:
				return 0;
		}
	}

	public List<Pair<Integer, Integer>> adjacentTiles(int tlx, int tly, int width, int height) {
		List<Pair<Integer, Integer>> tiles = new ArrayList<>();
		switch(this) {
			case NORTHWEST:
				tiles.add(new Pair<>(tlx - 1, tly - 1));
				break;
			case NORTHEAST:
				tiles.add(new Pair<>(tlx + width, tly - 1));
				break;
			case SOUTHEAST:
				tiles.add(new Pair<>(tlx + width, tly + height));
				break;
			case SOUTHWEST:
				tiles.add(new Pair<>(tlx - 1, tly + height));
				break;
			case NORTH:
				for(int i = tlx; i < tlx + width; i++)
					tiles.add(new Pair<>(i, tly - 1));
				break;
			case SOUTH:
				for(int i = tlx; i < tlx + width; i++)
					tiles.add(new Pair<>(i, tly + height));
				break;
			case WEST:
				for(int j = tly; j < tly + height; j++)
					tiles.add(new Pair<>(tlx - 1, j));
				break;
			case EAST:
				for(int j = tly; j < tly + height; j++)
					tiles.add(new Pair<>(tlx + width, j));
		}
		return tiles;
	}

	/**
	 * Get a direction that represents the given change in coordinates.
	 * Secondary directions (NE, SE, NW, SW) only apply to diagonals;
	 * any situations where the absolute values of x and y are not equal
	 * will result in cardinal directions. 
	 * 
	 * Returns null for (0,0). 
	 * @param x
	 * @param y
	 * @return
	 */
	public static Direction getDirection(int x, int y) {
		if(Math.abs(x) > Math.abs(y)) {//x is non-zero and > y
			if(x > 0) {
				return EAST;
			} else {
				return WEST;
			} 
		} else if(Math.abs(x) < Math.abs(y)) {//y is non-zero and > x
			if(y > 0) {
				return SOUTH;
			} else {
				return NORTH;
			}
		} else {//diagonal
			if(x < 0 && y < 0) {
				return NORTHWEST;
			} else if(x > 0 && y < 0) {
				return NORTHEAST;
			} else if(x > 0 && y > 0) {
				return SOUTHEAST;
			} else if(x < 0 && y > 0) {
				return SOUTHWEST;
			} else {
				return null;//0,0
			}
		}
	}
}
