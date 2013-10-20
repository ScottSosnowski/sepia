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

import edu.cwru.sepia.util.Rectangle;

/**
 * A subtype of Target that contains all information of a resource.
 * 
 */
public class ResourceNode extends Target implements Cloneable {
	private static final long serialVersionUID = 1L;

	private ResourceNodeType type;
	private int initialAmount;
	private int amountRemaining;
	private ResourceView view;

	public ResourceNode(ResourceNodeType type, int xPosition, int yPosition, int initialAmount, int ID) {
		super(ID);
		this.type = type;
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.initialAmount = initialAmount;
		this.amountRemaining = initialAmount;
		view = null;
	}

	@Override
	protected Object clone() {
		ResourceNode copy = new ResourceNode(type, xPosition, yPosition, initialAmount, id);
		copy.amountRemaining = amountRemaining;
		return copy;
	}

	public ResourceNode copyOf() {
		return (ResourceNode)clone();
	}

	public ResourceNodeType getType() {
		return type;
	}

	public ResourceView getView() {
		if(view == null)
			view = new ResourceView(this);
		return view;
	}

	public ResourceType getResourceType() {
		return type.getResource();
	}

	public Rectangle getBounds() {
		return new Rectangle(xPosition, yPosition, 1, 1);
	}

	public int getID() {
		return id;
	}

	public int getInitialAmount() {
		return initialAmount;
	}

	public int getAmountRemaining() {
		return amountRemaining;
	}

	/**
	 * Try to pick some resources out of this node
	 * 
	 * @param amount
	 * @return The amount of resources successfully removed from the node
	 */
	public int reduceAmountRemaining(int amount) {
		int prevAmount = amountRemaining;
		amountRemaining = Math.max(0, amountRemaining - amount);
		return prevAmount - amountRemaining;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object o) {
		if(!(o instanceof ResourceNode))
			return false;
		return ((ResourceNode)o).id == id;
	}

	public static class ResourceView extends Target implements Serializable {
		private static final long serialVersionUID = 1L;
		
		private ResourceNodeType type;
		private int amountRemaining;
		private Rectangle bounds;

		public ResourceView(ResourceNode node) {
			super(node.id);
			this.xPosition = node.getXPosition();
			this.yPosition = node.getYPosition();
			this.type = node.getType();
			this.amountRemaining = node.getAmountRemaining();
			this.bounds = node.getBounds();
		}

		public int getID() {
			return id;
		}

		public int getAmountRemaining() {
			return amountRemaining;
		}

		public ResourceNodeType getType() {
			return type;
		}

		public Rectangle getBounds() {
			return bounds;
		}
	}

	@Override
	public String toString() {
		return getClass().getName() + "[Type: " + type + " xPosition: " + xPosition + " yPosition:" + yPosition
				+ " amount: " + amountRemaining + "/" + initialAmount + "]";
	}
}
