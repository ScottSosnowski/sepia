//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.02.01 at 02:05:00 AM EST 
//


package edu.cwru.sepia.model.persistence.generated;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import edu.cwru.sepia.action.ActionResultType;

public class Adapter2
    extends XmlAdapter<String, ActionResultType>
{


    public ActionResultType unmarshal(String value) {
        return (ActionResultType.valueOf(value));
    }

    public String marshal(ActionResultType value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }

}
