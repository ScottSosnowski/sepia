package edu.cwru.sepia.agent.planner;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.action.ActionType;
import edu.cwru.sepia.action.LocatedAction;
import edu.cwru.sepia.action.LocatedProductionAction;
import edu.cwru.sepia.action.TargetedAction;
import edu.cwru.sepia.model.state.Direction;
import edu.cwru.sepia.model.state.State.StateView;
import edu.cwru.sepia.model.state.Target;
import edu.cwru.sepia.model.state.Unit.UnitView;
import edu.cwru.sepia.pathing.AStarNode;
import edu.cwru.sepia.util.Pair;
import edu.cwru.sepia.util.Rectangle;

public class PlanningContext {

	private Action action;
	private StateView state;
	private UnitView unit;

	private int nodesExpanded = 0;

	public PlanningContext(Action compoundAction, StateView state) {
		this.action = compoundAction;
		this.state = state;
	}

	/**
	 * Plans for the given compound action.
	 * 
	 * Build: moves next to location, does build primitive Gather, Deposit:
	 * moves next to target, does gather or deposit primitive Attack: moves
	 * within range of target, does attack primitive until target is dead Move:
	 * moves until unit's top left is at location
	 * 
	 * Will never return null. Throws a #{@code PlanInfeasibleException} with a
	 * useful message about why it couldn't come up with a plan instead.
	 * 
	 * @param state
	 * @param action
	 * @return
	 * @throws PlanInfeasibleException
	 */
	public Plan generatePlan() throws PlanInfeasibleException {
		nodesExpanded = 0;
		Plan plan = null;
		unit = getActor();
		if(action.getType().isPrimitive()) {
			plan = new Plan(action);
			plan.getActions().addLast(new FixedCountActionMacro(action));
		} else {
			plan = search();
		}
		return plan;
	}
	
	/**
	 * How many search nodes were considered during planning.
	 * Returns 0 if planning has not been done yet.
	 * Resets with each planning run.
	 * @return
	 */
	public int getNumNodesExpanded() {
		return nodesExpanded;
	}

	private Plan search() throws PlanInfeasibleException {
		Plan plan = null;
		Map<Pair<Integer, Integer>, AStarNode> closedSet = new HashMap<>();
		TreeSet<AStarNode> openQueue = new TreeSet<>();
		AStarNode start = getStart();
		openQueue.add(start);
		closedSet.put(new Pair<Integer, Integer>(start.getX(), start.getY()), start);
		while(!openQueue.isEmpty()) {
			AStarNode current = openQueue.pollFirst();
			nodesExpanded++;
			// done enough movement
			if(inRange(current)) {
				plan = fillInPlan(current);
				break;
			}
			// explore all primitive movement options
			for(Direction d : Direction.values()) {
				int newX = current.getX() + d.xComponent();
				int newY = current.getY() + d.yComponent();
				Rectangle newLocation = new Rectangle(newX, newY, unit.getTemplateView().getWidth(), unit.getTemplateView().getHeight());
				AStarNode newNode = new AStarNode(newX, newY, distanceToDestination(newLocation), current, d);
				Pair<Integer, Integer> position = new Pair<Integer, Integer>(newX, newY);
				AStarNode oldNode = closedSet.get(position);
				// only add a new node if the position has not been explored or
				// the new way to get to it is cheaper
				if(oldNode == null || newNode.compareTo(oldNode) < 0) { 
					// new node is more promising
					// make sure new node is feasible
					if(!state.isDifferentUnitIn(unit.id, newLocation)
							&& !state.isResourceIn(newLocation) && state.inBounds(newLocation)) {
						closedSet.put(position, newNode);
						if(oldNode != null)
							openQueue.remove(oldNode);
						openQueue.add(newNode);
					}
				}
			}
		}
		if(plan == null) {
			throw new PlanInfeasibleException("There is no path that puts the unit in range.");
		}
		return plan;
	}

	private Plan fillInPlan(AStarNode terminus) throws PlanInfeasibleException {
		Plan plan = new Plan(action);
		AStarNode current = terminus;
		// start at tail of AStar linked list and continually add to the head of
		// the plan to preserve order
		while(current.getPrevious() != null) {
			Action move = Action.createPrimitiveMove(action.getUnitId(), current.getDirectionFromPrevious());
			plan.getActions().addFirst(new FixedCountActionMacro(move));
			current = current.getPrevious();
		}
		// cap off plan with the non-movement action now that unit is in range
		Macro primitive = getFinalAction();
		if(primitive != null)// valid case for compound movement, which needs no
								// final non-movement action
			plan.getActions().addLast(primitive);
		return plan;
	}

	private AStarNode getStart() throws PlanInfeasibleException {
		return new AStarNode(unit.getXPosition(), unit.getYPosition(), distanceToDestination(unit.getBounds()));
	}

	private boolean inRange(AStarNode currentPosition) throws PlanInfeasibleException {
		int distance = distanceToDestination(currentPosition.asRectangle(unit.getTemplateView().getWidth(), unit
				.getTemplateView().getHeight()));
		if(action.getType() == ActionType.COMPOUNDMOVE) {
			// is unit's top left corner on top of destination?
			return distance == 0;
		} else if(action.getType() == ActionType.COMPOUNDATTACK) {
			// is target within range?
			return unit.getTemplateView().getRange() >= distance;
		} else {
			// is unit adjacent to or on top of the target or destination?
			return distance <= 1;
		}
	}

	private int distanceToDestination(Rectangle currentPosition) throws PlanInfeasibleException {
		Rectangle destination = getDestination();
		// compound move is the only one that requires that the unit be at the
		// position instead of adjacent or in range
		if(action.getType() == ActionType.COMPOUNDMOVE) {
			return Math.max(Math.abs(currentPosition.getLeft() - destination.getLeft()),
					Math.abs(currentPosition.getTop() - destination.getTop()));
		}
		return currentPosition.distanceTo(destination);
	}

	private Rectangle getDestination() throws PlanInfeasibleException {
		if(action instanceof LocatedAction) {
			LocatedAction locatedAction = (LocatedAction)action;
			return new Rectangle(locatedAction.getX(), locatedAction.getY());
		} else if(action instanceof TargetedAction) {
			Target target = null;
			if(action.getType() == ActionType.COMPOUNDATTACK)
				target = getTarget(false);
			else
				target = getTarget(true);
			return target.getBounds();
		}
		throw getUnknownActionTypeException(action);
	}

	/**
	 * Creates the final action to be done for the given compoundAction. Assumes
	 * that the given position is in range of the target or location.
	 * 
	 * @param state
	 * @param compound
	 * @return
	 * @throws PlanInfeasibleException
	 */
	private Macro getFinalAction() throws PlanInfeasibleException {
		if(action instanceof TargetedAction) {
			TargetedAction ta = (TargetedAction)action;
			if(action.getType() == ActionType.COMPOUNDATTACK) {
				return new CombatMacro(Action.createPrimitiveAttack(ta.getUnitId(), ta.getTargetId()));
			}

			UnitView unit = getActor();
			Target target = getTarget(true);
			int xDiff = target.getXPosition() - unit.getXPosition();
			int yDiff = target.getYPosition() - unit.getYPosition();
			Direction direction = Direction.getDirection(xDiff, yDiff);
			if(action.getType() == ActionType.COMPOUNDDEPOSIT) {
				return new FixedCountActionMacro(Action.createPrimitiveDeposit(ta.getUnitId(), direction));
			} else if(action.getType() == ActionType.COMPOUNDGATHER) {
				return new FixedCountActionMacro(Action.createPrimitiveGather(ta.getUnitId(), direction));
			} else {
				throw getUnknownActionTypeException(action);
			}
		} else if(action.getType() == ActionType.COMPOUNDBUILD) {
			LocatedProductionAction lpa = (LocatedProductionAction)action;
			return new FixedCountActionMacro(Action.createPrimitiveBuild(lpa.getUnitId(), lpa.getTemplateId()));
		} else if(action.getType() == ActionType.COMPOUNDMOVE) {
			return null;
		} else {
			throw getUnknownActionTypeException(action);
		}

	}

	private UnitView getActor() throws PlanInfeasibleException {
		int unitId = action.getUnitId();
		unit = state.getUnit(unitId);
		if(unit == null)
			throw new PlanInfeasibleException("Unit " + unitId + " does not exist");
		return unit;
	}

	private Target getTarget(boolean isResource) throws PlanInfeasibleException {
		TargetedAction ta = (TargetedAction)action;
		int targetId = ta.getTargetId();
		Target target = null;
		if(isResource)
			target = state.getResourceNode(targetId);
		else
			target = state.getUnit(targetId);
		if(target == null)
			throw new PlanInfeasibleException("Target " + targetId + " does not exist");
		return target;
	}

	private PlanInfeasibleException getUnknownActionTypeException(Action action) {
		return new PlanInfeasibleException("Requested action is of an unknown or primitive-only class"
				+ action.getClass().getName());
	}
}
