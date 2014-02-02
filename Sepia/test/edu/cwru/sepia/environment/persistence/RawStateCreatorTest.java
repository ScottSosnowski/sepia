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

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Random;

import org.junit.Test;

import edu.cwru.sepia.model.state.ResourceNode;
import edu.cwru.sepia.model.state.ResourceNodeType;
import edu.cwru.sepia.model.state.ResourceType;
import edu.cwru.sepia.model.state.State;
import edu.cwru.sepia.model.state.StateCreator;

public class RawStateCreatorTest {

	/**
	 * Uses the xml methods for compares because they are already there. <br/>
	 * TODO: make a state generating method in test that doesn't use xml
	 * 
	 * @throws IOException
	 */
	@Test
	public void testState() throws IOException {
		Random r = new Random();
		State.StateBuilder s = new State.StateBuilder();
		int nplayers = r.nextInt(6);
		int nresource = r.nextInt(12) + 2;
		for(int i = 0; i < nplayers; i++) {
			AdapterTestUtil.createExamplePlayer(r);
		}
		for(int i = 0; i < nresource; i++) {
			// AdapterTestUtil.createExampleResource(, );
		}
		State state = s.build();
		StateCreator stateCreator = state.getStateCreator();

		// Sanity Check
		assertEquals("state doesn't deep equal itself", state, state);

		State stateCopy = stateCreator.createState();
		State stateCopy2 = stateCreator.createState();

		assertEquals("Bad copy made", state, stateCopy);
		assertEquals("Bad copy made", state, stateCopy2);

		ResourceNodeType mine = new ResourceNodeType("GOLD_MINE", new ResourceType("GOLD"));
		// Make sure that the new are not linked to the old one
		state.addResource(new ResourceNode(mine, 4, 4, 343, state.nextTargetId()));
		assertThat("Copy linked to original", state, not(stateCopy));
		assertThat("Copy linked to original", state, not(stateCopy2));

		// Do the same thing to a copy
		stateCopy.addResource(new ResourceNode(mine, 4, 4, 343, stateCopy.nextTargetId()));

		assertEquals("Equals not doing equivalent", stateCopy, state);
		assertThat("Copies linked to each other", stateCopy, not(stateCopy2));
	}
}
