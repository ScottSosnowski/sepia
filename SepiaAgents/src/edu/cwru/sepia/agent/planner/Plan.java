package edu.cwru.sepia.agent.planner;

import java.util.LinkedList;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.model.state.State.StateView;

public class Plan {

	private Action compoundAction;
	private LinkedList<Macro> actions;
	
	public Plan(Action compoundAction) {
		this.compoundAction = compoundAction;
		actions = new LinkedList<>();
	}
	
	public LinkedList<Macro> getActions() {
		return actions;
	}
	
	public Action getCompoundAction() {
		return compoundAction;
	}
	
	public Action getNextAction(StateView state) {
		Action action = null;
		while(action == null && !actions.isEmpty()) {
			Macro macro = actions.getFirst();
			action = macro.getAction(state);
			if(action == null)
				actions.removeFirst();
		}
		return action;
	}
	
	public Plan getCopy() {
		Plan copy = new Plan(getCompoundAction());
		copy.actions = new LinkedList<>(actions);
		return copy;
	}
}
