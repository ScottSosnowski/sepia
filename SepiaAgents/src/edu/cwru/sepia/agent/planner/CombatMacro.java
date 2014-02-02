package edu.cwru.sepia.agent.planner;

import edu.cwru.sepia.action.TargetedAction;
import edu.cwru.sepia.model.state.State.StateView;
import edu.cwru.sepia.model.state.Unit.UnitView;

public class CombatMacro extends ActionMacro<TargetedAction> {

	public CombatMacro(TargetedAction action) {
		super(action);
	}

	@Override
	protected boolean shouldTerminate(StateView state) {
		UnitView unit = state.getUnit(action.getTargetId());
		return unit == null || unit.getHP() <= 0;
	}

	@Override
	public String toString() {
		return "CombatMacro [action=" + action + "]";
	}
}
