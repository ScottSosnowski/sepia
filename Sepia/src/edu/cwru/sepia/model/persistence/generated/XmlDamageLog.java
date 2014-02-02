//This file was generated with xjc
//For the time, see the LAST_GENERATED.properties







package edu.cwru.sepia.model.persistence.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DamageLog complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DamageLog">
 *   &lt;complexContent>
 *     &lt;restriction base="{http:
 *       &lt;sequence>
 *         &lt;element name="attackerID" type="{http:
 *         &lt;element name="attackerController" type="{http:
 *         &lt;element name="defenderID" type="{http:
 *         &lt;element name="defenderController" type="{http:
 *         &lt;element name="damage" type="{http:
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DamageLog", propOrder = {
    "attackerID",
    "attackerController",
    "defenderID",
    "defenderController",
    "damage"
})
public class XmlDamageLog {

    protected int attackerID;
    protected int attackerController;
    protected int defenderID;
    protected int defenderController;
    protected int damage;

    /**
     * Gets the value of the attackerID property.
     * 
     */
    public int getAttackerID() {
        return attackerID;
    }

    /**
     * Sets the value of the attackerID property.
     * 
     */
    public void setAttackerID(int value) {
        this.attackerID = value;
    }

    /**
     * Gets the value of the attackerController property.
     * 
     */
    public int getAttackerController() {
        return attackerController;
    }

    /**
     * Sets the value of the attackerController property.
     * 
     */
    public void setAttackerController(int value) {
        this.attackerController = value;
    }

    /**
     * Gets the value of the defenderID property.
     * 
     */
    public int getDefenderID() {
        return defenderID;
    }

    /**
     * Sets the value of the defenderID property.
     * 
     */
    public void setDefenderID(int value) {
        this.defenderID = value;
    }

    /**
     * Gets the value of the defenderController property.
     * 
     */
    public int getDefenderController() {
        return defenderController;
    }

    /**
     * Sets the value of the defenderController property.
     * 
     */
    public void setDefenderController(int value) {
        this.defenderController = value;
    }

    /**
     * Gets the value of the damage property.
     * 
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Sets the value of the damage property.
     * 
     */
    public void setDamage(int value) {
        this.damage = value;
    }

}
