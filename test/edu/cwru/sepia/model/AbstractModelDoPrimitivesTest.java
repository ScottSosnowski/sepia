package edu.cwru.sepia.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.action.DirectedAction;
import edu.cwru.sepia.action.LocatedAction;
import edu.cwru.sepia.action.LocatedProductionAction;
import edu.cwru.sepia.action.ProductionAction;
import edu.cwru.sepia.action.TargetedAction;
import edu.cwru.sepia.model.AbstractModel.FailureMode;
import edu.cwru.sepia.model.state.Direction;
import edu.cwru.sepia.model.state.PlayerState;
import edu.cwru.sepia.model.state.ResourceNode;
import edu.cwru.sepia.model.state.State;
import edu.cwru.sepia.model.state.State.StateBuilder;
import edu.cwru.sepia.model.state.Unit;
import edu.cwru.sepia.model.state.UnitTemplate;

public class AbstractModelDoPrimitivesTest extends AbstractModelTestBase {

	@Test
	public void testPrimitivesWrongType() {
		StateBuilder builder = singleUnitSetup();
		State state = builder.build();
		ModelImpl model = getModel(state);
		Unit unit = state.getUnit(0);
		
		Action[] actions = {new DirectedAction(0, null, Direction.EAST),
							new LocatedAction(0, null, unit.getxPosition() + 1, unit.getyPosition()),
							new TargetedAction(0, null, 0),
							new ProductionAction(0, null, 0),
							new LocatedProductionAction(0, null, 0, unit.getxPosition() + 1, unit.getyPosition())
		};
		
		testActions(actions, unit, 0, model.getPrimitiveMove());
		testActions(actions, unit, 0, model.getPrimitiveGather());
		testActions(actions, unit, 0, model.getPrimitiveDeposit());
		testActions(actions, unit, 2, model.getPrimitiveAttack());
		testActions(actions, unit, 3, model.getPrimitiveProduce());
		testActions(actions, unit, 3, model.getPrimitiveBuild());
	}
	
	private void testActions(Action[] actions, Unit unit, int valid, ActionMethod method) {
		for(int i = 0; i < actions.length; i++) {
			FailureMode result = method.execute(actions[i], unit, 0, 0);
			if(i == valid) {
				assertThat(FailureMode.WRONG_ACTION_TYPE, not(equalTo(result)));
			} else {
				assertEquals(FailureMode.WRONG_ACTION_TYPE, result);
			}
		}
	}
	
	@Test
	public void testMoveSuccess() {
		StateBuilder builder = singleUnitSetup();
		State state = builder.build();
		ModelImpl model = getModel(state);
		Unit unit = state.getUnit(0);
		
		int[][] points = new int[][]{{0,0},{8,0},{0,8},{8,8},{62,62}};
		for(int i = 0; i < points.length; i++) {
			unit.setxPosition(points[i][0]);
			unit.setyPosition(points[i][1]);
			int x = unit.getxPosition();
			int y = unit.getyPosition();
			
			Action a = Action.createPrimitiveMove(0, Direction.EAST);
			FailureMode result = model.doPrimitiveMove(a, unit, unit.getxPosition() + 1, unit.getyPosition());
			assertEquals(FailureMode.SUCCESS, result);
			assertEquals(x + 1, unit.getxPosition());
			assertEquals(y, unit.getyPosition());
			x = unit.getxPosition();
			
			a = Action.createPrimitiveMove(0, Direction.SOUTH);
			result = model.doPrimitiveMove(a, unit, unit.getxPosition(), unit.getyPosition() + 1);
			assertEquals(FailureMode.SUCCESS, result);
			assertEquals(x, unit.getxPosition());
			assertEquals(y + 1, unit.getyPosition());
			y = unit.getyPosition();
			
			a = Action.createPrimitiveMove(0, Direction.NORTHWEST);
			result = model.doPrimitiveMove(a, unit, unit.getxPosition() - 1, unit.getyPosition() - 1);
			assertEquals(FailureMode.SUCCESS, result);
			assertEquals(x - 1, unit.getxPosition());
			assertEquals(y - 1, unit.getyPosition());
		}
	}
	
	@Test
	public void testMoveFailsDueToBounds() {
		StateBuilder builder = singleUnitSetup();
		State state = builder.build();
		ModelImpl model = getModel(state);
		Unit unit = state.getUnit(0);
		unit.setxPosition(0);
		unit.setyPosition(0);
		int x = unit.getxPosition();
		int y = unit.getyPosition();

		Action a = Action.createPrimitiveMove(0, Direction.WEST);
		FailureMode result = model.doPrimitiveMove(a, unit, unit.getxPosition() - 1, unit.getyPosition());
		assertEquals(FailureMode.FAILED_ATTEMPT, result);
		assertEquals(x, unit.getxPosition());
		assertEquals(y, unit.getyPosition());
		
		a = Action.createPrimitiveMove(0, Direction.NORTH);
		result = model.doPrimitiveMove(a, unit, unit.getxPosition(), unit.getyPosition() - 1);
		assertEquals(FailureMode.FAILED_ATTEMPT, result);
		assertEquals(x, unit.getxPosition());
		assertEquals(y, unit.getyPosition());
		
		a = Action.createPrimitiveMove(0, Direction.NORTHWEST);
		result = model.doPrimitiveMove(a, unit, unit.getxPosition() - 1, unit.getyPosition() - 1);
		assertEquals(FailureMode.FAILED_ATTEMPT, result);
		assertEquals(x, unit.getxPosition());
		assertEquals(y, unit.getyPosition());
		
		unit.setxPosition(63);
		unit.setyPosition(63);
		x = unit.getxPosition();
		y = unit.getyPosition();

		a = Action.createPrimitiveMove(0, Direction.EAST);
		result = model.doPrimitiveMove(a, unit, unit.getxPosition() + 1, unit.getyPosition());
		assertEquals(FailureMode.FAILED_ATTEMPT, result);
		assertEquals(x, unit.getxPosition());
		assertEquals(y, unit.getyPosition());
		
		a = Action.createPrimitiveMove(0, Direction.SOUTH);
		result = model.doPrimitiveMove(a, unit, unit.getxPosition(), unit.getyPosition() + 1);
		assertEquals(FailureMode.FAILED_ATTEMPT, result);
		assertEquals(x, unit.getxPosition());
		assertEquals(y, unit.getyPosition());
		
		a = Action.createPrimitiveMove(0, Direction.SOUTHEAST);
		result = model.doPrimitiveMove(a, unit, unit.getxPosition() + 1, unit.getyPosition() + 1);
		assertEquals(FailureMode.FAILED_ATTEMPT, result);
		assertEquals(x, unit.getxPosition());
		assertEquals(y, unit.getyPosition());
	}
	
	@Test
	public void testMoveFailsDueToCollision() {
		StateBuilder builder = singleUnitSetup();
		UnitTemplate template = (UnitTemplate)builder.getTemplate(0, "t0");
		PlayerState ps0 = new PlayerState(0);
		Unit u2 = new Unit(template, 2);
		ps0.addUnit(u2);
		builder.addUnit(u2, 7, 7);
		PlayerState ps1 = new PlayerState(1);
		builder.addPlayer(ps1);
		ps1.addTemplate(template);
		Unit u1 = new Unit(template, 1);
		ps1.addUnit(u1);
		builder.addUnit(u1, 9, 8);
		ResourceNode tree = new ResourceNode(ResourceNode.Type.TREE, 7, 8, 100, 0);
		builder.addResource(tree);
		State state = builder.build();
		model = getModel(state);
		Unit u0 = state.getUnit(0);
		u0.setxPosition(8);
		u0.setyPosition(8);
		
		Action a = Action.createPrimitiveMove(0, Direction.EAST);
		FailureMode result = model.doPrimitiveMove(a, u0, u0.getxPosition() + 1, u0.getyPosition());
		assertEquals(FailureMode.FAILED_ATTEMPT, result);
		assertEquals(8, u0.getxPosition());
		assertEquals(8, u0.getyPosition());

		a = Action.createPrimitiveMove(0, Direction.NORTHWEST);
		result = model.doPrimitiveMove(a, u0, u0.getxPosition() - 1, u0.getyPosition() - 1);
		assertEquals(FailureMode.FAILED_ATTEMPT, result);
		assertEquals(8, u0.getxPosition());
		assertEquals(8, u0.getyPosition());

		a = Action.createPrimitiveMove(0, Direction.WEST);
		result = model.doPrimitiveMove(a, u0, u0.getxPosition() - 1, u0.getyPosition());
		assertEquals(FailureMode.FAILED_ATTEMPT, result);
		assertEquals(8, u0.getxPosition());
		assertEquals(8, u0.getyPosition());
	}
	
	@Test
	public void moveFailsImmobileUnit() {
		StateBuilder builder = singleUnitSetup();
		State state = builder.build();
		ModelImpl model = getModel(state);
		Unit unit = state.getUnit(0);
		UnitTemplate template = unit.getTemplate();
		template.setCanMove(false);
		int x = unit.getxPosition();
		int y = unit.getyPosition();
		
		Action a = Action.createPrimitiveMove(0, Direction.WEST);
		FailureMode result = model.doPrimitiveMove(a, unit, unit.getxPosition() - 1, unit.getyPosition());
		assertEquals(FailureMode.FAILED_ATTEMPT, result);
		assertEquals(x, unit.getxPosition());
		assertEquals(y, unit.getyPosition());
	}
}
