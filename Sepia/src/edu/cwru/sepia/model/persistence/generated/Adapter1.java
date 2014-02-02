//This file was generated with xjc
//For the time, see the LAST_GENERATED.properties







package edu.cwru.sepia.model.persistence.generated;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import edu.cwru.sepia.model.state.Direction;

public class Adapter1
    extends XmlAdapter<String, Direction>
{


    public Direction unmarshal(String value) {
        return (Direction.valueOf(value));
    }

    public String marshal(Direction value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }

}
