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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
public class UpgradeTemplate extends Template<Upgrade>
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID =-114292005500418550l;
	private int piercingAttackChange;
	private int basicAttackChange;
	private int armorChange;
	private int healthChange;
	private int rangeChange;
	private int sightRangeChange;
	private List<String> unitTemplatesAffected;
	private UpgradeTemplateView view;
	public UpgradeTemplate(int ID)
	{
		super(ID);
		unitTemplatesAffected = new ArrayList<String>();
	}
	/**
	 * A constructor to make an object from a view.
	 */
	public UpgradeTemplate(UpgradeTemplateView view) {
		super(view);
		setPiercingAttackChange(view.piercingAttackChange);
		setBasicAttackChange(view.basicAttackChange);
		setArmorChange(view.armorChange);
		setHealthChange(view.healthChange);
		setRangeChange(view.rangeChange);
		setSightRangeChange(view.sightRangeChange);
		for (String affectedUnitTemplate : view.affectedUnitTypes) {
			addAffectedUnit(affectedUnitTemplate);
		}
	}
	public Upgrade produceInstance(IdDistributor idsource)
	{
		return new Upgrade(this);
	}
	public List<String> getAffectedUnits()
	{
		return unitTemplatesAffected;
	}
	public void setPiercingAttackChange(int piercingAttackchange) {
		this.piercingAttackChange = piercingAttackchange;
	}
	public int getPiercingAttackChange() {
		return piercingAttackChange;
	}
	public void setBasicAttackChange(int basicAttackChange) {
		this.basicAttackChange = basicAttackChange;
	}
	public int getBasicAttackChange() {
		return basicAttackChange;
	}
	public void setArmorChange(int armorChange) {
		this.armorChange = armorChange;
	}
	public int getArmorChange() {
		return armorChange;
	}
	public void setHealthChange(int healthChange) {
		this.healthChange = healthChange;
	}
	public int getHealthChange() {
		return healthChange;
	}
	public void setRangeChange(int rangeChange) {
		this.rangeChange = rangeChange;
	}
	public int getRangeChange() {
		return rangeChange;
	}
	public void setSightRangeChange(int sightRangeChange) {
		this.sightRangeChange = sightRangeChange;
	}
	public int getSightRangeChange() {
		return sightRangeChange;
	}
	public void addAffectedUnit(String templateName)
	{
		unitTemplatesAffected.add(templateName);
	}
	@Override
	public UpgradeTemplateView getView() {
		if (view == null)
			view = new UpgradeTemplateView(this);
		return view;
	}
	@Override
	public void deprecateOldView() {
		view = null;
	}
	public static class UpgradeTemplateView extends TemplateView
	{
		private static final long	serialVersionUID	= 2L;
		private final int piercingAttackChange;
		private final int basicAttackChange;
		private final int armorChange;
		private final int healthChange;
		private final int rangeChange;
		private final int sightRangeChange;
		private final List<String> affectedUnitTypes;
		public UpgradeTemplateView(UpgradeTemplate template)
		{
			super(template);
			piercingAttackChange = template.piercingAttackChange;
			basicAttackChange = template.basicAttackChange;
			armorChange = template.armorChange;
			rangeChange = template.rangeChange;
			sightRangeChange = template.sightRangeChange;
			healthChange = template.healthChange;
			List<String> taffectedUnitTypes = new ArrayList<String>(template.unitTemplatesAffected.size());
			taffectedUnitTypes.addAll(template.unitTemplatesAffected);
			affectedUnitTypes = Collections.unmodifiableList(taffectedUnitTypes);
		}
		/**
		 * Get the increase in basic (armor-reduced) attack caused by this upgrade
		 * @return
		 */
		public int getBasicAttackChange() {
			return basicAttackChange;
		}
		/**
		 * Get the increase in piercing (armor-bypassing) attack caused by this upgrade
		 * @return
		 */
		public int getPiercingAttackChange() {
			return piercingAttackChange;
		}
		/**
		 * Get the increase in armor caused by this upgrade
		 * @return
		 */
		public int getArmorChange() {
			return armorChange;
		}
		/**
		 * Get the increase in range caused by this upgrade
		 * @return
		 */
		public int getRangeChange() {
			return rangeChange;
		}
		/**
		 * Get the increase in sight range caused by this upgrade
		 * @return
		 */
		public int getSightRangeChange() {
			return rangeChange;
		}
		/**
		 * Get the increase in health caused by this upgrade
		 * @return
		 */
		public int getHealthChange() {
			return healthChange;
		}
		
		/**
		 * Get an unmodifyable list of IDs representing the unit templates that are affected by this upgrade
		 * @return
		 */
		public List<String> getAffectedUnitTypes()
		{
			return affectedUnitTypes;
		}
	}
	@Override
	public boolean deepEquals(Object other) {
		//note, this method ignores the view.  Hopefully that is not an issue
		
				if (other == null || !this.getClass().equals(other.getClass()))
					return false;
				UpgradeTemplate o = (UpgradeTemplate)other;
				
				
				//Stuff common to all templates
				if (this.getID() != o.getID())
					return false;
				
				if (this.timeCost != o.timeCost)
					return false;
				if (this.goldCost != o.goldCost)
					return false;
				if (this.woodCost != o.woodCost)
					return false;
				if (this.foodCost != o.foodCost)
					return false;
				if (this.player != o.player)
					return false;
				if (!Objects.equals(this.buildPrerequisites, o.buildPrerequisites))
					return false;
				if (!Objects.equals(this.upgradePrerequisites, o.upgradePrerequisites))
					return false;
				{
					boolean thisnull = this.name== null;
					boolean othernull = o.name == null;
					if ((thisnull == othernull)==false)
					{
						return false;
					}
					//if both aren't null, need to check deeper
					if (!thisnull && !othernull)
					{
						if (!this.name.equals(o.name))
							return false;
					}
				}
				
				
				//UpgradeTemplate specific methods
				if (this.piercingAttackChange != o.piercingAttackChange)
					return false;
				if (this.basicAttackChange != o.basicAttackChange)
					return false;
				if (this.armorChange != o.armorChange)
					return false;
				if (this.healthChange != o.healthChange)
					return false;
				if (this.rangeChange != o.rangeChange)
					return false;
				if (this.sightRangeChange != o.sightRangeChange)
					return false;
				if (!Objects.equals(unitTemplatesAffected, o.unitTemplatesAffected))
					return false;
				return true;
	}
	
	
	
	
}
