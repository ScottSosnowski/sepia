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
package edu.cwru.sepia.agent;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Collection;

import org.apache.commons.configuration.Configuration;

import edu.cwru.sepia.action.Action;
import edu.cwru.sepia.model.history.History;
import edu.cwru.sepia.model.state.State;
/**
 * The base type for any agent that can interact with the Sepia environment.
 * @author Tim
 *
 */
public abstract class Agent implements Serializable {
	private static final long	serialVersionUID	= 1L;
	public static final int OBSERVER_ID = -999;
	
	protected final int playernum;
	protected Configuration configuration;
	// map: agentID -> flag, if this flag set false, then we will ignore this agent when checking terminal condition. 
	
	
	/**
	 * Create a new Agent to control a player.
	 * @param playernum The player number controlled by this agent.
	 */
	public Agent(int playernum) {
		this.playernum = playernum;
	}
	
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	/**
	 * Get the player number that this agent controls
	 * @return
	 */
	public int getPlayerNumber() {
		return playernum;
	}

	/**
	 * Responds to the first state of the episode with a mapping of unit Ids to unit actions.
	 * @param state
	 * @param history
	 * @return
	 */
	public abstract Collection<Action> initialStep(State.StateView state, History.HistoryView history);

	/**
	 * Responds to any state other than the first or last of the episode with a mapping of unit Ids to unit actions.
	 * @param state
	 * @param history
	 * @return
	 */
	public abstract Collection<Action> middleStep(State.StateView state, History.HistoryView history);
	
	/**
	 * Receives notification about the end of an episode.
	 * @param state
	 * @param history
	 * @return
	 */
	public abstract void terminalStep(State.StateView state, History.HistoryView history);
	
	/**
	 * Save data accumulated by the agent.
	 * @see {@link #loadPlayerData(InputStream)}
	 * @param os An output stream, such as to a file.
	 */
	public abstract void savePlayerData(OutputStream os);
	/**
	 * Load data stored by the agent.
	 * @see {@link #savePlayerData(OutputStream)}
	 * @param is An input stream, such as from a file.
	 */
	public abstract void loadPlayerData(InputStream is);
}
