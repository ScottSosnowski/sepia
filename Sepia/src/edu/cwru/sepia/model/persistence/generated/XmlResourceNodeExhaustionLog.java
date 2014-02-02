//This file was generated with xjc
//For the time, see the LAST_GENERATED.properties







package edu.cwru.sepia.model.persistence.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ResourceNodeExhaustionLog complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResourceNodeExhaustionLog">
 *   &lt;complexContent>
 *     &lt;restriction base="{http:
 *       &lt;sequence>
 *         &lt;element name="exhaustedNodeID" type="{http:
 *         &lt;element name="exhaustedNodeType" type="{}ResourceNodeType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResourceNodeExhaustionLog", propOrder = {
    "exhaustedNodeID",
    "exhaustedNodeType"
})
public class XmlResourceNodeExhaustionLog {

    protected int exhaustedNodeID;
    @XmlElement(required = true)
    protected XmlResourceNodeType exhaustedNodeType;

    /**
     * Gets the value of the exhaustedNodeID property.
     * 
     */
    public int getExhaustedNodeID() {
        return exhaustedNodeID;
    }

    /**
     * Sets the value of the exhaustedNodeID property.
     * 
     */
    public void setExhaustedNodeID(int value) {
        this.exhaustedNodeID = value;
    }

    /**
     * Gets the value of the exhaustedNodeType property.
     * 
     * @return
     *     possible object is
     *     {@link XmlResourceNodeType }
     *     
     */
    public XmlResourceNodeType getExhaustedNodeType() {
        return exhaustedNodeType;
    }

    /**
     * Sets the value of the exhaustedNodeType property.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlResourceNodeType }
     *     
     */
    public void setExhaustedNodeType(XmlResourceNodeType value) {
        this.exhaustedNodeType = value;
    }

}
