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
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;

import org.apache.commons.configuration.BaseConfiguration;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.model.SimpleDurativeModel;
import edu.cwru.sepia.model.state.State;
import edu.cwru.sepia.model.state.Template;
import edu.cwru.sepia.model.state.Unit;
import edu.cwru.sepia.model.state.UnitTemplate;
import edu.cwru.sepia.pathing.SimplePlanner;
import edu.cwru.sepia.util.TypeLoader;


public class CombatAgentDuelTest {
	static SimpleDurativeModel model;
	static SimplePlanner planner;
	static List<Template<?>> templates1;
	static List<Template<?>> templates2;
	static State state;
	static int player1 = 0;
	static int player2 = 1;
	@BeforeClass
	public static void loadTemplates() throws Exception {
		
		State.StateBuilder builder = new State.StateBuilder();
		
		
		
		builder.setSize(15,15);
		state = builder.build();
		
		
		templates1 = TypeLoader.loadFromFile("data/unit_templates",player1,state);		
		System.out.println("Sucessfully loaded templates");
		for (Template<?> t : templates1) {
			builder.addTemplate(t);
		}
		templates2 = TypeLoader.loadFromFile("data/unit_templates",player2,state);		
		System.out.println("Sucessfully loaded templates");
		for (Template<?> t : templates2) {
			builder.addTemplate(t);
		}
		
		{
			Unit u = ((UnitTemplate)builder.getTemplate(player1, "Footman")).produceInstance(state);
			u.setXPosition(5);
			u.setYPosition(5);
			builder.addUnit(u,u.getXPosition(),u.getYPosition());
		}
		{
			Unit u = ((UnitTemplate)builder.getTemplate(player1, "Footman")).produceInstance(state);
			u.setXPosition(5);
			u.setYPosition(4);
			builder.addUnit(u,u.getXPosition(),u.getYPosition());
		}
		
		
		{
			Unit u = ((UnitTemplate)builder.getTemplate(player2, "Footman")).produceInstance(state);
			u.setXPosition(6);
			u.setYPosition(5);
			builder.addUnit(u,u.getXPosition(),u.getYPosition());
		}
		{
			Unit u = ((UnitTemplate)builder.getTemplate(player2, "Footman")).produceInstance(state);
			u.setXPosition(6);
			u.setYPosition(4);
			builder.addUnit(u,u.getXPosition(),u.getYPosition());
		}
		
		planner = new SimplePlanner(state);
		model=new SimpleDurativeModel(state, null, new BaseConfiguration());
	}
	
	public void setUp() throws Exception {
	}
	
	@Test
	public void test() throws IOException, InterruptedException {
		CombatAgent agent1 = new CombatAgent(player1, new String[]{Integer.toString(player2), "false", "false" });
		CombatAgent agent2 = new CombatAgent(player2, new String[]{Integer.toString(player1), "false", "true"});
		for (int step = 0; step<30500; step++)
		{
			Collection<Action> acts1;
			Collection<Action> acts2;
			if (step == 0)
			{
				acts1=agent1.initialStep(model.getState().getView(player1),model.getHistory().getView(player1));
				acts2=agent2.initialStep(model.getState().getView(player2),model.getHistory().getView(player2));
			}
			else
			{
				acts1=agent1.middleStep(model.getState().getView(player1),model.getHistory().getView(player1));
				acts2=agent2.middleStep(model.getState().getView(player2),model.getHistory().getView(player2));
			}
			Collection<Action> actionsimmut1 = acts1;
			Collection<Action> actionsimmut2 = acts2;
			Action[] actions = new Action[actionsimmut1.size() + actionsimmut2.size()];
			{
				int i = 0;
				for (Action a : actionsimmut1)
				{
					actions[i] = a;
					i++;
				}
				for (Action a : actionsimmut2)
				{
					actions[i] = a;
					i++;
				}
			}
			System.out.println("Actions:");
			for (Action a : actions) {
				System.out.println(a);
			}
			System.out.println(state.getTextString());
			new BufferedReader(new InputStreamReader(System.in)).readLine();
//			System.out.println("Assets("+state.getUnits(player1).values().size()+"):");
//			Collection<Unit> units = state.getUnits(player1).values();
//			for (Unit u : units) {
//				System.out.println(u.getTemplate().getName() + " (ID: "+u.ID+") at "+u.getXPosition() + "," + u.getYPosition());
//				System.out.println("Carrying: " + u.getCurrentCargoAmount() + " (" + u.getCurrentCargoType() + ")");
//			}
//			System.out.println("All agents control a combined " + state.getUnits().values().size() + " units");
//			System.out.println(state.getResourceAmount(player, ResourceType.GOLD)+" Gold");
//			System.out.println(state.getResourceAmount(player, ResourceType.WOOD)+" Wood");
//			System.out.println(state.getSupplyAmount(player)+"/"+state.getSupplyCap(player) + " Food");
			model.addActions(acts1,player1);
			model.addActions(acts2,player2);
			model.executeStep();
		}
	}
}
