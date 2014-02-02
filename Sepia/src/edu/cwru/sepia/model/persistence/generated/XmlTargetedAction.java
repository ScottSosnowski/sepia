//This file was generated with xjc
//For the time, see the LAST_GENERATED.properties







package edu.cwru.sepia.model.persistence.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TargetedAction complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TargetedAction">
 *   &lt;complexContent>
 *     &lt;extension base="{}Action">
 *       &lt;sequence>
 *         &lt;element name="targetId" type="{http:
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TargetedAction", propOrder = {
    "targetId"
})
public class XmlTargetedAction
    extends XmlAction
{

    protected int targetId;

    /**
     * Gets the value of the targetId property.
     * 
     */
    public int getTargetId() {
        return targetId;
    }

    /**
     * Sets the value of the targetId property.
     * 
     */
    public void setTargetId(int value) {
        this.targetId = value;
    }

}
