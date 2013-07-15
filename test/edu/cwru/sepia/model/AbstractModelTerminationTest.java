package edu.cwru.sepia.model;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import edu.cwru.sepia.model.state.PlayerState;
import edu.cwru.sepia.model.state.ResourceType;
import edu.cwru.sepia.model.state.State;
import edu.cwru.sepia.model.state.State.StateBuilder;
import edu.cwru.sepia.model.state.Unit;
import edu.cwru.sepia.model.state.UnitTemplate;
import edu.cwru.sepia.util.ConfigurationAccessors;

public class AbstractModelTerminationTest extends AbstractModelTestBase {

	
	@Test
	public void testConquestVictoryComplete() {
		StateBuilder builder = singleUnitSetup();
		PlayerState ps1 = new PlayerState(1);
		builder.addPlayer(ps1);
		State state = builder.build();
		model = getModel(state);
		
		assertEquals(0, model.conquestTerminated());

		configuration.addProperty(ConfigurationAccessors.CONQUEST, true);
		assertEquals(true, model.isTerminated());
	}

	@Test
	public void testConquestVictoryNotComplete() {
		StateBuilder builder = singleUnitSetup();
		UnitTemplate template = (UnitTemplate)builder.getTemplate(0, "t0");
		PlayerState ps1 = new PlayerState(1);
		builder.addPlayer(ps1);
		ps1.addTemplate(template);
		Unit u1 = new Unit(template, 1);
		ps1.addUnit(u1);
		State state = builder.build();
		model = getModel(state);
		
		assertEquals(-1, model.conquestTerminated());

		assertEquals(false, model.isTerminated());
		configuration.addProperty(ConfigurationAccessors.CONQUEST, true);
		assertEquals(false, model.isTerminated());
	}
	
	@Test
	public void testConquestVictoryCompleteManyPlayers() {
		StateBuilder builder = singleUnitSetup();
		PlayerState ps1 = new PlayerState(1);
		builder.addPlayer(ps1);
		PlayerState ps2 = new PlayerState(2);
		builder.addPlayer(ps2);
		State state = builder.build();
		model = getModel(state);
		
		assertEquals(0, model.conquestTerminated());

		assertEquals(true, model.isTerminated());
		configuration.addProperty(ConfigurationAccessors.CONQUEST, true);
		assertEquals(true, model.isTerminated());
	}
	
	@Test
	public void testConquestVictoryNotCompleteManyPlayers() {
		StateBuilder builder = singleUnitSetup();
		UnitTemplate template = (UnitTemplate)builder.getTemplate(0, "t0");
		PlayerState ps1 = new PlayerState(1);
		builder.addPlayer(ps1);
		ps1.addTemplate(template);
		Unit u1 = new Unit(template, 1);
		ps1.addUnit(u1);
		PlayerState ps2 = new PlayerState(2);
		builder.addPlayer(ps2);
		State state = builder.build();
		model = getModel(state);
		
		assertEquals(-1, model.conquestTerminated());

		assertEquals(false, model.isTerminated());
		configuration.addProperty(ConfigurationAccessors.CONQUEST, true);
		assertEquals(false, model.isTerminated());	
	}
	
	@Test
	public void testMidasVictory() {
		StateBuilder builder = new StateBuilder();
		PlayerState ps0 = new PlayerState(0);
		builder.addPlayer(ps0);
		ps0.setCurrentResourceAmount(ResourceType.GOLD, 1000);
		ps0.setCurrentResourceAmount(ResourceType.WOOD, 1000);
		State state = builder.build();
		model = getModel(state);
		
		Set<Integer> p0s = Collections.singleton(0);
		assertEquals(p0s, model.resourceGatheringTerminated());

		configuration.addProperty(ConfigurationAccessors.CONQUEST, false);
		configuration.addProperty(ConfigurationAccessors.MIDAS, true);
		assertEquals(true, model.isTerminated());

		configuration.addProperty(ConfigurationAccessors.REQUIRED_GOLD, 100);
		configuration.addProperty(ConfigurationAccessors.REQUIRED_WOOD, 100);
		assertEquals(p0s, model.resourceGatheringTerminated());
		assertEquals(true, model.isTerminated());


		configuration.setProperty(ConfigurationAccessors.REQUIRED_GOLD, 1000);
		configuration.setProperty(ConfigurationAccessors.REQUIRED_WOOD, 1000);
		assertEquals(p0s, model.resourceGatheringTerminated());
		assertEquals(true, model.isTerminated());

		configuration.setProperty(ConfigurationAccessors.REQUIRED_GOLD, 1000);
		configuration.setProperty(ConfigurationAccessors.REQUIRED_WOOD, 100);
		assertEquals(p0s, model.resourceGatheringTerminated());
		assertEquals(true, model.isTerminated());

		configuration.setProperty(ConfigurationAccessors.REQUIRED_GOLD, 0);
		configuration.setProperty(ConfigurationAccessors.REQUIRED_WOOD, 1000);
		assertEquals(p0s, model.resourceGatheringTerminated());
		assertEquals(true, model.isTerminated());

		configuration.setProperty(ConfigurationAccessors.REQUIRED_GOLD, 1010);
		configuration.setProperty(ConfigurationAccessors.REQUIRED_WOOD, 100);
		assertEquals(Collections.emptySet(), model.resourceGatheringTerminated());
		assertEquals(false, model.isTerminated());

		configuration.setProperty(ConfigurationAccessors.REQUIRED_GOLD, 100);
		configuration.setProperty(ConfigurationAccessors.REQUIRED_WOOD, 1010);
		assertEquals(Collections.emptySet(), model.resourceGatheringTerminated());
		assertEquals(false, model.isTerminated());

		configuration.setProperty(ConfigurationAccessors.REQUIRED_GOLD, 10000);
		configuration.setProperty(ConfigurationAccessors.REQUIRED_WOOD, 1050);
		assertEquals(Collections.emptySet(), model.resourceGatheringTerminated());
		assertEquals(false, model.isTerminated());
	}
	
	@Test
	public void testMidasAnyPlayer() {
		StateBuilder builder = new StateBuilder();
		PlayerState ps0 = new PlayerState(0);
		builder.addPlayer(ps0);
		UnitTemplate template = new UnitTemplate(0);
		template.setBaseHealth(10);
		Unit u0 = new Unit(template, 0);
		ps0.addTemplate(template);
		ps0.addUnit(u0);
		ps0.setCurrentResourceAmount(ResourceType.GOLD, 100);
		ps0.setCurrentResourceAmount(ResourceType.WOOD, 100);
		PlayerState ps1 = new PlayerState(1);
		builder.addPlayer(ps1);
		State state = builder.build();
		model = getModel(state);
		
		configuration.addProperty(ConfigurationAccessors.CONQUEST, false);
		configuration.addProperty(ConfigurationAccessors.MIDAS, true);
		assertEquals(true, model.isTerminated());

		Set<Integer> winners = new HashSet<Integer>();
		winners.add(0);
		configuration.addProperty(ConfigurationAccessors.REQUIRED_GOLD, 100);
		configuration.addProperty(ConfigurationAccessors.REQUIRED_WOOD, 100);
		assertEquals(winners, model.resourceGatheringTerminated());
		assertEquals(true, model.isTerminated());

		winners.add(1);
		ps1.setCurrentResourceAmount(ResourceType.GOLD, 1000);
		ps1.setCurrentResourceAmount(ResourceType.WOOD, 1000);
		assertEquals(winners, model.resourceGatheringTerminated());
		assertEquals(true, model.isTerminated());
		
		winners.remove(0);
		ps0.setCurrentResourceAmount(ResourceType.GOLD, 50);
		ps0.setCurrentResourceAmount(ResourceType.WOOD, 10);
		assertEquals(winners, model.resourceGatheringTerminated());
		assertEquals(true, model.isTerminated());

		winners.remove(1);
		ps1.setCurrentResourceAmount(ResourceType.GOLD, 10);
		ps1.setCurrentResourceAmount(ResourceType.WOOD, 75);
		assertEquals(winners, model.resourceGatheringTerminated());
		assertEquals(false, model.isTerminated());
	}
	
	
	//TODO - test manifest destiny
	
	@Test
	public void testConquestAndMidas() {
		StateBuilder builder = new StateBuilder();
		PlayerState ps0 = new PlayerState(0);
		builder.addPlayer(ps0);
		UnitTemplate template = new UnitTemplate(0);
		template.setBaseHealth(10);
		Unit u0 = new Unit(template, 0);
		ps0.addTemplate(template);
		ps0.addUnit(u0);
		ps0.setCurrentResourceAmount(ResourceType.GOLD, 100);
		ps0.setCurrentResourceAmount(ResourceType.WOOD, 100);
		PlayerState ps1 = new PlayerState(1);
		builder.addPlayer(ps1);
		State state = builder.build();
		model = getModel(state);
		

		configuration.addProperty(ConfigurationAccessors.CONQUEST, true);
		configuration.addProperty(ConfigurationAccessors.MIDAS, true);
		configuration.addProperty(ConfigurationAccessors.REQUIRED_GOLD, 100);
		configuration.addProperty(ConfigurationAccessors.REQUIRED_WOOD, 100);
		assertEquals(true, model.isTerminated());//p0 conquest and midas
		
		configuration.setProperty(ConfigurationAccessors.REQUIRED_GOLD, 1000);
		assertEquals(false, model.isTerminated());//p0 conquest, not midas
		
		Unit u1 = new Unit(template, 1);
		ps1.addUnit(u1);
		assertEquals(false, model.isTerminated());//none
		
		configuration.setProperty(ConfigurationAccessors.REQUIRED_GOLD, 100);
		assertEquals(false, model.isTerminated());//p0 midas, not conquest
		
		u0.setHP(0);
		assertEquals(false, model.isTerminated());//p0 midas, p1 conquest
		
		ps0.getUnits().remove(u1);
		assertEquals(false, model.isTerminated());//p0 midas, p1 conquest
		
		ps1.setCurrentResourceAmount(ResourceType.GOLD, 100);
		ps1.setCurrentResourceAmount(ResourceType.WOOD, 100);
		assertEquals(true, model.isTerminated());//p0 & p1 midas, p1 conquest
	}
	
}
