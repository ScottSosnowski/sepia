//This file was generated with xjc
//For the time, see the LAST_GENERATED.properties







package edu.cwru.sepia.model.persistence.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UpgradeLog complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UpgradeLog">
 *   &lt;complexContent>
 *     &lt;restriction base="{http:
 *       &lt;sequence>
 *         &lt;element name="UpgradeTemplateID" type="{http:
 *         &lt;element name="ProducingUnitID" type="{http:
 *         &lt;element name="Controller" type="{http:
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UpgradeLog", propOrder = {
    "upgradeTemplateID",
    "producingUnitID",
    "controller"
})
public class XmlUpgradeLog {

    @XmlElement(name = "UpgradeTemplateID")
    protected int upgradeTemplateID;
    @XmlElement(name = "ProducingUnitID")
    protected int producingUnitID;
    @XmlElement(name = "Controller")
    protected int controller;

    /**
     * Gets the value of the upgradeTemplateID property.
     * 
     */
    public int getUpgradeTemplateID() {
        return upgradeTemplateID;
    }

    /**
     * Sets the value of the upgradeTemplateID property.
     * 
     */
    public void setUpgradeTemplateID(int value) {
        this.upgradeTemplateID = value;
    }

    /**
     * Gets the value of the producingUnitID property.
     * 
     */
    public int getProducingUnitID() {
        return producingUnitID;
    }

    /**
     * Sets the value of the producingUnitID property.
     * 
     */
    public void setProducingUnitID(int value) {
        this.producingUnitID = value;
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
