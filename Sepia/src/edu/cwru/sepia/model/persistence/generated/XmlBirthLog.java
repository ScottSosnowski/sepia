//This file was generated with xjc
//For the time, see the LAST_GENERATED.properties







package edu.cwru.sepia.model.persistence.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BirthLog complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BirthLog">
 *   &lt;complexContent>
 *     &lt;restriction base="{http:
 *       &lt;sequence>
 *         &lt;element name="newUnitID" type="{http:
 *         &lt;element name="controller" type="{http:
 *         &lt;element name="parentID" type="{http:
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BirthLog", propOrder = {
    "newUnitID",
    "controller",
    "parentID"
})
public class XmlBirthLog {

    protected int newUnitID;
    protected int controller;
    protected int parentID;

    /**
     * Gets the value of the newUnitID property.
     * 
     */
    public int getNewUnitID() {
        return newUnitID;
    }

    /**
     * Sets the value of the newUnitID property.
     * 
     */
    public void setNewUnitID(int value) {
        this.newUnitID = value;
    }

    /**
     * Gets the value of the controller property.
     * 
     */
    public int getController() {
        return controller;
    }

    /**
     * Sets the value of the controller property.
     * 
     */
    public void setController(int value) {
        this.controller = value;
    }

    /**
     * Gets the value of the parentID property.
     * 
     */
    public int getParentID() {
        return parentID;
    }

    /**
     * Sets the value of the parentID property.
     * 
     */
    public void setParentID(int value) {
        this.parentID = value;
    }

}
