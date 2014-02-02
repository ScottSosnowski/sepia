//This file was generated with xjc
//For the time, see the LAST_GENERATED.properties







package edu.cwru.sepia.model.persistence.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ResourceNode complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResourceNode">
 *   &lt;complexContent>
 *     &lt;restriction base="{http:
 *       &lt;sequence>
 *         &lt;element name="ID" type="{http:
 *         &lt;element name="type" type="{}ResourceNodeType"/>
 *         &lt;element name="xPosition" type="{http:
 *         &lt;element name="yPosition" type="{http:
 *         &lt;element name="initialAmount" type="{http:
 *         &lt;element name="amountRemaining" type="{http:
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResourceNode", propOrder = {
    "id",
    "type",
    "xPosition",
    "yPosition",
    "initialAmount",
    "amountRemaining"
})
public class XmlResourceNode {

    @XmlElement(name = "ID")
    protected int id;
    @XmlElement(required = true)
    protected XmlResourceNodeType type;
    protected int xPosition;
    protected int yPosition;
    protected int initialAmount;
    protected int amountRemaining;

    /**
     * Gets the value of the id property.
     * 
     */
    public int getID() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     */
    public void setID(int value) {
        this.id = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link XmlResourceNodeType }
     *     
     */
    public XmlResourceNodeType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlResourceNodeType }
     *     
     */
    public void setType(XmlResourceNodeType value) {
        this.type = value;
    }

    /**
     * Gets the value of the xPosition property.
     * 
     */
    public int getXPosition() {
        return xPosition;
    }

    /**
     * Sets the value of the xPosition property.
     * 
     */
    public void setXPosition(int value) {
        this.xPosition = value;
    }

    /**
     * Gets the value of the yPosition property.
     * 
     */
    public int getYPosition() {
        return yPosition;
    }

    /**
     * Sets the value of the yPosition property.
     * 
     */
    public void setYPosition(int value) {
        this.yPosition = value;
    }

    /**
     * Gets the value of the initialAmount property.
     * 
     */
    public int getInitialAmount() {
        return initialAmount;
    }

    /**
     * Sets the value of the initialAmount property.
     * 
     */
    public void setInitialAmount(int value) {
        this.initialAmount = value;
    }

    /**
     * Gets the value of the amountRemaining property.
     * 
     */
    public int getAmountRemaining() {
        return amountRemaining;
    }

    /**
     * Sets the value of the amountRemaining property.
     * 
     */
    public void setAmountRemaining(int value) {
        this.amountRemaining = value;
    }

}
