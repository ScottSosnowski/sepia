//This file was generated with xjc
//For the time, see the LAST_GENERATED.properties







package edu.cwru.sepia.model.persistence.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ActionResultLogger complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ActionResultLogger">
 *   &lt;complexContent>
 *     &lt;restriction base="{http:
 *       &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *         &lt;element name="actionResultList" type="{}ActionResultList"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ActionResultLogger", propOrder = {
    "actionResultList"
})
public class XmlActionResultLogger {

    protected List<XmlActionResultList> actionResultList;

    /**
     * Gets the value of the actionResultList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the actionResultList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getActionResultList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XmlActionResultList }
     * 
     * 
     */
    public List<XmlActionResultList> getActionResultList() {
        if (actionResultList == null) {
            actionResultList = new ArrayList<XmlActionResultList>();
        }
        return this.actionResultList;
    }

}
