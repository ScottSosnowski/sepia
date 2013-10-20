package edu.cwru.sepia.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Logger;

import org.apache.commons.configuration.Configuration;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.action.ActionQueue;
import edu.cwru.sepia.action.ActionResult;
import edu.cwru.sepia.action.ActionResultType;
import edu.cwru.sepia.action.LocatedAction;
import edu.cwru.sepia.action.LocatedProductionAction;
import edu.cwru.sepia.action.TargetedAction;
import edu.cwru.sepia.environment.TurnTracker;
import edu.cwru.sepia.model.history.History;
import edu.cwru.sepia.model.state.State;
import edu.cwru.sepia.model.state.StateCreator;
import edu.cwru.sepia.model.state.Unit;
import edu.cwru.sepia.model.state.UnitTemplate;
import edu.cwru.sepia.pathing.DurativePlanner;

public abstract class AbstractDurativeModel extends AbstractModel {
	private static final long serialVersionUID = 1L;

	protected DurativePlanner planner;
	protected HashMap<Integer, HashMap<Integer, ActionQueue>> queuedActions;
	protected TurnTracker turnTracker;
	
	public AbstractDurativeModel(State init, StateCreator restartTactic, Configuration configuration, Logger logger) {
		super(init, restartTactic, configuration, logger);
		planner = new DurativePlanner(init);
		queuedActions = new HashMap<Integer, HashMap<Integer, ActionQueue>>();
	}

	@Override
	public void setTurnTracker(TurnTracker turnTracker) {
		this.turnTracker = turnTracker;
	}

	@Override
	public void createNewWorld() {
		state = restartTactic.createState();
		history = new History();
		queuedActions = new HashMap<Integer,HashMap<Integer, ActionQueue>>();
		for (Integer i : state.getPlayers())
		{
			history.addPlayer(i);
			queuedActions.put(i, new HashMap<Integer,ActionQueue>());
		}
		
		planner = new DurativePlanner(state);
	}
	

	@Override
	public void addActions(Collection<Action> actions, int sendingPlayerNumber) {
		for (Action a : actions) {
			
			int unitId = a.getUnitId();
			history.recordCommandRecieved(sendingPlayerNumber, state.getTurnNumber(), unitId, a);
			//If the unit does not exist, ignore the action
			if (state.getUnit(unitId) == null) {
				history.recordCommandFeedback(sendingPlayerNumber, state.getTurnNumber(), new ActionResult(a,ActionResultType.INVALIDUNIT));
				continue;
			} else if(state.getUnit(unitId).getPlayer() != sendingPlayerNumber) {
				//If the unit is not the player's, ignore the action
				history.recordCommandFeedback(sendingPlayerNumber, state.getTurnNumber(), new ActionResult(a,ActionResultType.INVALIDCONTROLLER));
				continue;
			} else {//Valid
				ActionQueue queue = new ActionQueue(a, calculatePrimitives(a));
				ActionQueue original = queuedActions.get(sendingPlayerNumber).get(unitId);
				if(queue.equals(original))
					continue;
				state.getUnit(unitId).setDurativeStatus(queue.peekPrimitive(), 0);
				queuedActions.get(sendingPlayerNumber).put(unitId, queue);
				
			}
		}
	}

	protected LinkedList<Action> calculatePrimitives(Action action) {
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
		return primitives;
	}
}
