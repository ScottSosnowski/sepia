package edu.cwru.sepia.agent.planner;

import java.util.LinkedList;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.model.state.State.StateView;

/**
 * A #{@code Macro} that executed the specified compound actions
 * in a loop indefinitely. Plans for new actions as each previous one
 * completes.
 * 
 * This class has the option of whether to wait for compound actions to become feasible.
 * If skipInfeasibleActions is specified to be false, it will return null for an action
 * until the current compound action becomes feasible. Otherwise, it will move on to the 
 * next action, but will still return null if none of the compound actions have valid,
 * non-empty plans. 
 * 
 * @author tim
 *
 */
public class CompoundActionLoopMacro implements Macro {
	
	private LinkedList<Action> actions;
	private Plan currentPlan;
	private boolean skipInfeasibleActions;
	
	/**
	 * Enqueues the given actions for execution in the order that the #{@code java.util.Iterable}
	 * provides. If skipInfeasibleActions is false, #{@code #getAction(StateView)} will return
	 * null until the current compound action becomes feasible.
	 * @param actions
	 * @param skipInfeasibleActions
	 */
	public CompoundActionLoopMacro(Iterable<Action> actions, boolean skipInfeasibleActions) {
		this.actions = new LinkedList<>();
		for(Action action : actions) {
			this.actions.addLast(action);
		}
		this.skipInfeasibleActions = skipInfeasibleActions;
	}

	@Override
	public Action getAction(StateView state) {
		Action primitive = null;
		int tries = 0;
		while(primitive == null) {
			Action compound = actions.getFirst();
			if(currentPlan == null) {
				try {
					currentPlan = new PlanningContext(compound, state).generatePlan();
					primitive = currentPlan.getNextAction(state);
				} catch(PlanInfeasibleException ex) {
					if(!skipInfeasibleActions)
						return null;
				}
			}
			//no more actions to do for current compound action
			if(primitive == null) {
				currentPlan = null;//clear current plan so we get a new one on the next loop execution
				actions.addLast(actions.removeFirst());//rotate it to the back
				tries++;
				if(tries == actions.size())
					break;//prevents loops in the case that no compound action requires any primitives to be executed
			}
		}
		return primitive;
	}

}
