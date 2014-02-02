//This file was generated with xjc
//For the time, see the LAST_GENERATED.properties







package edu.cwru.sepia.model.persistence.generated;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import edu.cwru.sepia.action.ActionType;

public class Adapter3
    extends XmlAdapter<String, ActionType>
{


    public ActionType unmarshal(String value) {
        return (ActionType.valueOf(value));
    }

    public String marshal(ActionType value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }

}
