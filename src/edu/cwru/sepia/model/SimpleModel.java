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
package edu.cwru.sepia.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.configuration.Configuration;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.action.ActionQueue;
import edu.cwru.sepia.action.ActionResult;
import edu.cwru.sepia.action.ActionResultType;
import edu.cwru.sepia.action.ActionType;
import edu.cwru.sepia.action.LocatedAction;
import edu.cwru.sepia.action.LocatedProductionAction;
import edu.cwru.sepia.action.ProductionAction;
import edu.cwru.sepia.action.TargetedAction;
import edu.cwru.sepia.environment.TurnTracker;
import edu.cwru.sepia.model.history.History;
import edu.cwru.sepia.model.state.State;
import edu.cwru.sepia.model.state.StateCreator;
import edu.cwru.sepia.model.state.Template;
import edu.cwru.sepia.model.state.Unit;
import edu.cwru.sepia.model.state.UnitTemplate;
import edu.cwru.sepia.pathing.SimplePlanner;
import edu.cwru.sepia.util.Pair;
import edu.cwru.sepia.util.SerializerUtil;
/**
 * 
 * A "Simple" Model.
 * <br>This model is sequential, processing most actions one at a time, and resolves conflicts in this manner.
 * <br>This model assumes players don't take turns and primitive actions take exactly one step to complete, and will disregard all evidence to the contrary.
 * 
 * 
 */
public class SimpleModel extends AbstractModel {
	private static final long serialVersionUID = -8289868580233478749L;
	
	private HashMap<Unit, ActionQueue> queuedActions;
	private SimplePlanner planner;
	
	public SimpleModel(State init, StateCreator restartTactic, Configuration configuration) {
		super(init, restartTactic, configuration, Logger.getLogger(SimpleModel.class.getCanonicalName()));
		planner = new SimplePlanner(init);
		queuedActions = new HashMap<Unit, ActionQueue>();
	}
	
	@Override
	public void createNewWorld() {
		state = restartTactic.createState();
		history = new History();
		for (Integer i : state.getPlayers())
			history.addPlayer(i);
		queuedActions = new HashMap<Unit, ActionQueue>();
		planner = new SimplePlanner(state);
	}
	
	@Override
	public void addActions(Collection<Action> actions, int sendingPlayerNumber) {
		for (Action a : actions) {
			int unitId = a.getUnitId();
			history.recordCommandRecieved(sendingPlayerNumber, state.getTurnNumber(), unitId, a);
			if (a.getUnitId() != unitId) {
				if(logger.isLoggable(Level.FINE)) 
					logger.fine("Rejecting submitted action because key did not match action's unit ID: " + a);
				history.recordCommandFeedback(sendingPlayerNumber, state.getTurnNumber(), new ActionResult(a,ActionResultType.INVALIDUNIT));
				continue;
			} else if (state.getUnit(unitId) == null) {
				if(logger.isLoggable(Level.FINE)) 
					logger.fine("Rejecting submitted action because unit " + unitId + " does not exist");
				history.recordCommandFeedback(sendingPlayerNumber, state.getTurnNumber(), new ActionResult(a,ActionResultType.INVALIDUNIT));
				continue;
			} else if(state.getUnit(unitId).getPlayer() != sendingPlayerNumber) {
				if(logger.isLoggable(Level.FINE))
					logger.fine("Rejecting submitted action because player does not control unit: " + a);
				history.recordCommandFeedback(sendingPlayerNumber, state.getTurnNumber(), new ActionResult(a,ActionResultType.INVALIDCONTROLLER));
				continue;
			} else {
				if(logger.isLoggable(Level.FINE))
					logger.fine("Action submitted successfully: " + a);
				Unit actor = state.getUnit(unitId);
				ActionQueue queue = new ActionQueue(a, calculatePrimitives(a));
				queuedActions.put(actor, queue);
			}
		}
	}
	private LinkedList<Action> calculatePrimitives(Action action) {
		LinkedList<Action> primitives = null;
		Unit actor = state.getUnit(action.getUnitId());
		switch (action.getType()) {
			case PRIMITIVEMOVE:
			case PRIMITIVEATTACK:
			case PRIMITIVEGATHER:
			case PRIMITIVEDEPOSIT:
			case PRIMITIVEBUILD:
			case PRIMITIVEPRODUCE:
			case FAILED:
				//The only primitive action needed to execute a primitive action is itself
				primitives = new LinkedList<Action>();
				primitives.add(action);
				break;
			case COMPOUNDMOVE:
				LocatedAction aMove = (LocatedAction)action;
				primitives = planner.planMove(actor, aMove.getX(), aMove.getY());
				break;
			case COMPOUNDGATHER:
				TargetedAction aGather = (TargetedAction)action;
				int resourceId = aGather.getTargetId();
				primitives = planner.planGather(actor, state.getResource(resourceId));
				break;
			case COMPOUNDATTACK:
				TargetedAction aAttack = (TargetedAction)action;
				int targetId = aAttack.getTargetId();
				primitives = planner.planAttack(actor, state.getUnit(targetId));
				break;
			case COMPOUNDPRODUCE:
				ProductionAction aProduce = (ProductionAction)action;
				int unitTemplateId = aProduce.getTemplateId();
				primitives = planner.planProduce(actor, (UnitTemplate)state.getTemplate(unitTemplateId));
				break;
			case COMPOUNDBUILD:
				LocatedProductionAction aBuild = (LocatedProductionAction)action;
				int buildTemplateId = aBuild.getTemplateId();
				primitives = planner.planBuild(actor, aBuild.getX(), aBuild.getY(), (UnitTemplate)state.getTemplate(buildTemplateId));
				break;
			case COMPOUNDDEPOSIT:
				TargetedAction aDeposit = (TargetedAction)action;
				int depotId = aDeposit.getTargetId();
				primitives = planner.planDeposit(actor, state.getUnit(depotId));
				break;
			default:
				primitives = null;
			
		}
		if(logger.isLoggable(Level.FINER))
			logger.finer("Action " + action + " was turned into the following list of primitives: " + primitives);
		return primitives;
	}
	
	@Override
	public void executeStep() {
		
		//Set each agent to have no task
		for (Unit u : state.getUnits().values()) {
			u.deprecateOldView();
		}
		//Set each template to not keep the old view
		for (Integer player : state.getPlayers()) {
			for (@SuppressWarnings("rawtypes") Template t : state.getTemplates(player).values()) {
				t.deprecateOldView();
			}
		}
		
		//Run the Action
		for(ActionQueue queuedAct : queuedActions.values()) {
			if (logger.isLoggable(Level.FINE))
				logger.fine("Doing full action: "+queuedAct.getFullAction());
			//Pull out the primitive
			if (!queuedAct.hasNext()) 
				continue;
			Action a = queuedAct.popPrimitive();
			if (logger.isLoggable(Level.FINE))
				logger.fine("Doing primitive action: "+a);
			//Execute it
			Unit u = state.getUnit(a.getUnitId());			
			if (u == null)
				continue;
			//Set the tasks and grab the common features
			int x = u.getxPosition();
			int y = u.getyPosition();
			
			Pair<Integer,Integer> destination = getDestination(a, x, y);
			int xPrime = destination.a;
			int yPrime = destination.b;

			if (logger.isLoggable(Level.FINE))
				logger.fine("Action is from " + x + "," + y + " to " + xPrime + "," + yPrime);
			
			//Gather the last of the information and actually execute the actions
			int timesTried=0;
			FailureMode result = null;
			boolean fullIsPrimitive = a.getType().isPrimitive();
			/*recalculate and try again once if it has failed, so long as the full action 
			  is not primitive (since primitives will recalculate to the same as before) 
			  and not the wrong type (since something is wrong if it is the wrong type)*/
			do {
				timesTried++;
				
				result = doAction(a, u, x, y, xPrime, yPrime);
				
				if (result == FailureMode.SUCCESS) {
					if (!queuedAct.hasNext()) {
						if (logger.isLoggable(Level.FINE))
							logger.fine("Unit " + u.id + " has completed all actions in its queue");
						history.recordCommandFeedback(u.getPlayer(), state.getTurnNumber(), new ActionResult(queuedAct.getFullAction(),ActionResultType.COMPLETED));
						history.recordPrimitiveFeedback(u.getPlayer(), state.getTurnNumber(), new ActionResult(a,ActionResultType.COMPLETED));
						queuedActions.remove(queuedAct.getFullAction());
					} else {
						if (logger.isLoggable(Level.FINE))
							logger.fine("Unit " + u.id + " has completed a primitive action and has more in its queue");
						history.recordCommandFeedback(u.getPlayer(), state.getTurnNumber(), new ActionResult(queuedAct.getFullAction(),ActionResultType.INCOMPLETE));
						history.recordPrimitiveFeedback(u.getPlayer(), state.getTurnNumber(), new ActionResult(a,ActionResultType.COMPLETED));
					}
				} else if (result == FailureMode.WRONG_ACTION_TYPE) {
					//if it had the wrong type, then either the planner is bugged (unlikely) or the user provided a bad primitive action
					//either way, record it as failed and toss it
					history.recordCommandFeedback(u.getPlayer(), state.getTurnNumber(), 
												  new ActionResult(queuedAct.getFullAction(), ActionResultType.INVALIDTYPE));
					queuedActions.remove(queuedAct.getFullAction());
				} else if (a.getType() == ActionType.FAILEDPERMANENTLY || result == FailureMode.FAILED_ATTEMPT && fullIsPrimitive) {
					if (logger.isLoggable(Level.FINE))
						logger.fine("Unit " + u.id + " has permanently failed its current primitive action");
					history.recordCommandFeedback(u.getPlayer(), state.getTurnNumber(), new ActionResult(queuedAct.getFullAction(),ActionResultType.FAILED));
					history.recordPrimitiveFeedback(u.getPlayer(), state.getTurnNumber(), new ActionResult(a,ActionResultType.FAILED));
					queuedActions.remove(queuedAct.getFullAction());
				} else {//just a normal failure; will retry
					queuedAct.resetPrimitives(calculatePrimitives(queuedAct.getFullAction()));
					history.recordCommandFeedback(u.getPlayer(), state.getTurnNumber(), new ActionResult(queuedAct.getFullAction(),ActionResultType.INCOMPLETEMAYBESTUCK));
					history.recordPrimitiveFeedback(u.getPlayer(), state.getTurnNumber(), new ActionResult(a,ActionResultType.FAILED));
				}
			} while (timesTried < 2 && !fullIsPrimitive && result != FailureMode.SUCCESS && result != FailureMode.WRONG_ACTION_TYPE);
		}
		
		performCleanup();
	}

	public void save(String filename) {
		SerializerUtil.storeState(filename, state);
	}

	@Override
	public void setTurnTracker(TurnTracker turnTracker) {
		//This method does nothing, SimpleModel ignores turns
	}
	
}
