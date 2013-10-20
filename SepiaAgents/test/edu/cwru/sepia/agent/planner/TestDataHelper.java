package edu.cwru.sepia.agent.planner;

import edu.cwru.sepia.model.state.ResourceNode;
import edu.cwru.sepia.model.state.ResourceNodeType;
import edu.cwru.sepia.model.state.ResourceType;
import edu.cwru.sepia.model.state.State;
import edu.cwru.sepia.model.state.Unit;
import edu.cwru.sepia.model.state.UnitTemplate;

public class TestDataHelper {

	public static State createStateWithOneUnit() {
		State state = new State();
		state.setSize(16, 16);
		state.addPlayer(0);
		state.addUnit(new Unit(createTemplate(state, 0, 0), 0), 0, 0);
		return state;
	}
	
	public static State createStateWithTwoUnits() {
		State state = new State();
		state.setSize(16, 16);
		state.addPlayer(0);
		state.addPlayer(1);
		state.addUnit(new Unit(createTemplate(state, 0, 0), 0), 0, 0);
		state.addUnit(new Unit(createTemplate(state, 1, 1), 1), 0, 1);
		return state;
	}
	

	public static State createStateWithTwoSeparatedUnits() {
		State state = new State();
		state.setSize(16, 16);
		state.addPlayer(0);
		state.addPlayer(1);
		state.addUnit(new Unit(createTemplate(state, 0, 0), 0), 0, 0);
		state.addUnit(new Unit(createTemplate(state, 1, 1), 1), 4, 4);
		addTreeLine(state, 0, 3, 2, false);
		return state;
	}

	public static UnitTemplate createTemplate(State state, int id, int player) {
		UnitTemplate template = new UnitTemplate(id);
		template.setBaseHealth(10);
		template.setBasicAttack(5);
		template.setWidth(2);
		template.setHeight(2);
		template.setPlayer(player);
		template.setRange(1);
		state.addTemplate(template);
		return template;
	}
	
	public static void addTreeLine(State state, int start, int end, int invariantComponent, boolean horizontal) {
		ResourceType apple = new ResourceType("apple");
		ResourceNodeType tree = new ResourceNodeType("tree", apple);
		for(int i = start; i <= end; i++) {
			int x = horizontal ? i : invariantComponent;
			int y = horizontal ? invariantComponent : i;
			state.addResource(new ResourceNode(tree, x, y, 100, i));
		}
	}
}
