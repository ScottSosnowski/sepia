package edu.cwru.sepia.agent.planner;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.action.TargetedAction;
import edu.cwru.sepia.model.state.State;
import edu.cwru.sepia.model.state.State.StateView;

public class CombatMacroTest {

	@Test
	public void testEverything() {
		State state = TestDataHelper.createStateWithTwoUnits();
		StateView view = state.getView(0);
		
		TargetedAction action = Action.createPrimitiveAttack(0, 1);
		CombatMacro macro = new CombatMacro(action);
		assertEquals(action, macro.getAction(view));
		assertEquals(action, macro.getAction(view));
		
		state.getUnit(1).setHP(1);
		state.getUnit(1).deprecateOldView();
		view = state.getView(0);
		assertEquals(action, macro.getAction(view));
		
		state.getUnit(1).setHP(0);
		state.getUnit(1).deprecateOldView();
		view = state.getView(0);
		assertThat(macro.getAction(view), nullValue());
		assertThat(macro.getAction(view), nullValue());
		
		macro = new CombatMacro(action);
		state.removeUnit(1);
		view = state.getView(0);
		assertThat(macro.getAction(view), nullValue());
		assertThat(macro.getAction(view), nullValue());
	}
}
