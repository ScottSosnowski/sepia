package edu.cwru.sepia.environment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.agent.Agent;
import edu.cwru.sepia.model.Model;
import edu.cwru.sepia.util.Pair;

public class Environment2 {
	private static final Logger logger = Logger.getLogger(Environment2.class.getCanonicalName());
	
	private final ConcurrentAgentWrapper[] agents;
	private final Model model;
	private final ExecutorService executor;
	
	public Environment2(Model model, Agent... agents) {
		this.agents = new ConcurrentAgentWrapper[agents.length];
		for(int i = 0; i < agents.length; i++) {
			this.agents[i] = new ConcurrentAgentWrapper(agents[i]);
		}
		this.model = model;
		executor = Executors.newCachedThreadPool();
	}
	
	public void run() {
		while(!step()){}
	}
	
	public boolean step() {
		if(model.isTerminated())
			return true;
		
		Map<Integer,Collection<Action>> actions = null;
		if(model.getState().getTurnNumber() == 0) {
			actions = initialStep();
		} else {
			actions = middleStep();
		}
		for(Integer playerNumber : actions.keySet()) {
			model.addActions(actions.get(playerNumber), playerNumber);
		}
		model.executeStep();
		
		if(model.isTerminated()) {
			terminalStep();
		}
		return model.isTerminated();
	}
	
	private Map<Integer,Collection<Action>> initialStep() {
		List<Callable<Pair<Integer,Collection<Action>>>> callables = 
				new ArrayList<Callable<Pair<Integer,Collection<Action>>>>(agents.length);
		for(ConcurrentAgentWrapper agent : agents) {
			Callable<Pair<Integer,Collection<Action>>> callable = agent.initialStep(model);
			callables.add(callable);
		}
		return executeAndGetActions(callables);
	}

	private Map<Integer,Collection<Action>> middleStep() {
		List<Callable<Pair<Integer,Collection<Action>>>> callables = 
				new ArrayList<Callable<Pair<Integer,Collection<Action>>>>(agents.length);
		for(ConcurrentAgentWrapper agent : agents) {
			callables.add(agent.middleStep(model));
		}
		return executeAndGetActions(callables);
	} 
	
	private void terminalStep() {
		List<Callable<Void>> callables = 
				new ArrayList<Callable<Void>>(agents.length);
		for(ConcurrentAgentWrapper agent : agents) {
			callables.add(agent.terminalStep(model));
		}
		executeAll(callables);
	}
	
	private Map<Integer,Collection<Action>> executeAndGetActions(List<Callable<Pair<Integer,Collection<Action>>>> callables) {
		List<Pair<Integer,Collection<Action>>> actions = executeAll(callables);
		Map<Integer,Collection<Action>> playersToActions = new HashMap<Integer,Collection<Action>>();
		for(Pair<Integer,Collection<Action>> pair : actions) {
			Collection<Action> copy = new ArrayList<Action>(pair.b);
			playersToActions.put(pair.a, copy);
		}
		return playersToActions;
	}
	
	private <V> List<V> executeAll(List<Callable<V>> callables) {
		List<V> actions = new ArrayList<V>(agents.length);
		try {
			List<Future<V>> futures = executor.invokeAll(callables);
			for(Future<V> future : futures) {
				try {
					actions.add(future.get());
				} catch(ExecutionException ex) {
					//agents that throw exceptions don't get to take their turn
					//hopefully the exception will have enough information by itself
					logger.log(Level.WARNING, "An agent threw an exception.", ex);
				}
			}
		} catch(InterruptedException ex) {
			//interruption is not expected and probably means the program should shut down
			logger.log(Level.SEVERE, "Interrupted while awaiting actions from agents.", ex);
			throw new RuntimeException("Interrupted while awaiting actions from agents.", ex);
		}
		return actions;
	}
}
