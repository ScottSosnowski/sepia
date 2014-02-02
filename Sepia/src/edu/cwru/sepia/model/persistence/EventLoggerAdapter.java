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

import edu.cwru.sepia.model.history.BirthLog;
import edu.cwru.sepia.model.history.DamageLog;
import edu.cwru.sepia.model.history.DeathLog;
import edu.cwru.sepia.model.history.EventLogger;
import edu.cwru.sepia.model.history.ResourceDropoffLog;
import edu.cwru.sepia.model.history.ResourceNodeExhaustionLog;
import edu.cwru.sepia.model.history.ResourcePickupLog;
import edu.cwru.sepia.model.history.RevealedResourceNodeLog;
import edu.cwru.sepia.model.history.UpgradeLog;
import edu.cwru.sepia.model.persistence.generated.XmlBirthLog;
import edu.cwru.sepia.model.persistence.generated.XmlBirthLogList;
import edu.cwru.sepia.model.persistence.generated.XmlDamageLog;
import edu.cwru.sepia.model.persistence.generated.XmlDamageLogList;
import edu.cwru.sepia.model.persistence.generated.XmlDeathLog;
import edu.cwru.sepia.model.persistence.generated.XmlDeathLogList;
import edu.cwru.sepia.model.persistence.generated.XmlEventLogger;
import edu.cwru.sepia.model.persistence.generated.XmlResourceDropoffLog;
import edu.cwru.sepia.model.persistence.generated.XmlResourceDropoffLogList;
import edu.cwru.sepia.model.persistence.generated.XmlResourceNodeExhaustionLog;
import edu.cwru.sepia.model.persistence.generated.XmlResourceNodeExhaustionLogList;
import edu.cwru.sepia.model.persistence.generated.XmlResourcePickupLog;
import edu.cwru.sepia.model.persistence.generated.XmlResourcePickupLogList;
import edu.cwru.sepia.model.persistence.generated.XmlRevealedResourceNodeLog;
import edu.cwru.sepia.model.persistence.generated.XmlUpgradeLog;
import edu.cwru.sepia.model.persistence.generated.XmlUpgradeLogList;

public class EventLoggerAdapter {
	public static XmlEventLogger toXml(EventLogger eventLogger) {
		XmlEventLogger xml = new XmlEventLogger();
		{
			int maxRound = eventLogger.getHighestRoundBirth();
			for (int roundNumber = 0; roundNumber < maxRound + 1; roundNumber++) {
				XmlBirthLogList xmlloglist = new XmlBirthLogList();
				xmlloglist.setRoundNumber(roundNumber);
				for (BirthLog log : eventLogger.getBirths(roundNumber)) {
					XmlBirthLog xmllog = new XmlBirthLog();
					xmllog.setNewUnitID(log.getNewUnitID());
					xmllog.setParentID(log.getParentID());
					xmllog.setController(log.getController());
					xmlloglist.getBirthLog().add(xmllog);
				}
				xml.getBirthLogList().add(xmlloglist);
			}
		}
		{
			int maxRound = eventLogger.getHighestRoundDeath();
			for (int roundNumber = 0; roundNumber < maxRound + 1; roundNumber++) {
				XmlDeathLogList xmlloglist = new XmlDeathLogList();
				xmlloglist.setRoundNumber(roundNumber);
				for (DeathLog log : eventLogger.getDeaths(roundNumber)) {
					XmlDeathLog xmllog = new XmlDeathLog();
					xmllog.setDeadUnitID(log.getDeadUnitID());
					xmllog.setController(log.getController());
					xmlloglist.getDeathLog().add(xmllog);
				}
				xml.getDeathLogList().add(xmlloglist);
			}
		}
		{
			int maxRound = eventLogger.getHighestRoundDamage();
			for (int roundNumber = 0; roundNumber < maxRound + 1; roundNumber++) {
				XmlDamageLogList xmlloglist = new XmlDamageLogList();
				xmlloglist.setRoundNumber(roundNumber);
				for (DamageLog log : eventLogger.getDamage(roundNumber)) {
					XmlDamageLog xmllog = new XmlDamageLog();
					xmllog.setAttackerID(log.getAttackerID());
					xmllog.setAttackerController(log.getAttackerController());
					xmllog.setDefenderID(log.getDefenderID());
					xmllog.setDefenderController(log.getDefenderController());
					xmllog.setDamage(log.getDamage());
					xmlloglist.getDamageLog().add(xmllog);
				}
				xml.getDamageLogList().add(xmlloglist);
			}
		}
		{
			int maxRound = eventLogger.getHighestRoundResourceDropoff();
			for (int roundNumber = 0; roundNumber < maxRound + 1; roundNumber++) {
				XmlResourceDropoffLogList xmlloglist = new XmlResourceDropoffLogList();
				xmlloglist.setRoundNumber(roundNumber);
				for (ResourceDropoffLog log : eventLogger.getResourceDropoffs(roundNumber)) {
					XmlResourceDropoffLog xmllog = new XmlResourceDropoffLog();
					xmllog.setGathererID(log.getGathererID());
					xmllog.setDepotID(log.getDepotID());
					xmllog.setController(log.getController());
					xmllog.setResourceType(ResourceAdapter.toXml(log.getResourceType()));
					xmllog.setDepositAmount(log.getAmountDroppedOff());
					xmlloglist.getResourceDropoffLog().add(xmllog);
				}
				xml.getResourceDropoffLogList().add(xmlloglist);
			}
		}
		{
			int maxRound = eventLogger.getHighestRoundResourcePickup();
			for (int roundNumber = 0; roundNumber < maxRound + 1; roundNumber++) {
				XmlResourcePickupLogList xmlloglist = new XmlResourcePickupLogList();
				xmlloglist.setRoundNumber(roundNumber);
				for (ResourcePickupLog log : eventLogger.getResourcePickups(roundNumber)) {
					XmlResourcePickupLog xmllog = new XmlResourcePickupLog();
					xmllog.setGathererID(log.getGathererID());
					xmllog.setNodeID(log.getNodeID());
					xmllog.setResourceType(ResourceAdapter.toXml(log.getResourceType()));
					xmllog.setPickupAmount(log.getAmountPickedUp());
					xmllog.setController(log.getController());
					xmllog.setNodeType(ResourceAdapter.toXml(log.getNodeType()));
					xmlloglist.getResourcePickupLog().add(xmllog);
				}
				xml.getResourcePickupLogList().add(xmlloglist);
			}
		}
		{
			int maxRound = eventLogger.getHighestRoundResourceNodeExhaustion();
			for (int roundNumber = 0; roundNumber < maxRound + 1; roundNumber++) {
				XmlResourceNodeExhaustionLogList xmlloglist = new XmlResourceNodeExhaustionLogList();
				xmlloglist.setRoundNumber(roundNumber);
				for (ResourceNodeExhaustionLog log : eventLogger.getResourceNodeExhaustions(roundNumber)) {
					XmlResourceNodeExhaustionLog xmllog = new XmlResourceNodeExhaustionLog();
					xmllog.setExhaustedNodeID(log.getExhaustedNodeID());
					xmllog.setExhaustedNodeType(ResourceAdapter.toXml(log.getResourceNodeType()));
					xmlloglist.getResourceNodeExhaustionLog().add(xmllog);
				}
				xml.getResourceNodeExhaustionLogList().add(xmlloglist);
			}
		}
		{
			int maxRound = eventLogger.getHighestRoundUpgrade();
			for (int roundNumber = 0; roundNumber < maxRound + 1; roundNumber++) {
				XmlUpgradeLogList xmlloglist = new XmlUpgradeLogList();
				xmlloglist.setRoundNumber(roundNumber);
				for (UpgradeLog log : eventLogger.getUpgrades(roundNumber)) {
					XmlUpgradeLog xmllog = new XmlUpgradeLog();
					xmllog.setUpgradeTemplateID(log.getUpgradeTemplateID());
					xmllog.setProducingUnitID(log.getProducingUnitID());
					xmllog.setController(log.getController());
					xmlloglist.getUpgradeLog().add(xmllog);
				}
				xml.getUpgradeLogList().add(xmlloglist);
			}
		}
		for (RevealedResourceNodeLog log : eventLogger.getRevealedResourceNodes()) {
			XmlRevealedResourceNodeLog xmllog = new XmlRevealedResourceNodeLog();
			xmllog.setXPosition(log.getResourceNodeXPosition());
			xmllog.setYPosition(log.getResourceNodeYPosition());
			xmllog.setNodeType(ResourceAdapter.toXml(log.getResourceNodeType()));
			xml.getRevealedResourceNodeLog().add(xmllog);
		}
		return xml;
	}
	
	public static EventLogger fromXml(XmlEventLogger xml) {
		EventLogger e = new EventLogger();
		for (XmlBirthLogList xmlSingleTurn : xml.getBirthLogList())
		{
			for (XmlBirthLog xmlSingleEvent : xmlSingleTurn.getBirthLog()) {
				e.recordBirth(xmlSingleTurn.getRoundNumber(), xmlSingleEvent.getNewUnitID(), xmlSingleEvent.getParentID(), xmlSingleEvent.getController());
			}
		}
		for (XmlDeathLogList xmlSingleTurn : xml.getDeathLogList())
		{
			for (XmlDeathLog xmlSingleEvent : xmlSingleTurn.getDeathLog()) {
				e.recordDeath(xmlSingleTurn.getRoundNumber(), xmlSingleEvent.getDeadUnitID(), xmlSingleEvent.getController());
			}
		}
		for (XmlDamageLogList xmlSingleTurn : xml.getDamageLogList())
		{
			for (XmlDamageLog xmlSingleEvent : xmlSingleTurn.getDamageLog()) {
				e.recordDamage(xmlSingleTurn.getRoundNumber(), xmlSingleEvent.getAttackerID(), xmlSingleEvent.getAttackerController(), xmlSingleEvent.getDefenderID(), xmlSingleEvent.getDefenderController(), xmlSingleEvent.getDamage());
			}
		}
		for (XmlResourceDropoffLogList xmlSingleTurn : xml.getResourceDropoffLogList())
		{
			for (XmlResourceDropoffLog xmlSingleEvent : xmlSingleTurn.getResourceDropoffLog()) {
				e.recordResourceDropoff(xmlSingleTurn.getRoundNumber(), xmlSingleEvent.getGathererID(), xmlSingleEvent.getDepotID(), xmlSingleEvent.getController() , ResourceAdapter.fromXml(xmlSingleEvent.getResourceType()), xmlSingleEvent.getDepositAmount());
			}
		}
		for (XmlResourcePickupLogList xmlSingleTurn : xml.getResourcePickupLogList())
		{
			for (XmlResourcePickupLog xmlSingleEvent : xmlSingleTurn.getResourcePickupLog()) {
				e.recordResourcePickup(xmlSingleTurn.getRoundNumber(), xmlSingleEvent.getGathererID(), xmlSingleEvent.getController(), ResourceAdapter.fromXml(xmlSingleEvent.getResourceType()), xmlSingleEvent.getPickupAmount(), xmlSingleEvent.getNodeID(), ResourceAdapter.fromXml(xmlSingleEvent.getNodeType()));
			}
		}
		for (XmlResourceNodeExhaustionLogList xmlSingleTurn : xml.getResourceNodeExhaustionLogList())
		{
			for (XmlResourceNodeExhaustionLog xmlSingleEvent : xmlSingleTurn.getResourceNodeExhaustionLog()) {
				e.recordResourceNodeExhaustion(xmlSingleTurn.getRoundNumber(), xmlSingleEvent.getExhaustedNodeID(), ResourceAdapter.fromXml(xmlSingleEvent.getExhaustedNodeType()));
			}
		}
		for (XmlRevealedResourceNodeLog xmlSingleEvent : xml.getRevealedResourceNodeLog()) {
			e.recordRevealedResourceNode(xmlSingleEvent.getXPosition(), xmlSingleEvent.getYPosition(), ResourceAdapter.fromXml(xmlSingleEvent.getNodeType()));
		}
		for (XmlUpgradeLogList xmlSingleTurn : xml.getUpgradeLogList())
		{
			for (XmlUpgradeLog xmlSingleEvent : xmlSingleTurn.getUpgradeLog()) {
				e.recordUpgrade(xmlSingleTurn.getRoundNumber(), xmlSingleEvent.getUpgradeTemplateID(), xmlSingleEvent.getProducingUnitID(), xmlSingleEvent.getController());
			}
		}
		return e;
		
	}
}
