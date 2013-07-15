/**
 *  Strategy Engine for Programming Intelligent Agents (SEPIA)
    Copyright (C) 2012 Case Western Reserve University

    This file is part of SEPIA.

    SEPIA is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    SEPIA is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with SEPIA.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.cwru.sepia.environment.persistence;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Random;

import javax.xml.bind.JAXBException;

import org.json.JSONException;
import org.junit.Test;

import edu.cwru.sepia.model.history.History;
import edu.cwru.sepia.model.persistence.HistoryAdapter;

public class HistoryAdapterTest {
	
	
	@Test
	public void test() throws JSONException, JAXBException, IOException {
		History h = AdapterTestUtil.createExampleHistory(new Random());
		History copy = HistoryAdapter.fromXml(HistoryAdapter.toXml(h));
//		JAXBContext.newInstance(XmlHistory.class).createMarshaller().marshal(HistoryAdapter.toXml(h), new FileWriter(new File("temp.temp")));
		assertTrue("Problem in either copying to or copying from xml",h.deepEquals(copy));
		assertTrue("Problem in either copying to or copying from xml, and also deepequals isn't symmetric",copy.deepEquals(h));
	}
}
