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
 * <p>Java class for EventLogger complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EventLogger">
 *   &lt;complexContent>
 *     &lt;restriction base="{http:
 *       &lt;sequence>
 *         &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="birthLogList" type="{}BirthLogList"/>
 *         &lt;/sequence>
 *         &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="deathLogList" type="{}DeathLogList"/>
 *         &lt;/sequence>
 *         &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="damageLogList" type="{}DamageLogList"/>
 *         &lt;/sequence>
 *         &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="ResourceNodeExhaustionLogList" type="{}ResourceNodeExhaustionLogList"/>
 *         &lt;/sequence>
 *         &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="resourcePickupLogList" type="{}ResourcePickupLogList"/>
 *         &lt;/sequence>
 *         &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="resourceDropoffLogList" type="{}ResourceDropoffLogList"/>
 *         &lt;/sequence>
 *         &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="RevealedResourceNodeLog" type="{}RevealedResourceNodeLog"/>
 *         &lt;/sequence>
 *         &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="upgradeLogList" type="{}UpgradeLogList"/>
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
@XmlType(name = "EventLogger", propOrder = {
    "birthLogList",
    "deathLogList",
    "damageLogList",
    "resourceNodeExhaustionLogList",
    "resourcePickupLogList",
    "resourceDropoffLogList",
    "revealedResourceNodeLog",
    "upgradeLogList"
})
public class XmlEventLogger {

    protected List<XmlBirthLogList> birthLogList;
    protected List<XmlDeathLogList> deathLogList;
    protected List<XmlDamageLogList> damageLogList;
    @XmlElement(name = "ResourceNodeExhaustionLogList")
    protected List<XmlResourceNodeExhaustionLogList> resourceNodeExhaustionLogList;
    protected List<XmlResourcePickupLogList> resourcePickupLogList;
    protected List<XmlResourceDropoffLogList> resourceDropoffLogList;
    @XmlElement(name = "RevealedResourceNodeLog")
    protected List<XmlRevealedResourceNodeLog> revealedResourceNodeLog;
    protected List<XmlUpgradeLogList> upgradeLogList;

    /**
     * Gets the value of the birthLogList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the birthLogList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBirthLogList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XmlBirthLogList }
     * 
     * 
     */
    public List<XmlBirthLogList> getBirthLogList() {
        if (birthLogList == null) {
            birthLogList = new ArrayList<XmlBirthLogList>();
        }
        return this.birthLogList;
    }

    /**
     * Gets the value of the deathLogList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the deathLogList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDeathLogList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XmlDeathLogList }
     * 
     * 
     */
    public List<XmlDeathLogList> getDeathLogList() {
        if (deathLogList == null) {
            deathLogList = new ArrayList<XmlDeathLogList>();
        }
        return this.deathLogList;
    }

    /**
     * Gets the value of the damageLogList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the damageLogList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDamageLogList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XmlDamageLogList }
     * 
     * 
     */
    public List<XmlDamageLogList> getDamageLogList() {
        if (damageLogList == null) {
            damageLogList = new ArrayList<XmlDamageLogList>();
        }
        return this.damageLogList;
    }

    /**
     * Gets the value of the resourceNodeExhaustionLogList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resourceNodeExhaustionLogList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResourceNodeExhaustionLogList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XmlResourceNodeExhaustionLogList }
     * 
     * 
     */
    public List<XmlResourceNodeExhaustionLogList> getResourceNodeExhaustionLogList() {
        if (resourceNodeExhaustionLogList == null) {
            resourceNodeExhaustionLogList = new ArrayList<XmlResourceNodeExhaustionLogList>();
        }
        return this.resourceNodeExhaustionLogList;
    }

    /**
     * Gets the value of the resourcePickupLogList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resourcePickupLogList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResourcePickupLogList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XmlResourcePickupLogList }
     * 
     * 
     */
    public List<XmlResourcePickupLogList> getResourcePickupLogList() {
        if (resourcePickupLogList == null) {
            resourcePickupLogList = new ArrayList<XmlResourcePickupLogList>();
        }
        return this.resourcePickupLogList;
    }

    /**
     * Gets the value of the resourceDropoffLogList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resourceDropoffLogList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResourceDropoffLogList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XmlResourceDropoffLogList }
     * 
     * 
     */
    public List<XmlResourceDropoffLogList> getResourceDropoffLogList() {
        if (resourceDropoffLogList == null) {
            resourceDropoffLogList = new ArrayList<XmlResourceDropoffLogList>();
        }
        return this.resourceDropoffLogList;
    }

    /**
     * Gets the value of the revealedResourceNodeLog property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the revealedResourceNodeLog property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRevealedResourceNodeLog().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XmlRevealedResourceNodeLog }
     * 
     * 
     */
    public List<XmlRevealedResourceNodeLog> getRevealedResourceNodeLog() {
        if (revealedResourceNodeLog == null) {
            revealedResourceNodeLog = new ArrayList<XmlRevealedResourceNodeLog>();
        }
        return this.revealedResourceNodeLog;
    }

    /**
     * Gets the value of the upgradeLogList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the upgradeLogList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUpgradeLogList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XmlUpgradeLogList }
     * 
     * 
     */
    public List<XmlUpgradeLogList> getUpgradeLogList() {
        if (upgradeLogList == null) {
            upgradeLogList = new ArrayList<XmlUpgradeLogList>();
        }
        return this.upgradeLogList;
    }

}
