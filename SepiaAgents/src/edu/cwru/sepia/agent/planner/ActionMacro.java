package edu.cwru.sepia.agent.planner;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.model.state.State.StateView;

/**
 * A macro that repeats a single action until the #{@code TerminationCondition} is reached.
 * @author tim
 *
 * @param <A> - specific action subclass to which the #{@code TerminationCondition} applies
 */
public abstract class ActionMacro<A extends Action> implements Macro {

	protected A action;

	public ActionMacro(A action) {
		this.action = action;
	}
	
	@Override
	public Action getAction(StateView state) {
		if(action == null || shouldTerminate(state)) {
			action = null;//prevent action from recurring if macro is not cleaned up properly
			return null;
		}
		else {
			return action;
		}
	}

	protected abstract boolean shouldTerminate(StateView state);
}
