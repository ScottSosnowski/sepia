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
 * <p>Java class for Player complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Player">
 *   &lt;complexContent>
 *     &lt;restriction base="{http:
 *       &lt;sequence>
 *         &lt;element name="ID" type="{http:
 *         &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="unit" type="{}Unit"/>
 *         &lt;/sequence>
 *         &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="upgrade" type="{http:
 *         &lt;/sequence>
 *         &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="template" type="{}Template"/>
 *         &lt;/sequence>
 *         &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="resourceAmount" type="{}ResourceQuantity"/>
 *         &lt;/sequence>
 *         &lt;element name="supply" type="{http:
 *         &lt;element name="supplyCap" type="{http:
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Player", propOrder = {
    "id",
    "unit",
    "upgrade",
    "template",
    "resourceAmount",
    "supply",
    "supplyCap"
})
public class XmlPlayer {

    @XmlElement(name = "ID")
    protected int id;
    protected List<XmlUnit> unit;
    @XmlElement(type = Integer.class)
    protected List<Integer> upgrade;
    protected List<XmlTemplate> template;
    protected List<XmlResourceQuantity> resourceAmount;
    protected int supply;
    protected int supplyCap;

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
     * Gets the value of the unit property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the unit property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUnit().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XmlUnit }
     * 
     * 
     */
    public List<XmlUnit> getUnit() {
        if (unit == null) {
            unit = new ArrayList<XmlUnit>();
        }
        return this.unit;
    }

    /**
     * Gets the value of the upgrade property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the upgrade property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUpgrade().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getUpgrade() {
        if (upgrade == null) {
            upgrade = new ArrayList<Integer>();
        }
        return this.upgrade;
    }

    /**
     * Gets the value of the template property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the template property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTemplate().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XmlTemplate }
     * 
     * 
     */
    public List<XmlTemplate> getTemplate() {
        if (template == null) {
            template = new ArrayList<XmlTemplate>();
        }
        return this.template;
    }

    /**
     * Gets the value of the resourceAmount property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resourceAmount property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResourceAmount().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XmlResourceQuantity }
     * 
     * 
     */
    public List<XmlResourceQuantity> getResourceAmount() {
        if (resourceAmount == null) {
            resourceAmount = new ArrayList<XmlResourceQuantity>();
        }
        return this.resourceAmount;
    }

    /**
     * Gets the value of the supply property.
     * 
     */
    public int getSupply() {
        return supply;
    }

    /**
     * Sets the value of the supply property.
     * 
     */
    public void setSupply(int value) {
        this.supply = value;
    }

    /**
     * Gets the value of the supplyCap property.
     * 
     */
    public int getSupplyCap() {
        return supplyCap;
    }

    /**
     * Sets the value of the supplyCap property.
     * 
     */
    public void setSupplyCap(int value) {
        this.supplyCap = value;
    }

}
