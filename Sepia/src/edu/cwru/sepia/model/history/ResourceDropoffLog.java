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

import edu.cwru.sepia.model.state.ResourceType;

/**
 * A read only class documenting an historic event wherein resources are dropped
 * off (deposited)
 * 
 * @author The Condor
 * 
 */
public class ResourceDropoffLog implements Serializable {
	private static final long serialVersionUID = 1L;
	private int pickuperid;
	private ResourceType resource;
	private int amount;
	private int depotid;
	private int controller;

	public ResourceDropoffLog(int gathererid, int controller, int amountpickedup, ResourceType resource, int depotid) {
		pickuperid = gathererid;
		this.resource = resource;
		amount = amountpickedup;
		this.depotid = depotid;
		this.controller = controller;
	}

	public ResourceType getResourceType() {
		return resource;
	}

	public int getGathererID() {
		return pickuperid;
	}

	public int getAmountDroppedOff() {
		return amount;
	}

	public int getDepotID() {
		return depotid;
	}

	public int getController() {
		return controller;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + amount;
		result = prime * result + controller;
		result = prime * result + depotid;
		result = prime * result + pickuperid;
		result = prime * result + ((resource == null) ? 0 : resource.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		ResourceDropoffLog other = (ResourceDropoffLog)obj;
		if(amount != other.amount)
			return false;
		if(controller != other.controller)
			return false;
		if(depotid != other.depotid)
			return false;
		if(pickuperid != other.pickuperid)
			return false;
		if(resource == null) {
			if(other.resource != null)
				return false;
		} else if(!resource.equals(other.resource))
			return false;
		return true;
	}
}