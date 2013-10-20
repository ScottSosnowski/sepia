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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.cwru.sepia.action.Action;
/**
 * Logs the s for a single player.
 * @author The Condor
 *
 */
public class ActionLogger implements Serializable {
	private static final long	serialVersionUID	= 1L;
	
	private List<Map<Integer, Action>> actions;
	
	public ActionLogger () {
		actions = new ArrayList<Map<Integer, Action>>();
	}
	/**
	 * Insert an action , overwriting any Action for the same unit in the same round.
	 * <br>
	 * @param stepNumber
	 * @param action
	 */
	public void addAction(int stepNumber, Action action) {
		addAction(stepNumber,action.getUnitId(),action);
	}
	/**
	 * Insert an action , overwriting any Action for the same unit id in the same round.
	 * @param stepNumber
	 * @param unitID
	 * @param action
	 */
	public void addAction(int stepNumber, int unitID, Action action) {
		while (actions.size()<=stepNumber)
		{
			actions.add(new HashMap<Integer, Action>());
		}
		actions.get(stepNumber).put(unitID,action);
	}
	/**
	 * Get the s of actions for a specific round.
	 * @param roundnumber
	 * @return an unmodifiable list of Actions
	 */
	public Map<Integer, Action> getActions(int roundnumber) {
		if ( roundnumber<0 || roundnumber >= actions.size()) {
			return new HashMap<Integer, Action>();
		}
		else {
			return Collections.unmodifiableMap(actions.get(roundnumber));
		}
	}
	/**
	 * Get the number of the highest round for which this logger has recorded data.
	 * @return The highest recorded round
	 */
	public int getHighestRound() {
		return actions.size()-1;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int  product= 1;
		 product= prime *  + ((actions == null) ? 0 : actions.hashCode());
		return product;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActionLogger other = (ActionLogger) obj;
		if (actions == null) {
			if (other.actions != null)
				return false;
		} else if (!actions.equals(other.actions))
			return false;
		return true;
	}
}
