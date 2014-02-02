package edu.cwru.sepia.agent.planner;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.model.state.State.StateView;

public class FixedCountActionMacro extends ActionMacro<Action> {

	private int count;
	
	public FixedCountActionMacro(Action action) {
		super(action);
		this.count = 1;
	}
	
	public FixedCountActionMacro(Action action, int count) {
		super(action);
		this.count = count;
	}

	@Override
	protected boolean shouldTerminate(StateView state) {
		if(count == 0)
			return true;
		count--;
		return false;
	}

	@Override
	public String toString() {
		return "FixedCountActionMacro [count=" + count + ", action=" + action + "]";
	}

}
