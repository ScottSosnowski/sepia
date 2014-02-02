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

import java.util.Map;

import edu.cwru.sepia.model.persistence.generated.XmlUnit;
import edu.cwru.sepia.model.state.Template;
import edu.cwru.sepia.model.state.Unit;
import edu.cwru.sepia.model.state.UnitTemplate;

public class UnitAdapter {

	private Map<Integer,Template<?>> templates;
	
	public UnitAdapter(Map<Integer,Template<?>> templates) {
		this.templates = templates;
	}
	
	public Unit fromXml(XmlUnit xml) {
		int templateId = xml.getTemplateID();
		UnitTemplate template = (UnitTemplate) templates.get(templateId);
		Unit unit = new Unit(template,xml.getID());
		unit.setXPosition(xml.getXPosition());
		unit.setYPosition(xml.getYPosition());
		if(xml.getCargoAmount() > 0)
			unit.setCargo(ResourceAdapter.fromXml(xml.getCargoType()), xml.getCargoAmount());
		unit.setHP(xml.getCurrentHealth());
		unit.setDurativeStatus(ActionAdapter.fromXml(xml.getProgressPrimitive()), xml.getProgressAmount());
		return unit;
	}
	
	public XmlUnit toXml(Unit unit) {
		XmlUnit xml = new XmlUnit();
		xml.setID(unit.id);
		xml.setCurrentHealth(unit.getCurrentHealth());
		xml.setXPosition(unit.getXPosition());
		xml.setYPosition(unit.getYPosition());
		xml.setCargoType(ResourceAdapter.toXml(unit.getCurrentCargoType()));
		if(unit.getCurrentCargoAmount() > 0)
			xml.setCargoAmount(unit.getCurrentCargoAmount());
		xml.setTemplateID(unit.getTemplate().getID());
		xml.setProgressPrimitive(ActionAdapter.toXml(unit.getActionProgressPrimitive()));
		xml.setProgressAmount(unit.getActionProgressAmount());
		
		return xml;
	}
}
