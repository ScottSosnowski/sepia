//This file was generated with xjc
//For the time, see the LAST_GENERATED.properties







package edu.cwru.sepia.model.persistence.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UnitTemplate complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UnitTemplate">
 *   &lt;complexContent>
 *     &lt;extension base="{}Template">
 *       &lt;sequence>
 *         &lt;element name="baseHealth" type="{http:
 *         &lt;element name="baseAttack" type="{http:
 *         &lt;element name="piercingAttack" type="{http:
 *         &lt;element name="range" type="{http:
 *         &lt;element name="armor" type="{http:
 *         &lt;element name="sightRange" type="{http:
 *         &lt;element name="canGather" type="{http:
 *         &lt;element name="canBuild" type="{http:
 *         &lt;element name="canMove" type="{http:
 *         &lt;element name="foodProvided" type="{http:
 *         &lt;element name="character" type="{http:
 *         &lt;element name="durationMove" type="{}TerrainDuration" maxOccurs="unbounded"/>
 *         &lt;element name="durationAttack" type="{http:
 *         &lt;element name="durationDeposit" type="{http:
 *         &lt;element name="width" type="{http:
 *         &lt;element name="height" type="{http:
 *         &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="produces" type="{http:
 *         &lt;/sequence>
 *         &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="accepts" type="{http:
 *         &lt;/sequence>
 *         &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="resourceParameters" type="{}ResourceParameters"/>
 *         &lt;/sequence>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UnitTemplate", propOrder = {
    "baseHealth",
    "baseAttack",
    "piercingAttack",
    "range",
    "armor",
    "sightRange",
    "canGather",
    "canBuild",
    "canMove",
    "foodProvided",
    "character",
    "durationMove",
    "durationAttack",
    "durationDeposit",
    "width",
    "height",
    "produces",
    "accepts",
    "resourceParameters"
})
public class XmlUnitTemplate
    extends XmlTemplate
{

    protected int baseHealth;
    protected int baseAttack;
    protected int piercingAttack;
    protected int range;
    protected int armor;
    protected int sightRange;
    protected boolean canGather;
    protected boolean canBuild;
    protected boolean canMove;
    protected int foodProvided;
    protected short character;
    @XmlElement(required = true)
    protected List<XmlTerrainDuration> durationMove;
    protected int durationAttack;
    protected int durationDeposit;
    protected int width;
    protected int height;
    protected List<String> produces;
    protected List<String> accepts;
    protected List<XmlResourceParameters> resourceParameters;

    /**
     * Gets the value of the baseHealth property.
     * 
     */
    public int getBaseHealth() {
        return baseHealth;
    }

    /**
     * Sets the value of the baseHealth property.
     * 
     */
    public void setBaseHealth(int value) {
        this.baseHealth = value;
    }

    /**
     * Gets the value of the baseAttack property.
     * 
     */
    public int getBaseAttack() {
        return baseAttack;
    }

    /**
     * Sets the value of the baseAttack property.
     * 
     */
    public void setBaseAttack(int value) {
        this.baseAttack = value;
    }

    /**
     * Gets the value of the piercingAttack property.
     * 
     */
    public int getPiercingAttack() {
        return piercingAttack;
    }

    /**
     * Sets the value of the piercingAttack property.
     * 
     */
    public void setPiercingAttack(int value) {
        this.piercingAttack = value;
    }

    /**
     * Gets the value of the range property.
     * 
     */
    public int getRange() {
        return range;
    }

    /**
     * Sets the value of the range property.
     * 
     */
    public void setRange(int value) {
        this.range = value;
    }

    /**
     * Gets the value of the armor property.
     * 
     */
    public int getArmor() {
        return armor;
    }

    /**
     * Sets the value of the armor property.
     * 
     */
    public void setArmor(int value) {
        this.armor = value;
    }

    /**
     * Gets the value of the sightRange property.
     * 
     */
    public int getSightRange() {
        return sightRange;
    }

    /**
     * Sets the value of the sightRange property.
     * 
     */
    public void setSightRange(int value) {
        this.sightRange = value;
    }

    /**
     * Gets the value of the canGather property.
     * 
     */
    public boolean isCanGather() {
        return canGather;
    }

    /**
     * Sets the value of the canGather property.
     * 
     */
    public void setCanGather(boolean value) {
        this.canGather = value;
    }

    /**
     * Gets the value of the canBuild property.
     * 
     */
    public boolean isCanBuild() {
        return canBuild;
    }

    /**
     * Sets the value of the canBuild property.
     * 
     */
    public void setCanBuild(boolean value) {
        this.canBuild = value;
    }

    /**
     * Gets the value of the canMove property.
     * 
     */
    public boolean isCanMove() {
        return canMove;
    }

    /**
     * Sets the value of the canMove property.
     * 
     */
    public void setCanMove(boolean value) {
        this.canMove = value;
    }

    /**
     * Gets the value of the foodProvided property.
     * 
     */
    public int getFoodProvided() {
        return foodProvided;
    }

    /**
     * Sets the value of the foodProvided property.
     * 
     */
    public void setFoodProvided(int value) {
        this.foodProvided = value;
    }

    /**
     * Gets the value of the character property.
     * 
     */
    public short getCharacter() {
        return character;
    }

    /**
     * Sets the value of the character property.
     * 
     */
    public void setCharacter(short value) {
        this.character = value;
    }

    /**
     * Gets the value of the durationMove property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the durationMove property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDurationMove().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XmlTerrainDuration }
     * 
     * 
     */
    public List<XmlTerrainDuration> getDurationMove() {
        if (durationMove == null) {
            durationMove = new ArrayList<XmlTerrainDuration>();
        }
        return this.durationMove;
    }

    /**
     * Gets the value of the durationAttack property.
     * 
     */
    public int getDurationAttack() {
        return durationAttack;
    }

    /**
     * Sets the value of the durationAttack property.
     * 
     */
    public void setDurationAttack(int value) {
        this.durationAttack = value;
    }

    /**
     * Gets the value of the durationDeposit property.
     * 
     */
    public int getDurationDeposit() {
        return durationDeposit;
    }

    /**
     * Sets the value of the durationDeposit property.
     * 
     */
    public void setDurationDeposit(int value) {
        this.durationDeposit = value;
    }

    /**
     * Gets the value of the width property.
     * 
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the value of the width property.
     * 
     */
    public void setWidth(int value) {
        this.width = value;
    }

    /**
     * Gets the value of the height property.
     * 
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the value of the height property.
     * 
     */
    public void setHeight(int value) {
        this.height = value;
    }

    /**
     * Gets the value of the produces property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the produces property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProduces().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getProduces() {
        if (produces == null) {
            produces = new ArrayList<String>();
        }
        return this.produces;
    }

    /**
     * Gets the value of the accepts property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the accepts property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAccepts().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getAccepts() {
        if (accepts == null) {
            accepts = new ArrayList<String>();
        }
        return this.accepts;
    }

    /**
     * Gets the value of the resourceParameters property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resourceParameters property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResourceParameters().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XmlResourceParameters }
     * 
     * 
     */
    public List<XmlResourceParameters> getResourceParameters() {
        if (resourceParameters == null) {
            resourceParameters = new ArrayList<XmlResourceParameters>();
        }
        return this.resourceParameters;
    }

}
