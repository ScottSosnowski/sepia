package edu.cwru.sepia.model;

import java.util.Collection;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.junit.Before;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.environment.TurnTracker;
import edu.cwru.sepia.model.AbstractModel.FailureMode;
import edu.cwru.sepia.model.state.PlayerState;
import edu.cwru.sepia.model.state.State;
import edu.cwru.sepia.model.state.StateCreator;
import edu.cwru.sepia.model.state.Unit;
import edu.cwru.sepia.model.state.UnitTemplate;
import edu.cwru.sepia.model.state.State.StateBuilder;

public abstract class AbstractModelTestBase {

	protected Configuration configuration;
	protected ModelImpl model;
	protected Logger logger;
	
	@Before
	public void setup() {
		configuration = new BaseConfiguration();
		logger = Logger.getLogger(ModelImpl.class.getCanonicalName());
		Handler[] handlers = logger.getHandlers();
		for(Handler handler : handlers)
			logger.removeHandler(handler);
		logger.setUseParentHandlers(false);
		logger.setLevel(Level.OFF);
	}
	
	protected StateBuilder singleUnitSetup() {
		StateBuilder builder = new StateBuilder();
		builder.setSize(64, 64);
		PlayerState ps0 = new PlayerState(0);
		ps0.setVisibilityMatrix(new int[64][64]);
		builder.addPlayer(ps0);
		UnitTemplate template = new UnitTemplate(0);
		template.setBaseHealth(10);
		template.setName("t0");
		template.setCanBuild(true);
		template.setCanGather(true);
		template.setCanMove(true);
		Unit u0 = new Unit(template, 0);
		u0.setxPosition(8);
		u0.setyPosition(8);
		ps0.addTemplate(template);
		ps0.addUnit(u0);
		builder.addUnit(u0, u0.getxPosition(), u0.getyPosition());
		return builder;
	}
	
	protected ModelImpl getModel(State state) {
		return new ModelImpl(state, state.getStateCreator(), configuration, logger);
	}

	protected static class ModelImpl extends AbstractModel {
		private static final long serialVersionUID = 1L;

		public ModelImpl(State init, StateCreator restartTactic, Configuration configuration, Logger logger) {
			super(init, restartTactic, configuration, logger);
		}

		@Override
		public void createNewWorld() {}

		@Override
		public void addActions(Collection<Action> actions, int sendingPlayerNumber) {}

		@Override
		public void executeStep() {}

		@Override
		public void setTurnTracker(TurnTracker turnTracker) {}
		
		public ActionMethod getPrimitiveMove() {
			return new ActionMethod() {
				@Override public FailureMode execute(Action action, Unit unit, int x, int y) { 
					return doPrimitiveMove(action, unit, x, y); 
				}
			};
		}

		public ActionMethod getPrimitiveGather() {
			return new ActionMethod() {
				@Override public FailureMode execute(Action action, Unit unit, int x, int y) { 
					return doPrimitiveGather(action, unit, x, y); 
				}
			};
		}

		public ActionMethod getPrimitiveDeposit() {
			return new ActionMethod() {
				@Override public FailureMode execute(Action action, Unit unit, int x, int y) { 
					return doPrimitiveDeposit(action, unit, x, y); 
				}
			};
		}

		public ActionMethod getPrimitiveAttack() {
			return new ActionMethod() {
				@Override public FailureMode execute(Action action, Unit unit, int x, int y) { 
					return doPrimitiveAttack(action, unit, x, y); 
				}
			};
		}

		public ActionMethod getPrimitiveProduce() {
			return new ActionMethod() {
				@Override public FailureMode execute(Action action, Unit unit, int x, int y) { 
					return doPrimitiveProduce(action, unit, x, y); 
				}
			};
		}
		
		public ActionMethod getPrimitiveBuild() {
			return new ActionMethod() {
				@Override public FailureMode execute(Action action, Unit unit, int x, int y) { 
					return doPrimitiveBuild(action, unit, x, y); 
				}
			};
		}
	}

	protected static interface ActionMethod {
		public FailureMode execute(Action action, Unit unit, int x, int y);
	}
}
