package edu.cwru.sepia.agent.planner;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.action.ActionType;
import edu.cwru.sepia.action.DirectedAction;
import edu.cwru.sepia.action.LocatedAction;
import edu.cwru.sepia.action.TargetedAction;
import edu.cwru.sepia.model.state.Direction;
import edu.cwru.sepia.model.state.State;
import edu.cwru.sepia.model.state.Unit;
import edu.cwru.sepia.model.state.UnitTemplate;

public class PlanningContextFunctionalTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testUnobstructedMovement() throws PlanInfeasibleException {
		State state = TestDataHelper.createStateWithOneUnit();
		LocatedAction action1 = Action.createCompoundMove(0, 8, 4);
		Plan plan = new PlanningContext(action1, state.getView(0)).generatePlan();

		Unit unit = state.getUnit(0);
		int actionCount = 0;
		Action action = plan.getNextAction(state.getView(0));
		while(action != null) {
			actionCount++;
			assertThat(action, instanceOf(DirectedAction.class));
			DirectedAction da = (DirectedAction)action;
			Direction d = da.getDirection();
			assertThat(d, anyOf(is(Direction.EAST), is(Direction.SOUTHEAST), is(Direction.NORTHEAST)));
			unit.setXPosition(unit.getXPosition() + d.xComponent());
			unit.setYPosition(unit.getYPosition() + d.yComponent());
			unit.deprecateOldView();
			action = plan.getNextAction(state.getView(0));
		}
		assertEquals(8, actionCount);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testObstructedMovement() throws PlanInfeasibleException {
		/*
		 *x T 
		 *1 T
		 *2 T
		 *3 T o
		 *4 T333
		 *5 T222
		 *6 T11
		 *7 T0
		 * 89   
		 */
		State state = TestDataHelper.createStateWithOneUnit();
		TestDataHelper.addTreeLine(state, 0, 7, 2, false);
		LocatedAction action1 = Action.createCompoundMove(0, 4, 3);
		Plan plan = new PlanningContext(action1, state.getView(0)).generatePlan();

		Unit unit = state.getUnit(0);
		int actionCount = 0;
		Action action = plan.getNextAction(state.getView(0));
		while(action != null) {
			actionCount++;
			assertThat(action, instanceOf(DirectedAction.class));
			DirectedAction da = (DirectedAction)action;
			Direction d = da.getDirection();
			if(actionCount < 7)
				assertThat(d, anyOf(is(Direction.SOUTH), is(Direction.EAST), is(Direction.SOUTHEAST), is(Direction.SOUTHWEST)));
			else if(actionCount > 10)
				assertThat(d, anyOf(is(Direction.EAST), is(Direction.NORTHWEST), is(Direction.NORTHEAST), is(Direction.NORTH)));
			unit.setXPosition(unit.getXPosition() + d.xComponent());
			unit.setYPosition(unit.getYPosition() + d.yComponent());
			unit.deprecateOldView();
			action = plan.getNextAction(state.getView(0));
		}
		assertEquals(14, actionCount);
	}
	
	@Test(expected = PlanInfeasibleException.class)
	public void testNoRoute() throws PlanInfeasibleException {
		State state = TestDataHelper.createStateWithOneUnit();
		TestDataHelper.addTreeLine(state, 0, state.getYExtent(), 2, false);
		LocatedAction action1 = Action.createCompoundMove(0, 4, 3);
		new PlanningContext(action1, state.getView(0)).generatePlan();
		fail();//planner should throw an exception
	}
	
	@Test
	public void testGather() throws PlanInfeasibleException {
		State state = TestDataHelper.createStateWithOneUnit();
		TestDataHelper.addTreeLine(state, 5, 5, 5, false);
		TargetedAction planAction = Action.createCompoundGather(0, 5);
		Plan plan = new PlanningContext(planAction, state.getView(0)).generatePlan();

		Unit unit = state.getUnit(0);
		Action action = plan.getNextAction(state.getView(0));
		int actionCount = 0;
		while(action != null) {
			actionCount++;
			assertThat(action, instanceOf(DirectedAction.class));
			DirectedAction da = (DirectedAction)action;
			Direction d = da.getDirection();
			assertThat(d, is(Direction.SOUTHEAST));
			if(actionCount <= 3) {
				assertThat(action.getType(), is(ActionType.PRIMITIVEMOVE));
				unit.setXPosition(unit.getXPosition() + d.xComponent());
				unit.setYPosition(unit.getYPosition() + d.yComponent());
				unit.deprecateOldView();
			} else {
				assertThat(action.getType(), is(ActionType.PRIMITIVEGATHER));
			}
			action = plan.getNextAction(state.getView(0));
		}
		assertEquals(4, actionCount);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAttack() throws PlanInfeasibleException {
		State state = TestDataHelper.createStateWithTwoSeparatedUnits();
		TargetedAction planAction = Action.createCompoundAttack(0, 1);
		Plan plan = new PlanningContext(planAction, state.getView(0)).generatePlan();
		
		Action action = plan.getNextAction(state.getView(0));
		int actionCount = 0;
		Unit unit = state.getUnit(0);
		UnitTemplate targetTemplate = (UnitTemplate)state.getTemplate(1);
		int attackCount = (int)Math.ceil((double)targetTemplate.getBaseHealth() / (double)unit.getTemplate().getBasicAttack()); 
		while(action != null && actionCount < 20) {
			actionCount++;
			if(actionCount <= 5) {
				assertThat(action, instanceOf(DirectedAction.class));
				DirectedAction da = (DirectedAction)action;
				Direction d = da.getDirection();
				assertThat(d, anyOf(is(Direction.EAST), is(Direction.SOUTHEAST), is(Direction.SOUTH), is(Direction.NORTHEAST)));
				assertThat(action.getType(), is(ActionType.PRIMITIVEMOVE));
				unit.setXPosition(unit.getXPosition() + d.xComponent());
				unit.setYPosition(unit.getYPosition() + d.yComponent());
				unit.deprecateOldView();
			} else {
				assertThat(action, instanceOf(TargetedAction.class));
				TargetedAction ta = (TargetedAction)action;
				int targetId = ta.getTargetId();
				assertThat(targetId, is(1));
				Unit target = state.getUnit(targetId);
				target.setHP(target.getCurrentHealth() - unit.getTemplate().getBasicAttack());
				target.deprecateOldView();
			}
			action = plan.getNextAction(state.getView(0));
		}
		assertEquals(attackCount + 5, actionCount);
	}
}
