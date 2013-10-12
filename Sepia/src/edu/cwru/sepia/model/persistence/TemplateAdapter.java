package edu.cwru.sepia.model.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.cwru.sepia.model.persistence.generated.XmlResourceParameters;
import edu.cwru.sepia.model.persistence.generated.XmlTemplate;
import edu.cwru.sepia.model.persistence.generated.XmlUnitTemplate;
import edu.cwru.sepia.model.persistence.generated.XmlUpgradeTemplate;
import edu.cwru.sepia.model.state.ResourceType;
import edu.cwru.sepia.model.state.Template;
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
		ut.setName(xml.getName());
		ut.setPiercingAttack(xml.getPiercingAttack());
		for (String s : xml.getProduces())
			ut.addProductionItem(s);
		ut.setRange(xml.getRange());
		ut.setSightRange(xml.getSightRange());
		ut.setTimeCost(xml.getTimeCost());
		ut.setWoodCost(xml.getWoodCost());
		ut.setCanBuild(xml.isCanBuild());
		ut.setCanGather(xml.isCanGather());
		ut.setCanMove(xml.isCanMove());
		ut.setDurationAttack(xml.getDurationAttack());
		ut.setDurationDeposit(xml.getDurationDeposit());
		ut.setDurationMove(xml.getDurationMove());
		ut.setPlayer(player);
		for (String s : xml.getUnitPrerequisite())
			ut.addBuildPrerequisite(s);
		for (String s : xml.getUpgradePrerequisite())
			ut.addUpgradePrerequisite(s);
		
		List<ResourceType> accepts = new ArrayList<ResourceType>(xml.getAccepts().size());
		for(String resourceName : xml.getAccepts()) {
			accepts.add(new ResourceType(resourceName));
		}
		ut.setAccepts(accepts);
		
		Map<ResourceType, Integer> capacity = new HashMap<ResourceType, Integer>();
		Map<ResourceType, Integer> gatherRate = new HashMap<ResourceType, Integer>();
		Map<ResourceType, Integer> gatherDuration = new HashMap<ResourceType, Integer>();
		for(XmlResourceParameters params : xml.getResourceParameters()) {
			ResourceType type = new ResourceType(params.getResourceType());
			capacity.put(type, params.getCapacity());
			gatherRate.put(type, params.getGatherRate());
			gatherDuration.put(type, params.getGatherDuration());
		}
		ut.setCapacity(capacity);
		ut.setGatherDuration(gatherDuration);
		ut.setGatherRate(gatherRate);

		int width = xml.getWidth();
		if(width == 0)
			width = 1;
		ut.setWidth(width);
		int height = xml.getHeight();
		if(height == 0)
			height = 1;
		ut.setHeight(height);
		
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
		for (String s : xml.getUnitPrerequisite())//if(obj.has("BuildPrereq"))
			ut.addBuildPrerequisite(s);
		for (String s : xml.getUpgradePrerequisite())//if(obj.has("UpgradePrereq"))
			ut.addUpgradePrerequisite(s);
		
		for (String s : xml.getAffectedUnitTypes())//if(obj.has("Produces"))
			ut.addAffectedUnit(s);
		
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
		
		for (String s : ut.getBuildPrerequisites())
			xml.getUnitPrerequisite().add(s);
		for (String s : ut.getUpgradePrerequisites())
			xml.getUpgradePrerequisite().add(s);
		
		xml.setArmor(ut.getArmor());
		xml.setBaseAttack(ut.getBasicAttack());
		xml.setBaseHealth(ut.getBaseHealth());
		xml.setCanBuild(ut.canBuild());
		xml.setCanGather(ut.canGather());
		xml.setCanMove(ut.canMove());
		xml.setCharacter((short)ut.getCharacter());
		xml.setFoodProvided(ut.getFoodProvided());
		xml.setPiercingAttack(ut.getPiercingAttack());
		xml.setRange(ut.getRange());
		xml.setSightRange(ut.getSightRange());
		xml.setDurationAttack(ut.getDurationAttack());
		xml.setDurationDeposit(ut.getDurationDeposit());
		xml.setDurationMove(ut.getDurationMove());
		for (String s : ut.getProduces())
			xml.getProduces().add(s);
		
		for(ResourceType resource : ut.getAccepts()) {
			xml.getAccepts().add(resource.getName());
		}
		
		for(ResourceType resource : ut.getGatherableResources()) {
			XmlResourceParameters params = new XmlResourceParameters();
			params.setResourceType(resource.getName());
			params.setCapacity(ut.getCapacity(resource));
			params.setGatherRate(ut.getGatherRate(resource));
			params.setGatherDuration(ut.getGatherDuration(resource));
			xml.getResourceParameters().add(params);
		}
		
		xml.setWidth(ut.getWidth());
		xml.setHeight(ut.getHeight());
		
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
		for (String s : ut.getBuildPrerequisites())
			xml.getUnitPrerequisite().add(s);
		for(String s : ut.getUpgradePrerequisites())
			xml.getUpgradePrerequisite().add(s);
		
		for (String affected : ut.getAffectedUnits())
			xml.getAffectedUnitTypes().add(affected);
		
		return xml;
	}
}
