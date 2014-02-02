//This file was generated with xjc
//For the time, see the LAST_GENERATED.properties







package edu.cwru.sepia.model.persistence.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ResourceDropoffLog complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResourceDropoffLog">
 *   &lt;complexContent>
 *     &lt;restriction base="{http:
 *       &lt;sequence>
 *         &lt;element name="depositAmount" type="{http:
 *         &lt;element name="gathererID" type="{http:
 *         &lt;element name="depotID" type="{http:
 *         &lt;element name="controller" type="{http:
 *         &lt;element name="resourceType" type="{}ResourceType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResourceDropoffLog", propOrder = {
    "depositAmount",
    "gathererID",
    "depotID",
    "controller",
    "resourceType"
})
public class XmlResourceDropoffLog {

    protected int depositAmount;
    protected int gathererID;
    protected int depotID;
    protected int controller;
    @XmlElement(required = true)
    protected XmlResourceType resourceType;

    /**
     * Gets the value of the depositAmount property.
     * 
     */
    public int getDepositAmount() {
        return depositAmount;
    }

    /**
     * Sets the value of the depositAmount property.
     * 
     */
    public void setDepositAmount(int value) {
        this.depositAmount = value;
    }

    /**
     * Gets the value of the gathererID property.
     * 
     */
    public int getGathererID() {
        return gathererID;
    }

    /**
     * Sets the value of the gathererID property.
     * 
     */
    public void setGathererID(int value) {
        this.gathererID = value;
    }

    /**
     * Gets the value of the depotID property.
     * 
     */
    public int getDepotID() {
        return depotID;
    }

    /**
     * Sets the value of the depotID property.
     * 
     */
    public void setDepotID(int value) {
        this.depotID = value;
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
     * Gets the value of the resourceType property.
     * 
     * @return
     *     possible object is
     *     {@link XmlResourceType }
     *     
     */
    public XmlResourceType getResourceType() {
        return resourceType;
    }

    /**
     * Sets the value of the resourceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlResourceType }
     *     
     */
    public void setResourceType(XmlResourceType value) {
        this.resourceType = value;
    }

}
