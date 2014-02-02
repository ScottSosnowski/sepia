//This file was generated with xjc
//For the time, see the LAST_GENERATED.properties







package edu.cwru.sepia.model.persistence.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ResourceNodeExhaustionLogList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResourceNodeExhaustionLogList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http:
 *       &lt;sequence>
 *         &lt;element name="roundNumber" type="{http:
 *         &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="ResourceNodeExhaustionLog" type="{}ResourceNodeExhaustionLog"/>
 *         &lt;/sequence>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResourceNodeExhaustionLogList", propOrder = {
    "roundNumber",
    "resourceNodeExhaustionLog"
})
public class XmlResourceNodeExhaustionLogList {

    protected int roundNumber;
    @XmlElement(name = "ResourceNodeExhaustionLog")
    protected List<XmlResourceNodeExhaustionLog> resourceNodeExhaustionLog;

    /**
     * Gets the value of the roundNumber property.
     * 
     */
    public int getRoundNumber() {
        return roundNumber;
    }

    /**
     * Sets the value of the roundNumber property.
     * 
     */
    public void setRoundNumber(int value) {
        this.roundNumber = value;
    }

    /**
     * Gets the value of the resourceNodeExhaustionLog property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resourceNodeExhaustionLog property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResourceNodeExhaustionLog().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XmlResourceNodeExhaustionLog }
     * 
     * 
     */
    public List<XmlResourceNodeExhaustionLog> getResourceNodeExhaustionLog() {
        if (resourceNodeExhaustionLog == null) {
            resourceNodeExhaustionLog = new ArrayList<XmlResourceNodeExhaustionLog>();
        }
        return this.resourceNodeExhaustionLog;
    }

}
