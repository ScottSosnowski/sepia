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
import edu.cwru.sepia.util.DeepEquatable;

/**
 * A read only class that represents the exhaustion of a resource node
 * 
 * @author The Condor
 * 
 */
public class ResourceNodeExhaustionLog implements Serializable, DeepEquatable {
	private static final long serialVersionUID = 1L;
	private int nodeId;
	private ResourceNodeType nodeType;

	public ResourceNodeExhaustionLog(int exhaustedNodeId,
			ResourceNodeType resouceNodeType) {
		nodeId = exhaustedNodeId;
		this.nodeType = resouceNodeType;
	}

	public int getExhaustedNodeID() {
		return nodeId;
	}

	public ResourceNodeType getResourceNodeType() {
		return nodeType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + nodeId;
		result = prime * result
				+ ((nodeType == null) ? 0 : nodeType.hashCode());
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
		ResourceNodeExhaustionLog other = (ResourceNodeExhaustionLog) obj;
		if (nodeId != other.nodeId)
			return false;
		if (nodeType == null) {
			if (other.nodeType != null)
				return false;
		} else if (!nodeType.equals(other.nodeType))
			return false;
		return true;
	}

	@Override
	public boolean deepEquals(Object other) {
		return equals(other);
	}
}
