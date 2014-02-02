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
package edu.cwru.sepia.environment.persistence;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.cwru.sepia.model.persistence.ResourceAdapter;
import edu.cwru.sepia.model.persistence.generated.XmlResourceNode;
import edu.cwru.sepia.model.state.ResourceNode;
import edu.cwru.sepia.model.state.ResourceNodeType;
import edu.cwru.sepia.model.state.ResourceType;

public class ResourceNodeAdapterTest {

	private ResourceNodeType mine = new ResourceNodeType("GOLD_MINE", new ResourceType("GOLD"));
	@Test
	public void testToXml() {
		ResourceNode node = new ResourceNode(mine, 1, 2, 1000,34);
		
		XmlResourceNode xml = ResourceAdapter.toXml(node);
		
		assertEquals("resource type did not match!", node.getType(), xml.getType());
		assertEquals("x position did not match!", node.getXPosition(), xml.getXPosition());
		assertEquals("y position did not match!", node.getYPosition(), xml.getYPosition());
		assertEquals("amount remaining did not match!", node.getAmountRemaining(), xml.getAmountRemaining());
	}
	
	@Test
	public void testFromXml() {
		XmlResourceNode xml = new XmlResourceNode();
		xml.setType(ResourceAdapter.toXml(mine));
		xml.setXPosition(1);
		xml.setYPosition(2);
		xml.setAmountRemaining(100);
		xml.setID(34);
		ResourceNode node = ResourceAdapter.fromXml(xml);

		assertEquals("resource type did not match!", xml.getType(), node.getType());
		assertEquals("x position did not match!", xml.getXPosition(), node.getXPosition());
		assertEquals("y position did not match!", xml.getYPosition(), node.getYPosition());
		assertEquals("amount remaining did not match!", xml.getAmountRemaining(), node.getAmountRemaining());
		assertEquals("ID did not match!", xml.getID(), node.id);
		
	}
}
