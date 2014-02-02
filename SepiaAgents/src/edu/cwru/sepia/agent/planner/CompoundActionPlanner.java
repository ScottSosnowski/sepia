package edu.cwru.sepia.agent.planner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.action.ActionResult;
import edu.cwru.sepia.action.ActionResultType;
import edu.cwru.sepia.action.TargetedAction;
import edu.cwru.sepia.model.history.History.HistoryView;
import edu.cwru.sepia.model.state.State.StateView;
import edu.cwru.sepia.model.state.Unit.UnitView;

public class CompoundActionPlanner {

	private List<ActionResultType> dontReplan = Arrays.asList(ActionResultType.COMPLETED, ActionResultType.INCOMPLETE);

	private Map<Integer, Plan> plans = new HashMap<>();

	/**
	 * Returns the actions each planned unit should take on the current turn.
	 * Re-plans if actions failed or will be infeasible. Will not return actions
	 * for units if they are in the middle of executing an action.
	 * 
	 * @param state
	 * @param history
	 * @return
	 */
	public Collection<Action> getActions(StateView state, HistoryView history) {
		Collection<Action> actions = new ArrayList<>();
		for(Integer unitId : new HashSet<Integer>(plans.keySet())) {
			UnitView unit = state.getUnit(unitId);
			if(unit == null) {
				plans.remove(unitId);
				continue;
			}

			// if agent is idle
			if(unit.getCurrentDurativeAction() == null) {
				Plan plan = plans.get(unitId);
				ActionResult feedback = history.getCommandFeedback(unit.getTemplateView().getPlayer(),
						state.getTurnNumber() - 1).get(unitId);
				// if action is targeted or last primitive failed, re-plan
				if(plan.getCompoundAction() instanceof TargetedAction || !dontReplan.contains(feedback.getFeedback())) {
					try {
						createOrUpdatePlan(state, plan.getCompoundAction());
					} catch(PlanInfeasibleException ex) {
						plans.remove(unitId);
						continue;
					}
				}
				actions.add(plan.getNextAction(state));
			}
		}
		return actions;
	}

	/**
	 * Registers the given action for planning. Causes a plan to be generated.
	 * Overwrites the current plan for the action's unit.
	 * 
	 * @param state
	 * @param unit
	 * @param action
	 * @return the old plan
	 */
	public Plan createOrUpdatePlan(StateView state, Action action) throws PlanInfeasibleException {
		Plan plan = generatePlan(state, action);
		return plans.put(action.getUnitId(), plan);
	}

	/**
	 * Stops execution of the current plan for the given unit. Removes the plan.
	 * 
	 * @param unit
	 * @return
	 */
	public Plan removePlan(int unitId) {
		return plans.remove(unitId);
	}
	
	public Plan getPlan(int unitId) {
		Plan plan = plans.get(unitId);
		if(plan == null)
			return null;
		return plan.getCopy();
	}

	/**
	 * Plans for the given compound action using #{@code PlanningContext#generatePlan()}
	 * 
	 * @param state
	 * @param action
	 * @return
	 * @throws PlanInfeasibleException
	 */
	public Plan generatePlan(StateView state, Action action) throws PlanInfeasibleException {
		return new PlanningContext(action, state).generatePlan();
	}
}
