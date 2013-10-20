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
package edu.cwru.sepia.model.persistence;

import edu.cwru.sepia.model.persistence.generated.XmlResourceNode;
import edu.cwru.sepia.model.persistence.generated.XmlResourceNodeType;
import edu.cwru.sepia.model.persistence.generated.XmlResourceType;
import edu.cwru.sepia.model.state.ResourceNode;
import edu.cwru.sepia.model.state.ResourceNodeType;
import edu.cwru.sepia.model.state.ResourceType;

public class ResourceAdapter {

	public static ResourceNode fromXml(XmlResourceNode xml) {
		ResourceNode node = new ResourceNode(fromXml(xml.getType()), xml.getXPosition(), xml.getYPosition(), 
								 xml.getInitialAmount(), xml.getID());
		node.reduceAmountRemaining(xml.getInitialAmount() - xml.getAmountRemaining());
		return node;
	}
	
	public static ResourceNodeType fromXml(XmlResourceNodeType xml) {
		return new ResourceNodeType(xml.getName(), fromXml(xml.getResource()));
	}
	
	public static ResourceType fromXml(XmlResourceType xml) {
		return new ResourceType(xml.getName());
	}
	
	public static XmlResourceNode toXml(ResourceNode node) {
		XmlResourceNode xml = new XmlResourceNode();
		xml.setType(toXml(node.getType()));
		xml.setInitialAmount(node.getInitialAmount());
		xml.setAmountRemaining(node.getAmountRemaining());
		xml.setXPosition(node.getXPosition());
		xml.setYPosition(node.getYPosition());
		xml.setID(node.id);
		return xml;
	}
	
	public static XmlResourceNodeType toXml(ResourceNodeType type) {
		XmlResourceNodeType xml = new XmlResourceNodeType();
		xml.setName(type.getName());
		xml.setResource(toXml(type.getResource()));
		return xml;
	}
	
	public static XmlResourceType toXml(ResourceType type) {
		XmlResourceType xml = new XmlResourceType();
		xml.setName(type.getName());
		return xml;
	}
}
