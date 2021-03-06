//This file was generated with xjc
//For the time, see the LAST_GENERATED.properties







package edu.cwru.sepia.util.config.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ModelParameters complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ModelParameters">
 *   &lt;complexContent>
 *     &lt;restriction base="{http:
 *       &lt;sequence>
 *         &lt;element name="Conquest" type="{http:
 *         &lt;element name="Midas" type="{http:
 *         &lt;element name="ManifestDestiny" type="{http:
 *         &lt;element name="TimeLimit" type="{http:
 *         &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="Requirement" type="{}KeyValuePair"/>
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
@XmlType(name = "ModelParameters", propOrder = {
    "conquest",
    "midas",
    "manifestDestiny",
    "timeLimit",
    "requirement"
})
public class XmlModelParameters {

    @XmlElement(name = "Conquest")
    protected boolean conquest;
    @XmlElement(name = "Midas")
    protected boolean midas;
    @XmlElement(name = "ManifestDestiny")
    protected boolean manifestDestiny;
    @XmlElement(name = "TimeLimit")
    protected int timeLimit;
    @XmlElement(name = "Requirement")
    protected List<XmlKeyValuePair> requirement;

    /**
     * Gets the value of the conquest property.
     * 
     */
    public boolean isConquest() {
        return conquest;
    }

    /**
     * Sets the value of the conquest property.
     * 
     */
    public void setConquest(boolean value) {
        this.conquest = value;
    }

    /**
     * Gets the value of the midas property.
     * 
     */
    public boolean isMidas() {
        return midas;
    }

    /**
     * Sets the value of the midas property.
     * 
     */
    public void setMidas(boolean value) {
        this.midas = value;
    }

    /**
     * Gets the value of the manifestDestiny property.
     * 
     */
    public boolean isManifestDestiny() {
        return manifestDestiny;
    }

    /**
     * Sets the value of the manifestDestiny property.
     * 
     */
    public void setManifestDestiny(boolean value) {
        this.manifestDestiny = value;
    }

    /**
     * Gets the value of the timeLimit property.
     * 
     */
    public int getTimeLimit() {
        return timeLimit;
    }

    /**
     * Sets the value of the timeLimit property.
     * 
     */
    public void setTimeLimit(int value) {
        this.timeLimit = value;
    }

    /**
     * Gets the value of the requirement property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the requirement property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRequirement().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XmlKeyValuePair }
     * 
     * 
     */
    public List<XmlKeyValuePair> getRequirement() {
        if (requirement == null) {
            requirement = new ArrayList<XmlKeyValuePair>();
        }
        return this.requirement;
    }

}
