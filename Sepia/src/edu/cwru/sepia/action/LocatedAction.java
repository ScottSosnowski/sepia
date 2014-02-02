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
package edu.cwru.sepia.action;

/**
 * A sub-type of {@code Action} that includes CompoundMove. In addition to the
 * base attributes found in Action, this class also includes coordinates that
 * represent the destination of the action. For actions that only target
 * adjacent tiles. see {@code DirectedAction}. For actions that target a
 * specific unit, see @ TargetedAction} .
 * 
 */
public class LocatedAction extends Action {
	private static final long serialVersionUID = 1L;

	protected final int x;
	protected final int y;

	public LocatedAction(int unitid, ActionType type, int x, int y) {
		super(unitid, type);
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public String toString() {
		return "LocatedAction [x=" + x + ", y=" + y + ", type=" + type + ", unitId=" + unitId + "]";
	}

	@Override
	public boolean equals(Object other) {
		if(this == other) {
			return true;
		} else if(other == null || !this.getClass().equals(other.getClass())) {
			return false;
		} else {

			LocatedAction aother = (LocatedAction)other;
			return aother.type == type && aother.unitId == unitId && aother.x == x && aother.y == y;
		}
	}

	@Override
	public int hashCode() {
		int prime = 61;
		return prime * prime * prime * x + prime * prime * y + prime * type.hashCode() + unitId;
	}
}
