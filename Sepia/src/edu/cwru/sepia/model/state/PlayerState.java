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
package edu.cwru.sepia.model.state;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.cwru.sepia.model.state.State.StateView;

public class PlayerState implements Serializable {
	private static final long serialVersionUID = 1L;

	public final int playerNum;
	private HashMap<Integer,Unit> units;
	private Map<Integer,Template<?>> templates;
	private Set<Integer> upgrades;
	private Map<ResourceType,Integer> currentResources;
	private int currentSupply;
	private int currentSupplyCap;
	private StateView view;
	private int[][] canSee;
	
	public PlayerState(int id) {
		this.playerNum = id;
		units = new HashMap<Integer,Unit>();
		templates = new HashMap<Integer,Template<?>>();
		upgrades = new HashSet<Integer>();
		currentResources = new HashMap<ResourceType,Integer>();	
	}
	
	public Unit getUnit(int id) {
		return units.get(id);
	}
	
	public Map<Integer,Unit> getUnits() {
		return units;
	}
	
	public void addUnit(Unit unit) {
		units.put(unit.id, unit);
	}
	
	@SuppressWarnings("rawtypes")
	public Template getTemplate(int id) {
		return templates.get(id);
	}

	@SuppressWarnings("rawtypes")
	public Template getTemplate(String name) {
		for(Template t : templates.values())
		{
			if(name.equals(t.getName()))
			{
				return t;
			}
		}
		return null;
	}
	
	public Map<Integer,Template<?>> getTemplates() {
		return templates;
	}
	
	public void addTemplate(Template<?> template) {
		templates.put(template.getID(), template);
	}
	
	public Set<Integer> getUpgrades() {
		return upgrades;
	}
	
	public Map<ResourceType, Integer> getCurrentResources() {
		Map<ResourceType, Integer> toReturn = new HashMap<ResourceType, Integer>();
		toReturn.putAll(currentResources);
		return toReturn;
	}
	
	public int getCurrentResourceAmount(ResourceType type) {
		Integer amount = currentResources.get(type);
		if(amount != null)
		{
			return amount;
		}
		else
		{
			return 0;
		}
	}
	
	public void setCurrentResourceAmount(ResourceType type, int amount) {
		currentResources.put(type, amount);
	}
	
	public void addToCurrentResourceAmount(ResourceType type, int increase) {
		setCurrentResourceAmount(type, getCurrentResourceAmount(type) + increase);
	}
	
	public int getCurrentSupply() {
		return currentSupply;
	}
	
	public void setCurrentSupply(int supply) {
		currentSupply = supply;
	}
	
	public void addToCurrentSupply(int increase) {
		setCurrentSupply(getCurrentSupply() + increase);
	}
	/**
	 * Returns the maximum supply earned by the player.
	 * @return
	 */
	public int getCurrentSupplyCap() {
		return currentSupplyCap;
	}
	
	public void setCurrentSupplyCap(int supply) {
		currentSupplyCap = supply;
	}

	public void addToCurrentSupplyCap(int increase) {
		setCurrentSupplyCap(getCurrentSupplyCap() + increase);
	}
	
	public StateView getView() {
		return view;
	}
	
	public void setStateView(StateView view) {
		this.view = view;
	}
	
	
	public int[][] getVisibilityMatrix() {
		return canSee;
	}
	
	public void setVisibilityMatrix(int[][] matrix) {
		canSee = matrix;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(canSee);
		result = prime * result + ((currentResources == null) ? 0 : currentResources.hashCode());
		result = prime * result + currentSupply;
		result = prime * result + currentSupplyCap;
		result = prime * result + playerNum;
		result = prime * result + ((templates == null) ? 0 : templates.hashCode());
		result = prime * result + ((units == null) ? 0 : units.hashCode());
		result = prime * result + ((upgrades == null) ? 0 : upgrades.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlayerState other = (PlayerState)obj;
		if (!Arrays.deepEquals(canSee, other.canSee))
			return false;
		if (currentResources == null) {
			if (other.currentResources != null)
				return false;
		} else if (!currentResources.equals(other.currentResources))
			return false;
		if (currentSupply != other.currentSupply)
			return false;
		if (currentSupplyCap != other.currentSupplyCap)
			return false;
		if (playerNum != other.playerNum)
			return false;
		if (templates == null) {
			if (other.templates != null)
				return false;
		} else if (!templates.equals(other.templates))
			return false;
		if (units == null) {
			if (other.units != null)
				return false;
		} else if (!units.equals(other.units))
			return false;
		if (upgrades == null) {
			if (other.upgrades != null)
				return false;
		} else if (!upgrades.equals(other.upgrades))
			return false;
		return true;
	}

}
