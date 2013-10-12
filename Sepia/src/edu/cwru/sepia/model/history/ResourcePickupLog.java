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
package edu.cwru.sepia.model.history;

import java.io.Serializable;

import edu.cwru.sepia.model.state.ResourceNodeType;
import edu.cwru.sepia.model.state.ResourceType;
import edu.cwru.sepia.util.DeepEquatable;

/**
 * A read only class documenting an event
 * 
 * @author The Condor
 * 
 */
public class ResourcePickupLog implements Serializable, DeepEquatable {
	private static final long serialVersionUID = 1L;
	private int pickuper;
	private ResourceType resource;
	private int amount;
	private ResourceNodeType nodeType;
	private int nodeId;
	private int controller;

	public ResourcePickupLog(int gathererId, int controller,
			ResourceType resource, int amountPickedUp, int nodeid,
			ResourceNodeType nodeType) {
		pickuper = gathererId;
		this.resource = resource;
		amount = amountPickedUp;
		this.nodeType = nodeType;
		this.nodeId = nodeid;
		this.controller = controller;
	}

	public ResourceType getResourceType() {
		return resource;
	}

	public int getGathererID() {
		return pickuper;
	}

	public int getAmountPickedUp() {
		return amount;
	}

	public ResourceNodeType getNodeType() {
		return nodeType;
	}

	public int getNodeID() {
		return nodeId;
	}

	public int getController() {
		return controller;
	}

	@Override
	public boolean deepEquals(Object other) {
		return equals(other);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + amount;
		result = prime * result + controller;
		result = prime * result + nodeId;
		result = prime * result
				+ ((nodeType == null) ? 0 : nodeType.hashCode());
		result = prime * result + pickuper;
		result = prime * result
				+ ((resource == null) ? 0 : resource.hashCode());
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
		ResourcePickupLog other = (ResourcePickupLog) obj;
		if (amount != other.amount)
			return false;
		if (controller != other.controller)
			return false;
		if (nodeId != other.nodeId)
			return false;
		if (nodeType == null) {
			if (other.nodeType != null)
				return false;
		} else if (!nodeType.equals(other.nodeType))
			return false;
		if (pickuper != other.pickuper)
			return false;
		if (resource == null) {
			if (other.resource != null)
				return false;
		} else if (!resource.equals(other.resource))
			return false;
		return true;
	}

}