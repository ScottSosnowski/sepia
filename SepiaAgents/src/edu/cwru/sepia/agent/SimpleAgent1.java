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
package edu.cwru.sepia.agent;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.action.ActionType;
import edu.cwru.sepia.action.DirectedAction;
import edu.cwru.sepia.action.TargetedAction;
import edu.cwru.sepia.model.history.History;
import edu.cwru.sepia.model.state.Direction;
import edu.cwru.sepia.model.state.State.StateView;
import edu.cwru.sepia.model.state.Unit.UnitView;
/**
 * A simple agent that makes all its units move in random directions if they are not attacking.
 * Will attack any enemy within sight range.
 * @author Tim
 *
 */
public class SimpleAgent1 extends Agent {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(SimpleAgent1.class.getCanonicalName());

	public SimpleAgent1(int playernum) {
		super(playernum);
		
	}

	StateView currentState;
	
	@Override
	public Collection<Action> initialStep(StateView newstate, History.HistoryView statehistory) {		
		return middleStep(newstate, statehistory);
	}

	@Override
	public Collection<Action> middleStep(StateView newState, History.HistoryView statehistory) {
		Collection<Action> builder = new HashSet<Action>();
		currentState = newState;
		List<Integer> unitIds = currentState.getUnitIds(playernum);
		for(int unitId : unitIds)
		{
			UnitView u = currentState.getUnit(unitId);
			int sightRange = u.getTemplateView().getSightRange();
			int target = -1;
			for(int enemy : currentState.getAllUnitIds())
			{
				UnitView v = currentState.getUnit(enemy);
				if (v.getTemplateView().getPlayer() == playernum)
					continue;
				double distance = u.getBounds().distanceTo(v.getBounds());
				if(distance <= sightRange)
				{
					target = enemy;
					break;
				}						
			}
			if(target >= 0)
			{
				Action a = new TargetedAction(unitId, ActionType.COMPOUNDATTACK, target);
				logger.fine("Adding action "+a.toString());
				builder.add(a);
			}
			else
			{
				int dir = (int)(Math.random()*8);
				Action a = new DirectedAction(unitId, ActionType.PRIMITIVEMOVE, Direction.values()[dir]);
				logger.fine("Adding action "+a.toString());
				builder.add(a);
			}
		}
		return builder;
	}

	@Override
	public void terminalStep(StateView newstate, History.HistoryView statehistory) {
	}

	public static String getUsage() {
		return "None";
	}
	@Override
	public void savePlayerData(OutputStream os) {
		//this agent lacks learning and so has nothing to persist.
		
	}
	@Override
	public void loadPlayerData(InputStream is) {
		//this agent lacks learning and so has nothing to persist.
	}
}
