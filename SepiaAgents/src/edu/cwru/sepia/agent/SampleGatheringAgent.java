package edu.cwru.sepia.agent;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.action.ActionResult;
import edu.cwru.sepia.model.history.BirthLog;
import edu.cwru.sepia.model.history.History.HistoryView;
import edu.cwru.sepia.model.state.ResourceNode.ResourceView;
import edu.cwru.sepia.model.state.ResourceType;
import edu.cwru.sepia.model.state.State.StateView;
import edu.cwru.sepia.model.state.Template.TemplateView;
import edu.cwru.sepia.model.state.Unit.UnitView;

public class SampleGatheringAgent extends Agent {
	private static final long serialVersionUID = 1L;

	private boolean townHallBusy;

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
		townHallBusy = false;
		return actions;
	}

	@Override
	public Collection<Action> middleStep(StateView state, HistoryView history) {
		System.out.println("Step " + state.getTurnNumber());
		List<Action> actions = new ArrayList<>();

		Map<Integer, ActionResult> commandFeedback = history.getCommandFeedback(playernum,
				state.getTurnNumber() - 1);
		for(Map.Entry<Integer, ActionResult> entry : commandFeedback.entrySet()) {
			ActionResult result = entry.getValue();
			switch(result.getFeedback()) {
			case INCOMPLETE:
			case INCOMPLETEMAYBESTUCK:
				System.out.println("Waiting for " + entry.getKey() + " to finish its action");
				continue;
			default:
				UnitView unit = state.getUnit(result.getAction().getUnitId());
				System.out.println("Unit " + entry.getKey() + " completed its action "
						+ result.getAction().getType() + " with result " + result.getFeedback());
				if(unit.getTemplateView().getCharacter() == 'p') {
					System.out.println("Unit " + entry.getKey() + " is now holding "
							+ unit.getCargoAmount() + " of " + unit.getCargoType());
					actions.add(chooseAction(state, entry.getKey()));
				} else if(unit.getTemplateView().getCharacter() == 'H')
					townHallBusy = false;
			}
		}

		List<BirthLog> births = history.getBirthLogs(state.getTurnNumber() - 1);
		for(BirthLog birth : births) {
			System.out.println("New unit " + birth.getNewUnitID() + " was created by "
					+ birth.getParentID());
			actions.add(chooseAction(state, birth.getNewUnitID()));
		}

		Action buildPeasant = buildPeasantIfWanted(state);
		if(buildPeasant != null) {
			actions.add(buildPeasant);
			townHallBusy = true;
		}

		return actions;
	}

	private Action chooseAction(StateView state, Integer unitId) {
		UnitView actor = state.getUnit(unitId);
		if(actor.getCargoAmount() > 0) {
			UnitView townHall = findTownHall(state);
			return Action.createCompoundDeposit(unitId, townHall.getID());
		} else {
			List<ResourceView> goldMines = state.getResourceNodes("GOLD_MINE");
			ResourceView mine = goldMines.get(0);
			return Action.createCompoundGather(unitId, mine.getID());
		}
	}

	private Action buildPeasantIfWanted(StateView state) {
		if(townHallBusy)
			return null;

		UnitView townHall = findTownHall(state);

		List<UnitView> units = state.getUnits(playernum);
		int peasantCount = 0;
		for(UnitView unit : units) {
			if(unit.getTemplateView().getCharacter() == 'p')
				peasantCount++;
		}
		if(peasantCount >= 2)
			return null;

		TemplateView template = state.getTemplate(playernum, "Peasant");
		if(state.getResourceAmount(playernum, ResourceType.GOLD) >= template.getGoldCost()
				&& state.getResourceAmount(playernum, ResourceType.WOOD) >= template.getWoodCost()
				&& state.getSupplyCap(playernum) - state.getSupplyAmount(playernum) >= template
						.getFoodCost()) {
			return Action.createPrimitiveProduction(townHall.getID(), template.getID());
		} else {
			return null;
		}
	}

	private UnitView findTownHall(StateView state) {
		UnitView townHall = null;
		for(UnitView unit : state.getUnits(playernum)) {
			if(unit.getTemplateView().getCharacter() == 'H') {
				townHall = unit;
				break;
			}
		}
		return townHall;
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
