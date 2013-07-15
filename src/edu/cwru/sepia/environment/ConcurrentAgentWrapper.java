package edu.cwru.sepia.environment;

import java.util.Collection;
import java.util.concurrent.Callable;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.agent.Agent;
import edu.cwru.sepia.model.Model;
import edu.cwru.sepia.model.history.History.HistoryView;
import edu.cwru.sepia.model.state.State.StateView;
import edu.cwru.sepia.util.Pair;

public class ConcurrentAgentWrapper {

	private final Agent agent;
	private ParameterizedCallable<Pair<Integer,Collection<Action>>> initialStepCallable;
	private ParameterizedCallable<Pair<Integer,Collection<Action>>> middleStepCallable;
	private ParameterizedCallable<Void> terminalStepCallable;
	
	public ConcurrentAgentWrapper(final Agent agent) {
		this.agent = agent;
		initialStepCallable = new ParameterizedCallable<Pair<Integer,Collection<Action>>>() {
			@Override
			public Pair<Integer,Collection<Action>> call() {
				return new Pair<Integer,Collection<Action>>(agent.getPlayerNumber(), agent.initialStep(state, history));
			}
		};
		middleStepCallable = new ParameterizedCallable<Pair<Integer,Collection<Action>>>() {
			@Override
			public Pair<Integer,Collection<Action>> call() {
				return new Pair<Integer,Collection<Action>>(agent.getPlayerNumber(), agent.middleStep(state, history));
			}
		};
		terminalStepCallable = new ParameterizedCallable<Void>() {
			@Override
			public Void call() throws Exception {
				agent.terminalStep(state, history);
				return null;
			}
		};
	}
	
	public Callable<Pair<Integer,Collection<Action>>> initialStep(Model model) {
		initialStepCallable.state = model.getState().getView(agent.getPlayerNumber());
		initialStepCallable.history = model.getHistory().getView(agent.getPlayerNumber());
		return initialStepCallable;
	}
	
	public Callable<Pair<Integer,Collection<Action>>> middleStep(Model model) {
		middleStepCallable.state = model.getState().getView(agent.getPlayerNumber());
		middleStepCallable.history = model.getHistory().getView(agent.getPlayerNumber());
		return middleStepCallable;
	}
	
	public Callable<Void> terminalStep(Model model) {
		terminalStepCallable.state = model.getState().getView(agent.getPlayerNumber());
		terminalStepCallable.history = model.getHistory().getView(agent.getPlayerNumber());
		return terminalStepCallable;
	}
	
	private static abstract class ParameterizedCallable<V> implements Callable<V> {
		public StateView state;
		public HistoryView history;
	}
}
