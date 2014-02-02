//This file was generated with xjc
//For the time, see the LAST_GENERATED.properties







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
