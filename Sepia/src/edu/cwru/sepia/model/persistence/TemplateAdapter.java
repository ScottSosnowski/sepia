package edu.cwru.sepia.model.persistence;

import edu.cwru.sepia.model.persistence.generated.XmlTemplate;
import edu.cwru.sepia.model.persistence.generated.XmlTerrainDuration;
import edu.cwru.sepia.model.persistence.generated.XmlUnitTemplate;
import edu.cwru.sepia.model.persistence.generated.XmlUpgradeTemplate;
import edu.cwru.sepia.model.state.ResourceNode.Type;
import edu.cwru.sepia.model.state.Template;
import edu.cwru.sepia.model.state.Tile.TerrainType;
import edu.cwru.sepia.model.state.UnitTemplate;
import edu.cwru.sepia.model.state.UpgradeTemplate;

public class TemplateAdapter {

	public static Template<?> fromXml(XmlTemplate xml, int player) {
		if (xml instanceof XmlUnitTemplate)
			return fromXml((XmlUnitTemplate)xml, player);
		else if (xml instanceof XmlUpgradeTemplate)
			return fromXml((XmlUpgradeTemplate)xml, player);
		else
			return null;
	}
	
	public static UnitTemplate fromXml(XmlUnitTemplate xml, int player) {
		UnitTemplate ut = new UnitTemplate(xml.getID());
		ut.setArmor(xml.getArmor());
		ut.setBasicAttack(xml.getBaseAttack());
		ut.setBaseHealth(xml.getBaseHealth());
		ut.setCharacter((char)xml.getCharacter());
		ut.setFoodCost(xml.getFoodCost());
		ut.setFoodProvided(xml.getFoodProvided());
		ut.setGoldCost(xml.getGoldCost());
		ut.setGoldGatherRate(xml.getGoldGatherRate());
		ut.setName(xml.getName());
		ut.setPiercingAttack(xml.getPiercingAttack());
		for (Integer i : xml.getProduces())
			ut.addProductionItem(i);
		ut.setRange(xml.getRange());
		ut.setSightRange(xml.getSightRange());
		ut.setTimeCost(xml.getTimeCost());
		ut.setWoodCost(xml.getWoodCost());
		ut.setWoodGatherRate(xml.getWoodGatherRate());
		ut.setCanAcceptGold(xml.isCanAcceptGold());
		ut.setCanAcceptWood(xml.isCanAcceptWood());
		ut.setCanBuild(xml.isCanBuild());
		ut.setCanGather(xml.isCanGather());
		ut.setCanMove(xml.isCanMove());
		ut.setDurationAttack(xml.getDurationAttack());
		ut.setDurationDeposit(xml.getDurationDeposit());
		for (TerrainType terrainType : TerrainType.values()) {
			ut.setDurationMove(UnitTemplate.NO_DURATION, terrainType);
		}
		for (XmlTerrainDuration durationMoveXml : xml.getDurationMove()) {
			for (String terrainString : durationMoveXml.getTerrain()) {
				TerrainType terrainType = TerrainType.valueOf(terrainString);
				ut.setDurationMove(durationMoveXml.getDuration(), terrainType);
			}
		}
		ut.setDurationGatherGold(xml.getDurationGatherGold());
		ut.setDurationGatherWood(xml.getDurationGatherWood());
		ut.setPlayer(player);
		for (Integer i : xml.getUnitPrerequisite())
			ut.addBuildPrerequisite(i);
		for (Integer i : xml.getUpgradePrerequisite())
			ut.addUpgradePrerequisite(i);
		
		
		
		return ut;
	}
	
	public static UpgradeTemplate fromXml(XmlUpgradeTemplate xml,int player) {
		UpgradeTemplate ut = new UpgradeTemplate(xml.getID());
		ut.setFoodCost(xml.getFoodCost());//if(obj.has("FoodCost"))
		ut.setGoldCost(xml.getGoldCost());//template.setGoldCost(obj.getInt("GoldCost"));
		ut.setName(xml.getName());//template.setName(obj.getString("Name"));

		ut.setTimeCost(xml.getTimeCost());//template.setTimeCost(obj.getInt("TimeCost"));
		ut.setWoodCost(xml.getWoodCost());//template.setWoodCost(obj.getInt("WoodCost"));
		ut.setPlayer(player);
		for (Integer i : xml.getUnitPrerequisite())//if(obj.has("BuildPrereq"))
			ut.addBuildPrerequisite(i);
		for (Integer i : xml.getUpgradePrerequisite())//if(obj.has("UpgradePrereq"))
			ut.addUpgradePrerequisite(i);
		
		for (Integer i : xml.getAffectedUnitTypes())//if(obj.has("Produces"))
			ut.addAffectedUnit(i);
		
		ut.setPiercingAttackChange(xml.getPiercingAttackChange());
		ut.setBasicAttackChange(xml.getBasicAttackChange());
		ut.setArmorChange(xml.getArmorChange());
		ut.setHealthChange(xml.getHealthChange());
		ut.setRangeChange(xml.getRangeChange());
		ut.setSightRangeChange(xml.getSightRangeChange());
		return ut;
	}

	public static XmlTemplate toXml(Template<?> template) {
		if(template instanceof UnitTemplate)
			return toXml((UnitTemplate)template);
		else if(template instanceof UpgradeTemplate)
			return toXml((UpgradeTemplate)template);
		return null;
	}
	
	public static XmlUnitTemplate toXml(UnitTemplate ut) {
		XmlUnitTemplate xml = new XmlUnitTemplate();
		xml.setFoodCost(ut.getFoodCost());
		xml.setID(ut.getID());
		xml.setName(ut.getName());
		xml.setTimeCost(ut.getTimeCost());
		xml.setWoodCost(ut.getWoodCost());
		xml.setGoldCost(ut.getGoldCost());
		
		for (Integer i : ut.getBuildPrerequisites())
			xml.getUnitPrerequisite().add(i);
		for (Integer i : ut.getUpgradePrerequisites())
			xml.getUpgradePrerequisite().add(i);
		
		xml.setArmor(ut.getArmor());
		xml.setBaseAttack(ut.getBasicAttack());
		xml.setBaseHealth(ut.getBaseHealth());
		xml.setCanAcceptGold(ut.canAcceptGold());
		xml.setCanAcceptWood(ut.canAcceptWood());
		xml.setCanBuild(ut.canBuild());
		xml.setCanGather(ut.canGather());
		xml.setCanMove(ut.canMove());
		xml.setCharacter((short)ut.getCharacter());
		xml.setFoodProvided(ut.getFoodProvided());
		xml.setGoldGatherRate(ut.getGatherRate(Type.GOLD_MINE));
		xml.setPiercingAttack(ut.getPiercingAttack());
		xml.setRange(ut.getRange());
		xml.setSightRange(ut.getSightRange());
		
		xml.setWoodGatherRate(ut.getGatherRate(Type.TREE));
		xml.setDurationAttack(ut.getDurationAttack());
		xml.setDurationDeposit(ut.getDurationDeposit());
		for (TerrainType terrainType : TerrainType.values()) {
			XmlTerrainDuration xmlTerrainDuration = new XmlTerrainDuration();
			xmlTerrainDuration.getTerrain().add(terrainType.toString());
			xmlTerrainDuration.setDuration(ut.getDurationMove(terrainType));
			xml.getDurationMove().add(xmlTerrainDuration);
		}
		xml.setDurationGatherGold(ut.getDurationGatherGold());
		xml.setDurationGatherWood(ut.getDurationGatherWood());
		for (Integer i : ut.getProduces())
			xml.getProduces().add(i);
		
		return xml;
	}

	public static XmlUpgradeTemplate toXml(UpgradeTemplate ut) {
		XmlUpgradeTemplate xml = new XmlUpgradeTemplate();
		xml.setFoodCost(ut.getFoodCost());
		xml.setID(ut.getID());
		xml.setName(ut.getName());
		xml.setTimeCost(ut.getTimeCost());
		xml.setWoodCost(ut.getWoodCost());
		xml.setGoldCost(ut.getGoldCost());
		xml.setPiercingAttackChange(ut.getPiercingAttackChange());
		xml.setBasicAttackChange(ut.getBasicAttackChange());
		xml.setArmorChange(ut.getArmorChange());
		xml.setHealthChange(ut.getHealthChange());
		xml.setRangeChange(ut.getRangeChange());
		xml.setSightRangeChange(ut.getSightRangeChange());
		for (Integer i :ut.getBuildPrerequisites())
			xml.getUnitPrerequisite().add(i);
		for (Integer i:ut.getUpgradePrerequisites())
			xml.getUpgradePrerequisite().add(i);
		
		for (Integer affected :ut.getAffectedUnits())
			xml.getAffectedUnitTypes().add(affected);
		
		return xml;
	}
}
