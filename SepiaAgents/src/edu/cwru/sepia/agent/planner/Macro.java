package edu.cwru.sepia.agent.planner;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.model.state.State.StateView;

/**
 * Produces a sequence of actions based on internal logic and current game state.
 * Could be used for simple repetition (hit it 'till it's dead) or more complex
 * logic (alternating between gathering and depositing).
 * @author tim
 *
 */
public interface Macro {

	/**
	 * Returns the next action that should be taken according to this macro,
	 * or null if the execution of the macro is complete. 
	 * 
	 * This call is not guaranteed to be idempotent. In other words, multiple calls
	 * may return different actions, even if the state has not changed.
	 * @param state
	 * @return
	 */
	public Action getAction(StateView state);
}
