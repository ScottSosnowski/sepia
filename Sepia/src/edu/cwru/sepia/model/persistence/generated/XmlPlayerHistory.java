//This file was generated with xjc
//For the time, see the LAST_GENERATED.properties







package edu.cwru.sepia.model.persistence.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PlayerHistory complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PlayerHistory">
 *   &lt;complexContent>
 *     &lt;restriction base="{http:
 *       &lt;sequence>
 *         &lt;element name="playerNumber" type="{http:
 *         &lt;element name="eventLogger" type="{}EventLogger"/>
 *         &lt;element name="commandsIssued" type="{}ActionLogger"/>
 *         &lt;element name="commandFeedback" type="{}ActionResultLogger"/>
 *         &lt;element name="primitiveFeedback" type="{}ActionResultLogger"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PlayerHistory", propOrder = {
    "playerNumber",
    "eventLogger",
    "commandsIssued",
    "commandFeedback",
    "primitiveFeedback"
})
public class XmlPlayerHistory {

    protected int playerNumber;
    @XmlElement(required = true)
    protected XmlEventLogger eventLogger;
    @XmlElement(required = true)
    protected XmlActionLogger commandsIssued;
    @XmlElement(required = true)
    protected XmlActionResultLogger commandFeedback;
    @XmlElement(required = true)
    protected XmlActionResultLogger primitiveFeedback;

    /**
     * Gets the value of the playerNumber property.
     * 
     */
    public int getPlayerNumber() {
        return playerNumber;
    }

    /**
     * Sets the value of the playerNumber property.
     * 
     */
    public void setPlayerNumber(int value) {
        this.playerNumber = value;
    }

    /**
     * Gets the value of the eventLogger property.
     * 
     * @return
     *     possible object is
     *     {@link XmlEventLogger }
     *     
     */
    public XmlEventLogger getEventLogger() {
        return eventLogger;
    }

    /**
     * Sets the value of the eventLogger property.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlEventLogger }
     *     
     */
    public void setEventLogger(XmlEventLogger value) {
        this.eventLogger = value;
    }

    /**
     * Gets the value of the commandsIssued property.
     * 
     * @return
     *     possible object is
     *     {@link XmlActionLogger }
     *     
     */
    public XmlActionLogger getCommandsIssued() {
        return commandsIssued;
    }

    /**
     * Sets the value of the commandsIssued property.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlActionLogger }
     *     
     */
    public void setCommandsIssued(XmlActionLogger value) {
        this.commandsIssued = value;
    }

    /**
     * Gets the value of the commandFeedback property.
     * 
     * @return
     *     possible object is
     *     {@link XmlActionResultLogger }
     *     
     */
    public XmlActionResultLogger getCommandFeedback() {
        return commandFeedback;
    }

    /**
     * Sets the value of the commandFeedback property.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlActionResultLogger }
     *     
     */
    public void setCommandFeedback(XmlActionResultLogger value) {
        this.commandFeedback = value;
    }

    /**
     * Gets the value of the primitiveFeedback property.
     * 
     * @return
     *     possible object is
     *     {@link XmlActionResultLogger }
     *     
     */
    public XmlActionResultLogger getPrimitiveFeedback() {
        return primitiveFeedback;
    }

    /**
     * Sets the value of the primitiveFeedback property.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlActionResultLogger }
     *     
     */
    public void setPrimitiveFeedback(XmlActionResultLogger value) {
        this.primitiveFeedback = value;
    }

}
