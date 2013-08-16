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

import java.util.List;

import edu.cwru.sepia.model.persistence.generated.XmlPlayer;
import edu.cwru.sepia.model.persistence.generated.XmlResourceQuantity;
import edu.cwru.sepia.model.persistence.generated.XmlTemplate;
import edu.cwru.sepia.model.persistence.generated.XmlUnit;
import edu.cwru.sepia.model.state.PlayerState;
import edu.cwru.sepia.model.state.ResourceType;
import edu.cwru.sepia.model.state.Template;
import edu.cwru.sepia.model.state.Unit;

public class PlayerAdapter {
	

	public XmlPlayer toXml(PlayerState player) {
		XmlPlayer xml = new XmlPlayer();
		
		
		
		UnitAdapter unitAdapter = new UnitAdapter(player.getTemplates());
		
		xml.setID(player.playerNum);
		
		List<XmlUnit> units = xml.getUnit();
		for(Unit u : player.getUnits().values())
		{
			units.add(unitAdapter.toXml(u));
		}
		
		List<Integer> upgrades = xml.getUpgrade();
		upgrades.addAll(player.getUpgrades());
		
		List<XmlTemplate> xmltemplates= xml.getTemplate();
		for (@SuppressWarnings("rawtypes") Template t : player.getTemplates().values())
		{
			xmltemplates.add(TemplateAdapter.toXml(t));
		}
				
		List<XmlResourceQuantity> resources = xml.getResourceAmount();
		for(ResourceType type : ResourceType.values())
		{
			XmlResourceQuantity quantity = new XmlResourceQuantity();
			quantity.setType(type);
			quantity.setQuantity(player.getCurrentResourceAmount(type));
			resources.add(quantity);
		}

		xml.setSupply(player.getCurrentSupply());
		xml.setSupplyCap(player.getCurrentSupplyCap());
		
		return xml;
	}
	
	public PlayerState fromXml(XmlPlayer xml) {
		PlayerState player = new PlayerState(xml.getID());
		
		
		if(xml.getUpgrade() != null)
		{
			player.getUpgrades().addAll(xml.getUpgrade());
		}
		if(xml.getTemplate() != null)
		{
			for(XmlTemplate t : xml.getTemplate())
			{
				player.addTemplate(TemplateAdapter.fromXml(t,player.playerNum));
			}
		}
		
		UnitAdapter unitAdapter = new UnitAdapter(player.getTemplates());
		
		if(xml.getUnit() != null)
		{
			for(XmlUnit unit : xml.getUnit())
			{
				player.addUnit(unitAdapter.fromXml(unit));
			}
		}
		if(xml.getResourceAmount() != null)
		{
			for(XmlResourceQuantity resource : xml.getResourceAmount())
			{
				player.setCurrentResourceAmount(resource.getType(), resource.getQuantity());
			}
		}
		
		player.setCurrentSupply(xml.getSupply());
		player.setCurrentSupplyCap(xml.getSupplyCap());
		
		return player;
	}
}
