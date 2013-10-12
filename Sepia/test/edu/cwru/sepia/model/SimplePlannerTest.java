/**
 *  Strategy Engine for Programming Intelligent Agents (SEPIA)
    Copyright (C) 2012 Case Western Reserve University

    This file is part of SEPIA.

    SEPIA is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    SEPIA is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with SEPIA.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.cwru.sepia.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.configuration.BaseConfiguration;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.action.DirectedAction;
import edu.cwru.sepia.model.state.Direction;
import edu.cwru.sepia.model.state.ResourceNode;
import edu.cwru.sepia.model.state.ResourceNodeType;
import edu.cwru.sepia.model.state.ResourceType;
import edu.cwru.sepia.model.state.State;
import edu.cwru.sepia.model.state.Template;
import edu.cwru.sepia.model.state.Unit;
import edu.cwru.sepia.model.state.UnitTemplate;
import edu.cwru.sepia.pathing.SimplePlanner;
import edu.cwru.sepia.util.TypeLoader;

public class SimplePlannerTest {

	static Model model;
	static SimplePlanner planner;
	static List<Template<?>> templates;
	static State state;
	static int player=0;
	
	@BeforeClass
	public static void loadTemplates() throws Exception {
		
		State.StateBuilder builder = new State.StateBuilder();
		builder.setSize(15,15);
		state = builder.build();
		templates = TypeLoader.loadFromFile("data/unit_templates",player, state);		
		System.out.println("Sucessfully loaded templates");

		planner = new SimplePlanner(state);
		
		for(Template<?> t : templates)
		{
			if(!(t instanceof UnitTemplate))
				continue;
			builder.addTemplate(t);
		}
		{
			Unit u = ((UnitTemplate)builder.getTemplate(player, "Peasant")).produceInstance(state);
			builder.addUnit(u,10,10);
		}
		{
			Unit u = ((UnitTemplate)builder.getTemplate(player, "Barracks")).produceInstance(state);
			builder.addUnit(u,0,0);
		}
		{
			Unit u = ((UnitTemplate)builder.getTemplate(player, "Blacksmith")).produceInstance(state);
			builder.addUnit(u,0,1);
		}
		{
			Unit u = ((UnitTemplate)builder.getTemplate(player, "Blacksmith")).produceInstance(state);
			builder.addUnit(u,0,2);
		}
		
		ResourceNodeType tree = new ResourceNodeType("TREE", new ResourceType("WOOD"));
		for(int i = 0; i <= 12; i++)
		{
			ResourceNode t = new ResourceNode(tree, i, 8, 100, state.nextTargetId());
			builder.addResource(t);
		}
		ResourceNode t = new ResourceNode(tree, 7, 2, 100, state.nextTargetId());
		builder.addResource(t);
		t = new ResourceNode(tree, 7, 3, 100, state.nextTargetId());
		builder.addResource(t);
		t = new ResourceNode(tree, 8, 3, 100, state.nextTargetId());
		builder.addResource(t);
		t = new ResourceNode(tree, 8, 4, 100, state.nextTargetId());
		builder.addResource(t);
		t = new ResourceNode(tree, 9, 4, 100, state.nextTargetId());
		builder.addResource(t);
		t = new ResourceNode(tree, 10, 4, 100, state.nextTargetId());
		builder.addResource(t);
		model = new SimpleDurativeModel(state, null, new BaseConfiguration());
	}
	
	public void setUp() throws Exception {
	}
	
	@Test
	public void testPlanMove() {
		LinkedList<Action> plan = planner.planMove(0, 1, 10);
		System.out.println("\n\n");
		for(Action a : plan)
		{
			System.out.println(a);
			assertFalse("Unit moved in wrong direction!",((DirectedAction)a).getDirection() == Direction.NORTH);
			assertFalse("Unit moved in wrong direction!",((DirectedAction)a).getDirection() == Direction.NORTHEAST);
			assertFalse("Unit moved in wrong direction!",((DirectedAction)a).getDirection() == Direction.EAST);
			assertFalse("Unit moved in wrong direction!",((DirectedAction)a).getDirection() == Direction.SOUTHEAST);
			assertFalse("Unit moved in wrong direction!",((DirectedAction)a).getDirection() == Direction.SOUTH);
		}
	}
	@Test
	public void testPlanAndExecuteMove() {
		LinkedList<Action> plan = planner.planMove(0, 1, 10);
		for(Action a : plan)
		{
			model.addActions(Collections.singleton(a), player);
			model.executeStep();
		}
		Unit u = state.getUnit(0);
		assertEquals("Unit's x position did not match the expected value!",1,u.getXPosition());
		assertEquals("Unit's y position did not match the expected value!",10,u.getYPosition());
	}
	@Test
	public void testPlanMoveObstructed() {
		LinkedList<Action> plan = planner.planMove(0, 10, 1);
		System.out.println("\n\n");
		for(Action a : plan)
		{
			System.out.println(a);
			assertFalse("Unit moved in wrong direction!",((DirectedAction)a).getDirection() == Direction.SOUTH);
		}
	}
	@Test
	public void testPlanAndExecuteMoveObstructed() {
		System.out.println("Planning to move to 10,1");
		LinkedList<Action> plan = planner.planMove(0, 10, 1);
		System.out.println("\n\n");
		System.out.println(state.getTextString());
		System.out.println("Plan is: \n" + plan);
		for(Action a : plan)
		{
			model.addActions(Collections.singleton(a), player);
			model.executeStep();
		}
		System.out.println(state.getTextString());
		Unit u = state.getUnit(0);
		System.out.println("Unit position is now: " + u.getXPosition() + "," + u.getYPosition());
		assertEquals("Unit's y position did not match the expected value!",1,u.getYPosition());
		assertEquals("Unit's x position did not match the expected value!",10,u.getXPosition());
		
	}
	@Test
	public void testPlanFollowsShortestPath() {
		LinkedList<Action> plan = planner.planMove(0, 5, 6);
		System.out.println("\n\n");
		for(Action a : plan)
		{
			System.out.println(a);
		}
		assertEquals("Planner did not take shortest path!",8,plan.size());
	}
	@Test
	public void testProduceUnit() {
		
	}
}
