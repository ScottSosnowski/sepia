//This file was generated with xjc
//For the time, see the LAST_GENERATED.properties







package edu.cwru.sepia.model.persistence.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActionResultEntry complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ActionResultEntry">
 *   &lt;complexContent>
 *     &lt;restriction base="{http:
 *       &lt;sequence>
 *         &lt;element name="unitID" type="{http:
 *         &lt;element name="actionResult" type="{}ActionResult"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ActionResultEntry", propOrder = {
    "unitID",
    "actionResult"
})
public class XmlActionResultEntry {

    protected int unitID;
    @XmlElement(required = true)
    protected XmlActionResult actionResult;

    /**
     * Gets the value of the unitID property.
     * 
     */
    public int getUnitID() {
        return unitID;
    }

    /**
     * Sets the value of the unitID property.
     * 
     */
    public void setUnitID(int value) {
        this.unitID = value;
    }

    /**
     * Gets the value of the actionResult property.
     * 
     * @return
     *     possible object is
     *     {@link XmlActionResult }
     *     
     */
    public XmlActionResult getActionResult() {
        return actionResult;
    }

    /**
     * Sets the value of the actionResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlActionResult }
     *     
     */
    public void setActionResult(XmlActionResult value) {
        this.actionResult = value;
    }

}
