package edu.cwru.sepia.agent.planner;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.action.DirectedAction;
import edu.cwru.sepia.model.state.Direction;
import edu.cwru.sepia.model.state.State;
import edu.cwru.sepia.model.state.State.StateView;

public class FixedCountActionMacroTest {

	@Test
	public void testDefaultDoesntRepeat() {
		State state = TestDataHelper.createStateWithTwoUnits();
		StateView view = state.getView(0);
		
		DirectedAction action = Action.createPrimitiveDeposit(0, Direction.NORTHWEST);
		FixedCountActionMacro macro = new FixedCountActionMacro(action);
		assertEquals(action, macro.getAction(view));
		assertThat(macro.getAction(view), nullValue());
		assertThat(macro.getAction(view), nullValue());
	}

	@Test
	public void testRepeat() {
		State state = TestDataHelper.createStateWithTwoUnits();
		StateView view = state.getView(0);
		
		DirectedAction action = Action.createPrimitiveDeposit(0, Direction.NORTHWEST);
		FixedCountActionMacro macro = new FixedCountActionMacro(action, 4);
		assertEquals(action, macro.getAction(view));
		assertEquals(action, macro.getAction(view));
		assertEquals(action, macro.getAction(view));
		assertEquals(action, macro.getAction(view));
		assertThat(macro.getAction(view), nullValue());
		assertThat(macro.getAction(view), nullValue());
	}
}
