package edu.cwru.sepia.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.configuration.Configuration;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.action.DirectedAction;
import edu.cwru.sepia.action.LocatedAction;
import edu.cwru.sepia.action.ProductionAction;
import edu.cwru.sepia.action.TargetedAction;
import edu.cwru.sepia.model.history.History;
import edu.cwru.sepia.model.state.Direction;
import edu.cwru.sepia.model.state.ResourceNode;
import edu.cwru.sepia.model.state.ResourceType;
import edu.cwru.sepia.model.state.State;
import edu.cwru.sepia.model.state.StateCreator;
import edu.cwru.sepia.model.state.Template;
import edu.cwru.sepia.model.state.Unit;
import edu.cwru.sepia.model.state.UnitTemplate;
import edu.cwru.sepia.model.state.UpgradeTemplate;
import edu.cwru.sepia.util.ConfigurationAccessors;
import edu.cwru.sepia.util.DistanceMetrics;
import edu.cwru.sepia.util.Pair;


public abstract class AbstractModel implements Model {
	protected static final long serialVersionUID = 1L;
	
	protected Configuration configuration;
	protected State state;
	protected History history;
	protected Random random;
	protected Logger logger;
	protected StateCreator restartTactic;
	
	public AbstractModel(State init, StateCreator restartTactic, Configuration configuration, Logger logger) {
		this.state = init;
		this.configuration = configuration;
		this.restartTactic = restartTactic;
		this.logger = logger;
		random = new Random(configuration.getInt("RandomSeed", 6));
		history = new History();
		for (Integer i : state.getPlayers())
			history.addPlayer(i);
	}
	
	@Override
	public boolean isTerminated() {
		Set<Integer> winners = new HashSet<Integer>();
		if(ConfigurationAccessors.isConquest(configuration)) {
			int player = conquestTerminated();
			if(player == -2)
				return true;//everyone is dead; nothing else can be done
			if(player == -1)
				return false;
			winners.add(player);
		}
		
		if(ConfigurationAccessors.isMidas(configuration)) {
			Set<Integer> resourcePlayers = resourceGatheringTerminated();
			//conquering player does not have required resources
			if(!winners.isEmpty() && Collections.disjoint(winners, resourcePlayers))
				return false;
			winners.addAll(resourcePlayers); 
		}
		if(ConfigurationAccessors.isManifestDestiny(configuration)) {
			Set<Integer> buildingPlayers = buildingTerminated();
			if(winners.isEmpty())
				winners.addAll(buildingPlayers);
			else
				winners.retainAll(buildingPlayers);
		}
		return !winners.isEmpty() && state.getTurnNumber() <= ConfigurationAccessors.timeLimit(configuration);
	}

	@Override
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}
	
	@Override
	public Configuration getConfiguration() {
		return configuration;
	}
	
	protected int conquestTerminated() {
		int numLivePlayers = 0;
		int livePlayer = -2;
		for(Integer player : state.getPlayers())
		{
			if(state.getUnits(player).size() == 0)
			{
				continue;
			}
			for(Unit u : state.getUnits(player).values())
			{
				if(u.getCurrentHealth() > 0)
				{
					livePlayer = player;
					numLivePlayers++;
					break;
				}
			}
			if (numLivePlayers > 1)
				break;
			
		}
		return numLivePlayers <= 1 ? livePlayer : -1;
	}
	
	protected Set<Integer> resourceGatheringTerminated() {
		int gold = ConfigurationAccessors.requiredGold(configuration);
		int wood = ConfigurationAccessors.requiredWood(configuration);
		Set<Integer> successfulPlayers = new HashSet<Integer>();
		for(Integer player : state.getPlayers())
		{
			if(state.getResourceAmount(player, ResourceType.GOLD) >= gold &&
			   state.getResourceAmount(player, ResourceType.WOOD) >= wood) {
				successfulPlayers.add(player);
			}
		}
		return successfulPlayers;
	}
	
	protected Set<Integer> buildingTerminated() {
		Set<Integer> successfulPlayers = new HashSet<Integer>();
		for(Integer i : state.getPlayers())
		{
			boolean built = true;
			for(Template<?> template : state.getTemplates(i).values())
			{
				int required = 0;
				if(configuration.containsKey("model.Required"+template.getName()+"Player"+i))
					required = configuration.getInt("model.Required"+template.getName()+"Player"+i);
				int actual = 0;
				if (required > 0) //Only check if you need to find at least one
				{
					for(Unit u : state.getUnits(i).values())
					{
						if(u.getTemplate().equals(template))
							actual++;
						if(actual >= required) //if you found enough of a type of unit, you can stop looking for more
							break;
					}
				}
//				System.out.println("Player "+i+" has at least "+actual + "{"+template.getName()+"}s"+" (needed "+required+")");
				built = built && (actual >= required);
				if (!built) //if you haven't built one of the requirements, you can't have built all of them
					break;
			}
			if (built)
				successfulPlayers.add(i);
		}
		return successfulPlayers;
	}
	
	@Override
	public State getState() {
		return state;
	}
	
	@Override
	public History getHistory() {
		return history;
	}

	protected Pair<Integer,Integer> getDestination(Action a, int x, int y) {
		int xPrime = 0;
		int yPrime = 0;
		if(a instanceof DirectedAction)
		{
			Direction d = ((DirectedAction)a).getDirection();
			xPrime = x + d.xComponent();
			yPrime = y + d.yComponent();
		}
		else if(a instanceof LocatedAction)
		{
			xPrime = x + ((LocatedAction)a).getX();
			yPrime = y + ((LocatedAction)a).getY();
		}
		return new Pair<Integer,Integer>(xPrime, yPrime);
	}

	protected FailureMode doAction(Action a, Unit u, int x, int y, int xPrime, int yPrime) {
		switch(a.getType())
		{
			case PRIMITIVEMOVE:
				return doPrimitiveMove(a, u, xPrime, yPrime);
			case PRIMITIVEGATHER:
				return doPrimitiveGather(a, u, xPrime, yPrime);
			case PRIMITIVEDEPOSIT:
				return doPrimitiveDeposit(a, u, xPrime, yPrime);
			case PRIMITIVEATTACK:
				return doPrimitiveAttack(a, u, xPrime, yPrime);
			case PRIMITIVEBUILD:
				return doPrimitiveBuild(a, u, x, y);
			case PRIMITIVEPRODUCE:
				return doPrimitiveProduce(a, u, x, y);
			case FAILED:
			case FAILEDPERMANENTLY:
				return FailureMode.FAILED_ATTEMPT;
			default:
				logger.warning("Non-primitive action " + a.getType() + " was queued.");
				return FailureMode.WRONG_ACTION_TYPE;
		}
	}
	
	protected FailureMode doPrimitiveMove(Action a, Unit u, int xPrime, int yPrime) {
		if (!(a instanceof DirectedAction))
			return FailureMode.WRONG_ACTION_TYPE;
		if(state.inBounds(xPrime, yPrime) && u.canMove() && empty(xPrime,yPrime)) {
			if (logger.isLoggable(Level.FINE))
				logger.fine("Moving unit " + u.id);
			state.moveUnit(u, ((DirectedAction)a).getDirection());
			return FailureMode.SUCCESS;
		} else {
			return FailureMode.FAILED_ATTEMPT;
		}
	}
	
	protected FailureMode doPrimitiveGather(Action a, Unit u, int xPrime, int yPrime) {
		if (!(a instanceof DirectedAction))
			return FailureMode.WRONG_ACTION_TYPE;
		ResourceNode resource = state.resourceAt(xPrime, yPrime);
		if(resource == null)
			return FailureMode.INVALID_TARGET;
		else if(!u.canGather()) 
			return FailureMode.WRONG_UNIT_TYPE;
		
		if (logger.isLoggable(Level.FINE))
			logger.fine(u.id + " gathering from " + resource.id);
		int amountPickedUp = resource.reduceAmountRemaining(u.getTemplate().getGatherRate(resource.getType()));
		u.setCargo(resource.getResourceType(), amountPickedUp);
		history.recordResourcePickup(u, resource, amountPickedUp, state);
		return FailureMode.SUCCESS;
	}
	
	protected FailureMode doPrimitiveDeposit(Action a, Unit u, int xPrime, int yPrime) {
		if (!(a instanceof DirectedAction))
			return FailureMode.WRONG_ACTION_TYPE;
		//only can do a primitive if you are in the right position
		Unit townHall = state.unitAt(xPrime, yPrime);
		boolean canAccept=false;
		if (townHall!=null && townHall.getPlayer() == u.getPlayer()) {
			if (u.getCurrentCargoType() == ResourceType.GOLD && townHall.getTemplate().canAcceptGold())
				canAccept=true;
			else if (u.getCurrentCargoType() == ResourceType.WOOD && townHall.getTemplate().canAcceptWood())
				canAccept=true;
		}
		if(!canAccept) {
			if (logger.isLoggable(Level.FINE))
				logger.fine("Unable to deposit to " + townHall.id + " at " + xPrime + "," + yPrime);
			return FailureMode.INVALID_TARGET;
		} else {
			if (logger.isLoggable(Level.FINE))
				logger.fine(u.id + " depositing to " + townHall.id);
			int agent = u.getPlayer();
			history.recordResourceDropoff(u, townHall, state);
			state.addResourceAmount(agent, u.getCurrentCargoType(), u.getCurrentCargoAmount());
			u.clearCargo();
			return FailureMode.SUCCESS;
		}
	}
	
	protected FailureMode doPrimitiveAttack(Action a, Unit u, int xPrime, int yPrime) {
		if (!(a instanceof TargetedAction))
			return FailureMode.WRONG_ACTION_TYPE;
		Unit target = state.getUnit(((TargetedAction)a).getTargetId());
		if (target == null)
		{
			if (logger.isLoggable(Level.FINE))
				logger.fine(u.id + " failed to attack non-existent unit");
			return FailureMode.INVALID_TARGET;
			
		}
		int distance = DistanceMetrics.chebyshevDistance(u.getxPosition(), u.getyPosition(), 
												target.getxPosition(), target.getyPosition());
		if (u.getTemplate().getRange() < distance) {
			if (logger.isLoggable(Level.FINE))
				logger.fine(u.id + " failed to attack out-of-range unit " + target.id);
			return FailureMode.INVALID_TARGET;
		} else {
			int damage = calculateDamage(u,target);
			if (logger.isLoggable(Level.FINE))
			logger.fine(u.id + " did " + damage + " damage to " + target.id);
			history.recordDamage(u, target, damage, state);
			target.setHP(Math.max(0, target.getCurrentHealth()-damage));
			return FailureMode.SUCCESS;
		}
	}
	
	protected FailureMode doPrimitiveBuild(Action a, Unit u, int x, int y) {
		if (!(a instanceof ProductionAction))
			return FailureMode.WRONG_ACTION_TYPE;
		UnitTemplate template = (UnitTemplate)state.getTemplate(((ProductionAction)a).getTemplateId());
		if (!u.getTemplate().canProduce(template))
			return FailureMode.WRONG_UNIT_TYPE;
		boolean prerequisitesMet = true;
		//check if the prerequisites for the template's production are met
		for (Integer buildingtemplateid : template.getBuildPrerequisites()) {
			if (!state.hasUnit(u.getPlayer(), buildingtemplateid)) {
				prerequisitesMet = false;
				break;
			}
		}
		if (prerequisitesMet) {
			for (Integer upgradeTemplateId : template.getUpgradePrerequisites()) {
				if (!state.hasUpgrade(u.getPlayer(),upgradeTemplateId)) {
					prerequisitesMet = false;
					break;
				}
			}
		}
		if (prerequisitesMet) {
			Unit building = template.produceInstance(state);
			int[] newxy = state.getClosestPosition(x,y);
			if (state.tryProduceUnit(building,newxy[0],newxy[1])) {
				history.recordBirth(building, u, state);
			}
			if (logger.isLoggable(Level.FINE))
				logger.fine("Built building " + building.id + " at " + newxy[0] + "," + newxy[1]);
			return FailureMode.SUCCESS;
		} else {
			if (logger.isLoggable(Level.FINE))
				logger.fine(u.id + " failed building because prerequisites for template " + template.ID + " were not met");
			return FailureMode.MISSING_PREREQUISITES;
		}
	}

	protected FailureMode doPrimitiveProduce(Action a, Unit u, int x, int y) {
		if (!(a instanceof ProductionAction))
			return FailureMode.WRONG_ACTION_TYPE;
		Template<?> template = state.getTemplate(((ProductionAction)a).getTemplateId());
		//check if it is even capable of producing the template
		if(!u.getTemplate().canProduce(template))
			return FailureMode.WRONG_UNIT_TYPE;
		boolean prerequisitesMet = true;
		//check if the prerequisites for the template's production are met
		for (Integer buildingtemplateid : template.getBuildPrerequisites()) {
			if (!state.hasUnit(u.getPlayer(), buildingtemplateid)) {
				prerequisitesMet = false;
				break;
			}
		}
		if (prerequisitesMet) {
			for (Integer upgradetemplateid : template.getUpgradePrerequisites()) {
				if (!state.hasUpgrade(u.getPlayer(), upgradetemplateid)) {
					prerequisitesMet = false;
					break;
				}
			}
		}
		if(!prerequisitesMet)
			return FailureMode.MISSING_PREREQUISITES;

		if (template instanceof UnitTemplate) {
			Unit produced = ((UnitTemplate)template).produceInstance(state);
			int[] newxy = state.getClosestPosition(x, y);
			if (state.tryProduceUnit(produced,newxy[0], newxy[1]))
				history.recordBirth(produced, u, state);
			else
				return FailureMode.FAILED_ATTEMPT;
			if (logger.isLoggable(Level.FINE))
				logger.fine("Produced unit " + produced.id + " at " + newxy[0] + "," + newxy[1]);
		} else if (template instanceof UpgradeTemplate) {
			UpgradeTemplate upgradetemplate = ((UpgradeTemplate)template);
			if (state.tryProduceUpgrade(upgradetemplate.produceInstance(state)))
				history.recordUpgrade(upgradetemplate,u, state);
			else
				return FailureMode.FAILED_ATTEMPT;
			if (logger.isLoggable(Level.FINE))
				logger.fine("Upgrade " + upgradetemplate.getName() + " produced for player " + upgradetemplate.getPlayer());
		}
		return FailureMode.SUCCESS;
	}
	
	protected void performCleanup() {
		//Take all the dead units and clear them
		//Find the dead units
		Map<Integer, Unit> allunits = state.getUnits();
		List<Integer> dead= new ArrayList<Integer>(allunits.size());
		for (Unit u : allunits.values()) {
			if (u.getCurrentHealth() <= 0) {
				history.recordDeath(u, state);
				dead.add(u.id);
				if (logger.isLoggable(Level.FINE))
					logger.fine("Unit " + u.id + " has died.");
			}
		}
		//Remove them
		for (int uid : dead)
			state.removeUnit(uid);
		//Take all of the used up resources and get rid of them
		List<ResourceNode> allnodes = state.getResources();
		List<Integer> usedup= new ArrayList<Integer>(allnodes.size());
		for (ResourceNode r : allnodes) {
			if (r.getAmountRemaining() <= 0) {
				history.recordResourceNodeExhaustion(r, state);
				usedup.add(r.id);
				if (logger.isLoggable(Level.FINE))
					logger.fine("Resource node " + r.id + " has been exhausted");
			}
		}
		//Remove the used up resource nodes
		for (int rid : usedup)
			state.removeResourceNode(rid);
		
		state.incrementTurn();
	}
	
	protected int calculateDamage(Unit attacker, Unit defender)
	{
		int armor = defender.getTemplate().getArmor();
		int damage;
		int basic_damage;
		int piercing_damage;

		basic_damage = attacker.getTemplate().getBasicAttack();
		piercing_damage = attacker.getTemplate().getPiercingAttack();
//		if (bloodlust) {
//			basic_damage *= 2;
//			piercing_damage *= 2;
//		}

		damage = (basic_damage - armor) > 1 ?
			(basic_damage - armor) : 1;
		damage += piercing_damage;
		damage -= random.nextInt() % ((damage + 2) / 2);

		return damage;
	}

	protected boolean empty(int x, int y) {
		return state.unitAt(x, y) == null && state.resourceAt(x, y) == null;
	}

	protected static enum FailureMode {
		WRONG_ACTION_TYPE,
		WRONG_UNIT_TYPE,
		INVALID_TARGET,
		MISSING_PREREQUISITES,
		FAILED_ATTEMPT,
		SUCCESS
		;
	}
}
