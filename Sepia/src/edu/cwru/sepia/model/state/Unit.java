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

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.model.state.UnitTemplate.UnitTemplateView;

public class Unit extends Target /*implements Cloneable*/ {
	private static final long serialVersionUID = 1L;
	
	private UnitView view;
	protected int currentHealth;
	protected int xPosition;
	protected int yPosition;
	protected UnitTemplate template;
	protected ResourceType cargoType;
	protected int cargoAmount;
	protected Action currentDurativePrimitive;
	protected int currentDurativeProgress;
	public Unit(UnitTemplate template, int ID) {
		super(ID);
		this.template = template;
		this.currentHealth = template.getBaseHealth();
		currentDurativeProgress = 0;
		currentDurativePrimitive = null;
	}
	public boolean deepEquals(Object other) {
		if (other == null || !this.getClass().equals(other.getClass()))
			return false;
		Unit o = (Unit)other;
		if (this.id != o.id)
			return false;
		if (this.currentHealth != o.currentHealth)
			return false;
		if (this.xPosition != o.xPosition)
			return false;
		if (this.yPosition != o.yPosition)
			return false;
		if ((this.template == null) != (o.template == null) ||!this.template.deepEquals(o.template))
			return false;
		if (this.cargoType != o.cargoType)
			return false;
		if (this.cargoAmount != o.cargoAmount)
			return false;
		if ((this.currentDurativePrimitive == null) != (o.currentDurativePrimitive == null) || !this.currentDurativePrimitive.deepEquals(o.currentDurativePrimitive))
			return false;
		if (this.currentDurativeProgress != o.currentDurativeProgress)
			return false;
		return true;
		
	}
	
	public int getPlayer() {
		return template.getPlayer();
	}

	public char getCharacter() {
		return template.getCharacter();
	}
	public int getCurrentHealth() {
		return currentHealth;
	}

	public int getXPosition() {
		return xPosition;
	}
	/**
	 * Set the x position of the unit.
	 * DO NOT USE THIS TO MOVE UNITS
	 * @param x
	 */
	public void setXPosition(int x) {
		xPosition = x;
		
	}
	
	public int getYPosition() {
		return yPosition;
	}
	/**
	 * Set the y position of the unit.
	 * DO NOT USE THIS TO MOVE UNITS
	 * @param y
	 */
	public void setYPosition(int y) {
		yPosition = y;
	}
	
	public boolean occupiesLocation(int x, int y) {
		return between(x, xPosition, xPosition + getTemplate().getWidth() - 1) &&
				between(y, yPosition, yPosition + getTemplate().getHeight() - 1);
	}
	
	private boolean between(int value, int left, int right) {
		return value >= left && value <= right;
	}
	
	public UnitTemplate getTemplate() {
		return template;
	}
	/**
	 * Set the health of this unit to a specific value.
	 * @param amount The new amount of hit points.
	 */
	public void setHP(int amount)
	{
		currentHealth=amount;
	}
	/**
	 * Get the amount of progress toward the completion of a primitive action.
	 * @return
	 */
	public int getActionProgressAmount()
	{
		return currentDurativeProgress;
	}
	/**
	 * Get the primitive action that the unit is working towards.
	 * @return
	 */
	public Action getActionProgressPrimitive()
	{
		return currentDurativePrimitive;
	}
	public void resetDurative() {
		currentDurativePrimitive = null;
		currentDurativeProgress = 0;
	}
	/**
	 * Set the status of durative actions.
	 * @param primitive The primitive action being worked towards.
	 * @param progress The amount of progress towards the primitive action.
	 */
	public void setDurativeStatus(Action primitive, int progress) {
		
		currentDurativePrimitive = primitive;
		currentDurativeProgress = progress;
	}
	public boolean canGather()
	{
		return template.canGather;
	}
	public boolean canBuild()
	{
		return template.canBuild;
	}
	public boolean canMove()
	{
		return template.canMove;
	}
	public boolean canAttack()
	{
		return template.canAttack();
	}
	@Override
	public int hashCode() {
		return id;
	}
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Unit))
			return false;
		return ((Unit)o).id == id;
	}
	@Override
	public String toString() {
		return "Unit [ID=" + id + ", currentHealth="
				+ currentHealth + ", player=" + template.getPlayer() + ", xPosition="
				+ xPosition + ", yPosition=" + yPosition + ", template="
				+ template + ", cargoType=" + cargoType + ", cargoAmount="
				+ cargoAmount + "]";
	}
	public boolean setCargo(ResourceType type, int amount) {
		if(!(template).canGather())
			return false;
		cargoType = type;
		cargoAmount = amount;
		return true;
	}
	public void clearCargo() {
		cargoType = null;
		cargoAmount = 0;
	}
	public ResourceType getCurrentCargoType() {
		return cargoType;
	}
	public int getCurrentCargoAmount() {
		return cargoAmount;
	}
	public UnitView getView() {
		if(view == null)
			view = new UnitView(this);
		return view;
	}
	public void deprecateOldView()
	{
		//Stop linking to the old one
		view=null;
		
	}
	public static class UnitView implements Serializable{
		private static final long serialVersionUID = 1L;
		
		private final int currentHealth;
		private final int cargoAmount;
		private final ResourceType cargoType;
		private final int xPosition;
		private final int yPosition;
		private final UnitTemplateView templateView;
		private final int ID;
		private final Action currentDurativePrimitive;
		private final int currentDurativeProgress;
		public UnitView(Unit unit) {
			currentHealth = unit.currentHealth;
			templateView = unit.template.getView();
			cargoAmount = unit.cargoAmount;
			cargoType = unit.cargoType;
			xPosition = unit.xPosition;
			yPosition = unit.yPosition;
			ID = unit.id;
			currentDurativePrimitive = unit.currentDurativePrimitive;
			currentDurativeProgress = unit.currentDurativeProgress;
		}
		/**
		 * Get the current health of the unit
		 * @return
		 */
		public int getHP()   {
			return currentHealth;
		}
		/**
		 * Get the amount of gold or wood being carried by this unit.
		 * @return
		 */
		public int getCargoAmount()   {
			return cargoAmount;
		}
		/**
		 * Get the type of resource being carried by the unit.
		 * This is only relevant if getCargoAmount() indicates an amount being carried.
		 * @return
		 */
		public ResourceType getCargoType()   {
			return cargoType;
		}
		/**
		 * Get the x coordinate of the unit
		 * @return
		 */
		public int getXPosition()   {
			return xPosition;
		}
		/**
		 * Get the y coordinate of the unit
		 * @return
		 */
		public int getYPosition()   {
			return yPosition;
		}
		
		/**
		 * Get the progress of this unit toward executing a durative action.
		 * @return
		 */
		public int getCurrentDurativeProgress() {
			return currentDurativeProgress;
		}
		/**
		 * Get the template id of the unit or upgrade currently being produced.
		 * This is only relevant if getCurrentProductionAmount indicates something has progress
		 * @return The id of the template of what is being produced, or null
		 */
		public Action getCurrentDurativeAction() {
			return currentDurativePrimitive;
		}
		
		/**
		 * Get a view of the template this unit was made from.
		 * This carries information shared by all units of that type,
		 * such as the name, attack, armor, basic health, cost, 
		 * prerequisites for building it, and what units/upgrades it can produce 
		 * @return
		 */
		public UnitTemplateView getTemplateView()   {
			return templateView;
		}
		/**
		 * Get the unique identification number of this unit
		 * @return
		 */
		public int getID() {
			return ID;
		}
		
		
	}
}
