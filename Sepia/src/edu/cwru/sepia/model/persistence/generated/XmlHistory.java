//This file was generated with xjc
//For the time, see the LAST_GENERATED.properties







package edu.cwru.sepia.model.persistence.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http:
 *       &lt;sequence>
 *         &lt;element name="fogOfWar" type="{http:
 *         &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="playerHistories" type="{}PlayerHistory"/>
 *         &lt;/sequence>
 *         &lt;element name="observerHistory" type="{}PlayerHistory"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "fogOfWar",
    "playerHistories",
    "observerHistory"
})
@XmlRootElement(name = "History")
public class XmlHistory {

    protected boolean fogOfWar;
    protected List<XmlPlayerHistory> playerHistories;
    @XmlElement(required = true)
    protected XmlPlayerHistory observerHistory;

    /**
     * Gets the value of the fogOfWar property.
     * 
     */
    public boolean isFogOfWar() {
        return fogOfWar;
    }

    /**
     * Sets the value of the fogOfWar property.
     * 
     */
    public void setFogOfWar(boolean value) {
        this.fogOfWar = value;
    }

    /**
     * Gets the value of the playerHistories property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the playerHistories property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPlayerHistories().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XmlPlayerHistory }
     * 
     * 
     */
    public List<XmlPlayerHistory> getPlayerHistories() {
        if (playerHistories == null) {
            playerHistories = new ArrayList<XmlPlayerHistory>();
        }
        return this.playerHistories;
    }

    /**
     * Gets the value of the observerHistory property.
     * 
     * @return
     *     possible object is
     *     {@link XmlPlayerHistory }
     *     
     */
    public XmlPlayerHistory getObserverHistory() {
        return observerHistory;
    }

    /**
     * Sets the value of the observerHistory property.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlPlayerHistory }
     *     
     */
    public void setObserverHistory(XmlPlayerHistory value) {
        this.observerHistory = value;
    }

}
