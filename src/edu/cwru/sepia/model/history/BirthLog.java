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
package edu.cwru.sepia.model.history;

import java.io.Serializable;

import edu.cwru.sepia.util.DeepEquatable;

/**
 * A read only class that represents the birth of something
 * 
 */
public class BirthLog implements Serializable, DeepEquatable {
	private static final long	serialVersionUID	= 1L;
	private int unitid;
	private int player;
	private int parent;
	public BirthLog(int newunitid, int producerid, int player) {
		unitid=newunitid;
		this.player = player;
		this.parent = producerid;
	}
	public int getNewUnitID() {
		return unitid;
	}
	public int getController() {
		return player;
	}
	public int getParentID() {
		return parent;
	}
	@Override public boolean equals(Object other) {
		if (other == null || !this.getClass().equals(other.getClass()))
			return false;
		BirthLog o = (BirthLog)other;
		if (unitid != o.unitid)
			return false;
		if (player != o.player)
			return false;
		if (parent != o.parent)
			return false;
		return true;
	}
	@Override public int hashCode() {
		int product = 1;
		int sum = 0;
		int prime = 31;
		sum += (product = product*prime)*unitid;
		sum += (product = product*prime)*player;
		sum += (product = product*prime)*parent;
		return sum;
	}
	@Override public boolean deepEquals(Object other) {
		return equals(other);
	}
}
