//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.10.20 at 11:59:55 PM EDT 
//


package edu.cwru.sepia.model.persistence.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import edu.cwru.sepia.model.state.ResourceType;


/**
 * <p>Java class for Unit complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Unit">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="currentHealth" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="xPosition" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="yPosition" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="templateID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="cargoType" type="{}ResourceType"/>
 *         &lt;element name="cargoAmount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="progressPrimitive" type="{}Action"/>
 *         &lt;element name="progressAmount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Unit", propOrder = {

})
public class XmlUnit {

    @XmlElement(name = "ID")
    protected int id;
    protected int currentHealth;
    protected int xPosition;
    protected int yPosition;
    protected int templateID;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter3 .class)
    protected ResourceType cargoType;
    protected int cargoAmount;
    @XmlElement(required = true)
    protected XmlAction progressPrimitive;
    protected int progressAmount;

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
     * Gets the value of the currentHealth property.
     * 
     */
    public int getCurrentHealth() {
        return currentHealth;
    }

    /**
     * Sets the value of the currentHealth property.
     * 
     */
    public void setCurrentHealth(int value) {
        this.currentHealth = value;
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
     * Gets the value of the templateID property.
     * 
     */
    public int getTemplateID() {
        return templateID;
    }

    /**
     * Sets the value of the templateID property.
     * 
     */
    public void setTemplateID(int value) {
        this.templateID = value;
    }

    /**
     * Gets the value of the cargoType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public ResourceType getCargoType() {
        return cargoType;
    }

    /**
     * Sets the value of the cargoType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCargoType(ResourceType value) {
        this.cargoType = value;
    }

    /**
     * Gets the value of the cargoAmount property.
     * 
     */
    public int getCargoAmount() {
        return cargoAmount;
    }

    /**
     * Sets the value of the cargoAmount property.
     * 
     */
    public void setCargoAmount(int value) {
        this.cargoAmount = value;
    }

    /**
     * Gets the value of the progressPrimitive property.
     * 
     * @return
     *     possible object is
     *     {@link XmlAction }
     *     
     */
    public XmlAction getProgressPrimitive() {
        return progressPrimitive;
    }

    /**
     * Sets the value of the progressPrimitive property.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlAction }
     *     
     */
    public void setProgressPrimitive(XmlAction value) {
        this.progressPrimitive = value;
    }

    /**
     * Gets the value of the progressAmount property.
     * 
     */
    public int getProgressAmount() {
        return progressAmount;
    }

    /**
     * Sets the value of the progressAmount property.
     * 
     */
    public void setProgressAmount(int value) {
        this.progressAmount = value;
    }

}
