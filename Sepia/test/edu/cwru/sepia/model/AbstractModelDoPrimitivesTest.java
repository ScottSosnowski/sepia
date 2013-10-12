package edu.cwru.sepia.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.model.AbstractModel.FailureMode;
import edu.cwru.sepia.model.state.Direction;
import edu.cwru.sepia.model.state.PlayerState;
import edu.cwru.sepia.model.state.ResourceNode;
import edu.cwru.sepia.model.state.ResourceNodeType;
import edu.cwru.sepia.model.state.ResourceType;
import edu.cwru.sepia.model.state.State;
import edu.cwru.sepia.model.state.State.StateBuilder;
import edu.cwru.sepia.model.state.Unit;
import edu.cwru.sepia.model.state.UnitTemplate;

public class AbstractModelDoPrimitivesTest extends AbstractModelTestBase {
	
	@Test
	public void testMoveSuccess() {
		StateBuilder builder = singleUnitSetup();
		State state = builder.build();
		ModelImpl model = getModel(state);
		Unit unit = state.getUnit(0);
		
		int[][] points = new int[][]{{0,0},{8,0},{0,8},{8,8},{62,62}};
		for(int i = 0; i < points.length; i++) {
			unit.setXPosition(points[i][0]);
			unit.setYPosition(points[i][1]);
			int x = unit.getXPosition();
			int y = unit.getYPosition();
			
			Action a = Action.createPrimitiveMove(0, Direction.EAST);
			FailureMode result = model.doPrimitiveMove(a, unit);
			assertEquals(FailureMode.SUCCESS, result);
			assertEquals(x + 1, unit.getXPosition());
			assertEquals(y, unit.getYPosition());
			x = unit.getXPosition();
			
			a = Action.createPrimitiveMove(0, Direction.SOUTH);
			result = model.doPrimitiveMove(a, unit);
			assertEquals(FailureMode.SUCCESS, result);
			assertEquals(x, unit.getXPosition());
			assertEquals(y + 1, unit.getYPosition());
			y = unit.getYPosition();
			
			a = Action.createPrimitiveMove(0, Direction.NORTHWEST);
			result = model.doPrimitiveMove(a, unit);
			assertEquals(FailureMode.SUCCESS, result);
			assertEquals(x - 1, unit.getXPosition());
			assertEquals(y - 1, unit.getYPosition());
		}
	}
	
	@Test
	public void testMoveFailsDueToBounds() {
		StateBuilder builder = singleUnitSetup();
		State state = builder.build();
		ModelImpl model = getModel(state);
		Unit unit = state.getUnit(0);
		unit.setXPosition(0);
		unit.setYPosition(0);
		int x = unit.getXPosition();
		int y = unit.getYPosition();

		Action a = Action.createPrimitiveMove(0, Direction.WEST);
		FailureMode result = model.doPrimitiveMove(a, unit);
		assertEquals(FailureMode.FAILED_ATTEMPT, result);
		assertEquals(x, unit.getXPosition());
		assertEquals(y, unit.getYPosition());
		
		a = Action.createPrimitiveMove(0, Direction.NORTH);
		result = model.doPrimitiveMove(a, unit);
		assertEquals(FailureMode.FAILED_ATTEMPT, result);
		assertEquals(x, unit.getXPosition());
		assertEquals(y, unit.getYPosition());
		
		a = Action.createPrimitiveMove(0, Direction.NORTHWEST);
		result = model.doPrimitiveMove(a, unit);
		assertEquals(FailureMode.FAILED_ATTEMPT, result);
		assertEquals(x, unit.getXPosition());
		assertEquals(y, unit.getYPosition());
		
		unit.setXPosition(63);
		unit.setYPosition(63);
		x = unit.getXPosition();
		y = unit.getYPosition();

		a = Action.createPrimitiveMove(0, Direction.EAST);
		result = model.doPrimitiveMove(a, unit);
		assertEquals(FailureMode.FAILED_ATTEMPT, result);
		assertEquals(x, unit.getXPosition());
		assertEquals(y, unit.getYPosition());
		
		a = Action.createPrimitiveMove(0, Direction.SOUTH);
		result = model.doPrimitiveMove(a, unit);
		assertEquals(FailureMode.FAILED_ATTEMPT, result);
		assertEquals(x, unit.getXPosition());
		assertEquals(y, unit.getYPosition());
		
		a = Action.createPrimitiveMove(0, Direction.SOUTHEAST);
		result = model.doPrimitiveMove(a, unit);
		assertEquals(FailureMode.FAILED_ATTEMPT, result);
		assertEquals(x, unit.getXPosition());
		assertEquals(y, unit.getYPosition());
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
		ResourceNode tree = new ResourceNode(new ResourceNodeType("TREE", new ResourceType("WOOD")), 7, 8, 100, 0);
		builder.addResource(tree);
		State state = builder.build();
		model = getModel(state);
		Unit u0 = state.getUnit(0);
		u0.setXPosition(8);
		u0.setYPosition(8);
		
		Action a = Action.createPrimitiveMove(0, Direction.EAST);
		FailureMode result = model.doPrimitiveMove(a, u0);
		assertEquals(FailureMode.FAILED_ATTEMPT, result);
		assertEquals(8, u0.getXPosition());
		assertEquals(8, u0.getYPosition());

		a = Action.createPrimitiveMove(0, Direction.NORTHWEST);
		result = model.doPrimitiveMove(a, u0);
		assertEquals(FailureMode.FAILED_ATTEMPT, result);
		assertEquals(8, u0.getXPosition());
		assertEquals(8, u0.getYPosition());

		a = Action.createPrimitiveMove(0, Direction.WEST);
		result = model.doPrimitiveMove(a, u0);
		assertEquals(FailureMode.FAILED_ATTEMPT, result);
		assertEquals(8, u0.getXPosition());
		assertEquals(8, u0.getYPosition());
	}
	
	@Test
	public void moveFailsImmobileUnit() {
		StateBuilder builder = singleUnitSetup();
		State state = builder.build();
		ModelImpl model = getModel(state);
		Unit unit = state.getUnit(0);
		UnitTemplate template = unit.getTemplate();
		template.setCanMove(false);
		int x = unit.getXPosition();
		int y = unit.getYPosition();
		
		Action a = Action.createPrimitiveMove(0, Direction.WEST);
		FailureMode result = model.doPrimitiveMove(a, unit);
		assertEquals(FailureMode.FAILED_ATTEMPT, result);
		assertEquals(x, unit.getXPosition());
		assertEquals(y, unit.getYPosition());
	}
}
