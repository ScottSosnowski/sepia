//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.02.01 at 02:05:00 AM EST 
//


package edu.cwru.sepia.model.persistence.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ResourceParameters complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResourceParameters">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="resourceType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="capacity" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="gatherRate" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="gatherDuration" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResourceParameters", propOrder = {
    "resourceType",
    "capacity",
    "gatherRate",
    "gatherDuration"
})
public class XmlResourceParameters {

    @XmlElement(required = true)
    protected String resourceType;
    protected int capacity;
    protected int gatherRate;
    protected int gatherDuration;

    /**
     * Gets the value of the resourceType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResourceType() {
        return resourceType;
    }

    /**
     * Sets the value of the resourceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResourceType(String value) {
        this.resourceType = value;
    }

    /**
     * Gets the value of the capacity property.
     * 
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Sets the value of the capacity property.
     * 
     */
    public void setCapacity(int value) {
        this.capacity = value;
    }

    /**
     * Gets the value of the gatherRate property.
     * 
     */
    public int getGatherRate() {
        return gatherRate;
    }

    /**
     * Sets the value of the gatherRate property.
     * 
     */
    public void setGatherRate(int value) {
        this.gatherRate = value;
    }

    /**
     * Gets the value of the gatherDuration property.
     * 
     */
    public int getGatherDuration() {
        return gatherDuration;
    }

    /**
     * Sets the value of the gatherDuration property.
     * 
     */
    public void setGatherDuration(int value) {
        this.gatherDuration = value;
    }

}