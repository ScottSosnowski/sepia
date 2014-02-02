//This file was generated with xjc
//For the time, see the LAST_GENERATED.properties







package edu.cwru.sepia.model.persistence.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ResourcePickupLog complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResourcePickupLog">
 *   &lt;complexContent>
 *     &lt;restriction base="{http:
 *       &lt;sequence>
 *         &lt;element name="pickupAmount" type="{http:
 *         &lt;element name="gathererID" type="{http:
 *         &lt;element name="controller" type="{http:
 *         &lt;element name="nodeID" type="{http:
 *         &lt;element name="nodeType" type="{}ResourceNodeType"/>
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
@XmlType(name = "ResourcePickupLog", propOrder = {
    "pickupAmount",
    "gathererID",
    "controller",
    "nodeID",
    "nodeType",
    "resourceType"
})
public class XmlResourcePickupLog {

    protected int pickupAmount;
    protected int gathererID;
    protected int controller;
    protected int nodeID;
    @XmlElement(required = true)
    protected XmlResourceNodeType nodeType;
    @XmlElement(required = true)
    protected XmlResourceType resourceType;

    /**
     * Gets the value of the pickupAmount property.
     * 
     */
    public int getPickupAmount() {
        return pickupAmount;
    }

    /**
     * Sets the value of the pickupAmount property.
     * 
     */
    public void setPickupAmount(int value) {
        this.pickupAmount = value;
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
     * Gets the value of the nodeID property.
     * 
     */
    public int getNodeID() {
        return nodeID;
    }

    /**
     * Sets the value of the nodeID property.
     * 
     */
    public void setNodeID(int value) {
        this.nodeID = value;
    }

    /**
     * Gets the value of the nodeType property.
     * 
     * @return
     *     possible object is
     *     {@link XmlResourceNodeType }
     *     
     */
    public XmlResourceNodeType getNodeType() {
        return nodeType;
    }

    /**
     * Sets the value of the nodeType property.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlResourceNodeType }
     *     
     */
    public void setNodeType(XmlResourceNodeType value) {
        this.nodeType = value;
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
