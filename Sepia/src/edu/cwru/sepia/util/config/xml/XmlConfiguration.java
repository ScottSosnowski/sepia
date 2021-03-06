//This file was generated with xjc
//For the time, see the LAST_GENERATED.properties







package edu.cwru.sepia.util.config.xml;

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
 *         &lt;element name="Map" type="{http:
 *         &lt;sequence maxOccurs="unbounded">
 *           &lt;element name="Player" type="{}AgentParameters"/>
 *         &lt;/sequence>
 *         &lt;element name="ModelParameters" type="{}ModelParameters"/>
 *         &lt;element name="Runner" type="{}Runner"/>
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
    "map",
    "player",
    "modelParameters",
    "runner"
})
@XmlRootElement(name = "Configuration")
public class XmlConfiguration {

    @XmlElement(name = "Map", required = true)
    protected String map;
    @XmlElement(name = "Player", required = true)
    protected List<XmlAgentParameters> player;
    @XmlElement(name = "ModelParameters", required = true)
    protected XmlModelParameters modelParameters;
    @XmlElement(name = "Runner", required = true)
    protected XmlRunner runner;

    /**
     * Gets the value of the map property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMap() {
        return map;
    }

    /**
     * Sets the value of the map property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMap(String value) {
        this.map = value;
    }

    /**
     * Gets the value of the player property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the player property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPlayer().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XmlAgentParameters }
     * 
     * 
     */
    public List<XmlAgentParameters> getPlayer() {
        if (player == null) {
            player = new ArrayList<XmlAgentParameters>();
        }
        return this.player;
    }

    /**
     * Gets the value of the modelParameters property.
     * 
     * @return
     *     possible object is
     *     {@link XmlModelParameters }
     *     
     */
    public XmlModelParameters getModelParameters() {
        return modelParameters;
    }

    /**
     * Sets the value of the modelParameters property.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlModelParameters }
     *     
     */
    public void setModelParameters(XmlModelParameters value) {
        this.modelParameters = value;
    }

    /**
     * Gets the value of the runner property.
     * 
     * @return
     *     possible object is
     *     {@link XmlRunner }
     *     
     */
    public XmlRunner getRunner() {
        return runner;
    }

    /**
     * Sets the value of the runner property.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlRunner }
     *     
     */
    public void setRunner(XmlRunner value) {
        this.runner = value;
    }

}
