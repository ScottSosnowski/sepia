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
package edu.cwru.sepia.agent;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.apache.commons.configuration.BaseConfiguration;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.model.LessSimpleModel;
import edu.cwru.sepia.model.Model;
import edu.cwru.sepia.model.SimpleModel;
import edu.cwru.sepia.model.SimpleDurativeModel;
import edu.cwru.sepia.model.state.ResourceNode;
import edu.cwru.sepia.model.state.ResourceNodeType;
import edu.cwru.sepia.model.state.ResourceType;
import edu.cwru.sepia.model.state.State;
import edu.cwru.sepia.model.state.Template;
import edu.cwru.sepia.model.state.Unit;
import edu.cwru.sepia.model.state.UnitTemplate;
import edu.cwru.sepia.pathing.SimplePlanner;
import edu.cwru.sepia.util.TypeLoader;


public class ScriptedGoalAgentTest {
	static Model model;
	static SimplePlanner planner;
	static List<Template<?>> templates;
	static State state;
	static int player=0;
	static Unit founder;
	@BeforeClass
	public static void loadTemplates() throws Exception {
		
		State.StateBuilder builder = new State.StateBuilder();
		state = builder.build();
		templates = TypeLoader.loadFromFile("../Sepia/data/templates.xml",player,state);
		System.out.println("Sucessfully loaded templates");
		
		
		
		builder.setSize(15,15);
		for (Template<?> t : templates) {
			builder.addTemplate(t);
		}
		
		
		{
			
			Unit u = ((UnitTemplate)builder.getTemplate(player, "Peasant")).produceInstance(state);
			u.setXPosition(5);
			u.setYPosition(5);
			founder = u;
			builder.addUnit(u,u.getXPosition(),u.getYPosition());
		}
		{
			ResourceNode rn = new ResourceNode(new ResourceNodeType("GOLD_MINE", new ResourceType("GOLD")), 2, 2, 70000,state.nextTargetId());
			builder.addResource(rn);
		}
		{
			ResourceNode rn = new ResourceNode(new ResourceNodeType("TREE", new ResourceType("WOOD")), 1, 1, 70000,state.nextTargetId());
			builder.addResource(rn);
		}
		
		planner = new SimplePlanner(state);
		model=new SimpleModel(state, null, new BaseConfiguration());
	}
	
	public void setUp() throws Exception {
	}
	
	@Test
	public void test() throws InterruptedException {
		//Get the resources right
		state.addResourceAmount(player, ResourceType.GOLD, 1200);
		state.addResourceAmount(player, ResourceType.WOOD, 800);
		String commands="Build:TownHall:0:0//use your starting peasant to build a town hall\n"+
				"Transfer:1:Idle:Gold//make the builder gather gold\n" +
				"Wait:Gold:500//wait until you have enough gold for a farm\n" +
				"Transfer:1:Gold:Wood//make him gather wood\n" +
				"Wait:Wood:250//until you have enough wood too\n" +
				"Transfer:1:Wood:Idle//then free him up\n" +
				"Build:Farm:-2:2//make him build a farm\n" +
				"Transfer:1:Idle:Gold//make him go back to gold\n" +
				"Produce:Peasant//make a peasant when you can\n" +
				"Transfer:1:Idle:Wood//and put the new guy on woodcutting\n" +
				"Produce:Peasant//make another peasant when you can\n" +
				"Transfer:1:Idle:Gold//and put the new guy on gold\n" +
				"Produce:Peasant//and make another\n" +
				"Transfer:1:Idle:Gold//and put that one on gold too\n" +
				"Wait:Wood:400//when you have enough wood for a barracks\n" +
				"Transfer:1:Wood:Idle//free up the woodcutter to build\n" +
				"Build:Barracks:2:-2//build a barracks\n" +
				"Transfer:1:Idle:Gold//make the builder go to gold\n" +
				"Produce:Footman//make a footman\n"+
				"Attack:All";
		//int ncommands = 11;
		BufferedReader commandreader = new BufferedReader(new StringReader(commands));
		ScriptedGoalAgent agent = new ScriptedGoalAgent(0,commandreader, new Random(), true);
		//VisualAgent vagent = new VisualAgent(0,new String[]{"true","false"});
		for (int step = 0; step<390; step++)
		{
			System.out.println("--------------------------------------------------");
			System.out.println("---------------------"+step+"------------------------");
			Collection<Action> actionsimmut;
			if (step == 0)
			{
				actionsimmut = agent.initialStep(model.getState().getView(player), model.getHistory().getView(player));
				//vagent.initialStep(model.getState().getView(player), model.getHistory().getView(player));
			}
			else
			{
				actionsimmut = agent.middleStep(model.getState().getView(player), model.getHistory().getView(player));
				//vagent.middleStep(model.getState().getView(player), model.getHistory().getView(player));
			}
			Action[] actions = new Action[actionsimmut.size()];
			{
				int i = 0;
				for (Action a : actionsimmut)
				{
					actions[i] = a;
					i++;
				}
			}
			System.out.println("Actions:");
			for (Action a : actions) {
				System.out.println(a);
			}
			System.out.println("Assets("+state.getUnits(player).values().size()+"):");
			Collection<Unit> units = state.getUnits(player).values();
			for (Unit u : units) {
				System.out.println(u.getTemplate().getName() + " (ID: "+u.id+") at "+u.getXPosition() + "," + u.getYPosition());
				System.out.println("Carrying: " + u.getCurrentCargoAmount() + " (" + u.getCurrentCargoType() + ")");
			}
			System.out.println("Resources:");
			for (ResourceNode r : state.getResources()) {
				System.out.println(r.getType() + " " + r.getAmountRemaining());
			}
			System.out.println("All agents control a combined " + state.getUnits().values().size() + " units");
			System.out.println(state.getResourceAmount(player, ResourceType.GOLD)+" Gold");
			System.out.println(state.getResourceAmount(player, ResourceType.WOOD)+" Wood");
			System.out.println(state.getSupplyAmount(player)+"/"+state.getSupplyCap(player) + " Food");
			model.addActions(actionsimmut,agent.getPlayerNumber());
			model.executeStep();
		}
	}
}
