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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.util.Random;

import javax.xml.bind.JAXBException;

import org.junit.Test;

import edu.cwru.sepia.model.persistence.PlayerAdapter;
import edu.cwru.sepia.model.persistence.ResourceAdapter;
import edu.cwru.sepia.model.persistence.generated.XmlPlayer;
import edu.cwru.sepia.model.persistence.generated.XmlResourceQuantity;
import edu.cwru.sepia.model.persistence.generated.XmlTemplate;
import edu.cwru.sepia.model.persistence.generated.XmlTerrainDuration;
import edu.cwru.sepia.model.persistence.generated.XmlUnit;
import edu.cwru.sepia.model.persistence.generated.XmlUnitTemplate;
import edu.cwru.sepia.model.persistence.generated.XmlUpgradeTemplate;
import edu.cwru.sepia.model.state.PlayerState;
import edu.cwru.sepia.model.state.Template;
import edu.cwru.sepia.model.state.Tile.TerrainType;
import edu.cwru.sepia.model.state.Unit;
import edu.cwru.sepia.model.state.UnitTemplate;
import edu.cwru.sepia.model.state.UpgradeTemplate;

public class PlayerAdapterTest {

	@Test
	public void textFromXml() throws FileNotFoundException, JAXBException {
		XmlPlayer xml = AdapterTestUtil.createExamplePlayer(new Random(053));
		PlayerAdapter adapter = new PlayerAdapter();

		PlayerState player = adapter.fromXml(xml);
		checkEquality(xml, player);
	}

	/**
	 * Starts with a randomly generated xmlplayer, then goes to normal (which is
	 * assumed to work) Then it goes to and from again, and compares the first
	 * normal to the second using deepequals.
	 */
	@Test
	public void testToXmlAssumingFromWorks() {
		XmlPlayer xml = AdapterTestUtil.createExamplePlayer(new Random(053));
		PlayerAdapter adapter = new PlayerAdapter();

		PlayerState playerFirstTry = adapter.fromXml(xml);
		PlayerState playerSecondTry = adapter.fromXml(adapter.toXml(playerFirstTry));
		assertEquals(
				"The once and thrice converted don't match, therefore either toXml or fromXml is not working (or the equals, but hopefully not that)",
				playerFirstTry, playerSecondTry);
	}

	public void checkEquality(XmlPlayer xml, PlayerState player) {
		assertEquals("playernum did not match!", xml.getID(), player.playerNum);
		for(XmlResourceQuantity amount : xml.getResourceAmount()) {
			assertEquals(amount.getType() + " amount did not match!", amount.getQuantity(),
					player.getCurrentResourceAmount(ResourceAdapter.fromXml(amount.getType())));
		}
		assertEquals("supply did not match!", xml.getSupply(), player.getCurrentSupply());
		assertEquals("supply cap did not match!", xml.getSupplyCap(), player.getCurrentSupplyCap());
		assertEquals("different number of upgrades", player.getUpgrades().size(), xml.getUpgrade().size());
		for(Integer i : xml.getUpgrade()) {
			assertTrue("upgrade " + i + " from xml wasn't in player!", player.getUpgrades().contains(i));
		}
		for(Integer i : player.getUpgrades()) {
			assertTrue("upgrade " + i + " in player wasn't in xml!", xml.getUpgrade().contains(i));
		}
		assertEquals("Number of units did not match", xml.getUnit().size(), player.getUnits().values().size());
		for(XmlUnit xmlunit : xml.getUnit()) {
			Unit u = player.getUnit(xmlunit.getID());
			assertNotNull("Player did not contain unit with the right id", u);
			UnitAdapterTest.checkEquality(xmlunit, u);
		}
		for(XmlTemplate t : xml.getTemplate()) {
			checkEquality(t, xml.getID(), player.getTemplate(t.getID()));
		}
	}

	public static void checkEquality(XmlTemplate xt, int playernum, Template<?> t) {
		assertNotNull("xml one was null ", xt);
		assertNotNull("real one was null ", t);
		assertTrue("Weren't same type of template", xt instanceof XmlUnitTemplate && t instanceof UnitTemplate
				|| xt instanceof XmlUpgradeTemplate && t instanceof UpgradeTemplate);
		assertEquals(t.getID(), xt.getID());
		assertEquals(t.getFoodCost(), xt.getFoodCost());
		assertEquals(t.getGoldCost(), xt.getGoldCost());
		assertEquals(t.getName(), xt.getName());
		assertEquals(t.getPlayer(), playernum);
		assertEquals(t.getTimeCost(), xt.getTimeCost());
		assertEquals(t.getBuildPrerequisites().size(), xt.getUnitPrerequisite().size());
		for(String s : xt.getUnitPrerequisite()) {
			assertTrue(t.getBuildPrerequisites().contains(s));
		}
		assertEquals(t.getUpgradePrerequisites().size(), xt.getUpgradePrerequisite().size());
		for(String s : xt.getUpgradePrerequisite()) {
			assertTrue(t.getUpgradePrerequisites().contains(s));
		}
		assertEquals(t.getWoodCost(), xt.getWoodCost());
		if(t instanceof UpgradeTemplate) {
			UpgradeTemplate ut = (UpgradeTemplate)t;
			XmlUpgradeTemplate uxt = (XmlUpgradeTemplate)xt;
			assertEquals(ut.getPiercingAttackChange(), uxt.getPiercingAttackChange());
			assertEquals(ut.getBasicAttackChange(), uxt.getBasicAttackChange());
			assertEquals(ut.getArmorChange(), uxt.getArmorChange());
			assertEquals(ut.getHealthChange(), uxt.getHealthChange());
			assertEquals(ut.getRangeChange(), uxt.getRangeChange());
			assertEquals(ut.getSightRangeChange(), uxt.getSightRangeChange());

			assertEquals(ut.getAffectedUnits().size(), uxt.getAffectedUnitTypes().size());
			for(String affected : ut.getAffectedUnits()) {
				assertTrue("xml:" + uxt.getAffectedUnitTypes() + " normal:" + ut.getAffectedUnits(), uxt
						.getAffectedUnitTypes().contains(affected));
			}
		}
		if(t instanceof UnitTemplate) {
			UnitTemplate ut = (UnitTemplate)t;
			XmlUnitTemplate uxt = (XmlUnitTemplate)xt;
			assertEquals(ut.canBuild(), uxt.isCanBuild());
			assertEquals(ut.canGather(), uxt.isCanGather());
			assertEquals(ut.canMove(), uxt.isCanMove());
			assertEquals(ut.getArmor(), uxt.getArmor());
			assertEquals(ut.getBaseHealth(), uxt.getBaseHealth());
			assertEquals(ut.getBasicAttack(), uxt.getBaseAttack());
			assertEquals(ut.getCharacter(), uxt.getCharacter());
			assertEquals(ut.getFoodProvided(), uxt.getFoodProvided());
			assertEquals(ut.getPiercingAttack(), uxt.getPiercingAttack());
			assertEquals(ut.getDurationAttack(), uxt.getDurationAttack());
			assertEquals(ut.getDurationDeposit(), uxt.getDurationDeposit());
			for (TerrainType terrainType : TerrainType.values()) {
				int nonXmlDuration = ut.getDurationMove(terrainType);
				int timesFound = 0;
				int xmlDuration = -1;
				for (XmlTerrainDuration xmlTerrainSpeed : uxt.getDurationMove()) {
					if (xmlTerrainSpeed.getTerrain().contains(terrainType.toString())) {
						timesFound++;
						xmlDuration = xmlTerrainSpeed.getDuration();
					}
				}
				assertEquals("Found the wrong number of durations for the terrain type "+terrainType, 1, timesFound);
				assertEquals(nonXmlDuration, xmlDuration);
			}
			assertEquals(ut.getProduces().size(), uxt.getProduces().size());
			for(String s : uxt.getProduces()) {
				assertTrue(ut.getProduces().contains(s));
			}
			assertEquals(ut.getRange(), uxt.getRange());
			assertEquals(ut.getSightRange(), uxt.getSightRange());
		}

	}
}
