package edu.cwru.sepia.agent;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import edu.cwru.sepia.Main;
import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.action.ActionResult;
import edu.cwru.sepia.model.history.History.HistoryView;
import edu.cwru.sepia.model.state.ResourceNode;
import edu.cwru.sepia.model.state.ResourceNode.ResourceView;
import edu.cwru.sepia.model.state.State.StateView;
import edu.cwru.sepia.model.state.Unit.UnitView;

public class SampleGatheringAgent extends Agent {
	private static final long serialVersionUID = 1L;

	public SampleGatheringAgent(int playernum) {
		super(playernum);
	}

	@Override
	public Collection<Action> initialStep(StateView state, HistoryView history) {
		System.out.println("Initial step");
		List<Action> actions = new ArrayList<>();
		for(UnitView unit : state.getUnits(playernum)) {
			if(unit.getTemplateView().canGather()) {
				System.out.println("assigning action to unit " + unit.getID());
				actions.add(chooseAction(state, unit.getID()));
			} else {
				System.out.println("Not assigning action to non-gathering unit " + unit.getID());
			}
		}
		return actions;
	}

	@Override
	public Collection<Action> middleStep(StateView state, HistoryView history) {
		System.out.println("Step " + state.getTurnNumber());
		List<Action> actions = new ArrayList<>();
		
		Map<Integer, ActionResult> commandFeedback = history.getCommandFeedback(playernum, state.getTurnNumber() - 1);
		for(Map.Entry<Integer, ActionResult> entry : commandFeedback.entrySet()) {
			ActionResult result = entry.getValue();
			switch(result.getFeedback()) {
				case INCOMPLETE:
				case INCOMPLETEMAYBESTUCK:
					System.out.println("Waiting for " + entry.getKey() + " to finish its action");
					continue;
				default:
					System.out.println("Unit " + entry.getKey() + " completed its action with result " + result.getFeedback());
					actions.add(chooseAction(state, entry.getKey()));
			}
		}
		
		return actions;
	}
	
	private Action chooseAction(StateView state, Integer unitId) {
		UnitView actor = state.getUnit(unitId);
		if(actor.getCargoAmount() > 0) {
			UnitView townHall = null;
			for(UnitView unit : state.getUnits(playernum)) {
				if(unit.getTemplateView().getCharacter() == 'H') {
					townHall = unit;
					break;
				}
			}
			return Action.createCompoundDeposit(unitId, townHall.getID());
		} else {
			List<ResourceView> goldMines = state.getResourceNodes(ResourceNode.Type.GOLD_MINE);
			ResourceView mine = goldMines.get(0);
			return Action.createCompoundGather(unitId, mine.getID());
		}
	}

	@Override
	public void terminalStep(StateView state, HistoryView history) {
		System.out.println("Episode complete");
	}

	@Override
	public void savePlayerData(OutputStream os) {
	}

	@Override
	public void loadPlayerData(InputStream is) {
	}
}
