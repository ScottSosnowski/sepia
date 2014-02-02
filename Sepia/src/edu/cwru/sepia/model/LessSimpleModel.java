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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.configuration.Configuration;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.action.ActionQueue;
import edu.cwru.sepia.action.ActionResult;
import edu.cwru.sepia.action.ActionResultType;
import edu.cwru.sepia.action.ActionType;
import edu.cwru.sepia.action.DirectedAction;
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
 * A less simple model that allows consistency, turn-taking and durative actions.
 * No longer supports unit tasks
 * Recalculates compound actions automatically every step.
 * Supports consistent actions* according to the following principles:
 * The order of actions does not affect their results,
 * Sets of actions may not cause invalid states (so some must fail),
 * The effects of one action succeeding may not change based on the actions of others**,
 * An set of actions that fails cannot be made to succeed by adding actions for other units.
 * 
 * From this, compound actions may only calculate primitives once in a turn, and only based on the previous state.
 * This makes compound actions much much less effective than in SimpleModel.
 * 
 * 
 * The particular solution in this makes sure that successes based on ranges (including directional actions with proximity) are calculated based on the previous state.
 * It also ensures that an action may not fail due to a unit involved being attacked for enough damage to kill it.
 * * Production/build actions that make new units fail consistency.  The units are placed sequentially in places that were empty last state in such a way as to not disrupt move actions.  In the situation where more productions exist than empty spaces that are not being moved into, then only the minimum number of production actions will fail
 * ** More generally, where s is the event "action has succeeded" and E is a set of possible results, and O is actions sent to other units, E given s must be independant of O, IE: for all O, P(E|s) = P(E|s,O)***
 * *** as an example that works, attacks, being based on sequential Random variables from a seed, will change exact result, but the distribution of successful attacks does not change based on other actions.  As a failing example, random movements that only fail on an actual collision wouldn't work, as the action of the other unit affects the distribution of effects. 
 * 
 */
public class LessSimpleModel extends AbstractDurativeModel {
	private static final long serialVersionUID = -8289868580233478749L;

	public LessSimpleModel(State init, StateCreator restartTactic, Configuration configuration) {
		super(init, restartTactic, configuration, Logger.getLogger(LessSimpleModel.class.getCanonicalName()));
	}		
	
	/**
	 * The main loop of the engine.
	 * Removes the old views
	 */
	@Override
	public void executeStep() {
		
		//if for some reason you start getting views before you are done changing things, then you will need to deprecate them again

		
		
		
		//Run the Actions
		//This is based on 3 factors: the last state, spaces/resources/nodes with pending actions that are not known to be failures, and spaces/resources/nodes that are known to be failures
		
		//The basic procedure is (list and set are used colloquially, and may be implemented as other things):
			//if compound, check if you can do the action in the last state, recalculate if needed
			//if it is the last one in a compound,
			//check if you can do the action in general/based on last state
				//if you can't, put yourself on a fail list and stop processing this action
			//if you are a durative action that won't complete this step, put yourself on the successful list then stop processing this action
			//check if what you would effect is in the list of those with known problematic claims
				//if it is problematic, then put yourself on the fail list and stop processing this action
			//add your effect to the claims list
			//check if the claims including yourself will now cause a problem
				//if it will, mark yourself and all others claiming it on the failed list (removing them from the successful list too), and remove it from the claims list and put it on the known problematic list
				//if it will not cause a problem, mark yourself on the successful list and stop processing this action
		//after all actions have been checked and claimed
			//execute and log all actions remaining in the successful list
			//log all failed actions, and remove them from the queue
			
		//interpret coordinates as integers
		Map<Integer,ActionQueue> claimedspaces=new HashMap<Integer,ActionQueue>(); //merge the boolean for whether it has claimed with the set of actions that claimed it, and since one is known to be enough, don't need a set
		Set<Integer> problemspaces=new HashSet<Integer>();//places that you know are problems
		Map<Integer,Integer> claimedgathering=new HashMap<Integer,Integer>();//<nodeid,amountclaimed>
		Map<Integer,Set<ActionQueue>> claimedgatheringactions=new HashMap<Integer,Set<ActionQueue>>();//<nodeid,claimingactions>
		Set<Integer> problemgatherings=new HashSet<Integer>();//<problemnodeids>
		Map<Integer,Map<ResourceType,Integer>> claimedcosts=new HashMap<Integer,Map<ResourceType,Integer>>();//<player,<resourcetype,amountclaimed>>
		Map<Integer,Map<ResourceType,Set<ActionQueue>>> claimedcostactions=new HashMap<Integer,Map<ResourceType,Set<ActionQueue>>>();//<player,<resourcetypes,claimingactions>>
		Map<Integer,Set<ResourceType>> problemcosts=new HashMap<Integer,Set<ResourceType>>();//<player, problemresourcetypes>
		Map<Integer,Integer> claimedfoodcosts=new HashMap<Integer,Integer>();//<player,amountclaimed>
		Map<Integer,Set<ActionQueue>> claimedfoodcostactions=new HashMap<Integer,Set<ActionQueue>>();//<player,claimingactions>
		Set<Integer> problemfoodcosts=new HashSet<Integer>();//<problemplayer>
		Set<ActionQueue> failed=new HashSet<ActionQueue>();
		Set<ActionQueue> successfulsofar=new HashSet<ActionQueue>();
		/** Track all units of active players, to reset their progress if they failed or weren't moved*/Set<Integer> unsuccessfulUnits = new HashSet<Integer>(); 
		Set<ActionQueue> productionsuccessfulsofar=new HashSet<ActionQueue>();
		for (Integer player : queuedActions.keySet())
		{
			if (turnTracker == null || turnTracker.isPlayersTurn(player)) {
				//Gather all units of that player, so that we can remove the ones that were successful later
				for (Integer id : state.getUnits(player).keySet())
				{
					unsuccessfulUnits.add(id);
				}
				Iterator<Entry<Integer,ActionQueue>> playerActions=queuedActions.get(player).entrySet().iterator();
				while(playerActions.hasNext())
				{
					Entry<Integer,ActionQueue> entry = playerActions.next();;
					ActionQueue aq =  entry.getValue();
	//				if (a==null) //Then it failed to calculate primitives, so it fails
					int uid = entry.getKey();
					Unit u = state.getUnit(uid);
					if (u == null || uid!=aq.getFullAction().getUnitId())
					{
						//unit is dead or never existed
						playerActions.remove();
						history.recordCommandFeedback(player, state.getTurnNumber(), new ActionResult(aq.getFullAction(),ActionResultType.INVALIDUNIT));
						
					}
					else
					{
						aq.resetPrimitives(calculatePrimitives(aq.getFullAction()));
						Action a = aq.peekPrimitive();
					if (a==null) //This happens when you try to compound move to where you are, not sure about other cases
					{
						playerActions.remove();
						history.recordCommandFeedback(player, state.getTurnNumber(), new ActionResult(aq.getFullAction(),ActionResultType.COMPLETED));
					}
					else if (!ActionType.isPrimitive(a.getType()))
					{
						throw new RuntimeException("This should never happen, all subactions should be primitives");
					}
					else if (a.getType() == ActionType.PRIMITIVEATTACK && !(a instanceof TargetedAction)
							|| a.getType() == ActionType.PRIMITIVEGATHER && !(a instanceof DirectedAction)
							|| a.getType() == ActionType.PRIMITIVEDEPOSIT && !(a instanceof DirectedAction)
							|| a.getType() == ActionType.PRIMITIVEPRODUCE&& !(a instanceof ProductionAction)
							|| a.getType() == ActionType.PRIMITIVEBUILD && !(a instanceof ProductionAction)
							|| a.getType() == ActionType.PRIMITIVEMOVE && !(a instanceof DirectedAction))
					{//shouldn't have to do this, should make actions so it is never possible to have the types not match
						//log a wrong type thing
						history.recordCommandFeedback(player, state.getTurnNumber(), new ActionResult(aq.getFullAction(),ActionResultType.INVALIDTYPE));
						//remove it from the queues
						playerActions.remove();
					}
					else
					{
						
						if (a.getType() == ActionType.FAILED || a.getType() == ActionType.FAILEDPERMANENTLY)
						{
							failed.add(aq);
							//recalcAndStuff();//This marks a place where recalculation would be called for
						}
						//check if it is a move
						if (a.getType() == ActionType.PRIMITIVEMOVE)
						{
							//if it can't move, that is a problem
							if (!u.canMove())
							{
								logger.log(Level.FINER, "Unit " + u + " unable to move, action "+a +" failed");
								failed.add(aq);
								//recalcAndStuff();//This marks a place where recalculation would be called for
							}
							else //hasn't failed yet
							{
								//find out where it will be next
								
								DirectedAction da =(DirectedAction)a;
								Direction d = da.getDirection();
								int xdest = u.getXPosition() + d.xComponent();
								int ydest = u.getYPosition() + d.yComponent();
								
								//if it is not empty there is a problem
								if (!accessible(u, xdest, ydest))
								{ 
									failed.add(aq);
									//recalcAndStuff();//This marks a place where recalculation would be called for
								}
								else //hasn't failed yet
								{
									int newdurativeamount;
									if (da.equals(u.getActionProgressPrimitive()))
									{
										newdurativeamount = u.getActionProgressAmount()+1;
									}
									else
									{
										newdurativeamount = 1;
									}
									boolean willcompletethisturn = newdurativeamount== DurativePlanner.calculateMoveDuration(u,u.getXPosition(),u.getYPosition(), d, state);
									//if it will finish, then verify claim stuff
									if (willcompletethisturn)
									{
										Integer dest = getCoordInt(xdest,ydest);
										//check if the space is a problem
										if (problemspaces.contains(dest))
										{
											failed.add(aq);
										}
										else //not a problem space
										{
											//check if it is claimed
											ActionQueue priorclaimant = claimedspaces.get(dest);
											if (priorclaimant != null)
											{//it is claimed
												successfulsofar.remove(priorclaimant);
												failed.add(priorclaimant);
												failed.add(aq);
												problemspaces.add(dest);
												claimedspaces.remove(dest); //remove all claims, as it is now a problem, not a claim, may be pointless
											}
											else
											{//it is not claimed
												//so claim it
												claimedspaces.put(dest,aq);
												successfulsofar.add(aq);
											}
										}
									}
									else //won't complete, so passes all claims
									{
										successfulsofar.add(aq);
									}
								}
							}
						}
					
					else if (a.getType() == ActionType.PRIMITIVEDEPOSIT)
					{
						if (!u.canGather() || u.getCurrentCargoAmount() <= 0)
						{//if can't gather or isn't carrying anything, then this isn't an acceptible action
							failed.add(aq);
							//recalcAndStuff();//This marks a place where recalculation would be called for
						}
						else
						{
							DirectedAction da =(DirectedAction)a;
							Direction d = da.getDirection();
							int xdest = u.getXPosition() + d.xComponent();
							int ydest = u.getYPosition() + d.yComponent();
							Unit townHall = state.unitAt(xdest, ydest);
							if (townHall == null || townHall.getPlayer() != u.getPlayer())
							{//no unit there on your team
								failed.add(aq);
								//recalcAndStuff();//This marks a place where recalculation would be called for
							}
							else //there is a unit on your team
							{
								//check if the unit can accept the kind of resources that you have
								boolean canAccept=townHall.getTemplate().canAccept(u.getCurrentCargoType());
								if (!canAccept)
								{//then it isn't a town hall of the right type
									failed.add(aq);
									//recalcAndStuff();//This marks a place where recalculation would be called for
								}
								else //there is an appropriate town hall there
								{
									//deposit has no chance of conflicts, so this works
									successfulsofar.add(aq);
								}
							}
						}
					}
					else if (a.getType() == ActionType.PRIMITIVEATTACK)
					{
						//make sure you can attack and the target exists and is in range in the last state
						if (!u.canAttack())
						{
							failed.add(aq);
							//recalcAndStuff();//This marks a place where recalculation would be called for
						}
						else
						{
							TargetedAction ta =(TargetedAction)a;
							Unit target = state.getUnit(ta.getTargetId());
							if (target == null || target.getCurrentHealth() <= 0 || !inRange(u, target))
							{
								failed.add(aq);
								//recalcAndStuff();//This marks a place where recalculation would be called for
							}
							else //target exists and is in range
							{
								//no possibility for conflict, so this succeeds
								successfulsofar.add(aq);
							}
						}
					}
					else if (a.getType() == ActionType.PRIMITIVEPRODUCE || a.getType() == ActionType.PRIMITIVEBUILD)
					{//currently, this adds to productionsuccessfulsofar because they are not processed consistantly
					//consistancy could be restored by making unit production and building actions require a place or direction for the new unit to go, and then processing it as a move
						
						//last state check:
						ProductionAction pa =(ProductionAction)a;
						Template<?> t = state.getTemplate(pa.getTemplateId());
						if (a.getType() == ActionType.PRIMITIVEPRODUCE && u.canBuild()|| a.getType() == ActionType.PRIMITIVEBUILD && !u.canBuild())
						{//if it should build and is trying to produce or should produce and is trying to build
							failed.add(aq);
							//recalcAndStuff();//This marks a place where recalculation would be called for
						}
						else if (t==null || !u.getTemplate().canProduce(t) || !(prerequisitesMet(t,u.getPlayer())))
						{//if the template does not exist or the unit cannot make the template or the template's prerequisites are not met
							failed.add(aq);
							//recalcAndStuff();//This marks a place where recalculation would be called for
						}
						else //template exists, is producable by the unit, and has it's tech-tree prerequisites met
						{
							int newdurativeamount;
							if (pa.equals(u.getActionProgressPrimitive()))
							{
								newdurativeamount = u.getActionProgressAmount()+1;
							}
							else
							{
								newdurativeamount = 1;
							}
							boolean willcompletethisturn = newdurativeamount== DurativePlanner.calculateProductionDuration(u,t);
							if (willcompletethisturn)
							{
								if (!problemcosts.containsKey(player))
									problemcosts.put(player, new HashSet<ResourceType>());
								if (!claimedcosts.containsKey(player))
									claimedcosts.put(player, new HashMap<ResourceType, Integer>());
								if (!claimedcostactions.containsKey(player))
									claimedcostactions.put(player, new HashMap<ResourceType, Set<ActionQueue>>());
								boolean failedaclaim=false;
								
								//check all the resources, including supply for problems and claims
								//note that if you don't need any, it doesn't matter if it is overdrawn
								//do all even if one fails, because if you stop checking when you fail one resource and don't claim the others, then another production that should conflict will not be detected as such
								{
									int goldneeded = t.getGoldCost();
									//if you have a cost, then check the claims
									if (goldneeded > 0)
									{
										//check if it is a problem
										if (problemcosts.get(player).contains(ResourceType.GOLD))
										{
											failedaclaim=true;
										}
										else
										{//not a problem already, check claims
											//get the amount of the resource that you had before
											int previousamount = state.getResourceAmount(player, ResourceType.GOLD);
											// get the previous claim (if there is none, that is zero)
											Integer previousclaim = claimedcosts.get(player).get(ResourceType.GOLD); if (previousclaim==null) previousclaim=0;
											int updatedclaim=previousclaim+goldneeded;
											
											//check if the total claim is more than the amount the player has
											if (updatedclaim > previousamount)
											{
												//if the claim is more, then this and all others with claims on this resource fail
												Set<ActionQueue> otherclaimants = claimedcostactions.get(player).get(ResourceType.GOLD);
												if (otherclaimants != null)
												{
													for (ActionQueue otherclaimant : otherclaimants)
													{
														productionsuccessfulsofar.remove(otherclaimant);
														failed.add(otherclaimant);
													}
													//since we are marking this as a problem, don't need the claim anymore
													claimedcostactions.get(player).remove(ResourceType.GOLD);
												}
												failedaclaim=true;
												problemcosts.get(player).add(ResourceType.GOLD);
											}
											else
											{//not too much, so claim it
												claimedcosts.get(player).put(ResourceType.GOLD, updatedclaim);
												if (!claimedcostactions.get(player).containsKey(ResourceType.GOLD))
												{
													claimedcostactions.get(player).put(ResourceType.GOLD, new HashSet<ActionQueue>());
												}
												claimedcostactions.get(player).get(ResourceType.GOLD).add(aq);
											}
										}
									}
								}
									{
										int woodneeded = t.getWoodCost();
										//if you have a cost, then check the claims
										if (woodneeded > 0)
										{
											//check if it is a problem
											if (problemcosts.get(player).contains(ResourceType.WOOD))
											{
												failedaclaim=true;
											}
											else
											{//not a problem already, check claims
												//get the amount of the resource that you had before
												int previousamount = state.getResourceAmount(player, ResourceType.WOOD);
												// get the previous claim (if there is none, that is zero)
												Integer previousclaim = claimedcosts.get(player).get(ResourceType.WOOD); if (previousclaim==null) previousclaim=0;
												int updatedclaim=previousclaim+woodneeded;
												
												//check if the total claim is more than the amount the player has
												if (updatedclaim > previousamount)
												{
													//if the claim is more, then this and all others with claims on this resource fail
													Set<ActionQueue> otherclaimants = claimedcostactions.get(player).get(ResourceType.WOOD);
													if (otherclaimants != null)
													{
														for (ActionQueue otherclaimant : otherclaimants)
														{
															productionsuccessfulsofar.remove(otherclaimant);
															failed.add(otherclaimant);
														}
														//since we are marking this as a problem, don't need the claim anymore
														claimedcostactions.get(player).remove(ResourceType.WOOD);
													}
													failedaclaim=true;
													problemcosts.get(player).add(ResourceType.WOOD);
												}
												else
												{//not too much, so claim it
													claimedcosts.get(player).put(ResourceType.WOOD, updatedclaim);
													if (!claimedcostactions.get(player).containsKey(ResourceType.WOOD))
													{
														claimedcostactions.get(player).put(ResourceType.WOOD, new HashSet<ActionQueue>());
													}
													claimedcostactions.get(player).get(ResourceType.WOOD).add(aq);
												}
											}
										}
								}
								{
									int foodneeded = t.getFoodCost();
									//if you have a cost, then check the claims
									if (foodneeded > 0)
									{
										//check if it is a problem
										if (problemfoodcosts.contains(player))
										{
											failedaclaim=true;
										}
										else
										{//not a problem already, check claims
											//get the amount of the resource that you had before
											int previousamount = state.getSupplyCap(player)-state.getSupplyAmount(player);
											// get the previous claim (if there is none, that is zero)
											Integer previousclaim = claimedfoodcosts.get(player); if (previousclaim==null) previousclaim=0;
											int updatedclaim=previousclaim+foodneeded;
											
											//check if the total claim is more than the amount the player has
											if (updatedclaim > previousamount)
											{
												//if the claim is more, then this and all others with claims on this resource fail
												Set<ActionQueue> otherclaimants = claimedfoodcostactions.get(player);
												if (otherclaimants != null)
												{
													for (ActionQueue otherclaimant : otherclaimants)
													{
														productionsuccessfulsofar.remove(otherclaimant);
														failed.add(otherclaimant);
													}
													//since we are marking this as a problem, don't need the claim anymore
													claimedfoodcostactions.remove(player);
												}
												failedaclaim=true;
												problemfoodcosts.add(player);
											}
											else
											{//not too much, so claim it
												claimedfoodcosts.put(player, updatedclaim);
												if (!claimedfoodcostactions.containsKey(player))
												{
													claimedfoodcostactions.put(player, new HashSet<ActionQueue>());
												}
												claimedfoodcostactions.get(player).add(aq);
											}
										}
									}
								}
								
								if (failedaclaim)
								{
									failed.add(aq);
								}
								else
								{
									productionsuccessfulsofar.add(aq);
								}
								
							}
							else //won't complete, so passes all claims
							{
								successfulsofar.add(aq);
							}
						}
						
					}
					else if (a.getType() == ActionType.PRIMITIVEGATHER)
					{
						//check if it can gather at all
						if (!u.canGather())
						{
							failed.add(aq);
							//recalcAndStuff();//This marks a place where recalculation would be called for
						}
						else //it can gather
						{
							//find the node you want to gather from, and make sure it exists
							DirectedAction da =(DirectedAction)a;
							Direction d = da.getDirection();
							int xdest = u.getXPosition() + d.xComponent();
							int ydest = u.getYPosition() + d.yComponent();
							ResourceNode rn  = state.resourceAt(xdest,ydest);
							//check if the node exists and was not exhausted last turn
							if (rn==null || rn.getAmountRemaining() <= 0)
							{
								failed.add(aq);
								//recalcAndStuff();//This marks a place where recalculation would be called for
							}
							else //there is a node and it has resources
							{
								int newdurativeamount;
								if (da.equals(u.getActionProgressPrimitive()))
								{
									newdurativeamount = u.getActionProgressAmount()+1;
								}
								else
								{
									newdurativeamount = 1;
								}
								boolean willcompletethisturn = newdurativeamount== DurativePlanner.calculateGatherDuration(u,rn);
								if (willcompletethisturn)
								{
									
									
									//then check if the node will be a problem
									if (problemgatherings.contains(rn.id))
									{
										failed.add(aq);
									}
									else //no problem yet
									{
										
										
										//so test out the new claim
										int previousamount = rn.getAmountRemaining();
										boolean isotherclaimant=true;
										Integer otherclaims = claimedgathering.get(rn.id);
										//if there is no other claim, then the other claim is 0, and it should be noted that noone else is claiming it
										if (otherclaims == null)
										{
											isotherclaimant=false;
											otherclaims=0;
										}
										int updatedclaim = otherclaims + u.getTemplate().getGatherRate(rn.getType().getResource());
										//if the claim is too much, then the node has a problem
											//but don't fail if this is the only claimant
												//in that case, the result should be that this mines out the resource
										if (updatedclaim > previousamount && isotherclaimant)
										{
											//the node is a problem, so make all claimants fail and mark it as such
											problemgatherings.add(rn.id);
											for (ActionQueue otherclaimant :claimedgatheringactions.get(rn.id))
											{
												successfulsofar.remove(otherclaimant);
												failed.add(otherclaimant);
											}
											failed.add(aq);
											claimedgatheringactions.remove(rn.id);
										}
										else //the state isn't a problem
										{
											//so make the claim and succeed
											claimedgathering.put(rn.id, updatedclaim);
											//make sure the set is initialized
											if (!claimedgatheringactions.containsKey(rn.id))
												claimedgatheringactions.put(rn.id, new HashSet<ActionQueue>());
											claimedgatheringactions.get(rn.id).add(aq);
											successfulsofar.add(aq);
										}
									}
									
								}
								else //won't complete, so passes all claims
								{
									successfulsofar.add(aq);
								}
							}
						}
					}
					}
					}
				}
			}
		}
		
		//to make production spawning as consistant yet sequential as possible, find positions that weren't occupied before and which weren't claimed by moves or other production actions
		//need to avoid claimed spaces so that the inconsistancy doesn't break move's consistancy
		//so calculate now and use later
		Map<ActionQueue,int[]> productionplaces = new HashMap<ActionQueue,int[]>();
		
		{
			Set<Integer> productionclaimedspaces=new HashSet<Integer>();
			boolean nomorespaces=false;//once you run out of spaces, no further productions that make units will succeed
			Set<Integer> moveclaimedspaces = claimedspaces.keySet(); //grab the spaces claimed by move, it shouldn't change during this
			for (ActionQueue aq : productionsuccessfulsofar)
			{
				//only production actions that will complete should be in productionsuccessfulsofar
				ProductionAction a = (ProductionAction)aq.peekPrimitive(); 
				Unit u = state.getUnit(a.getUnitId());
				Template<?> producedTemplate = state.getTemplate(a.getTemplateId());
				//check if it is an upgrade and thus doesn't risk failure and can just succeed
				if (producedTemplate instanceof UpgradeTemplate)
				{
					successfulsofar.add(aq);
				}
				else //will be a unit/building, needs to reserve a space
				{
					if (nomorespaces)
					{//if you ran out of spaces before, then there is no point trying again, as it will be no better
						failed.add(aq);
					}
					else
					{
						//find the nearest open position, which will be null if there is none
						int[] newposition = getClosestEmptyUnclaimedPosition((UnitTemplate)producedTemplate, u.getXPosition(), u.getYPosition(), moveclaimedspaces, productionclaimedspaces);
						if (newposition == null)
						{//if no place for new unit
							//then this fails
							failed.add(aq);
							nomorespaces = true;
						}
						else //there is a place for the new unit
						{
							//so reserve the new position and mark as successful
							productionplaces.put(aq, newposition);
							productionclaimedspaces.add(getCoordInt(newposition[0],newposition[1]));
							successfulsofar.add(aq);
						}
					}
					
				}
			}
		}
		
		//Take all of the actions that haven't failed yet and execute them
		for (ActionQueue aq : successfulsofar)
		{
			//Mark it's unit as having moved successfully
			unsuccessfulUnits.remove(aq.getFullAction().getUnitId());
			
			//execute it without further checking, logging it
			Action a = aq.popPrimitive();
			int uid = a.getUnitId();
			Unit u = state.getUnit(uid);
			boolean willcompletethisturn = true;
			{
				//check if it is a move
				if (a.getType() == ActionType.PRIMITIVEMOVE)
				{
					//if it can't move, that is a problem
					{
						//find out where it will be next
						
						DirectedAction da =(DirectedAction)a;
						Direction d = da.getDirection();
						//calculate the amount of duration
						int newdurativeamount;
						if (da.equals(u.getActionProgressPrimitive()))
						{
							newdurativeamount = u.getActionProgressAmount()+1;
						}
						else
						{
							newdurativeamount = 1;
						}
						willcompletethisturn = newdurativeamount== DurativePlanner.calculateMoveDuration(u,u.getXPosition(),u.getYPosition(),d, state);
						//if it will finish, then execute the atomic action
						if (willcompletethisturn)
						{
							//do the atomic action
							state.moveUnit(u, d);
							//you did the action, so reset the progress
							u.resetDurative();
						}
						else
						{
							//increment the duration
							u.setDurativeStatus(da, newdurativeamount);
						}
					}
				}
			
			if (a.getType() == ActionType.PRIMITIVEDEPOSIT)
			{
				DirectedAction da =(DirectedAction)a;
				Direction d = da.getDirection();
				int xdest = u.getXPosition() + d.xComponent();
				int ydest = u.getYPosition() + d.yComponent();
				Unit townHall = state.unitAt(xdest, ydest);

				//calculate the amount of duration
					int newdurativeamount;
					if (da.equals(u.getActionProgressPrimitive()))
					{
						newdurativeamount = u.getActionProgressAmount()+1;
					}
					else
					{
						newdurativeamount = 1;
					}
					willcompletethisturn = newdurativeamount== DurativePlanner.calculateDepositDuration(u,townHall);
				//if it will finish, then execute the atomic action
				if (willcompletethisturn)
				{
					//do the atomic action
					int player = townHall.getPlayer();
					history.recordResourceDropoff(u, townHall, state);
					state.addResourceAmount(player, u.getCurrentCargoType(), u.getCurrentCargoAmount());
					u.clearCargo();
					//you completed the action, so reset the durative progress
					u.resetDurative();
				}
				else
				{
					//increment the duration
					u.setDurativeStatus(da, newdurativeamount);
				}
			}
			if (a.getType() == ActionType.PRIMITIVEATTACK)
			{
				//make sure you can attack and the target exists and is in range in the last state
				TargetedAction ta =(TargetedAction)a;
				Unit target = state.getUnit(ta.getTargetId());
				int newdurativeamount;
				if (ta.equals(u.getActionProgressPrimitive()))
				{
					newdurativeamount = u.getActionProgressAmount()+1;
				}
				else
				{
					newdurativeamount = 1;
				}
				willcompletethisturn = newdurativeamount== DurativePlanner.calculateAttackDuration(u,target);
				//if it will finish, then execute the atomic action
				if (willcompletethisturn)
				{
					//do the atomic action
					int damage = calculateDamage(u,target);
					history.recordDamage(u, target, damage, state);
					target.setHP(Math.max(target.getCurrentHealth()-damage,0));
					//you have finished the primitive, so progress resets
					u.resetDurative();
				}
				else
				{
					//increment the duration
					u.setDurativeStatus(ta, newdurativeamount);
				}
			}
			if (a.getType() == ActionType.PRIMITIVEPRODUCE || a.getType() == ActionType.PRIMITIVEBUILD)
			{
				//last state check:
				ProductionAction pa =(ProductionAction)a;
				@SuppressWarnings("rawtypes")
				Template t = state.getTemplate(pa.getTemplateId());
				//the willcomplete is somewhat related to the production amount
				int newdurativeamount;
				if (pa.equals(u.getActionProgressPrimitive()))
				{
					newdurativeamount = u.getActionProgressAmount()+1;
				}
				else
				{
					newdurativeamount = 1;
				}
				willcompletethisturn = newdurativeamount== DurativePlanner.calculateProductionDuration(u,t);
				//if it will finish, then execute the atomic action
				if (willcompletethisturn)
				{
					//do the atomic action
					if (t instanceof UnitTemplate)
					{
						Unit produced = ((UnitTemplate)t).produceInstance(state);
						int[] newxy = productionplaces.get(aq);
						if (u.canBuild())
						{
							int oldx = u.getXPosition();
							int oldy = u.getYPosition();
							state.transportUnit(u, newxy[0], newxy[1]);
							if (state.tryProduceUnit(produced,oldx,oldy))
							{
								history.recordBirth(produced, u, state);
							}
						}
						else
						{
							if (state.tryProduceUnit(produced,newxy[0],newxy[1]))
							{
								history.recordBirth(produced, u, state);
							}
						}
					}
					else if (t instanceof UpgradeTemplate) {
						UpgradeTemplate upgradetemplate = ((UpgradeTemplate)t);
						if (state.tryProduceUpgrade(upgradetemplate.produceInstance(state)))
						{
							history.recordUpgrade(upgradetemplate,u, state);
						}
					}
					//you have finished the primitive, so progress resets
					u.resetDurative();
				}
				else
				{
					//increment the duration
					u.setDurativeStatus(pa, newdurativeamount);
				}
			}
			if (a.getType() == ActionType.PRIMITIVEGATHER)
			{
				//check if it can gather at all
				//find the right node
				DirectedAction da =(DirectedAction)a;
				Direction d = da.getDirection();
				int xdest = u.getXPosition() + d.xComponent();
				int ydest = u.getYPosition() + d.yComponent();
				ResourceNode rn  = state.resourceAt(xdest,ydest);
				int newdurativeamount;
				if (da.equals(u.getActionProgressPrimitive()))
				{
					newdurativeamount = u.getActionProgressAmount()+1;
				}
				else
				{
					newdurativeamount = 1;
				}
				willcompletethisturn = newdurativeamount== DurativePlanner.calculateGatherDuration(u,rn);
				//if it will finish, then execute the atomic action
				if (willcompletethisturn)
				{
					//do the atomic action
					int amountPickedUp = rn.reduceAmountRemaining(u.getTemplate().getGatherRate(rn.getType().getResource()));
					u.setCargo(rn.getResourceType(), amountPickedUp);
					history.recordResourcePickup(u, rn, amountPickedUp, state);
					//you have finished the primitive, so progress resets
					u.resetDurative();
				}
				else
				{
					//increment the duration
					u.setDurativeStatus(da, newdurativeamount);
				}

			}
			}
			
			ActionResultType compoundFeedback;
			ActionResultType primitiveFeedback = willcompletethisturn?ActionResultType.COMPLETED:ActionResultType.INCOMPLETE;
			if (!aq.hasNext())
			{
				if (willcompletethisturn) {
					queuedActions.get(u.getPlayer()).remove(aq.getFullAction().getUnitId());
					compoundFeedback = ActionResultType.COMPLETED;
				}
				else {
					compoundFeedback = ActionResultType.INCOMPLETE;
				}
					
				
			}
			else
			{
				compoundFeedback = ActionResultType.INCOMPLETE;
			}
			history.recordCommandFeedback(state.getUnit(aq.getFullAction().getUnitId()).getPlayer(), state.getTurnNumber(), new ActionResult(aq.getFullAction(),compoundFeedback));
			history.recordPrimitiveFeedback(state.getUnit(aq.getFullAction().getUnitId()).getPlayer(), state.getTurnNumber(), new ActionResult(a,primitiveFeedback));
		}
		for (ActionQueue aq : failed)
		{
			//should be safe to get the unitid, as it should have not been put into failed if the player was bad
			history.recordCommandFeedback(state.getUnit(aq.getFullAction().getUnitId()).getPlayer(), state.getTurnNumber(), new ActionResult(aq.getFullAction(),ActionResultType.FAILED));
			history.recordPrimitiveFeedback(state.getUnit(aq.getFullAction().getUnitId()).getPlayer(), state.getTurnNumber(), new ActionResult(aq.peekPrimitive(),ActionResultType.FAILED));
			queuedActions.get(state.getUnit(aq.getFullAction().getUnitId()).getPlayer()).remove(aq.getFullAction().getUnitId());
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
		
		//Reset the progress of any unit of an active player that was unable to successfully move
		for (Integer id : unsuccessfulUnits)
		{
			//Grab the unsuccessful unit
			Unit slacker = state.getUnit(id);
			//Reset the progress of the unsuccessful unit if it didn't die or something
			if (slacker!=null)
			{
				slacker.resetDurative();
			}
		}
		
		state.incrementTurn();
		for (Unit u : state.getUnits().values()) {
			u.deprecateOldView();
		}
		//Set each template to not keep the old view
		for (Integer player : state.getPlayers())
			for (@SuppressWarnings("rawtypes") Template t : state.getTemplates(player).values())
				t.deprecateOldView();
	}

	
	
	private boolean prerequisitesMet(Template<?> t, int playerNumber) {
		
		boolean prerequisitesMet = true;
		//check if the prerequisites for the template's production are met
		for (String buildingtemplateid : t.getBuildPrerequisites()) {
			if (!state.hasUnit(playerNumber, buildingtemplateid)) {
				return false;
			}
		}
		if (prerequisitesMet) {
			for (String upgradetemplateid : t.getUpgradePrerequisites()) {
				if (!state.hasUpgrade(playerNumber,upgradetemplateid)) {
					return false;
				}
			}
		}
		return true;
	}
	/**
	 * More or less duplicates the functionality of getClosestPosition in state, with claims.
	 * Also returns null instead of -1,-1 if nothing is available.
	 * @param producedTemplate 
	 * @param x
	 * @param y
	 * @param claims
	 * @param otherclaims
	 * @return The closest in bounds position, null if there is none.
	 */
	private int[] getClosestEmptyUnclaimedPosition(UnitTemplate producedTemplate, int x,
			int y, Set<Integer> claims, Set<Integer> otherclaims) {
		//This is fairly inefficient so that getCoordInt can be altered without fear
		//It could be somewhat more efficient if it didn't check as many out-of-bounds positions
		
		//if the space in question is already open
		Integer xy = getCoordInt(x, y);
		if (accessible(producedTemplate, x,y)&&!claims.contains(xy) && !otherclaims.contains(xy))
			return new int[]{x,y};
		int xextent = state.getXExtent();
		int yextent = state.getYExtent();
		int maxradius = Math.max(Math.max(x, xextent-x), Math.max(y,yextent-y));
		for (int r = 1; r<=maxradius;r++)
		{
			//go up/left diagonal
			x = x-1;
			y = y-1;
			
			//go down
			for (int i = 0; i<2*r;i++) {
				y = y + 1;
				xy = getCoordInt(x, y);
				if (accessible(producedTemplate, x,y)&&!claims.contains(xy) && !otherclaims.contains(xy))
					return new int[]{x,y};
			}
			//go right
			for (int i = 0; i<2*r;i++) {
				x = x + 1;
				xy = getCoordInt(x, y);
				if (accessible(producedTemplate, x,y)&&!claims.contains(xy) && !otherclaims.contains(xy))
					return new int[]{x,y};
			}
			//go up
			for (int i = 0; i<2*r;i++) {
				y = y - 1;
				xy = getCoordInt(x, y);
				if (accessible(producedTemplate, x,y)&&!claims.contains(xy) && !otherclaims.contains(xy))
					return new int[]{x,y};
			}
			//go left
			for (int i = 0; i<2*r;i++) {
				x = x - 1;
				xy = getCoordInt(x, y);
				if (accessible(producedTemplate, x,y)&&!claims.contains(xy) && !otherclaims.contains(xy))
					return new int[]{x,y};
			}
		}
		return null;
	}

	private boolean inRange(Unit u, Unit target) {
		return DistanceMetrics.chebyshevDistance(u.getXPosition(), u.getYPosition(), target.getXPosition(), target.getYPosition()) <= u.getTemplate().getRange();
	}
	private Integer getCoordInt(int xdest, int ydest) {
		return xdest*state.getYExtent()+ydest;
	}
	
	public void save(String filename) {
		SerializerUtil.storeState(filename, state);
	}
}
