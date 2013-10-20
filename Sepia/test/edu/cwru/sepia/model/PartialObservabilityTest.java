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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import edu.cwru.sepia.model.history.RevealedResourceNodeLog;
import edu.cwru.sepia.model.state.Direction;
import edu.cwru.sepia.model.state.ResourceNode;
import edu.cwru.sepia.model.state.ResourceNode.ResourceView;
import edu.cwru.sepia.model.state.ResourceNodeType;
import edu.cwru.sepia.model.state.ResourceType;
import edu.cwru.sepia.model.state.State;
import edu.cwru.sepia.model.state.State.StateView;
import edu.cwru.sepia.model.state.Template;
import edu.cwru.sepia.model.state.Unit;
import edu.cwru.sepia.model.state.Unit.UnitView;
import edu.cwru.sepia.model.state.UnitTemplate;
import edu.cwru.sepia.util.Rectangle;
import edu.cwru.sepia.util.TypeLoader;

public class PartialObservabilityTest {
  
	@Test
	public void checkReveal()
	{
		assertFalse(true);
//		int player = 0;
//		Random r = new Random();
//		State state = new State();
//		state.setSize(40, 40);
//		state.addPlayer(player);
//		boolean correctrevealedness=r.nextBoolean();
//		state.setRevealedResources(correctrevealedness);
//		
//		EventLoggerView e = state.getView(player).getEventLog();
//		EventLoggerView obs = state.getView(Agent.OBSERVER_ID).getEventLog();
//		Map<Pair,Pair> actualpositioning=new HashMap<Pair,Pair>();
//		//Repeatedly add things, changing the revealedness every so often 
//		for (int i = 0; i<100;i++)
//		{
//			ResourceNode newres = new ResourceNode(r.nextBoolean()?ResourceNode.Type.GOLD_MINE:ResourceNode.Type.TREE,r.nextInt(state.getXExtent()),r.nextInt(state.getYExtent()),r.nextInt(),state.nextTargetID());
//			state.addResource(newres);
//			Pair pos = new Pair(newres.getxPosition(), newres.getyPosition());
//			Pair prevnum = actualpositioning.get(pos);
//			
//			int nummine = newres.getType()==ResourceNode.Type.GOLD_MINE?1:0;
//			int numtree = newres.getType()==ResourceNode.Type.TREE?1:0;
//			if (prevnum!=null)
//			{	
//				nummine+=prevnum.i1;
//				numtree+=prevnum.i2;
//			}
//			actualpositioning.put(pos, new Pair(nummine,numtree));
//			revealedStatusChecker(i, e,actualpositioning,correctrevealedness);
//			revealedStatusChecker(i, obs,actualpositioning,correctrevealedness);
//			correctrevealedness = r.nextDouble() < 0.9?correctrevealedness:!correctrevealedness;
//			state.setRevealedResources(correctrevealedness);
//		}
	}
	
	
@Test
/**
 * Do a long random walk and check the sight ranges to see if they are right
 * Also check that the getUnit and getResourceNode match the sight
 * Check that the getAllUnit and getAllResource match the sight/get 
 */
public void sightTest() throws FileNotFoundException, JAXBException {
	//Set up the state
	State state=new State();
	state.setSize(20, 20);

	int player = 0;
	int otherplayer = 1;
	List<Template<?>> templates = TypeLoader.loadFromFile("data/unit_templates",player,state);
	List<Template<?>> templates2 = TypeLoader.loadFromFile("data/unit_templates",otherplayer,state);
	
		ResourceNode[][] nodegrid = new ResourceNode[state.getXExtent()][state.getYExtent()];
	Unit[][] unitgrid = new Unit[state.getXExtent()][state.getYExtent()];
	for (Template<?> t : templates)
		state.addTemplate(t);
	for (Template<?> t2 : templates2)
		state.addTemplate(t2);
	UnitTemplate template = ((UnitTemplate)state.getTemplate(player, "Footman"));
	UnitTemplate enemytemplate = ((UnitTemplate)state.getTemplate(otherplayer, "Footman"));
	List<Unit> myunits = new ArrayList<Unit>();
	{	
		Unit u = new Unit(template,state.nextTargetId());
		state.addUnit(u, 0, 0);
		myunits.add(u);
	}
	for (int i = 0; i < state.getXExtent(); i++)
	{
		for (int j = 0; j < state.getYExtent(); j++)
		{
			nodegrid[i][j]=new ResourceNode(new ResourceNodeType("GOLD_MINE", new ResourceType("GOLD")),
					i,j,2344,state.nextTargetId());
			unitgrid[i][j]=new Unit(enemytemplate,state.nextTargetId());
			state.addUnit(unitgrid[i][j], i, j);
			state.addResource(nodegrid[i][j]);
		}
	}
	
	Random r = new Random();
	int numsteps = 1000;
	int howoftentoaddorremoveunits=25;
	StateView view = state.getView(player);
	for (int n = 0;n<numsteps;n++)
	{
		if ((n+1)%howoftentoaddorremoveunits==0)
		{
			if (myunits.size()==0||r.nextBoolean())//add
			{
				Unit u = new Unit(template,state.nextTargetId());
				state.addUnit(u, r.nextInt(state.getXExtent()), r.nextInt(state.getYExtent()));
				myunits.add(u);
			}
			else //remove
			{
				Unit u = myunits.remove(r.nextInt(myunits.size()));
				state.removeUnit(u.id);
			}
		}
		for (Unit u : myunits)
		{
		Direction d = Direction.values()[r.nextInt(Direction.values().length)];
		if (state.inBounds(u.getXPosition()+d.xComponent(), u.getYPosition()+d.yComponent()))
			state.moveUnit(u, d);
		}
		System.out.println("Step: "+n);
		System.out.println(printView(myunits, view));
		List<Integer> allresources = view.getAllResourceIds();
		List<Integer> allunits = view.getAllUnitIds();
		for (int i = 0; i<state.getXExtent(); i++)
			for (int j = 0; j<state.getYExtent(); j++)
			{
				assertTrue("Can't see properly in fully observable case",view.canSee(i, j) == true);
				
			}
		for (int i = 0; i<unitgrid.length;i++)
			for (int j = 0; j<unitgrid[i].length;j++)
				assertTrue(view.getUnit(unitgrid[i][j].id).getID() == unitgrid[i][j].id);//view.canSee(unitgrid[i][j].getxPosition(),unitgrid[i][j].getyPosition()))
		for (int i = 0; i<nodegrid.length;i++)
			for (int j = 0; j<nodegrid[i].length;j++)
				assertTrue(view.getResourceNode(nodegrid[i][j].id).getID() == nodegrid[i][j].id);//view.canSee(unitgrid[i][j].getxPosition(),unitgrid[i][j].getyPosition()))
		//check that you can actually get (and by the previous tests, see) all of the ids you are given
		for (Integer id : allunits)
		{
			UnitView unit = view.getUnit(id);
			assertTrue("Couldn't get unit that was listed in getAllUnits",unit!=null);
			assertTrue("It got the wrong unit (this is odd and bad beyond the partial observability)",unit.getID() == id);
		}
		for (Integer id : allresources)
		{
			ResourceView resource = view.getResourceNode(id);
			assertTrue("Couldn't get unit that was listed in getAllUnits",resource!=null);
			assertTrue("It got the wrong unit (this is odd and bad beyond the partial observability)",resource.getID() == id);
		}
		//check that you are given all the ones that you can see
		for (int i = 0; i < unitgrid.length; i++)
			for (int j = 0; j < unitgrid[i].length; j++)
			{
				int x = unitgrid[i][j].getXPosition();
				int y = unitgrid[i][j].getYPosition();
				boolean cansee = view.canSee(x, y);
				assertTrue(cansee == allunits.contains(unitgrid[i][j].id) );
			}
		for (int i = 0; i < nodegrid.length; i++)
			for (int j = 0; j < nodegrid[i].length; j++)
			{
				int x = nodegrid[i][j].getXPosition();
				int y = nodegrid[i][j].getYPosition();
				//x and y should just be i and j
				boolean cansee = view.canSee(x, y);
				assertTrue(cansee == allresources.contains(nodegrid[i][j].id) );
			}
		
		//Check the unitat and resourceat abilities
		//Note that you put something at each position
		for (int i = 0; i<view.getXExtent(); i++)
			for (int j = 0; j<view.getYExtent(); j++)
			{
				boolean cansee = view.canSee(i, j);
				boolean seeunitthere = view.unitAt(i, j)!=null;
				boolean seeresourcethere = view.resourceAt(i, j)!=null;
				assertTrue(cansee == seeunitthere);
				assertTrue(cansee == seeresourcethere);
			}
	}
	
	state.setFogOfWar(true);
	for (int n = 0;n<numsteps;n++)
	{
		
		for (Unit u : myunits)
		{
			Direction d = Direction.values()[r.nextInt(Direction.values().length)];
			if (state.inBounds(u.getXPosition()+d.xComponent(), u.getYPosition()+d.yComponent()))
				state.moveUnit(u, d);
		}
		
		System.out.println("Step: "+n);
		System.out.println(printView(myunits, view));
		List<Integer> allresources = view.getAllResourceIds();
		List<Integer> allunits = view.getAllUnitIds();
		for (int i = 0; i<state.getXExtent(); i++)
			for (int j = 0; j<state.getYExtent(); j++)
			{
				boolean cansee = view.canSee(i, j);
				boolean inrange = false;
				for (Unit u : myunits)
				{
					if( new Rectangle(i, j).distanceTo(u.getBounds()) <= u.getTemplate().getSightRange())
					{
						inrange = true;
						break;
					}
					
				}
				assertTrue("Step "+n+":"+(cansee?"Can":"Can't") + " see "+i+","+j+", but it "+(inrange?"is":"isn't")+" in range ",cansee == inrange);
			}
		
		
		for (int i = 0; i<unitgrid.length;i++)
			for (int j = 0; j<unitgrid[i].length;j++)
			{
				boolean cansee = view.canSee(unitgrid[i][j].getXPosition(),unitgrid[i][j].getYPosition());
				UnitView unitseen = view.getUnit(unitgrid[i][j].id);
				Integer idseen = unitseen==null?null:unitseen.getID();
				assertTrue(idseen == null && !cansee || idseen == unitgrid[i][j].id && cansee);
			}
		for (int i = 0; i<nodegrid.length;i++)
			for (int j = 0; j<nodegrid[i].length;j++)
			{
				boolean cansee = view.canSee(nodegrid[i][j].getXPosition(),nodegrid[i][j].getYPosition());
				ResourceView nodeseen = view.getResourceNode(nodegrid[i][j].id);
				Integer idseen = nodeseen==null?null:nodeseen.getID();
				assertTrue(idseen == null && !cansee || idseen == nodegrid[i][j].id && cansee);
			}
		//check that you can actually get (and by the previous tests, see) all of the ids you are given
		for (Integer id : allunits)
		{
			UnitView unit = view.getUnit(id);
			assertTrue("Couldn't get unit that was listed in getAllUnits",unit!=null);
			assertTrue("It got the wrong unit (this is odd and bad beyond the partial observability)",unit.getID() == id);
		}
		for (Integer id : allresources)
		{
			ResourceView resource = view.getResourceNode(id);
			assertTrue("Couldn't get resource that was listed in getAllResources",resource!=null);
			assertTrue("It got the wrong resource (this is odd and bad beyond the partial observability)",resource.getID() == id);
		}
		//check that you are given all the ones that you can see
		for (int i = 0; i < unitgrid.length; i++)
			for (int j = 0; j < unitgrid[i].length; j++)
			{
				int x = unitgrid[i][j].getXPosition();
				int y = unitgrid[i][j].getYPosition();
				boolean cansee = view.canSee(x, y);
				assertTrue(cansee == allunits.contains(unitgrid[i][j].id) );
			}
		for (int i = 0; i < nodegrid.length; i++)
			for (int j = 0; j < nodegrid[i].length; j++)
			{
				int x = nodegrid[i][j].getXPosition();
				int y = nodegrid[i][j].getYPosition();
				//x and y should just be i and j
				boolean cansee = view.canSee(x, y);
				assertTrue(cansee == allresources.contains(nodegrid[i][j].id) );
			}
		
		//Check the unitat and resourceat abilities
		//Note that you put something at each position
		for (int i = 0; i<view.getXExtent(); i++)
			for (int j = 0; j<view.getYExtent(); j++)
			{
				boolean cansee = view.canSee(i, j);
				boolean seeunitthere = view.unitAt(i, j)!=null;
				boolean seeresourcethere = view.resourceAt(i, j)!=null;
				assertTrue(cansee == seeunitthere);
				assertTrue(cansee == seeresourcethere);
			}
		
	}
	
}
public String printView(List<Unit> myunits,StateView v)
{
	String s="";
	for (int i = 0; i<v.getXExtent();i++)
	{
		for (int j = 0; j<v.getYExtent();j++)
		{
			boolean unitthere = false;
			for (Unit u : myunits)
			{
				if (u.getXPosition() == i && u.getYPosition() == j)
				{	
					unitthere = true;
					break;
				}
			}
			s+=unitthere?"|u":v.canSee(i, j)?"|x":"| ";
			
		}
		s+="|\n";
	}
	return s;
}

/**
 * A repeated call from checkReveal
 */
@SuppressWarnings("unused")
private void revealedStatusChecker(int step, List<RevealedResourceNodeLog> revealedResources, Map<Pair,Pair> actualpositioning, boolean shouldberevealed)
{
	System.out.println("Step "+step + ": " + (shouldberevealed?"revealed":"hidden"));
	if (!shouldberevealed)
	{
		assertTrue("Step " + step + ": Resources were revealed when they should have been hidden",revealedResources.size()==0);
	}
	else //They should be revealed
	{
		Map<Pair, Pair> revealedResourcePositioning=new HashMap<Pair, Pair>();;
		//Make it a map
		for (RevealedResourceNodeLog log : revealedResources)
		{
			Pair pos = new Pair(log.getResourceNodeXPosition(), log.getResourceNodeYPosition());
			int numgoldalreadythere = 0;
			int numtreealreadythere = 0;
			if (revealedResourcePositioning.containsKey(pos))
			{
				Pair alreadythere = revealedResourcePositioning.get(pos);
				numgoldalreadythere = alreadythere.i1;
				numtreealreadythere = alreadythere.i2;
			}
			if (log.getResourceNodeType().getName().equals("GOLD_MINE"))
				numgoldalreadythere++;
			else if (log.getResourceNodeType().getName().equals("TREE"))
				numtreealreadythere++;
			revealedResourcePositioning.put(pos,new Pair(numgoldalreadythere,numtreealreadythere));
		}
		System.out.println("Actual:");
		System.out.println(actualpositioning);
		System.out.println("Seen:");
		System.out.println(revealedResourcePositioning);
		//Check to see that they are the same
		for (Entry<Pair,Pair> real : actualpositioning.entrySet())
		{
			Pair seen = revealedResourcePositioning.get(real.getKey());
			assertTrue("Step " + step + "Less something seen than there are",seen != null);
			assertTrue("Step " + step + "More gold mines seen than there are",seen.i1 >= real.getValue().i1);
			assertTrue("Step " + step + "Less gold mines seen than there are",seen.i1 <= real.getValue().i1);
			assertTrue("Step " + step + "More gold mines seen than there are",seen.i2 >= real.getValue().i2);
			assertTrue("Step " + step + "Less gold mines seen than there are",seen.i2 <= real.getValue().i2);
		}
		for (Entry<Pair,Pair> seen : revealedResourcePositioning.entrySet())
		{
			Pair real = actualpositioning.get(seen.getKey());
			assertTrue("Step " + step + "More something seen than there are",real != null);
		}
	}
}
//need to test adding and removing units
//old unit view isn't updated between events
//same with templates and resources
//check some of the boolean things like occupied

private static class Pair
{
	public final int i1;
	public final int i2;
	public Pair(int i1, int i2)
	{
		this.i1=i1;
		this.i2=i2;
	}
	public int hashCode()
	{
		return i1+i2 + i1*i2;
	}
	public boolean equals(Object other)
	{
		if (!other.getClass().equals(this.getClass()))
			return false;
		Pair pairother = (Pair)other;
		return pairother.i1 == i1 && pairother.i2 == i2;
	}
	public String toString()
	{
		return i1 + " " + i2;
	}
}
}