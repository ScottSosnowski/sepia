//This file was generated with xjc
//For the time, see the LAST_GENERATED.properties







package edu.cwru.sepia.model.persistence.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DeathLog complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DeathLog">
 *   &lt;complexContent>
 *     &lt;restriction base="{http:
 *       &lt;sequence>
 *         &lt;element name="deadUnitID" type="{http:
 *         &lt;element name="controller" type="{http:
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DeathLog", propOrder = {
    "deadUnitID",
    "controller"
})
public class XmlDeathLog {

    protected int deadUnitID;
    protected int controller;

    /**
     * Gets the value of the deadUnitID property.
     * 
     */
    public int getDeadUnitID() {
        return deadUnitID;
    }

    /**
     * Sets the value of the deadUnitID property.
     * 
     */
    public void setDeadUnitID(int value) {
        this.deadUnitID = value;
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

}
