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

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.action.ActionType;
import edu.cwru.sepia.action.TargetedAction;
import edu.cwru.sepia.agent.Agent;
import edu.cwru.sepia.model.state.ResourceNode;
import edu.cwru.sepia.model.state.State;
import edu.cwru.sepia.model.state.State.StateView;
import edu.cwru.sepia.model.state.Direction;
import edu.cwru.sepia.model.state.Template;
import edu.cwru.sepia.model.state.Unit;
import edu.cwru.sepia.model.state.UnitTemplate;
import edu.cwru.sepia.util.DistanceMetrics;

/**
 * An implementation of basic planning methods
 * 
 * @author Scott
 * @author Tim
 */
public class SimplePlanner implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private State state;

	public SimplePlanner(State state) {
		this.state = state;
	}

	/**
	 * Uses A* to calculate the directions to arrive at the specified location.
	 * If there is no path, it returns null (it does not do a best effort) When
	 * you wish upon A* ...
	 * 
	 * @param startingx
	 * @param startingy
	 * @param endingx
	 * @param endingy
	 * @param tolerancedistance
	 *            The distance at which to stop trying to seek (IE, 0 is on top
	 *            of it, 1 is next to it, etc) Note that the distance should
	 *            count diagonals as 1 dist
	 * @param cancollideonfinal
	 *            Whether or not to allow a collision on the final move
	 *            (allowing it to be used for gathering by telling it the last
	 *            direction)
	 * @return Some primitive (IE, based on directions) moves that bring you
	 *         within distance of the ending x and y
	 */
	public static LinkedList<Direction> getDirections(StateView state, int startingx, int startingy, int endingx,
			int endingy, int tolerancedistance, boolean cancollideonfinal) {
		// System.out.println("Getting Directions from " + startingx + "," +
		// startingy + " to " + endingx + "," + endingy);
		PriorityQueue<AStarNode> queue = new PriorityQueue<AStarNode>();
		HashSet<AStarNode> checked = new HashSet<AStarNode>();
		boolean collidesatend = !state.inBounds(endingx, endingy)
				|| (state.isUnitAt(endingx, endingy) || state.isResourceAt(endingx, endingy));
		AStarNode bestnode = null;
		queue.offer(new AStarNode(startingx, startingy, Math.max(Math.abs(startingx - endingx),
				Math.abs(startingy - endingy))));
		// I haven't the foggiest idea why this used to have these next two
		// lines
		// if(distance == 0)
		// distance = state.getXExtent()*state.getYExtent();
		while (queue.size() > 0 && bestnode == null) {
			// Grab a node
			AStarNode currentnode = queue.poll();
			int currentdistancetogoal = DistanceMetrics.chebyshevDistance(currentnode.x, currentnode.y, endingx,
					endingy);
			// Check if you are done
			if (tolerancedistance >= currentdistancetogoal
					|| (!cancollideonfinal && currentdistancetogoal == 1 && collidesatend)) {
				bestnode = currentnode;
				break;
			}
			for (Direction d : Direction.values()) {
				int newx = currentnode.x + d.xComponent();
				int newy = currentnode.y + d.yComponent();

				int newdisttogoal = Math.max(Math.abs(newx - endingx), Math.abs(newy - endingy));
				// valid if the new state is within max distance and is in
				// bounds and either there is no collision or it is at the
				// target
				if (state.inBounds(newx, newy)
						&& ((!state.isUnitAt(newx, newy) && !state.isResourceAt(newx, newy)) || (cancollideonfinal && newdisttogoal == 0))) {
					AStarNode newnode = new AStarNode(newx, newy, currentnode.g + 1, currentnode.g + 1 + newdisttogoal,
							currentnode, d);
					if (!checked.contains(newnode)) {
						queue.offer(newnode);
						checked.add(newnode);
					}
				}

			}
		}
		if (bestnode == null)
			return new LinkedList<Direction>();
		else {
			// System.out.println("Found best at:" + bestnode.x + "," +
			// bestnode.y);
			LinkedList<Direction> toreturn = new LinkedList<Direction>();
			while (bestnode.previous != null) {
				toreturn.addFirst(bestnode.directionfromprevious);
				bestnode = bestnode.previous;
			}
			return toreturn;
		}
	}

	/**
	 * Uses {@link #getDirections(int, int, int, int, int, boolean)} to get
	 * directions to the specified place and {@link #planMove(Unit,
	 * LinkedList<Direction>)} to follow them.
	 * 
	 * @param actor
	 * @param x
	 * @param y
	 * @return
	 */
	public LinkedList<Action> planMove(Unit actor, int x, int y) {
		if (!state.inBounds(x, y)) {
			return planPermanentFail(actor.id);
		}
		LinkedList<Direction> directions = getDirections(state.getView(Agent.OBSERVER_ID), actor.getXPosition(),
				actor.getYPosition(), x, y, 0, false);
		if (directions == null) {
			return planFail(actor.id);
		} else
			return planMove(actor, directions);
	}

	public LinkedList<Action> planFail(int actor) {
		LinkedList<Action> failact = new LinkedList<Action>();
		failact.add(Action.createFail(actor));
		return failact;
	}

	public LinkedList<Action> planPermanentFail(int actor) {
		LinkedList<Action> failact = new LinkedList<Action>();
		failact.add(Action.createPermanentFail(actor));
		return failact;
	}

	/**
	 * Build primitive moves following the path made by the directions
	 * 
	 * @param actor
	 * @param path
	 * @return
	 */
	private LinkedList<Action> planMove(Unit actor, LinkedList<Direction> path) {
		LinkedList<Action> moves = new LinkedList<Action>();
		while (!path.isEmpty()) {
			moves.addLast(Action.createPrimitiveMove(actor.id, path.removeFirst()));
		}
		return moves;
	}

	public LinkedList<Action> planMove(int i, int x, int y) {
		return planMove(state.getUnit(i), x, y);
	}

	/**
	 * Uses {@link #getDirections(StateView, int, int, int, int, int, boolean)}
	 * to get directions to the specified place and {@link #planMove(Unit,
	 * LinkedList<Direction>)} to follow them. then adds an attack command.
	 * 
	 * @param actor
	 * @param target
	 * @return A series of actions that move the actor to the target and attacks
	 *         the target
	 */
	public LinkedList<Action> planAttack(Unit actor, Unit target) {
		/* TODO - this method current just assumes it has the actor's and target's sizes as
		 * slack. This is sort of okay since you can always re-plan if the actor needs to close
		 * in further, but a better solution would be to plan movement until the target is in range. 
		 */
		if (target == null) {
			return planPermanentFail(actor.id);
		}
		LinkedList<Direction> directions = getDirections(
				state.getView(Agent.OBSERVER_ID),
				actor.getXPosition(),
				actor.getYPosition(),
				target.getXPosition(),
				target.getYPosition(),
				actor.getTemplate().getRange()
						+ Math.max(actor.getTemplate().getWidth(), actor.getTemplate().getHeight()) - 1
						+ Math.max(target.getTemplate().getWidth(), target.getTemplate().getHeight()) - 1, false);
		if (directions == null)
			return planFail(actor.id);
		LinkedList<Action> plan = planMove(actor, directions);
		plan.addLast(new TargetedAction(actor.id, ActionType.PRIMITIVEATTACK, target.id));
		return plan;
	}

	public LinkedList<Action> planAttack(int actor, int target) {
		Unit targetunit = state.getUnit(target);

		return planAttack(state.getUnit(actor), targetunit);
	}

	/**
	 * Uses {@link #getDirections(int, int, int, int, int, boolean)} to get
	 * directions to the specified place and {@link #planMove(Unit,
	 * LinkedList<Direction>)} to move almost there. then Then adds the final
	 * direction with a gather.
	 * 
	 * @param actor
	 * @param target
	 * @param distance
	 * @return
	 */
	public LinkedList<Action> planDeposit(Unit actor, Unit target) {
		// plan a route to onto the resource
		// This requires that planmove handle a 0 distance move as having the
		// final primative move not be affected by collisions
		// if the above requirement is violated, this will not work
		if (target == null) {
			return planPermanentFail(actor.id);
		}
		LinkedList<Direction> directions = getDirections(state.getView(Agent.OBSERVER_ID), actor.getXPosition(),
				actor.getYPosition(), target.getXPosition(), target.getYPosition(), 0, true);
		if (directions == null || directions.size() < 1) {
			return planFail(actor.id);
		}
		Direction finaldirection = directions.pollLast();
		LinkedList<Action> plan = planMove(actor, directions);
		plan.addLast(Action.createPrimitiveDeposit(actor.id, finaldirection));
		return plan;
	}

	public LinkedList<Action> planDeposit(int actor, int target) {
		return planDeposit(state.getUnit(actor), state.getUnit(target));
	}

	/**
	 * Uses {@link #getDirections(int, int, int, int, int, boolean)} to get
	 * directions to the specified place and {@link #planMove(Unit,
	 * LinkedList<Direction>)} to move almost there. then Then adds the final
	 * direction with a gather.
	 * 
	 * @param actor
	 * @param target
	 * @param distance
	 * @return A series of actions that move the actor to the target and gathers
	 *         from the target
	 */
	public LinkedList<Action> planGather(Unit actor, ResourceNode target) {
		// plan a route to onto the resource
		// This requires that planmove handle a 0 distance move as having the
		// final primative move not be affected by collisions
		// if the above requirement is violated, this will not work
		if (target == null) {
			return planPermanentFail(actor.id);
		}
		LinkedList<Direction> directions = getDirections(state.getView(Agent.OBSERVER_ID), actor.getXPosition(),
				actor.getYPosition(), target.getxPosition(), target.getyPosition(), 0, true);
		if (directions == null || directions.size() < 1) {
			return planFail(actor.id);
		}
		Direction finaldirection = directions.pollLast();
		LinkedList<Action> plan = planMove(actor, directions);
		plan.addLast(Action.createPrimitiveGather(actor.id, finaldirection));
		return plan;
	}

	public LinkedList<Action> planGather(int actor, int target) {
		return planGather(state.getUnit(actor), state.getResource(target));
	}

	public LinkedList<Action> planBuild(Unit actor, int targetX, int targetY, UnitTemplate template) {
		LinkedList<Action> plan = new LinkedList<Action>();
		// it must go to the right place
		if (actor.getXPosition() != targetX || actor.getYPosition() != targetY) {// if
																					// it
																					// is
																					// not
																					// in
																					// the
																					// same
																					// place
																					// Then
																					// bring
																					// it
																					// there
			plan = planMove(
					actor,
					getDirections(state.getView(Agent.OBSERVER_ID), actor.getXPosition(), actor.getYPosition(),
							targetX, targetY, 0, false));
		}
		plan.addLast(Action.createPrimitiveBuild(actor.id, template.getID()));
		return plan;
	}

	public LinkedList<Action> planBuild(int actor, int targetX, int targetY, int template) {
		return planBuild(state.getUnit(actor), targetX, targetY, (UnitTemplate)state.getTemplate(template));
	}

	public LinkedList<Action> planProduce(Unit actor, @SuppressWarnings("rawtypes") Template template) {
		LinkedList<Action> plan = new LinkedList<Action>();
		// produce is simple, a single action
		plan.addLast(Action.createPrimitiveProduction(actor.id, template.getID()));
		return plan;
	}

	public LinkedList<Action> planProduce(int actor, int template) {
		return planProduce(state.getUnit(actor), state.getTemplate(template));
	}

}
