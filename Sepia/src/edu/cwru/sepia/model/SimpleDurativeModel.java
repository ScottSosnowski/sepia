package edu.cwru.sepia.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.configuration.Configuration;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.action.ActionQueue;
import edu.cwru.sepia.action.ActionResult;
import edu.cwru.sepia.action.ActionResultType;
import edu.cwru.sepia.action.ActionType;
import edu.cwru.sepia.model.state.ResourceNode;
import edu.cwru.sepia.model.state.ResourceType;
import edu.cwru.sepia.model.state.State;
import edu.cwru.sepia.model.state.StateCreator;
import edu.cwru.sepia.model.state.Unit;
import edu.cwru.sepia.model.state.UnitTemplate;
import edu.cwru.sepia.util.Pair;

public class SimpleDurativeModel extends AbstractDurativeModel {
	private static final long serialVersionUID = 1L;

	private List<Integer> playerOrder;
	
	public SimpleDurativeModel(State init, StateCreator restartTactic, Configuration configuration) {
		super(init, restartTactic, configuration, Logger.getLogger(SimpleDurativeModel.class.getCanonicalName()));
		playerOrder = new ArrayList<Integer>();
		for(Integer playerId : init.getPlayers())
			playerOrder.add(playerId);
	}

	@Override
	public void executeStep() {
		if(state.getTurnNumber() == 0) 
			Collections.shuffle(playerOrder);
		
		for(Integer playerId : playerOrder) {
			executePlayerStep(queuedActions.get(playerId));
		}
	}

	private void executePlayerStep(HashMap<Integer, ActionQueue> actions) {
		Set<Integer> toRemove = new HashSet<Integer>();
		for(Integer unitId : actions.keySet()) {
			ActionQueue actionQueue = actions.get(unitId);
			boolean inProgress = executeUnitStep(state.getUnit(unitId), actionQueue);
			if(!inProgress)
				toRemove.add(unitId);
		}
		for(Integer unitId : toRemove) {
			actions.remove(unitId);
		}
	}
	
	/**
	 * 
	 * @param unit
	 * @param queue
	 * @return - whether there is more to do in the action queue
	 */
	private boolean executeUnitStep(Unit unit, ActionQueue queue) {
		int progress = unit.getActionProgressAmount();
		Action currentAction = unit.getActionProgressPrimitive();
		int timeCost = getTimeCost(unit, currentAction);
		if(timeCost < 0) {
			logger.warning("Unexpected action in action queue " + currentAction);
			unit.setDurativeStatus(currentAction, -1);
			state.unreserveSpace(unit);
			return false;
		}
		if(progress + 1 < timeCost) {
			unit.setDurativeStatus(currentAction, progress + 1);
			return true;
		}
		
		completeAction(unit, queue);
		return cueUpNextAction(unit, queue);
	}
	
	private void completeAction(Unit unit, ActionQueue queue) {
		state.unreserveSpace(unit);
		Action a = queue.popPrimitive();
		Unit u = state.getUnit(a.getUnitId());			
		if (u == null)
			return;
		
		FailureMode result = doAction(a, u);

		if (result == FailureMode.SUCCESS) {
			if (!queue.hasNext()) {
				if (logger.isLoggable(Level.FINE))
					logger.fine("Unit " + u.id + " has completed all actions in its queue");
				history.recordCommandFeedback(u.getPlayer(), state.getTurnNumber(), new ActionResult(queue.getFullAction(),ActionResultType.COMPLETED));
				history.recordPrimitiveFeedback(u.getPlayer(), state.getTurnNumber(), new ActionResult(a,ActionResultType.COMPLETED));
				queuedActions.remove(queue.getFullAction());
			} else {
				if (logger.isLoggable(Level.FINE))
					logger.fine("Unit " + u.id + " has completed a primitive action and has more in its queue");
				history.recordCommandFeedback(u.getPlayer(), state.getTurnNumber(), new ActionResult(queue.getFullAction(),ActionResultType.INCOMPLETE));
				history.recordPrimitiveFeedback(u.getPlayer(), state.getTurnNumber(), new ActionResult(a,ActionResultType.COMPLETED));
			}
		} else if (result == FailureMode.WRONG_ACTION_TYPE) {
			//if it had the wrong type, then either the planner is bugged (unlikely) or the user provided a bad primitive action
			//either way, record it as failed and toss it
			history.recordCommandFeedback(u.getPlayer(), state.getTurnNumber(), 
										  new ActionResult(queue.getFullAction(), ActionResultType.INVALIDTYPE));
			queuedActions.remove(queue.getFullAction());
		} else if (a.getType() == ActionType.FAILEDPERMANENTLY || queue.getFullAction().getType().isPrimitive()) {
			if (logger.isLoggable(Level.FINE))
				logger.fine("Unit " + u.id + " has permanently failed its current primitive action");
			history.recordCommandFeedback(u.getPlayer(), state.getTurnNumber(), new ActionResult(queue.getFullAction(),ActionResultType.FAILED));
			history.recordPrimitiveFeedback(u.getPlayer(), state.getTurnNumber(), new ActionResult(a,ActionResultType.FAILED));
			queuedActions.remove(queue.getFullAction());
		} else {//just a normal failure; will retry
			queue.resetPrimitives(calculatePrimitives(queue.getFullAction()));
			history.recordCommandFeedback(u.getPlayer(), state.getTurnNumber(), new ActionResult(queue.getFullAction(),ActionResultType.INCOMPLETEMAYBESTUCK));
			history.recordPrimitiveFeedback(u.getPlayer(), state.getTurnNumber(), new ActionResult(a,ActionResultType.FAILED));
		}
	}
	
	/**
	 * 
	 * @param unit
	 * @param queue
	 * @return - whether there is another action to perform
	 */
	private boolean cueUpNextAction(Unit unit, ActionQueue queue) {
		Action nextAction = queue.peekPrimitive();
		unit.setDurativeStatus(nextAction, 0);
		if(nextAction == null)
			return false;

		for(int tries = 0; tries < 2; tries++) {
			Pair<Integer,Integer> destination = getDestination(nextAction, unit);
			int x = destination.a;
			int y = destination.b;
			if(!state.inBounds(x, y) || state.unitAt(x, y) != null || state.resourceAt(x, y) != null || 
				state.unitReservingSpace(destination) != null) {
				if(tries == 0)
					queue.resetPrimitives(calculatePrimitives(queue.getFullAction()));
				else {
					history.recordCommandFeedback(unit.getPlayer(), state.getTurnNumber(), new ActionResult(queue.getFullAction(), ActionResultType.FAILED));
					history.recordPrimitiveFeedback(unit.getPlayer(), state.getTurnNumber(), new ActionResult(queue.peekPrimitive(), ActionResultType.FAILED));
					return false;
				}
			}
		}
		return true;
	}
	
	private int getTimeCost(Unit unit, Action action) {
		UnitTemplate template = unit.getTemplate();
		switch(action.getType()) 
		{
			case PRIMITIVEMOVE:
				return template.getDurationMove();
			case PRIMITIVEGATHER:
				ResourceType type = getTargetResourceType(unit, action);
				if(type == null)
					return -1;
				return template.getGatherDuration(type);
			case PRIMITIVEDEPOSIT:
				return template.getDurationDeposit();
			case PRIMITIVEATTACK:
				return template.getDurationAttack();
			case PRIMITIVEBUILD:
			case PRIMITIVEPRODUCE:
				return 1;
			default:
				return -1;
		}
	}
	
	private ResourceType getTargetResourceType(Unit unit, Action action) {
		Pair<Integer, Integer> destination = getDestination(action, unit);
		ResourceNode resource = state.resourceAt(destination.a, destination.b);
		if(resource == null)
			return null;
		return resource.getResourceType();
	}
}
