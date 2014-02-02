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

/**
 * A read only class that represents the revealing of units at the start of the
 * game
 * 
 * @author The Condor
 * 
 */
public class RevealedResourceNodeLog implements Serializable {
	private static final long serialVersionUID = 1L;
	private int nodex;
	private int nodey;
	private ResourceNodeType nodetype;

	public RevealedResourceNodeLog(int resourcenodex, int resourcenodey, ResourceNodeType resoucenodetype) {
		this.nodex = resourcenodex;
		this.nodey = resourcenodey;
		this.nodetype = resoucenodetype;
	}

	public int getResourceNodeXPosition() {
		return nodex;
	}

	public int getResourceNodeYPosition() {
		return nodey;
	}

	public ResourceNodeType getResourceNodeType() {
		return nodetype;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nodetype == null) ? 0 : nodetype.hashCode());
		result = prime * result + nodex;
		result = prime * result + nodey;
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
		RevealedResourceNodeLog other = (RevealedResourceNodeLog)obj;
		if(nodetype == null) {
			if(other.nodetype != null)
				return false;
		} else if(!nodetype.equals(other.nodetype))
			return false;
		if(nodex != other.nodex)
			return false;
		if(nodey != other.nodey)
			return false;
		return true;
	}
}
