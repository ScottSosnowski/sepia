//This file was generated with xjc
//For the time, see the LAST_GENERATED.properties







package edu.cwru.sepia.model.persistence.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UpgradeLogList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UpgradeLogList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http:
 *       &lt;sequence>
 *         &lt;element name="roundNumber" type="{http:
 *         &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="upgradeLog" type="{}UpgradeLog"/>
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
@XmlType(name = "UpgradeLogList", propOrder = {
    "roundNumber",
    "upgradeLog"
})
public class XmlUpgradeLogList {

    protected int roundNumber;
    protected List<XmlUpgradeLog> upgradeLog;

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
     * Gets the value of the upgradeLog property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the upgradeLog property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUpgradeLog().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XmlUpgradeLog }
     * 
     * 
     */
    public List<XmlUpgradeLog> getUpgradeLog() {
        if (upgradeLog == null) {
            upgradeLog = new ArrayList<XmlUpgradeLog>();
        }
        return this.upgradeLog;
    }

}
