//This file was generated with xjc
//For the time, see the LAST_GENERATED.properties







package edu.cwru.sepia.model.persistence.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActionLogger complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ActionLogger">
 *   &lt;complexContent>
 *     &lt;restriction base="{http:
 *       &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *         &lt;element name="actionList" type="{}ActionList"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ActionLogger", propOrder = {
    "actionList"
})
public class XmlActionLogger {

    protected List<XmlActionList> actionList;

    /**
     * Gets the value of the actionList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the actionList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getActionList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XmlActionList }
     * 
     * 
     */
    public List<XmlActionList> getActionList() {
        if (actionList == null) {
            actionList = new ArrayList<XmlActionList>();
        }
        return this.actionList;
    }

}
