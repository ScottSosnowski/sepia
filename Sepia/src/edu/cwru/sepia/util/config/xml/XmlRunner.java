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
 * <p>Java class for Runner complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Runner">
 *   &lt;complexContent>
 *     &lt;restriction base="{http:
 *       &lt;sequence>
 *         &lt;element name="RunnerClass" type="{http:
 *         &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="Parameter" type="{}KeyValuePair"/>
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
@XmlType(name = "Runner", propOrder = {
    "runnerClass",
    "parameter"
})
public class XmlRunner {

    @XmlElement(name = "RunnerClass", required = true)
    protected String runnerClass;
    @XmlElement(name = "Parameter")
    protected List<XmlKeyValuePair> parameter;

    /**
     * Gets the value of the runnerClass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRunnerClass() {
        return runnerClass;
    }

    /**
     * Sets the value of the runnerClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRunnerClass(String value) {
        this.runnerClass = value;
    }

    /**
     * Gets the value of the parameter property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the parameter property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParameter().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XmlKeyValuePair }
     * 
     * 
     */
    public List<XmlKeyValuePair> getParameter() {
        if (parameter == null) {
            parameter = new ArrayList<XmlKeyValuePair>();
        }
        return this.parameter;
    }

}
