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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.configuration.Configuration;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.action.ActionQueue;
import edu.cwru.sepia.action.ActionResult;
import edu.cwru.sepia.action.ActionResultType;
import edu.cwru.sepia.action.ActionType;
import edu.cwru.sepia.action.DirectedAction;
import edu.cwru.sepia.action.LocatedAction;
import edu.cwru.sepia.action.LocatedProductionAction;
import edu.cwru.sepia.action.ProductionAction;
import edu.cwru.sepia.action.TargetedAction;
import edu.cwru.sepia.model.state.Direction;
import edu.cwru.sepia.model.state.ResourceNode;
import edu.cwru.sepia.model.state.ResourceType;
import edu.cwru.sepia.model.state.State;
import edu.cwru.sepia.model.state.StateCreator;
import edu.cwru.sepia.model.state.Template;
import edu.cwru.sepia.model.state.Unit;
import edu.cwru.sepia.model.state.UnitTemplate;
import edu.cwru.sepia.model.state.UpgradeTemplate;
import edu.cwru.sepia.pathing.DurativePlanner;
import edu.cwru.sepia.util.DistanceMetrics;
import edu.cwru.sepia.util.SerializerUtil;
/**
 * A Model that attempts the best effort.
 * <br>A branch of SimpleModel with some added features: durative actions and turn taking. 
 * <br>This model is sequential, processing most actions one at a time.  Because of this, it resolves conflicts by allowing them to proceed on a first-come-first-served basis.
 * <br> 
 */
public class BestEffortModel extends AbstractDurativeModel {
	private static final long serialVersionUID = -8289868580233478749L;
	
	
	private final String NUM_ATTEMPTS = "environment.model.numattempts";
	
	private int numAttempts;

	public BestEffortModel(State init, StateCreator restartTactic, Configuration configuration) {
		super(init, restartTactic, configuration, Logger.getLogger(BestEffortModel.class.getCanonicalName()));
		this.numAttempts = configuration.getInt(NUM_ATTEMPTS, 2);
	}

	@Override
	public void executeStep() {
		
		//Set each agent to have no task
		for (Unit u : state.getUnits().values()) {
			u.deprecateOldView();
		}
		//Set each template to not keep the old view
		for (Integer player : state.getPlayers())
			for (@SuppressWarnings("rawtypes") Template t : state.getTemplates(player).values())
				t.deprecateOldView();
		
		//Run the Action
		for (Integer player : state.getPlayers())
		{
			//hedge against players entering the state for some reason
			if (!queuedActions.containsKey(player)) {
				queuedActions.put(player, new HashMap<Integer, ActionQueue>());
			}
			if (turnTracker == null || turnTracker.isPlayersTurn(player)) {
				Iterator<ActionQueue> queuedActItr = queuedActions.get(player).values().iterator();
				while(queuedActItr.hasNext()) 
				{
					ActionQueue queuedAct = queuedActItr.next();
					if (logger.isLoggable(Level.FINE))
						logger.fine("Doing full action: "+queuedAct.getFullAction());
					//Pull out the primitive
					if (!queuedAct.hasNext()) 
						continue;
					Action a = queuedAct.popPrimitive();
					if (logger.isLoggable(Level.FINE))
						logger.fine("Doing primitive action: "+a);
					//Execute it
					Unit u = state.getUnit(a.getUnitId());			
					if (u == null)
						continue;
					//Set the tasks and grab the common features
					int x = u.getxPosition();
					int y = u.getyPosition();
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
					
					
					//Gather the last of the information and actually execute the actions
					int timesTried=0;
					boolean failedTry=true;
					boolean wrongType=false;
					boolean fullIsPrimitive=ActionType.isPrimitive(a.getType());
					boolean incompletePrimitive;
					/*recalculate and try again once if it has failed, so long as the full action 
					  is not primitive (since primitives will recalculate to the same as before) 
					  and not the wrong type (since something is wrong if it is the wrong type)*/
					do
					{
						timesTried++;
						failedTry = false;
						incompletePrimitive = false;
						switch(a.getType())
						{
							case PRIMITIVEMOVE:
								if (!(a instanceof DirectedAction))
								{
									wrongType=true;
									break;
								}
								if(state.inBounds(xPrime, yPrime) && u.canMove() && empty(xPrime,yPrime)) {
									int newdurativeamount;
									if (a.equals(u.getActionProgressPrimitive()))
									{
										newdurativeamount = u.getActionProgressAmount()+1;
									}
									else
									{
										newdurativeamount = 1;
									}
									Direction d = ((DirectedAction)a).getDirection();
									boolean willcompletethisturn = newdurativeamount== DurativePlanner.calculateMoveDuration(u,u.getxPosition(),u.getyPosition(),d);
									//if it will finish, then execute the atomic action
									if (willcompletethisturn)
									{
										state.moveUnit(u, d);
										u.resetDurative();
									}
									else {
										incompletePrimitive=true;
										u.setDurativeStatus(a, newdurativeamount);
									}
								}
								else {
									failedTry=true;
									queuedAct.resetPrimitives(calculatePrimitives(queuedAct.getFullAction()));
								}
								break;
							case PRIMITIVEGATHER:
								if (!(a instanceof DirectedAction))
								{
									wrongType=true;
									break;
								}
								boolean failed=false;
								ResourceNode resource = state.resourceAt(xPrime, yPrime);
								if(resource == null) {
									failed=true;
								}
								else if(!u.canGather()) {
									failed=true;
								}
								else {
									int newdurativeamount;
									if (a.equals(u.getActionProgressPrimitive()))
									{
										newdurativeamount = u.getActionProgressAmount()+1;
									}
									else
									{
										newdurativeamount = 1;
									}
									boolean willcompletethisturn = newdurativeamount== DurativePlanner.calculateGatherDuration(u, resource);
									//if it will finish, then execute the atomic action
									if (willcompletethisturn)
									{
										int amountPickedUp = resource.reduceAmountRemaining(u.getTemplate().getGatherRate(resource.getType()));
										u.setCargo(resource.getResourceType(), amountPickedUp);
										history.recordResourcePickup(u, resource, amountPickedUp, state);
										u.resetDurative();
									}
									else {
										incompletePrimitive=true;
										u.setDurativeStatus(a, newdurativeamount);
									}
								}
								if (failed) {
									failedTry=true;
									queuedAct.resetPrimitives(calculatePrimitives(queuedAct.getFullAction()));
								}
								break;
							case PRIMITIVEDEPOSIT:
								if (!(a instanceof DirectedAction))
								{
									wrongType=true;
									break;
								}
								//only can do a primitive if you are in the right position
								Unit townHall = state.unitAt(xPrime, yPrime);
								boolean canAccept=false;
								if (townHall!=null && townHall.getPlayer() == u.getPlayer())
								{
									if (u.getCurrentCargoType() == ResourceType.GOLD && townHall.getTemplate().canAcceptGold())
										canAccept=true;
									else if (u.getCurrentCargoType() == ResourceType.WOOD && townHall.getTemplate().canAcceptWood())
										canAccept=true;
								}
								if(!canAccept)
								{
									failedTry=true;
									queuedAct.resetPrimitives(calculatePrimitives(queuedAct.getFullAction()));
									break;
								}
								else {
									int newdurativeamount;
									if (a.equals(u.getActionProgressPrimitive()))
									{
										newdurativeamount = u.getActionProgressAmount()+1;
									}
									else
									{
										newdurativeamount = 1;
									}
									boolean willcompletethisturn = newdurativeamount== DurativePlanner.calculateDepositDuration(u, townHall);
									//if it will finish, then execute the atomic action
									if (willcompletethisturn)
									{
										int agent = u.getPlayer();
										history.recordResourceDropoff(u, townHall, state);
										state.addResourceAmount(agent, u.getCurrentCargoType(), u.getCurrentCargoAmount());
										u.clearCargo();
										u.resetDurative();
									}
									else {
										incompletePrimitive=true;
										u.setDurativeStatus(a, newdurativeamount);
									}
									break;
								}
							case PRIMITIVEATTACK:
								if (!(a instanceof TargetedAction))
								{
									wrongType=true;
									break;
								}
								Unit target = state.getUnit(((TargetedAction)a).getTargetId());
								if (target!=null)
								{
									if (u.getTemplate().getRange() >= DistanceMetrics.chebyshevDistance(u.getxPosition(),u.getyPosition(), target.getxPosition(), target.getyPosition()))
									{
										int newdurativeamount;
										if (a.equals(u.getActionProgressPrimitive()))
										{
											newdurativeamount = u.getActionProgressAmount()+1;
										}
										else
										{
											newdurativeamount = 1;
										}
										boolean willcompletethisturn = newdurativeamount== DurativePlanner.calculateAttackDuration(u, target);
										//if it will finish, then execute the atomic action
										if (willcompletethisturn)
										{
											int damage = calculateDamage(u,target);
											history.recordDamage(u, target, damage, state);
											target.setHP(Math.max(target.getCurrentHealth()-damage,0));
											u.resetDurative();
										}
										else {
											incompletePrimitive=true;
											u.setDurativeStatus(a, newdurativeamount);
										}
									}
									else //out of range
									{
										failedTry=true;
										queuedAct.resetPrimitives(calculatePrimitives(queuedAct.getFullAction()));
									}
								}
								else
								{
									failedTry=true;
									queuedAct.resetPrimitives(calculatePrimitives(queuedAct.getFullAction()));
								}
								break;
							case PRIMITIVEBUILD:
							{
								if (!(a instanceof ProductionAction))
								{
									wrongType=true;
									break;
								}
								if (queuedAct.getFullAction().getType() == ActionType.COMPOUNDBUILD && queuedAct.getFullAction() instanceof LocatedProductionAction)
								{
									LocatedProductionAction fullbuild = (LocatedProductionAction) queuedAct.getFullAction();
									if (fullbuild.getX() != u.getxPosition() || fullbuild.getY() != u.getyPosition())
									{
										failedTry=true;
										queuedAct.resetPrimitives(calculatePrimitives(queuedAct.getFullAction()));
										break;
									}
								}
								UnitTemplate template = (UnitTemplate)state.getTemplate(((ProductionAction)a).getTemplateId());
								if (u.getTemplate().canProduce(template))
								{
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
											if (!state.hasUpgrade(u.getPlayer(),upgradetemplateid)) {
												prerequisitesMet = false;
												break;
											}
										}
									}
									if (prerequisitesMet) {
										int newdurativeamount = a.equals(u.getActionProgressPrimitive())? u.getActionProgressAmount()+1 : 1;
										boolean willcompletethisturn = newdurativeamount== DurativePlanner.calculateProductionDuration(u, template);
										//if it will finish, then execute the atomic action
										if (willcompletethisturn)
										{
											Unit building = template.produceInstance(state);
											int[] newxy = state.getClosestPosition(x,y);
											if (state.tryProduceUnit(building,newxy[0],newxy[1]))
											{
												history.recordBirth(building, u, state);
											}
											u.resetDurative();
										}
										else {
											incompletePrimitive=true;
											u.setDurativeStatus(a, newdurativeamount);
										}
									}
									else //didn't meet prerequisites
									{
										failedTry=true;
									}
								}
								else //it can't produce the appropriate thing
								{
									failedTry=true;
								}
								
								break;
							}
							case PRIMITIVEPRODUCE:
							{
								if (!(a instanceof ProductionAction))
								{
									wrongType=true;
									break;
								}
								Template<?> template = state.getTemplate(((ProductionAction)a).getTemplateId());
								//check if it is even capable of producing the template
								if (u.getTemplate().canProduce(template))
								{
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
									if (prerequisitesMet) {
										int newdurativeamount = a.equals(u.getActionProgressPrimitive())? u.getActionProgressAmount()+1 : 1;
										boolean willcompletethisturn = newdurativeamount== DurativePlanner.calculateProductionDuration(u, template);
										//if it will finish, then execute the atomic action
										if (willcompletethisturn)
										{
											if (template instanceof UnitTemplate)
											{
												Unit produced = ((UnitTemplate)template).produceInstance(state);
												int[] newxy = state.getClosestPosition(x,y);
												if (state.tryProduceUnit(produced,newxy[0],newxy[1]))
												{
													history.recordBirth(produced, u, state);
												}
											}
											else if (template instanceof UpgradeTemplate) {
												UpgradeTemplate upgradetemplate = ((UpgradeTemplate)template);
												if (state.tryProduceUpgrade(upgradetemplate.produceInstance(state)))
												{
													history.recordUpgrade(upgradetemplate,u, state);
												}
											}
										}
										else {
											incompletePrimitive=true;
											u.setDurativeStatus(a, newdurativeamount);
										}
									}
									else { //prerequisites not met
										failedTry=true;
									}
								}
								else//can't produce it
								{
									failedTry=true;
								}
								break;
							}
							case FAILED:
							{
								failedTry=true;
								queuedAct.resetPrimitives(calculatePrimitives(queuedAct.getFullAction()));
								break;
							}
							case FAILEDPERMANENTLY:
							{
								break;
							}
						}
						
						//Record results
						
						ActionResultType primitiveFeedback=null;
						ActionResultType compoundFeedback=null;
						boolean removeAction=false;
						if (wrongType)
						{
							//if it had the wrong type, then either the planner is bugged (unlikely) or the user provided a bad primitive action
							//either way, record it as failed and toss it
							compoundFeedback = ActionResultType.INVALIDTYPE;
							removeAction = true;
						}
						else if (!failedTry && a.getType() != ActionType.FAILEDPERMANENTLY)
						{
							primitiveFeedback = incompletePrimitive? ActionResultType.INCOMPLETE : ActionResultType.COMPLETED;
							if (!incompletePrimitive && !queuedAct.hasNext())
							{
								compoundFeedback = ActionResultType.COMPLETED;
								removeAction=true;
							}
							else
							{
								compoundFeedback = ActionResultType.INCOMPLETE;
							}
						}
						else if (a.getType()==ActionType.FAILEDPERMANENTLY || failedTry && fullIsPrimitive)
						{
							compoundFeedback = ActionResultType.FAILED;
							primitiveFeedback = ActionResultType.FAILED;
							removeAction = true;
							
						}
						else
						{
							compoundFeedback = ActionResultType.INCOMPLETEMAYBESTUCK;
							primitiveFeedback = ActionResultType.FAILED;
						}
						if (compoundFeedback != null) {
							history.recordCommandFeedback(u.getPlayer(), state.getTurnNumber(), new ActionResult(queuedAct.getFullAction(),compoundFeedback));
						}
						if (primitiveFeedback != null) {
							history.recordPrimitiveFeedback(u.getPlayer(), state.getTurnNumber(), new ActionResult(a,primitiveFeedback));
						}
						if (removeAction) {
							queuedActItr.remove();
						}
					}
					while (timesTried < numAttempts && failedTry && !fullIsPrimitive && !wrongType);
				}
			}//end if it is the player's turn
		}
		
		
		//Take all the dead units and clear them
		//Find the dead units
		Map<Integer, Unit> allunits = state.getUnits();
		List<Integer> dead= new ArrayList<Integer>(allunits.size());
		for (Unit u : allunits.values()) {
			if (u.getCurrentHealth() <= 0)
			{
				history.recordDeath(u, state);
				dead.add(u.id);
			}
		}
		//Remove them
		for (int uid : dead)
		{
			state.removeUnit(uid);
		}
		//Take all of the used up resources and get rid of them
		List<ResourceNode> allnodes = state.getResources();
		List<Integer> usedup= new ArrayList<Integer>(allnodes.size());
		for (ResourceNode r : allnodes) {
			if (r.getAmountRemaining() <= 0)
			{
				history.recordResourceNodeExhaustion(r, state);
				usedup.add(r.id);
			}
		}
		//Remove the used up resource nodes
		for (int rid : usedup)
		{
			
			state.removeResourceNode(rid);
		}
		
		state.incrementTurn();
	}

	public void save(String filename) {
		SerializerUtil.storeState(filename, state);
	}

}
