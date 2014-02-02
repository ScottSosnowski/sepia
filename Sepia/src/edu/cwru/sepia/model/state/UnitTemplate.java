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
package edu.cwru.sepia.model.state;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import edu.cwru.sepia.model.state.Tile.TerrainType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains information shared between units of the same type.
 * 
 * @author Tim
 * 
 */

public class UnitTemplate extends Template<Unit> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Marks that there is no duration
	 */
	public static final int NO_DURATION = -1;
	
	protected int baseHealth;
	protected int basicAttack;
	protected int piercingAttack;
	protected int range;
	protected int armor;
	protected int sightRange;
	protected boolean canGather;
	protected boolean canBuild;
	protected boolean canMove;
	protected int foodProvided;
	protected char character;
	protected Map<TerrainType, Integer> durationMove;
	protected int durationAttack;
	protected int durationDeposit;

	private UnitTemplateView view;
	private List<String> producesNames;
	protected List<ResourceType> accepts = new ArrayList<ResourceType>();
	protected Map<ResourceType, Integer> capacity = new HashMap<ResourceType, Integer>();
	protected Map<ResourceType, Integer> gatherRate = new HashMap<ResourceType, Integer>();
	protected Map<ResourceType, Integer> gatherDuration = new HashMap<ResourceType, Integer>();
	protected int width = 1;
	protected int height = 1;

	public UnitTemplate(int ID) {
		super(ID);
		producesNames = new ArrayList<String>();
		durationMove = new EnumMap<TerrainType, Integer>(TerrainType.class);
	}

	/**
	 * Create a cloned unit template out of a view
	 */
	public UnitTemplate(UnitTemplateView view) {
		super(view);
		setBaseHealth(view.baseHealth);
		setBasicAttack(view.basicAttack);
		setPiercingAttack(view.piercingAttack);
		setRange(view.range);
		setArmor(view.armor);
		setSightRange(view.sightRange);
		setCanGather(view.canGather);
		setCanBuild(view.canBuild);
		setCanMove(view.canMove);
		setAccepts(view.accepts);
		setFoodProvided(view.foodProvided);
		setCharacter(view.character);
		setCapacity(view.capacity);
		setGatherRate(view.gatherRate);
		setGatherDuration(view.gatherDuration);
		durationMove = new EnumMap<TerrainType, Integer>(view.durationMove);
		setDurationAttack(view.durationAttack);
		setDurationDeposit(view.durationDeposit);
		for(String produced : view.producesNames)
			addProductionItem(produced);
	}

	@Override
	public Unit produceInstance(IdDistributor idsource) {
		Unit unit = new Unit(this, idsource.nextTargetId());
		return unit;
	}

	public int getBaseHealth() {
		return baseHealth;
	}

	public void setBaseHealth(int baseHealth) {
		this.baseHealth = baseHealth;
    }

	public int getWidth() {
		return width;
	}

	public char getCharacter() {
		return character;
    }

	public void setWidth(int width) {
		this.width = width;
	}

	public void setCharacter(char character) {
		this.character = character;
    }

    public int getHeight() {
		return height;
	}

	public int getBasicAttack() {
		return basicAttack;
    }

    public void setHeight(int height) {
		this.height = height;
	}

	public void setBasicAttack(int basicAttack) {
		this.basicAttack = basicAttack;
	}

	public int getPiercingAttack() {
		return piercingAttack;
	}

	public void setPiercingAttack(int piercingAttack) {
		this.piercingAttack = piercingAttack;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public int getArmor() {
		return armor;
	}

	public void setArmor(int armor) {
		this.armor = armor;
	}

	public int getSightRange() {
		return sightRange;
	}

	public void setSightRange(int sightRange) {
		this.sightRange = sightRange;
	}

	public boolean canAttack() {
		return basicAttack > 0 || piercingAttack > 0;
	}
	
	public List<ResourceType> getAccepts() {
		return Collections.unmodifiableList(accepts);
	}

	public boolean canAccept(ResourceType resource) {
		return accepts.contains(resource);
	}

	public void setAccepts(List<ResourceType> accepts) {
		this.accepts = accepts;
	}

	public int getFoodProvided() {
		return foodProvided;
	}

	public void setFoodProvided(int numFoodProvided) {
		this.foodProvided = numFoodProvided;
	}

	public Collection<ResourceType> getGatherableResources() {
		return gatherRate.keySet();
	}

	public void setGatherRate(Map<ResourceType, Integer> gatherRate) {
		this.gatherRate = gatherRate;
	}

	public void setGatherDuration(Map<ResourceType, Integer> gatherDuration) {
		this.gatherDuration = gatherDuration;
	}

	public void setCapacity(Map<ResourceType, Integer> capacity) {
		this.capacity = capacity;
	}

	public int getGatherRate(ResourceType type) {
		Integer value = gatherRate.get(type);
		if(value != null)
			return value;
		else
			return 0;
	}

	public int getGatherDuration(ResourceType type) {
		Integer value = gatherDuration.get(type);
		if(value != null)
			return value;
		else
			return 0;
	}

	public int getCapacity(ResourceType type) {
		Integer value = capacity.get(type);
		if(value != null)
			return value;
		else
			return 0;
	}

	/**
	 * Return the amount of resource that you can gather from each node type.
	 * 
	 * @param type
	 *            The type of resource node to gather from.
	 * @return
	 */
	public int getCarryingCapacity(ResourceNodeType type) {
		Integer value = capacity.get(type.getResource());
		if(value != null)
			return value;
		else
			return 0;
	}

	public boolean canGather() {
		return canGather;
	}

	public void setCanGather(boolean canGather) {
		this.canGather = canGather;
	}

	public boolean canBuild() {
		return canBuild;
	}

	public void setCanBuild(boolean canBuild) {
		this.canBuild = canBuild;
	}

	public boolean canMove() {
		return canMove;
	}

	public void setCanMove(boolean canMove) {
		this.canMove = canMove;
	}

	/**
	 * Get the number of steps needed to make a single move.
	 * 
	 * @return
	 */
	public int getDurationMove(TerrainType terrainType) {
		Integer duration = durationMove.get(terrainType);
		if (duration == null) {
			throw new IllegalStateException("Object not fully constructed.  When making a UnitTemplate, specify its move durations on each terrain on the map");
		}
		return duration.intValue();
	}
	/**
	 * Set the number of steps needed to make a single move.
	 * @param durationMove
	 */
	public void setDurationMove(int durationMove, TerrainType terrainType) {
		this.durationMove.put(terrainType,durationMove);
	}
	/**
	 * Get the number of steps needed to make a single attack
	 * 
	 * @return
	 */
	public int getDurationAttack() {
		return durationAttack;
	}

	/**
	 * Set the number of steps needed to make a single attack
	 * 
	 * @param durationAttack
	 */
	public void setDurationAttack(int durationAttack) {
		this.durationAttack = durationAttack;
	}

	/**
	 * Get the number of steps needed to make a single deposit.
	 * 
	 * @return
	 */
	public int getDurationDeposit() {
		return durationDeposit;
	}

	/**
	 * Set the number of steps needed to make a single deposit.
	 * 
	 * @param durationDeposit
	 */
	public void setDurationDeposit(int durationDeposit) {
		this.durationDeposit = durationDeposit;
	}

	public void addProductionItem(String templateName) {
		this.producesNames.add(templateName);
	}

	/**
	 * Get a list of IDs of templates that this unit can make. <br>
	 * Changing this list will alter the data of this template.
	 * 
	 * @return A list of IDs for templates that can be produced/built by this
	 *         unit.
	 */
	public List<String> getProduces() {
		return Collections.unmodifiableList(this.producesNames);
	}

	/**
	 * Return whether the unit is capable of producing a specific template
	 * 
	 * @param t
	 * @return
	 */
	public boolean canProduce(@SuppressWarnings("rawtypes") Template t) {
		if(t == null)
			return false;
		for(String s : producesNames)
			if(t.getName().equals(s))
				return true;
		return false;
	}

	public String toString() {
		return name;
	}

	@Override
	public UnitTemplateView getView() {
		if(view == null)
			view = new UnitTemplateView(this);
		return view;
	}

	@Override
	public void deprecateOldView() {
		view = null;
	}

	/**
	 * An immutable representation of a UnitTemplate.
	 */
	public static class UnitTemplateView extends TemplateView implements Serializable {
		private final boolean canGather;
		private final boolean canBuild;
		private final boolean canMove;
		private final boolean canAttack;
		private final int baseHealth;
		private final int basicAttack;
		private final int piercingAttack;
		private final int range;
		private final int armor;
		private final int sightRange;
		private final char character;
		private final int foodProvided;
		private final Map<TerrainType, Integer> durationMove;
		private final int durationAttack;
		private final int durationDeposit;
		private final int width;
		private final int height;
		private final List<String> producesNames;
		private final List<ResourceType> accepts;
		private final Map<ResourceType, Integer> capacity;
		private final Map<ResourceType, Integer> gatherRate;
		private final Map<ResourceType, Integer> gatherDuration;
		private static final long serialVersionUID = 1L;

		/**
		 * Copy all information from a template and save it.
		 * 
		 * @param template
		 */
		public UnitTemplateView(UnitTemplate template) {
			super(template);
			canGather = template.canGather();
			canBuild = template.canBuild();
			canMove = template.canMove();
			canAttack = template.canAttack();
			baseHealth = template.getBaseHealth();
			basicAttack = template.getBasicAttack();
			piercingAttack = template.getPiercingAttack();
			range = template.getRange();
			armor = template.getArmor();
			sightRange = template.getSightRange();
			character = template.getCharacter();
			foodProvided = template.getFoodProvided();
			durationMove = new EnumMap<TerrainType, Integer>(template.durationMove);
			durationAttack = template.getDurationAttack();
			durationDeposit = template.getDurationDeposit();
			List<String> tproducesNames = new ArrayList<String>(template.producesNames.size());
			for(String s : template.producesNames)
				tproducesNames.add(s);
			producesNames = Collections.unmodifiableList(tproducesNames);
			List<ResourceType> taccepts = new ArrayList<ResourceType>(template.accepts.size());
			for(ResourceType type : template.accepts)
				taccepts.add(type);
			accepts = Collections.unmodifiableList(taccepts);
			capacity = new HashMap<ResourceType, Integer>();
			capacity.putAll(template.capacity);
			gatherDuration = new HashMap<ResourceType, Integer>();
			gatherDuration.putAll(template.gatherDuration);
			gatherRate = new HashMap<ResourceType, Integer>();
			gatherRate.putAll(template.gatherRate);
			width = template.getWidth();
			height = template.getHeight();
 		}

		/**
		 * Get the amount of resources that the unit can carry after gathering
		 * from a node of a specific type.
		 * 
		 * @param resourceNodeType
		 * @return
		 */
		public int getCarryingCapacity(ResourceNodeType resourceNodeType) {
			Integer value = capacity.get(resourceNodeType);
			if(value != null)
				return value;
			else
				return 0;
		}

		/**
		 * Get the amount of resources that the unit can gather in a single
		 * action from a node of a specific type.
		 * 
		 * @param resourceNodeType
		 * @return
		 */
		public int getGatherRate(ResourceNodeType resourceNodeType) {
			Integer value = gatherRate.get(resourceNodeType);
			if(value != null)
				return value;
			else
				return 0;
		}

		public boolean canGather() {
			return canGather;
		}

		/**
		 * Get whether units with this template uses the build action to make
		 * things. This is independent of whether the template can actually make
		 * anything.
		 * 
		 * @return true if this template makes units with build actions, false
		 *         if produce actions are used instead.
		 */
		public boolean canBuild() {
			return canBuild;
		}

		/**
		 * Get whether units with this template can move
		 * 
		 * @return true if this template makes units that can move, false if it
		 *         makes units that can't move (like buildings)
		 */
		public boolean canMove() {
			return canMove;
		}

		/**
		 * Get whether units with this template can make attacks.
		 * 
		 * @return true if this template makes units that can attack, false if
		 *         it makes units that cannot attack.
		 */
		public boolean canAttack() {
			return canAttack;
		}

		/**
		 * Get the starting health of units with this template.
		 * 
		 * @return The amount of health/hit points that units made with this
		 *         template start with.
		 */
		public int getBaseHealth() {
			return baseHealth;
		}

		/**
		 * Get the Basic Attack of units with this template. This is one of the
		 * fields used in damage calculations. It represents the portion of the
		 * attack that can be mitigated with armor.
		 * 
		 * @return The Basic Attack of units with this template.
		 */
		public int getBasicAttack() {
			return basicAttack;
		}

		/**
		 * Get the Piercing Attack of units with this template. This is one of
		 * the fields used in damage calculations. It represents the portion of
		 * the attack that is unaffected by armor.
		 * 
		 * @return The Piercing Attack of units with this template.
		 */
		public int getPiercingAttack() {
			return piercingAttack;
		}

		/**
		 * Get the maximum distance at which units with this template are able
		 * to make successful attacks.
		 * 
		 * @return The range of attack for units with this template.
		 */
		public int getRange() {
			return range;
		}

		/**
		 * Get the armor of units with this template. Higher armor causes
		 * greater reduction of the Basic Attack component of damage.
		 * 
		 * @return The amount of armor of units with this template.
		 */
		public int getArmor() {
			return armor;
		}

		/**
		 * Get the sight range of units with this template. A unit "sees" only
		 * units and events that occur at distances not exceeding it's sight
		 * range. In partially observable maps, an agent is only able to observe
		 * events and units that can be seen by units it controls.
		 * 
		 * @return
		 */
		public int getSightRange() {
			return sightRange;
		}

		/**
		 * Get whether units with this template are able to make a specific
		 * other template. This includes making by either building or producing. <br>
		 * Currently just a List.contains()
		 * 
		 * @param templateName
		 *            The name of the template that may be able to BE produced.
		 * @return Whether this template can make the template in the parameter.
		 */
		public boolean canProduce(String templateName) {
			return producesNames.contains(templateName);
		};

		/**
		 * Get a list of template ids that can be produced by this unit. <br>
		 * The list is unmodifiable.
		 * 
		 * @return A list of ids of templates this unit can make.
		 */
		public List<String> getProduces() {
			return producesNames;
		}

		/**
		 * Get the character to be used in visualization.
		 * 
		 * @return The character to be used in visualization.
		 */
		public char getCharacter() {
			return character;
		}

		/**
		 * Get whether a unit with the template can accept a deposit of the
		 * given resource.
		 * 
		 * @param resourceName
		 * @return
		 */
		public boolean canAccept(String resourceName) {
			return accepts.contains(new ResourceType(resourceName));
		}

		/**
		 * Get the amount of food provided by a unit with this template.
		 * 
		 * @return The amount of food/supply that units made with this template
		 *         provide.
		 */
		public int getFoodProvided() {
			return foodProvided;
		}

		/**
		 * Get the duration of a primitive move action. This is the base amount
		 * for how many consecutive steps the primitive action needs to be
		 * repeated before it has an effect. Actual number of steps may depend
		 * on other factors, determined by the Planner and Model being used.
 * @param terrainType The type of terrain to get the move duration for 
		 * @return The base duration of a primitive move action.
		 */
		public int getDurationMove(TerrainType terrainType) {
			Integer duration = durationMove.get(terrainType);
			return duration == null ? NO_DURATION : duration.intValue();
		};

		/**
		 * Get the duration of a primitive attack action. This is the base
		 * amount for how many consecutive steps the primitive action needs to
		 * be repeated before it has an effect. Actual number of steps may
		 * depend on other factors, determined by the Planner and Model being
		 * used.
		 * 
		 * @return The base duration of a primitive attack action.
		 */
		public int getDurationAttack() {
			return durationAttack;
		};

		/**
		 * Get the duration of a primitive deposit action. This is the base
		 * amount for how many consecutive steps the primitive action needs to
		 * be repeated before it has an effect. Actual number of steps may
		 * depend on other factors, determined by the Planner and Model being
		 * used.
		 * 
		 * @return The base duration of a primitive deposit action.
		 */
		public int getDurationDeposit() {
			return durationDeposit;
		};
	
        public int getWidth() {
			return width;
		}
		public int getHeight() {
			return height;
		};
    }
}
