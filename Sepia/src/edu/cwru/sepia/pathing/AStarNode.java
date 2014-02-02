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
package edu.cwru.sepia.pathing;

import edu.cwru.sepia.model.state.Direction;
import edu.cwru.sepia.util.Rectangle;

/**
 * Note: compareTo and equals are not equivalently implemented equals checks xs
 * and ys, but no other fields
 * 
 */
public class AStarNode implements Comparable<AStarNode> {
	private final int x;
	private final int y;
	private final int distanceFromRoot; // the distance from the first node
	private final int value; // the distance from the start + the heuristic value
	private final Direction directionFromPrevious;
	private final int durativeSteps;

	private final AStarNode previous;

	/**
	 * Create a new root node
	 * 
	 * @param x
	 * @param y
	 * @param heuristic
	 */
	public AStarNode(int x, int y, int heuristic) {
		this(x, y, heuristic, null, null, -1);
	}
	
	/**
	 * Create a non-root node with fields for backtracking.
	 * 
	 * @param x
	 * @param y
	 * @param heuristic
	 * @param previous
	 * @param directionfromprevious
	 */
	public AStarNode(int x, int y, int heuristic, AStarNode previous, Direction directionfromprevious) {
		this(x, y, heuristic, previous, directionfromprevious, 1);
	}

	/**
	 * Create a non-root node with fields for backtracking.
	 * 
	 * @param x
	 * @param y
	 * @param heuristic
	 * @param previous
	 * @param directionfromprevious
	 * @param durativesteps
	 */
	public AStarNode(int x, int y, int heuristic, AStarNode previous, Direction directionfromprevious,
			int durativesteps) {
		this.x = x;
		this.y = y;
		if(previous != null)
			this.distanceFromRoot = previous.getDistanceFromStart() + durativesteps;
		else
			this.distanceFromRoot = 0;
		this.value = distanceFromRoot + heuristic;
		this.directionFromPrevious = directionfromprevious;
		this.previous = previous;
		this.durativeSteps = durativesteps;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Rectangle asRectangle() {
		return new Rectangle(x, y, 1, 1);
	}

	public Rectangle asRectangle(int width, int height) {
		return new Rectangle(x, y, width, height);
	}
	
	public int getDistanceFromStart() {
		return distanceFromRoot;
	}
	
	public AStarNode getPrevious() {
		return previous;
	}
	
	public Direction getDirectionFromPrevious() {
		return directionFromPrevious;
	}

	public int getDurativeSteps() {
		return durativeSteps;
	}
	
	public int compareTo(AStarNode other) {
		AStarNode o = (AStarNode)other;
		int diff = value - o.value;
		if(diff != 0)
			return diff;
		diff = distanceFromRoot - o.distanceFromRoot;
		if(diff != 0)
			return diff;
		diff = x - o.x;
		if(diff != 0)
			return diff;
		return y - o.y;
	}
	//TODO - get hashCode and equals to match compareTo (not sure if this will break older planners
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		AStarNode other = (AStarNode)obj;
		if(x != other.x)
			return false;
		if(y != other.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AStarNode [x=" + x + ", y=" + y + ", distanceFromRoot=" + distanceFromRoot + ", value=" + value + ", directionfromprevious="
				+ directionFromPrevious + ", duration=" + durativeSteps + "]";
	}

}
